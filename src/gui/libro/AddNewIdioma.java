package gui.libro;

import dataBase.dao.IdiomaDAO;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author Carlos
 * Ventana que permite crear, editar y eliminar idiomas del catálogo
 */
public class AddNewIdioma extends javax.swing.JDialog {
    // Para guardar el idioma
    int idioma_id;
    IdiomaDAO idioma_dao;

    /**
     * Creates new form 
     * @param parent
     * @param modal
     */
    public AddNewIdioma(java.awt.Frame parent, boolean modal) {
        // ventana modal
        super(parent, modal);
        // inicia los componentes
        initComponents();
        processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        // Crea el dao para acceder a la tabla IDIOMA
        idioma_dao = new IdiomaDAO();
        getRootPane().setDefaultButton(btnGuardar);                      
    }
    
    private void BorrarTextos(){
        // Borra el texto
        txtIdioma.setText("");
    }
    
    public void SetEditId(int idioma_id){
        // Asigna el id del actor a modificar
        this.idioma_id = idioma_id;
        // Busca el actor
        Object[] idioma_edit = idioma_dao.GetIdiomaById(idioma_id);
        // Muestra los datos en los controles
        txtIdioma.setText(idioma_edit[1].toString());        
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
        txtIdioma = new javax.swing.JTextField();
        lblNombre = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Idioma");
        setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage( getClass().getResource("/img/pen.png")));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        pnlTableList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtIdioma.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlTableList.add(txtIdioma, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, 140, 25));

        lblNombre.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblNombre.setText("Idioma:");
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
    
    // Descartar cambios al cerrar la ventana
    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        
    }//GEN-LAST:event_formWindowClosed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // Accion del boton Guardar
        if (txtIdioma.getText().trim().length() == 0){
            // Suena un beep
            Toolkit.getDefaultToolkit().beep();
            // Muestra un mensage de aviso
            JOptionPane.showMessageDialog(this, "Escriba el idioma", "Aviso", 2);
        }
        else{                      
            String idioma = txtIdioma.getText().trim();
            if (idioma_id == 0){ // Guarda un nuevo idioma                
                idioma_id = idioma_dao.SaveIdioma(idioma);                
            }
            else{ // Actualiza idioma                
                Object[] idioma_obj = new Object[2];
                idioma_obj[0] = idioma_id;              // Id del idioma
                idioma_obj[1] = idioma;                 // Idioma
                idioma_id = idioma_dao.UpdateIdioma(idioma_obj);                
            }
            
            if (idioma_id == 0){
                // Suena un beep
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(this, "Error al guardar el idioma", "Error", 0);                
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
    private javax.swing.JTextField txtIdioma;
    // End of variables declaration//GEN-END:variables
}