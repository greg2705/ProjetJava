package Vue;

import Model.Coordonnee;
import Model.Noeud;
import javafx.scene.shape.Line;

public class AreteGUI extends Line {
    private Noeud a,b;

    public Noeud getA() {
        return a;
    }

    public void setA(Noeud a) {
        this.a = a;
    }

    public Noeud getB() {
        return b;
    }

    public void setB(Noeud b) {
        this.b = b;
    }
    public AreteGUI(Noeud a, Noeud b, Coordonnee [] tabCord){
        super(tabCord[0].getX(),tabCord[0].getY(),tabCord[1].getX(), tabCord[1].getY()) ;
        this.a=a;
        this.b=b;

    }
    public AreteGUI(Noeud depart, Noeud arrive){
        super ();
        a=depart;
        b=arrive;
    }
}
