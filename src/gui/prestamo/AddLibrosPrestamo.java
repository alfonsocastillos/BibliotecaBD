package gui.prestamo;

import dataBase.dao.LibroDAO;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import tools.UtilsTable;

/**
 *
 * @author Carlos
 * Ventana que permite agregar paliculas a la renta
 */
public class AddLibrosPrestamo extends javax.swing.JDialog {
       int id = 0;
       LibroDAO daoLibros;
       Object librosLista [][];
       public Object libros[];

    /**
     * Creates new form AddPeliculasRentaD
     * @param parent
     * @param modal
     */
    public AddLibrosPrestamo(java.awt.Frame parent, boolean modal) {
        // ventana modal
        super(parent, modal);
        // Yiyulo de la ventana
        setTitle("Agregar libros al prestamo");
        // inicia los componentes
        initComponents();
        // Crea el daopara consltar las ventanas
        daoLibros = new LibroDAO();
        // llena la tabla
        llenaTabla();
    }
    
    private void llenaTabla(){        
        // Consulta las peliculas y lo guardaen un arreglo
        // consulta los datos de las peliculas
       librosLista = daoLibros.GetLibrosByDescripcion(txtFiltro.getText().trim());
        // Titulos de la tabla
        String[] T_FILMS = {"","Título"};
        // alineación de las celdas
        int[][] cellAlignment = {{0,javax.swing.SwingConstants.LEFT}};
        // Tamaño de las celdas
        int[][] cellSize = {{0,0},
                            {1,305}};
        /*
            Metodo que llena las tablas, recibe la tabla, los datos, los titulos,
            la alineación y el tamaño de las celdas
        */
        UtilsTable.llenaTabla(tableList, librosLista, T_FILMS, cellAlignment, cellSize);
    }
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        btnAceptar = new javax.swing.JButton();
        pnlTableList = new javax.swing.JPanel();
        scpTableList = new javax.swing.JScrollPane();
        tableList = new javax.swing.JTable();
        lblTitulo1 = new javax.swing.JLabel();
        txtFiltro = new javax.swing.JTextField();
        btnBorrarFiltro = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage( getClass().getResource("/img/libros.png")));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btnAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/ok.png"))); // NOI18N
        btnAceptar.setFocusable(false);
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        pnlTableList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Libros", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N

        scpTableList.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scpTableList.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tableList.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tableList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableListMouseClicked(evt);
            }
        });
        scpTableList.setViewportView(tableList);

        lblTitulo1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblTitulo1.setText("Filtrar:");

        txtFiltro.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroKeyReleased(evt);
            }
        });

        btnBorrarFiltro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/cancelarm.png"))); // NOI18N
        btnBorrarFiltro.setToolTipText("Borrar filtro");
        btnBorrarFiltro.setFocusable(false);
        btnBorrarFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarFiltroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlTableListLayout = new javax.swing.GroupLayout(pnlTableList);
        pnlTableList.setLayout(pnlTableListLayout);
        pnlTableListLayout.setHorizontalGroup(
            pnlTableListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableListLayout.createSequentialGroup()
                .addGroup(pnlTableListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTableListLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(scpTableList, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlTableListLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblTitulo1)
                        .addGap(6, 6, 6)
                        .addComponent(txtFiltro)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBorrarFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlTableListLayout.setVerticalGroup(
            pnlTableListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableListLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(pnlTableListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnBorrarFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(txtFiltro)
                    .addComponent(lblTitulo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(11, 11, 11)
                .addComponent(scpTableList, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlTableList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(169, 169, 169)
                        .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(pnlTableList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        // Accion del boton aceptar
        // Verifica si se selecciono un elemento de la tabla
        //System.out.print("ffff");
        if (tableList.getSelectedRow() < 0){
            // Suena un beep
            Toolkit.getDefaultToolkit().beep();
            // Muestra un mensage de aviso
            JOptionPane.showMessageDialog(this, "Seleccione un libro.", "Aviso",2);            
        }
        else{      
            /*
                si selecciono un elemento, guarda los datos de la pelicula 
                en un arreglo que retornará los datos a la pantalla anterior
            */
            libros = new Object [3];    
            // Guarda id, nombre y costo de la pelicula
            libros [0] = librosLista[tableList.getSelectedRow()][0];
            libros [1] = librosLista[tableList.getSelectedRow()][1];
            libros [2] = librosLista[tableList.getSelectedRow()][6];
            
            // Cierra la ventana
            dispose();   
        }
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // Cuando se activa llena la tabla
        // reinicia el filtro
        txtFiltro.setText("");
        llenaTabla();
    }//GEN-LAST:event_formWindowActivated

    private void tableListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListMouseClicked
        /*
        Cuando el usuario da doble click en una fila de la tabla hace la
        misma acción del boton aceptar
        */
        if (evt.getClickCount() == 2){  
            btnAceptarActionPerformed (null);
        }
    }//GEN-LAST:event_tableListMouseClicked

    private void txtFiltroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyReleased
        // Filtra
        llenaTabla();
    }//GEN-LAST:event_txtFiltroKeyReleased

    private void btnBorrarFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarFiltroActionPerformed
        // Borra el filtro de busqyeda
        txtFiltro.setText("");
        llenaTabla();
    }//GEN-LAST:event_btnBorrarFiltroActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // reinicia el filtro
        txtFiltro.setText("");
        llenaTabla();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnBorrarFiltro;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel lblTitulo1;
    private javax.swing.JPanel pnlTableList;
    private javax.swing.JScrollPane scpTableList;
    public javax.swing.JTable tableList;
    private javax.swing.JTextField txtFiltro;
    // End of variables declaration//GEN-END:variables
}