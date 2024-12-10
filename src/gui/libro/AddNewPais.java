package gui.libro;

import dataBase.dao.PaisDAO;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author Carlos
 * Ventana que permite crear, editar y eliminar idiomas del catálogo
 */
public class AddNewPais extends javax.swing.JDialog {
    // Para guardar el idioma
    int pais_id;
    PaisDAO pais_dao;

    /**
     * Creates new form 
     * @param parent
     * @param modal
     */
    public AddNewPais(java.awt.Frame parent, boolean modal) {
        // ventana modal
        super(parent, modal);
        // inicia los componentes
        initComponents();
        processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        // Crea el dao para acceder a la tabla PAIS
        pais_dao = new PaisDAO();
        getRootPane().setDefaultButton(btnGuardar);                      
    }
    
    private void BorrarTextos() {
        // Borra el texto
        txtPais.setText("");
    }
    
    public void SetEditId(int pais_id) {
        // Asigna el id del pais a modificar
        this.pais_id = pais_id;
        // Busca el pais
        Object[] pais_edit = pais_dao.getPaisById(pais_id);
        // Muestra los datos en los controles
        txtPais.setText(pais_edit[1].toString());        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTableList = new javax.swing.JPanel();
        txtPais = new javax.swing.JTextField();
        lblNombre = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pais");
        setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage( getClass().getResource("/img/libros.png")));

        pnlTableList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtPais.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlTableList.add(txtPais, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, 140, 25));

        lblNombre.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblNombre.setText("Pais");
        pnlTableList.add(lblNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, 25));

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/guardar.png"))); // NOI18N
        btnGuardar.setToolTipText("Guardar");
        btnGuardar.setFocusable(false);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        pnlTableList.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 40, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlTableList, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlTableList, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // Accion del boton Guardar
        if(txtPais.getText().trim().length() == 0) {
            // Suena un beep
            Toolkit.getDefaultToolkit().beep();
            // Muestra un mensage de aviso
            JOptionPane.showMessageDialog(this, "Escriba el idioma", "Aviso", 2);
        }
        else{                      
            String pais = txtPais.getText().trim();
            if(pais_id == 0) { // Guarda un nuevo idioma                
                pais_id = pais_dao.savePais(pais);                
            }
            else{ // Actualiza idioma                
                Object[] pais_obj = new Object[2];
                pais_obj[0] = pais_id;              // Id del pais
                pais_obj[1] = pais;                 // pais
                pais_id = pais_dao.updatePais(pais_obj);                
            }
            
            if(pais_id == 0) {
                // Suena un beep
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(this, "Error al guardar el pais", "Error", 0);                
            }
            else{
                BorrarTextos();
                dispose();
            }            
        }  
    }//GEN-LAST:event_btnGuardarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JPanel pnlTableList;
    private javax.swing.JTextField txtPais;
    // End of variables declaration//GEN-END:variables
}