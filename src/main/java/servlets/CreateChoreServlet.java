package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Chore;
import services.ChoreService;

@WebServlet("/createChore")
public class CreateChoreServlet extends HttpServlet {

	// service that talks to the database
	private final ChoreService choreService = new ChoreService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// check if user is logged in
		HttpSession session = req.getSession(false);
		String userId = session != null ? (String) session.getAttribute("userId") : null;

		// if not logged in send them to login page
		if (userId == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		// show the jsp page with the form
		req.getRequestDispatcher("/WEB-INF/createChore.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// again check login before doing anything
		HttpSession session = req.getSession(false);
		String userId = session != null ? (String) session.getAttribute("userId") : null;

		if (userId == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		// grab the form values
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

		// basic validation
		if (title == null || title.trim().isEmpty()) {
			req.setAttribute("error", "title is required");
			copyFormValues(req);
			req.getRequestDispatcher("/WEB-INF/createChore.jsp").forward(req, resp);
			return;
		}

		try {

			// handle hourly vs lump sum pricing
			if ("HOURLY".equals(priceType)) {

				String rateStr = req.getParameter("hourlyRate");
				String hoursStr = req.getParameter("hours");

				if (rateStr != null && !rateStr.isEmpty()) {
					hourlyRate = Double.parseDouble(rateStr);
				}

				if (hoursStr != null && !hoursStr.isEmpty()) {
					hours = Integer.parseInt(hoursStr);
				}

				// calculate total price if both values exist
				if (hourlyRate != null && hours != null) {
					priceAmount = hourlyRate * hours;
				}

			} else {

				// lump sum pricing
				String priceStr = req.getParameter("priceAmount");

				if (priceStr != null && !priceStr.isEmpty()) {
					priceAmount = Double.parseDouble(priceStr);
				}
			}

			// parse map coordinates
			if (latitudeStr != null && !latitudeStr.isEmpty()) {
				latitude = Double.parseDouble(latitudeStr);
			}

			if (longitudeStr != null && !longitudeStr.isEmpty()) {
				longitude = Double.parseDouble(longitudeStr);
			}

		} catch (NumberFormatException e) {

			// if something numeric fails just show error and reload form
			req.setAttribute("error", "please enter valid numbers");
			copyFormValues(req);
			req.getRequestDispatcher("/WEB-INF/createChore.jsp").forward(req, resp);
			return;
		}

		// build the chore object
		Chore chore = new Chore();
		chore.setId(UUID.randomUUID().toString());
		chore.setTitle(title);
		chore.setDescription(description);
		chore.setCreatedBy(userId);
		chore.setPublic(true); // default to public for now
		chore.setLatitude(latitude);
		chore.setLongitude(longitude);
		chore.setPriceType(priceType);
		chore.setHourlyRate(hourlyRate);
		chore.setHours(hours);
		chore.setPriceAmount(priceAmount);

		try {

			// save chore to database
			choreService.create(chore);

			// go back to the users chore list
			resp.sendRedirect(req.getContextPath() + "/choreList");

		} catch (SQLException e) {

			// database error
			e.printStackTrace();

			req.setAttribute("error", "unable to create chore");
			copyFormValues(req);
			req.getRequestDispatcher("/WEB-INF/createChore.jsp").forward(req, resp);
		}
	}

	// helper so user doesn't lose form values if something fails
	private void copyFormValues(HttpServletRequest req) {
		req.setAttribute("title", req.getParameter("title"));
		req.setAttribute("dueDate", req.getParameter("dueDate"));
		req.setAttribute("priority", req.getParameter("priority"));
		req.setAttribute("notes", req.getParameter("notes"));
		req.setAttribute("priceType", req.getParameter("priceType"));
		req.setAttribute("hourlyRate", req.getParameter("hourlyRate"));
		req.setAttribute("hours", req.getParameter("hours"));
		req.setAttribute("priceAmount", req.getParameter("priceAmount"));
		req.setAttribute("latitude", req.getParameter("latitude"));
		req.setAttribute("longitude", req.getParameter("longitude"));
	}
}