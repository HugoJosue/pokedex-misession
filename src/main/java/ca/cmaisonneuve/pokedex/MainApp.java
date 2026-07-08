package ca.cmaisonneuve.pokedex;

import ca.cmaisonneuve.pokedex.controller.PokedexController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class MainApp extends Application {

    private final PokedexController controller = new PokedexController();

    private final Label valeurHp = new Label("0");
    private final Label valeurAttaque = new Label("0");
    private final Label valeurDefense = new Label("0");
    private final Label valeurAttaqueSpe = new Label("0");
    private final Label valeurDefenseSpe = new Label("0");
    private final Label valeurVitesse = new Label("0");

    @Override
    public void start(Stage stagePrincipal) {
        BorderPane racine = new BorderPane();

        // --- header ---

        Region pokeballIcone = new Region();
        pokeballIcone.getStyleClass().add("pokeball-icone");

        Label titreApp = new Label("Pokédex");
        titreApp.getStyleClass().add("titre-app");

        HBox header = new HBox(12, pokeballIcone, titreApp);
        header.getStyleClass().add("header");
        header.setAlignment(Pos.CENTER_LEFT);

        // --- zone de recherche ---

        TextField champRecherche = new TextField();
        champRecherche.setPromptText("Nom ou id du pokémon...");

        Button boutonRechercher = new Button("Rechercher");

        HBox barreRecherche = new HBox(10, champRecherche, boutonRechercher);
        barreRecherche.getStyleClass().add("barre-recherche");
        barreRecherche.setAlignment(Pos.CENTER);

        VBox zoneHaut = new VBox(header, barreRecherche);
        racine.setTop(zoneHaut);

        // --- liste des pokémons capturés ---

        Label titreListe = new Label("CAPTURÉS");
        titreListe.getStyleClass().add("titre-section");

        ListView<String> listePokemons = new ListView<>();
        listePokemons.setPrefWidth(230);

        VBox panneauListe = new VBox(titreListe, listePokemons);
        panneauListe.getStyleClass().add("panneau-liste");
        VBox.setVgrow(listePokemons, Priority.ALWAYS);

        racine.setLeft(panneauListe);

        // --- carte info au centre ---

        ImageView imagePokemon = new ImageView();
        imagePokemon.setFitWidth(260);
        imagePokemon.setFitHeight(260);
        imagePokemon.setPreserveRatio(true);
        imagePokemon.getStyleClass().add("image-pokemon");

        Label idPokemon = new Label("");
        idPokemon.getStyleClass().add("id-pokemon");

        Label labelNom = new Label("Aucun pokémon sélectionné");
        labelNom.getStyleClass().add("nom-pokemon");
        labelNom.setWrapText(true);
        labelNom.setAlignment(Pos.CENTER);

        Label badgeType1 = new Label();
        Label badgeType2 = new Label();
        badgeType1.setVisible(false);
        badgeType2.setVisible(false);

        HBox lignesTypes = new HBox(8, badgeType1, badgeType2);
        lignesTypes.setAlignment(Pos.CENTER);

        Region separateur = new Region();
        separateur.getStyleClass().add("separateur");
        separateur.setPrefWidth(300);

        Label titreStats = new Label("STATISTIQUES DE BASE");
        titreStats.getStyleClass().add("titre-stats");

        GridPane grilleStats = new GridPane();
        grilleStats.setHgap(14);
        grilleStats.setVgap(12);
        grilleStats.setPadding(new Insets(10, 0, 0, 0));

        ProgressBar barreHp = new ProgressBar(0);
        ProgressBar barreAttaque = new ProgressBar(0);
        ProgressBar barreDefense = new ProgressBar(0);
        ProgressBar barreAttaqueSpe = new ProgressBar(0);
        ProgressBar barreDefenseSpe = new ProgressBar(0);
        ProgressBar barreVitesse = new ProgressBar(0);

        ajouterLigneStat(grilleStats, 0, "HP", barreHp, valeurHp);
        ajouterLigneStat(grilleStats, 1, "ATTAQUE", barreAttaque, valeurAttaque);
        ajouterLigneStat(grilleStats, 2, "DÉFENSE", barreDefense, valeurDefense);
        ajouterLigneStat(grilleStats, 3, "ATT. SPÉ", barreAttaqueSpe, valeurAttaqueSpe);
        ajouterLigneStat(grilleStats, 4, "DÉF. SPÉ", barreDefenseSpe, valeurDefenseSpe);
        ajouterLigneStat(grilleStats, 5, "VITESSE", barreVitesse, valeurVitesse);

        Button boutonSupprimer = new Button("Supprimer");
        boutonSupprimer.getStyleClass().add("bouton-supprimer");

        VBox carteInfo = new VBox(14, imagePokemon, idPokemon, labelNom, lignesTypes,
                separateur, titreStats, grilleStats, boutonSupprimer);
        carteInfo.getStyleClass().add("carte-info");
        carteInfo.setAlignment(Pos.CENTER);
        carteInfo.setPadding(new Insets(36));
        carteInfo.setMaxWidth(480);
        carteInfo.setMaxHeight(Region.USE_PREF_SIZE);

        // grande pokéball décorative derrière la carte, purement esthétique
        StackPane zoneCentrale = new StackPane();
        zoneCentrale.getChildren().add(creerPokeballDecorative(340));
        zoneCentrale.getChildren().add(carteInfo);

        // petites pokéballs décoratives dans les coins pour combler le vide
        Circle petiteDeco1 = creerPokeballDecorative(90);
        StackPane.setAlignment(petiteDeco1, Pos.TOP_RIGHT);
        petiteDeco1.setTranslateX(-40);
        petiteDeco1.setTranslateY(40);

        Circle petiteDeco2 = creerPokeballDecorative(70);
        StackPane.setAlignment(petiteDeco2, Pos.BOTTOM_LEFT);
        petiteDeco2.setTranslateX(60);
        petiteDeco2.setTranslateY(-40);

        zoneCentrale.getChildren().addAll(petiteDeco1, petiteDeco2);

        BorderPane conteneurCentre = new BorderPane();
        conteneurCentre.setCenter(zoneCentrale);
        conteneurCentre.setPadding(new Insets(20));

        racine.setCenter(conteneurCentre);

        // --- événements ---

        boutonRechercher.setOnAction(evenement -> {
            String texteRecherche = champRecherche.getText();
            controller.rechercherPokemon(texteRecherche, boutonRechercher, labelNom, idPokemon,
                    imagePokemon, listePokemons, badgeType1, badgeType2,
                    barreHp, barreAttaque, barreDefense, barreAttaqueSpe, barreDefenseSpe, barreVitesse,
                    valeurHp, valeurAttaque, valeurDefense, valeurAttaqueSpe, valeurDefenseSpe, valeurVitesse);
        });

        listePokemons.setOnMouseClicked(evenement -> {
            String nomSelectionne = listePokemons.getSelectionModel().getSelectedItem();
            controller.afficherPokemonSelectionne(nomSelectionne, labelNom, idPokemon, imagePokemon,
                    badgeType1, badgeType2,
                    barreHp, barreAttaque, barreDefense, barreAttaqueSpe, barreDefenseSpe, barreVitesse,
                    valeurHp, valeurAttaque, valeurDefense, valeurAttaqueSpe, valeurDefenseSpe, valeurVitesse);
        });

        boutonSupprimer.setOnAction(evenement -> {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de suppression");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Voulez-vous vraiment supprimer ce pokémon ?");

            if (confirmation.showAndWait().get() == ButtonType.OK) {
                controller.supprimerPokemonAffiche(labelNom, idPokemon, imagePokemon, listePokemons,
                        badgeType1, badgeType2,
                        barreHp, barreAttaque, barreDefense, barreAttaqueSpe, barreDefenseSpe, barreVitesse,
                        valeurHp, valeurAttaque, valeurDefense, valeurAttaqueSpe, valeurDefenseSpe, valeurVitesse);
            }
        });

        Scene scene = new Scene(racine, 1100, 720);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stagePrincipal.setTitle("Pokédex");
        stagePrincipal.setScene(scene);
        stagePrincipal.show();
    }

    private void ajouterLigneStat(GridPane grille, int ligne, String nomStat, ProgressBar barre, Label valeur) {
        Label label = new Label(nomStat);
        label.getStyleClass().add("label-stat");
        label.setPrefWidth(85);

        barre.setPrefWidth(220);

        valeur.getStyleClass().add("valeur-stat");
        valeur.setPrefWidth(35);

        grille.add(label, 0, ligne);
        grille.add(barre, 1, ligne);
        grille.add(valeur, 2, ligne);
    }

    // crée un simple cercle décoratif façon pokéball (juste le contour), purement visuel
    private Circle creerPokeballDecorative(double taille) {
        Circle cercle = new Circle(taille);
        cercle.getStyleClass().add("deco-pokeball");
        cercle.setMouseTransparent(true); // ne bloque jamais les clics sur les éléments en dessous
        return cercle;
    }

    public static void main(String[] args) {
        launch(args);
    }
}