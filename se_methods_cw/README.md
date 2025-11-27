This is a cloned repo - original is sem-quick-cw. 
# SE Methods Coursework

[![A workflow](https://github.com/ThatBoatSmell/se_methods_cw/actions/workflows/main.yml/badge.svg)](https://github.com/ThatBoatSmell/se_methods_cw/actions/workflows/main.yml)

This repository contains coursework for the **Software Engineering Methods** module.

> **Note:**  
> This is a cloned repository.  
> The original source project is **sem-quick-cw**.

This project demonstrates:
- Clean Python project structure  
- Automated testing using GitHub Actions  
- GitHub Issue Templates (Bug Report + Feature Request)  
- Contribution Guidelines  
- Code of Conduct  
- CI workflow (`main.yml`)  
- Proper documentation and repository setup following SE Methods requirements

---

## 📁 Project Structure
se_methods_cw/
├── .github/
│   ├── ISSUE_TEMPLATE/
│   │   ├── bug_report.yml
│   │   └── feature_request.yml
│   └── workflows/
│       └── main.yml
├── se_methods_cw/
│   ├── db/                     # Database init scripts (for Docker)
│   ├── img/                    # Images and references
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   └── com/napier/sem/
│       │   │       ├── App.java
│       │   │       ├── City.java
│       │   │       ├── Country.java
│       │   │       └── PopulationReport.java
│       │   └── resources/
│       └── test/
│           └── java/
│               ├── IntegrationTest.java
│               └── SmokeTest.java
├── docker-compose.yml
├── Dockerfile
├── pom.xml
├── LICENSE
├── README.md
└── world reference.PNG


---

## 🧪 Compile the project
# Compile the project with **runned dockerfile in db directory**
mvn compile

# Run all tests
mvn test
