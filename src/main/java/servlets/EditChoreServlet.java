package servlets;

import models.Chore;
import services.ChoreService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/chore/edit")
public class EditChoreServlet extends HttpServlet {
    private final ChoreService choreService = new ChoreService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String userId = (String) req.getSession().getAttribute("userId");
        String idStr = req.getParameter("id");
        String context = req.getContextPath();
        Chore ch = null;
        if (idStr != null) {
            try {
                ch = choreService.getById(idStr);
                if (ch == null || !ch.getCreatedBy().equals(userId)) ch = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        out.println("<!doctype html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='utf-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
        out.println("<link rel='stylesheet' href='" + context + "/styles.css'>");
        out.println("<link rel='icon' type='image/svg+xml' href='" + context + "/favicon.svg'>");
        out.println("<title>" + (ch != null ? "Edit Chore" : "Create Chore") + " · ChoreConnect</title>");
        out.println("</head>");
        out.println("<body>");

        // Header
        out.println("<header class='header container'>");
        out.println("<div class='brand-logo'><img class='brand-logo' src='" + context + "/choreconnect-logo.svg' alt='ChoreConnect Logo'></div>");
        out.println("<div class='brand-title'>ChoreConnect</div>");
        out.println("</header>");

        // Main card
        out.println("<main class='container'>");
        out.println("<section class='card'>");
        out.println("<h1>" + (ch != null ? "Edit Chore" : "Create New Chore") + "</h1>");

        // Form
        out.println("<form method='post' action='edit'>");

        if (ch != null) {
            out.println("<input type='hidden' name='id' value='" + ch.getId() + "'/>");
        }

        out.println("<div class='form-grid'>");
        out.println("<div>");
        out.println("<label for='title'>Title</label>");
        out.println("<input id='title' class='input' type='text' name='title' required maxlength='200' value='" + (ch != null ? ch.getTitle() : "") + "'/>");
        out.println("</div>");

        out.println("<div>");
        out.println("<label for='description'>Description</label>");
        out.println("<textarea id='description' class='input' name='description' rows='5'>" + (ch != null ? ch.getDescription() : "") + "</textarea>");
        out.println("</div>");

        out.println("<div>");
        out.println("<label><input type='checkbox' name='isPublic' " + ((ch == null || ch.isPublic()) ? "checked" : "") + "> Public</label>");
        out.println("</div>");

        out.println("<div>");
        out.println("<label for='latitude'>Latitude</label>");
        out.println("<input id='latitude' class='input' type='number' step='0.000001' name='latitude' value='" + (ch != null ? ch.getLatitude() : "") + "'/>");
        out.println("</div>");

        out.println("<div>");
        out.println("<label for='longitude'>Longitude</label>");
        out.println("<input id='longitude' class='input' type='number' step='0.000001' name='longitude' value='" + (ch != null ? ch.getLongitude() : "") + "'/>");
        out.println("</div>");
        out.println("</div>"); // form-grid

        out.println("<div class='actions' style='margin-top:12px'>");
        out.println("<button class='btn btn-primary' type='submit'>" + (ch != null ? "Update Chore" : "Create Chore") + "</button>");
        out.println("<a class='btn btn-secondary' href='" + context + "/choreList'>Back to My Chores</a>");
        out.println("</div>");

        out.println("</form>");
        out.println("</section>");

        out.println("<p class='footer'>© 2026 ChoreConnect</p>");
        out.println("</main>");

        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userId = (String) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login?message=Please+login");
            return;
        }

        String idStr = req.getParameter("id");
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        boolean isPublic = req.getParameter("isPublic") != null;
        double latitude = 0;
        double longitude = 0;

        try {
            String latStr = req.getParameter("latitude");
            String lngStr = req.getParameter("longitude");
            if (latStr != null && !latStr.isEmpty()) latitude = Double.parseDouble(latStr);
            if (lngStr != null && !lngStr.isEmpty()) longitude = Double.parseDouble(lngStr);
        } catch (NumberFormatException e) {
            // ignore invalid
        }

        if (title == null || title.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/chore/edit?message=Title+required");
            return;
        }

        Chore ch = new Chore();
        ch.setId(idStr);
        ch.setTitle(title.trim());
        ch.setDescription(description);
        ch.setPublic(isPublic);
        ch.setLatitude(latitude);
        ch.setLongitude(longitude);
        ch.setCreatedBy(userId);

        try {
            if (idStr == null || idStr.isEmpty()) {
                choreService.create(ch);
                resp.sendRedirect(req.getContextPath() + "/choreList?message=Chore+created");
            } else {
                choreService.update(ch, userId);
                resp.sendRedirect(req.getContextPath() + "/choreList?message=Chore+updated");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/choreList?message=Error+saving+chore");
        }
    }
}