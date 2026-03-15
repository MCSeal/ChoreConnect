<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Chore" %>

<%
    String context = request.getContextPath();
    String message = (String) request.getAttribute("message");
    String error = (String) request.getAttribute("error");
    String userId = (String) request.getAttribute("userId");
    String userName = (String) request.getAttribute("userName");
    List<Chore> publicChores = (List<Chore>) request.getAttribute("publicChores");
%>

<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="icon" type="image/svg+xml" href="<%= context %>/favicon.svg">
  <title>Chores Near You · ChoreConnect</title>
  <link rel="stylesheet" href="<%= context %>/styles.css">

  <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css">

  <style>
    #map {
      height: 400px;
      border-radius: 12px;
      margin-bottom: 1rem;
    }

    .chore-table tbody tr:hover {
      background-color: #f0faff;
      cursor: pointer;
    }
  </style>
</head>
<body>

  <header class="header container">
    <div class="brand-logo">
      <img class="brand-logo" src="<%= context %>/choreconnect-logo.svg" alt="ChoreConnect Logo">
    </div>

    <div class="brand-title">ChoreConnect</div>

    <% if (userId != null) { %>
      <nav class="actions">
        <a class="btn btn-secondary" href="<%= context %>/choreList">My Chores</a>
        <a class="btn btn-secondary" href="<%= context %>/createChore">Create Chore</a>

        <form method="post" action="<%= context %>/signout" style="display:inline;">
          <button class="btn" type="submit">Sign out</button>
        </form>
      </nav>
    <% } %>
  </header>

  <main class="container">
    <section class="card">
      <h1>Chores Nearby</h1>

      <% if (message != null) { %>
        <div class="notice success"><%= message %></div>
      <% } %>

      <% if (error != null) { %>
        <div class="notice error"><%= error %></div>
      <% } %>

      <div id="map"></div>

      <div class="card" style="padding:0;">
        <table class="table chore-table" aria-label="Nearby Chores">
          <thead>
            <tr>
              <th>Title</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>

          <% if (publicChores == null || publicChores.isEmpty()) { %>
            <tr>
              <td colspan="3" style="color: var(--muted);">No chores available nearby.</td>
            </tr>
          <% } else { %>
            <% for (Chore ch : publicChores) { %>
              <tr onclick="focusChore(<%= ch.getLatitude() %>, <%= ch.getLongitude() %>, '<%= ch.getId() %>')">
                <td><%= ch.getTitle() %></td>
                <td>
                  <span class="badge <%= "OPEN".equals(ch.getStatus()) ? "open" : "done" %>">
                    <%= ch.getStatus() %>
                  </span>
                </td>
                <td>
                  <a class="btn btn-primary" href="<%= context %>/viewChore?id=<%= ch.getId() %>">View</a>
                </td>
              </tr>
            <% } %>
          <% } %>

          </tbody>
        </table>
      </div>
    </section>
  </main>

  <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>

  <script>
    const map = L.map('map').setView([45.4215, -75.6972], 12);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap'
    }).addTo(map);

    <% if (publicChores != null) { %>
    <% for (Chore ch : publicChores) { 
         String safeTitle = ch.getTitle() == null ? "" : ch.getTitle().replace("\"", "\\\""); 
    %>
      L.marker([<%= ch.getLatitude() %>, <%= ch.getLongitude() %>])
        .addTo(map)
        .bindPopup(
          "<strong><%= safeTitle %></strong><br>" +
          "<a href='<%= context %>/viewChore?id=<%= ch.getId() %>'>View chore</a>"
        );
    <% } %>
  <% } %>

    function focusChore(lat, lng, id) {
      map.setView([lat, lng], 15);
    }
  </script>

</body>
</html>