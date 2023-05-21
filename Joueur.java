import java.io.*;
import java.util.ArrayList;

public class Joueur implements Serializable{
    private String nom;
    private boolean robot;
    private ArrayList<Configuration> jeux;
    private static final long serialVersionUID = 5030499181306931978L;

    public Joueur() {
        this.nom = "Anonyme"; // Pseudo par défaut
        this.robot = false;
        this.jeux = new ArrayList<>();
    }

    public void sauvegarder(){
        try {
            File f = new File("joueur.dat");
            f.delete();// permet de mettre a jour les informations
            FileOutputStream fichier = new FileOutputStream("joueur.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fichier);
            oos.writeObject(this);
            oos.flush();
            oos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    public void lire(){
        try{
            File f = new File("joueur.dat");
            if(f.createNewFile()){
                this.sauvegarder();
            }
            FileInputStream fichier = new FileInputStream("joueur.dat");
            ObjectInputStream ois = new ObjectInputStream(fichier);
            Joueur j = (Joueur) ois.readObject();
            this.nom = j.nom;
            this.jeux = j.jeux;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public boolean getRobot() {
        return robot;
    }

    public void setRobot(boolean robot) {
        this.robot = robot;
    }

    public ArrayList<Configuration> getJeux() {
        return jeux;
    }

    public void setJeux(ArrayList<Configuration> jeux) {
        this.jeux = jeux;
    }
}