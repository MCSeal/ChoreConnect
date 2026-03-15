<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String context = request.getContextPath();
    String error = (String) request.getAttribute("error");

    String title = request.getAttribute("title") != null ? (String) request.getAttribute("title") : "";
    String dueDate = request.getAttribute("dueDate") != null ? (String) request.getAttribute("dueDate") : "";
    String priority = request.getAttribute("priority") != null ? (String) request.getAttribute("priority") : "MEDIUM";
    String notes = request.getAttribute("notes") != null ? (String) request.getAttribute("notes") : "";
    String priceType = request.getAttribute("priceType") != null ? (String) request.getAttribute("priceType") : "LUMP_SUM";
    String hourlyRate = request.getAttribute("hourlyRate") != null ? (String) request.getAttribute("hourlyRate") : "";
    String hours = request.getAttribute("hours") != null ? (String) request.getAttribute("hours") : "";
    String priceAmount = request.getAttribute("priceAmount") != null ? (String) request.getAttribute("priceAmount") : "";
    String latitude = request.getAttribute("latitude") != null ? (String) request.getAttribute("latitude") : "";
    String longitude = request.getAttribute("longitude") != null ? (String) request.getAttribute("longitude") : "";
%>
<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="icon" type="image/svg+xml" href="<%= context %>/favicon.svg">
<title>New Chore · ChoreConnect</title>
<link rel="stylesheet" href="<%= context %>/styles.css">
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
<style>
.toggle-group {
    display: none;
}
.toggle-group.active {
    display: block;
}
</style>
</head>
<body>
    <header class="header container">
        <a class="brand" href="<%= context %>/choreList" aria-label="ChoreConnect">
            <div class="brand-logo" aria-hidden="true">
                <img class="brand-logo" src="<%= context %>/choreconnect-logo.svg" alt="ChoreConnect Logo">
            </div>

            <div class="brand-title">ChoreConnect</div>
        </a>
    </header>

    <main class="container">
        <section class="card" aria-labelledby="create-title">
            <h1 id="create-title">Create a chore</h1>

            <% if (error != null) { %>
                <div class="notice error" role="alert" style="margin-bottom:12px;">
                    <%= error %>
                </div>
            <% } %>

            <form method="POST" action="<%= context %>/createChore">
                <div class="form-grid cols-2">
                    <div>
                        <label for="chore-title">Title</label>
                        <input class="input"
                               id="chore-title"
                               name="title"
                               type="text"
                               maxlength="120"
                               value="<%= title %>"
                               required>
                    </div>

                    <div>
                        <label for="chore-due">Due date</label>
                        <input class="input"
                               id="chore-due"
                               name="dueDate"
                               type="date"
                               value="<%= dueDate %>">
                    </div>

                    <div>
                        <label for="chore-priority">Priority</label>
                        <select class="input" id="chore-priority" name="priority">
                            <option value="LOW" <%= "LOW".equals(priority) ? "selected" : "" %>>Low</option>
                            <option value="MEDIUM" <%= "MEDIUM".equals(priority) ? "selected" : "" %>>Medium</option>
                            <option value="HIGH" <%= "HIGH".equals(priority) ? "selected" : "" %>>High</option>
                        </select>
                    </div>

                    <div style="grid-column: 1/-1">
                        <label for="chore-notes">Notes</label>
                        <textarea class="input"
                                  id="chore-notes"
                                  name="notes"
                                  rows="4"
                                  placeholder="Optional"><%= notes %></textarea>
                    </div>

                    <div class="toggle">
                        <label>
                            <input type="radio"
                                   name="priceType"
                                   value="HOURLY"
                                   id="hourly"
                                   onchange="togglePriceFields()"
                                   <%= "HOURLY".equals(priceType) ? "checked" : "" %>>
                            Hourly
                        </label>

                        <label>
                            <input type="radio"
                                   name="priceType"
                                   value="LUMP_SUM"
                                   id="lump-sum"
                                   onchange="togglePriceFields()"
                                   <%= !"HOURLY".equals(priceType) ? "checked" : "" %>>
                            Lump Sum
                        </label>
                    </div>

                    <div class="toggle-group <%= "HOURLY".equals(priceType) ? "active" : "" %>" id="hourly-group">
                        <div>
                            <label for="chore-hourly-rate">Hourly Rate</label>
                            <input class="input"
                                   id="chore-hourly-rate"
                                   name="hourlyRate"
                                   type="number"
                                   step="0.01"
                                   value="<%= hourlyRate %>">
                        </div>

                        <div>
                            <label for="chore-hours">Number of Hours</label>
                            <input class="input"
                                   id="chore-hours"
                                   name="hours"
                                   type="number"
                                   value="<%= hours %>">
                        </div>
                    </div>

                    <div class="toggle-group <%= !"HOURLY".equals(priceType) ? "active" : "" %>" id="lump-sum-group">
                        <div>
                            <label for="chore-price-amount">Price Amount</label>
                            <input class="input"
                                   id="chore-price-amount"
                                   name="priceAmount"
                                   type="number"
                                   step="0.01"
                                   placeholder="Enter lump sum price"
                                   value="<%= priceAmount %>">
                        </div>
                    </div>

                    <div style="grid-column: 1/-1">
                        <label>Chore Location:</label>
                        <div id="map" style="height: 300px; border-radius: 12px;"></div>
                    </div>

                    <input type="hidden" name="latitude" id="latitude" value="<%= latitude %>">
                    <input type="hidden" name="longitude" id="longitude" value="<%= longitude %>">
                </div>

                <div class="actions" style="margin-top: 12px">
                    <button class="btn btn-primary" type="submit">Save chore</button>
                    <a class="btn btn-secondary" href="<%= context %>/choreList">Back to list</a>
                </div>
            </form>
        </section>
    </main>

    <script>
        function togglePriceFields() {
            const hourlyGroup = document.getElementById('hourly-group');
            const lumpSumGroup = document.getElementById('lump-sum-group');

            const hourlyRate = document.getElementById('chore-hourly-rate');
            const hours = document.getElementById('chore-hours');
            const lumpPrice = document.getElementById('chore-price-amount');

            if (document.getElementById('hourly').checked) {
                hourlyGroup.classList.add('active');
                lumpSumGroup.classList.remove('active');

                hourlyRate.required = true;
                hours.required = true;
                lumpPrice.required = false;
            } else {
                hourlyGroup.classList.remove('active');
                lumpSumGroup.classList.add('active');

                hourlyRate.required = false;
                hours.required = false;
                lumpPrice.required = true;
            }
        }

        const map = L.map('map').setView([45.4215, -75.6972], 12);

        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap'
        }).addTo(map);

        let marker;

        const existingLat = document.getElementById('latitude').value;
        const existingLng = document.getElementById('longitude').value;

        if (existingLat && existingLng) {
            const existingPoint = [parseFloat(existingLat), parseFloat(existingLng)];
            map.setView(existingPoint, 12);
            marker = L.marker(existingPoint).addTo(map);
        }

        map.on('click', function(e) {
            const lat = e.latlng.lat.toFixed(6);
            const lng = e.latlng.lng.toFixed(6);

            if (marker) {
                marker.setLatLng(e.latlng);
            } else {
                marker = L.marker(e.latlng).addTo(map);
            }

            document.getElementById('latitude').value = lat;
            document.getElementById('longitude').value = lng;
        });

        togglePriceFields();
    </script>
</body>
</html>