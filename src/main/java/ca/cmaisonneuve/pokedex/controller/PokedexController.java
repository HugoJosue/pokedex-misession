package ca.cmaisonneuve.pokedex.controller;

import ca.cmaisonneuve.pokedex.dao.PokemonDAO;
import ca.cmaisonneuve.pokedex.model.Pokemon;
import ca.cmaisonneuve.pokedex.service.PokemonApiService;
import ca.cmaisonneuve.pokedex.service.PokemonNonTrouveException;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PokedexController {

    // le controller a besoin du service (pour aller chercher sur internet)
    // et du dao (pour sauvegarder / lire la base de données)
    private final PokemonApiService apiService = new PokemonApiService();
    private final PokemonDAO dao = new PokemonDAO();

    // recherche un pokémon en arrière-plan, sans geler l'interface
    // nomOuId : ce que l'utilisateur a tapé
    // boutonRechercher : pour le désactiver pendant le chargement
    // labelNom : pour afficher un message de statut ou d'erreur
    public void rechercherPokemon(String nomOuId, Button boutonRechercher, Label labelNom) {

        if (nomOuId == null || nomOuId.isBlank()) {
            labelNom.setText("Veuillez entrer un nom ou un id.");
            return;
        }

        // on désactive le bouton tout de suite pour éviter les doubles clics
        // pendant que l'appel réseau est en cours
        boutonRechercher.setDisable(true);
        labelNom.setText("Recherche en cours...");

        // le vrai travail (appel réseau) se fait dans un thread séparé
        // pour ne pas bloquer l'interface pendant l'attente
        Thread threadRecherche = new Thread(() -> {
            try {
                Pokemon pokemon = apiService.rechercher(nomOuId);

                // on sauvegarde tout de suite en BD (UPSERT)
                dao.sauvegarder(pokemon);

                // retour sur le thread JavaFX pour mettre à jour l'interface
                Platform.runLater(() -> {
                    labelNom.setText(pokemon.nom());
                    boutonRechercher.setDisable(false);
                });

            } catch (PokemonNonTrouveException e) {
                Platform.runLater(() -> {
                    labelNom.setText("Pokémon introuvable : " + nomOuId);
                    boutonRechercher.setDisable(false);
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    labelNom.setText("Erreur : " + e.getMessage());
                    boutonRechercher.setDisable(false);
                });
            }
        });

        threadRecherche.start();
    }
}
