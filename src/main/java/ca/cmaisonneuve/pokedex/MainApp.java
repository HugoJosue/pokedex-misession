package ca.cmaisonneuve.pokedex;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stagePrincipal) {
        // BorderPane divise la fenêtre en 5 zones (haut/bas/gauche/droite/centre)
        // pratique pour organiser recherche en haut, liste à gauche, carte info au centre
        BorderPane racine = new BorderPane();

        Scene scene = new Scene(racine, 900, 600);

        stagePrincipal.setTitle("Pokédex");
        stagePrincipal.setScene(scene);
        stagePrincipal.show();
    }

    // point d'entrée standard Java, mais on doit appeler launch()
    // pour démarrer le cycle de vie JavaFX (qui va ensuite appeler start())
    public static void main(String[] args) {
        launch(args);
    }
}