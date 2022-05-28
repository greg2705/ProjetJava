package Jeux;

import Model.Graphe;
import Model.Jeu;
import Model.Joueur;
import Model.Noeud;
import Util.Util;

public class Col extends Jeu {
    private int tourNum=0;
    private Joueur currentPlayer;

    public Col(){
        super.joueurs=new Joueur[2];
        String [] coulJ1=new String [1];
        String [] coulJ2=new String [1];
        coulJ1[0]="bleu";
        coulJ2[0]="rouge";
        joueurs[0]=new Joueur("Joueur 1", coulJ1);
        joueurs[1]=new Joueur("Joueur 2",coulJ2);
        currentPlayer=joueurs[0];

    }
    @Override
    public boolean grapheIsOk(Graphe g) {
        return g.estConnexe();
    }

    @Override
    public boolean deplacementPermis() {
        return false;
    }

    @Override
    public boolean suppressionPermise() {
        return false;
    }

    @Override
    public boolean peutJouer(Joueur j, Noeud n, String selectedColor) {
        if(!n.getEtiquette().equals("blanc")){
            return false;//cas ou le noeud a deja été colorié
        }

        for(Noeud node: n.getListAdja()){
            if (node.getEtiquette().equals(selectedColor)) {
                return false;// cas ou il y a un noeud adja avec la m couleur
            }

        }
        return true;
    }

    @Override
    public void playingNode(Joueur j, Noeud selectedNode, String selectedColor) {
        selectedNode.setEtiquette(selectedColor);
        tourNum++;
        int indCurrentP= Util.determinerIndicJoueu(currentPlayer, joueurs);
        currentPlayer=joueurs[(indCurrentP+1)%joueurs.length];

    }

    @Override
    public void consequence(Joueur j, Noeud playedNode) {
        return;
    }

    @Override
    public boolean partieTerminee() {
        for (Joueur j: joueurs){
            for (String couleur : j.getColor()){
                for (Noeud n: graphe.getListNoeud()) {
                    if (peutJouer(j, n, couleur))
                        return false;
                }
            }
        }

        return true;
    }



    @Override
    public Joueur determinerGagnant() {
        int indCur=Util.determinerIndicJoueu(currentPlayer, joueurs);
        return joueurs[(indCur+1)%joueurs.length];
    }

    @Override
    public Joueur getCurrentPlayer(){
        return currentPlayer;
    }

    @Override
    public String toString(){
        return "Col";
    }

}
