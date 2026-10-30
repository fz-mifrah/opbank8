# OpBank — Application de gestion des opérations bancaires

![Java](https://img.shields.io/badge/Java-11-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.6.6-6DB33F?logo=springboot&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-13-DD0031?logo=angular&logoColor=white)
![JHipster](https://img.shields.io/badge/JHipster-7.8.1-3E8ACC?logo=jhipster&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-prod-4169E1?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker&logoColor=white)

OpBank est une application web de **banque en ligne** qui permet à un client de consulter son compte, d'effectuer des **virements de compte à compte**, de **recharger une ligne téléphonique** et de suivre l'**historique de ses opérations**. Un espace administrateur permet de gérer les clients, comptes, banques, cartes et opérations.

Le projet est une application monolithique générée avec **JHipster** (Spring Boot + Angular) à partir d'un modèle JDL, puis enrichie avec la logique métier bancaire.

---

## Sommaire

- [Fonctionnalités](#fonctionnalités)
- [Architecture](#architecture)
- [Modèle de données](#modèle-de-données)
- [API métier](#api-métier)
- [Stack technique](#stack-technique)
- [Démarrage rapide](#démarrage-rapide)
- [Tests et qualité](#tests-et-qualité)
- [Déploiement avec Docker](#déploiement-avec-docker)
- [Structure du projet](#structure-du-projet)
- [Auteure](#auteure)

---

## Fonctionnalités

### Espace client
- **Authentification JWT** : connexion sécurisée, gestion du compte utilisateur.
- **Mon compte** : consultation du compte courant (RIB, solde, date d'ouverture, carte bancaire).
- **Virement compte à compte** : transfert d'un montant vers un compte destinataire identifié par son RIB, avec libellé. Le solde est vérifié avant l'opération, puis les deux comptes sont mis à jour.
- **Recharge téléphonique** : recharge d'un numéro auprès d'un opérateur, débitée du compte courant.
- **Historique des opérations** : liste des opérations du client connecté (type, état, montant, date).

### Espace administrateur
- CRUD complet sur toutes les entités : clients, comptes, banques, cartes bancaires, opérations, virements, transferts, recharges, opérateurs, paiements de factures, services.
- Gestion des utilisateurs et des rôles.
- Monitoring : métriques, santé, configuration, logs (outils d'administration JHipster).

### Transverse
- Interface multilingue : **anglais**, **roumain**, **russe**.
- Pagination et défilement infini sur les listes.
- Cache Hibernate de second niveau avec **Hazelcast**.

---

## Architecture

```
┌──────────────────────────┐        HTTP / JSON (JWT)        ┌──────────────────────────────┐
│     Frontend Angular     │  ─────────────────────────────▶ │     Backend Spring Boot      │
│  - Espace client         │                                 │  web/rest   (contrôleurs)    │
│  - Espace admin          │ ◀───────────────────────────── │  service    (logique métier)  │
│  - i18n (en, ro, ru)     │                                 │  repository (Spring Data JPA)│
└──────────────────────────┘                                 └───────────────┬──────────────┘
                                                                             │ JPA / Liquibase
                                                                ┌────────────▼────────────┐
                                                                │ H2 (dev) / PostgreSQL   │
                                                                │ + cache Hazelcast       │
                                                                └─────────────────────────┘
```

Le backend suit une architecture en couches : **Resource → Service (interface + implémentation) → Repository**, avec des **DTO** et des mappers **MapStruct** entre les entités et l'API.

---

## Modèle de données

Le modèle complet est décrit dans [`opbank.jh`](opbank.jh) (JDL).

| Entité | Rôle | Champs principaux |
|---|---|---|
| `Client` | Titulaire du compte | cin, nom, prénom, adresse, code postal, date de naissance, email |
| `Compte` | Compte bancaire | rib, date d'ouverture, code, solde |
| `Banque` | Établissement bancaire | nom, adresse, email |
| `CarteBancaire` | Carte liée au compte | numéro de carte |
| `Operation` | Opération générique | numéro, date, type, état, montant |
| `Virement` | Virement compte à compte | description |
| `Transfer` | Transfert d'argent | cin, nom et prénom, téléphone |
| `Recharge` | Recharge téléphonique | numéro de téléphone |
| `Operateur` | Opérateur télécom | nom |
| `PaimentFacture` | Paiement de facture | référence |
| `ServiceClass` | Service facturé | nom du service |

**Énumérations**
- `TypeOperation` : `TypeVirement`, `TypeTransfer`, `TypeRecharge`, `TypePaimentFacture`
- `EtatOperation` : `Cree`, `EnCours`, `Valide`, `Annulee`, `Rejetee`

**Relations clés**
- `Client` 1–1 `Compte` 1–1 `CarteBancaire`
- `Banque` 1–N `Compte` 1–N `Operation`
- `Operation` 1–1 `Virement` | `Transfer` | `Recharge` | `PaimentFacture`
- `Recharge` N–1 `Operateur`, `PaimentFacture` N–1 `ServiceClass`

---

## API métier

En plus des endpoints CRUD générés (`/api/{entité}`), l'application expose :

| Méthode | Endpoint | Description |
|---|---|---|
| `GET` | `/api/clients/currentClient` | Client associé à l'utilisateur connecté |
| `GET` | `/api/comptes/currentCompte` | Compte du client connecté |
| `GET` | `/api/operations/operationHistory` | Historique des opérations du client connecté |
| `POST` | `/api/virements/effectuerVirement` | Virement de compte à compte |
| `POST` | `/api/recharges/effectuerRecharge/{montant}` | Recharge téléphonique |

Exemple de requête de virement :

```http
POST /api/virements/effectuerVirement
Authorization: Bearer <token>
Content-Type: application/json

{
  "monCompte": 1234567890,
  "compteDestinataire": 9876543210,
  "montant": 500,
  "label": "Loyer"
}
```

La documentation Swagger complète est accessible dans l'espace administrateur (**Administration → API**).

---

## Stack technique

| Couche | Technologies |
|---|---|
| Backend | Java 11, Spring Boot 2.6, Spring Security (JWT), Spring Data JPA, Hibernate, MapStruct, Liquibase |
| Frontend | Angular 13, TypeScript, Bootstrap, ngx-translate |
| Base de données | H2 (développement), PostgreSQL (production) |
| Cache | Hazelcast |
| Build | Maven, npm, Webpack |
| Tests | JUnit 5, ArchUnit, Jest, Cypress |
| Qualité | SonarQube, Checkstyle, ESLint, Prettier, Husky |
| Conteneurs | Docker, Docker Compose, Jib |
| Monitoring | Prometheus, Grafana |

---

## Démarrage rapide

### Prérequis
- Java 11+
- Node.js 16.14+ et npm
- (Optionnel) Docker et Docker Compose

### Lancer en développement

```bash
# 1. Installer les dépendances frontend
npm install

# 2. Démarrer le backend (port 8080, base H2)
./mvnw

# 3. Dans un second terminal, démarrer le frontend avec rechargement à chaud (port 9000)
npm start
```

Ouvrir ensuite [http://localhost:8080](http://localhost:8080) (ou `http://localhost:9000` en mode live reload).

Comptes par défaut :

| Rôle | Identifiant | Mot de passe |
|---|---|---|
| Administrateur | `admin` | `admin` |
| Utilisateur | `user` | `user` |

---

## Tests et qualité

```bash
# Tests backend (unitaires et d'intégration)
./mvnw verify

# Tests frontend (Jest)
npm test

# Tests end-to-end (Cypress)
npm run e2e

# Analyse SonarQube
docker-compose -f src/main/docker/sonar.yml up -d
./mvnw -Pprod clean verify sonar:sonar
```

---

## Déploiement avec Docker

```bash
# Construire l'image Docker de l'application
npm run java:docker

# Lancer l'application avec PostgreSQL
docker-compose -f src/main/docker/app.yml up -d
```

Build de production sans Docker :

```bash
./mvnw -Pprod clean verify
java -jar target/*.jar
```

Monitoring (Prometheus + Grafana) :

```bash
docker-compose -f src/main/docker/monitoring.yml up -d
```

---

## Structure du projet

```
opbank8/
├── opbank.jh                     # Modèle JDL (entités et relations)
├── pom.xml                       # Build Maven
├── package.json                  # Dépendances frontend
├── src/
│   ├── main/
│   │   ├── java/com/iga/opbank/
│   │   │   ├── domain/           # Entités JPA
│   │   │   ├── repository/       # Repositories Spring Data
│   │   │   ├── service/          # Interfaces, implémentations, DTO, mappers
│   │   │   ├── security/         # JWT et sécurité
│   │   │   └── web/rest/         # Contrôleurs REST
│   │   ├── resources/
│   │   │   ├── config/           # Configuration Spring et Liquibase
│   │   │   └── i18n/             # Messages traduits
│   │   ├── webapp/app/
│   │   │   ├── entities/         # Modules Angular par entité
│   │   │   │   ├── compte/current-compte/
│   │   │   │   ├── operation/history/
│   │   │   │   ├── virement/effectuer-virement/
│   │   │   │   └── recharge/effectuer-recharge/
│   │   │   ├── home/  login/  admin/  account/
│   │   │   └── shared/  core/  layouts/
│   │   └── docker/               # Fichiers Docker Compose
│   └── test/                     # Tests Java, Jest et Cypress
└── .jhipster/                    # Configuration des entités JHipster
```

---

## Auteure

**Fatima Ezzahra Mifrah**
Étudiante en génie logiciel — Java, Spring Boot, Angular & React, QA Automation & DevSecOps
Casablanca, Maroc

- GitHub : [@fz-mifrah](https://github.com/fz-mifrah)
- Email : mifrah.fz1@gmail.com
