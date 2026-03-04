package servlets;

import models.Chore;
import services.ChoreService;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/publicChoreList")
public class PublicChoreListServlet extends HttpServlet {
    private final ChoreService choreService = new ChoreService();

    @Override
    protected void doGet(HttpServletRequest req, javax.servlet.http.HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String message = req.getParameter("message");
        String userId = (String) (req.getSession() != null ? req.getSession().getAttribute("userId") : null);

        out.println("<html><head><title>Public Chores</title></head><body>");
        if (message != null) out.println("<p><b>" + Html.esc(message) + "</b></p>");
        out.println("<h2>Public chore list</h2>");
        out.println("<p>loginLogin</a> | registerRegister</a> | choreListMy chores</a></p>");

        try {
            List<Chore> open = choreService.listPublicOpen();
            if (open.isEmpty()) {
                out.println("<p>No public chores available.</p>");
            } else {
                for (Chore ch : open) {
                    out.println("<hr/>");
                    out.println("<b>" + ch.getTitle()) + "</b> [" + Html.esc(ch.getStatus()) + "]<br/><br/>");
                    out.println("<div>" + ch.getDescription() == null ? "" : ch.getDescription()) + "</div><br/>");
                    if (userId != null) {
                        out.println("<form method='post' action='chore/accept' style='ln("<input type='hidden' name='id' value='" + ch.getId() + "'/>");
                        out.println("<input type='submit' value='Accept chore'/>");
                        out.println("</form>");
                    } else {
                        out.println("login?message=Please+login+to+acceptLogin to accept</a>");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<p>Error loading public chores.</p>");
        }
        out.println("</body></html>");
    }
}