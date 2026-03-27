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

CREATE TABLE `logs` (
  `uuid` char(36) COLLATE utf8_unicode_ci NOT NULL,
  `title` char(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `content` text COLLATE utf8_unicode_ci,
  `createTimestamp` date DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

LOCK TABLES `logs` WRITE;
INSERT INTO `logs` VALUES ('ac299eb1-599e-4599-b22b-95e889448793','Two','Another content is 2',NULL),('d2bbd408-2836-4c96-92b2-0d44210e8502','One','One content',NULL);
UNLOCK TABLES;

CREATE TABLE `users` (
    `id` char(36) COLLATE utf8_unicode_ci NOT NULL,
    `email` char(100) COLLATE utf8_unicode_ci NOT NULL,
    `full_name` char(100) COLLATE utf8_unicode_ci NOT NULL,
    `password` char(255) NOT NULL,
    PRIMARY KEY (`id`)
);

LOCK TABLES `users` WRITE;
INSERT INTO `users` VALUES ('a3f1c2d4-1234-4abc-9f12-abcdef123456', 'john@example.com', 'John Doe', 'password123'),('b7e2d9f8-5678-4def-8a34-bcdefa654321', 'jane@example.com', 'Jane Smith', 'mypassword');
UNLOCK TABLES;


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

End of Instructions
