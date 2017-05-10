CREATE KEYSPACE IF NOT EXISTS cassandraKeySpace WITH replication = {'class':'SimpleStrategy','replication_factor':1};

USE cassandraKeySpace

CREATE TABLE IF NOT EXISTS cassandraKeySpace.course(courseId text, courseName text,userId text,enrolledDate text,description text,tocUrl text,courseProgressStatus text,active boolean,deltaMap MAP<text,text>,PRIMARY KEY (courseId,userId,enrolledDate));

CREATE INDEX user_Id ON cassandraKeySpace.course (userId);


CREATE TABLE IF NOT EXISTS cassandraKeySpace.content(contentId text, courseId text, userId text,deviceId text,viewPosition text,viewCount text,lastAccessTime text,completedCount text,progressstatus text,lastUpdatedTime text,PRIMARY KEY (contentId,courseId,userId));

CREATE INDEX course_Id ON cassandraKeySpace.content (courseId);

CREATE INDEX inx_user_Id ON cassandraKeySpace.content (userId);