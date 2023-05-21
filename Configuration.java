import java.io.Serializable;
import java.util.*;

public class Configuration implements Serializable {
	private int hauteur, largeur; // taille du tableau
	private int x , y; // coordonnees de la case vide
	private int[][] tableau, tableauInitial;
	private String chemin;
	private ArrayList<Configuration> successeur;
	private String[] deplacements;
	private static final long serialVersionUID = 2700826667261131736L;

	/**
	 * Constructeur de Configuration
	 * @param h Hauteur de la nouvelle configuration
	 * @param l Largeur de la nouvelle configuration
	 */
	public Configuration(int h, int l){
		this.hauteur = h;
		this.largeur = l;
		this.tableau = new int[h][l];
		this.creationTableau();
		this.tableauInitial = new int[h][l];
		copierTableau();
		this.chemin = "";
		this.successeur = new ArrayList<>();
		this.deplacements = new String[]{"haut", "bas", "gauche", "droite"};
	}

	/**
	 * Constructeur de Configuration qui copie une Configuration d&eacute;j&agrave; existante
	 * @param c Configuration &agrave; copier
	 */
	public Configuration(Configuration c){
		this.hauteur = c.hauteur;
		this.largeur = c.largeur;
		this.tableau = new int[this.hauteur][this.largeur];
		this.copier(c);
		this.successeur = new ArrayList<>();
		this.deplacements = new String[]{"haut", "bas", "gauche", "droite"};
	}

	/**
	 * Constructeur de Configuration qui initialise le taquin &agrave; partir d'un tableau de tableaux
	 * @param t tableau &agrave; copier
	 */
	public Configuration(int[][] t) {
		this.hauteur = t.length;
		this.largeur = t[0].length;
		this.tableau = t;
		initialisationXY();
		this.chemin = "";
		this.successeur = new ArrayList<>();
		this.deplacements = new String[]{"haut", "bas", "gauche", "droite"};
	}


	/**
	 * Copie la configuration entr&eacute;e afin que la configuration courante soit identique
	 * @param c la configuration &agrave; copier
	 */
	public void copier(Configuration c){
		for (int i=0; i<this.hauteur; i++) {
			for (int j=0; j<this.largeur; j++) {
				this.tableau[i][j] = c.tableau[i][j];
			}
		}
		this.tableauInitial = c.tableauInitial;
		this.chemin = c.chemin;
		this.x = c.x;
		this.y = c.y;
	}

	/**
	 * Initialise les coordonn&eacute;es x et y de la case vide
	 */
	public void initialisationXY () {
		for (int i=0; i<this.hauteur; i++) {
			for (int j=0; j<this.largeur; j++) {
				if (this.tableau[i][j] == 0) {
					this.x = i;
					this.y = j;
				}
			}
		}
	}

	/**
	 * M&eacute;lange le tableau afin d'obtenir une configuration al&eacute;atoire
	 */
	public void melangerTableau(){
		for (int i=0; i<this.hauteur; i++) {
			for (int j=0; j<this.largeur; j++) {
				// i1 & j1 sont des coordonnees pour l'echange
				int i1 = chiffreAleatoire(0, this.hauteur-1);
				int j1 = chiffreAleatoire(0, this.largeur-1);
				// On echange 2 cases entre elles
				int temp = this.tableau[i][j];
				this.tableau[i][j] = this.tableau[i1][j1];
				this.tableau[i1][j1] = temp;
			}
		}
		initialisationXY();
	}

	/**
	 * M&eacute;lange le tableau afin d'obtenir une configuration al&eacute;atoire et soluble
	 */
	public void creationTableau(){
		int a = 0;
		for(int i=0; i<this.hauteur; i++) {
			for (int j=0; j<this.largeur; j++) {
				this.tableau[i][j] = a; // Des valeurs sont attribuees au tableau pour ensuite le melanger
				a++;
			}
		}
		melangerTableau();
		while (!estSoluble()) { // verifie que le tableau soit soluble
			melangerTableau();
		}
	}

	public void copierTableau(){
		for(int i=0; i<this.tableau.length; i++){
			for (int j=0; j<this.tableau.length; j++){
				this.tableauInitial[i][j] = this.tableau[i][j];
			}
		}
	}

	/**
	 * Renvoie un chiffre al&eacute;atoire dans un intervalle born&eacute;
	 * @param min valeur minimale de l'intervalle
	 * @param max valeur maximale de l'intervalle
	 * @return int al&eacute;atoire dont la valeur est comprise entre min et max
	 */
	public int chiffreAleatoire(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	/**
	 * Modifie la configuration pour la rendre gagante
	 * @return une configuration dont les cases sont rang&eacute;es dans l'ordre croissant
	 */
	public Configuration tableauFinal() {
		Configuration g = new Configuration(this.hauteur, this.largeur);
		int[][] tab = new int[this.hauteur][this.largeur];
		int k = 1;
		for (int i = 0; i < hauteur; i++) {
			for (int j = 0; j < largeur; j++) {
				tab[i][j] = k;
				k++;
			}
		}
		tab[tab.length-1][tab[0].length-1] = 0;
		g.setTableau(tab);
		return g;
	}


	/**
	 * Affiche la taquin de facon claire
	 */
	public void afficher(){
		int size;
		for(int i=0; i<this.largeur; i++){
			System.out.print(" ----");
		}
		System.out.println();
		for(int i=0; i<this.largeur; i++){
			for(int j=0; j<this.hauteur; j++){
				size = String.valueOf(this.tableau[i][j]).length();
				if(size == 1){ // si la taille=1, ajouter un espace
					System.out.print("| " + this.tableau[i][j] + "  ");
				}else{
					System.out.print("| " + this.tableau[i][j] + " ");
				}
			}
			System.out.println("|");
			for(int k=0; k<this.largeur; k++){
				System.out.print(" ----");
			}
			System.out.println();
		}
	}

	/**
	 * V&eacute;rifie si le plateau est en position finale et donc si je le jeu est fini
	 * @return true si la configuration est r&eacute;solue, false sinon
	 */
	public boolean jeuGagne(){
		int k=1;
		for(int i=0; i<this.hauteur; i++){
			for(int j=0; j<this.largeur; j++){
				if(i == (this.hauteur-1) && j == (this.largeur-1) && this.tableau[i][j] == 0){
					return true;
				}else if(this.tableau[i][j] != k ){
					return false;
				}
				k++;
			}
		}
		return false;
	}

	/**
	 * Permet de deplacer la case vide du taquin en entrant la direction si le mouvement est autoris&eacute;
	 * @param dir direction demand&eacute;e, "haut" , "bas", "droite" ou "gauche"
	 * @return true si le d&eacute;placement est possible et appliqu&eacute;, sinon false
	 */
	public boolean mouvement(String dir){
		if((dir.toLowerCase().charAt(0) == 'h' || dir.toLowerCase().equals("haut")) && this.x>0){
			chemin += "H";
			echangeCase(this.x-1, this.y);
			return true;
		} else if((dir.toLowerCase().charAt(0) == 'b' || dir.toLowerCase().equals("bas")) && this.x<this.hauteur-1){
			chemin += "B";
			echangeCase(this.x+1, this.y);
			return true;
		} else if((dir.toLowerCase().charAt(0) == 'd' || dir.toLowerCase().equals("droite")) && this.y<this.largeur-1){
			chemin += "D";
			echangeCase(this.x, this.y+1);
			return true;
		} else if((dir.toLowerCase().charAt(0) == 'g' || dir.toLowerCase().equals("gauche")) && this.y>0){
			chemin += "G";
			echangeCase(this.x, this.y-1);
			return true;
		}
		return false;
	}


	/**
	 * Rempli la liste des successeurs du tableau
	 */
	public void successeurs(){
		for(int i=0; i<deplacements.length; i++){
			Configuration tmp = new Configuration(this);
			if(tmp.mouvement(deplacements[i])){
				successeur.add(tmp);
			}
		}
	}

	/**
	 * &Eacute;change une case en param&egrave;tre avec la case vide
	 * @param caseX coordonn&eacute;e X de la case &agrave; &eacute;changer
	 * @param caseY coordonn&eacute;e Y de la case &agrave; &eacute;changer
	 */
	public void echangeCase(int caseX, int caseY){
		int case0 = this.tableau[this.x][this.y];
		this.tableau[this.x][this.y] = this.tableau[caseX][caseY];
		this.tableau[caseX][caseY] = case0;
		this.x = caseX;
		this.y = caseY;
	}

	/**
	 * Renvoie la configuration sous forme de String
	 * @return String correspondant &agrave; la configuration actuelle
	 */
	public String tableauEnString(){
		String s = "";
		for(int i=0; i<this.hauteur; i++){
			for(int j=0; j<this.largeur; j++){
				s += Integer.toString(this.tableau[i][j]);
			}
		}
		return s;
	}

	/**
	 * Heuristique de manhattan
	 * @return la distance s&eacute;parant la configuration actuelle &agrave; celle r&eacute;solue
	 */
	public int manhattanHeuristique(){
		Configuration fin = this.tableauFinal();
		int val = 0;
		for(int i=0; i<this.hauteur; i++){
			for(int j=0; j<this.largeur; j++){
				int tile = this.tableau[i][j];
				if(tile != 0){
					for(int a=0; a<this.hauteur; a++){
						for(int b=0; b<this.largeur; b++){
							if(fin.tableau[a][b] == tile){
								val += Math.abs(a-i) + Math.abs(b-j);
							}
						}
					}
				}
			}
		}
		return val;
	}

	public String directionMouvement(int caseX, int caseY){
		int differenceCaseVide = Math.abs(caseX-this.x) + Math.abs(caseY-this.y);
		String dir = " ";
		if(differenceCaseVide == 1){
			if(caseX > this.x){
				dir = "bas";
			}else if(caseX < this.x){
				dir = "haut";
			}else if(caseY > this.y){
				dir = "droite";
			}else if(caseY < this.y){
				dir = "gauche";
			}
		}
		return dir;
	}

	/**
	 * Renvoie la configuration sous forme d'un tableau d'int
	 * @return un tableau de int correspondant &agrave; la configuration actuelle
	 */
	public int[] conversion() {
		int[] tab = new int[this.hauteur*this.largeur];
		int k = 0;
		for (int i=0; i<this.hauteur; i++) {
			for (int j=0; j<this.largeur; j++) {
				tab[k] = this.tableau[i][j];
				k++;
			}
		}
		return tab;
	}

	/**
	 * Renvoie la configuration sous forme d'un tableau de tableaux d'int
	 * @return un tableau de tableaux de int correspondant &agrave; la configuration actuelle
	 */
	public int[][] copierTab() {
		int[][] tab = new int[this.hauteur][this.largeur];
		for (int i = 0; i < this.hauteur; i++) {
			for (int j = 0; j < this.largeur; j++) {
				tab[i][j] = this.tableau[i][j];
			}
		}
		return tab;
	}

	/**
	 * Tri le tableau du taquin afin de compter le nombre d'echanges et de deplacements
	 * @return nombre d'&eacute;changes et nombre de d&eacute;placements de la case vide
	 */
	public int[] triEchangeParite() {
		int[] tab = conversion(); // plateau converti en tableau d'entiers
		Configuration copie = new Configuration(copierTab()); // plateau converti en tableau de tableaux d'entiers
		int[] res = new int[2];
		int nbEchanges = 0; // nombre de deplacements total des Cases
		int tmp;

		//calcul du nombre d'echanges
		for (int i = 0; i < tab.length-1; i++) {
			for (int j = i+1; j < tab.length; j++) {
				if (tab[i] == 0) {
					nbEchanges++;
					tmp = tab[j];
					tab[j] = tab[i];
					tab[i] = tmp;
				}
				if (tab[j] == 0) {
					continue;
				}
				else {
					if (tab[i] > tab[j]) {
						nbEchanges++;
						tmp = tab[i];
						tab[i] = tab[j];
						tab[j] = tmp;
					}
				}
			}
		}

		//calcul de la parite du 0
		while (copie.tableau[copie.hauteur-1][copie.largeur-1] != 0) {
			copie.mouvement("droite");
			copie.mouvement("bas");
		}

		res[0] = nbEchanges;
		res[1] = copie.chemin.length();
		return res;
	}

	/**
	 * V&eacute;rifie que la configuration est soluble en fonction de la position de la case vide
	 * @return true si la configuration est soluble, sinon non
	 */
	public boolean estSoluble() {
		return triEchangeParite()[0] % 2 == triEchangeParite()[1] % 2;
	}


	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHauteur() {
		return hauteur;
	}

	public int getLargeur() {
		return largeur;
	}

	public int[][] getTableau() {
		return tableau;
	}

	public void setTableau(int[][] tableau) {
		this.tableau = tableau;
	}

	public String getChemin() {
		return chemin;
	}

	public ArrayList<Configuration> getSuccesseur() {
		return this.successeur;
	}

	public String[] getDeplacements() {
		return deplacements;
	}

	public void setChemin(String chemin) {
		this.chemin = chemin;
	}

	public int[][] getTableauInitial() {
		return tableauInitial;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Configuration that = (Configuration) o;

		return Arrays.deepEquals(tableau, that.tableau);
	}

	@Override
	public int hashCode() {
		return Arrays.deepHashCode(tableau);
	}
}
