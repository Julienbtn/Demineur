package Demineur;

import java.io.File;
import static java.lang.Integer.min;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import modeledemineur.GrilleModele;

public class VueControleur extends Application{
    //modele du jeu 
    private GrilleModele jeu;
    
    //méthode de lancement de la fenêtre
    @Override
    public void start(Stage primaryStage) {
        init(primaryStage);
    }
    
    //implémentation du mode survival
    public void survival(Stage primaryStage){
        // nivieau 1 : on lance une partie avec 10 lignes et 10 colonnes et avec 5 mines 
        init(primaryStage,10,10,5,false);
        jeu.addObserver(new Observer(){
            @Override
            public void update(Observable o,Object arg){
                if(jeu.isGagne())
                {
                    //Si on gagne on continue la partie 
                    // nivieau 2 : on lance une partie avec 15 lignes et 15 colonnes et avec 20 mines
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
                            //Si on gagne on continue la partie 
                            // nivieau 3 (final : on lance une partie avec 20 lignes 
                            //et 20 colonnes et avec 60 mines
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
                                     //Si on gagne : on a terminé le mode survival 
                                     Alert victoire3 = new Alert(AlertType.INFORMATION);   
                                     victoire3.setTitle("Victoire finale");
                                     victoire3.setHeaderText(null);
                                     victoire3.setContentText("Felicitation vous êtes arrivé au bout de ce survival");
                                     victoire3.show();
                                 }
                                 else if (jeu.isPerdu())  
                                 {
                                     //Si on perd au niveau 3, on peut choisir de recommencer du début
                                     //le mode ou refuser et sélectionner un autre type de partie                                      
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
                            //Si on perd au niveau 2, on peut choisir de recommencer du début
                            //le mode ou refuser et sélectionner un autre type de partie
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
                    //Si on perd au niveau 1, on peut choisir de recommencer du début
                    //le mode ou refuser et sélectionner un autre type de partie
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
    
    public void modeTempsLimite(Stage primaryStage){
        int[] temp = new int[3];
        temp[0]=15;
        temp[1]=15;
        temp[2]=40;
        init(primaryStage,temp,true,18);
    }
    
    //méthode qui lance la grille initiale avec 10 lignes, 10 colonnes et 10 mines
    public void init(Stage primaryStage){
        int[] temp = new int[3];
        temp[0]=temp[1]=temp[2]=10;
        init(primaryStage,temp,true,-1);
    }
    
    //méhode qui lance une grille de avec des paramètres choisis et un type défini
    public void init(Stage primaryStage,int nbligne, int nbcolonne, int nbmine,boolean obsfin)
    {
        int[] temp = {nbligne, nbcolonne, nbmine};
        init(primaryStage,temp,obsfin,-1);
    }
    
    //méthode initiale de création de la fenêtre qui prend en argument une scène, un tableau
    //où sont rangées les différentes variables du plateau de jeu (lignes, colonnes et mines)
    //et un boolean permettant de définir les observateurs en fonction du ytpe de parie choisi
    public void init(Stage primaryStage, int[] caracs, boolean obsfin, int tempsMax){
        jeu=new GrilleModele(caracs[0], caracs[1], caracs[2]);
        BorderPane border = new BorderPane();
        Group persogroupe = new Group();
        
        //implémentation du menu de la fenêtre 
        final MenuBar menu =new MenuBar();
        
        //implémentation du menu possédant les différents niveaux de difficultés
        //Le niveau de difficulté dépend de la taille de la grille et du nombre de mines 
        final Menu difficulte = new Menu("Niveau de difficulté");
        final MenuItem facile=new MenuItem("Facile");
        facile.setOnAction(actionEvent -> init(primaryStage));
        final MenuItem moyen = new MenuItem("Moyen");
        moyen.setOnAction(actionEvent -> init(primaryStage,15,15,40,true));
        final MenuItem difficile = new MenuItem("Difficile");
        difficile.setOnAction(actionEvent -> init(primaryStage, 20, 20, 120,true));
        final MenuItem personnalise = new MenuItem("Personnalisé");
        personnalise.setOnAction(actionEvent -> border.setTop(persogroupe));
        difficulte.getItems().setAll(facile,moyen,difficile,personnalise);
        
        //Implémentation du menu avec les différents modes de jeu 
        final Menu mode = new Menu("Mode de jeu");
        final MenuItem survival = new MenuItem("Survival");
        survival.setOnAction(actionEvent -> survival(primaryStage));
        final MenuItem tempsLimite = new MenuItem("Temps limité");
        tempsLimite.setOnAction(actionEvent -> modeTempsLimite(primaryStage));
        mode.getItems().setAll(survival,tempsLimite);
        
        //Implémentation de la barre pour créer une grille de jeu personnalisée 
        GridPane personalise = new GridPane();
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
        personalise.add(new Label("  Lignes :"),0,0);
        personalise.add(longueur,1,0);
        personalise.add(new Label("  Colonnes :"),2,0);
        personalise.add(largeur,3,0);
        personalise.add(new Label("  Mines :"),4,0);
        personalise.add(mines,5,0);
        personalise.add(new Label("  "),6,0);
        personalise.add(jouer,7,0);
        //Création de l'observateur pour créer une partie personnalisée
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
                            init(primaryStage,c,true,-1);
                    }
                });
        //On ajoute les différents éléments dans la barre de personnalisation
        persogroupe.getChildren().add(personalise);
        
        //On place le menu en haut et ajout les menus difficulté et mode
        border.setTop(menu);
        menu.getMenus().setAll(difficulte,mode);
        
        //taille de base des cases 
        int taille = 40;
        //définition de la partie du plateau 
        Group milieu = new Group();
        GridPane plateau = new GridPane();
        // pour essayer d'enlever le halo bleu autour d'une case quand on clique dessus
        milieu.setStyle("-fx-focus-color: transparent;"
                +"-fx-faint-focus-color: transparent;");
        for (int i =0; i<jeu.getLignes();i++){
            for(int j=0; j<jeu.getColonnes(); j++){
                //chaque case possède un bouton et un id unique 
                final int id = i*jeu.getColonnes()+j;
                Button b= new Button();
                b.setPrefSize(taille,taille);
                b.setMaxSize(taille, taille);
                //chaque case peut être cliquée on ajoute donc un événement
                //si on fait un clic gauche, on joue dans cette case
                //si on fait un clic droit, on met un drapeau sur cette case
                b.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event){
                        if(event.getButton().equals(MouseButton.PRIMARY)&&!jeu.getCase(id).isDrapeau())
                            jeu.jouer(id);
                        else if(event.getButton().equals(MouseButton.SECONDARY))
                            jeu.modifDrapeau(id);
                    }
                });
                //on ajoute un observeur pour voir le contenu de la case
                jeu.addObserver(new Observer(){
                    @Override
                    public void update(Observable o,Object arg){
                        if(jeu.getCase(id).isVisible()){
                            if (jeu.getCase(id).getValeur()>=0)
                            {
                                b.setText(""+jeu.getCase(id).getValeur());
                                switch(jeu.getCase(id).getValeur()){
                                    //on change la couleur de l'affichage en fonction du nombre
                                    case 1: b.setTextFill(Color.BLUE);break;
                                    case 2: b.setTextFill(Color.GREEN);break;
                                    case 3: b.setTextFill(Color.RED);break;
                                    case 4: b.setTextFill(Color.BROWN);break;
                                    case 5: b.setTextFill(Color.ORANGE);break;
                                    case 6: b.setTextFill(Color.GRAY);break;
                                    case 7: b.setTextFill(Color.YELLOW);break;
                                    case 8: b.setTextFill(Color.BLACK);break;
                                }
                                //on met le texte en gras
                                b.setStyle("-fx-font-weight: bold;");
                                b.setGraphic(null);
                                b.setDisable(true); 
                            }
                        }
                        //si on pose un drapeau sur la case on met l'image du drapeau 
                        else if(jeu.getCase(id).isDrapeau()){
                            String drapeauURI = new File("image/drapeau.png").toURI().toString();
                            Image drapeau = new Image(drapeauURI,b.getWidth()*0.6,b.getHeight()*0.6,false,false);
                            ImageView drapeauView = new ImageView(drapeau);
                            b.setGraphic(drapeauView);
                        }
                        else{
                            //si on reclique droit dessus on enlève le drapeau
                            b.setGraphic(null);
                        }
                        
                        if(jeu.isGagne()||jeu.isPerdu()){
                            //si la case cliquée est une mine on affiche l'image de la mine 
                            if (jeu.getCase(id).getValeur()==-1){
                                String mineURI = new File("image/mine.png").toURI().toString();
                                Image mine = new Image(mineURI,b.getWidth()*0.6,b.getHeight()*0.6,false,false);
                                ImageView mineView = new ImageView(mine);
                                b.setGraphic(mineView);
                                b.setText(null);
                              }
                            //dans tous les cas on désactive la case 
                            b.setDisable(true);
                        }
                    }
                });
                plateau.add(b, j, i);
            }
        }
        //On place le plateau au milieu 
         milieu.getChildren().add(plateau);
         
        // Observer fin de partie
        if (obsfin)
        {
            //on observe la partie pour voir si elle est fini et son résultat 
            jeu.addObserver(new Observer(){
                boolean fenetreDejaAff = false;
                @Override
                public void update(Observable o,Object arg){
                    if(!fenetreDejaAff){
                        if(jeu.isGagne())
                        {
                            fenetreDejaAff=true;
                            //si on a gagné on affiche une fenêtre pop up pour le signaler au joueur  
                            Alert victoire = new Alert(AlertType.INFORMATION);   
                            victoire.setTitle("Victoire");
                            victoire.setHeaderText(null);
                            victoire.setContentText("Felicitation vous avez gagné !!!");
                            victoire.show();
                        }
                        else if (jeu.isPerdu()&&tempsMax<=0)
                        {
                            fenetreDejaAff=true;
                            //si on a perdu on affiche une fenêtre pop up pour le signaler au joueur
                            //on peut recommencer la même partie en cliquant sur OK
                            Alert defaite = new Alert(AlertType.CONFIRMATION);   
                            defaite.setTitle("Défaite");
                            defaite.setHeaderText(null);
                            defaite.setHeaderText("Oh non, vous avez perdu ...");
                            defaite.setContentText("Voulez-vous recommencez la partie ? ");
                            Optional<ButtonType> result = defaite.showAndWait();
                            if (result.get() == ButtonType.OK){
                                int[] c = new int[3];
                                c[0]=jeu.getLignes();
                                c[1]=jeu.getColonnes();
                                c[2]=jeu.getMines();
                                init(primaryStage,c,obsfin,tempsMax);
                            }
                        }
                        else if(jeu.isPerdu()){
                            fenetreDejaAff=true;
                            //si on perd EN MODE TEMPS LIMITE
                            // On ne peut pas mettre la fenetre classique car la fonction
                            // showandwait ne peut pas etre appelée depuis un timer
                            Alert defaite = new Alert(AlertType.INFORMATION);   
                            defaite.setTitle("Defaite");
                            defaite.setHeaderText(null);
                            defaite.setContentText("Oh non, vous avez perdu ...");
                            defaite.show();
                        }
                    }
                    
                }
            });
        }
       
        //conteneur des composants affichés en bas de fenêtre
        Group bas = new Group();
        GridPane contenubas = new GridPane();
        
        // Ajout du timer pour le temps
        if(tempsMax<=0){
            contenubas.add(new Label("Temps : "),0,0);
            Label tempsaff=new Label("0");
            contenubas.add(tempsaff,1,0);
            Timeline timeline = new Timeline();
            AnimationTimer timersec = new AnimationTimer() {
                int time=0;
                int ms=0;
                @Override
                public void handle(long l) {
                    ms++;
                    if(ms>=60&&jeu.isEnCours()){
                        tempsaff.setText(""+time);
                        time++;
                        ms=0;
                    }
                    if(jeu.isPerdu())
                        this.stop();
                }
            };
            timeline.play();
            timersec.start();
        }
        else{
            contenubas.add(new Label("Temps restant : "),0,0);
            Label tempsaff=new Label(""+tempsMax);
            contenubas.add(tempsaff,1,0);
            Timeline timeline = new Timeline();
            AnimationTimer timersec = new AnimationTimer() {
                int time=tempsMax;
                int ms=0;
                @Override
                public void handle(long l) {
                    ms++;
                    if(time<0){
                        jeu.setPerdu();
                        this.stop();
                    }
                    else if(jeu.isPerdu())
                        this.stop();
                    else if(ms>=60&&jeu.isEnCours()){
                        time--;
                        tempsaff.setText(""+time);
                        ms=0;
                    }
                }
            };
            timeline.play();
            timersec.start();
        }
        
        // Ajout du compteur de mines restantes
        contenubas.add(new Label("     Mines restantes : "),2,0);
        final Label minesaff=new Label(""+(jeu.getMines()-jeu.getDrapeaux()));
        contenubas.add(minesaff,3,0);
        jeu.addObserver(new Observer(){
            @Override
            public void update(Observable o,Object arg){
                minesaff.setText(""+(jeu.getMines()-jeu.getDrapeaux()));
            }
        });
        
        //ajout dans les containeurs des composants
        bas.getChildren().add(contenubas);
        
        
        int width = min(jeu.getColonnes()*taille+10,800);
        int height = min(jeu.getLignes()*taille+60,600);
        // ajout espace pour l'éventuelle scrollbar
        if(width>780)
            height+=15;
        if(height>580)
            width+=15;
        Scene scene = new Scene(border,width,height);
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(milieu);
        border.setCenter(scrollPane);
        border.setBottom(bas);
        primaryStage.setTitle("Demineur");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(250);
        primaryStage.setMinHeight(60);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch();
    }     
}