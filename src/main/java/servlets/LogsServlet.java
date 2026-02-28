package servlets;

import models.Log;
import services.LogService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/LogsServlet")
public class LogsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Service to handle database operations
    private LogService logService = new LogService();

    /**
     * Handles GET requests:
     * - Displays form (empty or prepopulated if editing)
     * - Shows all logs from the database
     * - Handles delete actions via query parameters
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        String message = request.getParameter("message");
        String action = request.getParameter("action");
        String id = request.getParameter("id");

        writer.println("<html><head><title>Loggy App</title></head><body>");

        // Show message if present, errors, success edits
        if (message != null) {
            writer.println("<p><b>" + message + "</b></p>");
        }

        // deleting logs
        if ("deleteLog".equals(action) && id != null) {
            try {
                logService.deleteLog(id);
                response.sendRedirect("LogsServlet?message=Log deleted successfully!");
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("LogsServlet?message=Error deleting log.");
                return;
            }
        }

        // editings logs aka grabbing the id and sending it to the action method
        Log editLog = null;
        if ("editLog".equals(action) && id != null) {
            try {
                List<Log> logs = logService.getAllLogs();
                for (Log log : logs) {
                    if (log.getId().equals(id)) {
                        editLog = log;
                        break;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //create new log html, if editing show the file to be edited
        writer.println("<h2>" + (editLog != null ? "Edit Log" : "Create New Log") + "</h2>");
        writer.println("<form method='post' action='LogsServlet'>");

        // Title input
        writer.println("Title: <input type='text' name='title' maxlength='70' required value='" +
                (editLog != null ? editLog.getTitle() : "") + "'/><br/><br/>");

        // Content input
        writer.println("Content:<br/>");
        writer.println("<textarea name='content' maxlength='180' required>" +
                (editLog != null ? editLog.getContent() : "") + "</textarea><br/><br><br><br>");

        // Hidden field for ID when editing
        if (editLog != null) {
            writer.println("<input type='hidden' name='id' value='" + editLog.getId() + "'/>");
        }

        // Submit button
        writer.println("<input type='submit' value='" + (editLog != null ? "Update" : "Submit") + "'/>");
        writer.println("</form>");

        // ----- DISPLAY ALL LOGS -----
        try {
            List<Log> logs = logService.getAllLogs();
            writer.println("<h3>All Logs:</h3>");
            for (Log log : logs) {
                writer.println("<hr>");
                writer.println("<b>" + log.getTitle() + "</b><br>");
                writer.println("<small>" + log.getTimestamp() + "</small><br><br>");
                writer.println("<p>" + log.getContent() + "</p>");
                writer.println("<a href='LogsServlet?action=editLog&id=" + log.getId() + "'>Edit</a> | ");
                writer.println("<a href='LogsServlet?action=deleteLog&id=" + log.getId() + "'>Delete</a>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            writer.println("<p>Error retrieving logs from database.</p>");
        }

        writer.println("</body></html>");
    }

    /**
handles post requests
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String id = request.getParameter("id"); // hidden field in form for edits

        // Validate inputs
        if (title == null || content == null || title.isEmpty() || content.isEmpty()) {
            response.sendRedirect("LogsServlet?message=Error: Title and Content are required.");
            return;
        }

        try {
            // update existing log if exists
            if (id != null && !id.isEmpty()) {
                Log logToUpdate = new Log(); // no-args constructor
                logToUpdate.setId(id);
                logToUpdate.setTitle(title);
                logToUpdate.setContent(content);
                logService.updateLog(logToUpdate);
                response.sendRedirect("LogsServlet?message=Log updated successfully!");
            }
            // creates new log if there isn't one already found by that ID
            else {
                Log newLog = new Log(title, content);
                logService.createLog(newLog);
                response.sendRedirect("LogsServlet?message=Log created successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("LogsServlet?message=Database error occurred.");
        }
    }
}
