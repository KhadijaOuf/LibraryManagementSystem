package bibliotheque;

public class Livres {
	String titre;
	String nom_auteur;
	int annee;
	
	public Livres (String titre, String nom_auteur, int annee) {
		this.titre = titre;
		this.nom_auteur = nom_auteur;
		this.annee = annee;
	}
	
	// setters :
	public void setTitre( String titre ) {
		this.titre = titre;
	}
	public void setAuteur( String nom_auteur ) {
		this.nom_auteur = nom_auteur;
	}
	public void setAnnee( int annee ) {
		this.annee = annee;
	}
	
	// getters :
	public String getTitre() {
		return titre;
	}
	public String getAuteur() {
		return nom_auteur;
	}
	public int getAnnee() {
		return annee;
	}
	
}
