<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="models.Chore" %>

<%
    String context = request.getContextPath();
    Chore ch = (Chore) request.getAttribute("chore");
    String message = (String) request.getAttribute("message");
    String error = (String) request.getAttribute("error");

    String title = "";
    String description = "";
    boolean isPublic = true;
    String latitude = "";
    String longitude = "";

    String priceType = "LUMP_SUM";
    String hourlyRate = "";
    String hours = "";
    String priceAmount = "";

    // if the form failed, keep what the user typed
    if (request.getAttribute("formTitle") != null) {
        title = (String) request.getAttribute("formTitle");
        description = (String) request.getAttribute("formDescription");
        isPublic = request.getAttribute("formIsPublic") != null && (Boolean) request.getAttribute("formIsPublic");
        latitude = (String) request.getAttribute("formLatitude");
        longitude = (String) request.getAttribute("formLongitude");

        priceType = request.getAttribute("formPriceType") != null ? (String) request.getAttribute("formPriceType") : "LUMP_SUM";
        hourlyRate = request.getAttribute("formHourlyRate") != null ? (String) request.getAttribute("formHourlyRate") : "";
        hours = request.getAttribute("formHours") != null ? (String) request.getAttribute("formHours") : "";
        priceAmount = request.getAttribute("formPriceAmount") != null ? (String) request.getAttribute("formPriceAmount") : "";

    } else if (ch != null) {
        title = ch.getTitle() != null ? ch.getTitle() : "";
        description = ch.getDescription() != null ? ch.getDescription() : "";
        isPublic = ch.isPublic();
        latitude = String.valueOf(ch.getLatitude());
        longitude = String.valueOf(ch.getLongitude());

        priceType = ch.getPriceType() != null ? ch.getPriceType() : "LUMP_SUM";

        // format money to 2 decimals so edit values stay clean
        hourlyRate = ch.getHourlyRate() != null ? String.format("%.2f", ch.getHourlyRate()) : "";
        hours = ch.getHours() != null ? String.valueOf(ch.getHours()) : "";
        priceAmount = ch.getPriceAmount() != null ? String.format("%.2f", ch.getPriceAmount()) : "";
    }

    boolean canEditPrice = (ch == null) || "OPEN".equals(ch.getStatus());
    boolean isHourly = "HOURLY".equals(priceType);
%>

<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="<%= context %>/styles.css">
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
<title><%= ch != null ? "Edit Chore" : "Create Chore" %> · ChoreConnect</title>
</head>

<body>
<div class="container">

    <header class="header container">
        <a class="brand" href="<%= context %>/choreList" aria-label="ChoreConnect">
            <div class="brand-logo" aria-hidden="true">
                <img class="brand-logo" src="<%= context %>/choreconnect-logo.svg" alt="ChoreConnect Logo">
            </div>
            <div class="brand-title">ChoreConnect</div>
        </a>

        <div class="actions">
            <a class="btn btn-secondary" href="<%= context %>/choreList">My Chores</a>
            <a class="btn btn-secondary" href="<%= context %>/publicChoreList">Public Chores</a>

            <form method="post" action="<%= context %>/signout" style="display:inline">
                <button class="btn" type="submit">Sign out</button>
            </form>
        </div>
    </header>

    <section class="card">
        <h2><%= ch != null ? "Edit Chore" : "Create Chore" %></h2>

        <% if (message != null) { %>
            <div class="notice success"><%= message %></div>
        <% } %>

        <% if (error != null) { %>
            <div class="notice error"><%= error %></div>
        <% } %>

        <form method="post" action="<%= context %>/chore/edit">

            <% if (ch != null) { %>
                <input type="hidden" name="id" value="<%= ch.getId() %>">
            <% } %>

            <div class="form-grid cols-2">

                <div>
                    <label for="title">Title</label>
                    <input class="input" id="title" type="text" name="title" value="<%= title %>" required>
                </div>

                <% if (canEditPrice) { %>
                <div>
                    <label for="priceType">Price Type</label>
                    <select class="input" id="priceType" name="priceType">
                        <option value="HOURLY" <%= "HOURLY".equals(priceType) ? "selected" : "" %>>Hourly</option>
                        <option value="LUMP_SUM" <%= "LUMP_SUM".equals(priceType) ? "selected" : "" %>>Lump Sum</option>
                    </select>
                </div>
                <% } %>

                <div style="grid-column:1/-1">
                    <label for="description">Description</label>
                    <textarea class="input" id="description" name="description"><%= description %></textarea>
                </div>

                <% if (canEditPrice) { %>

                    <div id="hourlyFields" style="<%= isHourly ? "" : "display:none;" %>">
                        <label for="hourlyRate">Hourly Rate</label>
                        <input
                            class="input"
                            id="hourlyRate"
                            type="number"
                            step="1.00"
                            min="0"
                            name="hourlyRate"
                            value="<%= hourlyRate %>"
                            placeholder="e.g. 25.00">
                    </div>

                    <div id="hoursField" style="<%= isHourly ? "" : "display:none;" %>">
                        <label for="hours">Hours</label>
                        <input
                            class="input"
                            id="hours"
                            type="number"
                            min="1"
                            name="hours"
                            value="<%= hours %>"
                            placeholder="e.g. 2">
                    </div>

                    <div id="lumpSumField" style="<%= isHourly ? "display:none;" : "" %>">
                        <label for="priceAmount">Total Price</label>
                        <input
                            class="input"
                            id="priceAmount"
                            type="number"
                            step="1.00"
                            min="0"
                            name="priceAmount"
                            value="<%= priceAmount %>"
                            placeholder="e.g. 50.00">
                    </div>

                <% } else { %>

                    <div style="grid-column:1/-1">
                        <p class="muted">Price can no longer be changed after the chore has been accepted.</p>

                        <p>
                            <b>Type:</b> <%= priceType %><br>

                            <% if ("HOURLY".equals(priceType)) { %>
                                $<%= hourlyRate %> × <%= hours %> hour(s)
                            <% } else { %>
                                $<%= priceAmount %>
                            <% } %>
                        </p>
                    </div>

                <% } %>

                <div>
                    <label>
                        <input type="checkbox" name="isPublic" <%= isPublic ? "checked" : "" %>>
                        Public
                    </label>
                </div>

                <div style="grid-column:1/-1">
                    <label>Location</label>
                    <div id="map" style="height:300px;border-radius:12px;"></div>
                    <p class="muted" style="margin-top:8px;">Click on the map to set or update the chore location.</p>
                </div>

                <input type="hidden" id="latitude" name="latitude" value="<%= latitude %>">
                <input type="hidden" id="longitude" name="longitude" value="<%= longitude %>">

            </div>

            <div class="actions">
                <button class="btn btn-primary" type="submit">
                    <%= ch != null ? "Save Changes" : "Create Chore" %>
                </button>
                <a class="btn btn-secondary" href="<%= context %>/choreList">Cancel</a>
            </div>

        </form>
    </section>

</div>

<script>
const map = L.map('map').setView([45.4215, -75.6972], 12);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);

let marker;

const lat = document.getElementById('latitude').value;
const lng = document.getElementById('longitude').value;

if (lat && lng && lat !== "0.0") {
    const point = [parseFloat(lat), parseFloat(lng)];
    map.setView(point, 12);
    marker = L.marker(point).addTo(map);
}

map.on('click', function(e) {
    if (marker) {
        marker.setLatLng(e.latlng);
    } else {
        marker = L.marker(e.latlng).addTo(map);
    }

    document.getElementById('latitude').value = e.latlng.lat.toFixed(6);
    document.getElementById('longitude').value = e.latlng.lng.toFixed(6);
});

// switch hourly vs lump sum without reloading
const priceTypeSelect = document.getElementById('priceType');
const hourlyFields = document.getElementById('hourlyFields');
const hoursField = document.getElementById('hoursField');
const lumpSumField = document.getElementById('lumpSumField');

function updatePriceFields() {
    if (!priceTypeSelect) return;

    if (priceTypeSelect.value === 'HOURLY') {
        hourlyFields.style.display = '';
        hoursField.style.display = '';
        lumpSumField.style.display = 'none';
    } else {
        hourlyFields.style.display = 'none';
        hoursField.style.display = 'none';
        lumpSumField.style.display = '';
    }
}

if (priceTypeSelect) {
    priceTypeSelect.addEventListener('change', updatePriceFields);
    updatePriceFields();
}
</script>

</body>
</html>