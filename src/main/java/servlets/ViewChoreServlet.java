package servlets;

import models.Chore;
import services.ChoreService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/viewChore")
public class ViewChoreServlet extends HttpServlet {
    private final ChoreService choreService = new ChoreService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        HttpSession session = req.getSession(false);
        String userId = session != null ? (String) session.getAttribute("userId") : null;

        String id = req.getParameter("id");
        String message = req.getParameter("message");

        Chore ch = null;
        try {
            ch = choreService.getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        out.println("<!doctype html><html lang='en'><head>");
        out.println("<meta charset='utf-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
        out.println("<link rel='stylesheet' href='styles.css'>");
        out.println("<title>View Chore · ChoreConnect</title>");
        out.println("</head><body><div class='container'>");

        // Header
        out.println("<header class='header'>");
        out.println("<a class='brand' href='publicChoreList'>ChoreConnect</a>");
        out.println("<form method='post' action='" + req.getContextPath() + "/signout'>");
        out.println("<button class='btn' type='submit'>Sign out</button>");
        out.println("</form>");
        out.println("</header>");

        if (message != null) {
            out.println("<div class='notice success'>" + message + "</div>");
        }

        if (ch == null) {
            out.println("<div class='card notice error'>Chore not found!</div>");
            out.println("<p><a class='btn btn-secondary' href='choreList'>Back</a></p>");
        } else {

            out.println("<div class='card'>");
            out.println("<h2>" + ch.getTitle() + "</h2>");
            out.println("<p>" + (ch.getDescription() == null ? "" : ch.getDescription()) + "</p>");
            out.println("<p>Status: <span class='badge " + ch.getStatus().toLowerCase() + "'>" + ch.getStatus() + "</span></p>");
            out.println("<p>Public: " + (ch.isPublic() ? "Yes" : "No") + "</p>");
            out.println("<p>Created by: " + ch.getCreatedBy() + "</p>");

            // Creator can edit
            if (userId != null && userId.equals(ch.getCreatedBy())) {
                out.println("<a class='btn btn-primary' href='chore/edit?id=" + ch.getId() + "'>Edit Chore</a>");
            }

            // Accept button
            else if (userId != null && !userId.equals(ch.getCreatedBy()) && "OPEN".equals(ch.getStatus())) {
                out.println("<form method='post' action='chore/accept' style='display:inline'>");
                out.println("<input type='hidden' name='choreId' value='" + ch.getId() + "'/>");
                out.println("<button class='btn btn-primary' type='submit'>Accept Chore</button>");
                out.println("</form>");
            }

            // Mark done (only accepted user)
            if (userId != null &&
                userId.equals(ch.getAcceptedBy()) &&
                "ACCEPTED".equals(ch.getStatus())) {

                out.println("<form method='post' action='viewChore' style='display:inline;margin-left:10px'>");
                out.println("<input type='hidden' name='id' value='" + ch.getId() + "'/>");
                out.println("<input type='hidden' name='action' value='markDone'/>");
                out.println("<button class='btn btn-success' type='submit'>Mark Done</button>");
                out.println("</form>");
            }

            out.println("<p style='margin-top:1rem;'><a class='btn btn-secondary' href='choreList'>Back to My Chores</a></p>");
            out.println("</div>");
        }

        out.println("</div></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        HttpSession session = req.getSession(false);
        String userId = session != null ? (String) session.getAttribute("userId") : null;

        String action = req.getParameter("action");
        String id = req.getParameter("id");

        if ("markDone".equals(action) && userId != null) {
            try {
                boolean success = choreService.markDone(id, userId);

                if (success) {
                    resp.sendRedirect(req.getContextPath() + "/choreList?message=Chore+completed");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/viewChore?id=" + id + "&message=Not+allowed");
                }
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                resp.sendRedirect(req.getContextPath() + "/viewChore?id=" + id + "&message=Database+error");
                return;
            }
        }

        resp.sendRedirect(req.getContextPath() + "/choreList");
    }
}