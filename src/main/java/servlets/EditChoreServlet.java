package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Chore;
import services.ChoreService;

@WebServlet("/chore/edit")
public class EditChoreServlet extends HttpServlet {

	private final ChoreService choreService = new ChoreService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		String userId = session != null ? (String) session.getAttribute("userId") : null;

		if (userId == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		String idStr = req.getParameter("id");
		String message = req.getParameter("message");
		Chore chore = null;

		if (idStr != null && !idStr.isEmpty()) {
			try {
				chore = choreService.getById(idStr);

				if (chore == null || !userId.equals(chore.getCreatedBy())) {
					chore = null;
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		req.setAttribute("chore", chore);
		req.setAttribute("message", message);

		req.getRequestDispatcher("/WEB-INF/editChore.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		HttpSession session = req.getSession(false);
		String userId = session != null ? (String) session.getAttribute("userId") : null;

		if (userId == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		String idStr = req.getParameter("id");
		String title = req.getParameter("title");
		String description = req.getParameter("description");
		boolean isPublic = req.getParameter("isPublic") != null;

		double latitude = 0;
		double longitude = 0;

		String priceType = req.getParameter("priceType");
		Double hourlyRate = null;
		Integer hours = null;
		Double priceAmount = null;

		try {
			String latStr = req.getParameter("latitude");
			String lngStr = req.getParameter("longitude");

			if (latStr != null && !latStr.isEmpty()) {
				latitude = Double.parseDouble(latStr);
			}

			if (lngStr != null && !lngStr.isEmpty()) {
				longitude = Double.parseDouble(lngStr);
			}

			String hourlyRateStr = req.getParameter("hourlyRate");
			String hoursStr = req.getParameter("hours");
			String priceAmountStr = req.getParameter("priceAmount");

			if (hourlyRateStr != null && !hourlyRateStr.isEmpty()) {
				hourlyRate = Double.parseDouble(hourlyRateStr);
			}

			if (hoursStr != null && !hoursStr.isEmpty()) {
				hours = Integer.parseInt(hoursStr);
			}

			if (priceAmountStr != null && !priceAmountStr.isEmpty()) {
				priceAmount = Double.parseDouble(priceAmountStr);
			}

		} catch (NumberFormatException e) {
			req.setAttribute("error", "Invalid number entered.");
			req.setAttribute("formTitle", title);
			req.setAttribute("formDescription", description);
			req.setAttribute("formIsPublic", isPublic);
			req.setAttribute("formLatitude", req.getParameter("latitude"));
			req.setAttribute("formLongitude", req.getParameter("longitude"));
			req.setAttribute("formPriceType", req.getParameter("priceType"));
			req.setAttribute("formHourlyRate", req.getParameter("hourlyRate"));
			req.setAttribute("formHours", req.getParameter("hours"));
			req.setAttribute("formPriceAmount", req.getParameter("priceAmount"));
			req.getRequestDispatcher("/WEB-INF/editChore.jsp").forward(req, resp);
			return;
		}

		if (title == null || title.trim().isEmpty()) {
			req.setAttribute("error", "Title required.");
			req.setAttribute("formTitle", title);
			req.setAttribute("formDescription", description);
			req.setAttribute("formIsPublic", isPublic);
			req.setAttribute("formLatitude", req.getParameter("latitude"));
			req.setAttribute("formLongitude", req.getParameter("longitude"));
			req.setAttribute("formPriceType", req.getParameter("priceType"));
			req.setAttribute("formHourlyRate", req.getParameter("hourlyRate"));
			req.setAttribute("formHours", req.getParameter("hours"));
			req.setAttribute("formPriceAmount", req.getParameter("priceAmount"));
			req.getRequestDispatcher("/WEB-INF/editChore.jsp").forward(req, resp);
			return;
		}

		Chore existing = null;

		try {
			if (idStr != null && !idStr.isEmpty()) {
				existing = choreService.getById(idStr);

				if (existing == null || !userId.equals(existing.getCreatedBy())) {
					resp.sendRedirect(req.getContextPath() + "/choreList?message=Chore+not+found");
					return;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			resp.sendRedirect(req.getContextPath() + "/choreList?message=Error+loading+chore");
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

		if (existing == null || "OPEN".equals(existing.getStatus())) {
			ch.setPriceType(priceType);
			ch.setHourlyRate(hourlyRate);
			ch.setHours(hours);
			ch.setPriceAmount(priceAmount);
		} else {
			ch.setPriceType(existing.getPriceType());
			ch.setHourlyRate(existing.getHourlyRate());
			ch.setHours(existing.getHours());
			ch.setPriceAmount(existing.getPriceAmount());
		}

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