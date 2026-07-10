# Pokédex JavaFX

Application de bureau permettant de rechercher des Pokémon via PokéAPI, de les sauvegarder dans une base de données PostgreSQL locale, et de consulter sa collection de Pokémon capturés.

## Fonctionnalités

- Recherche d'un Pokémon par nom ou par id
- Sauvegarde automatique en base de données (upsert)
- Liste des Pokémon capturés, consultables en un clic
- Suppression d'un Pokémon avec confirmation
- Appels API en arrière-plan (multi-threading), sans geler l'interface
- Gestion des erreurs : Pokémon introuvable, pas de connexion internet, base de données inaccessible
- Interface avec les couleurs officielles des 18 types Pokémon

## Stack technique

- Java 17+
- JavaFX 21
- PostgreSQL 15+
- Jackson (parsing JSON)
- Maven
- PokéAPI (pokeapi.co)

## Installation

### Prérequis

- Java 17 ou plus
- PostgreSQL installé et démarré
- Maven (ou utiliser le wrapper inclus)

### 1. Cloner le projet
git clone https://github.com/HugoJosue/pokedex-misession.git
cd pokedex-app

### 2. Créer la base de données

Dans pgAdmin ou via psql, créez une base nommée `pokedex_db`, puis exécutez le script `dump.sql` fourni à la racine du projet pour créer la table nécessaire.

### 3. Configurer la connexion

Dans `src/main/java/ca/cmaisonneuve/pokedex/db/DatabaseConnection.java`, ajustez si nécessaire :

```java
private static final String URL = "jdbc:postgresql://localhost:5432/pokedex_db";
private static final String UTILISATEUR = "postgres";
private static final String MOT_DE_PASSE = "VOTRE_MOT_DE_PASSE";
```

### 4. Lancer l'application
mvn javafx:run

## Captures d'écran





## Auteur

Hugo Josue Alcin — Collège de Maisonneuve