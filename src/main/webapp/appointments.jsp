<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="activePage" value="appointments"/>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Appointments | MedFlow</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=dark-theme-20261118">
</head>
<body>
<%@ include file="/WEB-INF/includes/header.jspf" %>

<main class="page-shell">
  <div class="container">
    <header class="page-heading">
      <h1>Appointments</h1>
      <p>Schedule clinic visits and keep the day’s care plan organized.</p>
    </header>

    <c:if test="${not empty error}">
      <div class="alert alert--error" role="alert"><c:out value="${error}"/></div>
    </c:if>

    <div class="content-grid">
      <section class="card form-card" aria-labelledby="book-appointment-title">
        <div class="card-heading">
          <div>
            <h2 id="book-appointment-title">Book Appointment</h2>
            <p>Select a patient, clinician, and visit time.</p>
          </div>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/appointments">
          <div class="form-grid">
            <div class="form-field">
              <label for="appointment-patient">Patient</label>
              <select id="appointment-patient" name="patientId" required>
                <option value="">Select patient</option>
                <c:forEach var="p" items="${patients}">
                  <option value="${p.id}"><c:out value="${p.name}"/></option>
                </c:forEach>
              </select>
            </div>
            <div class="form-field">
              <label for="appointment-doctor">Doctor</label>
              <select id="appointment-doctor" name="doctorId" required>
                <option value="">Select doctor</option>
                <c:forEach var="d" items="${doctors}">
                  <option value="${d.id}"><c:out value="${d.name}"/> — <c:out value="${d.specialization}"/></option>
                </c:forEach>
              </select>
            </div>
            <div class="form-field">
              <label for="appointment-time">Date and time</label>
              <input id="appointment-time" type="datetime-local" name="time" required>
            </div>
          </div>
          <div class="form-actions">
            <button class="button button--primary" type="submit">Book appointment</button>
          </div>
        </form>
      </section>

      <section class="card table-card" aria-labelledby="appointment-list-title">
        <div class="card-heading">
          <div>
            <h2 id="appointment-list-title">Appointment schedule</h2>
            <p>Review scheduled and completed visits.</p>
          </div>
        </div>
        <c:choose>
          <c:when test="${not empty appointments}">
            <div class="table-wrap">
              <table class="data-table">
                <thead>
                  <tr><th scope="col">ID</th><th scope="col">Patient ID</th><th scope="col">Doctor ID</th><th scope="col">Date and time</th><th scope="col">Status</th></tr>
                </thead>
                <tbody>
                  <c:forEach var="a" items="${appointments}">
                    <tr>
                      <td><c:out value="${a.id}"/></td>
                      <td><c:out value="${a.patientId}"/></td>
                      <td><c:out value="${a.doctorId}"/></td>
                      <td><c:out value="${a.timeText}"/></td>
                      <td>
                        <c:choose>
                          <c:when test="${a.status == 'BOOKED'}"><span class="badge badge--booked"><c:out value="${a.status}"/></span></c:when>
                          <c:when test="${a.status == 'COMPLETED'}"><span class="badge badge--completed"><c:out value="${a.status}"/></span></c:when>
                          <c:when test="${a.status == 'CANCELLED'}"><span class="badge badge--cancelled"><c:out value="${a.status}"/></span></c:when>
                          <c:otherwise><span class="badge badge--cancelled"><c:out value="${a.status}"/></span></c:otherwise>
                        </c:choose>
                      </td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
          </c:when>
          <c:otherwise>
            <div class="empty-state" role="status">No appointments have been scheduled yet.</div>
          </c:otherwise>
        </c:choose>
      </section>
    </div>
  </div>
</main>

<jsp:include page="/WEB-INF/includes/footer.jspf"/>
<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
