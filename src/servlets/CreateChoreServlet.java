package servlets;

import models.Chore;
import services.ChoreService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/createChore")
public class CreateChoreServlet extends HttpServlet {

	private final ChoreService choreService = new ChoreService();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// Check session (must be logged in)
		HttpSession session = req.getSession(false);
		String userId = session != null ? (String) session.getAttribute("userId") : null;

		// temp code to hard code user until Log in is working
//		String userId = "testUser";   // temporary test user

		if (userId == null) {
			resp.sendRedirect(req.getContextPath() + "/login.html");
			return;
		}

		// Read form parameters
		String title = req.getParameter("title");
		String description = req.getParameter("notes");
		String latitudeStr = req.getParameter("latitude");
		String longitudeStr = req.getParameter("longitude");

		String priceType = req.getParameter("priceType");

		Double hourlyRate = null;
		Integer hours = null;
		Double priceAmount = null;
		
		double latitude = 0;
		double longitude = 0;

		try {
			if ("HOURLY".equals(priceType)) {
				hourlyRate = Double.parseDouble(req.getParameter("hourlyRate"));
				hours = Integer.parseInt(req.getParameter("hours"));
			} else {
				priceAmount = Double.parseDouble(req.getParameter("priceAmount"));
			}

			if ("HOURLY".equals(priceType) && hourlyRate != null && hours != null) {
				priceAmount = hourlyRate * hours;
			}

			if (latitudeStr != null && longitudeStr != null) {
				latitude = Double.parseDouble(latitudeStr);
				longitude = Double.parseDouble(longitudeStr);
			}
		} catch (NumberFormatException e) {
			latitude = 0;
			longitude = 0;
		}

		// 🏗 Create Chore object
		Chore chore = new Chore();
		chore.setTitle(title);
		chore.setDescription(description);
		chore.setCreatedBy(userId);
		chore.setPublic(true); // default public (you can change this)
		chore.setLatitude(latitude);
		chore.setLongitude(longitude);
		chore.setPriceType(priceType);
		chore.setHourlyRate(hourlyRate);
		chore.setHours(hours);
		chore.setPriceAmount(priceAmount);

		try {
			choreService.create(chore);
			resp.sendRedirect(req.getContextPath() + "/choreList");
		} catch (SQLException e) {
			e.printStackTrace();
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to create chore.");
		}
	}

	// Optional: Show create form if someone goes directly to URL
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.sendRedirect(req.getContextPath() + "/choreCreate.html");
	}
}
