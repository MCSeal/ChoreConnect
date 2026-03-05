package servlets;

import models.Chore;
import services.ChoreService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/choreList")
public class ChoreListServlet extends HttpServlet {

    private final ChoreService choreService = new ChoreService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        HttpSession session = req.getSession(false);
        String userId = session != null ? (String) session.getAttribute("userId") : null;

        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<Chore> posted = null;
        List<Chore> accepted = null;
        List<Chore> completed = null;

        try {
            posted = choreService.getByCreator(userId);
            accepted = choreService.getByAccepted(userId);
            completed = choreService.getCompletedByUser(userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        out.println("<!doctype html><html lang='en'><head>");
        out.println("<meta charset='utf-8'>");
        out.println("<title>My Chores · ChoreConnect</title>");
        out.println("<link rel='stylesheet' href='styles.css'>");
        out.println("</head><body>");
        out.println("<div class='container'>");

        // Header
        String context = req.getContextPath();

        out.println("<header class='header'>");
        out.println("<a class='brand' href='" + context + "/publicChoreList'>ChoreConnect</a>");

        out.println("<div class='actions'>");
        out.println("<a class='btn btn-secondary' href='" + context + "/publicChoreList'>Public Chores</a>");

        out.println("<form method='post' action='" + context + "/signout' style='display:inline'>");
        out.println("<button class='btn'>Sign out</button>");
        out.println("</form>");
        out.println("</div>");

        out.println("</header>");

        out.println("<h1>My Chores</h1>");

        // ===== POSTED =====
        out.println("<section class='card'>");
        out.println("<h2>Your Posted Chores</h2>");
        if (posted == null || posted.isEmpty()) {
            out.println("<p>No chores posted.</p>");
        } else {
            for (Chore c : posted) {
            	out.println("<div class='chore-item'>");

            	out.println("<div class='chore-info'>");
            	out.println("<b>" + c.getTitle() + "</b>");
            	if (c.getDescription() != null) {
            	    out.println("<div class='muted'>" + c.getDescription() + "</div>");
            	}
            	out.println("</div>");

            	out.println("<div class='chore-actions'>");
            	out.println("<a class='btn btn-primary' href='viewChore?id=" + c.getId() + "'>View</a>");
            	out.println("</div>");

            	out.println("</div>");
            }
        }
        out.println("</section>");

        // ===== ACCEPTED =====
        out.println("<section class='card'>");
        out.println("<h2>Chores You Accepted</h2>");
        if (accepted == null || accepted.isEmpty()) {
            out.println("<p>No accepted chores.</p>");
        } else {
            for (Chore c : accepted) {
                out.println("<div class='chore-item'>");
                out.println("<b>" + c.getTitle() + "</b>");
                out.println("<br>");
                out.println("<a class='btn btn-primary' href='viewChore?id=" + c.getId() + "'>View</a>");
                out.println("</div>");
            }
        }
        out.println("</section>");

        // ===== COMPLETED =====
        out.println("<section class='card'>");
        out.println("<h2>Completed Chores</h2>");
        if (completed == null || completed.isEmpty()) {
            out.println("<p>No completed chores.</p>");
        } else {
            for (Chore c : completed) {
                out.println("<div class='chore-item'>");
                out.println("<b>" + c.getTitle() + "</b>");
                out.println("</div>");
            }
        }
        out.println("</section>");

        out.println("</div></body></html>");
    }
}