
package restaurant;

import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class CategoryPage extends javax.swing.JFrame {
    
    /*** Définir les informations de connexion à votre base de données MySQL ***/
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/mydb";
    static final String USER = "root";
    static final String PASS = "";
    
    public CategoryPage() {
        initComponents();
        displayData();
    }
    
     
    /****  Méthode pour afficher les données dans la table ****/
            private void displayData() {
                     DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                     model.setRowCount(0);
                     try {
                         // Enregistrer le pilote JDBC
                         Class.forName(JDBC_DRIVER);

                         // Ouvrir une connexion
                         Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

                         // Exécuter une requête SQL
                         Statement stmt = conn.createStatement();
                         String sql = "SELECT * FROM category";
                         ResultSet rs = stmt.executeQuery(sql);

                         // Remplir le modèle de table avec les données de la base de données
                         while (rs.next()) {
                             int id = rs.getInt("id");
                             String name = rs.getString("name");
                             model.addRow(new Object[]{id, name});
                         }
                         rs.close();
                         stmt.close();
                         conn.close();
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
             }

    /**** Méthode pour insérer des données ****/
            private void addData(String nom) {
                try {
                    Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    String sql = "INSERT INTO category (name) VALUES (?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, nom);
                    pstmt.executeUpdate();
                    pstmt.close();
                    conn.close();
                    displayData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

    /**** Méthode pour modifier des données ***/
            private void editData(int id, String name) {
                try {
                    Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    String sql = "UPDATE category SET name=? WHERE id=?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, name);
                    pstmt.setInt(2, id);
                    pstmt.executeUpdate();
                    pstmt.close();
                    conn.close();
                    displayData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

    /**** Méthode pour supprimer des données ****/
            private void deleteData(int id) {
                try {
                    Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    String sql = "DELETE FROM category WHERE id=?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, id);
                    pstmt.executeUpdate();
                    pstmt.close();
                    conn.close();
                    displayData();
                } catch (SQLException e) {
                    e.printStackTrace();
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
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Nirmala UI", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Catégories");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, -1, 70));

        jTable1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null}
            },
            new String [] {
                "ID", "Nom"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 320, 290));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Nom");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 160, 84, -1));
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 190, 310, 42));

        btnAdd.setBackground(new java.awt.Color(204, 102, 0));
        btnAdd.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/restaurant/icon/ajouter-30.png"))); // NOI18N
        btnAdd.setText("Ajouter");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        getContentPane().add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 270, 140, 39));

        btnEdit.setBackground(new java.awt.Color(204, 102, 0));
        btnEdit.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/restaurant/icon/modifier-30.png"))); // NOI18N
        btnEdit.setText("Modifier");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        getContentPane().add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 340, 150, 39));

        btnDelete.setBackground(new java.awt.Color(204, 102, 0));
        btnDelete.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/restaurant/icon/supprimer-30.png"))); // NOI18N
        btnDelete.setText("Supprimer");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        getContentPane().add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 270, 170, 39));

        btnClose.setBackground(new java.awt.Color(204, 102, 0));
        btnClose.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnClose.setText("Retour");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        getContentPane().add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(677, 340, 100, 39));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/restaurant/photo/category.jpg"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 830, 520));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /****   Action lors de l'ajout d'une catégorie ***/
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        String valeur = jTextField1.getText();
        addData(valeur);
    }//GEN-LAST:event_btnAddActionPerformed

    /*** ction lors de la modification d'une catégorie ***/
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        int row = jTable1.getSelectedRow();
        if (row == -1) {
            return;
        }
        int id = (int) jTable1.getValueAt(row, 0);
        String valeur = jTextField1.getText();
        editData(id, valeur);
    }//GEN-LAST:event_btnEditActionPerformed

    /*** Action lors de la suppression d'une catégorie ***/
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
         int row = jTable1.getSelectedRow();
        if (row == -1) {
            return;
        }
        int id = (int) jTable1.getValueAt(row, 0);
        deleteData(id);
    }//GEN-LAST:event_btnDeleteActionPerformed

    /*** Action lors de la fermeture de la page des catégories ***/
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
                new CategoryPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

}
