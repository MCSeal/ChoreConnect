package servlets;

import services.ChoreService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/chore/accept")
public class AcceptChoreServlet extends HttpServlet {
    private final ChoreService choreService = new ChoreService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String userId = (String) req.getSession().getAttribute("userId");
        String choreId = req.getParameter("choreId");

        out.println("<!doctype html><html lang='en'><head>");
        out.println("<meta charset='utf-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
        out.println("<link rel='stylesheet' href='styles.css'>");
        out.println("<title>Chore Accepted · ChoreConnect</title>");
        out.println("</head><body><div class='container'>");

        out.println("<header class='header'>");
        out.println("<a class='brand' href='publicChoreList'>ChoreConnect</a>");
        out.println("<nav class='actions'><form method='post' action='/signout'>");
        out.println("<button class='btn' type='submit'>Sign out</button>");
        out.println("</form></nav></header>");

        if (userId != null && choreId != null) {
            try {
                if (choreService.accept(choreId, userId)) {
                    out.println("<div class='card notice success'>You have successfully accepted this chore!</div>");
                    out.println("<p><a class='btn btn-primary' href='choreList'>Go to My Chores</a></p>");
                    out.println("<p><a class='btn btn-secondary' href='publicChoreList'>Back to Public Chores</a></p>");
                } else {
                    out.println("<div class='card notice error'>Unable to accept this chore. It might already be taken.</div>");
                    out.println("<p><a class='btn btn-secondary' href='publicChoreList'>Back to Public Chores</a></p>");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                out.println("<div class='card notice error'>Error accepting chore.</div>");
            }
        } else {
            out.println("<div class='card notice error'>Missing user or chore info.</div>");
        }

        out.println("</div></body></html>");
    }
}