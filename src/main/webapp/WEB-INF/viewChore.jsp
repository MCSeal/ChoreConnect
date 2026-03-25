
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="models.Chore" %>
<%@ page import="java.util.Map" %>

<%
    String context = request.getContextPath();
    Chore ch = (Chore) request.getAttribute("chore");
    String userId = (String) request.getAttribute("userId");
    String message = (String) request.getAttribute("message");

    // map of user id -> full name passed from servlet
    Map<String, String> userNames = (Map<String, String>) request.getAttribute("userNames");

    String creatorFirstName = "";
    String acceptedFirstName = "";

    if (ch != null && userNames != null) {
        String creatorFullName = userNames.get(ch.getCreatedBy());
        String acceptedFullName = userNames.get(ch.getAcceptedBy());

        if (creatorFullName != null && !creatorFullName.trim().isEmpty()) {
            creatorFirstName = creatorFullName.contains(" ")
                ? creatorFullName.substring(0, creatorFullName.indexOf(" "))
                : creatorFullName;
        }

        if (acceptedFullName != null && !acceptedFullName.trim().isEmpty()) {
            acceptedFirstName = acceptedFullName.contains(" ")
                ? acceptedFullName.substring(0, acceptedFullName.indexOf(" "))
                : acceptedFullName;
        }
    }
%>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<%= context %>/styles.css">
    <title>View Chore · ChoreConnect</title>
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
                <a class="btn btn-secondary" href="<%= context %>/choreList">My Chores</a>
                <a class="btn btn-secondary" href="<%= context %>/publicChoreList">Public Chores</a>

                <form method="post" action="<%= context %>/signout" style="display:inline">
                    <button class="btn" type="submit">Sign out</button>
                </form>
            </div>
        </header>

    <% if (message != null) { %>
        <div class="notice success">
            <%= message %>
        </div>
    <% } %>

    <% if (ch == null) { %>

        <div class="card notice error">
            Chore not found!
        </div>

        <p>
            <a class="btn btn-secondary" href="<%= context %>/choreList">Back</a>
        </p>

    <% } else { %>

        <div class="card">

            <h2><%= ch.getTitle() %></h2>

            <p>
                <%= ch.getDescription() == null ? "" : ch.getDescription() %>
            </p>

            <p>
                Status:
                <span class="badge <%= ch.getStatus().toLowerCase() %>">
                    <%= ch.getStatus() %>
                </span>
            </p>

            <p>
                Public: <%= ch.isPublic() ? "Yes" : "No" %>
            </p>

            <!-- show creator first name if we found it, otherwise fall back -->
            <p>
                Created by:
                <%= !creatorFirstName.isEmpty() ? creatorFirstName : ch.getCreatedBy() %>
            </p>

            <!-- only show accepted by if someone actually accepted it -->
            <% if (ch.getAcceptedBy() != null && !ch.getAcceptedBy().trim().isEmpty()) { %>
                <p>
                    Accepted by:
                    <%= !acceptedFirstName.isEmpty() ? acceptedFirstName : ch.getAcceptedBy() %>
                </p>
            <% } %>

            <!-- pay info -->
            <p>
                Pay:
                <% if ("HOURLY".equals(ch.getPriceType())) { %>
                    $<%= ch.getHourlyRate() != null ? ch.getHourlyRate() : 0 %>/hour
                    <% if (ch.getHours() != null) { %>
                        for <%= ch.getHours() %> hour<%= ch.getHours() == 1 ? "" : "s" %>
                    <% } %>
                <% } else { %>
                    $<%= ch.getPriceAmount() != null ? ch.getPriceAmount() : 0 %> lump sum
                <% } %>
            </p>

            <% if (userId != null && userId.equals(ch.getCreatedBy())) { %>
                <a class="btn btn-primary top-padding" href="<%= context %>/chore/edit?id=<%= ch.getId() %>">
                    Edit Chore
                </a>
            <% } %>

            <% if (userId != null && !userId.equals(ch.getCreatedBy()) && "OPEN".equals(ch.getStatus())) { %>
                <div class="top-padding">
                    <form method="post" action="<%= context %>/chore/accept" style="display:inline">
                        <input type="hidden" name="choreId" value="<%= ch.getId() %>">
                        <button class="btn btn-primary" type="submit">
                            Accept Chore
                        </button>
                    </form>
                </div>
            <% } %>

            <!-- worker can request completion -->
            <% if (userId != null &&
                   userId.equals(ch.getAcceptedBy()) &&
                   "ACCEPTED".equals(ch.getStatus())) { %>

                <div class="top-padding">
                    <form method="post" action="<%= context %>/viewChore" style="display:inline">
                        <input type="hidden" name="id" value="<%= ch.getId() %>">
                        <input type="hidden" name="action" value="requestCompletion">
                        <button class="btn btn-success" type="submit">
                            Request Completion
                        </button>
                    </form>
                </div>
            <% } %>

            <!-- owner can approve completion -->
            <% if (userId != null &&
                   userId.equals(ch.getCreatedBy()) &&
                   "PENDING_COMPLETION".equals(ch.getStatus())) { %>

                <div class="top-padding">
                    <form method="post" action="<%= context %>/viewChore" style="display:inline">
                        <input type="hidden" name="id" value="<%= ch.getId() %>">
                        <input type="hidden" name="action" value="approveCompletion">
                        <button class="btn btn-success" type="submit">
                            Approve Completion
                        </button>
                    </form>
                </div>

                <p class="muted">
                    the worker marked this chore as finished and it is waiting for your approval.
                </p>
            <% } %>

            <div class="top-padding">
                <a class="btn btn-secondary" href="<%= context %>/choreList">
                    Back to My Chores
                </a>
            </div>

        </div>

    <% } %>

</div>
</body>
</html>