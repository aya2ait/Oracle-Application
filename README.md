# Oracle-Application
Application Administration d'oracle 
## 1. Introduction

L'application web d'administration Oracle permet d'effectuer des opérations essentielles de gestion des bases de données Oracle. Cela inclut la gestion des utilisateurs, les sauvegardes, la surveillance des performances et l'optimisation des bases de données Oracle. Elle est développée avec **Spring Boot** pour le backend et **HTML5/CSS** pour le frontend.

### Objectifs principaux :
- **Gestion des utilisateurs** : création, modification, suppression, et gestion des rôles et privilèges.
- **Sauvegarde et restauration** : gestion des sauvegardes complètes et incrémentielles via RMAN.
- **Surveillance des performances** : rapport des performances en temps réel.
- **Optimisation des requêtes SQL** : consultation et optimisation des requêtes lentes.

## 2. Documentation d'Installation

### 2.1 Prérequis

Avant de commencer l'installation, assurez-vous que votre environnement respecte les conditions suivantes :

#### Système d'exploitation
- **Windows**, **Linux** ou **macOS**.

#### Logiciels nécessaires
- **Oracle Database 23 AI** (installation locale ou via Docker).
- **Docker** et **Docker Compose** (si l'option Docker est choisie).
- **JDK 17** ou supérieur.
- **Maven 3.6+**.
- **Navigateur web moderne** (Chrome, Firefox, Edge).

#### Configuration minimale du matériel
- **RAM** : 8 Go.
- **Stockage** : 20 Go d'espace libre.

### 2.2 Installation

#### Option 1 : Utilisation de Docker

##### Étape 1 : Installer Docker et Docker Compose
1. Installez **Docker** et **Docker Compose** sur votre machine.
2. Vérifiez l'installation en exécutant :
   ```bash
   docker --version
   docker-compose --version
Étape 2 : Télécharger et configurer l'image Oracle
Téléchargez l'image Docker pour Oracle Database 23 AI avec :

bash
docker pull oracle/database:23-ai
Créez un fichier docker-compose.yml avec le contenu suivant :

yaml
version: '3.8'
services:
  oracle-db:
    image: oracle/database:23-ai
    container_name: oracle23ai
    ports:
      - "1521:1521"
      - "5500:5500"
    environment:
      ORACLE_SID: ORCLCDB
      ORACLE_PDB: ORCLPDB1
      ORACLE_PWD: mysecurepassword
    volumes:
      - oracle_data:/opt/oracle/oradata
volumes:
  oracle_data:
Lancez le conteneur avec Docker Compose :

bash
docker-compose up -d
Vérifiez que le conteneur est bien en cours d'exécution :

bash
docker ps
Connectez-vous à la base de données via SQL*Plus ou un autre client SQL pour vérifier la connexion.

Option 2 : Installation locale d'Oracle Database
Étape 1 : Télécharger Oracle Database
Allez sur le site officiel d'Oracle et téléchargez Oracle Database 23 AI.
Suivez l'assistant d'installation et configurez la base de données avec les paramètres par défaut ou personnalisés.
Étape 2 : Configurer la base
Configurez votre base de données en définissant le SID, les utilisateurs, et les privilèges nécessaires.
Prenez note des informations de connexion, telles que l'hôte, le port, le SID, l'utilisateur, et le mot de passe.
Étape 3 : Configurer l'application
Ouvrez le fichier application.properties dans le dossier src/main/resources.
Modifiez les paramètres pour correspondre à votre environnement Oracle :
properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/ORCLPDB1
spring.datasource.username=admin
spring.datasource.password=mysecurepassword
Étape 4 : Construire et exécuter le projet
Compilez l'application avec Maven :

bash

mvn clean package
Lancez l'application en utilisant la commande suivante :

bash
java -jar target/oracle.jar
Étape finale : Accéder à l'application
Ouvrez un navigateur web et allez à l'URL suivante pour accéder à l'interface web :

arduino
http://localhost:8080
3. Fonctionnalités
3.1 Gestion des utilisateurs
Création, modification, suppression d'utilisateurs Oracle.
Gestion des rôles et des privilèges d'accès via une interface web intuitive.
Gestion des quotas d'espace et des politiques de mot de passe.
3.2 Sauvegarde et restauration
Déclenchement de sauvegardes complètes et incrémentielles via RMAN.
Historique des sauvegardes et option de restauration pour une date donnée.
Planification automatique des sauvegardes avec personnalisation de l'intervalle.
3.3 Sécurité des données
Configuration des politiques de chiffrement (TDE) et activation des audits de sécurité.
Gestion de la Virtual Private Database (VPD) pour restreindre l'accès à certaines données sensibles.
3.4 Surveillance des performances
Visualisation des rapports AWR et ASH avec des graphiques interactifs via des bibliothèques comme Chart.js.
Tableau de bord affichant les statistiques en temps réel sur l'utilisation des ressources système (CPU, I/O, mémoire).
3.5 Optimisation des performances
Consultation et optimisation des requêtes lentes avec l'outil SQL Tuning Advisor.
Planification de recalcul des statistiques des tables et index pour améliorer les performances de la base de données.
