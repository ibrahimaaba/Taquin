import java.util.*;
import java.util.Queue;

public class ParcoursLargeur{
    private Configuration configuration;
    private HashSet<String> marqueur;
    private Queue<Configuration> file;

    /**
     * Contructeur de l'algorithme du parcours en largeur
     * @param c Configuration initiale
     */
    public ParcoursLargeur(Configuration c){
        this.configuration = c;
        this.marqueur = new HashSet<>();
        this.file = new LinkedList<>();
    }

    /**
     * R&eacute;sout un taquin avec l'algorithme de parcours en largeur
     * @return la configuration r&eacute;solue &agrave; partir d'une configuration quelconque
     */
    public Configuration parcoursEnLargeur(){
        try {
            marqueur.add(this.configuration.tableauEnString());
            file.add(this.configuration);
            while (!file.isEmpty()) {
                Configuration a = file.poll();
                if (a.jeuGagne()) {
                    System.out.println("Taille du marqueur : " + marqueur.size());
                    return a;
                }
                // Verifie chaque deplacement du taquin a
                for (int i = 0; i < a.getDeplacements().length; i++) {
                    Configuration tmp = new Configuration(a);
                    tmp.mouvement(a.getDeplacements()[i]);
                    if (this.marqueur.add(tmp.tableauEnString())) {
                        file.add(tmp);
                    }
                }
            }
        }catch (OutOfMemoryError oome){
            this.file.clear();
            this.marqueur.clear();;
            System.out.println("Aucune solution n'a pu être trouvée");
        }
        return null;
    }
}