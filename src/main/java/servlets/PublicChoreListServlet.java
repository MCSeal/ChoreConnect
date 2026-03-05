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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String context = req.getContextPath();
        String message = req.getParameter("message");
        HttpSession session = req.getSession(false);
        String userId = session != null ? (String) session.getAttribute("userId") : null;
        String userName = session != null ? (String) session.getAttribute("userName") : "Guest";

        out.println("<!doctype html>");
        out.println("<html lang='en'><head>");
        out.println("<meta charset='utf-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
        out.println("<link rel='icon' type='image/svg+xml' href='" + context + "/favicon.svg'>");
        out.println("<title>Chores Near You · ChoreConnect</title>");
        out.println("<link rel='stylesheet' href='" + context + "/styles.css'>");

        // Leaflet CSS
        out.println("<link rel='stylesheet' href='https://unpkg.com/leaflet@1.9.4/dist/leaflet.css'>");
        out.println("<style>#map {height:400px;border-radius:12px;margin-bottom:1rem;} .chore-table tbody tr:hover {background-color:#f0faff;cursor:pointer;}</style>");
        out.println("</head><body>");

        // Header
        out.println("<header class='header container'>");
        out.println("<div class='brand-logo'><img class='brand-logo' src='" + context + "/choreconnect-logo.svg' alt='ChoreConnect Logo'></div>");
        out.println("<div class='brand-title'>ChoreConnect</div>");
        if (userId != null) {
            out.println("<nav class='actions'>");
            out.println("<form method='post' action='" + context + "/signout'>");
            out.println("<button class='btn' type='submit'>Sign out</button>");
            out.println("</form></nav>");
        }
        out.println("</header>");

        out.println("<main class='container'><section class='card'>");
        out.println("<h1>Chores Nearby</h1>");
        if (message != null) out.println("<div class='notice success'>" + message + "</div>");

        // Map container
        out.println("<div id='map'></div>");

     // Table of chores
        out.println("<div class='card' style='padding:0;'><table class='table chore-table' aria-label='Nearby Chores'>");
        out.println("<thead><tr><th>Title</th><th>Status</th><th>Action</th></tr></thead><tbody>");

        try {
            List<Chore> publicChores = choreService.getPublicOpenChores();
            if (publicChores == null || publicChores.isEmpty()) {
                out.println("<tr><td colspan='3' style='color: var(--muted);'>No chores available nearby.</td></tr>");
            } else {
                for (Chore ch : publicChores) {
                    out.println("<tr onclick=\"focusChore(" + ch.getLatitude() + "," + ch.getLongitude() + ",'" + ch.getId() + "')\">");
                    out.println("<td>" + ch.getTitle() + "</td>");
                    out.println("<td><span class='badge " + (ch.getStatus().equals("OPEN") ? "open" : "done") + "'>" + ch.getStatus() + "</span></td>");
                    out.println("<td><a class='btn btn-primary' href='" + context + "/viewChore?id=" + ch.getId() + "'>View</a></td>");
                    out.println("</tr>");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<tr><td colspan='3'>Error loading public chores.</td></tr>");
        }

        out.println("</tbody></table></div>");

        // Leaflet JS
        out.println("<script src='https://unpkg.com/leaflet@1.9.4/dist/leaflet.js'></script>");
        out.println("<script>");
        out.println("const map = L.map('map').setView([45.4215, -75.6972], 12);");
        out.println("L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { attribution:'© OpenStreetMap' }).addTo(map);");

        // Add markers dynamically
        try {
            List<Chore> publicChores = choreService.getPublicOpenChores();
            for (Chore ch : publicChores) {
                out.println("L.marker([" + ch.getLatitude() + "," + ch.getLongitude() + "])");
                out.println(".addTo(map).bindPopup(\"<strong>" + ch.getTitle() + "</strong><br><a href='" + context + "/viewChore?id=" + ch.getId() + "'>View chore</a>\");");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Focus function
        out.println("function focusChore(lat, lng, id){ map.setView([lat,lng],15); }");
        out.println("</script>");
        out.println("</body></html>");
    }
}