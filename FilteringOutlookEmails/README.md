# 📧 Filtering Outlook Emails

A Java project that integrates with the Microsoft Graph API, allowing you to list, filter, and manage Outlook emails easily and programmatically.

## 🚀 Features

This application allows you to:

- **List emails** from the authenticated Outlook account using Microsoft Graph.
- **Copy the sender's email address** from selected messages.
- **Open emails directly in Outlook Web** from the application.
- **Delete emails**:
  - From specific selected senders.
  - From an individually selected email.

## 🔑 How to Use

1. Go to the [Microsoft Graph Explorer](https://developer.microsoft.com/en-us/graph/graph-explorer) and generate a valid authentication token.
2. In the application, navigate to `File > Change Token` and paste the generated token.
3. After adding the token:
   - The application will make two requests:
     - `/me` — to get user information.
     - `/me/messages` — to retrieve the list of emails.
4. Emails and user information will be loaded automatically into the interface.

## 🛠️ Technologies

- **Java**
- **Microsoft Graph API**
- **Swing** (Graphical User Interface)

## 📂 Project Structure

```
FilteringOutlookEmails/
 ├── src/
 ├── pom.xml
 ├── .gitignore
 └── README.md
```

## 📝 Requirements

- Java 11+
- Access token generated via [Microsoft Graph Explorer](https://developer.microsoft.com/en-us/graph/graph-explorer)

## 📌 Notes

- Emails are only loaded after a valid token is provided.
- Delete and open-in-browser actions are performed based on the currently loaded email list.

## 📄 License

This project is licensed under the [MIT License](../LICENSE).