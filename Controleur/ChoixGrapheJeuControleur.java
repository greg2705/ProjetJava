package Controleur;

import Model.AppModel;
import Model.Graphe;
import Model.Joueur;
import Model.Jeu;
import Util.Util;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;




public class ChoixGrapheJeuControleur {
    private Stage primaryStage;
    private AppModel model;
    @FXML Button rtnMenuPrincipBtn;
    @FXML Button choixGraphe;
    @FXML Button playBtn;
    @FXML ChoiceBox jeuChoiceBox;
    @FXML ChoiceBox grapheChoiceBox;
    @FXML VBox nomsJoueursVB;



    public ChoixGrapheJeuControleur(AppModel mod, Stage stagePrincipale){
        this.model=mod;
        this.primaryStage=stagePrincipale;
    }
    public void initialize(){
        model.setListeJeux(Util.loadJeux());
        for (Jeu jeu : model.getListeJeux()){
            jeuChoiceBox.getItems().add(jeu);
        }
        rtnMenuPrincipBtn.setOnAction(this::rtnMenuPrincipHand);
        grapheChoiceBox.setOnAction(this::grapheChoiceBoxHand);
        jeuChoiceBox.setOnAction(this::jeuChoiceBoxHand);
        playBtn.setDisable(true);
        playBtn.setOnAction(this::platBtnHand);
        grapheChoiceBox.setDisable(true);


    }
    private void jeuChoiceBoxHand(Event event){

        if (jeuChoiceBox.getValue()== null){
            grapheChoiceBox.setDisable(true);
            return;
        }
        grapheChoiceBox.setDisable(false);
        Jeu selectedGame= (Jeu) jeuChoiceBox.getValue();
        grapheChoiceBox.getItems().clear();
        model.setListeGraphes(Util.loadGraphes());//le clonage des graphes est délicat a cause des références circulaires
        for (Graphe graphe : model.getListeGraphes()){
            if (selectedGame.grapheIsOk(graphe)){
                grapheChoiceBox.getItems().add(graphe);
            }
        }
        afficherSaisieNomsJ();

    }
    private void grapheChoiceBoxHand(Event event){
        if (grapheChoiceBox.getValue() != null){
            playBtn.setDisable(false);
        }
    }
    private void platBtnHand(ActionEvent event){
        Jeu selectedJeu= (Jeu) jeuChoiceBox.getValue();
        Graphe selectedGraphe=(Graphe) grapheChoiceBox.getValue();

        selectedJeu.setGraphe(selectedGraphe);
        Joueur[] joueurs = selectedJeu.getJoueurs();
        for(int i=0;i<nomsJoueursVB.getChildren().size();i++){
            TextField champ= (TextField) nomsJoueursVB.getChildren().get(i);
            if(champ.getText() != null && champ.getText().length()!= 0){
                joueurs[i].setNom(champ.getText());
            }else{
                joueurs[i].setNom("joueur "+Integer.toString((i+1)));
            }
        }
        GameplayControleur controleurDeJeu=new GameplayControleur(primaryStage,model, selectedJeu, selectedGraphe);
        FXMLLoader loader =new FXMLLoader();
        loader.setController(controleurDeJeu);
        loader.setLocation(getClass().getResource("../Vue/Gameplay.fxml"));
        Parent root=new Parent() {
        };
        try {
            root=loader.load();
        }catch (Exception e){
            e.printStackTrace();
        }
        primaryStage.setScene(new Scene(root));
        controleurDeJeu.updateVue();
    }
    private void rtnMenuPrincipHand(ActionEvent event){
        FXMLLoader loader =new FXMLLoader();
        MainControleur mainControleur=new MainControleur();
        mainControleur.setPrimaryStage(primaryStage);
        mainControleur.setModel(model);
        loader.setController(mainControleur);
        loader.setLocation(getClass().getResource("../Vue/MainMenu.fxml"));
        Parent root=new Parent() {};
        try {
            root=loader.load();
        }catch (Exception e){
            e.printStackTrace();
        }
        primaryStage.setScene(new Scene(root));

    }
    private void afficherSaisieNomsJ(){
        if(jeuChoiceBox.getValue()!= null) {
            nomsJoueursVB.getChildren().clear();
            Jeu jeuChoisi=(Jeu) jeuChoiceBox.getValue();
            for (int i = 1; i < jeuChoisi.getJoueurs().length+1; i++){
                TextField champ =new TextField();
                champ.setPromptText("Nom joueur "+i);
                nomsJoueursVB.getChildren().add(champ);
            }
        }
    }
}
