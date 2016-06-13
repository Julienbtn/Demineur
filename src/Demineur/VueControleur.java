package Demineur;

import java.io.File;
import static java.lang.Integer.min;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
        
        int taille = 40;
        Group milieu = new Group();
        GridPane plateau = new GridPane();
        // pour essayer d'enlever le halo bleu autour d'une case quand on clique dessus
        milieu.setStyle("-fx-focus-color: transparent;"
                +"-fx-faint-focus-color: transparent;");
        for (int i =0; i<jeu.getLignes();i++){
            for(int j=0; j<jeu.getColonnes(); j++){
                final int id = i*jeu.getColonnes()+j;
                Button b= new Button();
                
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
                        if(jeu.getCase(id).isVisible()){
                            if (jeu.getCase(id).getValeur()>=0)
                            {
                                b.setText(""+jeu.getCase(id).getValeur());
                                switch(jeu.getCase(id).getValeur()){
                                    case 1: b.setTextFill(Color.BLUE);break;
                                    case 2: b.setTextFill(Color.GREEN);break;
                                    case 3: b.setTextFill(Color.RED);break;
                                    case 4: b.setTextFill(Color.BROWN);break;
                                    case 5: b.setTextFill(Color.ORANGE);break;
                                    case 6: b.setTextFill(Color.GRAY);break;
                                    case 7: b.setTextFill(Color.YELLOW);break;
                                    case 8: b.setTextFill(Color.BLACK);break;
                                }
                                // en gras
                                b.setStyle("-fx-font-weight: bold;");
                                b.setGraphic(null);
                                b.setDisable(true); 
                            }
                        }
                        else if(jeu.getCase(id).isDrapeau()){
                            String drapeauURI = new File("image/drapeau.png").toURI().toString();
                            Image drapeau = new Image(drapeauURI,b.getWidth()*0.6,b.getHeight()*0.6,false,false);
                            ImageView drapeauView = new ImageView(drapeau);
                            b.setGraphic(drapeauView);
                        }
                        else{
                            b.setGraphic(null);
                        }
                        
                        if(jeu.isGagne()||jeu.isPerdu()){
                            if (jeu.getCase(id).getValeur()==-1){
                                String mineURI = new File("image/mine.jpg").toURI().toString();
                                Image mine = new Image(mineURI,b.getWidth()*0.6,b.getHeight()*0.6,false,false);
                                ImageView mineView = new ImageView(mine);
                                b.setGraphic(mineView);
                                b.setText(null);
                              }
                            b.setDisable(true);
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
                Alert défaite = new Alert(AlertType.CONFIRMATION);
                défaite.setTitle("Défaite");
                défaite.setHeaderText(null);
                défaite.setHeaderText("Oh non, vous avez perdu ...");
                défaite.setContentText("Voulez-vous recommencez la partie ? ");
                Optional<ButtonType> result = défaite.showAndWait();
                if (result.get() == ButtonType.OK){
                    int[] c = new int[3];
                    c[0]=jeu.getLignes();
                    c[1]=jeu.getColonnes();
                    c[2]=jeu.getMines();
                    init(primaryStage,c);
                }
            }
        }
    });
        final Label temps=new Label("0");
        
        gridpane.add(temps,8,0);
        Thread timer = new Thread() {
        public void run() {
            int secondes=0;
            while(true){
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    System.out.println("Bug timer");
                }
                secondes++;
                temps.setText(""+secondes);
            }
          }
        };
        timer.start();
        milieu.getChildren().add(plateau);
        int width = min(jeu.getColonnes()*taille+10,800);
        int height = min(jeu.getLignes()*taille+40,600);
        // ajout espace pour l'éventuelle scrollbar
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
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch();
    }     
}