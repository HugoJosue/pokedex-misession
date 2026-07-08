package ca.cmaisonneuve.pokedex.service;

import ca.cmaisonneuve.pokedex.model.Pokemon;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PokemonApiService {

    private static final String BASE_URL = "https://pokeapi.co/api/v2/pokemon/";

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public Pokemon rechercher(String nomOuId) throws Exception {
        String url = BASE_URL + nomOuId.toLowerCase();

        HttpRequest requete = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> reponse = client.send(requete, HttpResponse.BodyHandlers.ofString());

        if (reponse.statusCode() == 404) {
            throw new PokemonNonTrouveException("Aucun pokémon trouvé pour : " + nomOuId);
        }

        if (reponse.statusCode() != 200) {
            throw new RuntimeException("Erreur API, code : " + reponse.statusCode());
        }

        JsonNode racine = mapper.readTree(reponse.body());

        return convertirEnPokemon(racine);
    }

    private Pokemon convertirEnPokemon(JsonNode racine) {
        int id = racine.get("id").asInt();
        String nom = racine.get("name").asText();

        JsonNode typesNode = racine.get("types");
        String type1 = typesNode.get(0).get("type").get("name").asText();
        String type2 = typesNode.size() > 1
                ? typesNode.get(1).get("type").get("name").asText()
                : null;

        JsonNode statsNode = racine.get("stats");
        int hp = trouverStat(statsNode, "hp");
        int attaque = trouverStat(statsNode, "attack");
        int defense = trouverStat(statsNode, "defense");
        int attaqueSpe = trouverStat(statsNode, "special-attack");
        int defenseSpe = trouverStat(statsNode, "special-defense");
        int vitesse = trouverStat(statsNode, "speed");

        String spriteUrl = racine.get("sprites")
                .get("other")
                .get("official-artwork")
                .get("front_default")
                .asText();

        return new Pokemon(id, nom, type1, type2, hp, attaque, defense, attaqueSpe, defenseSpe, vitesse, spriteUrl);
    }

    private int trouverStat(JsonNode statsNode, String nomStat) {
        for (JsonNode statEntry : statsNode) {
            String nom = statEntry.get("stat").get("name").asText();
            if (nom.equals(nomStat)) {
                return statEntry.get("base_stat").asInt();
            }
        }
        return 0;
    }
}