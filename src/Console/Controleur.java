package Console;

import modeledemineur.CaseModele;
import modeledemineur.GrilleModele;

public class Controleur {
    private InputConsole in;
    
    public Controleur(){
        in = new InputConsole();
    }
    

    public void start() {
        do{
            GrilleModele jeu = ini();
            int l,c;
            do{
                aff(jeu);
                System.out.print("Quel ligne? ");
                l=in.readChose(jeu.getLignes())-1;
                System.out.print("Quel colonne? ");
                c=in.readChose(jeu.getColonnes())-1;
                jeu.jouer(jeu.getColonnes() * l + c);
            }while(!(jeu.isGagne()||jeu.isPerdu()));
            aff(jeu);
            if(jeu.isGagne())
                System.out.println("Felicitations, vous avez gagné !!!");
            else
                System.out.println("Dommage, vous avez perdu");
            System.out.println("\nVoules-vous faire une nouvelle partie :");
            System.out.println("  1. Oui\n  2. Non");
        }while(in.readChose(2)==1);
    }
    
    public GrilleModele ini(){
        int l,c,m;
        do{
            System.out.print("Nombre de lignes : ");
            l=in.readInteger();
            System.out.print("Nombre de colonnes : ");
            c=in.readInteger();
            System.out.print("Nombre de mines : ");
            m=in.readInteger();
        }while(m>=l*c);
        return new GrilleModele(l,c,m);
    }
    
    public void aff(GrilleModele p){
        int l=p.getLignes();
        int c=p.getColonnes();
        CaseModele cm;
        System.out.print("  ");
        for(int i=1;i<=c;i++)
            System.out.print(i+(i<10?".":""));
        System.out.println();
        for(int i=0;i<l*c;i++){
            if(i%c==0)
                System.out.print(i/c+1+(i/c+1<10?".":""));
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
            if(i%c==c-1)
                System.out.println();
            else
                System.out.print("|");
        }
    }
    
}
