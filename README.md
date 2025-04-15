## 🛠️ Base de données

Voici le script SQL pour créer la base de données `ticketdb` avec toutes les tables nécessaires au bon fonctionnement de l'application (événements, utilisateurs, billets et réservations).  
Vous pouvez copier l’ensemble du script ci-dessous et l’exécuter dans votre gestionnaire de base de données (comme MySQL Workbench, phpMyAdmin, etc.).

```sql
-- Création de la base de données
CREATE DATABASE IF NOT EXISTS ticketdb;
USE ticketdb;

-- Table Evenement
CREATE TABLE IF NOT EXISTS evenements (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL,
    description TEXT,
    date_debut DATETIME NOT NULL,
    date_fin DATETIME NOT NULL,
    lieu VARCHAR(255) NOT NULL
);

-- Table Utilisateur
CREATE TABLE IF NOT EXISTS utilisateurs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL
);

-- Table Billet
CREATE TABLE IF NOT EXISTS billets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    type_billet VARCHAR(255) NOT NULL,
    prix DOUBLE NOT NULL,
    quantite_disponible INT NOT NULL,
    evenement_id BIGINT NOT NULL,
    FOREIGN KEY (evenement_id) REFERENCES evenements(id)
);

-- Table Reservation
CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    utilisateur_id BIGINT NOT NULL,
    billet_id BIGINT NOT NULL,
    date_reservation DATETIME NOT NULL,
    quantite INT NOT NULL,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id),
    FOREIGN KEY (billet_id) REFERENCES billets(id)
);


-- Création d'index pour améliorer les performances
CREATE INDEX idx_billets_evenement ON billets(evenement_id);
CREATE INDEX idx_reservations_utilisateur ON reservations(utilisateur_id);
CREATE INDEX idx_reservations_billet ON reservations(billet_id);
