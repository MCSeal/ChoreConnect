package servlets;

import models.Chore;
import services.ChoreService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/publicChores")
public class ChoreApiServlet extends HttpServlet {
    private final ChoreService choreService = new ChoreService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            List<Chore> publicChores = choreService.getPublicOpenChores();

            out.print("["); // start JSON array
            for (int i = 0; i < publicChores.size(); i++) {
                Chore ch = publicChores.get(i);
                out.printf("{\"id\":\"%s\",\"title\":\"%s\",\"lat\":%f,\"lng\":%f,\"price\":\"N/A\"}",
                        ch.getId(), ch.getTitle(), ch.getLatitude(), ch.getLongitude());
                if (i < publicChores.size() - 1) out.print(",");
            }
            out.print("]"); // end JSON array

        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(500);
            out.print("{\"error\":\"Failed to fetch public chores.\"}");
        }
    }
}