package bibliotheque;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JScrollPane;


public class BiblioGUI extends JFrame implements ActionListener {
	
	private JLabel libraryLabel, titreLabel, auteurLabel, anneeLabel;
	private JPanel mainPanel, recherchePanel;
	private JButton btnAdd , btnDelete, btnUpdate, btnSearch, btnReload;
	private JTextField titreField, auteurField, anneeField, rechercheField;
	private JTable table;
	private JScrollPane scrollPane;
	private DefaultTableModel model;
	private LivreDAO livreDAO;

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public BiblioGUI() {  //constructor
		
		// connection à la base de donnée 
		String url ="jdbc:mysql://localhost:3306/bibliotheque";
		String username = "root";
		String password ="";

		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			livreDAO = new LivreDAO(connection);
			System.out.println("Connexion réussie !");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données.");
			e.printStackTrace();
		}
		
		
		this.setTitle("Gestionnaire de bibliotheque");
		this.setSize(1000, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null); // Désactive le gestionnaire de mise en page ( absolute layout )
		
		// Bibliotheque :
		libraryLabel = new JLabel("Bibliothèque", SwingConstants.CENTER);
		libraryLabel.setFont(new Font("Verdana", Font.BOLD, 33));
		libraryLabel.setBounds(0, 10, 976, 72);
		getContentPane().add(libraryLabel);
		
		/////////////////////////////////////// Panel d'ajout et suppression  //////////////////////////////////////////////////////////
		
		mainPanel = new JPanel();
		Font titleBorderFont = new Font("Sylfaen", Font.ITALIC, 19); 
		mainPanel.setBorder(new TitledBorder(null, "  Saisie ", TitledBorder.LEADING, TitledBorder.TOP, titleBorderFont, null));
		mainPanel.setBounds(29, 103, 413, 284);
		getContentPane().add(mainPanel);
		mainPanel.setLayout(null);
		
		   // titre :
		titreLabel = new JLabel ("Titre :", SwingConstants.LEFT);
		titreLabel.setBounds(21, 46, 68, 26);
		titreLabel.setFont(new Font("Tahoma", Font.PLAIN, 22));
		mainPanel.add(titreLabel);
		
		titreField = new JTextField();
		titreField.setBounds(107,44, 285, 32);
		titreField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		mainPanel.add(titreField);
		titreField.setColumns(10);
		
		
		   // Auteur :
		auteurLabel = new JLabel ("Auteur :", SwingConstants.LEFT);
		auteurLabel.setBounds(21, 102, 196, 26);
		auteurLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		mainPanel.add(auteurLabel);
		
		auteurField = new JTextField();
		auteurField.setColumns(10);
		auteurField.setBounds(107, 100, 285, 32);
		auteurField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		mainPanel.add(auteurField);
		
		
		  // Annee :
		anneeLabel = new JLabel ("Année :", SwingConstants.LEFT);
		anneeLabel.setBounds(21, 157, 254, 26);
		anneeLabel.setFont( new Font("Tahoma", Font.PLAIN, 20));
		mainPanel.add(anneeLabel);
		
		anneeField = new JTextField();
		anneeField.setColumns(10);
		anneeField.setBounds(107, 155, 285, 32);
		anneeField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		mainPanel.add(anneeField);
		
		  // buttons :
		btnAdd = new JButton("Ajouter");
		btnAdd.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnAdd.setBounds(21, 214, 98, 42);
		btnAdd.addActionListener(this);
		mainPanel.add(btnAdd);
		
		btnDelete = new JButton("Supprimer");
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnDelete.setBackground((Color) null);
		btnDelete.setBounds(141, 214, 123, 42);
		btnDelete.addActionListener(this);
		mainPanel.add(btnDelete);
		
		
		btnUpdate = new JButton("Modifier");
		btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnUpdate.setBackground((Color) null);
		btnUpdate.setBounds(284, 214, 105, 42);
		btnUpdate.addActionListener(this);
		mainPanel.add(btnUpdate);
		
		
		/////////////////////////////////////// Panel de recherche //////////////////////////////////////////////////////////
	
		recherchePanel = new JPanel();
		recherchePanel.setBorder(new TitledBorder(null, "  Recherche ", TitledBorder.LEADING, TitledBorder.TOP, titleBorderFont, null));
		recherchePanel.setBounds(29, 405, 413, 137);
		getContentPane().add(recherchePanel);
		recherchePanel.setLayout(null);
		
		rechercheField = new JTextField();
		rechercheField.setColumns(10);
		rechercheField.setBounds(22, 35, 364, 32);
		rechercheField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		recherchePanel.add(rechercheField);
		
		btnSearch = new JButton("Rechercher");
		btnSearch.addActionListener(this);
		btnSearch.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnSearch.setBackground((Color) null);
		btnSearch.setBounds(53, 77, 135, 40);
		recherchePanel.add(btnSearch);
		
		btnReload = new JButton("Rafraichir");
		btnReload.addActionListener(this);
		btnReload.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnReload.setBackground((Color) null);
		btnReload.setBounds(222, 77, 135, 40);
		recherchePanel.add(btnReload);
		
		
		
		/////////////////////////////////////// Table de la base de donnée  //////////////////////////////////////////////////////////// 
		
		model = new DefaultTableModel(new String[]{"Titre", "Auteur", "Année de publication"}, 0);
		table = new JTable(model);
	
		table.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));
		table.setRowHeight(23);
		table.getTableHeader().setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));
		table.getTableHeader().setPreferredSize(new java.awt.Dimension(table.getTableHeader().getPreferredSize().width, 23));
		
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int selectedRow = table.getSelectedRow();
				if ( selectedRow != -1 ) {
					titreField.setText(model.getValueAt(selectedRow,0).toString());
					auteurField.setText(model.getValueAt(selectedRow,1).toString());
					anneeField.setText(model.getValueAt(selectedRow,2).toString());
				}
			}
		});

		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(455, 115, 508, 427);
		getContentPane().add(scrollPane);
		scrollPane.setViewportView(table);
	
		afficherLivres();  // affichage des livres
		this.setVisible(true);
		
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()== btnAdd) {
			ajouterLivre();
		}
		
		if(e.getSource()== btnDelete) {
			supprimerLivre();
		}
		
		if(e.getSource()== btnUpdate ) {
			modifierLivre();
		}
		
		if(e.getSource()== btnSearch) {
			rechercherLivre();
		}
		
		if(e.getSource()== btnReload) {
			afficherLivres();
		}
	}
	

	////////////////////// affichage ////////////////////////////////////////
	
	private void afficherLivres() {
		try {
			model.setRowCount(0);  
			for ( Livres livre : livreDAO.afficherLivres() ) {
				model.addRow( new Object[] {livre.getTitre(), livre.getAuteur(), livre.getAnnee()} );
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Erreur lors de l'affichage des livres.");
		}
		
	}
	
	
	////////////////////// ajouter ////////////////////////////////////////
	
	private void ajouterLivre() {
		String titreTxt = titreField.getText();
		String auteurTxt = auteurField.getText();
		String anneeTxt = anneeField.getText();
		if ( titreTxt.isEmpty() || auteurTxt.isEmpty() || anneeTxt.isEmpty() ) {
			JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
			return;
		}
		int annee = Integer.parseInt(anneeTxt);
		try {
			livreDAO.ajouterLivre(titreTxt,auteurTxt,annee);
			afficherLivres();
			JOptionPane.showMessageDialog(this, "Livre ajouté avec succès !");
			titreField.setText("");
			auteurField.setText("");
			anneeField.setText("");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du livre.");
		}
		
	}
	
	////////////////////// supprimer ////////////////////////////////////////
	
	private void supprimerLivre() {
		String titreTxt = titreField.getText();
		String auteurTxt = auteurField.getText();
		if ( titreTxt.isEmpty() || auteurTxt.isEmpty() ) {
			JOptionPane.showMessageDialog(this, "Veuillez sélectionner un livre à supprimer.");
			return;
		}
		try {
			livreDAO.supprimerLivre(titreTxt,auteurTxt);
			afficherLivres();
			JOptionPane.showMessageDialog(this, "Livre supprimé avec succès !");
			titreField.setText("");
			auteurField.setText("");
			anneeField.setText("");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Erreur lors de la supression du livre.");
		}
		
	}
	
	////////////////////// modifier  ////////////////////////////////////////

	private void modifierLivre() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un livre à modifier.");
            return;
        }
		String titreAncien = model.getValueAt(selectedRow,0).toString(); // récupérer le titre de la ligne sélectionnée à modifier
		String titreTxt = titreField.getText();
		String auteurTxt = auteurField.getText();
		String anneeTxt = anneeField.getText();
		if ( titreTxt.isEmpty() || auteurTxt.isEmpty() || anneeTxt.isEmpty() ) {
			JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
			return;
		}
		int annee = Integer.parseInt(anneeTxt);
		try {
			livreDAO.modifierLivre(titreAncien, titreTxt, auteurTxt, annee);
			afficherLivres();
			JOptionPane.showMessageDialog(this, "Livre modifié avec succès !");
			titreField.setText("");
			auteurField.setText("");
			anneeField.setText("");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Erreur lors de la modification du livre.");
		}
		
	}
	
	////////////////////// rechercher ////////////////////////////////////////

	private void rechercherLivre() {
		String rechercheTxt = rechercheField.getText();
		if ( rechercheTxt.isEmpty() ) {
			JOptionPane.showMessageDialog(this, "Veuillez entrer un titre ou un auteur pour la recherche.");
			return;
		}
		try {
			model.setRowCount(0);
			for ( Livres livre : livreDAO.rechercherLivre(rechercheTxt) ) {
				model.addRow( new Object[] {livre.getTitre(), livre.getAuteur(), livre.getAnnee()} );
			}
			rechercheField.setText("");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Erreur lors de la recherche du livre.");
			afficherLivres();
		}
		
	}

	
	
	public static void main(String[] args) {
		BiblioGUI biblioFenetre = new BiblioGUI();

	}
}
