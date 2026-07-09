-- création de la table pokemon
-- id = id officiel pokéapi, utilisé comme clé primaire pour permettre l'upsert
CREATE TABLE pokemon (
                         id              INTEGER PRIMARY KEY,
                         nom             VARCHAR(50) NOT NULL,
                         type1           VARCHAR(20) NOT NULL,
                         type2           VARCHAR(20),
                         hp              INTEGER NOT NULL CHECK (hp >= 0),
                         attaque         INTEGER NOT NULL CHECK (attaque >= 0),
                         defense         INTEGER NOT NULL CHECK (defense >= 0),
                         attaque_spe     INTEGER NOT NULL CHECK (attaque_spe >= 0),
                         defense_spe     INTEGER NOT NULL CHECK (defense_spe >= 0),
                         vitesse         INTEGER NOT NULL CHECK (vitesse >= 0),
                         sprite_url      VARCHAR(255),
                         date_capture    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- quelques données de démo pour tester rapidement l'application
-- sans avoir à faire des recherches api au premier lancement
INSERT INTO pokemon (id, nom, type1, type2, hp, attaque, defense, attaque_spe, defense_spe, vitesse, sprite_url)
VALUES
    (25, 'pikachu', 'electric', NULL, 35, 55, 40, 50, 50, 90,
     'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png'),
    (6, 'charizard', 'fire', 'flying', 78, 84, 78, 109, 85, 100,
     'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/6.png'),
    (1, 'bulbasaur', 'grass', 'poison', 45, 49, 49, 65, 65, 45,
     'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png');