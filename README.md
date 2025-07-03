# 📚 Library Management System

A full-stack Library Management System built with **Spring Boot (Backend)** and **React (Frontend)**. This application manages books, users, borrowing activities, returns, late fines, and user authentication. It includes **role-based access control** for Admins and Students, as well as **password encryption** and secure storage.

---

## 🔧 Features

### ✅ Core Features
- 📘 **Create Book List**
  - Admins can add, update, and delete books.
  - View all available books with details like title, author, category, and total copies.

- 👥 **User Management**
  - Admins can create and manage student accounts.
  - View user list and details.

- 📖 **Borrow Book**
  - Students can borrow books if available.
  - Each borrow entry records borrow date and expected return date (7 days from borrow).

- 🔁 **Return Book**
  - Students can return previously borrowed books.
  - Automatically calculates late return and generates fine (if returned after expected date).

- ⏳ **Late Fine**
  - Fine is calculated based on the number of days past the expected return date.

### 🔐 Authentication & Authorization
- Admin and Student roles using Spring Security.
- JWT-based authentication system (optional, if used).
- Role-based access for different endpoints (e.g., only Admin can add books).
- Passwords are encrypted using **BCrypt** and stored securely in the database.

---

## 🛠️ Tech Stack

### Backend
- Java 17+
- Spring Boot
- Spring Data JPA (Hibernate)
- Spring Security
- MySQL
- Lombok

---

## 🗂️ Project Structure (Backend)

src/
├── controller/ # REST Controllers
├── service/ # Business Logic
├── repository/ # JPA Repositories
├── entity/ # JPA Entities (Book, User, BorrowedBook, etc.)
├── dto/ # Data Transfer Objects
├── config/ # Spring Security Configurations
└── exception/ # Custom Exception Handling


---

## 🔐 Roles and Permissions

| Role     | Capabilities                                   |
|----------|------------------------------------------------|
| Admin    | Manage books, users, and view all data         |
| Student  | View books, borrow & return, view own fines    |

---

## 🔑 Security

- Passwords are encrypted with **BCryptPasswordEncoder**.
- Authentication and authorization are handled via **Spring Security**.
- Role-based method access using `@PreAuthorize`.

---

## 🗓️ Return & Fine Logic

- **Expected Return Date:** Borrow date + 7 days
- **Late Fine:** Automatically applied per day after 7-day period (can be set in config)

---

## 🚀 How to Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/library-management-system.git
   cd library-management-system

