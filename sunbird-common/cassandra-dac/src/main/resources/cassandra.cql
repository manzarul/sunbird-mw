CREATE KEYSPACE IF NOT EXISTS cassandraKeySpace WITH replication = {'class':'SimpleStrategy','replication_factor':1};

USE cassandraKeySpace

CREATE TABLE IF NOT EXISTS cassandraKeySpace.course(id text, courseId text, courseName text,userId text,enrolledDate text,description text,tocUrl text,courseProgressStatus text,active boolean,delta text,PRIMARY KEY (id,courseId,userId));

CREATE INDEX user_Id ON cassandraKeySpace.course (userId);
CREATE INDEX inx_course_Id ON cassandraKeySpace.course (courseId);


CREATE TABLE IF NOT EXISTS cassandraKeySpace.content(id text, contentId text, courseId text, userId text,deviceId text,viewPosition text,viewCount text,lastAccessTime text,completedCount text,progressstatus text,lastUpdatedTime text,PRIMARY KEY (id,contentId,courseId,userId));

CREATE INDEX course_Id ON cassandraKeySpace.content (courseId);
CREATE INDEX inx_user_Id ON cassandraKeySpace.content (userId);
CREATE INDEX inx_content_Id ON cassandraKeySpace.content (contentId);


