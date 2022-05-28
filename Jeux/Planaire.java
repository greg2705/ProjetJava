package Jeux;
import Model.Graphe;
import Model.Jeu;
import Model.Joueur;
import Model.Noeud;
import Util.Util;
import java.awt.geom.Line2D;

import static java.awt.geom.Line2D.linesIntersect;

public class Planaire extends Jeu {
    private Joueur currentPlayer;
    public Planaire(){
        super.joueurs=new Joueur[1];
        String [] coulJ1=new String [1];
        coulJ1[0]="bleu";
        joueurs[0]=new Joueur("Joueur 1", coulJ1);
        currentPlayer=joueurs[0];

    }
    @Override
    public boolean grapheIsOk(Graphe g) {
        return true;
    }

    @Override
    public boolean deplacementPermis() {
        return true;
    }

    @Override
    public boolean suppressionPermise() {
        return false;
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
        double x1,y1,x2,y2,x3,y3,x4,y4;
        int nbarrete = 0;
        int[][] mat = graphe.matAdja();
        for (int i = 0; i < mat[0].length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                if (mat[i][j] == 1) {
                    nbarrete += 1;
                }
            }
        }
        double [][] tabcoord=new double[nbarrete][4];
        int i=0;
        for(Noeud a:graphe.getListNoeud()){
            for(Noeud b:a.getListAdja()){
                tabcoord[i][0]=a.getCoordonnee().getX();
                tabcoord[i][1]=a.getCoordonnee().getY();
                tabcoord[i][2]=b.getCoordonnee().getX();
                tabcoord[i][3]=b.getCoordonnee().getX();
                i+=1;
                for (int k = 0; k <nbarrete; k++) {
                    for (int j = 0; j <nbarrete; j++) {
                        if(linesIntersect(tabcoord[k][0],tabcoord[k][1],tabcoord[k][2],tabcoord[k][3],tabcoord[j][0],tabcoord[j][1],tabcoord[j][2],tabcoord[j][3])){
                            if(tabcoord[k][0]!=tabcoord[j][0]&&tabcoord[k][1]!=tabcoord[j][1]&&tabcoord[k][2]!=tabcoord[j][2]&&tabcoord[k][3]!=tabcoord[j][3]&&tabcoord[k][0]!=tabcoord[j][2]&&tabcoord[k][1]!=tabcoord[j][3]&&tabcoord[k][0]!=tabcoord[j][2]&&tabcoord[k][3]!=tabcoord[j][0]){
                                return false;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("mdr");
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
        return "Planaire";
    }

}

