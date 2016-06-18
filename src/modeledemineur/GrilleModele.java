package modeledemineur;

import java.util.Observable;
import java.util.Random;

public class GrilleModele extends Observable {
    private int lignes;
    private int colonnes;
    private int mines;
    private boolean premierClic;
    private int drapeaux;
    private int fin;
    //-1 perdu, 1 gagné
    private CaseModele[] plateau;
    // plateau[colonnes * l + c]
    
    public GrilleModele(int nblignes, int nbcolonnes,int nbmines){
        lignes=nblignes;
        colonnes=nbcolonnes;
        mines=nbmines;
        premierClic=true;
        fin=0;
        drapeaux=0;
        plateau = new CaseModele[lignes*colonnes];
        for(int i=0;i<lignes*colonnes;i++)
            plateau[i]=new CaseModele();
    }
    
    // On ne peut pas perdre au premier clic, on place donc les mines en conséquence
    public void placerMines(int idPremierClic){
        int i = 0,val;
        Random rand = new Random();
        while(i<mines){
            val = rand.nextInt(lignes*colonnes);
            if(!plateau[val].isMine() && val != idPremierClic){
                plateau[val].setMine();
                majValeurs(val);
                i++;
            }
        }
    }
    
    public void majValeurs(int id){
        int colonne = id%colonnes;
        int l=(id-colonne)/colonnes-1;
        int c;
        for(int i=0;i<9;i++){
            if(i==3||i==6)
                l++;
            c=colonne-1+(i%3);
            if(c>=0&&c<colonnes&&l>=0&&l<lignes&&i!=4)
                plateau[colonnes*l+c].upValeur();
        }
    }
    
    public void modifDrapeau(int id){
        plateau[id].modifDrapeau();
        drapeaux+=plateau[id].isDrapeau()?1:-1;
        setChanged();
        notifyObservers();
    }
    
    public void jouer(int id){
        if(premierClic){
            placerMines(id);
            premierClic=false;
        }
        boolean nouv = !plateau[id].isVisible();
        plateau[id].setVisible();
        if(!plateau[id].isMine()){
            if(nouv && plateau[id].getValeur()==0){
                int colonne = id%colonnes;
                int l=(id-colonne)/colonnes-1;
                int c;
                for(int i=0;i<9;i++){
                    if(i==3||i==6)
                        l++;
                    c=colonne-1+(i%3);
                    if(c>=0&&c<colonnes&&l>=0&&l<lignes&&i!=4)
                        jouerRecu(colonnes*l+c);
                }
            }
        }
        testFin(id);
        setChanged();
        notifyObservers();
    }
    
    public void jouerRecu(int id){
        boolean nouv = !plateau[id].isVisible();
        if(plateau[id].isDrapeau())
            modifDrapeau(id);
        plateau[id].setVisible();
        if(!plateau[id].isMine()){
            if(nouv && plateau[id].getValeur()==0){
                int colonne = id%colonnes;
                int l=(id-colonne)/colonnes-1;
                int c;
                for(int i=0;i<9;i++){
                    if(i==3||i==6)
                        l++;
                    c=colonne-1+(i%3);
                    if(c>=0&&c<colonnes&&l>=0&&l<lignes&&i!=4)
                        jouerRecu(colonnes*l+c);
                }
            }
        }
    }
    
    public void testFin(int id){
        if(plateau[id].isMine())
            fin =-1;
        else{
            int i=0;
            while(i<lignes*colonnes&&(plateau[i].isVisible()||plateau[i].isMine()))
                i++;
            if(i==lignes*colonnes)
                fin=1;
        }
    }
    
    public void setPerdu(){
        fin=-1;
        setChanged();
        notifyObservers();
    }
    
    public int getMines(){
        return mines;
    }
    public int getLignes(){
        return lignes;
    }
    public int getColonnes(){
        return colonnes;
    }
    public CaseModele getCase(int id){
        return plateau[id];
    }
    public int getDrapeaux(){
        return drapeaux;
    }
    public boolean isGagne(){
        return fin==1;
    }
    public boolean isPerdu(){
        return fin==-1;
    }
    public boolean isEnCours(){
        return !premierClic&&fin==0;
    }
}
