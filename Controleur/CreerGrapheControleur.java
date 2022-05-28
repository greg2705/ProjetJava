package Controleur;

import Model.AppModel;
import Model.Coordonnee;
import Model.Graphe;
import Model.Noeud;
import Vue.GrapheDisplayer;
import Vue.NoeudGUI;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import Vue.AreteGUI;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import java.util.LinkedList;
import java.util.Optional;

public class CreerGrapheControleur implements GrapheManager{
    private Stage primaryStage;// pointeur vers le Stage
    private Graphe tempGraphe; // graphe temporaire
    private AppModel model; //model de l'application
    @FXML private Button rtnMenuPrncpBtn;
    @FXML private Button addNode;
    @FXML private Button rmvNode;
    @FXML private Pane panneau;
    @FXML private ToggleButton addArrete;
    @FXML private ToggleButton delBtn;
    @FXML private Button saveBtn;
    @FXML private Button matAdja;
    @FXML private TextField etiq;
    @FXML private TextField nameField;
    private ToggleGroup tglGroupe;
    private Etat etat;
    private Noeud noeudDepart;
    private Noeud noeudArrive;
    private final double radius=15;
    private GrapheDisplayer grapheDisplayer;
    private boolean mouvingAllowed=true;

    public CreerGrapheControleur(AppModel m, Stage prmStg) {
        tempGraphe=new Graphe();
        this.model=m;
        this.primaryStage=prmStg;
        tglGroupe= new ToggleGroup();

    }

    public void initialize (){
        grapheDisplayer= new GrapheDisplayer(tempGraphe, panneau, this);
        rtnMenuPrncpBtn.setOnAction(this::rtnMenuPrncpBtnHand);
        addNode.setOnAction(this::addNodeHand);
        addArrete.setOnAction(this::addArreteHand);
        delBtn.setOnAction(this::delBtnHand);
        saveBtn.setOnAction(this::saveBtnHand);
        matAdja.setOnAction(this::matAdjaAction);
        addArrete.setToggleGroup(tglGroupe);
        delBtn.setToggleGroup(tglGroupe);
    }

    /**
     * cette methode s'occuped d'afficher le graphe en cours de creation
     * et met a jour l'affichage des que l'utilisateur ajoute ou supprime
     * un element du graphe
     **/


    @FXML
    public void rtnMenuPrncpBtnHand(ActionEvent event){
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
    @FXML
    public void addNodeHand(ActionEvent event){
        tglGroupe.selectToggle(null);
        etat=Etat.AddNoeud;
        mouvingAllowed=true;
        String etiquette=this.etiq.getText();
        if (etiquette.length()==0) {
            tempGraphe.ajouterNoeud();
        }
        else{
            tempGraphe.ajouterNoeud(etiquette);
        }
        etiq.setText("");
        grapheDisplayer.updateVue();

    }

    @FXML
    public void matAdjaAction(ActionEvent event){
        Dialog dialog = new TextInputDialog("[[011][101][100]]");
        dialog.setTitle("Matrice Adja");
        dialog.setHeaderText("Entrer une matrice int[][]");

        Optional<String> result = dialog.showAndWait();
        String entered = "none.";
        if (result.isPresent()) {
            entered = result.get();
        }
        if(tempGraphe.verfiMatAdja(entered)){
            int[][] mat=tempGraphe.convertStringMat(entered);
            Graphe gene=tempGraphe.AdjaToGraph(mat);
           // tempGraphe=tempGraphe.AdjaToGraph(mat);

            double random;
            for(Noeud a:gene.getListNoeud()){
                random = Math.random()*480;
                a.getCoordonnee().setX(random);
                random = Math.random()*385;
                a.getCoordonnee().setY(random);
                //grapheDisplayer.setDisplayedGraph(tempGraphe);
                tempGraphe.getListNoeud().add(a);
            }
            grapheDisplayer.updateVue();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Vous n'avez pas rentrer de matrice d'adjacence");
            alert.showAndWait();
        }
        grapheDisplayer.updateVue();
    }


    public AppModel getModel() {
        return model;
    }

    public void setModel(AppModel model) {
        this.model = model;
    }




    public void addArreteHand(ActionEvent event){
        if (addArrete.isSelected()){
            etat=Etat.AddArrete;
            mouvingAllowed=false;
        }else {
            etat=Etat.AddNoeud;
            mouvingAllowed= true;
        }
        grapheDisplayer.updateVue();
    }


    @Override
    public void nodeClicked(Noeud target) {
        switch (etat){

            case Suppression:
                tempGraphe.supprimerNoeud(target);
                break;
        }
        grapheDisplayer.updateVue();
    }

    @Override
    public void edgeClicked(AreteGUI edge) {
        if (etat == Etat.Suppression){
            tempGraphe.retirerArete(edge.getA(), edge.getB());
        }
        grapheDisplayer.updateVue();
    }

    @Override
    public void nodeMoved(Noeud movedNode) {

    }

    @Override
    public void nodeDraged(Noeud dragedNode) {
        if (etat == Etat.AddArrete){
            noeudDepart=dragedNode;
        }
    }

    @Override
    public void dragReleasedOnNode(Noeud targetNode) {
        if (etat==Etat.AddArrete){
            noeudArrive=targetNode;
            tempGraphe.ajouterArete(noeudDepart, noeudArrive);
            noeudDepart=null;
            noeudArrive=null;
            grapheDisplayer.updateVue();
        }
    }

    @Override
    public boolean isMovingNodeAllowed() {
        return mouvingAllowed;
    }

    @Override
    public boolean isDelAllowed() {
        return etat==Etat.Suppression;
    }

    private enum Etat{
        AddNoeud,AddArrete,Suppression;
    }
    private void delBtnHand(ActionEvent event){
        if (delBtn.isSelected()){
            etat=Etat.Suppression;
            mouvingAllowed=false;

        }else {
            etat=Etat.AddNoeud;
            mouvingAllowed=true;

        }
        grapheDisplayer.updateVue();
    }

    private void saveBtnHand(ActionEvent event){
        if (nameField.getText()!= null && nameField.getText().length()!=0){
            tempGraphe.setNom(nameField.getText());
            nameField.setText(null);
        }
        model.ajouterGraphe(tempGraphe);
        Util.Util.saveGraphes(model.getListeGraphes());
        tempGraphe=new Graphe();
        grapheDisplayer.setDisplayedGraph(tempGraphe);
        grapheDisplayer.updateVue();
    }

}

