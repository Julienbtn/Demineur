import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class VueControleur extends Application{
    
    //Modele m;
    
    public void start(Stage primaryStage) {
        
        //m = new Model();
        
        BorderPane border = new BorderPane();
        
        GridPane gPane = new GridPane();
        
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
        Button b;
        for (int i =0; i<10;i++){
            for(int j=0; j<10; j++){
                b=new Button();
                b.setMinSize(40, 40);
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
        primaryStage.show();
        
    }
        
    
    public static void main(String[] args) {
        launch();
    }
        
    }
    