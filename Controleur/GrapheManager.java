package Controleur;

import Model.Noeud;
import Vue.AreteGUI;

public interface GrapheManager {
    void nodeClicked(Noeud target) ;
    void edgeClicked(AreteGUI edge);
    void nodeMoved(Noeud movedNode);//est appelé quand le noeud est deplacé
    void nodeDraged(Noeud dragedNode);// est appelé quand un drag est encléché sur un noeud
    void dragReleasedOnNode(Noeud targetNode);
    boolean isMovingNodeAllowed();
    boolean isDelAllowed();

}
