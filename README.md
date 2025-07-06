DevOps Project: CI/CD & Monitoring Pipeline
This repository contains the configuration and setup for a complete CI/CD and monitoring pipeline. The project uses a suite of industry-standard DevOps tools to automate the building, testing, analysis, and monitoring of a software application.

The entire environment is provisioned using Vagrant, making it easy to replicate and run locally.

🏛️ Architecture & Pipeline Flow
The core of this project is a Jenkins pipeline that orchestrates the entire CI/CD process. The workflow is as follows:

Code Commit: A developer pushes new code to the repository.

Pipeline Trigger: Jenkins automatically detects the change and triggers a new build.

Build & Test: The pipeline clones the repository, compiles the source code, and runs unit tests using JUnit and Mockito.

Code Analysis: The code is then analyzed by SonarQube to check for bugs, vulnerabilities, and code smells. A quality gate must be passed for the pipeline to continue.

Artifact Management: Build artifacts and dependencies are managed and stored in Nexus Repository Manager.

Containerization: The application is packaged into a Docker image.

Orchestration: Docker Compose is used to run the application service alongside other necessary services.

Monitoring: Prometheus continuously scrapes performance and application metrics, which are then visualized in real-time on Grafana dashboards.

🛠️ Technologies Used
This project integrates the following tools to create a robust DevOps toolchain:

Tool

Category

Purpose

Vagrant

Virtualization

Provisions and manages the virtual machine environment.

Jenkins

CI/CD Automation

Orchestrates the entire build, test, and deployment pipeline.

SonarQube

Code Quality

Performs static code analysis and enforces quality gates.

Nexus

Artifact Repository

Manages and stores dependencies and build artifacts.

Docker

Containerization

Packages the application and its dependencies into containers.

Docker Compose

Orchestration

Defines and runs multi-container Docker applications.

Prometheus

Monitoring

Collects time-series data and metrics from services.

Grafana

Visualization

Creates dashboards to visualize and analyze metrics from Prometheus.


Export to Sheets
🏁 Getting Started
To get the entire pipeline running on your local machine, you need to have Vagrant and a virtualization provider (like VirtualBox or VMware) installed.

Prerequisites
Vagrant

Oracle VirtualBox

Installation
Clone this repository:

Bash

git clone https://github.com/czraine/5SAE3-Fantasy_Dev.git
cd 5SAE3-Fantasy_Dev
Start the environment with Vagrant:
This single command will provision the virtual machine, install all the necessary tools, and configure them to work together.

Bash

vagrant up
Please be patient, as this process may take some time while it downloads and configures all the software.

Accessing the Services
Once vagrant up is complete, you can access the various services through your browser at the following default addresses:

Jenkins: http://localhost:8080

SonarQube: http://localhost:9000

Nexus: http://localhost:8081

Grafana: http://localhost:3000

You can now trigger the pipeline in Jenkins or by committing a change to see the automation in action.
