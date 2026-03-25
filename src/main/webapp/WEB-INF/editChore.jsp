<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="models.Chore" %>

<%
    // basic setup stuff
    String context = request.getContextPath();
    Chore ch = (Chore) request.getAttribute("chore");
    String message = (String) request.getAttribute("message");
    String error = (String) request.getAttribute("error");

    // default values (used for create or fallback)
    String title = "";
    String description = "";
    boolean isPublic = true;
    String latitude = "";
    String longitude = "";

    String priceType = "LUMP_SUM";
    String hourlyRate = "";
    String hours = "";
    String priceAmount = "";

    // if form was submitted and failed, keep what user typed
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

    // otherwise load from db if editing
    } else if (ch != null) {
        title = ch.getTitle() != null ? ch.getTitle() : "";
        description = ch.getDescription() != null ? ch.getDescription() : "";
        isPublic = ch.isPublic();
        latitude = String.valueOf(ch.getLatitude());
        longitude = String.valueOf(ch.getLongitude());

        priceType = ch.getPriceType() != null ? ch.getPriceType() : "LUMP_SUM";
        hourlyRate = ch.getHourlyRate() != null ? String.valueOf(ch.getHourlyRate()) : "";
        hours = ch.getHours() != null ? String.valueOf(ch.getHours()) : "";
        priceAmount = ch.getPriceAmount() != null ? String.valueOf(ch.getPriceAmount()) : "";
    }

    // only allow editing price if still open
    boolean canEditPrice = (ch == null) || "OPEN".equals(ch.getStatus());
%>

<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="<%= context %>/styles.css">
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
<title>Edit Chore · ChoreConnect</title>
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
<!-- keep id hidden so we know what we're editing -->
<input type="hidden" name="id" value="<%= ch.getId() %>">
<% } %>

<div class="form-grid cols-2">

<!-- title -->
<div>
<label>Title</label>
<input class="input" type="text" name="title" value="<%= title %>" required>
</div>

<!-- only show price type if still editable -->
<% if (canEditPrice) { %>
<div>
<label>Price Type</label>
<select class="input" name="priceType">
<option value="HOURLY" <%= "HOURLY".equals(priceType) ? "selected" : "" %>>Hourly</option>
<option value="LUMP_SUM" <%= "LUMP_SUM".equals(priceType) ? "selected" : "" %>>Lump Sum</option>
</select>
</div>
<% } %>

<!-- description -->
<div style="grid-column:1/-1">
<label>Description</label>
<textarea class="input" name="description"><%= description %></textarea>
</div>

<!-- price section -->
<% if (canEditPrice) { %>

    <!-- hourly pricing -->
    <% if ("HOURLY".equals(priceType)) { %>

    <div>
    <label>Hourly Rate</label>
    <input
        class="input"
        type="number"
        step="0.01"
        name="hourlyRate"
        value="<%= hourlyRate %>"
        placeholder="<%= hourlyRate.isEmpty() ? "e.g. 25.00" : hourlyRate %>">
    </div>

    <div>
    <label>Hours</label>
    <input
        class="input"
        type="number"
        name="hours"
        value="<%= hours %>"
        placeholder="<%= hours.isEmpty() ? "e.g. 2" : hours %>">
    </div>

    <% } else { %>

    <!-- flat price -->
    <div>
    <label>Total Price</label>
    <input
        class="input"
        type="number"
        step="0.01"
        name="priceAmount"
        value="<%= priceAmount %>"
        placeholder="<%= priceAmount.isEmpty() ? "e.g. 50.00" : priceAmount %>">
    </div>

    <% } %>

<% } else { %>

<!-- once accepted, just show it -->
<div style="grid-column:1/-1">
<p class="muted">price is locked after someone accepts the chore</p>

<p>
<b>type:</b> <%= priceType %><br>

<% if ("HOURLY".equals(priceType)) { %>
$<%= hourlyRate %> × <%= hours %> hours
<% } else { %>
$<%= priceAmount %>
<% } %>

</p>
</div>

<% } %>

<!-- public toggle -->
<div>
<label>
<input type="checkbox" name="isPublic" <%= isPublic ? "checked" : "" %>>
Public
</label>
</div>

<!-- map -->
<div style="grid-column:1/-1">
<label>Location</label>
<div id="map" style="height:300px;border-radius:12px;"></div>
</div>

<!-- hidden lat/lng updated by map -->
<input type="hidden" id="latitude" name="latitude" value="<%= latitude %>">
<input type="hidden" id="longitude" name="longitude" value="<%= longitude %>">

</div>

<div class="actions">
<button class="btn btn-primary">Save</button>
<a class="btn btn-secondary" href="<%= context %>/choreList">Cancel</a>
</div>

</form>

</section>

</div>

<script>
// basic leaflet setup (same as create page)
const map = L.map('map').setView([45.4215, -75.6972], 12);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);

let marker;

// if chore already has location, show it
const lat = document.getElementById('latitude').value;
const lng = document.getElementById('longitude').value;

if (lat && lng && lat !== "0.0") {
    const point = [parseFloat(lat), parseFloat(lng)];
    map.setView(point, 12);
    marker = L.marker(point).addTo(map);
}

// click to set/update location
map.on('click', function(e) {
    if (marker) {
        marker.setLatLng(e.latlng);
    } else {
        marker = L.marker(e.latlng).addTo(map);
    }

    document.getElementById('latitude').value = e.latlng.lat.toFixed(6);
    document.getElementById('longitude').value = e.latlng.lng.toFixed(6);
});
</script>

</body>
</html>