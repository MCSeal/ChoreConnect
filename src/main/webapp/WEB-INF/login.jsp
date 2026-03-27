<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="icon" type="image/svg+xml" href="<%= request.getContextPath() %>/favicon.svg">
  <title>Login · ChoreConnect</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/styles.css">
</head>
<body>
  <header class="header container">
    <div class="brand-logo" aria-hidden="true">
      <img class="brand-logo" src="<%= request.getContextPath() %>/choreconnect-logo.svg" alt="ChoreConnect Logo">
    </div>

    <div class="brand-title">ChoreConnect</div>
  </header>

  <main class="container">
    <section class="card" aria-labelledby="login-title">
      <h1 id="login-title">Sign in</h1>

      <% String error = (String) request.getAttribute("error"); %>
      <% if (error != null) { %>
        <div class="notice error" role="alert" style="margin-bottom: 12px;">
          <%= error %>
        </div>
      <% } %>

      <form action="<%= request.getContextPath() %>/login" method="POST">
        <div class="form-grid">

          <div>
            <label for="login-email">Email</label>
            <input
              class="input"
              id="login-email"
              name="email"
              type="email"
              required
              value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>">
          </div>

          <div>
            <label for="login-password">Password</label>
            <input
              class="input"
              id="login-password"
              name="password"
              type="password"
              minlength="4"
              required>
          </div>

        </div>

        <div class="actions" style="margin-top:12px">
          <button class="btn btn-primary" type="submit">Sign in</button>
        </div>
      </form>

      <p>Don't have an account? <a href="<%= request.getContextPath() %>/register">Create account</a></p>
    </section>

    <p class="footer">© 2026 ChoreConnect</p>
  </main>
</body>
</html>