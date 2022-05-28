
import Controleur.MainControleur;

import Model.AppModel;
import Model.Jeu;
import Util.Util;

import java.util.LinkedList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private AppModel model;
    private MainControleur controleur;
    private Stage stage;
    private Scene scenePrincipale;

    @Override
    public void init() throws Exception{
        model =new AppModel();
        model.setListeGraphes(Util.loadGraphes());
        LinkedList<Jeu> loadedGames=Util.loadJeux();
        model.setListeJeux(loadedGames);
        controleur=new MainControleur();
    }
    public static void main(String []args){
        launch(args);
    }

    @Override
    public void start(javafx.stage.Stage stage) throws Exception {
        this.stage=stage;
        FXMLLoader loader =new FXMLLoader();
        loader.setController(controleur);
        loader.setLocation(getClass().getResource("Vue/MainMenu.fxml"));
        controleur.setPrimaryStage(stage);
        controleur.setModel(model);
        Parent root=loader.load();
        scenePrincipale=new javafx.scene.Scene(root);
        stage.setTitle("Jouons dans les Graphes");
        stage.setScene(scenePrincipale);
        stage.show();

    }

}
