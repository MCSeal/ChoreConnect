<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="models.Chore" %>

<%
    String context = request.getContextPath();
    Chore ch = (Chore) request.getAttribute("chore");
    String userId = (String) request.getAttribute("userId");
    String message = (String) request.getAttribute("message");
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

<header class="header">

<a class="brand" href="<%= context %>/publicChoreList">
ChoreConnect
</a>

<form method="post" action="<%= context %>/signout">
<button class="btn" type="submit">Sign out</button>
</form>

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
<a class="btn btn-secondary" href="<%= context %>/choreList">
Back
</a>
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

<p>
Created by: <%= ch.getCreatedBy() %>
</p>

<% if (userId != null && userId.equals(ch.getCreatedBy())) { %>

<a class="btn btn-primary"
href="<%= context %>/chore/edit?id=<%= ch.getId() %>">
Edit Chore
</a>

<% } else if (userId != null && !userId.equals(ch.getCreatedBy()) && "OPEN".equals(ch.getStatus())) { %>

<form method="post" action="<%= context %>/chore/accept" style="display:inline">

<input type="hidden" name="choreId" value="<%= ch.getId() %>">

<button class="btn btn-primary" type="submit">
Accept Chore
</button>

</form>

<% } %>

<% if (userId != null &&
       userId.equals(ch.getAcceptedBy()) &&
       "ACCEPTED".equals(ch.getStatus())) { %>

<form method="post"
action="<%= context %>/viewChore"
style="display:inline;margin-left:10px">

<input type="hidden" name="id" value="<%= ch.getId() %>">
<input type="hidden" name="action" value="markDone">

<button class="btn btn-success" type="submit">
Mark Done
</button>

</form>

<% } %>

<p style="margin-top:1rem;">

<a class="btn btn-secondary"
href="<%= context %>/choreList">

Back to My Chores

</a>

</p>

</div>

<% } %>

</div>
</body>
</html>