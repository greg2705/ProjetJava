package Controleur;

import Model.*;
import Util.Util;
import Vue.AreteGUI;
import Vue.GrapheDisplayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class GameplayControleur implements GrapheManager{
    private Stage primaryStage;// pointeur vers le Stage
    private Jeu jeuActuel;
    private Graphe grapheSupport;
    private AppModel model;
    private Etat etat;
    private GrapheDisplayer grapheDisplayer;
    private ToggleGroup tglGroup;
    @FXML private Label playerInfo;
    @FXML private Pane panneau;
    @FXML private Button rtnChoixJeu;
    @FXML private HBox pallete;
    @FXML private ToggleButton deplacerBtn;
    @FXML private ToggleButton deleteBtn;




    public GameplayControleur(Stage rootStage,AppModel mod, Jeu jeu, Graphe plateau){
        this.primaryStage=rootStage;
        this.model=mod;
        this.jeuActuel=jeu;
        this.grapheSupport=plateau;
        //nextPlayer=jeuActuel.getJoueurs()[0];
    }
    public void initialize() {
        tglGroup =new ToggleGroup();
        deplacerBtn.setToggleGroup(tglGroup);
        deleteBtn.setToggleGroup(tglGroup);
        playerInfo.setText(jeuActuel.getCurrentPlayer().toString());
        rtnChoixJeu.setOnAction(this::rtnChoixJeuHand);
        deleteBtn.setOnAction(this::supprBtnHand);
        deplacerBtn.setOnAction(this::deplacerBtnHand);
        deplacerBtn.setDisable(!jeuActuel.deplacementPermis());
        deleteBtn.setDisable(!jeuActuel.suppressionPermise());
        grapheDisplayer = new GrapheDisplayer(grapheSupport, panneau, this);
        etat=Etat.Coloriage;
    }
    public void updateVue(){
        grapheDisplayer.updateVue();
        String [] palleteNextPlayer=jeuActuel.getCurrentPlayer().getColor();

        ToggleButton btn ;
        pallete.getChildren().clear();
        for( int i=0;i<palleteNextPlayer.length;i++){
            btn= new ToggleButton(palleteNextPlayer[i]);
            btn.setToggleGroup(tglGroup);
            btn.getStyleClass().add(palleteNextPlayer[i]);
            btn.setOnAction(this::colorTglBtnHand);
            pallete.getChildren().add(btn);
        }
        this.playerInfo.setText("C'est au tour du joueur : "+jeuActuel.getCurrentPlayer().getNom());
    }

    public void rtnChoixJeuHand(ActionEvent event){
        FXMLLoader loader =new FXMLLoader();
        ChoixGrapheJeuControleur choixGrJeu=new ChoixGrapheJeuControleur(model,primaryStage);
        loader.setController(choixGrJeu);
        loader.setLocation(getClass().getResource("../Vue/ChoixGrapheJeu.fxml"));
        Parent root=new Parent() {};
        try {
            root=loader.load();
        }catch (Exception e){
            e.printStackTrace();
        }
        primaryStage.setScene(new Scene(root));
    }

    private void colorTglBtnHand(ActionEvent event){
        etat=Etat.Coloriage;
        grapheDisplayer.updateVue();
    }
    private void deplacerBtnHand(ActionEvent event){
        etat=Etat.Deplacement;
        grapheDisplayer.updateVue();
    }
    private void supprBtnHand(ActionEvent event){
        etat=Etat.Suppression;
        grapheDisplayer.updateVue();
    }

    @Override
    public void nodeClicked(Noeud target) {
        Joueur currentPlay=jeuActuel.getCurrentPlayer();
        switch (etat) {
            case Coloriage:
                ToggleButton selectedTgl = (ToggleButton) tglGroup.getSelectedToggle();
                if (selectedTgl != null && jeuActuel.peutJouer(currentPlay, target, selectedTgl.getText() )) {
                    jeuActuel.playingNode(currentPlay, target, selectedTgl.getText());
                    jeuActuel.consequence(currentPlay, target);
                }
                if (jeuActuel.partieTerminee()) {
                    updateVue();
                    annoncerGagnantQuitPart(jeuActuel.determinerGagnant().getNom());
                }
                break;
            case Suppression:
                grapheSupport.supprimerNoeud(target);
                if (!jeuActuel.partieTerminee()) {

                }else {
                    updateVue();
                    annoncerGagnantQuitPart(jeuActuel.determinerGagnant().getNom());
                }
                break;
            case Deplacement:
                if (!jeuActuel.partieTerminee()) {

                }else {
                    updateVue();
                    annoncerGagnantQuitPart(jeuActuel.determinerGagnant().getNom());
                }
                break;
        }
        updateVue();
    }

    @Override
    public void edgeClicked(AreteGUI edge) {
       if (jeuActuel.suppressionPermise() && etat == Etat.Suppression){
           grapheSupport.retirerArete(edge.getA(), edge.getB());

       }
        updateVue();
    }

    @Override
    public void nodeMoved(Noeud movedNode) {
        Joueur currentPlay=jeuActuel.getCurrentPlayer();
        jeuActuel.consequence(currentPlay, movedNode);
        updateVue();
    }

    @Override
    public void nodeDraged(Noeud dragedNode) {
    }

    @Override
    public void dragReleasedOnNode(Noeud targetNode) {
    }

    @Override
    public boolean isMovingNodeAllowed() {
        return jeuActuel.deplacementPermis() && etat== Etat.Deplacement;
    }

    @Override
    public boolean isDelAllowed() {
        return jeuActuel.suppressionPermise();
    }
    private void annoncerGagnantQuitPart(String nomGagnant){
        Util.annoncerGagnant(nomGagnant);//declare le gagnat
        rtnChoixJeuHand(null);// retourner au choix jeu et graphe
    }

    private enum Etat{
        Deplacement,Coloriage,Suppression;
    }
}
