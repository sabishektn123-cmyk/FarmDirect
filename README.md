
### FarmDirect 🌱

A Java-Based Direct Farmer-to-Customer Marketplace

FarmDirect is a desktop application built with Java Swing and SQLite
that connects farmers directly with customers. It supports product
listing, fair-price calculation, order tracking, feedback, and bulk
sales through customer bidding.

### ✨ Features

- 👨‍🌾 Farmer and customer registration and login
- 🥬 Crop and product listing with quantity and freshness
- 💰 Fair-price calculation based on farmer quotations
- 🛒 Customer product browsing and purchasing
- 📦 Order history and delivery-status tracking
- ⭐ Farmer ratings and customer feedback
- 🤝 Bulk listings and competitive customer bidding
- 🏆 Highest-bid selection and bulk-sale recording
- 🗄️ SQLite database persistence
- 🖥️ Java Swing graphical user interface

### 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Application development |
| Java Swing | Desktop user interface |
| JDBC | Database connectivity |
| SQLite | Local data persistence |
| Eclipse IDE | Development environment |
| Git and GitHub | Version control and collaboration |

### 📁 Project Structure

```text
FarmDirect/
├── src/
│   ├── farmdirect/
│   ├── farmdirect.model/
│   ├── farmdirect.ui/
│   ├── farmdirect.dao/
│   ├── farmdirect.service/
│   ├── farmdirect.database/
│   ├── farmdirect.exception/
│   └── farmdirect.util/
├── lib/
├── .gitignore
└── README.md
```

The exact files and folders may vary with the current project setup.

### ⚙️ Requirements

- Java Development Kit (JDK) 21
- Eclipse IDE with Java support
- SQLite JDBC driver

### 🚀 Run the Project

1. Clone or download this repository.
2. Open Eclipse.
3. Select **File → Import → Existing Projects into Workspace**.
4. Select the downloaded `FarmDirect` project folder.
5. Verify the JDK and SQLite JDBC driver configuration.
6. Run `farmdirect.Main` as a Java Application.

The application initializes its database tables on startup.

 ### 👥 User Workflows

Farmer
- Register and log in.
- Add products and manage listings.
- Review customer orders.
- Update order statuses.
- Create bulk-sale listings.
- Review bids and accept the highest valid bid.

 ### Customer
- Register and log in.
- Browse available products.
- Purchase products at the displayed fair price.
- Track orders.
- Submit feedback and ratings.
- Browse bulk listings and place bids.

 ### 🔒 Data and Security

- Local SQLite database files are excluded from version control.
- Never commit passwords, API keys, personal customer data, or private
  configuration files.
- This project is an educational prototype. Production deployment
  would require stronger password storage, authorization, transaction
  handling, and security testing.

 ### 📸 Screenshots
<img width="1917" height="1078" alt="Screenshot 2026-10-09 213624" src="https://github.com/user-attachments/assets/c6ea41fb-560e-4c0f-9827-5de672d00d78" />
<img width="1917" height="1076" alt="Screenshot 2026-10-09 213807" src="https://github.com/user-attachments/assets/2fc1d0fe-562b-4642-afe1-c7d87f4fe313" />
<img width="665" height="676" alt="Screenshot 2026-10-09 213839" src="https://github.com/user-attachments/assets/a7fe0ad8-e7de-4781-9d83-3138e7d4afad" />
<img width="1917" height="1076" alt="Screenshot 2026-10-09 214010" src="https://github.com/user-attachments/assets/8a56d3cf-77f0-4df1-9736-49186ce148d2" />
<img width="1220" height="797" alt="Screenshot 2026-10-09 214025" src="https://github.com/user-attachments/assets/fa32e48d-7281-49f5-ba24-be27348fa368" />

 ### 📌 Project Status

Academic Java micro-project. Features and interface may evolve during
development.

## 📄 License

No license has been selected yet. Add a license before permitting others
to reuse or distribute this project.
