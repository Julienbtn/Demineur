package modeledemineur;

public class ModeleDemineur {

    public static void main(String[] args) {
        int l=10;
        int c=10;
        int m=100;
        GrilleModele p = new GrilleModele(l,c,m);
        p.placerMines(55);
        p.jouer(55);
        aff(p);
    }
    
    public static void aff(GrilleModele p){
        int l=p.getLignes();
        int c=p.getColonnes();
        CaseModele cm;
        for(int i=0;i<l*c;i++){
            cm=p.getCase(i);
            if(cm.isVisible()){
                if(cm.getValeur()==-1)
                    System.out.print("X");
                else
                    System.out.print(cm.getValeur());
            }
            else if(cm.isDrapeau())
                System.out.print("!");
            else
                System.out.print(" ");
            if(i%l==l-1)
                System.out.println();
            else
                System.out.print("|");
        }
    }
    
}
