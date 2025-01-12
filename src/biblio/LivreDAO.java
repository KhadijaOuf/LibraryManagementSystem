package bibliotheque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LivreDAO {
	
	private Connection connection;
	
	// le constructeur avec le paramètre Connection est conçu pour recevoir une connexion déjà établie. Il permet d'injecter une instance de connexion à la base de données dans l'objet LivreDAO.
	public LivreDAO ( Connection connection ) {
		this.connection = connection;
	}
	
	
	////////////////////// Affichage ////////////////////////////////////////
	
	public ArrayList<Livres> afficherLivres() throws SQLException {
		String query ="SELECT * FROM livres";
		ArrayList<Livres> livres = new ArrayList<Livres>();
		try ( Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)){
			while ( rs.next() ) {
				livres.add(new Livres(rs.getString("titre"), rs.getString("nom_auteur"), rs.getInt("annee_publication")) );
			}
		}
		return livres;
	}
	
	////////////////////// ajouter ////////////////////////////////////////
	
	public void ajouterLivre(String titre, String nom_auteur, int annee) throws SQLException {
		String query = "INSERT INTO livres (titre, nom_auteur, annee_publication) VALUES (? ,? ,?)" ;
		try ( PreparedStatement stmt = connection.prepareStatement(query) ){
			stmt.setString(1, titre);
			stmt.setString(2, nom_auteur);
			stmt.setInt(3, annee);
			stmt.executeUpdate();
		}	
	}
	
	////////////////////// supprimer ////////////////////////////////////////
	
	public void supprimerLivre(String titre,String nom_auteur) throws SQLException {
		String query = "DELETE FROM livres WHERE titre = ? AND nom_auteur=?";
		try ( PreparedStatement stmt = connection.prepareStatement(query) ){
			stmt.setString(1, titre);
			stmt.setString(2, nom_auteur);
			stmt.executeUpdate();
		}		
	}
	
	////////////////////// modifier  ////////////////////////////////////////

	public void modifierLivre(String titre, String nv_titre, String nv_nom_auteur, int nv_annee) throws SQLException {
		String query = "UPDATE livres SET titre=?, nom_auteur=?, annee_publication=? WHERE titre=?" ;
		try ( PreparedStatement stmt = connection.prepareStatement(query) ){
			stmt.setString(1, nv_titre);
			stmt.setString(2, nv_nom_auteur);
			stmt.setInt(3, nv_annee);
			stmt.setString(4, titre);
			stmt.executeUpdate();
		}	
		
	}
	
	////////////////////// rechercher ////////////////////////////////////////

	public ArrayList<Livres> rechercherLivre(String mot) throws SQLException {
		String query ="SELECT * FROM livres WHERE titre LIKE ? OR nom_auteur LIKE ?";
		ArrayList<Livres> livres = new ArrayList<Livres>();
		try ( PreparedStatement stmt = connection.prepareStatement(query) ) {
			stmt.setString(1,"%" + mot + "%");
			stmt.setString(2,"%" + mot + "%");
			try ( ResultSet rs = stmt.executeQuery() ){
				while ( rs.next() ) {
					livres.add( new Livres(rs.getString("titre"), rs.getString("nom_auteur"), rs.getInt("annee_publication")) );
				}
			}
		}
		return livres;
	}	

}
