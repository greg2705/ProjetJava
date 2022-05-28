package Vue;

import Model.Noeud;
import javafx.scene.shape.Circle;

public class NoeudGUI extends Circle {
    private Noeud noeud;
    private double orgTranslateX;
    private double orgTranslateY;
    private double orgSceneX;
    private double orgSceneY;


    public double getOrgTranslateX() {
        return orgTranslateX;
    }

    public double getOrgSceneX() {
        return orgSceneX;
    }

    public void setOrgSceneX(double orgSceneX) {
        this.orgSceneX = orgSceneX;
    }

    public double getOrgSceneY() {
        return orgSceneY;
    }

    public void setOrgSceneY(double orgSceneY) {
        this.orgSceneY = orgSceneY;
    }

    public double getOrgTranslateY() {
        return orgTranslateY;
    }

    public void setOrgTranslateY(double orgTranslateY) {
        this.orgTranslateY = orgTranslateY;
    }

    public void setOrgTranslateX(double orgTranslateX) {
        this.orgTranslateX = orgTranslateX;
    }

    public double getOrgTranslatY() {
        return orgTranslatY;
    }

    public void setOrgTranslatY(double orgTranslatY) {
        this.orgTranslatY = orgTranslatY;
    }

    private double orgTranslatY;
    public NoeudGUI(Noeud n){
        this.noeud=n;
    }

    public Noeud getNoeud() {
        return noeud;
    }

    public void setNoeud(Noeud noeud) {
        this.noeud = noeud;
    }
}
