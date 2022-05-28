package Util;

import Model.Graphe;
import Model.Joueur;
import Model.Noeud;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Constructor;

import java.util.LinkedList;
import java.util.Scanner;
import Model.Jeu;
import javafx.scene.control.Alert;

/**
 * Cette classe contient divers methodes static utiles
 */
public class Util {

    public static LinkedList<Graphe> loadGraphes(){
       // URL url = Util.class.getResource("../Sauvegarde/savedGraphes.json");
        FileReader fileR= null;
        try {
            fileR = new FileReader("Sauvegarde/savedGraphes.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Gson gsonObject= new Gson();
        if (fileR==null){
            return null;
        }
        JsonReader jreader=new JsonReader(fileR);
        jreader.setLenient(true);
        LinkedList<Graphe> grapheList=new LinkedList<>();
        GrapheSave tempGrapheSave=null;
        Graphe tempGraphe =null;
        Scanner sc=new Scanner(fileR);
        try {
            while (jreader.peek() != JsonToken.NULL  && jreader.peek() != JsonToken.END_DOCUMENT ) {
                tempGrapheSave = gsonObject.fromJson(jreader, GrapheSave.class);
                tempGraphe= convertGrapheSaveToGraphe(tempGrapheSave);
                grapheList.add(tempGraphe);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return grapheList;


    }
    private static Graphe convertGrapheSaveToGraphe(GrapheSave save){
        LinkedList<Noeud> listeNoeud=save.getListNoeud();
        int [][] matAdj=save.getMatAdj();
        for (int i=0; i<listeNoeud.size(); i++){
            LinkedList<Noeud> listeAdjacence=new LinkedList<>();
            for(int k=0; k<matAdj[i].length;k++){
                if (k!=i && matAdj[i][k]==1){
                   listeAdjacence.add(listeNoeud.get(k));
                }
            }
            listeNoeud.get(i).setListAdja(listeAdjacence);
        }
        Graphe restored=new Graphe(save.getNom());
        restored.setListNoeud(listeNoeud);
        restored.setNbNoeud(listeNoeud.size());
        return restored;


    }
    public static void saveGraphes(LinkedList<Graphe> graphesToSave){
        //URL url = Util.class.getResource("../Sauvegarde/savedGraphes.json");
        Gson gson=new Gson();
        FileWriter fileW=null;
        JsonWriter jwriter=null;
       try {
           //fileW=new FileWriter(url.getFile(),false);
           fileW =new FileWriter("Sauvegarde/savedGraphes.json",false);
           jwriter= new JsonWriter(fileW);
           for (Graphe g: graphesToSave){
               int [][] matriceAdj=g.matAdja();
               LinkedList<Noeud> listeNoeud=g.getListNoeud();
               GrapheSave save= new GrapheSave(matriceAdj, listeNoeud, g.getNom());
               gson.toJson(save,GrapheSave.class,jwriter);
           }
           jwriter.flush();
           jwriter.close();
       }catch (Exception e){
           e.printStackTrace();
       }

    }
    public static LinkedList<Jeu> loadJeux(){
        LinkedList<Jeu> listeJeu= new LinkedList<>();
        LinkedList<String> listeNomsJeux=getClassesName("Jeux");
        Jeu loadedGame=null;
        if (listeNomsJeux == null || listeNomsJeux.size()==0){
            return listeJeu;
        }
        for(String nomJeu : listeNomsJeux){
            loadedGame = loadAndInstantiate("Jeux."+nomJeu);
            if (loadedGame != null){
                listeJeu.add(loadedGame);
            }
        }
        return listeJeu;
    }
    /**
     * cette methode privée s'occupe de chercher les noms des fichiers
     * java qui se trouvent dans le fichier dont le path est path
     * @param path le path du fichier dans lequel la methode cherche les fichiers java
     * @return une liste chainéé des noms des fichiers java trouvés
     */
    private static LinkedList<String> getClassesName(String path){
        File file=new File(path);
                if (file ==null){
            return null;
        }
        LinkedList<String> clsName=new LinkedList<>();
        for (File f: file.listFiles()){
            String completeName=f.getName();
            if (completeName.contains(".java")){
                int index=completeName.indexOf(".java");
                clsName.add(completeName.substring(0,index));
            }
        }
        return clsName;
    }
    private static Jeu loadAndInstantiate(String gameName){
        ClassLoader classLoad=Util.class.getClassLoader();
        Jeu loadedGame=null;
        try{
            ClassLoader cload=Util.class.getClassLoader();
            Class cJeu=cload.loadClass(gameName);
            Constructor constructor=cJeu.getConstructor();
            Object instance=constructor.newInstance();
            if (instance instanceof  Jeu){
                loadedGame=(Jeu)instance;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return loadedGame;
    }
    public static void annoncerGagnant(String nom){
        Alert msg = new Alert(Alert.AlertType.INFORMATION);
        msg.setHeaderText("Partie finie");
        msg.setTitle("partie finie");
        msg.setContentText(" et le gagant est " + nom);
        msg.showAndWait();

    }
    public static int determinerIndicJoueu(Joueur j, Joueur[] tabJoueurs){
        int indP= 0;
        for(Joueur jx : tabJoueurs){
            if (jx== j){
                break;
            }else {
                indP++;
            }
        }
        return indP;
    }

    private static class GrapheSave{
        int [][] matAdj;
        LinkedList<Noeud> listNoeud;
        String nom;
         public GrapheSave (int [][] mat, LinkedList<Noeud> list, String n){
             matAdj=mat;
             listNoeud=list;
             nom=n;
         }

        public int[][] getMatAdj() {
            return matAdj;
        }
        public String getNom(){
             return nom;
        }

        public void setMatAdj(int[][] matAdj) {
            this.matAdj = matAdj;
        }

        public LinkedList<Noeud> getListNoeud() {
            return listNoeud;
        }

        public void setListNoeud(LinkedList<Noeud> listNoeud) {
            this.listNoeud = listNoeud;
        }

    }
}
