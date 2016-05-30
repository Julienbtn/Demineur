package modeledemineur;

import java.util.Random;

// plateau[lignes * l + c] 
public class GrilleModele {
    private int lignes;
    private int colonnes;
    private int mines;
    private CaseModele[] plateau;
    
    
    public GrilleModele(int nblignes, int nbcolonnes,int nbmines){
        lignes=nblignes;
        colonnes=nbcolonnes;
        mines=nbmines;
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
        int colonne = id%lignes;
        int l=(id-colonne)/lignes-1;
        int c;
        for(int i=0;i<9;i++){
            if(i==3||i==6)
                l++;
            c=colonne-1+i%3;
            if(c>=0&&c<colonnes&&l>=0&&l<lignes&&i!=4)
                plateau[lignes*l+c].upValeur();
        }
    }
    
    public boolean jouer(int id){
        boolean nouv = !plateau[id].isVisible();
        plateau[id].setVisible();
        if(plateau[id].isMine())
            return true;
        if(nouv && plateau[id].getValeur()==0){
            int colonne = id%lignes;
            int l=(id-colonne)/lignes-1;
            int c;
            for(int i=0;i<9;i++){
                if(i==3||i==6)
                    l++;
                c=colonne-1+i%3;
                if(c>=0&&c<colonnes&&l>=0&&l<lignes&&i!=4)
                    jouer(lignes*l+c);
            }
        }
        return false;
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
    
}
