<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Chore" %>

<%
    List<Chore> posted = (List<Chore>) request.getAttribute("posted");
    List<Chore> accepted = (List<Chore>) request.getAttribute("accepted");
    List<Chore> completed = (List<Chore>) request.getAttribute("completed");
    String error = (String) request.getAttribute("error");
    String context = request.getContextPath();
%>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>My Chores · ChoreConnect</title>
    <link rel="stylesheet" href="<%= context %>/styles.css">
</head>
<body>
    <div class="container">

        <header class="header">
            <a class="brand" href="<%= context %>/publicChoreList">ChoreConnect</a>

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

            <% if (posted == null || posted.isEmpty()) { %>
                <p>No chores posted.</p>
            <% } else { %>
                <% for (Chore c : posted) { %>
                    <div class="chore-item">

                        <div class="chore-info">
                            <b><%= c.getTitle() %></b>
                            <% if (c.getDescription() != null) { %>
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
            <h2>Chores You Accepted</h2>

            <% if (accepted == null || accepted.isEmpty()) { %>
                <p>No accepted chores.</p>
            <% } else { %>
                <% for (Chore c : accepted) { %>
                    <div class="chore-item">
                        <b><%= c.getTitle() %></b>
                        <br>
                        <a class="btn btn-primary" href="<%= context %>/viewChore?id=<%= c.getId() %>">View</a>
                    </div>
                <% } %>
            <% } %>
        </section>

        <section class="card">
            <h2>Completed Chores</h2>

            <% if (completed == null || completed.isEmpty()) { %>
                <p>No completed chores.</p>
            <% } else { %>
                <% for (Chore c : completed) { %>
                    <div class="chore-item">
                        <b><%= c.getTitle() %></b>
                    </div>
                <% } %>
            <% } %>
        </section>

    </div>
</body>
</html>