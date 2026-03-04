package servlets;

import services.ChoreService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/chore/accept")
public class AcceptChoreServlet extends HttpServlet {
    private final ChoreService choreService = new ChoreService();

    @Override
    protected void doPost(javax.servlet.http.HttpServletRequest req,
                          javax.servlet.http.HttpServletResponse resp) throws IOException {
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendRedirect(req.getContextPath() + "/publicChoreList?message=Missing+chore+id");
            return;
        }
        try {
            boolean ok = choreService.accept(Integer.parseInt(idStr), userId);
            if (ok) {
                resp.sendRedirect(req.getContextPath() + "/choreList?message=Chore+accepted");
            } else {
                resp.sendRedirect(req.getContextPath() + "/publicChoreList?message=Unable+to+accept+(maybe+already+accepted)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/publicChoreList?message=Database+error");
        }
    }
}
