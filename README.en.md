# Public Organizations and Services Directory

## e-Government Information System

This project was developed as part of the Master's thesis:

**Development of a Web Application for a Directory of Public Organizations and Services**

🎓 Hellenic Open University  
School of Science and Technology  
Postgraduate Program: *Specialization in Information Systems*  
**Supervisor**: Georgios Mavrommatis  
**Author**: Dionysios Charalampopoulos  
📧 Contact: [dion.charalampopoulos@gmail.com](mailto:dion.charalampopoulos@gmail.com)

---

## ✨ Overview

This project involves the design and implementation of an **e-Government** web application that makes the **Registry of Public Services and Organizations** available online.

The application:

- Is updated using official data from the Ministry of Interior  
  🔗 [Registry of Public Services](https://www.ypes.gr/category/dioikitiki-anasygkrotisi/mitroo-dimosion-ypiresion/)
- Allows user-friendly search of services and organizations using filters
- Provides combined information through interoperability with external APIs (Diavgeia, Google Places API (New))
- Includes an admin environment for adding, editing, and bulk importing organizations

---

## 🚀 Features

- 🔍 Search organizations using filters (name, Ministry, Local Authority, legal form, public/private sector)
- 🗺️ View organizations on a map (OpenStreetMap)
- 📄 Integration with Diavgeia API for displaying recent decisions
- 🧩 Automatic filling of address, phone, website, and coordinates using Google Places API (New)
- 👤 Admin panel with CRUD functionality for Ministries, Local Authorities, and Organizations
- 📤 Mass data update & cleanup using official Excel files from the Ministry of Interior

---

## ⚙️ Technologies

- **Backend**: Java 17, Spring Boot, Spring Security, Spring Data JPA, Hibernate, JWT
- **Frontend**: Vue.js (Vite, Composition API), Axios, OpenStreetMap, Node.js & npm
- **Database**: MariaDB

- **API Integrations**:
  - [Diavgeia API](https://diavgeia.gov.gr)
  - [Google Places API (New) (SearchText + Place Details)](https://developers.google.com/maps/documentation/places/web-service/op-overview)

- **Architecture**:
  - RESTful API
  - MVC Pattern
  - Layered Architecture
  - Separation of Concerns

---

## 🔐 Security Notes

- Authentication uses **JWT tokens** stored in **HttpOnly cookies**
- Access to **admin endpoints** is protected by role-based access (`ROLE_ADMIN`)
- No real credentials or API keys are included in this public repository

---

## 🧪 Running in Development Mode

To run the application locally:

1. **Backend (Spring Boot)**  
   Use your preferred IDE (STS/Eclipse/IntelliJ) to run the Spring Boot main class (e.g., `PublicAgenciesCatalogueApplication.java`)

2. **Frontend (Vue.js)**  
   Run the following commands:

   ```bash
   npm install
   npm run dev
