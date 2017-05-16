/**
 *
 */
package org.sunbird.learner.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import org.sunbird.bean.LearnerStateOperation;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.cassandraimpl.CassandraOperationImpl;
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.models.util.LogHelper;
import org.sunbird.common.models.util.ProjectUtil;
import org.sunbird.common.request.Request;
import org.sunbird.common.responsecode.ResponseCode;
import org.sunbird.learner.util.ActorUtility;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This actor will handle learner's state update operation .
 *
 * @author Manzarul
 */
public class LearnerStateUpdateActor extends UntypedAbstractActor {

    private CassandraOperation cassandraOperation = new CassandraOperationImpl();
    private LogHelper logger = LogHelper.getInstance(LearnerStateUpdateActor.class.getName());
    SimpleDateFormat sdf = ProjectUtil.format;
    SimpleDateFormat cassandraSdf = ProjectUtil.cassandraFormat;

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Request) {
            logger.debug("LearnerStateUpdateActor onReceive called");
            Request actorMessage = (Request) message;
            Response response = new Response();
            if (actorMessage.getOperation().equalsIgnoreCase(LearnerStateOperation.ADD_CONTENT.getValue())) {
                ActorUtility.DbInfo dbInfo = ActorUtility.dbInfoMap.get(LearnerStateOperation.ADD_CONTENT.getValue());
                // get user id
                String userId = (String) actorMessage.getRequest().get(JsonKey.USER_ID);
                List<Map<String, Object>> contentList = (List<Map<String, Object>>) actorMessage.getRequest().get(JsonKey.CONTENTS);

                if (!(contentList.isEmpty())) {
                    for (Map<String, Object> map : contentList) {
                        preOperation(map, userId);
                        map.put(JsonKey.USER_ID, userId);
                        generateandAppendPrimaryKey(map, userId);
                        try {
                            Response result = cassandraOperation.insertRecord(dbInfo.getKeySpace(), dbInfo.getTableName(), map);
                            response.getResult().put((String) map.get(JsonKey.CONTENT_ID), "SUCCESS");
                        } catch (Exception ex) {
                            response.getResult().put((String) map.get(JsonKey.CONTENT_ID), ex.getMessage());
                        }
                    }
                }
                sender().tell(response, self());
            } else {
                logger.info("UNSUPPORTED OPERATION");
                ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidOperationName.getErrorCode(), ResponseCode.invalidOperationName.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
                sender().tell(exception, ActorRef.noSender());
            }
        } else {
            logger.info("UNSUPPORTED MESSAGE");
            ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode(), ResponseCode.invalidRequestData.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
            sender().tell(exception, ActorRef.noSender());
        }
    }

    private void preOperation(Map<String, Object> req, String userId) throws ParseException {

        ActorUtility.DbInfo dbInfo = ActorUtility.dbInfoMap.get(LearnerStateOperation.ADD_CONTENT.getValue());
        generateandAppendPrimaryKey(req, userId);
        Response response = cassandraOperation.getRecordById(dbInfo.getKeySpace(), dbInfo.getTableName(), (String) req.get(JsonKey.ID));

        List<Map<String, Object>> resultList = (List<Map<String, Object>>) response.getResult().get("response");
        if (!(resultList.isEmpty())) {
            Map<String, Object> result = resultList.get(0);

            int currentStatus = Integer.parseInt((String) result.get(JsonKey.COURSE_STATUS));
            int requestedStatus = Integer.parseInt((String) req.get(JsonKey.COURSE_STATUS));

            Date lastUpdatedTime = parseDate(result.get(JsonKey.LAST_UPDATED_TIME), cassandraSdf);
            Date requestedUpdatedTime = parseDate(req.get(JsonKey.LAST_UPDATED_TIME), sdf);
            Date accessTime = parseDate(result.get(JsonKey.LAST_ACCESS_TIME), cassandraSdf);
            Date requestAccessTime = parseDate(req.get(JsonKey.LAST_ACCESS_TIME), sdf);

            Date completedDate = parseDate(result.get(JsonKey.LAST_COMPLETED_TIME), cassandraSdf);
            Date requestCompletedTime = parseDate(req.get(JsonKey.LAST_COMPLETED_TIME), sdf);

            int completedCount;
            if (null != result.get(JsonKey.COMPLETED_COUNT)) {
                completedCount = Integer.parseInt((String) result.get(JsonKey.COMPLETED_COUNT));
            } else {
                completedCount = 0;
            }
            int viewCount;
            if (null != result.get(JsonKey.VIEW_COUNT)) {
                viewCount = Integer.parseInt((String) result.get(JsonKey.VIEW_COUNT));
            } else {
                viewCount = 0;
            }


            if (requestedStatus >= currentStatus) {
                req.put(JsonKey.COURSE_STATUS, String.valueOf(requestedStatus));
                if (requestedStatus == 2) {
                    req.put(JsonKey.COMPLETED_COUNT, String.valueOf(completedCount + 1));
                    req.put(JsonKey.LAST_COMPLETED_TIME, compareTime(completedDate, requestCompletedTime));
                }
                req.put(JsonKey.VIEW_COUNT, String.valueOf(viewCount + 1));
                req.put(JsonKey.LAST_ACCESS_TIME, compareTime(accessTime, requestAccessTime));
                req.put(JsonKey.LAST_UPDATED_TIME, compareTime(lastUpdatedTime, requestedUpdatedTime));

            } else {
                req.put(JsonKey.COURSE_STATUS, String.valueOf(currentStatus));
                req.put(JsonKey.VIEW_COUNT, String.valueOf(viewCount + 1));
                req.put(JsonKey.LAST_ACCESS_TIME, compareTime(accessTime, requestAccessTime));
                req.put(JsonKey.LAST_UPDATED_TIME, compareTime(lastUpdatedTime, requestedUpdatedTime));
            }

        } else {
            // IT IS NEW CONTENT SIMPLY ADD IT
            req.put(JsonKey.VIEW_COUNT, String.valueOf(0));
            req.put(JsonKey.COMPLETED_COUNT, String.valueOf(0));
            Date requestedUpdatedTime = parseDate(req.get(JsonKey.LAST_UPDATED_TIME), sdf);
            Date requestAccessTime = parseDate(req.get(JsonKey.LAST_ACCESS_TIME), sdf);

            if (requestedUpdatedTime != null) {
                req.put(JsonKey.LAST_UPDATED_TIME, new Timestamp(requestedUpdatedTime.getTime()));
            } else {
                req.put(JsonKey.LAST_UPDATED_TIME, new Timestamp(System.currentTimeMillis()));
            }
            if (requestAccessTime != null) {
                req.put(JsonKey.LAST_ACCESS_TIME, new Timestamp(requestAccessTime.getTime()));
            } else {
                req.put(JsonKey.LAST_ACCESS_TIME, new Timestamp(System.currentTimeMillis()));
            }
        }

    }

    private Date parseDate(Object obj, SimpleDateFormat formatter) throws ParseException {
        if (null == obj) {
            return null;
        }
        return formatter.parse((String) obj);
    }

    private Timestamp compareTime(Date currentValue, Date requestedValue) {

        if (currentValue == null) {
            return new Timestamp(requestedValue.getTime());
        }
        return (requestedValue.after(currentValue) ? new Timestamp(requestedValue.getTime()) : new Timestamp(currentValue.getTime()));
    }

    private void generateandAppendPrimaryKey(Map<String, Object> req, String userId) {
        String contentId = (String) req.get(JsonKey.CONTENT_ID);
        String id = contentId + "##" + userId;
        req.put(JsonKey.ID, id);
    }

}
