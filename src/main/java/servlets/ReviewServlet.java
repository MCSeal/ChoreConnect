package servlets;

import services.ReviewService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {
    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest req, javax.servlet.http.HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        String choreIdStr = req.getParameter("choreId");

        out.println("<html><head><title>Review Worker</title></head><body>");
        if (choreIdStr == null) {
            out.println("<p>Missing choreId</p></body></html>");
            return;
        }

        try {
            int choreId = Integer.parseInt(choreIdStr);
            if (!reviewService.canOwnerReviewWorker(choreId, userId)) {
                out.println("<p>Cannot review: chore not DONE or not your chore.</p>");
                out.println("</body></html>");
                return;
            }
            out.println("<h2>Review worker for chore #" + choreId + "</h2>");
            out.println("<eview");
            out.println("<input type='hidden' name='choreId' value='" + choreId + "'/>");
            out.println("Rating (1-5): <input type='number' name='rating' min='1' max='5' required/><br/><br/>");
            out.println("Comment:<br/><textarea name='comment' rows='5' cols='50'></textarea><br/><br/>");
            out.println("<input type='submit' value='Submit review'/>");
            out.println("</form>");
        } catch (Exception e) {
            out.println("<p>Invalid choreId</p>");
        }
        out.println("</body></html>");
    }

    @Override
    protected void doPost(javax.servlet.http.HttpServletRequest req,
                          javax.servlet.http.HttpServletResponse resp) throws IOException {
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        String choreIdStr = req.getParameter("choreId");
        String ratingStr = req.getParameter("rating");
        String comment = req.getParameter("comment");

        try {
            int choreId = Integer.parseInt(choreIdStr);
            int rating = Integer.parseInt(ratingStr);
            if (rating < 1 || rating > 5) throw new IllegalArgumentException();

            if (reviewService.canOwnerReviewWorker(choreId, userId)) {
                boolean ok = reviewService.createOwnerToWorkerReview(choreId, userId, rating, comment);
                if (ok) {
                    resp.sendRedirect(req.getContextPath() + "/choreList?message=Review+submitted");
                    return;
                }
            }
            resp.sendRedirect(req.getContextPath() + "/choreList?message=Unable+to+submit+review");
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/choreList?message=Error+submitting+review");
        }
    }
}
