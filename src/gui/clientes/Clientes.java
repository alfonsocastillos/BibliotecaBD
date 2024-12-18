package gui.clientes;
import javax.swing.JOptionPane;
import java.util.HashMap;
import java.util.Map;
import java.awt.Toolkit;
import dataBase.dao.EscolaridadDAO;
import dataBase.dao.ClienteDAO;
import dataBase.dao.EstadoDAO;
import dataBase.dao.PaisDAO;
import tools.UtilsTable;
import gui.VentanaReporte;
import dataBase.CustomReportPDF;

/**
 * Ventana que registra clientes
 * @author Jordi
 */
public class Clientes extends javax.swing.JInternalFrame {    
    int clienteId;
    String reportUrl = "/reportes/Clientes.jrxml";
    Object[][] clientesLista;    
    Map<String, Object> reportParameters;
    
    // Daos que traen los datos de la DB.
    ClienteDAO clienteDAO;
    EscolaridadDAO escolaridadDAO;
    EstadoDAO estadoDAO;
    PaisDAO paisDAO;    
    
    // Ventanas para agregar clientes, credenciales, etc.
    AddEscolaridad addEscolaridad;
    AddPais addPais;
    
    /**
     * Constructor
     */
    public Clientes() {
        initComponents();
        clienteId = 0;
        
        // Se instancian todos los DAO.
        clienteDAO = new ClienteDAO();
        escolaridadDAO = new EscolaridadDAO();
        estadoDAO = new EstadoDAO();
        paisDAO = new PaisDAO();
                
        // Instanciando ventanas para agregar Escolaridades, Paises y Estados.
        addEscolaridad = new AddEscolaridad((java.awt.Frame)this.getParent(), true);
        addPais = new AddPais((java.awt.Frame)this.getParent(), true);
        
        setLocale(null);        
        llenaDatos();
        cancelaEdit();
    }
    
    /**
     * Llena de datos las tablas a desplegar y los combobox.
     */
    private void llenaDatos() {
        llenaTablaClientes();
        llenadoEscolaridades();
        LlenadoPaises();
        llenadoEstados();
    }
    
    /**
     * Cancela la edicion y reinicia los controles a su valor predeterminado.
     */
    private void cancelaEdit () {
        clienteId = 0;
        txtNombre.setText("");
        txtApellidoPat.setText("");
        txtApellidoMat.setText("");
        txtCorreo.setText("");
        txtAlcaldia.setText("");
        txtCP.setText("");
        txtCalle.setText("");
        txtNoExterior.setText("");
        txtNoInterior.setText("");
        txtFiltro.setText("");
        cmbEscolaridad.setSelectedIndex(-1);         
        cmbEstado.setSelectedIndex(-1);
        cmbPais.setSelectedIndex(-1);
    }
       
    /**
     * Popula el ComboBox de Escolaridades disponibles.
     */
    private void llenadoEscolaridades() {
        cmbEscolaridad.removeAllItems();
        Object[][] escolaridades = escolaridadDAO.getAllEscolaridad();
        for(Object[] escolaridad : escolaridades) {
            
            // Llena los datos de Escolaridades en el combo.
            cmbEscolaridad.addItem(escolaridad[1].toString());
        }
        cmbEscolaridad.setSelectedIndex(-1);
    }
        
    /**
     * Popula el combobox de Paises disponibles.
     */
    private void LlenadoPaises() {
        cmbPais.removeAllItems();                
        Object[][] paises = paisDAO.getAllPaises();
        for(Object[] pais : paises) {
           
            // Llena los datos de Paises en el combo.
            cmbPais.addItem(pais[1].toString());
        }        
    }
    
    /**
     * Popula el combobox de Estados disponibles.
     */
    private void llenadoEstados() {
        cmbEstado.removeAllItems();
        if(cmbPais.getSelectedIndex() >= 0) {
            String paisElegido = cmbPais.getSelectedItem().toString();
            int paisId = (int) paisDAO.getPaisesByNombre(paisElegido)[0][0];        
            if(paisId > 0) {
                Object[][] estados = estadoDAO.getAllEstadosByPais(paisId);
                for(Object[] estado : estados) {
                   
                    // Llena los datos de Estados en el combo.
                    cmbEstado.addItem(estado[1].toString());
                }
            } 
        }               
    }
        
    /**
     * Llena de datos la tabla que despliega todos los Libros.
     */
    private void llenaTablaClientes() {       
        clientesLista = clienteDAO.getClientesByFilter(txtFiltro.getText().trim()); 
        
        // Configuración de la tabla.
        String[] columnasNombre = {"", "Nombre", "Apellido Paterno", "Apellido Materno", "Correo", "Credencial"};
        int[][] cellAlignment = {{0,javax.swing.SwingConstants.LEFT}};
        int[][] cellSize = {{0, 0}, // Id
                {1, 150},           // Nombre
                {2, 150},           // Apellido Paterno
                {3, 150},           // Apellido Materno
                {4, 240},           // Correo
                {5, 95}};           // Credencial
                            
        // Pone los datos en la tabla.
        
        UtilsTable.llenaTabla(cellAlignment, cellSize, columnasNombre, tblClientes, clientesLista);
        UtilsTable.quitarColumna(0,tblClientes);
        lblCantidad.setText(clientesLista.length + "");
    }
    
    private boolean estanLlenos() {        
        if(txtNombre.getText().trim().length() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Introduzca un nombre.", "Aviso", 2);
            txtNombre.requestFocus();
            return false;
        } else if(txtApellidoPat.getText().trim().length() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Introduzca un apellido paterno.", "Aviso", 2);
            txtApellidoPat.requestFocus();
            return false;
        } else if(cmbPais.getSelectedIndex() < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un pais.", "Aviso", 2);
            cmbPais.requestFocus();
            return false;
        } else if(cmbEstado.getSelectedIndex() < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un estado.", "Aviso", 2);
            cmbEstado.requestFocus();
            return false;
        } else if(txtAlcaldia.getText().trim().length() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Introduzca una alcaldia.", "Aviso", 2);
            txtAlcaldia.requestFocus();
            return false;
        } else if(txtCP.getText().trim().length() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Introduzca un codigo postal.", "Aviso", 2);
            txtCP.requestFocus();
            return false;
        } else if(txtCalle.getText().trim().length() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Introduzca una calle.", "Aviso", 2);
            txtCalle.requestFocus();
            return false;
        } else if(txtNoExterior.getText().trim().length() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Introduzca un numero exterior.", "Aviso", 2);
            txtNoExterior.requestFocus();
            return false;
        } else if(cmbEscolaridad.getSelectedIndex() < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una escolaridad.", "Aviso", 2);
            cmbEscolaridad.requestFocus();
            return false;
        } else {
            return true;
        }            
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTableList9 = new javax.swing.JPanel();
        pnlTableList10 = new javax.swing.JPanel();
        pnlTableList11 = new javax.swing.JPanel();
        pnlTableList12 = new javax.swing.JPanel();
        pnlClientes = new javax.swing.JPanel();
        btnGuarda = new javax.swing.JButton();
        lblCliente = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        lblTotal1 = new javax.swing.JLabel();
        lblTotal2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtApellidoPat = new javax.swing.JTextField();
        txtApellidoMat = new javax.swing.JTextField();
        txtCorreo = new javax.swing.JTextField();
        txtAlcaldia = new javax.swing.JTextField();
        txtCP = new javax.swing.JTextField();
        txtCalle = new javax.swing.JTextField();
        txtNoExterior = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtNoInterior = new javax.swing.JTextField();
        cmbEscolaridad = new javax.swing.JComboBox<>();
        pnlTableList4 = new javax.swing.JPanel();
        btnEditEscolaridades = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox<>();
        cmbPais = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        pnlTableList13 = new javax.swing.JPanel();
        btnEditarPaises = new javax.swing.JButton();
        pnlTableList14 = new javax.swing.JPanel();
        pnlTableList15 = new javax.swing.JPanel();
        pnlTableList16 = new javax.swing.JPanel();
        btnRefrescar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        pnlTableList = new javax.swing.JPanel();
        scpTableList = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        lblTitulo1 = new javax.swing.JLabel();
        txtFiltro = new javax.swing.JTextField();
        btnDelete = new javax.swing.JButton();
        btnBorrarFiltro = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        lblCantidad = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();
        btnReporte = new javax.swing.JButton();
        btnReportePDF = new javax.swing.JButton();

        pnlTableList9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Escolaridad", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlTableList10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Escolaridad", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnlTableList9.add(pnlTableList10, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 90, 190, 60));

        pnlTableList11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Escolaridad", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlTableList12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Escolaridad", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnlTableList11.add(pnlTableList12, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 90, 190, 60));

        pnlTableList9.add(pnlTableList11, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 170, 190, 60));

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Clientes");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/img/user_icon.png"))); // NOI18N
        setName(""); // NOI18N

        pnlClientes.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agregar Clientes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlClientes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnGuarda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/guardar.png"))); // NOI18N
        btnGuarda.setToolTipText("Guardar Cliente");
        btnGuarda.setFocusable(false);
        btnGuarda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardaActionPerformed(evt);
            }
        });
        pnlClientes.add(btnGuarda, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 20, 60, 60));

        lblCliente.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblCliente.setText("Nombre:");
        pnlClientes.add(lblCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, 20));

        lblFecha.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblFecha.setText("Apellido Paterno:");
        pnlClientes.add(lblFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, 20));

        lblTotal.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblTotal.setText("Alcaldia:");
        pnlClientes.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, -1, 20));

        lblTotal1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblTotal1.setText("Apellido Materno:");
        pnlClientes.add(lblTotal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, 20));

        lblTotal2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblTotal2.setText("Correo:");
        pnlClientes.add(lblTotal2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, 20));

        jLabel1.setText("CP:");
        pnlClientes.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, -1, -1));

        jLabel2.setText("Calle:");
        pnlClientes.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, -1, -1));

        jLabel3.setText("No. Exterior:");
        pnlClientes.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, -1, -1));

        jLabel4.setText("Escolaridad:");
        pnlClientes.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, -1, -1));

        txtNombre.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 250, 20));

        txtApellidoPat.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(txtApellidoPat, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 250, 20));

        txtApellidoMat.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(txtApellidoMat, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 250, 20));

        txtCorreo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(txtCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, 250, 20));

        txtAlcaldia.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(txtAlcaldia, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 230, 250, 20));

        txtCP.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(txtCP, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 260, 250, 20));

        txtCalle.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(txtCalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 290, 250, 20));

        txtNoExterior.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(txtNoExterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 320, 250, 20));

        jLabel5.setText("No. Interior:");
        pnlClientes.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, -1, -1));

        txtNoInterior.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(txtNoInterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 350, 250, 20));

        pnlClientes.add(cmbEscolaridad, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 380, 250, -1));

        pnlTableList4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Escolaridades", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnEditEscolaridades.setText("Editar escolaridades");
        btnEditEscolaridades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditEscolaridadesActionPerformed(evt);
            }
        });
        pnlTableList4.add(btnEditEscolaridades, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 170, -1));

        pnlClientes.add(pnlTableList4, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 250, 190, 60));

        jLabel7.setText("Estado");
        pnlClientes.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, -1));

        pnlClientes.add(cmbEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 250, -1));

        cmbPais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPaisActionPerformed(evt);
            }
        });
        pnlClientes.add(cmbPais, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 170, 250, -1));

        jLabel8.setText("País");
        pnlClientes.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, -1, -1));

        pnlTableList13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Paises", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList13.setFocusCycleRoot(true);
        pnlTableList13.setName(""); // NOI18N
        pnlTableList13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnEditarPaises.setText("Editar paises");
        btnEditarPaises.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarPaisesActionPerformed(evt);
            }
        });
        pnlTableList13.add(btnEditarPaises, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 160, -1));

        pnlTableList14.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Escolaridad", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnlTableList13.add(pnlTableList14, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 90, 190, 60));

        pnlTableList15.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Escolaridad", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlTableList16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Escolaridad", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnlTableList15.add(pnlTableList16, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 90, 190, 60));

        pnlTableList13.add(pnlTableList15, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 170, 190, 60));

        pnlClientes.add(pnlTableList13, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 150, 190, 60));
        pnlTableList13.getAccessibleContext().setAccessibleName("");
        pnlTableList13.getAccessibleContext().setAccessibleDescription("");

        btnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/refresh.png"))); // NOI18N
        btnRefrescar.setToolTipText("Guardar Cliente");
        btnRefrescar.setFocusable(false);
        btnRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefrescarActionPerformed(evt);
            }
        });
        pnlClientes.add(btnRefrescar, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 20, 60, 60));

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/cancelar.png"))); // NOI18N
        btnCancelar.setToolTipText("Guardar Cliente");
        btnCancelar.setFocusable(false);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        pnlClientes.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 20, 60, 60));

        pnlTableList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lista de clientes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scpTableList.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scpTableList.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblClientes.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        scpTableList.setViewportView(tblClientes);

        pnlTableList.add(scpTableList, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 810, 240));

        lblTitulo1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblTitulo1.setText("Filtrar:");
        pnlTableList.add(lblTitulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, 25));

        txtFiltro.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroKeyReleased(evt);
            }
        });
        pnlTableList.add(txtFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 230, 25));

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/borrar.png"))); // NOI18N
        btnDelete.setToolTipText("Borrar libro");
        btnDelete.setFocusable(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        pnlTableList.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 20, 40, 40));

        btnBorrarFiltro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/cancelarm.png"))); // NOI18N
        btnBorrarFiltro.setToolTipText("Borrar filtro");
        btnBorrarFiltro.setFocusable(false);
        btnBorrarFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarFiltroActionPerformed(evt);
            }
        });
        pnlTableList.add(btnBorrarFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 28, 30, 30));

        jLabel6.setText("Numero de clientes:");
        pnlTableList.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, -1, 30));

        lblCantidad.setText("000");
        pnlTableList.add(lblCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 310, -1, 30));

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/editar.png"))); // NOI18N
        btnEdit.setToolTipText("Editar");
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        pnlTableList.add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 20, 40, 40));

        btnReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/reporte.png"))); // NOI18N
        btnReporte.setToolTipText("Mostrar reporte");
        btnReporte.setFocusable(false);
        btnReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteActionPerformed(evt);
            }
        });
        pnlTableList.add(btnReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 20, 40, 40));

        btnReportePDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/pdf-icon.png"))); // NOI18N
        btnReportePDF.setToolTipText("Mostrar en PDF");
        btnReportePDF.setFocusable(false);
        btnReportePDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportePDFActionPerformed(evt);
            }
        });
        pnlTableList.add(btnReportePDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 20, 40, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlTableList, javax.swing.GroupLayout.DEFAULT_SIZE, 860, Short.MAX_VALUE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlClientes, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTableList, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlClientes.getAccessibleContext().setAccessibleName("Clientes");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Guarda el registro del Cliente.
     * @param evt evento que dispara la funcion.
     */
    private void btnGuardaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardaActionPerformed
        if(estanLlenos()) {

            // Obtener datos de los campos de texto.
            String nombre = txtNombre.getText();
            String apellidoPat = txtApellidoPat.getText();
            String apellidoMat = txtApellidoMat.getText();
            String correo = txtCorreo.getText();
            String estado = cmbEstado.getSelectedItem().toString();        
            int estadoId = (int) estadoDAO.getEstadosByNombre(estado)[0][0];
            String alcaldia = txtAlcaldia.getText();
            String cp = txtCP.getText();
            String calle = txtCalle.getText();
            String noExterior = txtNoExterior.getText();
            String noInterior = txtNoInterior.getText();

            // Obtener la escolaridad seleccionada en el ComboBox
            String escolaridadSeleccionada = cmbEscolaridad.getSelectedItem().toString();
            String escolaridadId = escolaridadDAO.getEscolaridadByNombre(escolaridadSeleccionada)[0][0].toString();

            // Crear un objeto para representar la información del Cliente.
            Object[] cliente = {nombre, apellidoPat, apellidoMat, correo, alcaldia, cp, calle, noExterior, noInterior, estadoId, escolaridadId};
            
            // Guardar o editar Cliente
            if(clienteId == 0) {
                // Insertar en la base de datos y obtener el nuevo ID del cliente.
                int newClienteId = clienteDAO.saveCliente(cliente);

                // Si el Id no es 0, procede a llenar los demas datos.
                if(newClienteId != 0) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Datos guardados con éxito.", "Información", 1);
                    llenaTablaClientes();
                    UtilsTable.mueveTabla(UtilsTable.getRow(newClienteId, clientesLista), tblClientes);
                    txtNombre.requestFocus();
                    cancelaEdit();
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar.", "Error", 1);
                } 
            } else {
                clienteId = clienteDAO.updateCliente(clienteId, cliente);
                if(clienteId != 0) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Datos actualizados con éxito.", "Información", 1);
                    llenaTablaClientes();
                    UtilsTable.mueveTabla(UtilsTable.getRow(clienteId, clientesLista), tblClientes);
                    txtNombre.requestFocus();
                    cancelaEdit();
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Error al actualizar.", "Error", 1);
                } 
            }                       
        }    
    }//GEN-LAST:event_btnGuardaActionPerformed

    /**
     * Abre una ventana para editar Escolaridades.
     * @param evt evento que dispara la funcion.
     */
    private void btnEditEscolaridadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditEscolaridadesActionPerformed
        
        // Hace visible la ventana para agregar Escolaridades.
        addEscolaridad.setLocationRelativeTo(this);
        addEscolaridad.setVisible(true);
        llenadoEscolaridades();
    }//GEN-LAST:event_btnEditEscolaridadesActionPerformed

    /**
     * Llena los datos del Cliente seleccionado de la tabla de Clientes.
     * @param evt evento que dispara la funcion.
     */
    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        if(evt.getClickCount() == 2) {
            btnEditActionPerformed(null);
        }        
    }//GEN-LAST:event_tblClientesMouseClicked

    /**
     * Filtra la tabla de Clientes al introducir una tecla en el filtro.
     * @param evt evento que dispara la funcion.
     */
    private void txtFiltroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyReleased
        llenaTablaClientes();
    }//GEN-LAST:event_txtFiltroKeyReleased

    /**
     * Borra el texto introducido en el filtro de Clientes.
     * @param evt evento que dispara la funcion.
     */
    private void btnBorrarFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarFiltroActionPerformed
        txtFiltro.setText("");
        llenaTablaClientes();
    }//GEN-LAST:event_btnBorrarFiltroActionPerformed

    /**
     * Llena el combobox de Estados al seleccionar un Pais.
     * @param evt evento que dispara la funcion.
     */
    private void cmbPaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPaisActionPerformed
        llenadoEstados();
    }//GEN-LAST:event_cmbPaisActionPerformed
    
    /**
     * Abre una ventana para editar los Paises disponibles.
     * @param evt evento que dispara la funcion.
     */
    private void btnEditarPaisesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarPaisesActionPerformed
        
        // Hace visible la ventana para agregar Paises.
        addPais.setLocationRelativeTo(this);
        addPais.setVisible(true);

        // Al cerrar, actualizar los Paises disponibles.
        LlenadoPaises();
        cancelaEdit();
    }//GEN-LAST:event_btnEditarPaisesActionPerformed

    /**
     * Llena los datos del Cliente seleccionado de la tabla de Clientes.
     * @param evt evento que dispara la funcion.
     */
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if(tblClientes.getSelectedRow() < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila.", "Información", 1);
        } else {
          
            // Consulta los datos.            
            Object[] clienteEdit = clienteDAO.getClienteById((int) UtilsTable.obtenerValor(tblClientes.getSelectedRow(), 0, tblClientes)); 
            if(clienteEdit != null) {    
                clienteId = (int) clienteEdit[0];                           // Id.
                txtNombre.setText(clienteEdit[1].toString());               // Nombre.
                txtApellidoPat.setText(clienteEdit[2].toString());          // Apellido Paterno.
                txtApellidoMat.setText(clienteEdit[3].toString());          // Apellido Materno.
                txtCorreo.setText(clienteEdit[4].toString());               // Correo.
                cmbPais.setSelectedItem(clienteEdit[5].toString());         // Pais.
                cmbEstado.setSelectedItem(clienteEdit[6].toString());       // Estado.
                txtAlcaldia.setText(clienteEdit[7].toString());             // Alcaldia.
                txtCP.setText(clienteEdit[8].toString());                   // CP.
                txtCalle.setText(clienteEdit[9].toString());                // Calle.
                txtNoExterior.setText(clienteEdit[10].toString());          // Numero Exterior.
                if(clienteEdit[11] != null) {
                    txtNoInterior.setText(clienteEdit[11].toString());      // Numero Interior.
                }                
                cmbEscolaridad.setSelectedItem(clienteEdit[12].toString()); // Estado.                             
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed

    /**
     * Elimina a un Cliente de la base de datos.
     * @param evt evento que dispara la funcion.
     */
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        if(tblClientes.getSelectedRow() < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila.", "Aviso", 2);
        } else {
            
            // Suena un beep y se muestra un mensaje de confirmacion.
            java.awt.Toolkit.getDefaultToolkit().beep();
            int res = javax.swing.JOptionPane.showConfirmDialog(this, "¿Eliminar " + clientesLista[tblClientes.getSelectedRow()][1].toString() + "?",
                    "Seleccione", JOptionPane.YES_NO_OPTION);
            
            // Si la respuesta es afirmativa, elimina el registro.
            if(res == 0) {
                String msj = "";
                clienteId = (int) UtilsTable.obtenerValor(tblClientes.getSelectedRow(), 0, tblClientes);
                int ret = clienteDAO.deleteCliente(clienteId);
                if(ret == 1) {
                    msj = "Se dio de baja el cliente.";
                } else {
                    msj = "No se pudo dar de baja por que tiene registros asignados o\nno fue seleccionado apropiadamente.";
                }
                
                // Suena un beep y se muestra un mensaje.
                java.awt.Toolkit.getDefaultToolkit().beep();
                javax.swing.JOptionPane.showMessageDialog(this, msj, "Información", 1);
                
                // Reinicia controles y parametros.
                cancelaEdit();
                llenaTablaClientes(); 
                txtNombre.requestFocus();
            }        
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    /**
     * Actualiza manualmente los datos disponibles en los controladores.
     * @param evt evento que dispara la funcion.
     */
    private void btnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefrescarActionPerformed
        llenaDatos();
        cancelaEdit();
    }//GEN-LAST:event_btnRefrescarActionPerformed

    /**
     * Vacia los campos de los controladores.
     * @param evt evento que dispara la funcion.
     */
    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelaEdit();
        tblClientes.clearSelection();
        llenaTablaClientes();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteActionPerformed
        reportParameters = new HashMap();
        reportParameters.put("filtro", "%" + txtFiltro.getText().trim() + "%");
        VentanaReporte reporteLibros = new VentanaReporte(reportUrl, reportParameters);
        reporteLibros.setVisible(true);
    }//GEN-LAST:event_btnReporteActionPerformed

    private void btnReportePDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportePDFActionPerformed
        reportParameters = new HashMap();
        reportParameters.put("filtro", "%" + txtFiltro.getText().trim() + "%");
        CustomReportPDF reporte = new CustomReportPDF();
        reporte.generateReport(reportUrl, reportParameters);
    }//GEN-LAST:event_btnReportePDFActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBorrarFiltro;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnEditEscolaridades;
    private javax.swing.JButton btnEditarPaises;
    private javax.swing.JButton btnGuarda;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JButton btnReporte;
    private javax.swing.JButton btnReportePDF;
    private javax.swing.JComboBox<String> cmbEscolaridad;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbPais;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblCliente;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblTitulo1;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotal1;
    private javax.swing.JLabel lblTotal2;
    private javax.swing.JPanel pnlClientes;
    private javax.swing.JPanel pnlTableList;
    private javax.swing.JPanel pnlTableList10;
    private javax.swing.JPanel pnlTableList11;
    private javax.swing.JPanel pnlTableList12;
    private javax.swing.JPanel pnlTableList13;
    private javax.swing.JPanel pnlTableList14;
    private javax.swing.JPanel pnlTableList15;
    private javax.swing.JPanel pnlTableList16;
    private javax.swing.JPanel pnlTableList4;
    private javax.swing.JPanel pnlTableList9;
    private javax.swing.JScrollPane scpTableList;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtAlcaldia;
    private javax.swing.JTextField txtApellidoMat;
    private javax.swing.JTextField txtApellidoPat;
    private javax.swing.JTextField txtCP;
    private javax.swing.JTextField txtCalle;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtFiltro;
    private javax.swing.JTextField txtNoExterior;
    private javax.swing.JTextField txtNoInterior;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
