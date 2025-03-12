## **📌 Secure Password Management System**
A **secure and efficient** password manager that encrypts and stores credentials safely. This project ensures **BCrypt hashing for authentication**, **AES encryption for password storage**, and **SQLite database integration** for structured data management.

---

## **🚀 Features**
✅ **User Authentication** – Secure login using **BCrypt hashing**.  
✅ **AES Encryption** – Stores passwords securely in an encrypted format.  
✅ **Prevent Duplicate Usernames** – Ensures no duplicate usernames for the same website.  
✅ **Secure Password Storage** – Uses **SQLite database** for structured management.  
✅ **Modify & Delete Passwords** – Update or remove stored credentials.  
✅ **Automatic Password Generation** – Generates strong, random passwords.  
✅ **User-Friendly UI** – Built with **JavaFX** for an intuitive experience.

---

## **🛠 Technologies Used**
- **Java (JavaFX)** – User interface.
- **SQLite** – Local database for storage.
- **BCrypt** – Hashing for user authentication.
- **AES Encryption** – Secure password storage.

---

## **📂 Project Structure**

```
📦 Secure-Password-Management-System
 ┣ 📂 src/main/java/application
 ┃ ┣ 📜 Main.java
 ┃ ┣ 📜 UIController.java
 ┃ ┣ 📜 DashboardController.java
 ┃ ┣ 📜 DatabaseHelper.java
 ┃ ┣ 📜 PasswordManager.java
 ┃ ┣ 📜 AuthManager.java
 ┃ ┗ 📜 User.java
 ┣ 📂 src/main/resources
 ┃ ┣ 📜 login.fxml
 ┃ ┣ 📜 register.fxml
 ┃ ┗ 📜 dashboard.fxml
 ┣ 📜 pom.xml  # Maven Dependencies
 ┣ 📜 README.md  # Project Documentation
 ┗ 📜 password_manager.db  # SQLite Database File (auto-generated)
```

---

## **📥 Installation & Setup**
### **🔹 Prerequisites**
Ensure you have the following installed:
- **Java 17+** (JDK 21 recommended).
- **Maven** (for dependency management).
- **SQLite JDBC Driver**.
- **JavaFX SDK 21** (for UI components).

### **🔹 Steps to Run the Project**
1️⃣ **Clone the Repository**
```bash
git clone https://github.com/JV-Vigneesh/Secure-Password-Management-System.git
cd Secure-Password-Management-System
```

2️⃣ **Install Dependencies (if using Maven)**
```bash
mvn clean install
```

3️⃣ **Run the Application**
```bash
mvn javafx:run
```
OR  
Run **Main.java** from your IDE.

---

## **📸 Screenshots**
| **Login Page** | **Dashboard** | **Save Password** |  
|---------------|-------------|----------------|  
| ![Login](https://via.placeholder.com/300x200) | ![Dashboard](https://via.placeholder.com/300x200) | ![Save Password](https://via.placeholder.com/300x200) |  

---

## **📝 How It Works**
1️⃣ **User Registration/Login** – Users sign up, and credentials are hashed with **BCrypt**.  
2️⃣ **Password Storage** – Credentials are **AES-encrypted** before being saved in **SQLite**.  
3️⃣ **Password Retrieval** – Stored passwords are decrypted upon user request.  
4️⃣ **Modify & Delete Passwords** – Users can edit or remove their credentials.

---

## **📈 Future Improvements**
🚀 **Multi-Factor Authentication (MFA)** – Add OTP verification.  
🚀 **Cloud Sync** – Store passwords securely on cloud servers.  
🚀 **Browser Integration** – Autofill and password suggestions.  
🚀 **Biometric Authentication** – Use fingerprint or facial recognition.  
🚀 **Auto-Password Change** – Periodic password updates for security.

---

## **📄 License**
This project is **open-source** and available under the **MIT License**.

---

## **📌 Contributing**
1️⃣ Fork the repository.  
2️⃣ Create a new branch (`git checkout -b feature-branch`).  
3️⃣ Commit your changes (`git commit -m "Added new feature"`).  
4️⃣ Push to the branch (`git push origin feature-branch`).  
5️⃣ Create a **Pull Request**.

---

## **💡 Credits**
- **Developer:** [Your Name](https://github.com/yourusername)

---