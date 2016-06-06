package Demineur;

import java.io.File;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import modeledemineur.GrilleModele;

public class VueControleur extends Application{
    private GrilleModele jeu;
    
    @Override
    public void start(Stage primaryStage) {
        init(primaryStage);
    }
    
    public void init(Stage primaryStage){
        int[] temp = new int[3];
        temp[0]=temp[1]=temp[2]=10;
        init(primaryStage,temp);
    }
    
    public void init(Stage primaryStage, int[] caracs){
        jeu=new GrilleModele(caracs[0], caracs[1], caracs[2]);
        BorderPane border = new BorderPane();
        Group score = new Group();
        GridPane gridpane = new GridPane();
        
        /*
        Label labelmine = new Label("Nombre de mines restantes : ");
        Label nbmines = new Label("0");
        Label labeltemps = new Label("Temps écoulé : ");
        Label nbtemps = new Label("0");
        gridpane.add(labelmine,0,0);
        gridpane.add(nbmines,1,0);
        gridpane.add(labeltemps, 2, 0);
        gridpane.add(nbtemps,3,0);
                */
        TextField longueur = new TextField ();
        TextField largeur = new TextField ();
        TextField mines = new TextField ();
        longueur.setPrefWidth(50);
        largeur.setPrefWidth(50);
        mines.setPrefWidth(50);
        longueur.setText(""+caracs[0]);
        largeur.setText(""+caracs[1]);
        mines.setText(""+caracs[2]);
        Button jouer= new Button("Jouer");
        gridpane.add(new Label("  Lignes :"),0,0);
        gridpane.add(longueur,1,0);
        gridpane.add(new Label("  Colonnes :"),2,0);
        gridpane.add(largeur,3,0);
        gridpane.add(new Label("  Mines :"),4,0);
        gridpane.add(mines,5,0);
        gridpane.add(new Label("  "),6,0);
        gridpane.add(jouer,7,0);
        jouer.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event){
                        int[] c = new int[3];
                        if(longueur.getText().matches("[0-9]*"))
                            c[0]=Integer.parseInt(longueur.getText());
                        if(largeur.getText().matches("[0-9]*"))
                            c[1]=Integer.parseInt(largeur.getText());
                        if(mines.getText().matches("[0-9]*"))
                            c[2]=Integer.parseInt(mines.getText());
                        if(c[0]>0&&c[1]>0&&c[2]>0&&c[0]*c[1]>c[2])
                            init(primaryStage,c);
                    }
                });
        
        score.getChildren().add(gridpane);
        border.setTop(score);
        
        Group milieu = new Group();
        GridPane plateau = new GridPane();
        
        for (int i =0; i<jeu.getLignes();i++){
            for(int j=0; j<jeu.getColonnes(); j++){
                final int id = i*jeu.getColonnes()+j;
                Button b= new Button();
                b.setPrefHeight(400/jeu.getColonnes());
                b.setPrefWidth(400/jeu.getLignes());
                b.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event){
                        if(event.getButton().equals(MouseButton.PRIMARY))
                            jeu.jouer(id);
                        else if(event.getButton().equals(MouseButton.SECONDARY))
                            jeu.modifDrapeau(id);
                    }
                });
                jeu.addObserver(new Observer(){
                    @Override
                    public void update(Observable o,Object arg){
                        if(jeu.isGagne()||jeu.isPerdu())
                            b.setDisable(true);

                        if(jeu.getCase(id).isVisible()){
                            
                            if (jeu.getCase(id).getValeur()==-1){
                                String mineURI = new File("image/mine.jpg").toURI().toString();
                                Image mine = new Image(mineURI);
                                ImageView mineView = new ImageView(mine);
                                b.setGraphic(mineView);  
                              }
                            else
                                b.setText(""+jeu.getCase(id).getValeur());
                            b.setDisable(true);                         
                        }
                        else if(jeu.getCase(id).isDrapeau()){
                            String drapeauURI = new File("image/drapeau.png").toURI().toString();
                            Image drapeau = new Image(drapeauURI);
                            ImageView drapeauView = new ImageView(drapeau);
                            b.setGraphic(drapeauView);
                        }
                        else{
                            b.setText("");
                        }
                    }
                });
                plateau.add(b, j, i);
            } 
        }
        jeu.addObserver(new Observer(){
        @Override
        public void update(Observable o,Object arg){
            if(jeu.isGagne())
            {         
                Alert victoire = new Alert(AlertType.INFORMATION);   
                victoire.setTitle("Victoire");
                victoire.setHeaderText(null);
                victoire.setContentText("Felicitation vous avez gagné !!!");
                victoire.show();
            }
            else if (jeu.isPerdu())
            {
                Alert défaite = new Alert(AlertType.INFORMATION);   
                défaite.setTitle("Défaite");
                défaite.setHeaderText(null);
                défaite.setContentText("Oh non, vous avez perdu ...");
                défaite.show();
            }
        }
    });
        
        milieu.getChildren().add(plateau);
        border.setCenter(milieu);
        
        Scene scene = new Scene(border);
        primaryStage.setTitle("Demineur");
        primaryStage.setScene(scene);
        primaryStage.setHeight(700);
        primaryStage.setWidth(600);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch();
    }     
}
    