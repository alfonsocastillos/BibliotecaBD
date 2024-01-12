package gui;

// import gui.renta.Renta;
import gui.libro.Libros;
import gui.prestamo.Prestamo;
import gui.clientes.Clientes;
import gui.reportes.Reportes;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import tools.UtilsGUI;

/**
 *
 * @author Carlos Cortés Bazán
 * Ventana principal 
 */
public class MDIPrincipal extends javax.swing.JFrame {
    // Para guardar los ids y ponerlos en los registros
    String id_empleado;
    String id_sucursal;
    // Para guardar los nombres de la tienda y empleado para mostralos
    // en la interfaz
    String sucursal;
    String empleado_nombre;
    // Declara las ventanas
    Libros vtn_libros;
    // Ventana Clientes
    Clientes vtn_clientes;
    Prestamo vtn_prestamo;

    /**
     * Creates new form MDIPrincipal
     * @param id_sucursal
     * @param id_empleado
     * @param sucursal
     * @param empleado_nombre
     */
    public MDIPrincipal(String id_sucursal, String id_empleado, String sucursal, String empleado_nombre) { 
        // Obtiene los valores que consultó la ventana de inicio de sesión 
        this.id_sucursal = id_sucursal;
        this.id_empleado = id_empleado;
        // Nombre de la sucursal
        this.sucursal = sucursal;
        // Nombre del empleado
        this.empleado_nombre = empleado_nombre;
        
        // inicia todos los componentes
        initComponents();        
        // Instancia y agrega las ventanas
        configComponents();      
        
        // Muestra en nombre de la tienda y de empleado en la parte inferior
        lblSucursal. setText(" Sucursal: " + sucursal);
        lblEmpleado. setText(" Empleado: " + empleado_nombre);
    }
    
    private void configComponents(){
        // Carga el aspecto grafico
        UtilsGUI.setLookAndFeel(this);
        // Localización de la ventana en la pantalla 
        setLocationRelativeTo(this.getParent());    
        // Ventana maximizada
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        // Instancia las ventanas
        vtn_libros = new Libros();
        vtn_clientes = new Clientes();
        vtn_prestamo = new Prestamo(id_empleado,id_sucursal, empleado_nombre);
        //vtn_prestamo = new Prestamo();
        // Agrega las ventanas al panel
        desktopPane.add(vtn_libros);
        desktopPane.add(vtn_clientes);
        desktopPane.add(vtn_prestamo);
        pack(); // Ajusta el tamaño de la ventana al preferido por sus componentes internos
    }
      
    private void setLocation (JInternalFrame frame){
        // Metodo que posiciona las ventanas flotantes
        Dimension desktopSize = desktopPane.getSize();
        Dimension frameSize = frame.getSize();
        frame.setLocation((desktopSize.width - frameSize.width)/2, (desktopSize.height- frameSize.height)/2);
    }
       
    private void abrePrestamo (){  
        
        if (!vtn_prestamo.isVisible()){
            // Posiciona la ventana
            setLocation(vtn_prestamo);
            //Abre la ventana renta
            vtn_prestamo.setVisible(true);
        }// Si ya está abierta, la pone adelante
        else 
            vtn_prestamo.moveToFront();
    }
    
    private void AbreLibros(){
        if (vtn_libros == null) {
            vtn_libros = new Libros();
        }
        
        if (!vtn_libros.isVisible()){
            // Posiciona la ventana
            setLocation(vtn_libros);
            //Abre la ventana pelicula
            vtn_libros.setVisible(true);
        }// Si ya está abierta, la pone adelante
        else
            vtn_libros.moveToFront();
    }
    
    
    private void AbreClientes(){
                if (!vtn_clientes.isVisible()){
            // Posiciona la ventana
            setLocation(vtn_clientes);
            //Abre la ventana renta
            vtn_clientes.setVisible(true);
        }// Si ya está abierta, la pone adelante
        else 
            vtn_clientes.moveToFront();
    }
    
    private void AbreReportes(){
                if (!vtn_reportes.isVisible()){
            // Posiciona la ventana
            setLocation(vtn_reportes);
            //Abre la ventana renta
            vtn_reportes.setVisible(true);
        }// Si ya está abierta, la pone adelante
        else 
            vtn_reportes.moveToFront();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktopPane = new javax.swing.JDesktopPane();
        jLabel1 = new javax.swing.JLabel();
        barraDeAcceso = new javax.swing.JToolBar();
        btnRenta = new javax.swing.JButton();
        btnLibros = new javax.swing.JButton();
        btnClientes = new javax.swing.JButton();
        btnReportes = new javax.swing.JButton();
        barVent = new javax.swing.JToolBar();
        sptDown = new javax.swing.JSplitPane();
        lblSucursal = new javax.swing.JLabel();
        lblEmpleado = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        menuRenta = new javax.swing.JMenuItem();
        menuExit = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        menuPelicula = new javax.swing.JMenuItem();
        menuActor = new javax.swing.JMenuItem();
        menuCliente = new javax.swing.JMenuItem();
        menuEmpleado = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        menuAyuda = new javax.swing.JMenuItem();
        menuAcercaDe = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Renta de películas");
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage( getClass().getResource("/img/icono.png")));
        setResizable(false);

        desktopPane.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/biblioteca_fondo.png"))); // NOI18N

        desktopPane.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout desktopPaneLayout = new javax.swing.GroupLayout(desktopPane);
        desktopPane.setLayout(desktopPaneLayout);
        desktopPaneLayout.setHorizontalGroup(
            desktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        desktopPaneLayout.setVerticalGroup(
            desktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(desktopPaneLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2, Short.MAX_VALUE))
        );

        barraDeAcceso.setRollover(true);

        btnRenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/prestamo_libro.png"))); // NOI18N
        btnRenta.setFocusable(false);
        btnRenta.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRenta.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRentaActionPerformed(evt);
            }
        });
        barraDeAcceso.add(btnRenta);

        btnLibros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/libros.png"))); // NOI18N
        btnLibros.setFocusable(false);
        btnLibros.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLibros.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLibros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLibrosActionPerformed(evt);
            }
        });
        barraDeAcceso.add(btnLibros);

        btnClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/actor.png"))); // NOI18N
        btnClientes.setFocusable(false);
        btnClientes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClientes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientesActionPerformed(evt);
            }
        });
        barraDeAcceso.add(btnClientes);

        btnReportes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/seo-report.png"))); // NOI18N
        btnReportes.setFocusable(false);
        btnReportes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReportes.setPreferredSize(new java.awt.Dimension(34, 33));
        btnReportes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportesActionPerformed(evt);
            }
        });
        barraDeAcceso.add(btnReportes);

        barVent.setRollover(true);

        sptDown.setDividerLocation(250);

        lblSucursal.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblSucursal.setText("Sucursal: ");
        lblSucursal.setToolTipText("");
        sptDown.setLeftComponent(lblSucursal);

        lblEmpleado.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblEmpleado.setText(" Empleado:");
        sptDown.setRightComponent(lblEmpleado);

        barVent.add(sptDown);

        fileMenu.setMnemonic('f');
        fileMenu.setText("Prestamos");
        fileMenu.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        menuRenta.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuRenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/renta.png"))); // NOI18N
        menuRenta.setMnemonic('o');
        menuRenta.setText("Prestamos");
        menuRenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRentaActionPerformed(evt);
            }
        });
        fileMenu.add(menuRenta);

        menuExit.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir.png"))); // NOI18N
        menuExit.setMnemonic('x');
        menuExit.setText("Exit");
        menuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExitActionPerformed(evt);
            }
        });
        fileMenu.add(menuExit);

        menuBar.add(fileMenu);

        editMenu.setMnemonic('e');
        editMenu.setText("Editar");
        editMenu.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        menuPelicula.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuPelicula.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/libros.png"))); // NOI18N
        menuPelicula.setMnemonic('t');
        menuPelicula.setText("Libros");
        menuPelicula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPeliculaActionPerformed(evt);
            }
        });
        editMenu.add(menuPelicula);

        menuActor.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuActor.setMnemonic('y');
        menuActor.setText("Actores");
        menuActor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuActorActionPerformed(evt);
            }
        });
        editMenu.add(menuActor);

        menuCliente.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/actor.png"))); // NOI18N
        menuCliente.setMnemonic('p');
        menuCliente.setText("Clientes");
        editMenu.add(menuCliente);

        menuEmpleado.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuEmpleado.setMnemonic('d');
        menuEmpleado.setText("Empleados");
        editMenu.add(menuEmpleado);

        menuBar.add(editMenu);

        helpMenu.setMnemonic('h');
        helpMenu.setText("Ayuda");
        helpMenu.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        menuAyuda.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuAyuda.setMnemonic('c');
        menuAyuda.setText("Ayuda");
        helpMenu.add(menuAyuda);

        menuAcercaDe.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuAcercaDe.setMnemonic('a');
        menuAcercaDe.setText("Acerca de...");
        helpMenu.add(menuAcercaDe);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane)
            .addComponent(barraDeAcceso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(barVent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barraDeAcceso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(desktopPane)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barVent, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExitActionPerformed
        // Cierra el sistema desde la opción de cerrar del menu 
        System.exit(0);
    }//GEN-LAST:event_menuExitActionPerformed

    // Abrir la ventana de libros
    private void menuPeliculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPeliculaActionPerformed
        AbreLibros();
    }//GEN-LAST:event_menuPeliculaActionPerformed
    
    // Abrir la ventana de libros
    private void menuClienteActionPerformed(java.awt.event.ActionEvent evt) {                                             
        AbreClientes();
    } 
    
    private void menuActorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuActorActionPerformed
        // Accion del menu actor

    }//GEN-LAST:event_menuActorActionPerformed

    private void menuRentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRentaActionPerformed
        // Accion del menu renta
        abrePrestamo();
    }//GEN-LAST:event_menuRentaActionPerformed

    private void btnRentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRentaActionPerformed
        // Accion del boton renta de la barra de accesos rapidos 
        abrePrestamo();
    }//GEN-LAST:event_btnRentaActionPerformed
    
    private void btnLibrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLibrosActionPerformed
        // Accion del boton pelicula de la barra de accesos rapidos 
        AbreLibros();
    }//GEN-LAST:event_btnLibrosActionPerformed

    private void btnClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientesActionPerformed
       AbreClientes();
    }//GEN-LAST:event_btnClientesActionPerformed

    private void btnReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportesActionPerformed
        // TODO add your handling code here:
        AbreReportes();
    }//GEN-LAST:event_btnReportesActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barVent;
    private javax.swing.JToolBar barraDeAcceso;
    private javax.swing.JButton btnClientes;
    private javax.swing.JButton btnLibros;
    private javax.swing.JButton btnRenta;
    private javax.swing.JButton btnReportes;
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblEmpleado;
    private javax.swing.JLabel lblSucursal;
    private javax.swing.JMenuItem menuAcercaDe;
    private javax.swing.JMenuItem menuActor;
    private javax.swing.JMenuItem menuAyuda;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem menuCliente;
    private javax.swing.JMenuItem menuEmpleado;
    private javax.swing.JMenuItem menuExit;
    private javax.swing.JMenuItem menuPelicula;
    private javax.swing.JMenuItem menuRenta;
    private javax.swing.JSplitPane sptDown;
    // End of variables declaration//GEN-END:variables
}