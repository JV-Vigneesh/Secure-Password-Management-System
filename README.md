# Secure Password Management System

This project implements a secure password management system using hashing for password storage and retrieval. It provides a user-friendly interface for managing website credentials.

## **THIS IS A BETA PROJECT - NOT FINAL**

## Features

* **User Authentication:**
    * Login and registration functionality.
    * Secure password storage using hashing.
* **Password Management:**
    * Add new website credentials (Website/Name, Username, Password).
    * Generate strong, random passwords.
    * Display saved website credentials (after decryption).
    * Passwords are encrypted before storing to the database.
* **Security:**
    * Passwords are hashed using a robust hashing algorithm (e.g., bcrypt, Argon2) before storage.
    * Passwords are encrypted for database storage.

## Technologies Used

* [**Programming Language:** Java]
* [**Database:** SQLite]
* [**Hashing Library:** bcrypt (for now, MAY CHANGE IN THE FUTURE)]
* [**Encryption Library:** cryptography]
* [**Frontend Framework/Library:** JAVA FX]

## Getting Started

### Prerequisites

* [List any required software, e.g., Python 3.x, Node.js, database server]
* Make sure you have the necessary libraries installed.

### Installation

1.  Clone the repository:

    ```bash
    git clone [repository URL]
    cd [repository directory]
    ```

2.  Install dependencies:

    ```bash
    # Example for Python (using pip)
    pip install -r requirements.txt

    # Example for Node.js (using npm or yarn)
    npm install
    # or
    yarn install
    ```

3.  Set up the database:

    * Create a database and configure the connection settings in the application's configuration file.
    * Run any database migrations or schema creation scripts.

4.  Run the application:

    ```bash
    # Example for Python
    python app.py

    # Example for Node.js
    node server.js
    ```

5.  Open your web browser and navigate to the application's URL.

## Usage

1.  **Login/Registration:**
    * If you are a new user, register an account.
    * If you already have an account, log in.
2.  **Add New Password:**
    * Click the "Add New Password" button.
    * Enter the Website/Name, Username, and Password.
    * Alternatively, click generate password to create a strong password.
    * Click "Save" to store the credentials.
3.  **View Saved Passwords:**
    * Click the refresh button to display the saved passwords.
    * The passwords will be decrypted and displayed.
4.  **Security Notes:**
    * The passwords are encrypted and hashed before being stored in the database.
    * The encrypted passwords in the database are not directly readable.
    * Keep your master password secure.

## Database Structure (Example)


```users:
id (INTEGER PRIMARY KEY)
username (TEXT)
password_hash (TEXT)

passwords:
id (INTEGER PRIMARY KEY)
user_id (INTEGER FOREIGN KEY REFERENCES users(id))
website (TEXT)
username (TEXT)
encrypted_password (TEXT)

```


```
## Security Considerations

* Use a strong, unique master password.
* Keep your system and dependencies up-to-date.
* Consider implementing additional security measures, such as two-factor authentication.
* Never commit your database credentials or encryption keys to version control.
* Use a strong encryption algorithm and key derivation function.

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues.

## License

[License (e.g., MIT, Apache 2.0)]