package Demineur;

import java.io.File;
import static java.lang.Integer.min;
import java.util.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import modeledemineur.GrilleModele;

public class VueControleur extends Application{
    private GrilleModele jeu;
    
    @Override
    public void start(Stage primaryStage) {
        init(primaryStage);
    }
    
    public void survival(Stage primaryStage){
        init(primaryStage,10,10,5,false);
        jeu.addObserver(new Observer(){
            @Override
            public void update(Observable o,Object arg){
                if(jeu.isGagne())
                {
                    init(primaryStage,15,15,20,false);
                    Alert victoire1 = new Alert(AlertType.INFORMATION);   
                    victoire1.setTitle("Victoire");
                    victoire1.setHeaderText(null);
                    victoire1.setContentText("Felicitation vous avez gagné le premier tour !!!");
                    victoire1.show();
                    jeu.addObserver(new Observer(){
                    @Override
                    public void update(Observable o,Object arg){
                        if(jeu.isGagne())
                        {
                            init(primaryStage,20,20,60,false);
                            Alert victoire2 = new Alert(AlertType.INFORMATION);   
                            victoire2.setTitle("Victoire");
                            victoire2.setHeaderText(null);
                            victoire2.setContentText("Felicitation vous avez gagné le deuxième tour !!!");
                            victoire2.show();
                            jeu.addObserver(new Observer(){
                            @Override
                             public void update(Observable o,Object arg){
                                 if(jeu.isGagne())
                                 { 
                                     Alert victoire3 = new Alert(AlertType.INFORMATION);   
                                     victoire3.setTitle("Victoire finale");
                                     victoire3.setHeaderText(null);
                                     victoire3.setContentText("Felicitation vous êtes arrivé au bout de ce survival");
                                     victoire3.show();
                                 }
                                 else if (jeu.isPerdu())  
                                 {
                                     Alert defaite3 = new Alert(AlertType.CONFIRMATION);   
                                     defaite3.setTitle("Défaite");
                                     defaite3.setHeaderText(null);
                                     defaite3.setHeaderText("Oh non, vous avez perdu au dernier tour");
                                     defaite3.setContentText("Voulez-vous recommencez le survival ? ");
                                     Optional<ButtonType> result = defaite3.showAndWait();
                                     if (result.get() == ButtonType.OK){
                                         survival(primaryStage);
                                     }
                                 }
                             };
                            });  
                        }
                        else if (jeu.isPerdu())         
                        {        
                            Alert defaite2 = new Alert(AlertType.CONFIRMATION);          
                            defaite2.setTitle("Défaite");    
                            defaite2.setHeaderText(null);
                            defaite2.setHeaderText("Oh non, vous avez perdu au deuxième tour");     
                            defaite2.setContentText("Voulez-vous recommencez le survival ? ");     
                            Optional<ButtonType> result = defaite2.showAndWait();  
                            if (result.get() == ButtonType.OK){   
                                survival(primaryStage);      
                            }     
                        }
                    };
                });
                }
                else if (jeu.isPerdu())           
                {           
                    Alert defaite1 = new Alert(AlertType.CONFIRMATION);                  
                    defaite1.setTitle("Défaite");    
                    defaite1.setHeaderText(null);    
                    defaite1.setHeaderText("Oh non, vous avez perdu au premier tour");     
                    defaite1.setContentText("Voulez-vous recommencez le survival ? ");                 
                    Optional<ButtonType> result = defaite1.showAndWait();   
                    if (result.get() == ButtonType.OK){     
                        survival(primaryStage);              
                    }           
                }
            };
        });
    }
    
    public void init(Stage primaryStage){
        int[] temp = new int[3];
        temp[0]=temp[1]=temp[2]=10;
        init(primaryStage,temp,true);
    }
    
    public void init(Stage primaryStage,int nbligne, int nbcolonne, int nbmine,boolean obsfin)
    {
        int[] temp = {nbligne, nbcolonne, nbmine};
        init(primaryStage,temp,obsfin);
    }
    
    public void init(Stage primaryStage, int[] caracs,boolean obsfin){
        jeu=new GrilleModele(caracs[0], caracs[1], caracs[2]);
        BorderPane border = new BorderPane();
        Group score = new Group();
       
        
        
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
        
        final MenuBar menu =new MenuBar();
        
        final Menu difficulte = new Menu("Niveau de difficulté");
        final MenuItem facile=new MenuItem("Facile");
        facile.setOnAction(actionEvent -> init(primaryStage));
        final MenuItem moyen = new Menu("Moyen");
        moyen.setOnAction(actionEvent -> init(primaryStage,15,15,40,true));
        final MenuItem difficile = new Menu("Difficile");
        difficile.setOnAction(actionEvent -> init(primaryStage, 20, 20, 120,true));
        final MenuItem personnalise = new Menu("Personnalisé");
        personnalise.setOnAction(actionEvent -> border.setTop(score));
        difficulte.getItems().setAll(facile,moyen,difficile,personnalise);
        
        final Menu mode = new Menu("Mode de jeu");
        final MenuItem survival = new MenuItem("Survival");
        survival.setOnAction(actionEvent -> survival(primaryStage));
        mode.getItems().setAll(survival);
        
        GridPane gridpane = new GridPane();
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
                            init(primaryStage,c,true);
                    }
                });
        
        score.getChildren().add(gridpane);
        //border.setTop(score);
        
        border.setTop(menu);
        menu.getMenus().setAll(difficulte,mode);
        
        int taille = min(min(40,580/jeu.getColonnes()),740/jeu.getLignes());
        if (taille<25)
            taille=25;
        Group milieu = new Group();
        GridPane plateau = new GridPane();
        for (int i =0; i<jeu.getLignes();i++){
            for(int j=0; j<jeu.getColonnes(); j++){
                final int id = i*jeu.getColonnes()+j;
                Button b= new Button();
                /* CASES RECTANULAIRES???????
                b.setPrefHeight(400/jeu.getColonnes());
                b.setPrefWidth(400/jeu.getLignes());
                
                Sinon ça? Mais les cases peuvent pas être miniatures => Scroll en bas
                int taille =min(min(40,400/jeu.getColonnes())400/jeu.getLignes());
                b.setPrefSize(taille,taille);
                */
                b.setPrefSize(taille,taille);
                b.setMaxSize(taille, taille);
                b.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event){
                        if(event.getButton().equals(MouseButton.PRIMARY)&&!jeu.getCase(id).isDrapeau())
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
                                b.setText(null);
                              }
                            else
                            {
                                b.setText(""+jeu.getCase(id).getValeur());
                                b.setGraphic(null);
                                b.setDisable(true); 
                            }
                        }
                        else if(jeu.getCase(id).isDrapeau()){
                            String drapeauURI = new File("image/drapeau.png").toURI().toString();
                            Image drapeau = new Image(drapeauURI);
                            ImageView drapeauView = new ImageView(drapeau);
                            b.setGraphic(drapeauView);
                        }
                        else{
                            b.setGraphic(null);
                        }
                    }
                });
                plateau.add(b, j, i);
            } 
        }
        if (obsfin)
        {
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
                    Alert défaite = new Alert(AlertType.CONFIRMATION);   
                    défaite.setTitle("Défaite");
                    défaite.setHeaderText(null);
                    défaite.setHeaderText("Oh non, vous avez perdu ...");
                    défaite.setContentText("Voulez-vous recommencez la partie ? ");
                    //défaite.show();
                    Optional<ButtonType> result = défaite.showAndWait();
                    if (result.get() == ButtonType.OK){
                        int[] c = new int[3];
                        c[0]=jeu.getLignes();
                        c[1]=jeu.getColonnes();
                        c[2]=jeu.getMines();
                        init(primaryStage,c,true);
                    }
            }
            }
        });
        }
        
        milieu.getChildren().add(plateau);
        //border.setCenter(milieu);
        int width = jeu.getColonnes()*taille+10;
        int height = jeu.getLignes()*taille+40;
        if(width>780)
            height+=15;
        if(height>580)
            width+=15;
        Scene scene = new Scene(border,width,height);
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(milieu);
        border.setCenter(scrollPane);
        
        primaryStage.setTitle("Demineur");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(370);
        primaryStage.setMinHeight(60);
        primaryStage.setMaxWidth(800);
        primaryStage.setMaxHeight(600);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch();
    }     
}