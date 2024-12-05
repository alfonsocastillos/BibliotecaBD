package gui.clientes;
import javax.swing.JOptionPane;
import dataBase.dao.EscolaridadDAO;
import dataBase.dao.ClienteDAO;
import dataBase.dao.EstadoDAO;
import dataBase.dao.PaisDAO;
import tools.UtilsTable;

/**
 *
 * @author Jordi
 * Ventana que registra clientes
 */
public class Clientes extends javax.swing.JInternalFrame {
    int cliente_id;
    Object clientes_lista[][];
    // Dao´s que traen los datos de la DB
    ClienteDAO cliente_dao;
    EscolaridadDAO escolaridad_dao;
    EstadoDAO estado_dao;
    PaisDAO pais_dao;    
    
    // Ventanas para agregar clientes, credenciales, etc.
    AddEscolaridad add_escolaridad;
    AddPais add_pais;
    
    public Clientes() {
        // Constructor
        initComponents();
        
        // Un 0 significa error en el id
        cliente_id = 0;
        
        // se instancian todos los DAO
        cliente_dao = new ClienteDAO();
        escolaridad_dao = new EscolaridadDAO();
        estado_dao = new EstadoDAO();
        pais_dao = new PaisDAO();
                
        // Instanciando ventanas para agregar Credenciales, clientes, etc.
        add_escolaridad = new AddEscolaridad((java.awt.Frame)this.getParent(), true);
        add_pais = new AddPais((java.awt.Frame)this.getParent(), true);
        
        // localización de la ventana
        setLocale(null);
        
        LlenaDatos();
        // Vacia las selecciones de campos
        CancelaEdit();
    }
    
    // Llena de datos las tablas a desplegar y los combo box
    private void LlenaDatos() {
        // Llena combos y tabla de clientes
        LlenaTablaClientes();
        LlenadoEscolaridades();
        LlenadoPaises();
        LlenadoEstados();
    }
    
    // Cancela la edición y reinicia los controles a su valor predeterminado
    private void CancelaEdit (){        
        TextFieldNombre.setText("");
        TextFieldApellidoPat.setText("");
        TextFieldApellidoMat.setText("");
        TextFieldCorreo.setText("");
        TextFieldAlcaldia.setText("");
        TextFieldCP.setText("");
        TextFieldCalle.setText("");
        TextFieldNoExterior.setText("");
        TextFieldNoInterior.setText("");
        TextFieldFiltro.setText("");
        cmbEscolaridad.setSelectedIndex(-1);         
        cmbEstado.setSelectedIndex(-1);
        cmbPais.setSelectedIndex(-1);
    }
    
    // Popula el ComboBox de escolaridades disponibles
    private void LlenadoEscolaridades() {
        cmbEscolaridad.removeAllItems();
        Object[][] escolaridades = escolaridad_dao.GetAllEscolaridad();
        for (Object[] escolaridad : escolaridades) {
            // llena los datos de escolaridades en el combo 
            cmbEscolaridad.addItem(escolaridad[1].toString());
        }
        cmbEscolaridad.setSelectedIndex(-1);
    }
    
    // Popula el ComboBox de paises disponibles
    private void LlenadoPaises() {
        cmbPais.removeAllItems();                
        Object[][] paises = pais_dao.GetAllPaises();
        for (Object[] pais : paises) {
            // llena los datos de paises en el combo 
            cmbPais.addItem(pais[1].toString());
        }        
    }
    
    // Popula el ComboBox de estados disponibles
    private void LlenadoEstados() {
        cmbEstado.removeAllItems();
        if(cmbPais.getSelectedIndex() >= 0) {
            String pais_elegido = cmbPais.getSelectedItem().toString();
            int pais_id = (int) pais_dao.GetPaisesByNombre(pais_elegido)[0][0];        
            if(pais_id > 0) {
                Object[][] estados = estado_dao.GetAllEstadosByPais(pais_id);
                for (Object[] estado : estados) {
                    // llena los datos de estados en el combo 
                    cmbEstado.addItem(estado[1].toString());
                }
            } 
        }               
    }
    
    // Llena de datos la tabla que despliega todos los libros
    private void LlenaTablaClientes(){       
        // consulta los datos de las peliculas
        clientes_lista = cliente_dao.GetClientesByFilter(TextFieldFiltro.getText().trim()); 
        // configuración de la tabla
        String[] T_CLIENTES = {"", "Nombre", "Apellido Paterno", "Apellido Materno", "Correo", "Credencial"};
        int[][] cellAlignment = {{0,javax.swing.SwingConstants.LEFT}};
        int[][] cellSize = {{0, 0},     // Id
                            {1, 150},   // Nombre
                            {2, 150},   // Apellido Paterno
                            {3, 150},   // Apellido Materno
                            {4, 240},   // Correo
                            {5, 95}};   // Credencial
                            
        // pone los datos en la tabla
        UtilsTable.llenaTabla(tableListClientes, clientes_lista, T_CLIENTES, cellAlignment, cellSize);
        UtilsTable.quitarColumna(tableListClientes, 0);
        lblCantidad.setText(clientes_lista.length + "");
    }
    
    private boolean EstanLlenos(){        
        if (TextFieldNombre.getText().trim().length() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Introduzca un nombre.", "Aviso", 2);
            TextFieldNombre.requestFocus();
            return false;
        } else if (TextFieldApellidoPat.getText().trim().length() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Introduzca un apellido paterno.", "Aviso", 2);
            TextFieldApellidoPat.requestFocus();
            return false;
        } else if (cmbPais.getSelectedIndex() < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un pais.", "Aviso", 2);
            cmbPais.requestFocus();
            return false;
        } else if (cmbEstado.getSelectedIndex() < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un estado.", "Aviso", 2);
            cmbEstado.requestFocus();
            return false;
        } else if (TextFieldAlcaldia.getText().trim().length() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Introduzca una alcaldia.", "Aviso", 2);
            TextFieldAlcaldia.requestFocus();
            return false;
        } else if (TextFieldCP.getText().trim().length() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Introduzca un codigo postal.", "Aviso", 2);
            TextFieldCP.requestFocus();
            return false;
        } else if (TextFieldCalle.getText().trim().length() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Introduzca una calle.", "Aviso", 2);
            TextFieldCalle.requestFocus();
            return false;
        } else if (TextFieldNoExterior.getText().trim().length() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Introduzca un numero exterior.", "Aviso", 2);
            TextFieldNoExterior.requestFocus();
            return false;
        } else if (cmbEscolaridad.getSelectedIndex() < 0) {
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
        TextFieldNombre = new javax.swing.JTextField();
        TextFieldApellidoPat = new javax.swing.JTextField();
        TextFieldApellidoMat = new javax.swing.JTextField();
        TextFieldCorreo = new javax.swing.JTextField();
        TextFieldAlcaldia = new javax.swing.JTextField();
        TextFieldCP = new javax.swing.JTextField();
        TextFieldCalle = new javax.swing.JTextField();
        TextFieldNoExterior = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        TextFieldNoInterior = new javax.swing.JTextField();
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
        tableListClientes = new javax.swing.JTable();
        lblTitulo1 = new javax.swing.JLabel();
        TextFieldFiltro = new javax.swing.JTextField();
        btnBorrar = new javax.swing.JButton();
        btnBorrarFiltro = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        lblCantidad = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();

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

        setClosable(true);
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

        TextFieldNombre.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(TextFieldNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 250, 20));

        TextFieldApellidoPat.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(TextFieldApellidoPat, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 250, 20));

        TextFieldApellidoMat.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(TextFieldApellidoMat, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 250, 20));

        TextFieldCorreo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(TextFieldCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, 250, 20));

        TextFieldAlcaldia.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(TextFieldAlcaldia, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 230, 250, 20));

        TextFieldCP.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(TextFieldCP, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 260, 250, 20));

        TextFieldCalle.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(TextFieldCalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 290, 250, 20));

        TextFieldNoExterior.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(TextFieldNoExterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 320, 250, 20));

        jLabel5.setText("No. Interior:");
        pnlClientes.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, -1, -1));

        TextFieldNoInterior.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(TextFieldNoInterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 350, 250, 20));

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

        tableListClientes.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tableListClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableListClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableListClientesMouseClicked(evt);
            }
        });
        scpTableList.setViewportView(tableListClientes);

        pnlTableList.add(scpTableList, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 810, 240));

        lblTitulo1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblTitulo1.setText("Filtrar:");
        pnlTableList.add(lblTitulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, 25));

        TextFieldFiltro.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        TextFieldFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextFieldFiltroActionPerformed(evt);
            }
        });
        TextFieldFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TextFieldFiltroKeyReleased(evt);
            }
        });
        pnlTableList.add(TextFieldFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 230, 25));

        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/borrar.png"))); // NOI18N
        btnBorrar.setToolTipText("Borrar libro");
        btnBorrar.setFocusable(false);
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });
        pnlTableList.add(btnBorrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 20, 40, 40));

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
        pnlTableList.add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(715, 20, 40, 40));

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

    private void btnGuardaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardaActionPerformed
    if(EstanLlenos()) {
        // Obtener datos de los campos de texto
        String nombre = TextFieldNombre.getText();
        String apellidoPat = TextFieldApellidoPat.getText();
        String apellidoMat = TextFieldApellidoMat.getText();
        String correo = TextFieldCorreo.getText();
        String estado = cmbEstado.getSelectedItem().toString();        
        String estado_id = estado_dao.GetEstadosByNombre(0, estado)[0][0].toString();
        String alcaldia = TextFieldAlcaldia.getText();
        String cp = TextFieldCP.getText();
        String calle = TextFieldCalle.getText();
        String noExterior = TextFieldNoExterior.getText();
        String noInterior = TextFieldNoInterior.getText();

        // Obtener la escolaridad seleccionada en el ComboBox
        String escolaridadSeleccionada = cmbEscolaridad.getSelectedItem().toString();
        String escolaridad_id = escolaridad_dao.GetEscolaridadByNombre(escolaridadSeleccionada)[0][0].toString();

        // Crear un objeto para representar la información del nuevo cliente
        Object[] nuevoCliente = {nombre, apellidoPat, apellidoMat, correo, alcaldia, cp, calle, noExterior, noInterior, estado_id, escolaridad_id};

        // Insertar en la base de datos y obtener el nuevo ID del cliente
        String new_cliente_id = cliente_dao.saveCliente(nuevoCliente);
        // Si el id no es 0, procede a llenar los demas datos
        if (!new_cliente_id.isEmpty()){                
            javax.swing.JOptionPane.showMessageDialog(this, "Datos guardados con éxito.", "Información", 1);
            LlenaTablaClientes();
            UtilsTable.mueveTabla(tableListClientes, UtilsTable.getRow(clientes_lista, cliente_id));
            TextFieldNombre.requestFocus();
            CancelaEdit();
        }
        else {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar.","Error", 1);
        }            
    }
    
    }//GEN-LAST:event_btnGuardaActionPerformed

    private void btnEditEscolaridadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditEscolaridadesActionPerformed
        // Hace visible la ventana para agregar paises
        add_escolaridad.setLocationRelativeTo(this);
        add_escolaridad.setVisible(true);
        LlenadoEscolaridades();
    }//GEN-LAST:event_btnEditEscolaridadesActionPerformed

    private void tableListClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListClientesMouseClicked
        // Muestra un ballon si es necesarios
        if (evt.getClickCount() == 2) {
            btnEditActionPerformed(null);
        }        
    }//GEN-LAST:event_tableListClientesMouseClicked

    private void TextFieldFiltroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextFieldFiltroKeyReleased
        LlenaTablaClientes();
    }//GEN-LAST:event_TextFieldFiltroKeyReleased

    private void btnBorrarFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarFiltroActionPerformed
        TextFieldFiltro.setText("");
        LlenaTablaClientes();
    }//GEN-LAST:event_btnBorrarFiltroActionPerformed

    private void TextFieldFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextFieldFiltroActionPerformed

    }//GEN-LAST:event_TextFieldFiltroActionPerformed

    private void cmbPaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPaisActionPerformed
        LlenadoEstados();
    }//GEN-LAST:event_cmbPaisActionPerformed

    private void EditEscolaridad8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditEscolaridad8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EditEscolaridad8ActionPerformed

    private void btnEditarPaisesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarPaisesActionPerformed
        // Hace visible la ventana para agregar paises
        add_pais.setLocationRelativeTo(this);
        add_pais.setVisible(true);

        // Al cerrar, actualizar los idiomas disponibles
        LlenadoPaises();
        CancelaEdit();
    }//GEN-LAST:event_btnEditarPaisesActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Botón que consulta el registro selecionado de la tabla para editar
        if (tableListClientes.getSelectedRow() < 0)
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila.", "Información", 1);
        else{
            // Consulta los datos
            Object[] cliente_edit = cliente_dao.getClienteById((Integer) 
                    UtilsTable.obtenerValor(
                          tableListClientes, tableListClientes.getSelectedRow(), 0)); 
            if (cliente_edit != null){    
                cliente_id = (Integer) cliente_edit[0];                         // Id
                TextFieldNombre.setText(cliente_edit[1].toString());            // Nombre
                TextFieldApellidoPat.setText(cliente_edit[2].toString());       // Apellido Paterno
                TextFieldApellidoMat.setText(cliente_edit[3].toString());       // Apellido Materno
                TextFieldCorreo.setText(cliente_edit[4].toString());            // Correo
                cmbPais.setSelectedItem(cliente_edit[5].toString());            // Pais
                cmbEstado.setSelectedItem(cliente_edit[6].toString());          // Estado
                TextFieldAlcaldia.setText(cliente_edit[7].toString());          // Alcaldia
                TextFieldCP.setText(cliente_edit[8].toString());                // CP
                TextFieldCalle.setText(cliente_edit[9].toString());             // Calle
                TextFieldNoExterior.setText(cliente_edit[10].toString());       // Numero Exterior
                if(cliente_edit[11] != null) {
                    TextFieldNoInterior.setText(cliente_edit[11].toString());   // Numero Interior
                }                
                cmbEscolaridad.setSelectedItem(cliente_edit[12].toString());    // Estado                                
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        if (tableListClientes.getSelectedRow() < 0)
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila.", "Aviso", 2);
        else{
            // suena un beep
            java.awt.Toolkit.getDefaultToolkit().beep();
            // pregunta si quiere eliminar el registro y camtura la respuesta
            int res = javax.swing.JOptionPane.showConfirmDialog(this, "¿Eliminar " + clientes_lista[tableListClientes.getSelectedRow()][1].toString() + "?",
                 "Seleccione", JOptionPane.YES_NO_OPTION);
            // evalua la respuesta 
            if (res == 0){
                String msj = "";
                // si la respuesta es afirmativa, elimina el registro
                int ret = cliente_dao.DeleteCliente(cliente_id);
                if (ret == 0){
                    msj = "Se dio de baja el cliente.";
                }
                else if (ret == 1){
                    msj = "No se pudo dar de baja por que tiene registros asignados o\nno fue seleccionado apropiadamente.";
                }
                // suena un beep
                java.awt.Toolkit.getDefaultToolkit().beep();
                javax.swing.JOptionPane.showMessageDialog(this, msj, "Información", 1);
                // Reinicia controles y parametros                               
                CancelaEdit();
                LlenaTablaClientes(); 
                TextFieldNombre.requestFocus();
            }        
        }
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefrescarActionPerformed
        LlenaDatos();
        CancelaEdit();
    }//GEN-LAST:event_btnRefrescarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        CancelaEdit();
        tableListClientes.clearSelection();
        LlenaTablaClientes();
    }//GEN-LAST:event_btnCancelarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField TextFieldAlcaldia;
    private javax.swing.JTextField TextFieldApellidoMat;
    private javax.swing.JTextField TextFieldApellidoPat;
    private javax.swing.JTextField TextFieldCP;
    private javax.swing.JTextField TextFieldCalle;
    private javax.swing.JTextField TextFieldCorreo;
    private javax.swing.JTextField TextFieldFiltro;
    private javax.swing.JTextField TextFieldNoExterior;
    private javax.swing.JTextField TextFieldNoInterior;
    private javax.swing.JTextField TextFieldNombre;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnBorrarFiltro;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnEditEscolaridades;
    private javax.swing.JButton btnEditarPaises;
    private javax.swing.JButton btnGuarda;
    private javax.swing.JButton btnRefrescar;
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
    private javax.swing.JTable tableListClientes;
    // End of variables declaration//GEN-END:variables

    private void btnEditActionPerformed(Object object) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
