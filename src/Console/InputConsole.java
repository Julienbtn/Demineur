//Controleur pour l'utilisateur
//Source : https://github.com/Crazy-Tacos/Une-histoire-de-tacos-Part1
//Auteur : Lilian Varrel
package Console;

import java.util.Scanner;


public class InputConsole {
    private Scanner in;
    
    public InputConsole(){
        in = new Scanner(System.in);
    }
    
    public  int readChose(int max){
        int choix;
        do
            choix = readInteger();
        while (choix < 1 || choix > max);
        return choix;
    }
    public String readString(){
        String str;
        do
            str = in.nextLine();
        while(str.length() == 0);
        return str;
    }
    public int readInteger(){
        int choix = -1;
        String str;
        boolean chiffres = true;
        char c;
        do{
            str = readString();
            for(int i = 0; i<str.length();i++){
                c = str.toCharArray()[i];
                if(c<'0' || c>'9')
                    chiffres = false;
            }
            if (chiffres)
                choix = Integer.parseInt(str); 
            chiffres = true;
        } while (choix < 0);
        return choix;
    }
}
