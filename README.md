
# FarmDirect 🌱

### A Java-Based Direct Farmer-to-Customer Marketplace

FarmDirect is a desktop application built with Java Swing and SQLite
that connects farmers directly with customers. It supports product
listing, fair-price calculation, order tracking, feedback, and bulk
sales through customer bidding.

## ✨ Features

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

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Application development |
| Java Swing | Desktop user interface |
| JDBC | Database connectivity |
| SQLite | Local data persistence |
| Eclipse IDE | Development environment |
| Git and GitHub | Version control and collaboration |

## 📁 Project Structure

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

## ⚙️ Requirements

- Java Development Kit (JDK) 21
- Eclipse IDE with Java support
- SQLite JDBC driver

## 🚀 Run the Project

1. Clone or download this repository.
2. Open Eclipse.
3. Select **File → Import → Existing Projects into Workspace**.
4. Select the downloaded `FarmDirect` project folder.
5. Verify the JDK and SQLite JDBC driver configuration.
6. Run `farmdirect.Main` as a Java Application.

The application initializes its database tables on startup.

## 👥 User Workflows

### Farmer
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

## 🔒 Data and Security

- Local SQLite database files are excluded from version control.
- Never commit passwords, API keys, personal customer data, or private
  configuration files.
- This project is an educational prototype. Production deployment
  would require stronger password storage, authorization, transaction
  handling, and security testing.

## 📸 Screenshots

Add screenshots of the login screen, farmer dashboard, product browsing,
order tracking, and bulk bidding to a `screenshots/` directory.

Example Markdown:

```markdown
![FarmDirect Login](screenshots/login.png)
```

## 📌 Project Status

Academic Java micro-project. Features and interface may evolve during
development.

## 📄 License

No license has been selected yet. Add a license before permitting others
to reuse or distribute this project.
