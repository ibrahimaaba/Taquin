import java.util.HashMap;

public class Aetoile {
	private Configuration depart;
	private HashMap<Configuration, Integer> ferme;
	private FdPg<Configuration> ouvert;

	/**
	 * Constructeur de la classe A*
	 * @param d Configuration qu'on veut r&eacute;soudre
	 */
	public Aetoile (Configuration d) {
		this.depart = d;
		this.ferme = new HashMap<>();
		this.ouvert = new FdPg<>();
	}

	/**
	 * Fonction impl&eacute;mentant l'algorithme A*
	 * @return configuration finale
	 */
	public Configuration aetoile(){
		try {
			ouvert.Ajouter(this.depart, 0);
			while (!ouvert.EstVide()) {
				Configuration u = ouvert.ExtraireMin();
				ferme.put(u, u.getChemin().length());
				if (u.jeuGagne()) {
					return u;
				}
				u.successeurs();
				for (Configuration v : u.getSuccesseur()) {
					int i = ouvert.IndiceDsF(v);
					if (i != -1) {
						Configuration w = ouvert.GetSommet(i);
						if (w.getChemin().length() > v.getChemin().length()) {
							w.setChemin(v.getChemin());
							ouvert.MaJ(v, v.getChemin().length());
						}
					} else {
						if (ferme.containsKey(v)) {
							int d = ferme.get(v);
							if (d > v.getChemin().length()) {
								ferme.remove(v);
								ouvert.Ajouter(v, v.getChemin().length() + v.manhattanHeuristique());
							}
						} else {
							ouvert.Ajouter(v, v.getChemin().length() + v.manhattanHeuristique());
						}
					}
				}
			}
		}catch (OutOfMemoryError oome){
			this.ouvert.hmap.clear();
			this.ferme.clear();
			System.out.println("Aucune solution n'a pu être trouvée");
		}
		return null;
	}
}



