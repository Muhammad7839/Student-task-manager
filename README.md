
# Student Task Manager

Student Task Manager is a simple system to help instructors and students keep track of student tasks and their completion status. It stores student records (id, name, class, task, status) in a Firebase Firestore database and provides a Java backend and JavaFX frontend to add, view, update, and delete tasks.

This project is for CSC 325 – Software Engineering.

---

## 1. Project Goals

- Help instructors quickly see which students have completed their tasks.
- Give a clear list of tasks for each student (homework, lab reports, projects, etc.).
- Use a real backend with Firebase instead of only local data.
- Follow good software engineering structure (models, repository, config, separation of concerns).
- Provide a clean JavaFX dashboard where students can log in, see tasks, and manage them.

---

## 2. Features (current)

### Backend features

- Add a student task into the system.
- View all students and their tasks.
- Update the status of a task (for example, from "Incomplete" to "Complete").
- Delete a student record from the system.
- Store data in Firebase Cloud Firestore so it is not lost when the program ends.

### Frontend (JavaFX) features

- Login / signup screens with basic validation.
- Personalized sidebar showing the logged-in student’s name.
- Dashboard with:
    - Overall progress summary.
    - Quick task creation box.
    - Alerts for tasks due today and overdue tasks.
- Tasks screen with:
    - Table of tasks (title, course, due date, priority, status).
    - Filters by status and priority.
    - Buttons to view details, edit, and delete tasks.
- Add / edit task form:
    - Title, course/context, due date, priority, status, notes.
    - Syncs changes to Firebase through the backend service layer.

### Planned or possible future features

- Search or filter by class name.
- Filter students by status (only "Incomplete" or only "Complete").
- Record when a task was created or completed (timestamps).
- More analytics and reports on the dashboard (per-class completion, weekly trends).
- Simple web interface on top of the same backend.

---

## 3. Technology Stack

- Language: Java 17
- Build tool: Maven
- Backend: Firebase Admin SDK with Cloud Firestore
- Frontend: JavaFX (FXML, controllers, styles)
- Icons: Ikonli (FontAwesome pack)
- Version control: Git and GitHub

---

## 4. Project Structure

Main folders (only listing important ones):

```text
StudentTaskManager/
├── backend/
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/studenttaskmanager/backend/
│           │       ├── app/
│           │       │   └── Main.java
│           │       ├── db/
│           │       │   └── FirebaseConfig.java
│           │       ├── models/
│           │       │   └── Student.java
│           │       └── repository/
│           │           └── FirebaseStudentRepository.java
│           └── resources/
│               └── serviceAccountKey.json   (not tracked in Git)
├── frontend/
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           │   └── frontend/
│           │       ├── MainApp.java
│           │       ├── Service/
│           │       │   ├── TaskService.java
│           │       │   └── UserSession.java
│           │       ├── controller/
│           │       │   ├── LoginController.java
│           │       │   ├── SignupController.java
│           │       │   ├── DashboardController.java
│           │       │   ├── TasksController.java
│           │       │   ├── AddTaskController.java
│           │       │   ├── TaskDetailsController.java
│           │       │   ├── CalendarController.java
│           │       │   ├── AnalyticsController.java
│           │       │   ├── SettingsController.java
│           │       │   └── SidebarController.java
│           │       └── model/
│           │           └── Task.java
│           └── resources/
│               └── frontend/
│                   ├── fxml/
│                   │   ├── Login.fxml
│                   │   ├── SignUp.fxml
│                   │   ├── Dashboard.fxml
│                   │   ├── Tasks.fxml
│                   │   ├── AddTask.fxml
│                   │   ├── TaskDetails.fxml
│                   │   ├── Calendar.fxml
│                   │   ├── Analytics.fxml
│                   │   ├── Settings.fxml
│                   │   └── Sidebar.fxml
│                   ├── css/
│                   │   ├── styles.css
│                   │   └── styles-dark.css
│                   └── images/
│                       ├── STMLogo.png
│                       └── login-illustration.png
└── README.md

Short explanation of key backend classes:
	•	Student
Represents one student task record with:
	•	id
	•	firstName
	•	lastName
	•	className
	•	task
	•	status
	•	FirebaseConfig
	•	Loads the Firebase service account key from src/main/resources/serviceAccountKey.json.
	•	Initializes Firebase Admin SDK.
	•	Provides a getFirestore() method that returns a Firestore instance.
	•	FirebaseStudentRepository
	•	Talks directly to Firestore.
	•	Methods:
	•	addStudent(Student s)
	•	getAllStudents()
	•	updateStatus(int id, String newStatus)
	•	deleteStudent(int id)
	•	Main (backend)
	•	Simple runner used for testing the backend.
	•	Initializes Firebase, creates a repository, adds test data, reads it, updates it, and deletes it.

⸻

5. How to Run the Backend Locally

Prerequisites
	•	Java 17 installed
	•	Maven installed
	•	A Firebase project created in the Firebase Console
	•	A Firebase service account key JSON file

Steps
	1.	Clone the repository:

git clone https://github.com/Muhammad7839/Student-task-manager.git
cd Student-task-manager/backend


	2.	Place your Firebase service account key:
	•	In the Firebase console, go to
“Project Settings” → “Service Accounts” → “Firebase Admin SDK” → “Generate new private key”.
	•	Download the JSON file.
	•	Rename it to:
serviceAccountKey.json
	•	Put it inside:
backend/src/main/resources/
	3.	Make sure the key is ignored by Git (already handled in .gitignore, but for reference):

*.json
serviceAccountKey.json
backend/src/main/resources/serviceAccountKey.json


	4.	Build the backend:

mvn clean install


	5.	Run the backend test runner (optional):
In IntelliJ:
	•	Open Main.java in backend/app.
	•	Run the main method.
Or using Maven (if you add a main entry later):

mvn exec:java


	6.	Check Firestore:
	•	Go to Firebase Console → Firestore Database.
	•	You should see a students collection with documents like 20001, 20002, etc.

⸻

6. How Firebase Is Used
	•	The project uses Firebase Admin SDK (Java) and Cloud Firestore.
	•	Connection is done with the service account key stored locally in src/main/resources.
	•	Firestore stores each student as a document in the students collection.
	•	Document fields match the fields in the Student class:
	•	id
	•	firstName
	•	lastName
	•	className
	•	task
	•	status

Example Firestore document:

students / 20001
  id: 20001
  firstName: "John"
  lastName: "Doe"
  className: "Math 101"
  task: "Homework 1"
  status: "Complete"


⸻

7. UML (to be attached later)

The final submission will include UML diagrams such as:
	•	Class Diagram
	•	Student
	•	FirebaseConfig
	•	FirebaseStudentRepository
	•	Main
	•	(and UI classes if added)
	•	Sequence Diagram(s)
	•	Example: “Add Student Task”
	•	Example: “Update Task Status”

These diagrams will be based on the latest version of the Java code and will be created in Lucidchart.

⸻

	8.	Team

8. Team Members & Roles
	•	Muhammad Imran
Backend development (Firebase integration, repositories, configuration), project coordination, documentation.
	•	Justice
Backend development (database logic, CRUD operations, backend testing).
	•	Dieunie
Frontend development (UI screens, displaying student tasks, connecting UI with backend).
	•	Brianna Christopher
Testing, QA, and validation.
Helps check that backend logic, error handling, and UI behavior work correctly.
Reviews functionality, reports issues, and assists with final project polishing.

⸻