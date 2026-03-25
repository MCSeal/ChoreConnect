<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    String context = request.getContextPath();
    String error = (String) request.getAttribute("error");
    String choreId = (String) request.getAttribute("choreId");
    String rating = (String) request.getAttribute("rating");
    String comment = (String) request.getAttribute("comment");
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

        <% if (choreId != null && (error == null || rating != null || comment != null)) { %>
            <form method="post" action="<%= context %>/review">

                <input type="hidden" name="choreId" value="<%= choreId %>">

                <div class="form-grid cols-2">

                    <div>
                        <label for="rating">Rating (1-5)</label>
                        <input
                            class="input"
                            id="rating"
                            type="number"
                            name="rating"
                            min="1"
                            max="5"
                            required
                            value="<%= rating != null ? rating : "" %>">
                    </div>

                    <div style="grid-column: 1/-1">
                        <label for="comment">Comment</label>
                        <textarea
                            class="input"
                            id="comment"
                            name="comment"
                            rows="5"
                            placeholder="Optional review comment"><%= comment != null ? comment : "" %></textarea>
                    </div>

                </div>

                <div class="actions" style="margin-top:12px">
                    <button class="btn btn-primary" type="submit">Submit Review</button>
                    <a class="btn btn-secondary" href="<%= context %>/choreList">Cancel</a>
                </div>

            </form>
        <% } else if (choreId != null && error == null) { %>
            <form method="post" action="<%= context %>/review">

                <input type="hidden" name="choreId" value="<%= choreId %>">

                <div class="form-grid cols-2">

                    <div>
                        <label for="rating">Rating (1-5)</label>
                        <input
                            class="input"
                            id="rating"
                            type="number"
                            name="rating"
                            min="1"
                            max="5"
                            required>
                    </div>

                    <div style="grid-column: 1/-1">
                        <label for="comment">Comment</label>
                        <textarea
                            class="input"
                            id="comment"
                            name="comment"
                            rows="5"
                            placeholder="Optional review comment"></textarea>
                    </div>

                </div>

                <div class="actions" style="margin-top:12px">
                    <button class="btn btn-primary" type="submit">Submit Review</button>
                    <a class="btn btn-secondary" href="<%= context %>/choreList">Cancel</a>
                </div>

            </form>
        <% } else { %>
            <div class="actions">
                <a class="btn btn-secondary" href="<%= context %>/choreList">Back to My Chores</a>
            </div>
        <% } %>

    </section>

</div>
</body>
</html>