(function () {
  "use strict";

  var menuButton = document.querySelector(".menu-toggle");
  var navigation = document.getElementById("main-nav");

  if (menuButton && navigation) {
    menuButton.addEventListener("click", function () {
      var isOpen = navigation.classList.toggle("is-open");
      menuButton.setAttribute("aria-expanded", String(isOpen));
    });
  }

  document.querySelectorAll("[data-confirm-delete]").forEach(function (form) {
    form.addEventListener("submit", function (event) {
      if (!window.confirm("Are you sure you want to delete this patient?")) {
        event.preventDefault();
      }
    });
  });

  document.querySelectorAll("[data-current-year]").forEach(function (element) {
    element.textContent = new Date().getFullYear();
  });
}());
