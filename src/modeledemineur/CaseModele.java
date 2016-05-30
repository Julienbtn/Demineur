package modeledemineur;

// Mine : valeur = -1
public class CaseModele {
    private int valeur;
    private boolean drapeau;
    private boolean visible;
    
    public CaseModele(){
        valeur=0;
        drapeau=false;
        visible=false;
    }
    
    public void setValeur(int val){
        valeur=val;
    }
    public int getValeur(){
        return valeur;
    }
    public boolean isMine(){
        return valeur==-1;
    }
    public void setMine(){
        valeur=-1;
    }
    public void upValeur(){
        if(valeur>=0)
            valeur++;
    }
    public boolean isDrapeau(){
        return drapeau;
    }
    public void modifDrapeau(){
        drapeau=!drapeau;
    }
    public boolean isVisible(){
        return visible;
    }
    public void setVisible(){
        visible=true;
    }
    
}
