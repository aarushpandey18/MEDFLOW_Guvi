# MedFlow (Java) - Servlets + JSP + JDBC + MySQL

Requires Java 25 or newer.

## Run
1. MySQL me `sql/schema.sql` run karo.
2. DB credentials set karo (ya default root/root): env vars `DB_URL`, `DB_USER`, `DB_PASS`.
3. `mvn clean package` -> `target/medflow.war`
4. WAR ko **Tomcat 10.1+** ke `webapps/` me daalo -> http://localhost:8080/medflow/

## Rubric mapping (kaunsa concept kahan hai)
| Rubric | File |
|---|---|
| Inheritance | `model/User` -> `Patient`, `Doctor` |
| Polymorphism | `getRole()`, `notifyUser()` overridden; `AppointmentService.book` loops over `List<Notifiable>` |
| Interfaces | `Notifiable`, `GenericDAO<T,ID>` |
| Abstraction | abstract class `User` |
| Exception handling | `InvalidAppointmentException`, `DatabaseException`, try-with-resources, servlet catch blocks |
| Collections & Generics | `List`, `Map<Integer,Doctor>`, `Optional`, `GenericDAO<T,ID>`, `groupByDoctor()` |
| Multithreading & Sync | `ReminderService` (BlockingQueue + worker thread), `synchronized book()` |
| DB operation classes | `dao/PatientDAO`, `DoctorDAO`, `AppointmentDAO` |
| JDBC | `util/DBConnection`, PreparedStatement in every DAO |
| Servlets & Web | `servlet/*` + `webapp/*.jsp` (MVC: Servlet -> DAO/Service -> JSP) |

## Architecture
JSP (view) <-> Servlet (controller) <-> Service (logic) <-> DAO (JDBC) <-> MySQL

## Render deployment
The included `render.yaml` configures Render to build and run the Docker deployment. Set `DB_URL`, `DB_USER`, and `DB_PASS` in the Render service environment before deploying. Render provides the runtime `PORT` automatically.
