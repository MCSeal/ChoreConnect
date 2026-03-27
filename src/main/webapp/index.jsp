<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>ChoreConnect</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            height: 100vh;
            background: linear-gradient(135deg, #4CAF50, #2E7D32);
            display: flex;
            justify-content: center;
            align-items: center;
            color: white;
        }

        .container {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(12px);
            border-radius: 20px;
            padding: 50px 40px;
            text-align: center;
            box-shadow: 0 8px 30px rgba(0,0,0,0.2);
            width: 350px;
        }

        .logo {
            width: 100px;
            margin-bottom: 20px;
        }

        h1 {
            font-size: 2.2rem;
            margin-bottom: 10px;
        }

        p {
            font-size: 0.95rem;
            margin-bottom: 30px;
            opacity: 0.9;
        }

        .btn {
            display: block;
            width: 100%;
            padding: 12px;
            margin: 10px 0;
            border-radius: 10px;
            border: none;
            font-size: 1rem;
            font-weight: bold;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
        }

        .login-btn {
            background-color: white;
            color: #2E7D32;
        }

        .login-btn:hover {
            background-color: #f1f1f1;
        }

        .register-btn {
            background-color: #1B5E20;
            color: white;
        }

        .register-btn:hover {
            background-color: #145A18;
        }

        .footer {
            margin-top: 20px;
            font-size: 0.8rem;
            opacity: 0.7;
        }
    </style>
</head>
<body>

<div class="container">

    <!-- Logo -->
    <img src="choreconnect-logo.svg" alt="ChoreConnect Logo" class="logo">

    <!-- App Name -->
    <h1>ChoreConnect</h1>

    <!-- Tagline -->
    <p>Organize chores. Share responsibilities. Stay connected.</p>

    <!-- Buttons -->
    <a href="login.jsp" class="btn login-btn">Login</a>
    <a href="register.jsp" class="btn register-btn">Register</a>

    <!-- Footer -->
    <div class="footer">
        © 2026 ChoreConnect
    </div>

</div>

</body>
</html>