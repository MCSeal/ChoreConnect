<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="icon" type="image/svg+xml" href="<%= request.getContextPath() %>/favicon.svg">
  <title>Register · ChoreConnect</title>
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
    <section class="card" aria-labelledby="register-title">
      <h1 id="register-title">Create your account</h1>

      <% String error = (String) request.getAttribute("error"); %>
      <% if (error != null) { %>
        <div class="notice error" role="alert" style="margin-bottom:12px;">
          <%= error %>
        </div>
      <% } %>

      <form action="<%= request.getContextPath() %>/register" method="POST">
        <div class="form-grid cols-2">
          <div>
            <label for="reg-name">Full name</label>
            <input
              class="input"
              id="reg-name"
              name="fullName"
              type="text"
              required
              value="<%= request.getAttribute("fullName") != null ? request.getAttribute("fullName") : "" %>">
          </div>

          <div>
            <label for="reg-email">Email</label>
            <input
              class="input"
              id="reg-email"
              name="email"
              type="email"
              required
              value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>">
          </div>

          <div>
            <label for="reg-password">Password</label>
            <input
              class="input"
              id="reg-password"
              name="password"
              type="password"
              minlength="8"
              required>
          </div>

          <div>
            <label for="reg-confirm">Confirm password</label>
            <input
              class="input"
              id="reg-confirm"
              name="confirmPassword"
              type="password"
              minlength="8"
              required>
          </div>
        </div>

        <div class="actions" style="margin-top:12px">
          <button class="btn btn-primary" type="submit">Create account</button>
        </div>
      </form>

      <p>Already have an account? <a href="<%= request.getContextPath() %>/login" class="btn btn-secondary">Sign in</a></p>
    </section>
  </main>
</body>
</html>