package Jeux;
import Model.Graphe;
import Model.Jeu;
import Model.Joueur;
import Model.Noeud;
import Util.Util;

public class Eulerien extends Jeu {
    private Joueur currentPlayer;
    public Eulerien(){
        super.joueurs=new Joueur[1];
        String [] coulJ1=new String [1];
        coulJ1[0]="bleu";
        joueurs[0]=new Joueur("Joueur 1", coulJ1);
        currentPlayer=joueurs[0];

    }
    @Override
    public boolean grapheIsOk(Graphe g) {
        if( g.estConnexe()==false){
            return false;
        }
        return true;
    }

    @Override
    public boolean deplacementPermis() {
        return true;
    }

    @Override
    public boolean suppressionPermise() {
        return true;
    }

    @Override
    public boolean peutJouer(Joueur j, Noeud n, String selectedColor) {
        return true;
    }

    @Override
    public void playingNode(Joueur j, Noeud selectedNode, String selectedColor) {
        return;
    }

    @Override
    public void consequence(Joueur j, Noeud playedNode) {
        return;
    }

    @Override
    public boolean partieTerminee() {
        int[][] mat = graphe.matAdja();
        int paire;
        for (int i = 0; i < mat[0].length; i++) {
            paire = 0;
            for (int j = 0; j < mat[0].length; j++) {
                if (mat[i][j] == 1) {
                    paire +=1;
                }
            }
            if (paire % 2 == 1) {
                return false;
            }
        }
        if(!graphe.estConnexe()){
            System.out.print("Le graphe n'est plus connexe");
            return false;
        }
        return true;
    }

    @Override
    public Joueur determinerGagnant() {
        return currentPlayer;
    }

    @Override
    public Joueur getCurrentPlayer(){
        return currentPlayer;
    }

    @Override
    public String toString(){
        return "Eulerien";
    }


}
