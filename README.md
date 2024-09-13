
# Wildlife Habitat Management System

## Overview

The **Wildlife Habitat Management System** is a desktop application designed to help manage and monitor wildlife habitats. The system is built using JavaFX for the front-end interface, MySQL for the back-end database, and several other tools like MaterialFX, JasperReports, and SceneBuilder. It aims to provide a user-friendly interface for managing habitat data, monitoring wildlife, and generating insightful reports.

## Features

- **User Management**: Admins can manage users, assign roles, and control access.
- **Habitat Data Management**: Record and update information about various habitats.
- **Wildlife Monitoring**: Track and manage wildlife data, including species, populations, and movements.
- **Reports Generation**: Generate and export reports using JasperReports.
- **Data Visualization**: Use graphs and charts to visualize data trends and insights.
- **Responsive UI**: Built with JavaFX and MaterialFX for an interactive and modern user experience.

## Technologies Used

- **JavaFX**: For building the user interface.
- **MySQL**: For the relational database management system.
- **MaterialFX**: A JavaFX theme providing a Material Design look and feel.
- **JasperReports**: For generating reports.
- **CSS**: For additional styling of the UI.
- **SceneBuilder**: A visual layout tool for designing JavaFX application interfaces.
- **IntelliJ IDEA**: The integrated development environment (IDE) used for development.
- **GitHub**: For version control and collaboration.

## Installation

### Prerequisites

- Java Development Kit (JDK) 21 or higher
- MySQL Server
- IntelliJ IDEA or any other Java IDE
- Git (if you want to clone the repository)

### Steps

1. **Clone the Repository**

   ```bash
   git clone https://github.com/your-username/Wildlife_HMS.git
   cd Wildlife_HMS
   ```

2. **Set Up the Database**

   - Install MySQL and create a database named `wildlifehms`.
   - Execute the SQL scripts located in the `database` folder to create tables and insert initial data.

3. **Configure Database Connection**

   - Open the project in IntelliJ IDEA.
   - Navigate to the `src/main/java` folder and locate the `DatabaseConnection.java` file.
   - Update the database connection details (URL, username, password).

4. **Build and Run the Application**

   - Open the project in IntelliJ IDEA.
   - Build the project using the IDE's build tools.
   - Run the application by executing the `Main.java` file.

## Usage

- **Login**: Start the application and log in using your credentials.
- **Manage Habitats**: Navigate to the habitat management module to add, update, or delete habitat records.
- **Monitor Wildlife**: Use the wildlife module to input and track wildlife data.
- **Generate Reports**: Go to the reports section to create and export reports in various formats.

## License

This project is licensed under the MIT License. See the `LICENSE` file for more details.

## Contact

- **Project Maintainer**: 071-3950532 

Feel free to reach out for any questions or support!

---

Thank you for using the Wildlife Habitat Management System!
