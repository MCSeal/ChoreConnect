
// src/main/java/servlets/ChoreListServlet.java
package servlets;

import models.Chore;
import services.ChoreService;


import javax.servlet.ServletException;
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

        String message = req.getParameter("message");
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        String userName = (String) req.getSession().getAttribute("userName");

        out.println("<html><head><title>Your Chores</title></head><body>");
        out.println("<h2>Hello, " + (userName != null ? userName : "Guest") + "</h2>");
        if (message != null) out.println("<p><b>" + message + "</b></p>");

        try {
            // Fetch chores for this user
            List<Chore> mine = choreService.getChoresByUser(userId);

            out.println("<h3>Your chores</h3>");
            if (mine == null || mine.isEmpty()) {
                out.println("<p>No chores yet.</p>");
            } else {
                for (Chore ch : mine) {
                    out.println("<hr/>");
                    out.println("<b>" + ch.getTitle() + "</b> [" + ch.getStatus() + "]<br/>");
                    out.println("<small>Public: " + (ch.isPublic() ? "Yes" : "No") + "</small><br/><br/>");
                    out.println("<div>" + (ch.getDescription() == null ? "" : ch.getDescription()) + "</div><br/>");
                    out.println("<a href='edit?id=" + ch.getId() + "'>Edit</a>");
                    if ("ACCEPTED".equals(ch.getStatus())) {
                        out.println(" | <a href='t?action=markDone&id=" + ch.getId() + "'>Mark done</a>");
                    }
                    if ("DONE".equals(ch.getStatus())) {
                        out.println(" | <a href='review?choreId=" + ch.getId() + "'>Review</a>");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<p>Error loading chores.</p>");
        }

        out.println("</body></html>");
    }
}
				