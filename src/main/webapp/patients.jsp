<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="activePage" value="patients"/>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Patients | MedFlow</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=dark-theme-20261118">
</head>
<body>
<%@ include file="/WEB-INF/includes/header.jspf" %>

<main class="page-shell">
  <div class="container">
    <header class="page-heading">
      <h1>Patients</h1>
      <p>Maintain accurate records for everyone receiving care at the clinic.</p>
    </header>

    <c:if test="${not empty error}">
      <div class="alert alert--error" role="alert"><c:out value="${error}"/></div>
    </c:if>

    <div class="content-grid">
      <section class="card form-card" aria-labelledby="add-patient-title">
        <div class="card-heading">
          <div>
            <h2 id="add-patient-title">Add Patient</h2>
            <p>Enter the patient’s contact details.</p>
          </div>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/patients">
          <div class="form-grid">
            <div class="form-field">
              <label for="patient-name">Full name</label>
              <input id="patient-name" name="name" type="text" autocomplete="name" required>
            </div>
            <div class="form-field">
              <label for="patient-email">Email address</label>
              <input id="patient-email" name="email" type="email" autocomplete="email" required>
            </div>
            <div class="form-field">
              <label for="patient-age">Age</label>
              <input id="patient-age" name="age" type="number" min="0" max="120" required>
            </div>
            <div class="form-field">
              <label for="patient-phone">Phone number</label>
              <input id="patient-phone" name="phone" type="tel" autocomplete="tel">
            </div>
          </div>
          <div class="form-actions">
            <button class="button button--primary" type="submit">Add patient</button>
          </div>
        </form>
      </section>

      <section class="card table-card" aria-labelledby="patient-list-title">
        <div class="card-heading">
          <div>
            <h2 id="patient-list-title">Patient records</h2>
            <p>Current patients registered with the clinic.</p>
          </div>
        </div>
        <div class="table-wrap">
          <table class="data-table">
            <thead>
              <tr><th scope="col">Patient</th><th scope="col">Email</th><th scope="col">Age</th><th scope="col">Phone</th><th scope="col"><span class="sr-only">Actions</span></th></tr>
            </thead>
            <tbody>
              <c:choose>
                <c:when test="${not empty patients}">
                  <c:forEach var="p" items="${patients}">
                    <tr>
                      <td>
                        <div class="patient-cell">
                          <span class="avatar" aria-hidden="true"><c:out value="${p.name.substring(0, 1).toUpperCase()}"/></span>
                          <span><c:out value="${p.name}"/></span>
                        </div>
                      </td>
                      <td><c:out value="${p.email}"/></td>
                      <td><c:out value="${p.age}"/></td>
                      <td><c:out value="${p.phone}"/></td>
                      <td>
                        <form method="post" action="${pageContext.request.contextPath}/patients" data-confirm-delete>
                          <input type="hidden" name="action" value="delete">
                          <input type="hidden" name="id" value="${p.id}">
                          <button class="button button--danger" type="submit">Delete</button>
                        </form>
                      </td>
                    </tr>
                  </c:forEach>
                </c:when>
                <c:otherwise>
                  <tr><td class="empty-state" colspan="5">No patient records are available yet.</td></tr>
                </c:otherwise>
              </c:choose>
            </tbody>
          </table>
        </div>
      </section>
    </div>
  </div>
</main>

<jsp:include page="/WEB-INF/includes/footer.jspf"/>
<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
