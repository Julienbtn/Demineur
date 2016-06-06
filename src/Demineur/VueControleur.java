package Demineur;

import java.io.File;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import modeledemineur.GrilleModele;

public class VueControleur extends Application{
    
    // Une fois que la case est cliquée
    // b.setDisable(true);
    
    private GrilleModele jeu;
    
    @Override
    public void start(Stage primaryStage) {
        jeu=new GrilleModele(10, 10, 10);
        
        BorderPane border = new BorderPane();
        
        Group score = new Group();
        GridPane gridpane = new GridPane();
        
        Label labelmine = new Label("Nombre de mines restantes : ");
        Label nbmines = new Label("0");
        Label labeltemps = new Label("Temps écoulé : ");
        Label nbtemps = new Label("0");
        gridpane.add(labelmine,0,0);
        gridpane.add(nbmines,1,0);
        gridpane.add(labeltemps, 2, 0);
        gridpane.add(nbtemps,3,0);
        score.getChildren().add(gridpane);
        border.setTop(score);
        
        Group milieu = new Group();
        GridPane plateau = new GridPane();
        
        for (int i =0; i<10;i++){
            for(int j=0; j<10; j++){
                final int id = i*jeu.getLignes()+j;
                Button b= new Button();
                b.setPrefHeight(40);
                b.setPrefWidth(40);
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
                plateau.add(b, i, j);
            } 
        }
        
        milieu.getChildren().add(plateau);
        border.setCenter(milieu);
        
        Scene scene = new Scene(border);
        primaryStage.setTitle("Demineur");
        primaryStage.setScene(scene);
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
            
    public static void main(String[] args) {
        launch();
    }
        
}
    