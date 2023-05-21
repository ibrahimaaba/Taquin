import java.lang.reflect.Type;
import java.util.*;
import javax.swing.*;
import java.util.Scanner;

public class TaquinLanceur {

    public static boolean launcher(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Souhaitez vous lancer le taquin en mode (t)erminal, en mode (g)raphique ou bien (q)uitter?");

        String command = scan.next();
        if(command.equals("t")){
            Jeu jeu = new Jeu();
            return true;
        }
        if(command.equals("g")){
            Vue a=new Vue();
            return true;
        }
        if(command.equals("q")){
            return false;

        }
        System.out.println("Commande inconnue");
        return true;
    }

    public static void main(String[] args) {
        boolean run = true;
        while(run){
            run = launcher();
        }
        System.out.println("Fermeture");

        //Configuration c = new Configuration(4,4);
        //int[][] t = {{4,5,6,7},{8,9,10,11},{1,2,3,12},{13,14,15,0}};
        
        //Configuration c = new Configuration(5,5);
        //int[][] t = {{1,2,3,4,10},{6,8,9,5,14},{16,7,11,12,13},{17,18,19,15,20},{21,22,23,0,24}};

        //c.setTableau(t);
        //c.initialisationXY();
        //c.afficher();
        //System.out.println(c.estSoluble());
        //Algorithme a = new Algorithme(c, new Aetoile(c));

    }

}
