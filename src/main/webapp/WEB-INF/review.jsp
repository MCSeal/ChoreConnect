<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    String context = request.getContextPath();
    String error = (String) request.getAttribute("error");
    String choreId = (String) request.getAttribute("choreId");
%>

<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Review Worker · ChoreConnect</title>
<link rel="stylesheet" href="<%= context %>/styles.css">
</head>

<body>

<div class="container">

<header class="header">
<a class="brand" href="<%= context %>/choreList">ChoreConnect</a>
</header>

<section class="card">

<h2>Review Worker</h2>

<% if (error != null) { %>
<div class="notice error">
<%= error %>
</div>
<% } %>

<% if (choreId != null && error == null) { %>

<form method="post" action="<%= context %>/review">

<input type="hidden" name="choreId" value="<%= choreId %>">

<div class="form-grid">

<div>
<label>Rating (1-5)</label>
<input
class="input"
type="number"
name="rating"
min="1"
max="5"
required>
</div>

<div style="grid-column: 1/-1">
<label>Comment</label>
<textarea
class="input"
name="comment"
rows="5"
placeholder="Optional review comment"></textarea>
</div>

</div>

<div class="actions" style="margin-top:12px">

<button class="btn btn-primary" type="submit">
Submit Review
</button>

<a class="btn btn-secondary" href="<%= context %>/choreList">
Cancel
</a>

</div>

</form>

<% } %>

</section>

</div>

</body>
</html>