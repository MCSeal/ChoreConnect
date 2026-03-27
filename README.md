ChoreConnect Deployment Instructions

1. Prerequisites
- Java JDK 8+
- Apache Tomcat 9
- MySQL Server
- Eclipse IDE (Enterprise/Web)
- MySQL Connector/J

2. Project Setup in Eclipse
- File → Import → Existing Projects into Workspace
- Select ChoreConnectMVP

3. Project Structure
src/main/java/ → Servlets, Models, Services
src/main/webapp/ → Public files
src/main/webapp/WEB-INF/ → JSP

4. Database Setup
CREATE DATABASE choreconnect;
USE choreconnect;

CREATE DATABASE choreconnectmvp;
USE choreconnectmvp;

CREATE TABLE users (
id VARCHAR(36) PRIMARY KEY,
email VARCHAR(255) NOT NULL UNIQUE,
full_name VARCHAR(255) NOT NULL,
password VARCHAR(255) NOT NULL
);

CREATE TABLE chores (
id VARCHAR(36) PRIMARY KEY,
title VARCHAR(255) NOT NULL,
description TEXT,
created_by VARCHAR(36) NOT NULL,
accepted_by VARCHAR(36),
status VARCHAR(20) NOT NULL,
is_public BOOLEAN NOT NULL,
created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
CONSTRAINT fk_chores_created_by
FOREIGN KEY (created_by) REFERENCES users(id),
latitude DOUBLE NOT NULL,
longitude DOUBLE NOT NULL,
CONSTRAINT fk_chores_accepted_by
FOREIGN KEY (accepted_by) REFERENCES users(id)
);
 
CREATE TABLE reviews (
id VARCHAR(36) PRIMARY KEY,
chore_id VARCHAR(36) NOT NULL,
reviewer_id VARCHAR(36) NOT NULL,
reviewee_id VARCHAR(36) NOT NULL,
rating INT NOT NULL,
comment TEXT,
created_at TIMESTAMP NOT NULL,
CONSTRAINT fk_reviews_chore
FOREIGN KEY (chore_id) REFERENCES chores(id),
CONSTRAINT fk_reviews_reviewer
FOREIGN KEY (reviewer_id) REFERENCES users(id),
CONSTRAINT fk_reviews_reviewee
FOREIGN KEY (reviewee_id) REFERENCES users(id)
);


5. Database Connection
Update DBConnection.java with your credentials.

6. Add MySQL JDBC Driver
- Add .jar to Build Path OR place in WEB-INF/lib/

7. Configure Tomcat
- Create Apache Tomcat v9 server in Eclipse

8. Deploy Project
- Add ChoreConnectMVP to server

9. Deployment Assembly
Ensure:
src/main/webapp → /

10. Run Application
http://localhost:8080/ChoreConnectMVP/

11. Testing
- Register user
- Login user

12. Common Issues
- 404: Check JSP location and servlet routing
- DB errors: Check credentials
- Driver errors: Add JDBC jar
- Changes not showing: Clean & restart

13. WAR Deployment (Optional)
- Export WAR
- Copy to Tomcat webapps/
- Start Tomcat
