package Controleur;

import Model.AppModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class MainControleur  {

    private Stage primaryStage;
    private AppModel model;
    @FXML
    private Button creerGraphe;

    @FXML
    private Button jouer;
    @FXML
    private Button rtnMenuPrncpBtn;
    @FXML
    private Button addNode;

    public void initialize (){
        creerGraphe.setOnAction(this::creerGrapheBtnHand);
        jouer.setOnAction(this::jouerBtnHand);


    }

    public void creerGrapheBtnHand(ActionEvent event){
        FXMLLoader loader =new FXMLLoader();
        CreerGrapheControleur controleur=new CreerGrapheControleur(model, primaryStage);
        loader.setController(controleur);
        loader.setLocation(getClass().getResource("../Vue/CreerGraphe.fxml"));
        Parent root=new Parent() {
        };
        try {
            root=loader.load();
        }catch (Exception e){
            e.printStackTrace();
        }

        primaryStage.setScene(new Scene(root));

    }
    public void jouerBtnHand(ActionEvent event){
        FXMLLoader loader =new FXMLLoader();
        ChoixGrapheJeuControleur controleur=new ChoixGrapheJeuControleur(model, primaryStage);
        loader.setController(controleur);
        loader.setLocation(getClass().getResource("../Vue/ChoixGrapheJeu.fxml"));
        Parent root=new Parent() {
        };
        try {
            root=loader.load();
        }catch (Exception e){
            e.printStackTrace();
        }

        primaryStage.setScene(new Scene(root));

    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public void setModel(AppModel m){
        this.model=m;

    }


}
