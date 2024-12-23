package gui;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import gui.libro.Libros;
import gui.prestamo.Prestamo;
import gui.clientes.Clientes;
import tools.UtilsGUI;

/**
 * Menu principal de la aplicacion.
 * @author Carlos Cortés Bazán
 */
public class MDIPrincipal extends javax.swing.JFrame {
    
    // Para guardar los Ids y ponerlos en los registros.
    String empleadoId;
    String sucursalId;
    
    // Para guardar los nombres de la tienda y empleado para mostralos en la interfaz.
    String empleadoNombre;
    
    // Declara las ventanas.
    Libros librosVentana;    
    Clientes clientesVentana;
    Prestamo prestamoVentana;
        
    String appVersion = "1.48";
    
    /**
     * Creates new form MDIPrincipal.
     * @param sucursalId Id de la sucursal a la que pertenece el empleado que inicia sesion.
     * @param empleadoId Id del empleado que inicia sesion.
     * @param sucursal Nombre de la sucursal.
     * @param empleadoNombre Nombre del empleado.
     */
    public MDIPrincipal(String sucursalId, String empleadoId, String sucursal, String empleadoNombre) { 
        
        // Obtiene los valores que consultó la ventana de inicio de sesión.
        this.sucursalId = sucursalId;
        this.empleadoId = empleadoId;
        this.empleadoNombre = empleadoNombre;
        
        // Inicia todos los componentes.
        initComponents();        
        
        // Instancia y agrega las ventanas.
        configComponents();      
        
        // Muestra en nombre de la tienda y de empleado en la parte inferior.
        lblSucursal. setText(" Sucursal: " + sucursal);
        lblEmpleado. setText(" Empleado: " + empleadoNombre);
    }
    
    /**
     * Configura aspectos graficos de la pantalla.
     */
    private void configComponents() {
        
        // Carga el aspecto grafico.
        UtilsGUI.setLookAndFeel(this);
        
        // Localización de la ventana en la pantalla.
        setLocationRelativeTo(this.getParent());    
        
        // Ventana maximizada.
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        
        // Instancia las ventanas.
        librosVentana = new Libros();
        clientesVentana = new Clientes();
        prestamoVentana = new Prestamo(empleadoId,sucursalId, empleadoNombre);
                
        // Agrega las ventanas al panel.
        desktopPane.add(librosVentana);
        desktopPane.add(clientesVentana);
        desktopPane.add(prestamoVentana);
        
        // Ajusta el tamaño de la ventana al preferido por sus componentes internos.
        pack(); 
    }
      
    /**
     * Posiciona las ventanas flotantes.
     * @param frame cuadro sobre el que se despliegan las demas ventanas.
     */
    private void setLocation (JInternalFrame frame) {
        Dimension desktopSize = desktopPane.getSize();
        Dimension frameSize = frame.getSize();
        frame.setLocation((desktopSize.width - frameSize.width) / 2, (desktopSize.height- frameSize.height) / 2);
    }
       
    /**
     * Despliega la ventana de prestamos.
     */
    private void abrePrestamo () {          
        if(!prestamoVentana.isVisible()) {
            
            //Abre la ventana prestamo.
            setLocation(prestamoVentana);                        
            prestamoVentana.setVisible(true);
        } else {
            
            // Si ya está abierta, la pone adelante
            prestamoVentana.moveToFront();
        }            
    }
    
    /**
     * Despliega la ventana de libros.
     */
    private void abreLibros() {
        if(librosVentana == null) {
            librosVentana = new Libros();
        }
        
        // Abre la ventana libros.
        if(!librosVentana.isVisible()) {            
            setLocation(librosVentana);            
            librosVentana.setVisible(true);
        } else {
            
            // Si ya está abierta, la pone adelante
            librosVentana.moveToFront();
        }            
    }
    
    /**
     * Despliega la ventana de clientes.
     */
    private void abreClientes() {
        
        // Abre la ventana clientes.
        if(!clientesVentana.isVisible()) {           
            setLocation(clientesVentana);            
            clientesVentana.setVisible(true);
        } else {
            
            // Si ya está abierta, la pone adelante.
            clientesVentana.moveToFront();
        }            
    }
    
    /**
     * Despliega la ventana de clientes.
     */
    private void abreAutores() {
        AddAutores addAutores = new AddAutores(null, true);        
        addAutores.setLocationRelativeTo(this);
        addAutores.setVisible(true);           
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
        btnPrestamo = new javax.swing.JButton();
        btnLibros = new javax.swing.JButton();
        btnClientes = new javax.swing.JButton();
        barVent = new javax.swing.JToolBar();
        sptDown = new javax.swing.JSplitPane();
        lblSucursal = new javax.swing.JLabel();
        lblEmpleado = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        menuPrestamos = new javax.swing.JMenu();
        menuItemPrestamos = new javax.swing.JMenuItem();
        menuItemExit = new javax.swing.JMenuItem();
        menuEdit = new javax.swing.JMenu();
        menuLibros = new javax.swing.JMenuItem();
        menuAutores = new javax.swing.JMenuItem();
        menuClientes = new javax.swing.JMenuItem();
        menuAyuda = new javax.swing.JMenu();
        menuItemAyuda = new javax.swing.JMenuItem();
        menuAcercaDe = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Prestamo de libros");
        setAutoRequestFocus(false);
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage( getClass().getResource("/img/icono.png")));

        desktopPane.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/biblioteca_fondo.png"))); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(1920, 1080));
        jLabel1.setMinimumSize(new java.awt.Dimension(860, 540));

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
                .addGap(0, 11, Short.MAX_VALUE))
        );

        barraDeAcceso.setRollover(true);

        btnPrestamo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/prestamo_libro.png"))); // NOI18N
        btnPrestamo.setFocusable(false);
        btnPrestamo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrestamo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPrestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrestamoActionPerformed(evt);
            }
        });
        barraDeAcceso.add(btnPrestamo);

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

        btnClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/user_icon.png"))); // NOI18N
        btnClientes.setFocusable(false);
        btnClientes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClientes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientesActionPerformed(evt);
            }
        });
        barraDeAcceso.add(btnClientes);

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

        menuPrestamos.setMnemonic('f');
        menuPrestamos.setText("Prestamos");
        menuPrestamos.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        menuItemPrestamos.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuItemPrestamos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/renta.png"))); // NOI18N
        menuItemPrestamos.setMnemonic('o');
        menuItemPrestamos.setText("Prestamos");
        menuItemPrestamos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemPrestamosActionPerformed(evt);
            }
        });
        menuPrestamos.add(menuItemPrestamos);

        menuItemExit.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuItemExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir.png"))); // NOI18N
        menuItemExit.setMnemonic('x');
        menuItemExit.setText("Exit");
        menuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemExitActionPerformed(evt);
            }
        });
        menuPrestamos.add(menuItemExit);

        menuBar.add(menuPrestamos);

        menuEdit.setMnemonic('e');
        menuEdit.setText("Editar");
        menuEdit.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        menuLibros.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuLibros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/libros.png"))); // NOI18N
        menuLibros.setMnemonic('t');
        menuLibros.setText("Libros");
        menuLibros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLibrosActionPerformed(evt);
            }
        });
        menuEdit.add(menuLibros);

        menuAutores.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuAutores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pen.png"))); // NOI18N
        menuAutores.setMnemonic('y');
        menuAutores.setText("Autores");
        menuAutores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAutoresActionPerformed(evt);
            }
        });
        menuEdit.add(menuAutores);

        menuClientes.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/user_icon.png"))); // NOI18N
        menuClientes.setMnemonic('p');
        menuClientes.setText("Clientes");
        menuClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuClientesActionPerformed(evt);
            }
        });
        menuEdit.add(menuClientes);

        menuBar.add(menuEdit);

        menuAyuda.setMnemonic('h');
        menuAyuda.setText("Ayuda");
        menuAyuda.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        menuItemAyuda.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuItemAyuda.setMnemonic('c');
        menuItemAyuda.setText("Ayuda");
        menuItemAyuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAyudaActionPerformed(evt);
            }
        });
        menuAyuda.add(menuItemAyuda);

        menuAcercaDe.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        menuAcercaDe.setMnemonic('a');
        menuAcercaDe.setText("Acerca de...");
        menuAcercaDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAcercaDeActionPerformed(evt);
            }
        });
        menuAyuda.add(menuAcercaDe);

        menuBar.add(menuAyuda);

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

    /**
     * Cierra el sistema desde la opción de cerrar del menu.
     * @param evt evento que dispara la funcion.
     */
    private void menuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_menuItemExitActionPerformed
   
    /**
     * Abrir la ventana de libros cuando se le da clic.
     * @param evt evento que dispara la funcion.
     */
    private void menuLibrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLibrosActionPerformed
        abreLibros();
    }//GEN-LAST:event_menuLibrosActionPerformed
    
    /**
     * Abrir la ventana de prestamos cuando se le da clic.
     * @param evt evento que dispara la funcion.
     */
    private void menuItemPrestamosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemPrestamosActionPerformed
        abrePrestamo();
    }//GEN-LAST:event_menuItemPrestamosActionPerformed

    /**
     * Abrir la ventana de prestamos cuando se le da clic.
     * @param evt evento que dispara la funcion.
     */
    private void btnPrestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrestamoActionPerformed
        abrePrestamo();
    }//GEN-LAST:event_btnPrestamoActionPerformed
    
    /**
     * Abrir la ventana de libros cuando se le da clic.
     * @param evt evento que dispara la funcion.
     */
    private void btnLibrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLibrosActionPerformed
        abreLibros();
    }//GEN-LAST:event_btnLibrosActionPerformed

    /**
     * Abrir la ventana de clientes cuando se le da clic.
     * @param evt evento que dispara la funcion.
     */
    private void btnClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientesActionPerformed
       abreClientes();
    }//GEN-LAST:event_btnClientesActionPerformed

    /**
     * Abrir la ventana de clientes cuando se le da clic.
     * @param evt evento que dispara la funcion.
     */
    private void menuClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuClientesActionPerformed
        abreClientes();
    }//GEN-LAST:event_menuClientesActionPerformed

    /**
     * Mostrar un mensaje de ayuda para el usuario.
     * @param evt evento que dispara la funcion.
     */
    private void menuItemAyudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAyudaActionPerformed
        JOptionPane.showMessageDialog(this, "Seleccione alguno de los iconos en el menu para comenzar a consultar, agregar, editar y eliminar registros", "Aviso", 2);   
    }//GEN-LAST:event_menuItemAyudaActionPerformed

    /**
     * Mostrar un mensaje de informacion para el usuario.
     * @param evt evento que dispara la funcion.
     */
    private void menuAcercaDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAcercaDeActionPerformed
        JOptionPane.showMessageDialog(this, "Version: " + appVersion + "\nOS: " + System.getProperty("os.name"), "Aviso", 2);
    }//GEN-LAST:event_menuAcercaDeActionPerformed

    /**
     * Abre una ventana para agregar, editar y eliminar Autores del catalogo.
     * @param evt evento que dispara la funcion.
     */
    private void menuAutoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAutoresActionPerformed
        abreAutores();
    }//GEN-LAST:event_menuAutoresActionPerformed
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barVent;
    private javax.swing.JToolBar barraDeAcceso;
    private javax.swing.JButton btnClientes;
    private javax.swing.JButton btnLibros;
    private javax.swing.JButton btnPrestamo;
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblEmpleado;
    private javax.swing.JLabel lblSucursal;
    private javax.swing.JMenuItem menuAcercaDe;
    private javax.swing.JMenuItem menuAutores;
    private javax.swing.JMenu menuAyuda;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem menuClientes;
    private javax.swing.JMenu menuEdit;
    private javax.swing.JMenuItem menuItemAyuda;
    private javax.swing.JMenuItem menuItemExit;
    private javax.swing.JMenuItem menuItemPrestamos;
    private javax.swing.JMenuItem menuLibros;
    private javax.swing.JMenu menuPrestamos;
    private javax.swing.JSplitPane sptDown;
    // End of variables declaration//GEN-END:variables
}