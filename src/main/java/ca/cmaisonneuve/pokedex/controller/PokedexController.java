package ca.cmaisonneuve.pokedex.controller;

import ca.cmaisonneuve.pokedex.dao.PokemonDAO;
import ca.cmaisonneuve.pokedex.model.Pokemon;
import ca.cmaisonneuve.pokedex.service.PokemonApiService;
import ca.cmaisonneuve.pokedex.service.PokemonNonTrouveException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class PokedexController {

    private final PokemonApiService apiService = new PokemonApiService();
    private final PokemonDAO dao = new PokemonDAO();

    private static final double STAT_MAX = 255.0;

    // garde en mémoire la dernière liste chargée depuis la bd
    // pour pouvoir retrouver un pokémon par son nom quand on clique dessus dans la liste
    private List<Pokemon> pokemonsCourants;

    public void rechercherPokemon(String nomOuId, Button boutonRechercher, Label labelNom,
                                  ImageView imagePokemon, ListView<String> listePokemons,
                                  ProgressBar barreHp, ProgressBar barreAttaque, ProgressBar barreDefense,
                                  ProgressBar barreAttaqueSpe, ProgressBar barreDefenseSpe, ProgressBar barreVitesse) {

        if (nomOuId == null || nomOuId.isBlank()) {
            labelNom.setText("Veuillez entrer un nom ou un id.");
            return;
        }

        boutonRechercher.setDisable(true);
        labelNom.setText("Recherche en cours...");

        Thread threadRecherche = new Thread(() -> {
            try {
                Pokemon pokemon = apiService.rechercher(nomOuId);
                dao.sauvegarder(pokemon);

                List<Pokemon> tousLesPokemons = dao.listerTous();

                Platform.runLater(() -> {
                    pokemonsCourants = tousLesPokemons;

                    afficherPokemon(pokemon, labelNom, imagePokemon,
                            barreHp, barreAttaque, barreDefense, barreAttaqueSpe, barreDefenseSpe, barreVitesse);

                    ObservableList<String> noms = FXCollections.observableArrayList();
                    for (Pokemon p : tousLesPokemons) {
                        noms.add(p.nom());
                    }
                    listePokemons.setItems(noms);

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

    // appelée quand l'utilisateur clique sur un nom dans la listview
    // pas besoin de thread ici : les données sont déjà en mémoire, aucun appel réseau
    public void afficherPokemonSelectionne(String nom, Label labelNom, ImageView imagePokemon,
                                           ProgressBar barreHp, ProgressBar barreAttaque, ProgressBar barreDefense,
                                           ProgressBar barreAttaqueSpe, ProgressBar barreDefenseSpe, ProgressBar barreVitesse) {

        if (pokemonsCourants == null || nom == null) {
            return;
        }

        // on cherche le pokémon correspondant au nom cliqué dans la liste en mémoire
        for (Pokemon p : pokemonsCourants) {
            if (p.nom().equals(nom)) {
                afficherPokemon(p, labelNom, imagePokemon,
                        barreHp, barreAttaque, barreDefense, barreAttaqueSpe, barreDefenseSpe, barreVitesse);
                return;
            }
        }
    }

    // méthode commune qui met à jour la carte info
    // utilisée à la fois après une recherche et après un clic dans la liste
    // évite de dupliquer ce code à deux endroits
    private void afficherPokemon(Pokemon pokemon, Label labelNom, ImageView imagePokemon,
                                 ProgressBar barreHp, ProgressBar barreAttaque, ProgressBar barreDefense,
                                 ProgressBar barreAttaqueSpe, ProgressBar barreDefenseSpe, ProgressBar barreVitesse) {

        labelNom.setText(pokemon.nom());
        imagePokemon.setImage(new Image(pokemon.spriteUrl(), true));

        barreHp.setProgress(pokemon.hp() / STAT_MAX);
        barreAttaque.setProgress(pokemon.attaque() / STAT_MAX);
        barreDefense.setProgress(pokemon.defense() / STAT_MAX);
        barreAttaqueSpe.setProgress(pokemon.attaqueSpe() / STAT_MAX);
        barreDefenseSpe.setProgress(pokemon.defenseSpe() / STAT_MAX);
        barreVitesse.setProgress(pokemon.vitesse() / STAT_MAX);
    }
}