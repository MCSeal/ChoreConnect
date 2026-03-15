<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="models.Chore" %>

<%
    String context = request.getContextPath();
    Chore ch = (Chore) request.getAttribute("chore");
    String message = (String) request.getAttribute("message");
    String error = (String) request.getAttribute("error");

    String title = "";
    String description = "";
    boolean isPublic = true;
    String latitude = "";
    String longitude = "";

    if (request.getAttribute("formTitle") != null) {
        title = (String) request.getAttribute("formTitle");
        description = request.getAttribute("formDescription") != null ? (String) request.getAttribute("formDescription") : "";
        isPublic = request.getAttribute("formIsPublic") != null && (Boolean) request.getAttribute("formIsPublic");
        latitude = request.getAttribute("formLatitude") != null ? (String) request.getAttribute("formLatitude") : "";
        longitude = request.getAttribute("formLongitude") != null ? (String) request.getAttribute("formLongitude") : "";
    } else if (ch != null) {
        title = ch.getTitle() != null ? ch.getTitle() : "";
        description = ch.getDescription() != null ? ch.getDescription() : "";
        isPublic = ch.isPublic();
        latitude = String.valueOf(ch.getLatitude());
        longitude = String.valueOf(ch.getLongitude());
    }
%>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<%= context %>/styles.css">
    <link rel="icon" type="image/svg+xml" href="<%= context %>/favicon.svg">
    <title><%= ch != null ? "Edit Chore" : "Create Chore" %> · ChoreConnect</title>
</head>
<body>

    <header class="header container">
        <div class="brand-logo">
            <img class="brand-logo" src="<%= context %>/choreconnect-logo.svg" alt="ChoreConnect Logo">
        </div>
        <div class="brand-title">ChoreConnect</div>
    </header>

    <main class="container">
        <section class="card">
            <h1><%= ch != null ? "Edit Chore" : "Create New Chore" %></h1>

            <% if (message != null) { %>
                <div class="notice success" style="margin-bottom:12px;"><%= message %></div>
            <% } %>

            <% if (error != null) { %>
                <div class="notice error" style="margin-bottom:12px;"><%= error %></div>
            <% } %>

            <form method="post" action="<%= context %>/chore/edit">

                <% if (ch != null) { %>
                    <input type="hidden" name="id" value="<%= ch.getId() %>">
                <% } %>

                <div class="form-grid">
                    <div>
                        <label for="title">Title</label>
                        <input
                            id="title"
                            class="input"
                            type="text"
                            name="title"
                            required
                            maxlength="200"
                            value="<%= title %>">
                    </div>

                    <div>
                        <label for="description">Description</label>
                        <textarea
                            id="description"
                            class="input"
                            name="description"
                            rows="5"><%= description %></textarea>
                    </div>

                    <div>
                        <label>
                            <input type="checkbox" name="isPublic" <%= isPublic ? "checked" : "" %>>
                            Public
                        </label>
                    </div>

                    <div>
                        <label for="latitude">Latitude</label>
                        <input
                            id="latitude"
                            class="input"
                            type="number"
                            step="0.000001"
                            name="latitude"
                            value="<%= latitude %>">
                    </div>

                    <div>
                        <label for="longitude">Longitude</label>
                        <input
                            id="longitude"
                            class="input"
                            type="number"
                            step="0.000001"
                            name="longitude"
                            value="<%= longitude %>">
                    </div>
                </div>

                <div class="actions" style="margin-top:12px">
                    <button class="btn btn-primary" type="submit">
                        <%= ch != null ? "Update Chore" : "Create Chore" %>
                    </button>
                    <a class="btn btn-secondary" href="<%= context %>/choreList">Back to My Chores</a>
                </div>

            </form>
        </section>

        <p class="footer">© 2026 ChoreConnect</p>
    </main>

</body>
</html>