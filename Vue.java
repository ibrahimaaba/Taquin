import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.ActionEvent;
import java.io.*;
import javax.imageio.*;

/**
* Classe utilisé pour la creation de l'interfaca graphique
*/
public class Vue extends JFrame{
    private Joueur joueur;
    private JPanel accueilHaut, accueilBas, reglesDuJeu, chargerJeux, pageDeJeu, panelJeu;
    private JLabel  height, algo, chemin, nbreMouvement, temps;
    private JButton jouer, charger, regles, validerPseudo, retourAccueil, pcel, aEtoile, dijk, generer;
    private JSlider taille;
    private JTextField pseudo;
    private Configuration configuration;

    /**
    *Créer une page d'accueil lors de la creation de l'objet
    *
    */
    public Vue(){
        joueur = new Joueur();
        joueur.lire();
        configuration = new Configuration(3,3);
        this.setTitle("Taquin 4");
        this.setSize(1250, 800);
        this.setResizable(false);
        getContentPane().setLayout(new GridLayout(2,1));

        chemin = new JLabel();
        nbreMouvement = new JLabel();
        temps = new JLabel();
        jouer = new JButton("Jouer");
        charger = new JButton("Charger");
        regles = new JButton("Règles");
        validerPseudo = new JButton("Valider");
        retourAccueil = new JButton("Retour à l'accueil");

        panelAcceuil();

        /* Action Listener */

        regles.addActionListener((event)->{
            getContentPane().setLayout(new GridLayout(1,1));
            getContentPane().removeAll();
            getContentPane().add(panelRegles());
            getContentPane().validate();
            getContentPane().repaint();

        });

        charger.addActionListener((event)->{
            getContentPane().setLayout(new GridLayout(1,1));
            getContentPane().removeAll();
            panelCharger();
            getContentPane().revalidate();
            getContentPane().repaint();
        });

        jouer.addActionListener(
                (ActionEvent e)->{
                    panelJeu();
                    this.revalidate();
                    this.repaint();
                });

        validerPseudo.addActionListener((event) ->{
            joueur.setNom(pseudo.getText());
            joueur.sauvegarder();
        });

        retourAccueil.addActionListener((event)->{
            getContentPane().setLayout(new GridLayout(2,1));
            getContentPane().removeAll();
            panelAcceuil();
            getContentPane().validate();
            getContentPane().repaint();
        });


        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    /**
    * Permet de charger la page d'accueil dans la fenetre
    *
    */
    public void panelAcceuil(){
        JPanel accueil = new JPanel();

        accueilHaut = new JPanel();
        accueilBas = new JPanel();

        accueilHaut.setBackground(Color.WHITE);
        accueilBas.setBackground(Color.GRAY);

        accueilHaut.setLayout(null);
        accueilBas.setLayout(null);

        getContentPane().add(accueilHaut);
        getContentPane().add(accueilBas);

        // Panneau HAUT
        JLabel titre = new JLabel();
        titre.setText("Bienvenue sur le jeu du Taquin !");
        titre.setFont(new Font("New Times Roman", Font.BOLD, 40));
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setVerticalAlignment(SwingConstants.TOP);
        titre.setBounds(150,20,1000,50);
        accueilHaut.add(titre);

        JLabel id=new JLabel("Votre pseudo :");
        id.setBounds(450,100,300,50);
        accueilHaut.add(id);

        pseudo = new JTextField(joueur.getNom());
        pseudo.setBounds(480,150,300,50);
        accueilHaut.add(pseudo);

        accueilHaut.add(validerPseudo);
        validerPseudo.setBounds(790,150,100,50);

        //Panneau BAS
        accueilBas.add(jouer);
        accueilBas.add(charger);
        accueilBas.add(regles);
        jouer.setBounds(150,100,300,90);
        charger.setBounds(480,100,300,90);
        regles.setBounds(810,100,300,90);
    }

    /**
    *Permet de charger la page de regle dans la fenetre
    *
    */
    public JPanel panelRegles(){
        reglesDuJeu = new JPanel();
        reglesDuJeu.setLayout(null);
        reglesDuJeu.add(retourAccueil);
        retourAccueil.setBounds(100,500,1050,150);
        JLabel texte=new JLabel("<html>Résolution manuelle :<br>Afin de résoudre le célèbre jeu du taquin, il faut déplacer les cases verticalement et horizontalement sur la case vide.<br>Une fois avoir déplacer les cases nécéssaires et qu'elles sont dans l'ordre croissant, vous avez gagnez !<br><br>Résolution automatique :<br>Sélectionner la taille de la configuation de taquin souhaité.<br>Choisir l'algorithme de votre choix.<br>Cliquez sur Lancer !<br></html>");
        JLabel continuation=new JLabel("Profitez bien du jeu !!!");
        texte.setFont(new Font("New Times Roman", Font.BOLD, 15));
        texte.setBounds(50,50,1150,400);
        continuation.setBounds(500,150,1150,400);
        continuation.setFont(new Font("New Times Roman", Font.BOLD, 17));
        reglesDuJeu.add(texte);
        reglesDuJeu.add(continuation);
        return reglesDuJeu;
    }

    /**
    *Permet de charger le panel pour aficher la liste des taquins resolu
    *
    */
    public void panelCharger(){
        chargerJeux = new JPanel();
        chargerJeux.setLayout(null);
        getContentPane().add(chargerJeux);

        ArrayList<Configuration> jeux = this.joueur.getJeux();

        chargerJeux.add(retourAccueil);
        retourAccueil.setBounds(530,20,170,80);
        if(jeux != null) {
            int i = 0;
            int j = 0;
            int x = jeux.size()-1;
            if(x>49){
                for(int k=0; k<jeux.size()-50; k++){
                    this.joueur.getJeux().remove(k);
                }
                this.joueur.sauvegarder();
                x=49;
            }
            while(x >= 0){
                Configuration c = jeux.get(x);
                JButton tmp = new JButton((x+1) + " (" + c.getLargeur() + "x" + c.getHauteur()+")");
                tmp.setLayout(new GridLayout(3,1));
                tmp.setBounds(i*115+50,j*115+130,100,100);
                chargerJeux.add(tmp);
                i++;
                x--;
                if(i>9){
                    i = 0;
                    j++;
                }
                tmp.addActionListener((event)->{
                    getContentPane().setLayout(new GridLayout(1,1));
                    getContentPane().removeAll();
                    taquinCharger(c);
                    getContentPane().validate();
                    getContentPane().repaint();
                });
            }

        }
    }
    /**
    *Permet de charger les differente configurations vus afin de les revoirs
    *
    *@param c Configuration chargée
    */
    public void taquinCharger(Configuration c){
            JPanel chargerTaquin = new JPanel();
            chargerTaquin.setLayout(null);
            getContentPane().add(chargerTaquin);
            chargerTaquin.add(charger);
            charger.setBounds(530,20,170,80);
            int tailleBouton = 500/c.getLargeur();
            for(int i=0; i<c.getHauteur(); i++){
                for(int j=0; j<c.getLargeur(); j++){
                    int x = c.getTableauInitial()[i][j];
                    if(x != 0) {
                        JButton tmp = new JButton("" + x);
                        tmp.setBounds(350+tailleBouton*j, 140+tailleBouton*i,tailleBouton, tailleBouton);
                        chargerTaquin.add(tmp);
                    }

            }
        }
        JLabel texteChecminRes = new JLabel("Chemin de résolution :");
        JLabel chemin = new JLabel(c.getChemin());
        texteChecminRes.setBounds(550,650, 200,30);
        chemin.setBounds(500,690, 400,50);
        chargerTaquin.add(texteChecminRes);
        chargerTaquin.add(chemin);
    }
    /**
    *Permet de charger le panel pour afficher la page de jeu principale
    *
    */
    public void panelJeu() {

        this.getContentPane().removeAll();
        this.pageDeJeu = new JPanel();
        this.setLayout(new GridLayout(1,1));
        this.pageDeJeu.setBounds(0,0,1250, 800);
        this.pageDeJeu.setBackground(new Color(233,233,233));
        this.pageDeJeu.setLayout(null);
        this.add(pageDeJeu);
        this.taille = new JSlider(2,6,3);
        this.taille.setBounds(0, 180, 110, 400);
        this.taille.setMajorTickSpacing(1);
        this.taille.setPaintTicks(true);
        this.taille.setPaintLabels(true);
        this.taille.setOrientation(SwingConstants.VERTICAL);
        pageDeJeu.add(retourAccueil);
        retourAccueil.setBounds(20,20,200,50);
        this.panelJeu = new JPanel();
        this.panelJeu.setLayout(null);
        this.panelJeu.setBounds(200, 180, 800, 500);
        this.panelJeu.setBackground(new Color(139,69,19));
        this.height = new JLabel("Taille");
        this.height.setBounds(0, 0, 70, 20);

        this.algo = new JLabel("ALGORITHME");
        this.algo.setBounds(425, 10, 400, 50);
        this.algo.setFont(new Font("Serif",Font.PLAIN,50));

        this.aEtoile = new JButton("A*");
        this.aEtoile.setBounds(248, 94, 200, 50);
        this.aEtoile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.dijk = new JButton("Dijkstra");
        this.dijk.setBounds(508, 94, 200, 50);
        this.dijk.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.pcel = new JButton("Pcel"); //parcours en largeur
        this.pcel.setBounds(770, 94, 200, 50);
        this.pcel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        this.generer = new JButton ("generer");
        this.generer.setBounds(10, 600, 91, 25);
        this.generer.setFocusable(false);
        this.generer.setCursor(new Cursor(Cursor.HAND_CURSOR));

        this.pageDeJeu.add(generer);
        this.pageDeJeu.add(this.panelJeu);
        this.pageDeJeu.add(this.taille);
        this.pageDeJeu.add(this.dijk);
        this.pageDeJeu.add(this.aEtoile);
        this.pageDeJeu.add(this.pcel);
        this.pageDeJeu.add(this.algo);
        genererTaquin();

        this.generer.addActionListener((event) -> {
            int t = this.taille.getValue();
            this.panelJeu.removeAll();
            this.configuration = new Configuration(t,t);
            genererTaquin();
            this.panelJeu.validate();
            this.panelJeu.repaint();
        });

        this.pcel.addActionListener((event) -> {
        	long tempsDebut = System.nanoTime();
        	ParcoursLargeur pl = new ParcoursLargeur(this.configuration);
        	Configuration c = pl.parcoursEnLargeur();
        	long tempsFin = System.nanoTime();
        	String tps = Double.toString(((tempsFin - tempsDebut)/1000000)/1000.0);
        	temps.setText("résolu en : "+tps + "s");
        	temps.setBounds(270, 140, 400, 50);
        	temps.setFont(new Font("Serif",Font.CENTER_BASELINE,15));
        	this.pageDeJeu.add(temps);
        	AffichageAlgo(c);
        	this.repaint();
        	this.revalidate();
        });

        this.dijk.addActionListener((event) -> {
        	long tempsDebut = System.nanoTime();
        	Dijkstra d = new Dijkstra(this.configuration);
        	Configuration c = d.dijkstra();
        	long tempsFin = System.nanoTime();
        	String tps = Double.toString(((tempsFin - tempsDebut)/1000000)/1000.0);
        	temps.setText("résolu en : "+tps + "s");
        	temps.setBounds(270, 140, 400, 50);
        	temps.setFont(new Font("Serif",Font.CENTER_BASELINE,15));
        	this.pageDeJeu.add(temps);
        	AffichageAlgo(c);
        	this.repaint();
        	this.revalidate();
        });

        this.aEtoile.addActionListener((event) -> {
        	long tempsDebut = System.nanoTime();
        	Aetoile ae = new Aetoile(this.configuration);
        	Configuration c = ae.aetoile();
        	long tempsFin = System.nanoTime();
        	String tps = Double.toString(((tempsFin - tempsDebut)/1000000)/1000.0);
        	temps.setText("résolu en : "+tps + "s");
        	temps.setBounds(270, 140, 400, 50);
        	temps.setFont(new Font("Serif",Font.CENTER_BASELINE,15));
        	this.pageDeJeu.add(temps);
        	AffichageAlgo(c);
        	this.repaint();
        	this.revalidate();
        });
    }
    /**
    *Permet de generer un taquin de la taille voulu et jouer dessus
    *
    */
    public void genererTaquin(){
        int tailleBtn = 460/configuration.getLargeur();
        for(int i=0; i<configuration.getHauteur(); i++){
            for(int j=0; j<configuration.getLargeur(); j++){
                int n = configuration.getTableau()[i][j];
                if(n != 0) {
                    String num = Integer.toString(configuration.getTableau()[i][j]);
                    JButton btn = new JButton(num);
                    btn.setBounds(180+tailleBtn*j, 20+tailleBtn*i,tailleBtn, tailleBtn);
                    this.panelJeu.add(btn);

                    int finalI = i;
                    int finalJ = j;

                    btn.addActionListener((event)->{
                        String dir = configuration.directionMouvement(finalI, finalJ);
                        configuration.mouvement(dir);
                        this.panelJeu.removeAll();
                        genererTaquin();
                        this.panelJeu.revalidate();
                        this.panelJeu.repaint();
                        felicitations();
                    });
                }
            }
        }
    }

    /**
     * Permet l'affichage d'un message de félicitation à la fin du jeu
     */

    public void felicitations(){
        if(configuration.jeuGagne()){
            this.panelJeu.removeAll();
            // sauvegarde
            this.joueur.getJeux().add(configuration);
            this.joueur.sauvegarder();
            //Message de bravo
            JLabel bravo=new JLabel("Félicitations ! Vous avez gagné !");
            bravo.setFont(new Font("New Times Roman", Font.BOLD, 25));
            bravo.setBounds(200,80,500,80);
            panelJeu.add(bravo);

            //Bouton pour rejouer
            JButton rejouer=new JButton("Rejouer");
            rejouer.setBounds(250,200,300,90);
            this.panelJeu.add(rejouer);

            rejouer.addActionListener((event)->{
                panelJeu.removeAll();
                int t=taille.getValue();
                configuration=new Configuration(t,t);
                genererTaquin();
                this.panelJeu.repaint();
                this.panelJeu.revalidate();
            });

            this.panelJeu.repaint();
            this.panelJeu.revalidate();
        }
    }
/**
*Permet d'utiliser un algorithme et de voir son temps d'execution et le chemin de resolution du taquin
*
*/

    public void AffichageAlgo(Configuration c) {
    	String ch = c.getChemin(); //affichage du chemin
    	String nbr = Integer.toString(c.getChemin().length());
    	ParcoursLargeur pl = new ParcoursLargeur(this.configuration);
    	chemin.setText(ch);
    	chemin.setBounds(580, 140, 400, 50);
    	chemin.setFont(new Font("Serif",Font.CENTER_BASELINE,15));
        nbreMouvement.setText(nbr+" Mouvements :");
    	nbreMouvement.setBounds(430, 140, 400, 50);
    	nbreMouvement.setFont(new Font("Serif",Font.CENTER_BASELINE,15));
    	this.pageDeJeu.add(chemin);
    	this.pageDeJeu.add(nbreMouvement);

    }

}
