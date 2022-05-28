package Jeux;
import Model.Graphe;
import Model.Jeu;
import Model.Joueur;
import Model.Noeud;
import Util.Util;

public class Othello extends Jeu {
    private int tourNum=0;
    private Joueur currentPlayer;
    private int[][] grille;
    private boolean passetour;
    public Othello(){
        super.joueurs=new Joueur[2];
        String [] coulJ1=new String [1];
        String [] coulJ2=new String [1];
        coulJ1[0]="bleu";
        coulJ2[0]="rouge";
        joueurs[0]=new Joueur("Joueur 1", coulJ1);
        joueurs[1]=new Joueur("Joueur 2",coulJ2);
        currentPlayer=joueurs[0];
        grille=new int[8][8];
        passetour=false;
    }
    @Override
    public boolean grapheIsOk(Graphe g) {
        return g.getNom().equals("Othello");
    }

    @Override
    public boolean deplacementPermis() {
        return false;
    }

    @Override
    public boolean suppressionPermise() {
        return false;
    }
    public boolean passeTour(Joueur j) {
        for (Noeud a : graphe.getListNoeud()) {
            if (peutJouer(j, a, j.getColor()[0])) {
                return false;
            }
        }
        return true;
    }
    @Override
    public boolean peutJouer(Joueur j, Noeud n, String selectedColor) {
        if(passeTour(j)){
            passetour=true;
            return true;
        }
        if(n.getEtiquette().equals("special")==false){
            return false;
        }

        return true;
    }

    @Override
    public void playingNode(Joueur j, Noeud selectedNode, String selectedColor) {
        if(passetour==false){
            selectedNode.setEtiquette(selectedColor);
            int numgrille=Integer.parseInt(selectedNode.getEtiquette())-1;
            int x=numgrille/8;
            int y=numgrille%8;
            if(tourNum%2==0){
                grille[x][y]=1;
            }
            else{
                grille[x][y]=2;
            }
        }
        tourNum++;
        int indCurrentP= Util.determinerIndicJoueu(currentPlayer, joueurs);
        currentPlayer=joueurs[(indCurrentP+1)%joueurs.length];
        passetour=false;
    }

    @Override
    public void consequence(Joueur j, Noeud playedNode) {
        return;
    }

    @Override
    public boolean partieTerminee() {
        int caseremplie=0;
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(grille[i][j]!=0){
                    caseremplie+=1;
                }
            }
        }
        if(caseremplie==64){
            return true;
        }
        for(Noeud a:graphe.getListNoeud()){
            if(peutJouer(joueurs[0],a,joueurs[0].getColor()[0])||peutJouer(joueurs[1],a,joueurs[1].getColor()[0])){
                return false;
            }
        }
        return true;
    }



    @Override
    public Joueur determinerGagnant() {
        int piontj1=0;
        int piontj2=0;
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(grille[i][j]==1){
                    piontj1+=1;
                }
                if(grille[i][j]==2){
                    piontj2+=2;
                }
            }
        }
        if(piontj1>piontj2){
            return joueurs[0];
        }
        return joueurs[1];
    }

    @Override
    public Joueur getCurrentPlayer(){
        return currentPlayer;
    }

    @Override
    public String toString(){
        return "Othello";
    }

}
