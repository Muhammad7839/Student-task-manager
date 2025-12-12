# Student Task Manager

<img width="864" height="640" alt="image" src="https://github.com/user-attachments/assets/443ef944-33d9-4fe1-b28a-10d682d6ea9f" />


Student Task Manager is a Java-based application designed to help users manage academic tasks efficiently. The system uses a Java backend with Firebase Cloud Firestore for persistent storage and a JavaFX frontend for an interactive and user-friendly experience.

---

## 1. Project Goals

- Help users organize and track academic tasks.
- Provide a clear overview of task status (Not Started, In Progress, Completed).
- Use a real cloud backend (Firebase Firestore) instead of local-only storage.
- Follow proper software engineering practices (layered architecture, separation of concerns).
- Deliver a clean, modern JavaFX graphical interface.

---

## 2. Features

### Backend Features
- Create, read, update, and delete tasks stored in Firebase Firestore.
- Secure backend connection using Firebase Admin SDK.
- Repository pattern to isolate database logic.
- Clear data model representing tasks.

### Frontend (JavaFX) Features
- Login and signup screens with validation.
- Sidebar-based navigation layout.
- Dashboard showing task overview and progress.
- Tasks screen with table view and filters.
- Add / Edit Task form with full validation.
- Calendar view for due dates.
- Analytics screen showing task statistics.
- Light and Dark mode support.

### Planned / Future Features
- Advanced search and filtering.
- More detailed analytics and reports.
- Web-based frontend using the same backend.
- Role-based access (student vs instructor).

---

## 3. Project Overview

Student Task Manager is a full-stack Java application that allows users to manage academic tasks through a centralized system. Users can log in, create tasks, update their status, view deadlines in a calendar format, and analyze task progress using built-in analytics.

The application combines a JavaFX frontend with a Firebase-backed Java backend, following a layered architecture that separates UI, business logic, and data access. This design improves maintainability, testability, and scalability.

Detailed screenshots and a demo video demonstrating the application’s functionality are provided later in this document.

---

## 4. Technology Stack

- **Language:** Java 17  
- **Build Tool:** Maven  
- **Backend:** Firebase Admin SDK, Cloud Firestore  
- **Frontend:** JavaFX (FXML, Controllers, CSS)  
- **Icons:** Ikonli (FontAwesome pack)  
- **Version Control:** Git and GitHub  
- **Testing:** JUnit 5  

---

## 5. Architecture Overview

The application follows a layered architecture:

- UI Layer: JavaFX views defined using FXML
- Controller Layer: Handles user interaction and navigation
- Service Layer: Business logic and validation
- Repository Layer: Firestore database access
- Configuration Layer: Firebase initialization and security

This separation improves maintainability, testability, and scalability.

---

## 6. Project Structure

```text
StudentTaskManager/
├── backend/
│   ├── pom.xml
│   └── src/main/java/com/studenttaskmanager/backend/
│       ├── app/
│       │   └── Main.java
│       ├── db/
│       │   └── FirebaseConfig.java
│       ├── models/
│       │   └── Student.java
│       └── repository/
│           └── FirebaseStudentRepository.java
│
├── frontend/
│   ├── pom.xml
│   └── src/main/java/frontend/
│       ├── MainApp.java
│       ├── controller/
│       ├── service/
│       └── model/
│
└── README.md
```

##  Project Structure Screenshot 

<img width="440" height="285" alt="image" src="https://github.com/user-attachments/assets/c0fe1ee6-4e86-4ed7-a274-b2d7bb99a944" />

## 7. Firebase Integration

The application uses **Firebase Cloud Firestore** as its primary database for persistent storage.

- Firebase is accessed using the **Firebase Admin SDK (Java)**.
- A service account key file (`serviceAccountKey.json`) is required for secure authentication.
- The key file is stored locally inside:
  `backend/src/main/resources/`
- For security reasons, the key file is **ignored by Git** using `.gitignore`.
- All database operations are isolated inside the **repository layer**, following clean architecture principles.

Each task is stored as a Firestore document with the following fields:
- `id`
- `firstName`
- `lastName`
- `className`
- `task`
- `status`

<img width="2744" height="1456" alt="image" src="https://github.com/user-attachments/assets/66731079-17bc-4595-9cde-61c6a7173e13" />

<img width="2700" height="1374" alt="image" src="https://github.com/user-attachments/assets/8c1750d2-493a-4baa-8391-a114cdd56ff0" />



---

## 8. Testing

Unit testing is implemented using **JUnit 5** to validate backend and service-layer logic.

- Tests focus on **non-UI components**.
- Covered scenarios include:
  - Authentication logic
  - Input validation
  - Task creation
  - Task updates
  - Error handling
- All tests were executed successfully with **zero failures**.

Screenshots of JUnit test execution and results are included in the project documentation.

<img width="468" height="303" alt="image" src="https://github.com/user-attachments/assets/ec894c9e-47d1-42a4-981a-1b6617dda3f1" />

<img width="468" height="303" alt="image" src="https://github.com/user-attachments/assets/a608a1b5-ac23-4ea2-8631-43e1609b3c64" />


---

## 9. UML Diagrams

The project includes UML diagrams created based on the final implementation.

### Class Diagram
Illustrates the structure and relationships between:
- Models
- Repository layer
- Service layer
- Controllers

<img width="2482" height="1178" alt="UML Class Diagram" src="https://github.com/user-attachments/assets/93bbfc1a-aaf4-4364-91e0-016bef7f6ffd" />



### Sequence Diagrams
Demonstrate key workflows:
- User login
- Create new task
- Password reset

These diagrams help visualize how the frontend, backend services, and Firebase interact.

<img width="843" height="583" alt="Sequence Diagram – Create New Task" src="https://github.com/user-attachments/assets/51cf0be9-6020-4fdc-8858-b8d130e02d5b" />

<img width="603" height="705" alt="Sequence Diagram – Reset Password" src="https://github.com/user-attachments/assets/3a729960-6f2c-4f0d-ae10-44c2c84a9608" />

<img width="703" height="630" alt="Sequence Diagram" src="https://github.com/user-attachments/assets/d8f6a0bc-bc6e-43ae-a401-1d7ec2dc961a" />




---

## 10. Demo & Screenshots

### Demo Video
(To be added)

- Login screen
  
<img width="2880" height="1864" alt="image" src="https://github.com/user-attachments/assets/46ac0ef1-ef19-4c6b-93a0-abd01354f8eb" />

  
- Dashboard overview
  
<img width="2880" height="1864" alt="image" src="https://github.com/user-attachments/assets/de34e0a1-d152-4fb9-8ab5-dd0d957004c0" />



- Tasks list with filters
  
<img width="468" height="307" alt="image" src="https://github.com/user-attachments/assets/09d05e09-15ab-4634-8d77-75cf78fe8757" />

<img width="2232" height="1410" alt="image" src="https://github.com/user-attachments/assets/41fafe2c-ef93-467c-826c-42d3a679a864" />


  
- Calendar view
  
<img width="468" height="298" alt="image" src="https://github.com/user-attachments/assets/8b38e54e-7e6f-4091-a4b2-d85964844916" />

  
- Analytics dashboard
  
<img width="468" height="301" alt="image" src="https://github.com/user-attachments/assets/136e7353-0d1b-409a-a8e9-713e5d540e54" />

<img width="2290" height="1440" alt="image" src="https://github.com/user-attachments/assets/4bca1b1a-3fcc-4fa3-9a63-2dacf2b5a401" />


  
- Light mode and Dark mode comparison
  
<img width="468" height="331" alt="image" src="https://github.com/user-attachments/assets/7f75d41a-425b-4b94-8fb2-aa948124c408" />

---

## 11. Version Control

GitHub is used for version control and project history tracking.

- The `main` branch represents the final, stable, executable version of the application.
- Commits reflect incremental development and feature completion.
- Project history shows backend development, frontend integration, and testing activity.

---

## 12. Conclusion

Student Task Manager demonstrates a complete software engineering workflow, including system design, implementation, testing, and documentation.

The application combines a JavaFX frontend with a Firebase-backed Java backend, follows clean architectural principles, and is structured to be maintainable and extensible. This project serves as a strong example of a full-stack Java application suitable for professional and portfolio use.
