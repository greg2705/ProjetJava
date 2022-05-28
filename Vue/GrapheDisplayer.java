package Vue;

import Controleur.GrapheManager;
import Model.Coordonnee;
import Model.Graphe;
import Model.Noeud;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.util.LinkedList;

public class GrapheDisplayer {
    private static double radius=15;
    private Graphe displayedGraph;
    private Pane displayerPane;
    private GrapheManager grapheManager;

    public GrapheDisplayer(Graphe grapheAaffich, Pane displayer, GrapheManager manag){
        this.displayerPane =displayer;
        this.displayedGraph=grapheAaffich;
        this.grapheManager=manag;
    }

    public void updateVue(){
        NoeudGUI node;
        displayerPane.getChildren().clear( );

        for (Noeud  n : displayedGraph.getListNoeud()){
            node =new NoeudGUI(n);
            if (node.getNoeud().getCoordonnee().getX()==0 && node.getNoeud().getCoordonnee().getY()==0 ){
                node.getNoeud().setCoordonnee(new Coordonnee((100), (100)));
            }
            node.setLayoutX( node.getNoeud().getCoordonnee().getX() );
            node.setLayoutY( node.getNoeud().getCoordonnee().getY() );
            node.setRadius(radius);
            node.getStyleClass().add(n.getEtiquette());
            //node.setCursor(Cursor.MOVE);
            if (grapheManager.isMovingNodeAllowed()){
                node.setOnMouseDragged(this::nodeMovDragHandler);
            }
            else{
                node.setOnMouseDragged(this::nodeDragHandler);
            }
            node.setOnMouseClicked(this::nodeClickedHandler);
            node.setOnMouseReleased(this::mouseReleased);
            displayerPane.getChildren().add(node);
        }
        for (Noeud noeud: displayedGraph.getListNoeud()){
            for (Noeud noeudAdj: noeud.getListAdja()){
                if (noeud==noeudAdj)
                    break;
                Coordonnee [] coord=this.determinerCoordonnesDepArrArrete(noeud, noeudAdj);
                AreteGUI ar =new AreteGUI(noeud,noeudAdj,coord);

                    ar.setOnMouseClicked(this::edgeClicked);
                displayerPane.getChildren().add(ar);


            }
        }


    }

    public void setDisplayedGraph(Graphe displayedGraph) {
        this.displayedGraph = displayedGraph;
    }

    private Coordonnee[] determinerCoordonnesDepArrArrete(Noeud dep, Noeud arr){
        double depX = dep.getCoordonnee().getX();
        double depY = dep.getCoordonnee().getY();
        double arrX = arr.getCoordonnee().getX();
        double arrY = arr.getCoordonnee().getY();
        double deltaX = arrX - depX;
        double deltaY = arrY - depY;
        double norme=Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        Coordonnee coordep=new Coordonnee((deltaX * radius / norme ) + depX, (deltaY * radius / norme ) + depY);
        Coordonnee coordArr=new Coordonnee((((-deltaX) * radius) / norme)+ arrX   , (((-deltaY) * radius) / norme) + arrY);
        Coordonnee []res=new Coordonnee[2];
        res  [0]=coordep;
        res[1]=coordArr;
        return res;
    }
    private void nodeMovDragHandler(MouseEvent event){
        double maxX =displayerPane.getWidth()+displayerPane.getLayoutX();
        double maxY= displayerPane.getHeight()+displayerPane.getLayoutY();
        double minX=displayerPane.getLayoutX();
        double minY=displayerPane.getLayoutY();
        NoeudGUI c = (NoeudGUI) event.getSource();
        double newX = event.getSceneX();
        double newY = event.getSceneY();
        double x = c.getLayoutX();
        double y = c.getLayoutY();
        if(newX < radius + minX )
            newX = radius + minX ;

        if(newY < radius + minY)
            newY=radius + minY ;

        if (newX > maxX - radius )
            newX = maxX - radius;

        if (newY > maxY - radius)
            newY = maxY - radius;
        c.setTranslateX(newX -x -displayerPane.getLayoutX());
        c.setTranslateY(newY -y -displayerPane.getLayoutY());
        Bounds frontiere =c.getBoundsInParent();
        double newwX=frontiere.getCenterX();
        double newwY=frontiere.getCenterY();

        c.getNoeud().setCoordonnee(new Coordonnee(newwX, newwY));
        redrawArete();
    }

    private void nodeDragHandler(MouseEvent event){
        NoeudGUI target=(NoeudGUI) event.getSource();
        grapheManager.nodeDraged(target.getNoeud());
    }
    private void nodeClickedHandler(MouseEvent event){
        NoeudGUI c = (NoeudGUI) event.getSource();
        if(grapheManager!=null){
            grapheManager.nodeClicked(c.getNoeud());
        }
    }
    private void mouseReleased(MouseEvent event){
        Node target=  event.getPickResult().getIntersectedNode();
        if (target instanceof NoeudGUI){
            NoeudGUI noeudCible=(NoeudGUI) target;
            grapheManager.dragReleasedOnNode(noeudCible.getNoeud());
        }

    }
    private void edgeClicked(MouseEvent event){
        grapheManager.edgeClicked((AreteGUI) event.getSource());
    }
    public void redrawArete(){
        ObservableList<Node> children = displayerPane.getChildren();
        LinkedList<AreteGUI> arete=new LinkedList<>();
        for(Node node: children){
            if (node instanceof AreteGUI){
                arete.add((AreteGUI) node);
            }
        }
        for(AreteGUI ar:arete){
            children.remove(ar);
        }
        for (Noeud noeud: displayedGraph.getListNoeud()){
            for (Noeud noeudAdj: noeud.getListAdja()){
                if (noeud==noeudAdj) {
                    break;
                }
                Coordonnee [] coord=this.determinerCoordonnesDepArrArrete(noeud, noeudAdj);
                AreteGUI ar =new AreteGUI(noeud,noeudAdj,coord);

                    ar.setOnMouseClicked(this::edgeClicked);
                children.add(ar);
            }
        }
    }
}
