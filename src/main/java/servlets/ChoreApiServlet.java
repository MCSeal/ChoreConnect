package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Chore;
import services.ChoreService;

@WebServlet("/api/publicChores")
public class ChoreApiServlet extends HttpServlet {

	// service used to fetch chores from db
	private final ChoreService choreService = new ChoreService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		// api returns json instead of html
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter out = resp.getWriter();

		try {

			// get all public chores that are still open
			List<Chore> publicChores = choreService.getPublicOpenChores();

			// start json array
			out.print("[");

			for (int i = 0; i < publicChores.size(); i++) {

				Chore ch = publicChores.get(i);

				// build simple json object for each chore
				out.printf("{\"id\":\"%s\",\"title\":\"%s\",\"lat\":%f,\"lng\":%f,\"price\":\"N/A\"}", ch.getId(),
						ch.getTitle(), ch.getLatitude(), ch.getLongitude());

				// comma between objects
				if (i < publicChores.size() - 1) {
					out.print(",");
				}
			}

			// end json array
			out.print("]");

		} catch (SQLException e) {

			e.printStackTrace();

			// send error response
			resp.setStatus(500);
			out.print("{\"error\":\"Failed to fetch public chores.\"}");
		}
	}
}