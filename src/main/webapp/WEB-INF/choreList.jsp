<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Map" %>
<%@ page import="models.Chore" %>

<%
    List<Chore> posted = (List<Chore>) request.getAttribute("posted");
    List<Chore> accepted = (List<Chore>) request.getAttribute("accepted");
    List<Chore> completedByYou = (List<Chore>) request.getAttribute("completedByYou");
    List<Chore> postedCompleted = (List<Chore>) request.getAttribute("postedCompleted");
    Set<String> reviewedChoreIds = (Set<String>) request.getAttribute("reviewedChoreIds");
    Map<String, String> acceptedUserNames = (Map<String, String>) request.getAttribute("acceptedUserNames");

    String error = (String) request.getAttribute("error");
    String context = request.getContextPath();
%>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
      <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="icon" type="image/svg+xml" href="<%= context %>/favicon.svg">
    <title>My Chores · ChoreConnect</title>
    <link rel="stylesheet" href="<%= context %>/styles.css">
</head>
<body>
    <div class="container">

        
    <header class="header container">
        <a class="brand" href="<%= context %>/choreList" aria-label="ChoreConnect">
            <div class="brand-logo" aria-hidden="true">
                <img class="brand-logo" src="<%= context %>/choreconnect-logo.svg" alt="ChoreConnect Logo">
            </div>

            <div class="brand-title">ChoreConnect</div>
        </a>
     
            <div class="actions">
                <a class="btn btn-secondary" href="<%= context %>/createChore">Create new Chore</a>
                <a class="btn btn-secondary" href="<%= context %>/publicChoreList">Public Chores</a>

                <form method="post" action="<%= context %>/signout" style="display:inline">
                    <button class="btn" type="submit">Sign out</button>
                </form>
            </div>
        </header>

        <h1>My Chores</h1>

        <% if (error != null) { %>
            <div class="notice error" role="alert">
                <%= error %>
            </div>
        <% } %>

        <section class="card">
            <h2>Your Posted Chores</h2>
            <p class="section-subtext">Jobs you created that are still active.</p>

            <% if (posted == null || posted.isEmpty()) { %>
                <p>No active posted chores.</p>
            <% } else { %>
                <% for (Chore c : posted) {
                    String status = c.getStatus() != null ? c.getStatus().trim() : "OPEN";
                    String statusLower = status.toLowerCase();

                    String badgeClass = "status-badge";
                    if ("open".equals(statusLower)) {
                        badgeClass += " status-open";
                    } else if ("accepted".equals(statusLower)) {
                        badgeClass += " status-accepted";
                    } else if ("completed".equals(statusLower)) {
                        badgeClass += " status-completed";
                    } else if ("cancelled".equals(statusLower)) {
                        badgeClass += " status-cancelled";
                    }

                    String acceptedById = c.getAcceptedBy();
                    String fullName = acceptedUserNames != null ? acceptedUserNames.get(acceptedById) : null;
                    String firstName = "";

                    if (fullName != null && !fullName.trim().isEmpty()) {
                        firstName = fullName.contains(" ")
                            ? fullName.substring(0, fullName.indexOf(" "))
                            : fullName;
                    }
                %>
                    <div class="chore-item">
                        <div class="chore-info">
                            <div class="chore-title-row">
                                <span class="chore-title"><%= c.getTitle() %></span>
                                <span class="<%= badgeClass %>"><%= status %></span>
                            </div>

                            <% if (c.getDescription() != null && !c.getDescription().trim().isEmpty()) { %>
                                <div class="muted"><%= c.getDescription() %></div>
                            <% } %>

                            <div class="chore-meta">
                                <% if (!firstName.isEmpty()) { %>
                                    Accepted by: <%= firstName %>
                                <% } else { %>
                                    Waiting for a worker to accept
                                <% } %>
                            </div>
                        </div>

                        <div class="chore-actions">
                            <a class="btn btn-primary" href="<%= context %>/viewChore?id=<%= c.getId() %>">View</a>
                        </div>
                    </div>
                <% } %>
            <% } %>
        </section>

        <section class="card">
            <h2>Chores You Accepted</h2>
            <p class="section-subtext">Jobs you picked up and are still working on.</p>

            <% if (accepted == null || accepted.isEmpty()) { %>
                <p>No active accepted chores.</p>
            <% } else { %>
                <% for (Chore c : accepted) {
                    String status = c.getStatus() != null ? c.getStatus().trim() : "ACCEPTED";
                    String statusLower = status.toLowerCase();

                    String badgeClass = "status-badge";
                    if ("open".equals(statusLower)) {
                        badgeClass += " status-open";
                    } else if ("accepted".equals(statusLower)) {
                        badgeClass += " status-accepted";
                    } else if ("completed".equals(statusLower)) {
                        badgeClass += " status-completed";
                    } else if ("cancelled".equals(statusLower)) {
                        badgeClass += " status-cancelled";
                    }
                %>
                    <div class="chore-item">
                        <div class="chore-info">
                            <div class="chore-title-row">
                                <span class="chore-title"><%= c.getTitle() %></span>
                                <span class="<%= badgeClass %>"><%= status %></span>
                            </div>

                            <% if (c.getDescription() != null && !c.getDescription().trim().isEmpty()) { %>
                                <div class="muted"><%= c.getDescription() %></div>
                            <% } %>
                        </div>

                        <div class="chore-actions">
                            <a class="btn btn-primary" href="<%= context %>/viewChore?id=<%= c.getId() %>">View</a>
                        </div>
                    </div>
                <% } %>
            <% } %>
        </section>

        <section class="card">
            <h2>Chores Completed By You</h2>
            <p class="section-subtext">Jobs you finished as the worker.</p>

            <% if (completedByYou == null || completedByYou.isEmpty()) { %>
                <p>No chores completed by you yet.</p>
            <% } else { %>
                <% for (Chore c : completedByYou) { %>
                    <div class="chore-item">
                        <div class="chore-info">
                            <div class="chore-title-row">
                                <span class="chore-title"><%= c.getTitle() %></span>
                                <span class="status-badge status-completed">COMPLETED</span>
                            </div>

                            <% if (c.getDescription() != null && !c.getDescription().trim().isEmpty()) { %>
                                <div class="muted"><%= c.getDescription() %></div>
                            <% } %>

                            <div class="chore-meta">
                                Completed by you
                            </div>
                        </div>

                        <div class="chore-actions">
                            <a class="btn btn-primary" href="<%= context %>/viewChore?id=<%= c.getId() %>">View</a>
                        </div>
                    </div>
                <% } %>
            <% } %>
        </section>

        <section class="card">
            <h2>Your Posted Chores That Were Completed</h2>
            <p class="section-subtext">Finished jobs you created. You can review the worker once the chore has been completed.</p>

            <% if (postedCompleted == null || postedCompleted.isEmpty()) { %>
                <p>No completed posted chores yet.</p>
            <% } else { %>
                <% for (Chore c : postedCompleted) {
                    boolean reviewed = reviewedChoreIds != null && reviewedChoreIds.contains(c.getId());

                    String acceptedById = c.getAcceptedBy();
                    String fullName = acceptedUserNames != null ? acceptedUserNames.get(acceptedById) : null;
                    String firstName = "";

                    if (fullName != null && !fullName.trim().isEmpty()) {
                        firstName = fullName.contains(" ")
                            ? fullName.substring(0, fullName.indexOf(" "))
                            : fullName;
                    }
                %>
                    <div class="chore-item">
                        <div class="chore-info">
                            <div class="chore-title-row">
                                <span class="chore-title"><%= c.getTitle() %></span>
                                <span class="status-badge status-completed">COMPLETED</span>
                                <% if (reviewed) { %>
                                    <span class="status-badge status-reviewed">Reviewed</span>
                                <% } %>
                            </div>

                            <% if (c.getDescription() != null && !c.getDescription().trim().isEmpty()) { %>
                                <div class="muted"><%= c.getDescription() %></div>
                            <% } %>

                            <div class="chore-meta">
                                <% if (!firstName.isEmpty()) { %>
                                    Completed by: <%= firstName %>
                                <% } else { %>
                                    Worker information unavailable
                                <% } %>
                            </div>

                            <div class="chore-meta">
                                <% if (reviewed) { %>
                                    Review already submitted
                                <% } else { %>
                                    Not reviewed yet
                                <% } %>
                            </div>
                        </div>

                        <div class="chore-actions">
                            <a class="btn btn-primary" href="<%= context %>/viewChore?id=<%= c.getId() %>">View</a>
                            <% if (!reviewed) { %>
                                <a class="btn btn-secondary" href="<%= context %>/review?choreId=<%= c.getId() %>">Leave Review</a>
                            <% } %>
                        </div>
                    </div>
                <% } %>
            <% } %>
        </section>

    </div>
</body>
</html>