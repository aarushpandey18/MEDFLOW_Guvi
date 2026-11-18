<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="activePage" value="dashboard"/>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Dashboard | MedFlow</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=dark-theme-20261118">
</head>
<body>
<%@ include file="/WEB-INF/includes/header.jspf" %>

<main class="page-shell">
  <div class="container">
    <section class="hero" aria-labelledby="dashboard-title">
      <p class="hero-label">Clinic Management Portal</p>
      <h1 id="dashboard-title">Welcome to MedFlow</h1>
      <p>Manage patient records and appointments from one clear, dependable workspace.</p>
    </section>

    <section class="card-grid" aria-label="Clinic areas">
      <article class="card info-card">
        <span class="info-card-icon" aria-hidden="true">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
            <circle cx="12" cy="8" r="3.2"/><path d="M5.5 20c.7-3.5 2.8-5.3 6.5-5.3s5.8 1.8 6.5 5.3M19 5v6M16 8h6"/>
          </svg>
        </span>
        <h2>Patients</h2>
        <p>Review patient records and keep contact information current.</p>
        <a class="button" href="${pageContext.request.contextPath}/patients">Manage patients</a>
      </article>

      <article class="card info-card">
        <span class="info-card-icon" aria-hidden="true">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
            <circle cx="12" cy="7" r="3"/><path d="M5.5 20c.8-3.4 2.9-5.1 6.5-5.1s5.7 1.7 6.5 5.1M19 4v6M16 7h6"/>
          </svg>
        </span>
        <h2>Doctors</h2>
        <p>View the clinicians available for appointment scheduling.</p>
        <a class="button" href="${pageContext.request.contextPath}/appointments">View doctors</a>
      </article>

      <article class="card info-card">
        <span class="info-card-icon" aria-hidden="true">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
            <rect x="4" y="5" width="16" height="15" rx="2"/><path d="M8 3v4M16 3v4M4 10h16M8 14h3M8 17h5"/>
          </svg>
        </span>
        <h2>Appointments</h2>
        <p>Book and review appointments for patients and doctors.</p>
        <a class="button button--primary" href="${pageContext.request.contextPath}/appointments">Manage appointments</a>
      </article>
    </section>
  </div>
</main>

<jsp:include page="/WEB-INF/includes/footer.jspf"/>
<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
