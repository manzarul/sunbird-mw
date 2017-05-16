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
    SimpleDateFormat sdf = ProjectUtil.cassandraFormat;

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
                        preOperation(map);
                        map.put(JsonKey.USER_ID, userId);
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

    private void preOperation(Map<String, Object> req) throws ParseException {

        ActorUtility.DbInfo dbInfo = ActorUtility.dbInfoMap.get(LearnerStateOperation.ADD_CONTENT.getValue());
        generateandAppendPrimaryKey(req);
        Response response = cassandraOperation.getRecordById(dbInfo.getKeySpace(), dbInfo.getTableName(), (String) req.get(JsonKey.ID));

        List<Map<String, Object>> resultList = (List<Map<String, Object>>) response.getResult().get("response");
        if (!(resultList.isEmpty())) {
            Map<String, Object> result = resultList.get(0);

            int currentStatus = Integer.parseInt((String) result.get(JsonKey.COURSE_STATUS));
            int requestedStatus = Integer.parseInt((String) req.get(JsonKey.COURSE_STATUS));

            Date lastUpdatedTime = parseDate(result.get(JsonKey.LAST_UPDATED_TIME));
            Date requestedUpdatedTime = parseDate(req.get(JsonKey.LAST_UPDATED_TIME));
            Date accessTime = parseDate(result.get(JsonKey.LAST_ACCESS_TIME));
            Date requestAccessTime = parseDate(req.get(JsonKey.LAST_ACCESS_TIME));

            Date completedDate = parseDate(result.get(JsonKey.LAST_COMPLETED_TIME));
            Date requestCompletedTime = parseDate(req.get(JsonKey.LAST_COMPLETED_TIME));

            int completedCount = Integer.parseInt((String) result.get(JsonKey.COMPLETED_COUNT));
            int viewCount = Integer.parseInt((String) result.get(JsonKey.VIEW_COUNT));


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
        }


    }

    private Date parseDate(Object obj) throws ParseException {
        if(null == obj){
            return null;
        }
        return sdf.parse((String) obj);
    }

    private String compareTime(Date currentValue, Date requestedValue) {

        if(currentValue == null){
            return requestedValue.toString();
        }

        return (requestedValue.after(currentValue) ? requestedValue.toString() : currentValue.toString());
    }

    private void generateandAppendPrimaryKey(Map<String, Object> req) {
        String userId = (String) req.get(JsonKey.USER_ID);
        String contentId = (String) req.get(JsonKey.CONTENT_ID);
        String id = contentId + "##" + userId;
        req.put(JsonKey.ID, id);

    }

}
