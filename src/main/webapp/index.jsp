<%@ page contentType="text/html;charset=UTF-8" language="java"%>
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
	background: linear-gradient(135deg, #2DD4BF, #0F766E);
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
    transition: all 0.3s ease;
}

.container:hover {
    box-shadow: 0 10px 40px rgba(45, 212, 191, 0.4);
    transform: translateY(-3px);
}

.logo {
    width: 100px;
    margin-bottom: 20px;
    transition: all 0.3s ease;
}

.logo:hover {
    filter: drop-shadow(0 0 10px rgba(45, 212, 191, 0.7));
    transform: scale(1.05);
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
	color: #0F766E;
}

.register-btn {
	background-color: #2DD4BF;
	color: white;
}

.login-btn:hover {
    background-color: #ecfeff;
    box-shadow: 0 0 15px rgba(45, 212, 191, 0.5);
    transform: translateY(-2px);
}

.register-btn:hover {
    background-color: #14B8A6;
    box-shadow: 0 0 20px rgba(45, 212, 191, 0.7);
    transform: translateY(-2px);
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
		<a href="<%=request.getContextPath()%>/login" class="btn login-btn">Login</a> 
		<a href="<%=request.getContextPath()%>/register" class="btn register-btn">Register</a>

		<!-- Footer -->
		<div class="footer">© 2026 ChoreConnect</div>

	</div>

</body>
</html>