<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String context = request.getContextPath();
    String message = (String) request.getAttribute("message");
    String messageType = (String) request.getAttribute("messageType");
%>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<%= context %>/styles.css">
    <title>Chore Accepted · ChoreConnect</title>
</head>
<body>
    <div class="container">

        <header class="header">
            <a class="brand" href="<%= context %>/publicChoreList">ChoreConnect</a>

            <nav class="actions">
                <form method="post" action="<%= context %>/signout" style="display:inline;">
                    <button class="btn" type="submit">Sign out</button>
                </form>
            </nav>
        </header>

        <div class="card notice <%= messageType %>">
            <%= message %>
        </div>

        <p>
            <a class="btn btn-primary" href="<%= context %>/choreList">Go to My Chores</a>
        </p>

        <p>
            <a class="btn btn-secondary" href="<%= context %>/publicChoreList">Back to Public Chores</a>
        </p>

    </div>
</body>
</html>