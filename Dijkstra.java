import java.util.*;

public class Dijkstra {
    private Configuration depart;
    private HashSet<String> vu;
    private FdPg<Configuration> file;

    /**
     * Contructeur de l'algorithme du parcours en largeur
     * @param d Configuration initiale
     */
    public Dijkstra(Configuration d){
        this.depart = d;
        this.vu = new HashSet<>();
        this.file = new FdPg<Configuration>();
    }

    /**
     * R&eacute;sout un taquin avec l'algorithme de Dijkstra
     * @return la configuration r&eacute;solue &agrave; partir d'une configuration quelconque
     */
    public Configuration dijkstra() {
        try {
            this.vu.add(this.depart.tableauEnString());
            this.file.Ajouter(this.depart, 0);
            while (!this.file.EstVide()) {
                Configuration min = this.file.ExtraireMin();
                this.vu.add(min.tableauEnString());
                if (min.jeuGagne())
                    return min;
                int distance = min.getChemin().length();
                min.successeurs();
                ArrayList<Configuration> fils = min.getSuccesseur();
                for (Configuration tmp : fils) {
                    if (this.vu.add(tmp.tableauEnString())) {
                        this.file.Ajouter(tmp, distance + 1);
                    }
                }
            }
        }catch (OutOfMemoryError oome){
            this.file.hmap.clear();
            this.vu.clear();
            System.out.println("Aucune solution n'a pu être trouvée");
        }
        return null;
    }
}