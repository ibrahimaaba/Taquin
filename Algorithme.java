public class Algorithme {
    private Object algo;
    private Configuration config;

    /**
     * Constructeur de la classe Algorithme
     * @param c Configuration initiale
     * @param a Algorithme qu'on utilise pour la r&eacute;solution
     */

    public Algorithme(Configuration c, Object a){
        this.algo = a;
        this.config = c;
        executerAlgorithme();
    }

    /**
     * Affiche les infos a propos de l'ex&eacute;cution d'un algorithme quelconque
     */
    public void executerAlgorithme(){
        Configuration resultat = null;
        long tempsDebut, tempsFin;
        System.out.println();
        try {
            tempsDebut = System.nanoTime();
            if(this.algo instanceof ParcoursLargeur){
                System.out.println("Résolution du taquin avec le Parcours en Largeur :");
                ParcoursLargeur pl = new ParcoursLargeur(this.config);
                resultat = pl.parcoursEnLargeur();
            }else if(this.algo instanceof Dijkstra){
                System.out.println("Résolution du taquin avec Dijkstra :");
                Dijkstra d = new Dijkstra(this.config);
                resultat = d.dijkstra();
            }else if(this.algo instanceof Aetoile){
                System.out.println("Résolution du taquin avec A* :");
                Aetoile ae = new Aetoile(this.config);
                resultat = ae.aetoile();
            }
            tempsFin = System.nanoTime();
            resultat.afficher();
            System.out.println();
            System.out.println("Résolu en : " + ((tempsFin - tempsDebut)/1000000)/1000.0 + "s");
            System.out.println("Longueur du chemin : " + resultat.getChemin().length());
            System.out.println("Chemin de résolution : " + resultat.getChemin());
        }catch (NullPointerException npe) {
            System.out.println("Aucune solution ne peut être trouvée");
        }
    }
}
