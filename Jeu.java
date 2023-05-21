import java.util.Scanner;

public class Jeu {
    private Scanner scanner;
    private Joueur joueur;
    private Configuration configuration;

    /**
     * Constructeur de la classe Jeu
     */

    public Jeu() {
        this.scanner = new Scanner(System.in);
        this.joueur = new Joueur();
        this.nouvellePartie();
    }

    /**
     * Appelle les fonctions pour cr&eacute;er une partie de taquin du début &agrave; la fin
     */
    public void nouvellePartie(){
        System.out.println("  ------");
        System.out.println("| TAQUIN |");
        System.out.println("  ------");
        System.out.println("Comment vous appelez-vous ?");
        String pseudo = scanner.nextLine();
        this.joueur.setNom(pseudo);
        System.out.println("Bienvenue " + joueur.getNom() + " !");
        if(!jouerOuRegles()){
            reglesDuTaquin();
        }
        jouer();
    }

    /**
     * M&eacute;thode demandant la taille du taquin souhait&eacute; et si on r&eacute;sout le taquin manuellement ou automatiquement
     */

    public void jouer() {
        System.out.println();
        System.out.println("Jouons, mais commençons par créer le taquin \n" +
                "De quelle taile voulez vous que votre Taquin soit ?");
        creationTaquin();
        this.configuration.afficher();
        joueurOuRobot();
        if(!this.joueur.getRobot()){
            String mouv = "";
            long tempsDebut, tempsFin;
            tempsDebut = System.nanoTime();
            while (!this.configuration.jeuGagne()) {
                demanderMouvement();
                this.configuration.afficher();
            }
            tempsFin = System.nanoTime();
            System.out.println("Félicitations " + joueur.getNom() + " ! Vous avez gagné en : "
                    + ((tempsFin - tempsDebut)/1000000)/1000.0 + "s");
        }
        System.out.println();
        rejouerOuQuitter();
    }

    /**
     * Cette fonction retourne soit la partie à jouer ou afficher les r&egrave; du jeu
     * @return
     */

    public boolean jouerOuRegles() {
        System.out.println("Voulez-vous (j)ouer ou (a)pprendre les règles?");
        String reponse = scanner.nextLine();
        if (reponseEstVraie(reponse, "jouer")) {
            return true;
        } else if (reponseEstVraie(reponse, "apprendre")) {
            return false;
        } else {
            System.out.println("Je n'ai pas compris.");
            return jouerOuRegles();
        }
    }

    /**
     * Affichage des regles du jeu
     */

    public void reglesDuTaquin(){
        System.out.println("  ----------------\n"    +
                "| Règles du Taquin |\n" +
                "  ----------------");
        System.out.println("Règles : \n"                                                                                        +
                "     -Vous avez un tableau de N*N cases \n"                                                         +
                "     -Le tableau est mélangé avec des cases allant de 0 à N*N - 1 \n"                               +
                "     -La case vide (0) doit être déplacé (si c'est possible) vers le haut, bas, gauche et droite\n" +
                "     -Votre but est de le trier (at d'arriver a la configuration finale le plus rapidement possible, avec un minimum de coups");
        System.out.println("Exemple :");
        System.out.println(" Un tableau de taille 3 x 3 ressemble à : ");
        Configuration exemple = new Configuration(3, 3);
        exemple.afficher();
        System.out.println(" La configuration finale de ce taquin est :");
        Aetoile a = new Aetoile(exemple);
        Configuration res = a.aetoile();
        res.afficher();
        System.out.println(" Et le chemin pour y arriver serait de déplacer la case vide de cette façon : ");
        System.out.println("  " + res.getChemin());
    }

    /**
     * Cr&eacute;ation d'une configuration avec la taille souhaité
     */

    public void creationTaquin(){
        System.out.println("Choisissez une taille entre 3 et 10 compris : ");
        String rep = scanner.nextLine();
        if(estInteger(rep)){
            int x = Integer.parseInt(rep);
            if(x < 3 || x > 10){
                System.out.println("La taille ne correspond pas.");
                creationTaquin();
            }else{
                this.configuration = new Configuration(x, x);
                System.out.println("Votre taquin de taille " + x + "x" + x + " a été créé.");
                System.out.println();
            }
        }else{
            System.out.println("Je n'ai pas compris.");
            creationTaquin();
        }
    }
    
    /**
     * Fonction donnant le choix entre une resolution manuelle ou automatique avec un des algorithmes
     */

    public void joueurOuRobot(){
        System.out.println("Souhaitez-vous (j)ouer ou laisser le (r)obot faire?");
        String rep = scanner.nextLine();
        if(reponseEstVraie(rep, "jouer")){
            this.joueur.setRobot(false);
            System.out.println("Bonne chance");
            System.out.println();
        }else if(reponseEstVraie(rep, "robot")){
            this.joueur.setRobot(true);
            System.out.println("Votre robot doit fonctionner avec quel algorithme?");
            System.out.println("(p)arcours en largeur, (d)ijkstra, (a)etoile");
            System.out.println("/!\\ Les algorithmes ne peuvent pas résoudre tous les taquins.");
            System.out.println("/!\\ A* est à privilégier");
            choixAlgo();
        }else{
            System.out.println("Je n'ai pas compris.");
            joueurOuRobot();
        }
    }

    public void choixAlgo(){
        String repAlgo = scanner.nextLine();
        if(reponseEstVraie(repAlgo, "parcours") || reponseEstVraie(repAlgo, "parcours en largeur")){
            new Algorithme(this.configuration, new ParcoursLargeur(this.configuration));
        }else if(reponseEstVraie(repAlgo, "dijkstra")){
            new Algorithme(this.configuration, new Dijkstra(this.configuration));
        }else if(reponseEstVraie(repAlgo, "aetoile")){
            new Algorithme(this.configuration, new Aetoile(this.configuration));
        }else{
            System.out.println("Cet algorithme n'existe pas.");
            System.out.println("Les différents algos sont :");
            System.out.println("(p)arcours en largeur, (d)ijkstra, (a)etoile");
            choixAlgo();
        }
    }

    /**
     * Fonction demandant notre choix de déplacement
     */

    public void demanderMouvement(){
        System.out.println("Où voulez-vous déplacer le 0 ? ( (h)aut, (b)as, (g)auche, (d)roite )");
        String s = scanner.nextLine();
        if(mouvementExiste(s) == null || !this.configuration.mouvement(s)){
            System.out.println("Mouvement non valide.");
            demanderMouvement();
        }
    }

    /**
     * Fonction de fin de partie, le joueur peut choisir entre partir ou de rejouer une nouvelle partie
     */

    public void rejouerOuQuitter(){
        System.out.println("Souhaitez-vous (r)ejouer ou (q)uitter ?");
        String rep = scanner.nextLine();
        if(reponseEstVraie(rep, "quitter")){
            System.out.println("A bientôt");
        }else if(reponseEstVraie(rep, "rejouer")){
            jouer();
        }else{
            System.out.println("Je n'ai pas compris.");
            rejouerOuQuitter();
        }
    }

    /**
     * Verification si les mouvements demand&eacute;s sont existants
     * @param s une chaine de caract&egrave;
     * @return chaine de caracteme si elle est valide
     */
    public String mouvementExiste(String s){
        switch (s.toLowerCase()) {
            case "h":
            case "haut":
                return "haut";
            case "b":
            case "bas":
                return "bas";
            case "g":
            case "gauche":
                return "gauche";
            case "d":
            case "droite":
                return "droite";
        }
        return null;
    }

    public boolean estInteger(String s){
        if(s == null)
            return false;
        try {
            int x = Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    // La fonction regarde si le String donne est egal au String voulu ou si donne est egal a la premiere lettre de voulu
    public boolean reponseEstVraie(String donne, String voulu){
        if(donne.toLowerCase().equals(voulu.toLowerCase())) // donne == voulu
            return true;
        return donne.toLowerCase().equals("" + voulu.toLowerCase().charAt(0)); // si donne == v return true, false sinon
    }


}