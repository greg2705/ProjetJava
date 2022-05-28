package Jeux;

import Model.Graphe;
import Model.Jeu;
import Model.Joueur;
import Model.Noeud;
import Util.Util;

import java.util.LinkedList;
import java.util.Stack;

public class Hex extends Jeu {
    private int tourNum=0;
    private Joueur currentPlayer;

    public Hex(){
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
        return g.estConnexe() && pointsCardinauxPresents(g);
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
        return n.getEtiquette().equals("blanc");
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
        return estConnectedToOuest() || nordConnectedToSud();
    }


    @Override
    public Joueur determinerGagnant() {
        if(estConnectedToOuest())  {
            return joueurs[0];
        }
        return joueurs[1];
    }

    @Override
    public Joueur getCurrentPlayer() {
        return currentPlayer;
    }

    private boolean estConnectedToOuest(){
        Noeud est=getEst(graphe);
        Noeud ouest= getOuest(graphe);
        Stack<Noeud> pile=new Stack<>();
        LinkedList<Noeud> visited=new LinkedList<>();
        pile.push(est);
        visited.add(est);
        while(!pile.empty()){
            Noeud poped=pile.pop();
            if(poped.equals(ouest))
                return true;
            for(Noeud n : poped.getListAdja()){
                if (n.equals(ouest)){
                    return true;
                }
                if(!pile.contains(n) && !visited.contains(n) && n.getEtiquette().equals("bleu")){
                    pile.push(n);
                    visited.add(n);
                }
            }
        }

        return false;
    }
    private boolean nordConnectedToSud(){
        Noeud nord=getNord(graphe);
        Noeud sud = getSud(graphe);
        Stack<Noeud> pile=new Stack<>();
        LinkedList<Noeud> visited=new LinkedList<>();
        pile.push(nord);
        visited.add(nord);
        while(!pile.empty()){
            Noeud poped=pile.pop();
            if(poped.equals(sud))
                return true;
            for(Noeud n : poped.getListAdja()){
                if (n.equals(sud)){
                    return true;
                }
                if(!pile.contains(n) && !visited.contains(n) && n.getEtiquette().equals("rouge")){
                    pile.push(n);
                    visited.add(n);
                }
            }
        }

        return false;
    }
    @Override
    public String toString(){
        return "Hex";
    }
    private boolean pointsCardinauxPresents(Graphe g){
        return getNord(g)!=null && getEst(g)!=null  && getSud(g)!= null && getOuest(g)!=null ;
    }
    private Noeud getEst(Graphe g){
        for (Noeud n :g.getListNoeud()){
            if (n.getEtiquette().equals("est")){
                return n;
            }
        }
        return null;
    }
    private Noeud getOuest(Graphe g){
        for (Noeud n :g.getListNoeud()){
            if (n.getEtiquette().equals("ouest")){
                return n;
            }
        }
        return null;
    }
    private Noeud getNord(Graphe g){
        for (Noeud n :g.getListNoeud()){
            if (n.getEtiquette().equals("nord")){
                return n;
            }
        }
        return null;
    }
    private Noeud getSud(Graphe g){
        for (Noeud n :g.getListNoeud()){
            if (n.getEtiquette().equals("sud")){
                return n;
            }
        }
        return null;
    }
}
