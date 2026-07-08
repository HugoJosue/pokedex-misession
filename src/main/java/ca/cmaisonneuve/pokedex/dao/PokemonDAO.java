package ca.cmaisonneuve.pokedex.dao;

import ca.cmaisonneuve.pokedex.db.DatabaseConnection;
import ca.cmaisonneuve.pokedex.model.Pokemon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PokemonDAO {

    public void sauvegarder(Pokemon pokemon) throws SQLException {
        String sql = """
                INSERT INTO pokemon (id, nom, type1, type2, hp, attaque, defense, attaque_spe, defense_spe, vitesse, sprite_url)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON CONFLICT (id)
                DO UPDATE SET
                    nom = EXCLUDED.nom,
                    type1 = EXCLUDED.type1,
                    type2 = EXCLUDED.type2,
                    hp = EXCLUDED.hp,
                    attaque = EXCLUDED.attaque,
                    defense = EXCLUDED.defense,
                    attaque_spe = EXCLUDED.attaque_spe,
                    defense_spe = EXCLUDED.defense_spe,
                    vitesse = EXCLUDED.vitesse,
                    sprite_url = EXCLUDED.sprite_url
                """;

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement stmt = connexion.prepareStatement(sql)) {

            stmt.setInt(1, pokemon.id());
            stmt.setString(2, pokemon.nom());
            stmt.setString(3, pokemon.type1());
            stmt.setString(4, pokemon.type2());
            stmt.setInt(5, pokemon.hp());
            stmt.setInt(6, pokemon.attaque());
            stmt.setInt(7, pokemon.defense());
            stmt.setInt(8, pokemon.attaqueSpe());
            stmt.setInt(9, pokemon.defenseSpe());
            stmt.setInt(10, pokemon.vitesse());
            stmt.setString(11, pokemon.spriteUrl());

            stmt.executeUpdate();
        }
    }

    public List<Pokemon> listerTous() throws SQLException {
        String sql = "SELECT * FROM pokemon ORDER BY id";
        List<Pokemon> pokemons = new ArrayList<>();

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement stmt = connexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pokemon pokemon = new Pokemon(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("type1"),
                        rs.getString("type2"),
                        rs.getInt("hp"),
                        rs.getInt("attaque"),
                        rs.getInt("defense"),
                        rs.getInt("attaque_spe"),
                        rs.getInt("defense_spe"),
                        rs.getInt("vitesse"),
                        rs.getString("sprite_url")
                );
                pokemons.add(pokemon);
            }
        }

        return pokemons;
    }

    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM pokemon WHERE id = ?";

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement stmt = connexion.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
