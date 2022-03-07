CREATE INDEX USR_userId_index
ON USR (userId);

CREATE INDEX USR_password_index
ON USR (password);

CREATE INDEX USR_email_index
ON USR (email);

CREATE INDEX USR_name_index
ON USR (name);

CREATE INDEX USR_dateOfBirth_index
ON USR (dateOfBirth);

CREATE INDEX WORK_EXPR_userId_index
ON WORK_EXPR (userId);

CREATE INDEX WORK_EXPR_company_index
ON WORK_EXPR (company);

CREATE INDEX WORK_EXPR_role_index
ON WORK_EXPR (role);

CREATE INDEX WORK_EXPR_location_index
ON WORK_EXPR (location);

CREATE INDEX WORK_EXPR_startDate_index
ON WORK_EXPR (startDate);

CREATE INDEX WORK_EXPR_endDate_index
ON WORK_EXPR (endDate);

CREATE INDEX EDUCATIONAL_DETAILS_userId_index
ON EDUCATIONAL_DETAILS (userId);

CREATE INDEX EDUCATIONAL_DETAILS_instituitionName_index
ON EDUCATIONAL_DETAILS (instituitionName);

CREATE INDEX EDUCATIONAL_DETAILS_major_index
ON EDUCATIONAL_DETAILS (major);

CREATE INDEX EDUCATIONAL_DETAILS_degree_index
ON EDUCATIONAL_DETAILS (degree);

CREATE INDEX EDUCATIONAL_DETAILS_startdate_index
ON EDUCATIONAL_DETAILS (startdate);

CREATE INDEX EDUCATIONAL_DETAILS_enddate_index
ON EDUCATIONAL_DETAILS (enddate);

CREATE INDEX MESSAGE_msgId_index
ON MESSAGE (msgId);

CREATE INDEX MESSAGE_senderId_index
ON MESSAGE (senderId);

CREATE INDEX MESSAGE_receiverId_index
ON MESSAGE (receiverId);

CREATE INDEX MESSAGE_contents_index
ON MESSAGE (contents);

CREATE INDEX MESSAGE_sendTime_index
ON MESSAGE (sendTime);

CREATE INDEX MESSAGE_deleteStatus_index
ON MESSAGE (deleteStatus);

CREATE INDEX MESSAGE_status_index
ON MESSAGE (status);

CREATE INDEX CONNECTION_USR_userId_index
ON CONNECTION_USR (userId);

CREATE INDEX CONNECTION_USR_connectionId_index
ON CONNECTION_USR (connectionId);

CREATE INDEX CONNECTION_USR_status_index
ON CONNECTION_USR (status);
