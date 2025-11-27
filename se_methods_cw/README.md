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
├── src/                 # main application code
├── tests/               # automated unit tests
├── .github/
│   ├── ISSUE_TEMPLATE/  # bug_report.yml, feature_request.yml (created via GitHub UI)
│   └── workflows/       # GitHub Actions CI workflow
├── CODE_OF_CONDUCT.md
├── CONTRIBUTING.md
└── README.md


---

## 🧪 Compile the project
# Compile the project with **runned dockerfile in db directory**
mvn compile

# Run all tests
mvn test
