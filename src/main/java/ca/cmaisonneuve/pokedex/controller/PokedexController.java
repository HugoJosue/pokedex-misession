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

    private List<Pokemon> pokemonsCourants;
    private Pokemon pokemonAffiche;

    public void rechercherPokemon(String nomOuId, Button boutonRechercher, Label labelNom, Label idPokemon,
                                  ImageView imagePokemon, ListView<String> listePokemons,
                                  Label badgeType1, Label badgeType2,
                                  ProgressBar barreHp, ProgressBar barreAttaque, ProgressBar barreDefense,
                                  ProgressBar barreAttaqueSpe, ProgressBar barreDefenseSpe, ProgressBar barreVitesse,
                                  Label valeurHp, Label valeurAttaque, Label valeurDefense,
                                  Label valeurAttaqueSpe, Label valeurDefenseSpe, Label valeurVitesse) {

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

                    afficherPokemon(pokemon, labelNom, idPokemon, imagePokemon, badgeType1, badgeType2,
                            barreHp, barreAttaque, barreDefense, barreAttaqueSpe, barreDefenseSpe, barreVitesse,
                            valeurHp, valeurAttaque, valeurDefense, valeurAttaqueSpe, valeurDefenseSpe, valeurVitesse);

                    rafraichirListe(listePokemons, tousLesPokemons);

                    boutonRechercher.setDisable(false);
                });

            } catch (PokemonNonTrouveException e) {
                Platform.runLater(() -> {
                    labelNom.setText("Pokémon introuvable : " + nomOuId);
                    boutonRechercher.setDisable(false);
                });

            } catch (java.net.http.HttpTimeoutException e) {
                Platform.runLater(() -> {
                    labelNom.setText("Le serveur PokéAPI met trop de temps à répondre. Réessayez.");
                    boutonRechercher.setDisable(false);
                });

            } catch (java.net.ConnectException | java.net.UnknownHostException e) {
                Platform.runLater(() -> {
                    labelNom.setText("Pas de connexion internet. Vérifiez votre réseau.");
                    boutonRechercher.setDisable(false);
                });

            } catch (java.sql.SQLException e) {
                Platform.runLater(() -> {
                    labelNom.setText("Impossible de joindre la base de données. Vérifiez que PostgreSQL est démarré.");
                    boutonRechercher.setDisable(false);
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    labelNom.setText("Erreur inattendue : " + e.getMessage());
                    boutonRechercher.setDisable(false);
                });
            }
        });

        threadRecherche.start();
    }

    public void afficherPokemonSelectionne(String nom, Label labelNom, Label idPokemon, ImageView imagePokemon,
                                           Label badgeType1, Label badgeType2,
                                           ProgressBar barreHp, ProgressBar barreAttaque, ProgressBar barreDefense,
                                           ProgressBar barreAttaqueSpe, ProgressBar barreDefenseSpe, ProgressBar barreVitesse,
                                           Label valeurHp, Label valeurAttaque, Label valeurDefense,
                                           Label valeurAttaqueSpe, Label valeurDefenseSpe, Label valeurVitesse) {

        if (pokemonsCourants == null || nom == null) {
            return;
        }

        for (Pokemon p : pokemonsCourants) {
            if (p.nom().equals(nom)) {
                afficherPokemon(p, labelNom, idPokemon, imagePokemon, badgeType1, badgeType2,
                        barreHp, barreAttaque, barreDefense, barreAttaqueSpe, barreDefenseSpe, barreVitesse,
                        valeurHp, valeurAttaque, valeurDefense, valeurAttaqueSpe, valeurDefenseSpe, valeurVitesse);
                return;
            }
        }
    }

    public void supprimerPokemonAffiche(Label labelNom, Label idPokemon, ImageView imagePokemon,
                                        ListView<String> listePokemons, Label badgeType1, Label badgeType2,
                                        ProgressBar barreHp, ProgressBar barreAttaque, ProgressBar barreDefense,
                                        ProgressBar barreAttaqueSpe, ProgressBar barreDefenseSpe, ProgressBar barreVitesse,
                                        Label valeurHp, Label valeurAttaque, Label valeurDefense,
                                        Label valeurAttaqueSpe, Label valeurDefenseSpe, Label valeurVitesse) {

        if (pokemonAffiche == null) {
            return;
        }

        Thread threadSuppression = new Thread(() -> {
            try {
                dao.supprimer(pokemonAffiche.id());
                List<Pokemon> tousLesPokemons = dao.listerTous();

                Platform.runLater(() -> {
                    rafraichirListe(listePokemons, tousLesPokemons);

                    labelNom.setText("Aucun pokémon sélectionné");
                    idPokemon.setText("");
                    imagePokemon.setImage(null);

                    barreHp.setProgress(0);
                    barreAttaque.setProgress(0);
                    barreDefense.setProgress(0);
                    barreAttaqueSpe.setProgress(0);
                    barreDefenseSpe.setProgress(0);
                    barreVitesse.setProgress(0);

                    valeurHp.setText("0");
                    valeurAttaque.setText("0");
                    valeurDefense.setText("0");
                    valeurAttaqueSpe.setText("0");
                    valeurDefenseSpe.setText("0");
                    valeurVitesse.setText("0");

                    badgeType1.setVisible(false);
                    badgeType2.setVisible(false);

                    pokemonAffiche = null;
                    pokemonsCourants = tousLesPokemons;
                });

            } catch (java.sql.SQLException e) {
                Platform.runLater(() ->
                        labelNom.setText("Impossible de supprimer : la base de données est inaccessible."));

            } catch (Exception e) {
                Platform.runLater(() -> labelNom.setText("Erreur inattendue lors de la suppression : " + e.getMessage()));
            }
        });

        threadSuppression.start();
    }

    private void afficherPokemon(Pokemon pokemon, Label labelNom, Label idPokemon, ImageView imagePokemon,
                                 Label badgeType1, Label badgeType2,
                                 ProgressBar barreHp, ProgressBar barreAttaque, ProgressBar barreDefense,
                                 ProgressBar barreAttaqueSpe, ProgressBar barreDefenseSpe, ProgressBar barreVitesse,
                                 Label valeurHp, Label valeurAttaque, Label valeurDefense,
                                 Label valeurAttaqueSpe, Label valeurDefenseSpe, Label valeurVitesse) {

        this.pokemonAffiche = pokemon;

        idPokemon.setText(String.format("#%03d", pokemon.id()));

        labelNom.setText(capitaliser(pokemon.nom()));
        imagePokemon.setImage(new Image(pokemon.spriteUrl(), true));

        appliquerBadgeType(badgeType1, pokemon.type1());

        if (pokemon.type2() != null) {
            appliquerBadgeType(badgeType2, pokemon.type2());
        } else {
            badgeType2.setVisible(false);
        }

        barreHp.setProgress(pokemon.hp() / STAT_MAX);
        barreAttaque.setProgress(pokemon.attaque() / STAT_MAX);
        barreDefense.setProgress(pokemon.defense() / STAT_MAX);
        barreAttaqueSpe.setProgress(pokemon.attaqueSpe() / STAT_MAX);
        barreDefenseSpe.setProgress(pokemon.defenseSpe() / STAT_MAX);
        barreVitesse.setProgress(pokemon.vitesse() / STAT_MAX);

        valeurHp.setText(String.valueOf(pokemon.hp()));
        valeurAttaque.setText(String.valueOf(pokemon.attaque()));
        valeurDefense.setText(String.valueOf(pokemon.defense()));
        valeurAttaqueSpe.setText(String.valueOf(pokemon.attaqueSpe()));
        valeurDefenseSpe.setText(String.valueOf(pokemon.defenseSpe()));
        valeurVitesse.setText(String.valueOf(pokemon.vitesse()));
    }

    private String capitaliser(String texte) {
        if (texte == null || texte.isEmpty()) {
            return texte;
        }
        return texte.substring(0, 1).toUpperCase() + texte.substring(1);
    }

    private void appliquerBadgeType(Label badge, String type) {
        badge.setText(type.toUpperCase());
        badge.getStyleClass().removeIf(classe -> classe.startsWith("type-"));
        badge.getStyleClass().add("type-badge");
        badge.getStyleClass().add("type-" + type);
        badge.setVisible(true);
    }

    private void rafraichirListe(ListView<String> listePokemons, List<Pokemon> pokemons) {
        ObservableList<String> noms = FXCollections.observableArrayList();
        for (Pokemon p : pokemons) {
            noms.add(p.nom());
        }
        listePokemons.setItems(noms);
    }
}