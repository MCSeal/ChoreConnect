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
    protected void doGet(HttpServletRequest req, javax.servlet.http.HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String action = req.getParameter("action");
        Integer userId = (Integer) req.getSession().getAttribute("userId");

        // Mark done action
        if ("markDone".equals(action)) {
            String idStr = req.getParameter("id");
            if (idStr != null) {
                try {
                    if (choreService.markDone(Integer.parseInt(idStr), userId)) {
                        resp.sendRedirect(req.getContextPath() + "/choreList?message=Marked+as+done");
                        return;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            resp.sendRedirect(req.getContextPath() + "/choreList?message=Unable+to+mark+done");
            return;
        }

        String idStr = req.getParameter("id");
        Chore edit = null;
        if (idStr != null) {
            try {
                edit = choreService.getById(Integer.parseInt(idStr));
                // optionally validate ownership
                if (edit == null || !edit.getCreatedBy().equals(userId)) edit = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        out.println("<html><head><title>" + (edit != null ? "Edit Chore" : "New Chore") + "</title></head><body>");
        out.println("<h2>" + (edit != null ? "Edit Chore" : "Create New Chore") + "</h2>");
        out.println("edit");

        out.println("Title: <input type='text' name='title' required maxlength='200' value='" + edit != null ? edit.getTitle() : "" + "'/><br/><br/>");
        out.println("Description:<br/>");
        out.println("<textarea name='description' rows='5' cols='50'>" + edit != null ? edit.getDescription() : "" + "</textarea><br/><br/>");
        out.println("Public: <input type='checkbox' name='isPublic' " + ((edit == null || edit.isPublic()) ? "checked" : "") + " /><br/><br/>");

        if (edit != null) {
            out.println("<input type='hidden' name='id' value='" + edit.getId() + "'/>");
        }
        out.println("<input type='submit' value='" + (edit != null ? "Update" : "Create") + "'/>");
        out.println("</form>");

        out.println("<p><a href='" + req.getContextPath() + "/choreList'>Back to My Chores</a></p>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(javax.servlet.http.HttpServletRequest req,
                          javax.servlet.http.HttpServletResponse resp) throws IOException {
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        String idStr = req.getParameter("id");
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        boolean isPublic = req.getParameter("isPublic") != null;

        if (title == null || title.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/chore/edit?message=Title+required");
            return;
        }

        Chore ch = new Chore();
        ch.setTitle(title.trim());
        ch.setDescription(description);
        ch.setPublic(isPublic);
        ch.setCreatedBy(userId);

        try {
            if (idStr == null || idStr.isEmpty()) {
                choreService.create(ch);
                resp.sendRedirect(req.getContextPath() + "/choreList?message=Chore+created");
            } else {
                ch.setId(Integer.parseInt(idStr));
                choreService.update(ch, userId);
                resp.sendRedirect(req.getContextPath() + "/choreList?message=Chore+updated");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/choreList?message=Error+saving+chore");
        }
    }
}
