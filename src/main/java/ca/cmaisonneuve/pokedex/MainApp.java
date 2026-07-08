package ca.cmaisonneuve.pokedex;

import ca.cmaisonneuve.pokedex.controller.PokedexController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    private final PokedexController controller = new PokedexController();

    @Override
    public void start(Stage stagePrincipal) {
        BorderPane racine = new BorderPane();

        // zone de recherche
        TextField champRecherche = new TextField();
        champRecherche.setPromptText("Nom ou id du pokémon...");

        Button boutonRechercher = new Button("Rechercher");

        HBox barreRecherche = new HBox(10, champRecherche, boutonRechercher);
        barreRecherche.setPadding(new Insets(10));

        racine.setTop(barreRecherche);

        // liste des pokémons capturés
        ListView<String> listePokemons = new ListView<>();
        listePokemons.setPrefWidth(200);

        racine.setLeft(listePokemons);

        // --- carte info au centre ---

        ImageView imagePokemon = new ImageView();
        imagePokemon.setFitWidth(200);
        imagePokemon.setFitHeight(200);
        imagePokemon.setPreserveRatio(true);

        Label labelNom = new Label("Aucun pokémon sélectionné");
        labelNom.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane grilleStats = new GridPane();
        grilleStats.setHgap(10);
        grilleStats.setVgap(8);
        grilleStats.setPadding(new Insets(15, 0, 0, 0));

        ProgressBar barreHp = new ProgressBar(0);
        ProgressBar barreAttaque = new ProgressBar(0);
        ProgressBar barreDefense = new ProgressBar(0);
        ProgressBar barreAttaqueSpe = new ProgressBar(0);
        ProgressBar barreDefenseSpe = new ProgressBar(0);
        ProgressBar barreVitesse = new ProgressBar(0);

        ajouterLigneStat(grilleStats, 0, "HP", barreHp);
        ajouterLigneStat(grilleStats, 1, "Attaque", barreAttaque);
        ajouterLigneStat(grilleStats, 2, "Défense", barreDefense);
        ajouterLigneStat(grilleStats, 3, "Att. Spé", barreAttaqueSpe);
        ajouterLigneStat(grilleStats, 4, "Déf. Spé", barreDefenseSpe);
        ajouterLigneStat(grilleStats, 5, "Vitesse", barreVitesse);

        VBox carteInfo = new VBox(15, imagePokemon, labelNom, grilleStats);
        carteInfo.setPadding(new Insets(20));
        carteInfo.setStyle("-fx-alignment: center;");

        racine.setCenter(carteInfo);

        // on passe maintenant tous les éléments dont le controller a besoin
        // pour mettre à jour l'image, les stats et la liste
        boutonRechercher.setOnAction(evenement -> {
            String texteRecherche = champRecherche.getText();
            controller.rechercherPokemon(texteRecherche, boutonRechercher, labelNom,
                    imagePokemon, listePokemons,
                    barreHp, barreAttaque, barreDefense, barreAttaqueSpe, barreDefenseSpe, barreVitesse);
        });
        // quand on clique sur un nom dans la liste, on réaffiche ce pokémon
        // (données déjà en mémoire, pas besoin de rappeler l'api)
        listePokemons.setOnMouseClicked(evenement -> {
            String nomSelectionne = listePokemons.getSelectionModel().getSelectedItem();
            controller.afficherPokemonSelectionne(nomSelectionne, labelNom, imagePokemon,
                    barreHp, barreAttaque, barreDefense, barreAttaqueSpe, barreDefenseSpe, barreVitesse);
        });
        Scene scene = new Scene(racine, 900, 600);

        stagePrincipal.setTitle("Pokédex");
        stagePrincipal.setScene(scene);
        stagePrincipal.show();
    }

    private void ajouterLigneStat(GridPane grille, int ligne, String nomStat, ProgressBar barre) {
        Label label = new Label(nomStat);
        label.setPrefWidth(80);

        barre.setPrefWidth(200);

        grille.add(label, 0, ligne);
        grille.add(barre, 1, ligne);
    }

    public static void main(String[] args) {
        launch(args);
    }
}