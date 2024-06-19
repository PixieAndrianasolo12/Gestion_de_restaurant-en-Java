/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package restaurant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class Plats extends javax.swing.JFrame {

    private final String URL = "jdbc:mysql://localhost:3306/mydb";
    private final String USERNAME = "root";
    private final String PASSWORD = "";
    
    public Plats() {
        initComponents();
        remplirComboBoxCategories();
        afficherProduits();
    }
    
    /*** Méthode privée pour obtenir une connexion à la base de données ***/
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    /*** Méthode pour remplir le JComboBox avec les catégories de la base de données ***/
    private void remplirComboBoxCategories() {
        // Supprime tous les éléments existants dans le JComboBox
        jComboBox1.removeAllItems();
        
        try (Connection connection = getConnection(); // Ouvre la connexion à la base de données
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM category"); // Prépare la requête SQL
             ResultSet resultSet = preparedStatement.executeQuery()) { // Exécute la requête et récupère le résultat

            // Parcours des résultats
            while (resultSet.next()) {
                // Récupère le nom de la catégorie depuis le ResultSet
                String categorie = resultSet.getString("name");
                // Ajoute le nom de la catégorie au JComboBox
                jComboBox1.addItem(categorie);
            }
        } catch (SQLException ex) { // Gestion des erreurs SQL
            // Affiche un message d'erreur en cas d'échec de la récupération des catégories
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des catégories: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


       /*** Méthode pour afficher les produits dans un JTable ***/
    private void afficherProduits() {
        try (Connection connection = getConnection(); // Ouvre la connexion à la base de données
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT products.id, products.name, products.price, category.name AS categorie FROM products JOIN category ON products.category_id = category.id"); // Prépare la requête SQL avec une jointure
             ResultSet resultSet = preparedStatement.executeQuery()) { // Exécute la requête et récupère le résultat

            // Récupère le modèle de données du JTable
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            // Efface toutes les lignes existantes du JTable
            model.setRowCount(0);

            // Parcours des résultats
            while (resultSet.next()) {
                // Récupère les données de chaque colonne
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("name");
                double prix = resultSet.getDouble("price");
                String categorie = resultSet.getString("categorie"); // Utilise le nom correct de la colonne renommée dans la requête SQL

                // Crée un tableau d'objets contenant les données de la ligne
                Object[] row = {id, nom, prix, categorie};
                // Ajoute la ligne au modèle de données du JTable
                model.addRow(row);
            }
        } catch (SQLException ex) { // Gestion des erreurs SQL
            // Affiche un message d'erreur en cas d'échec de l'affichage des produits
            JOptionPane.showMessageDialog(this, "Erreur lors de l'affichage des produits: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }




    /*** Code ajoute Produit ***/
    private void ajouterProduit() {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO products (name, price, category_id) VALUES (?, ?, ?)")) {

            String nom = jTextField1.getText();
            String prixText = jTextField2.getText();
            // Vérification si le texte est un nombre valide
            if (!prixText.matches("\\d+(\\.\\d+)?")) {
                JOptionPane.showMessageDialog(this, "Le prix doit être un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return; // Arrête l'exécution de la méthode si le prix n'est pas valide
            }
            double prix = Double.parseDouble(prixText);
            String categorie = jComboBox1.getSelectedItem().toString();

            // Récupérer l'ID de la catégorie correspondant au nom de la catégorie sélectionné
            int categoryId = getCategoryIdByName(categorie);
            if (categoryId == -1) {
                JOptionPane.showMessageDialog(this, "La catégorie sélectionnée n'existe pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return; // Arrête l'exécution de la méthode si la catégorie n'existe pas
            }

            preparedStatement.setString(1, nom);
            preparedStatement.setDouble(2, prix);
            preparedStatement.setInt(3, categoryId); // Utiliser l'ID de la catégorie au lieu du nom

            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Plat ajouté avec succès.");
            afficherProduits();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du plat: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthode pour récupérer l'ID de la catégorie par son nom
    private int getCategoryIdByName(String categoryName) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM category WHERE name = ?")) {

            preparedStatement.setString(1, categoryName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                return -1; // Retourne -1 si la catégorie n'est pas trouvée
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération de l'ID de la catégorie: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }
    
    /*** Code Suppression de Porduit ***/
    private void supprimerProduit(int produitId) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM products WHERE id = ?")) {

            preparedStatement.setInt(1, produitId);
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Plat supprimé avec succès.");
            // Mettez à jour l'affichage des produits après la suppression
            afficherProduits();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du plat: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*** Code Modification des Produits ***/
    private void modifierProduit(int produitId, String nouveauNom, double nouveauPrix, String nouvelleCategorie) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE products SET name = ?, price = ?, category_id = ? WHERE id = ?")) {

            // Récupérer l'ID de la nouvelle catégorie
            int nouvelleCategorieId = getCategoryIdByName(nouvelleCategorie);
            if (nouvelleCategorieId == -1) {
                JOptionPane.showMessageDialog(this, "La catégorie sélectionnée n'existe pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return; // Arrête l'exécution de la méthode si la catégorie n'existe pas
            }

            preparedStatement.setString(1, nouveauNom);
            preparedStatement.setDouble(2, nouveauPrix);
            preparedStatement.setInt(3, nouvelleCategorieId);
            preparedStatement.setInt(4, produitId);

            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Plats modifié avec succès.");
            // Mettez à jour l'affichage des produits après la modification
            afficherProduits();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification du produit: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }




    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tw Cen MT Condensed", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Plats");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 100, -1));

        jTable1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nom", "Prix", "Catégories"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 60, 460, 370));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Nom ");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 80, 81, 23));
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 110, 250, 34));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Catégories");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 250, 113, 23));
        getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 200, 250, 34));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Prix ( en Ar)");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 170, 150, 23));

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Séléctionner", " ", " " }));
        getContentPane().add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 280, 250, 40));

        btnAdd.setBackground(new java.awt.Color(255, 153, 51));
        btnAdd.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/restaurant/icon/ajouter-30.png"))); // NOI18N
        btnAdd.setText("Ajouter");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        getContentPane().add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(88, 446, -1, 38));

        btnEdit.setBackground(new java.awt.Color(255, 153, 51));
        btnEdit.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/restaurant/icon/modifier-30.png"))); // NOI18N
        btnEdit.setText("Modifier");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        getContentPane().add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 446, -1, 38));

        btnDelete.setBackground(new java.awt.Color(255, 153, 51));
        btnDelete.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/restaurant/icon/supprimer-30.png"))); // NOI18N
        btnDelete.setText("Supprimer");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        getContentPane().add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 450, -1, 38));

        btnClose.setBackground(new java.awt.Color(255, 153, 51));
        btnClose.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnClose.setText("Retour");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        getContentPane().add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 450, -1, 38));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/restaurant/photo/istockphoto-1191675284-1024x1024.jpg"))); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 530));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
         ajouterProduit();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
       int selectedRowIndex = jTable1.getSelectedRow();
    if (selectedRowIndex == -1) {
        JOptionPane.showMessageDialog(this, "Veuillez sélectionner un produit à modifier.", "Avertissement", JOptionPane.WARNING_MESSAGE);
        return; // Arrête l'exécution de la méthode si aucun produit n'est sélectionné
    }

    // Récupérer l'ID du produit sélectionné
    int id = (int) jTable1.getValueAt(selectedRowIndex, 0); // Supposons que l'ID est dans la première colonne

    // Vérifier que les champs de texte ne sont pas vides
    String nom = jTextField1.getText();
    if (nom.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Le champ nom ne doit pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String prixText = jTextField2.getText();
    if (prixText.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Le champ prix ne doit pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Vérifier que le texte du prix est un nombre valide
    if (!prixText.matches("\\d+(\\.\\d+)?")) {
        JOptionPane.showMessageDialog(this, "Le prix doit être un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    double prix = Double.parseDouble(prixText);

    String categorie = jComboBox1.getSelectedItem().toString();
    if (categorie.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Veuillez sélectionner une catégorie.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Appeler la méthode pour modifier le produit
    modifierProduit(id, nom, prix, categorie);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
         int selectedRowIndex = jTable1.getSelectedRow();
    if (selectedRowIndex == -1) {
        JOptionPane.showMessageDialog(this, "Veuillez sélectionner un produit à supprimer.", "Avertissement", JOptionPane.WARNING_MESSAGE);
        return;}
    
    int id = (int) jTable1.getValueAt(selectedRowIndex, 0);
    supprimerProduit(id);
    
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
         new Dashboard_admin().setVisible(true);
            this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Plats().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables

    
}
