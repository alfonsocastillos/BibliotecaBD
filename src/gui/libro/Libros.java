package gui.libro;

import dataBase.ReporteLibrosPDF;
import dataBase.dao.AutorDAO;
import dataBase.dao.GeneroDAO;
import dataBase.dao.LibroDAO;
import dataBase.dao.IdiomaDAO;
import dataBase.dao.PaisDAO;
import dataBase.dao.EditorialDAO;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import tools.*;

/**
 *
 * @author Alfonso
 * Ventana que administra los libros
 */
public class Libros extends javax.swing.JInternalFrame {
    boolean editando = false;
    int libro_id;
    // Dao´s que traen los datos de la DB
    GeneroDAO genero_dao;
    IdiomaDAO idioma_dao;
    LibroDAO libro_dao;
    AutorDAO autor_dao;
    PaisDAO pais_dao;
    EditorialDAO editorial_dao;
    // Objtos que guardaran los datos    
    Object idiomas[][];
    Object generos[][];
    Object libros_lista[][];
    Object autores_lista[][];
    
    // Ventanas para agregar autores, editoriales y autorias
    AddAutores add_autor;           // Para agregar (o crear) autores a la autoria 
    AddIdioma add_idioma;           // Para crear, editar y eliminar idiomas
    AddPais add_pais;               // Para crear, editar y eliminar paises
    AddEditorial add_editorial;     // Para crear, editar y eliminar editoriales
    AddGenero add_genero;           // Para crear, editar y eliminar generos
    
    public Libros() {
        // Constructor
        initComponents();
        
        // Un 0 significa error en el id
        libro_id = 0;
        
        // se instancian todos los DAO
        genero_dao = new GeneroDAO();
        idioma_dao = new IdiomaDAO();
        libro_dao = new LibroDAO();
        autor_dao = new AutorDAO();
        pais_dao = new PaisDAO();
        editorial_dao = new EditorialDAO();
        
        // Instanciando ventanas para agregar autores, idiomas, paises, editoriales y generos
        add_autor = new AddAutores((java.awt.Frame)this.getParent(), true);
        add_editorial = new AddEditorial((java.awt.Frame)this.getParent(), true);
        add_idioma = new AddIdioma((java.awt.Frame)this.getParent(), true);
        add_pais = new AddPais((java.awt.Frame)this.getParent(), true);
        add_genero = new AddGenero((java.awt.Frame)this.getParent(), true);
        
        // localización de la ventana
        setLocale(null);
        // llena los datos      
        LlenaDatos();
        // Vacia las selecciones de campos
        CancelaEdit();
    }
    
    // Llena de datos las tablas a desplegar y los combo box
    private void LlenaDatos() {
        // Llena combos y tabla de libros
        LlenadoIdiomas();
        LlenadoPaises();
        LlenadoEditoriales();
        LlenadoGeneros();
        LlenaTablaLibros();
    }    
    
    // Popula el ComboBox de idiomas disponibles
    private void LlenadoIdiomas(){        
        cmbIdioma.removeAllItems();
        Object[][] idiomas = idioma_dao.GetAllIdiomas();
        for (Object[] idioma : idiomas) {
            // llena los datos de idioma en el combo 
            cmbIdioma.addItem(idioma[1].toString());
        }                             
    }   
    
    // Popula el ComboBox de paises disponibles
    private void LlenadoPaises() {        
        cmbPais.removeAllItems();
        Object[][] paises = pais_dao.GetAllPaises();
        for (Object[] pais : paises) {
            // llena los datos de idioma en el combo 
            cmbPais.addItem(pais[1].toString());
        }
    }
    
    // Popula el ComboBox de editoriales disponibles
    private void LlenadoEditoriales() {        
        cmbEditorial.removeAllItems();
        Object[][] editoriales = editorial_dao.GetAllEditoriales();
        for (Object[] editorial : editoriales) {
            // llena los datos de idioma en el combo 
            cmbEditorial.addItem(editorial[1].toString());
        }
    }
    
    // Popula el ComboBox de generos disponibles
    private void LlenadoGeneros() {        
        cmbGenero.removeAllItems();
        Object[][] generos = genero_dao.GetAllGeneros();
        for (Object[] genero : generos) {
            // llena los datos de idioma en el combo 
            cmbGenero.addItem(genero[1].toString());
        }
    }
    
    // Llena de datos la tabla que despliega todos los libros
    private void LlenaTablaLibros(){       
        // consulta los datos de las peliculas
        libros_lista = libro_dao.GetLibrosByDescripcion(txtFiltro.getText().trim()); // LIBRO_ID, TITULO, EDICION, EDITORIAL, GENERO, ANIO, NUM_PAGINAS, IDIOMA
        // configuración de la tabla
        String[] T_LIBROS = {"", "Libro", "Edicion", "Editorial", "Genero", "Año", "Páginas", "Idioma"};
        int[][] cellAlignment = {{0,javax.swing.SwingConstants.LEFT}};
        int[][] cellSize = {{0, 0},     // 
                            {1, 270},   // Libro
                            {2, 40},    // Edicion
                            {3, 180},   // Editorial
                            {4, 95},    // Genero
                            {5, 55},    // Año
                            {6, 55},    // Paginas
                            {7, 75}};   // Idioma    
        // pone los datos en la tabla
        UtilsTable.llenaTabla(tableList, libros_lista, T_LIBROS, cellAlignment, cellSize);
        UtilsTable.quitarColumna(tableList, 0);
        lblCantidad.setText(libros_lista.length + "");
    }
    
    // Llena la tabla de autores en la autoria de un libro
    private void LlenaTablaAutores(){       
        // consulta los datos de los autores de autoria
        autores_lista = autor_dao.GetAutoresByLibroId(libro_id);
        // configuración de la tabla
        String[] T_AUTORES = {"","Nombre"};
        int[][] cellAlignment = {{0,javax.swing.SwingConstants.LEFT}};
        int[][] cellSize = {{0,0},
                            {1,170}};
        // pone los datos en la tabla
        UtilsTable.llenaTabla(tableListAutor, autores_lista, T_AUTORES, cellAlignment, cellSize);        
    }
      
    // Valida si se llenaron los datos del libro antes de guardar
    private boolean EstanLlenos(){        
        if (txtTitulo.getText().trim().length() == 0){
            javax.swing.JOptionPane.showMessageDialog(this, "Introduzca el titulo.", "Aviso", 2);
            txtTitulo.requestFocus();
            return false;
        }
        else if (cmbIdioma.getSelectedIndex() < 0)
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un idioma.", "Aviso", 2);
            cmbIdioma.requestFocus();
            return false;
        }
        else if (cmbPais.getSelectedIndex() < 0)
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un pais.", "Aviso", 2);
            cmbPais.requestFocus();
            return false;
        }
        else if (cmbEditorial.getSelectedIndex() < 0)
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una editorial.", "Aviso", 2);
            cmbEditorial.requestFocus();
            return false;
        }
        else if (txtAnio.getText().trim().length() == 0){            
            javax.swing.JOptionPane.showMessageDialog(this, "Introduzca el año.", "Aviso", 2);            
            txtAnio.requestFocus();
            return false;
        }
        else if (cmbGenero.getSelectedIndex() < 0)
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un genero.", "Aviso", 2);
            cmbGenero.requestFocus();
            return false;
        }
        else if (txtEdicion.getText().trim().length() == 0){
            javax.swing.JOptionPane.showMessageDialog(this, "Introduzca el número de páginas.", "Aviso", 2);
            txtEdicion.requestFocus();
            return false;
        }
        else if (txtPags.getText().trim().length() == 0){
            javax.swing.JOptionPane.showMessageDialog(this, "Introduzca el número de páginas.", "Aviso", 2);
            txtPags.requestFocus();
            return false;
        }
        else
            return true;
    }
    
    // Cancela la edición y reinicia los controles a su valor predeterminado
    private void CancelaEdit (){        
        txtTitulo.setText("");
        cmbIdioma.setSelectedIndex(-1);
        cmbPais.setSelectedIndex(-1);
        cmbEditorial.setSelectedIndex(-1);
        cmbGenero.setSelectedIndex(-1);
        txtEdicion.setText("");
        txtAnio.setText("");
        cmbGenero.setSelectedIndex(-1);
        txtPags.setText("");
        editando = false;
        libro_id = 0;
        txtTitulo.requestFocus();
        btnCancela.setEnabled(false);        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlLibro = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        txtTitulo = new javax.swing.JTextField();
        lblEdicion = new javax.swing.JLabel();
        txtEdicion = new javax.swing.JTextField();
        lblGenero = new javax.swing.JLabel();
        cmbGenero = new javax.swing.JComboBox<>();
        lblClas = new javax.swing.JLabel();
        lblRenta = new javax.swing.JLabel();
        lblDiasR = new javax.swing.JLabel();
        lblGenero1 = new javax.swing.JLabel();
        cmbPais = new javax.swing.JComboBox<>();
        cmbIdioma = new javax.swing.JComboBox<>();
        cmbEditorial = new javax.swing.JComboBox<>();
        lblAnio = new javax.swing.JLabel();
        txtPags = new javax.swing.JTextField();
        txtAnio = new javax.swing.JTextField();
        pnlTableList = new javax.swing.JPanel();
        scpTableList = new javax.swing.JScrollPane();
        tableList = new javax.swing.JTable();
        lblTitulo1 = new javax.swing.JLabel();
        txtFiltro = new javax.swing.JTextField();
        btnBorrar = new javax.swing.JButton();
        btnReporte = new javax.swing.JButton();
        btnReportePDF = new javax.swing.JButton();
        btnBorrarFiltro = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lblCantidad = new javax.swing.JLabel();
        pnlTableList1 = new javax.swing.JPanel();
        scpTableList1 = new javax.swing.JScrollPane();
        tableListAutor = new javax.swing.JTable();
        btnAddAutoria = new javax.swing.JButton();
        btnRmveAutoria = new javax.swing.JButton();
        btnAddLibro = new javax.swing.JButton();
        btnGuarda = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnCancela = new javax.swing.JButton();
        pnlTableList3 = new javax.swing.JPanel();
        EditIdioma = new javax.swing.JButton();
        pnlTableList4 = new javax.swing.JPanel();
        EditPais = new javax.swing.JButton();
        pnlTableList5 = new javax.swing.JPanel();
        EditEditorial = new javax.swing.JButton();
        pnlTableList6 = new javax.swing.JPanel();
        EditGenero = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Libros");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/img/libros.png"))); // NOI18N
        setName(""); // NOI18N
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        pnlLibro.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Libro", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlLibro.setName(""); // NOI18N
        pnlLibro.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblTitulo.setText("Título:");
        pnlLibro.add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, 20));

        txtTitulo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlLibro.add(txtTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 290, 20));

        lblEdicion.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblEdicion.setText("Edicion:");
        pnlLibro.add(lblEdicion, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 110, -1, 20));

        txtEdicion.setDocument(new InputType(InputType.TIPO_SOLO_NUMEROS, 2, false, false));
        txtEdicion.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtEdicion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pnlLibro.add(txtEdicion, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 110, 130, 20));

        lblGenero.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblGenero.setText("Género:");
        pnlLibro.add(lblGenero, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 70, -1, 20));

        pnlLibro.add(cmbGenero, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 70, 230, -1));

        lblClas.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblClas.setText("No. páginas:");
        pnlLibro.add(lblClas, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 150, -1, 20));

        lblRenta.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblRenta.setText("País");
        pnlLibro.add(lblRenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, 20));

        lblDiasR.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblDiasR.setText("Editorial");
        pnlLibro.add(lblDiasR, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, -1, 20));

        lblGenero1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblGenero1.setText("Idioma:");
        pnlLibro.add(lblGenero1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, 20));

        cmbPais.setNextFocusableComponent(cmbGenero);
        cmbPais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPaisActionPerformed(evt);
            }
        });
        pnlLibro.add(cmbPais, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 110, 290, -1));

        cmbIdioma.setNextFocusableComponent(cmbGenero);
        cmbIdioma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbIdiomaActionPerformed(evt);
            }
        });
        pnlLibro.add(cmbIdioma, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 290, -1));

        cmbEditorial.setNextFocusableComponent(cmbGenero);
        cmbEditorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEditorialActionPerformed(evt);
            }
        });
        pnlLibro.add(cmbEditorial, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 150, 290, -1));

        lblAnio.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblAnio.setText("Año:");
        pnlLibro.add(lblAnio, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 30, -1, 20));

        txtPags.setDocument(new InputType(InputType.TIPO_SOLO_NUMEROS, 4, false, false));
        txtPags.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtPags.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pnlLibro.add(txtPags, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 150, 130, 20));

        txtAnio.setDocument(new InputType(InputType.TIPO_SOLO_NUMEROS, 4, false, false));
        txtAnio.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAnio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAnioActionPerformed(evt);
            }
        });
        pnlLibro.add(txtAnio, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, 130, 20));

        pnlTableList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lista de libros", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        pnlTableList.add(scpTableList, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 790, 160));

        lblTitulo1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblTitulo1.setText("Filtrar:");
        pnlTableList.add(lblTitulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, 25));

        txtFiltro.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltroActionPerformed(evt);
            }
        });
        txtFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroKeyReleased(evt);
            }
        });
        pnlTableList.add(txtFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 230, 25));

        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/borrar.png"))); // NOI18N
        btnBorrar.setToolTipText("Borrar libro");
        btnBorrar.setFocusable(false);
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });
        pnlTableList.add(btnBorrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 20, 40, 40));

        btnReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/reporte.png"))); // NOI18N
        btnReporte.setToolTipText("Mostrar reporte");
        btnReporte.setFocusable(false);
        btnReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteActionPerformed(evt);
            }
        });
        pnlTableList.add(btnReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 20, 40, 40));

        btnReportePDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/pdf-icon.png"))); // NOI18N
        btnReportePDF.setToolTipText("Mostrar en PDF");
        btnReportePDF.setFocusable(false);
        btnReportePDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportePDFActionPerformed(evt);
            }
        });
        pnlTableList.add(btnReportePDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 20, 40, 40));

        btnBorrarFiltro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/cancelarm.png"))); // NOI18N
        btnBorrarFiltro.setToolTipText("Borrar filtro");
        btnBorrarFiltro.setFocusable(false);
        btnBorrarFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarFiltroActionPerformed(evt);
            }
        });
        pnlTableList.add(btnBorrarFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 28, 30, 30));

        jLabel2.setText("Numero de libros en catálogo:");
        pnlTableList.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, -1, 30));
        jLabel2.getAccessibleContext().setAccessibleDescription("");

        lblCantidad.setText("000");
        pnlTableList.add(lblCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 230, -1, 30));

        pnlTableList1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Autoría", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scpTableList1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        tableListAutor.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tableListAutor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scpTableList1.setViewportView(tableListAutor);

        pnlTableList1.add(scpTableList1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 170, 100));

        btnAddAutoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/add.png"))); // NOI18N
        btnAddAutoria.setToolTipText("Agregar autor");
        btnAddAutoria.setFocusable(false);
        btnAddAutoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddAutoriaActionPerformed(evt);
            }
        });
        pnlTableList1.add(btnAddAutoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 30, 30));

        btnRmveAutoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/quitar.png"))); // NOI18N
        btnRmveAutoria.setToolTipText("Borrar autor");
        btnRmveAutoria.setFocusable(false);
        btnRmveAutoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRmveAutoriaActionPerformed(evt);
            }
        });
        pnlTableList1.add(btnRmveAutoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 30, 30));

        btnAddLibro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/add_libro.png"))); // NOI18N
        btnAddLibro.setToolTipText("Nuevo");
        btnAddLibro.setFocusable(false);
        btnAddLibro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAddLibroMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAddLibroMouseExited(evt);
            }
        });
        btnAddLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddLibroActionPerformed(evt);
            }
        });

        btnGuarda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/guardar.png"))); // NOI18N
        btnGuarda.setToolTipText("Guardar");
        btnGuarda.setFocusable(false);
        btnGuarda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardaActionPerformed(evt);
            }
        });

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/editar.png"))); // NOI18N
        btnEdit.setToolTipText("Editar");
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnCancela.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/cancelar.png"))); // NOI18N
        btnCancela.setToolTipText("Cancelar");
        btnCancela.setEnabled(false);
        btnCancela.setFocusable(false);
        btnCancela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelaActionPerformed(evt);
            }
        });

        pnlTableList3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Idiomas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        EditIdioma.setText("Editar idiomas");
        EditIdioma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditIdiomaActionPerformed(evt);
            }
        });
        pnlTableList3.add(EditIdioma, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 140, -1));

        pnlTableList4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Países", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        EditPais.setText("Editar países");
        EditPais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditPaisActionPerformed(evt);
            }
        });
        pnlTableList4.add(EditPais, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 140, -1));

        pnlTableList5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editoriales", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        EditEditorial.setText("Editar editoriales");
        EditEditorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditEditorialActionPerformed(evt);
            }
        });
        pnlTableList5.add(EditEditorial, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        pnlTableList6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Generos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        EditGenero.setText("Editar editoriales");
        EditGenero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditGeneroActionPerformed(evt);
            }
        });
        pnlTableList6.add(EditGenero, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/refresh.png"))); // NOI18N
        btnRefresh.setToolTipText("Actualizar");
        btnRefresh.setFocusable(false);
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlTableList, javax.swing.GroupLayout.DEFAULT_SIZE, 817, Short.MAX_VALUE)
                            .addComponent(pnlLibro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlTableList1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlTableList6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlTableList5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlTableList4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlTableList3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAddLibro, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGuarda, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(btnCancela, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddLibro, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuarda, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancela, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlTableList3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlTableList4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlTableList5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlLibro, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 54, Short.MAX_VALUE)
                        .addComponent(pnlTableList, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 34, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlTableList6, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(pnlTableList1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardaActionPerformed
        //  Guarda y actualiza los datos        
        if (EstanLlenos()){
            Object idioma_id = idioma_dao.GetIdiomasByNombre(cmbIdioma.getSelectedItem().toString())[0][0];
            Object genero_id = genero_dao.GetGenerosByNombre(cmbGenero.getSelectedItem().toString())[0][0];
            Object pais_id = pais_dao.GetPaisesByNombre(cmbPais.getSelectedItem().toString())[0][0];
            Object editorial_id = editorial_dao.GetEditorialByNombre(cmbEditorial.getSelectedItem().toString())[0][0];
            
            // guarda los valores de los controles en un arreglo de objetos 
            Object [] libro = new Object[9];                            // (TITULO, EDICION, ANIO, NUM_PAGS, IDIOMA_ID, GENERO_ID, PAIS_ID, EDITORIAL_ID)
            libro[0] = libro_id;
            libro[1] = txtTitulo.getText().trim();                      // Titulo
            libro[2] = Integer.valueOf(txtEdicion.getText().trim());    // Edicion 
            libro[3] = Integer.valueOf(txtAnio.getText().trim());       // Año
            libro[4] = Integer.valueOf(txtPags.getText().trim());       // No. paginas
            libro[5] = (Integer) idioma_id;                             // Id idioma
            libro[6] = (Integer) genero_id;                             // Id genero
            libro[7] = (Integer) pais_id;                               // Id pais
            libro[8] = editorial_id.toString();                         // Id editorial
                        
            if (editando){  // Si está actualizando
                libro[0] = libro_id;
                // Actualiza los datos y regresa el id
                libro_id = libro_dao.UpdateLibro(libro);
                if (libro_id != 0) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Datos actualizados.", "Información", 1);
                }                
            }            
            else{   // si está guardando un registro nuevo
                // Guarda los datos  y regresa el id
                libro_id = libro_dao.SaveLibro(libro);
            }
            
            // Si el id no es 0, procede a llenar los demas datos
            if (libro_id != 0){                
                javax.swing.JOptionPane.showMessageDialog(this, "Datos guardados con éxito.", "Información", 1);
                LlenaTablaLibros();
                UtilsTable.mueveTabla(tableList, UtilsTable.getRow(libros_lista, libro_id));
                editando = true;
                btnCancela.setEnabled(true);
                txtTitulo.requestFocus();
                CancelaEdit();
            }
            else
                javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar.","Error",1);
        }
    }//GEN-LAST:event_btnGuardaActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Botón que consulta el registro selecionado de la tabla para editar
        if (tableList.getSelectedRow() < 0)
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila.", "Información", 1);
        else{
            // Consulta los datos
            Object[] libro_edit = libro_dao.GetLibroById((Integer) 
                    UtilsTable.obtenerValor(
                          tableList, tableList.getSelectedRow(), 0)); 
            if (libro_edit != null){
                // LIBRO_ID, TITULO, EDICION, EDITORIAL, GENERO, ANIO, NUM_PAGINAS, IDIOMA, PAIS
                txtTitulo.setText(libro_edit[1].toString());            // Titulo
                txtEdicion.setText(libro_edit[2].toString());           // Edicion
                cmbEditorial.setSelectedItem(libro_edit[3].toString()); // Editorial
                cmbGenero.setSelectedItem(libro_edit[4].toString());    // Genero
                txtAnio.setText(libro_edit[5].toString());              // Año
                txtPags.setText(libro_edit[6].toString());              // No. pags
                cmbIdioma.setSelectedItem(libro_edit[7].toString());    // Idioma
                cmbPais.setSelectedItem(libro_edit[8].toString());      // Pais
                libro_id = (Integer) libro_edit[0];                     // Id
                
                // llena las tablas de autoria 
                LlenaTablaAutores();
                
                // indica que está editando
                editando = true;
                btnCancela.setEnabled(true);
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed

    // Cancela la edición y reinicia controles y parametros
    private void btnCancelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelaActionPerformed
        CancelaEdit();
        tableList.clearSelection();
        LlenaTablaAutores();
    }//GEN-LAST:event_btnCancelaActionPerformed

    // Borra el libro seleccionado
    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        if (tableList.getSelectedRow() < 0)
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila.", "Aviso", 2);
        else{
            // suena un beep
            java.awt.Toolkit.getDefaultToolkit().beep();
            // pregunta si quiere eliminar el registro y camtura la respuesta
            int res = javax.swing.JOptionPane.showConfirmDialog(this, "¿Eliminar " + libros_lista[tableList.getSelectedRow()][1].toString() + "?",
                 "Seleccione", JOptionPane.YES_NO_OPTION);
            // evalua la respuesta 
            if (res == 0){
                String msj = "";
                // si la respuesta es afirmativa, elimina el registro
                int ret = libro_dao.DeleteLibro(libro_id);
                if (ret == 0){
                    msj = "Se eliminó el libro.";
                }
                else if (ret == 1){
                    msj = "No se pudo eliminar por que tiene registros asignados o\nno fue seleccionado apropiadamente.";
                }
                // suena un beep
                java.awt.Toolkit.getDefaultToolkit().beep();
                javax.swing.JOptionPane.showMessageDialog(this, msj, "Información", 1);
                // Reinicia controles y parametros
                LlenaTablaLibros();
                LlenaTablaAutores();                
                CancelaEdit();
                txtTitulo.requestFocus();
            }        
        }
    }//GEN-LAST:event_btnBorrarActionPerformed

    // Limpia los campos del libro para poder escribir
    private void btnAddLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddLibroActionPerformed
        CancelaEdit();
        tableList.clearSelection();
        LlenaTablaAutores();        
    }//GEN-LAST:event_btnAddLibroActionPerformed

    // Llena los campos con el libro seleccionado
    private void tableListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListMouseClicked
        // Muestra un ballon si es necesarios
        if (evt.getClickCount() == 2)
            btnEditActionPerformed(null);
    }//GEN-LAST:event_tableListMouseClicked

    // Abre una ventana con el reporte de los libros dentro de la app
    private void btnReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteActionPerformed
        // genera reporte de peliculas
        VentanaReporteLibros reporte_libros = new VentanaReporteLibros();
        reporte_libros.setVisible(true);
    }//GEN-LAST:event_btnReporteActionPerformed

    // Crea un docuemento PDF con el reporte de los libros en el lector PDF
    private void btnReportePDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportePDFActionPerformed
        // genera y ejecuta el reporte en PDF
        ReporteLibrosPDF reporte = new ReporteLibrosPDF();
        reporte.genReport();
    }//GEN-LAST:event_btnReportePDFActionPerformed
    
    // Llena la tabla de libros cada que se presiona una tecla en el filtro
    private void txtFiltroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyReleased
        LlenaTablaLibros();
    }//GEN-LAST:event_txtFiltroKeyReleased

    // Borra el texto del filtro y llena la tabla
    private void btnBorrarFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarFiltroActionPerformed
        txtFiltro.setText("");
        LlenaTablaLibros();
    }//GEN-LAST:event_btnBorrarFiltroActionPerformed

    // Despliega un texto cuando se pone el mouse encima del botón
    private void btnAddLibroMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddLibroMouseEntered
        // Tool tip text de la librería ballontip
        UtilsGUI.showBallonAviso(btnAddLibro, "Agregar nuevo libro.", false);
    }//GEN-LAST:event_btnAddLibroMouseEntered

    // Oculta el texto cuando el mouse deja de estar encima
    private void btnAddLibroMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddLibroMouseExited
        // Quitar ballontip
        UtilsGUI.hideBallonTxt();
    }//GEN-LAST:event_btnAddLibroMouseExited

    // Cancela edición, reinicia los parámetros y limpia al cerrar la ventana
    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        CancelaEdit();
        tableList.clearSelection();
        txtFiltro.setText("");
        LlenaTablaLibros();
        LlenaTablaAutores();               
    }//GEN-LAST:event_formInternalFrameClosing
    
    // No se usa
    private void cmbIdiomaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbIdiomaActionPerformed
        
    }//GEN-LAST:event_cmbIdiomaActionPerformed

    // Borra a un autor de un libro
    private void btnRmveAutoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRmveAutoriaActionPerformed
        // Verifica si se selecciono un elemento de la tabla
        if (tableListAutor.getSelectedRow() < 0){
            // Suena un beep
            Toolkit.getDefaultToolkit().beep();
            // Muestra un mensage de aviso
            JOptionPane.showMessageDialog(this, "Seleccione un autor.", "Aviso", 2);
        }
        else{
            // suena un beep
            java.awt.Toolkit.getDefaultToolkit().beep();
            // pregunta si quiere eliminar el registro y camtura la respuesta
            int res = javax.swing.JOptionPane.showConfirmDialog(this, "¿Eliminar " + autores_lista[tableListAutor.getSelectedRow()][1].toString() + "?",
                "Seleccione", JOptionPane.YES_NO_OPTION);
            // evalua la respuesta
            if (res == 0){
                String msj = "";
                // si la respuesta es afirmativa, elimina el registro
                String autor_id = tableListAutor.getValueAt(tableListAutor.getSelectedRow(), 0).toString();
                int ret = autor_dao.DeleteAutoria(autor_id, libro_id);
                if (ret == 1){
                    msj = "No se pudo eliminar por que tiene egistros asignados.";
                    // suena un beep
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    javax.swing.JOptionPane.showMessageDialog(this, msj, "Información", 1);
                }

                // Reinicia controles y parametros
                LlenaTablaAutores();
            }
        }
    }//GEN-LAST:event_btnRmveAutoriaActionPerformed

    // Abre una ventana para agregar a un autor a la autoria de un libro
    private void btnAddAutoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddAutoriaActionPerformed
        // Agregar casting
        // Verifica si esta editando una pelicula
        if (!editando){
            // Suena un beep
            Toolkit.getDefaultToolkit().beep();
            // Muestra un mensage de aviso
            JOptionPane.showMessageDialog(this, "Para agregar autores primero guarde\nNo edite un libro.", "Aviso", 2);
        }
        else{   // Abre la ventana para agregar autores            
            // Asigna el id de la pelicula            
            add_autor.SetLibroId(libro_id);                                   
            // Localización de la ventana
            add_autor.setLocationRelativeTo(this);
            // hace visible la ventana
            add_autor.setVisible(true);
            // cuando cierra la ventana agrega el autor seleccionada a la tabla de autores
            LlenaTablaAutores();
        }
    }//GEN-LAST:event_btnAddAutoriaActionPerformed

    // Recarga todos los combo box para mostrar informacion actualizada
    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        LlenaDatos();
        CancelaEdit();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void EditPaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditPaisActionPerformed
        // Hace visible la ventana para agregar paises
        add_pais.setLocationRelativeTo(this);
        add_pais.setVisible(true);
        
        // Al cerrar, actualizar los paises disponibles
        LlenadoPaises();
        CancelaEdit();
    }//GEN-LAST:event_EditPaisActionPerformed

    private void EditEditorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditEditorialActionPerformed
        // Hace visible la ventana para agregar editoriales
        add_editorial.setLocationRelativeTo(this);
        add_editorial.setVisible(true);
        
        // Al cerrar, actualizar las editoriales disponibles
        LlenadoEditoriales();
        CancelaEdit();
    }//GEN-LAST:event_EditEditorialActionPerformed

    private void EditGeneroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditGeneroActionPerformed
        // Hace visible la ventana para agregar generos
        add_genero.setLocationRelativeTo(this);
        add_genero.setVisible(true);
        
        // Al cerrar, actualizar los generos disponibles
        LlenadoGeneros();
        CancelaEdit();
    }//GEN-LAST:event_EditGeneroActionPerformed

    private void txtAnioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAnioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAnioActionPerformed

    private void EditIdiomaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditIdiomaActionPerformed
        // Hace visible la ventana para agregar idiomas
        add_idioma.setLocationRelativeTo(this);
        add_idioma.setVisible(true);

        // Al cerrar, actualizar los idiomas disponibles
        LlenadoIdiomas();
        CancelaEdit();
    }//GEN-LAST:event_EditIdiomaActionPerformed

    private void cmbPaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPaisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPaisActionPerformed

    private void cmbEditorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEditorialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbEditorialActionPerformed

    private void txtFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton EditEditorial;
    private javax.swing.JButton EditGenero;
    private javax.swing.JButton EditIdioma;
    private javax.swing.JButton EditPais;
    private javax.swing.JButton btnAddAutoria;
    private javax.swing.JButton btnAddLibro;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnBorrarFiltro;
    private javax.swing.JButton btnCancela;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnGuarda;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnReporte;
    private javax.swing.JButton btnReportePDF;
    private javax.swing.JButton btnRmveAutoria;
    private javax.swing.JComboBox<String> cmbEditorial;
    private javax.swing.JComboBox<String> cmbGenero;
    private javax.swing.JComboBox<String> cmbIdioma;
    private javax.swing.JComboBox<String> cmbPais;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblAnio;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblClas;
    private javax.swing.JLabel lblDiasR;
    private javax.swing.JLabel lblEdicion;
    private javax.swing.JLabel lblGenero;
    private javax.swing.JLabel lblGenero1;
    private javax.swing.JLabel lblRenta;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTitulo1;
    private javax.swing.JPanel pnlLibro;
    private javax.swing.JPanel pnlTableList;
    private javax.swing.JPanel pnlTableList1;
    private javax.swing.JPanel pnlTableList3;
    private javax.swing.JPanel pnlTableList4;
    private javax.swing.JPanel pnlTableList5;
    private javax.swing.JPanel pnlTableList6;
    private javax.swing.JScrollPane scpTableList;
    private javax.swing.JScrollPane scpTableList1;
    private javax.swing.JTable tableList;
    private javax.swing.JTable tableListAutor;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JTextField txtEdicion;
    private javax.swing.JTextField txtFiltro;
    private javax.swing.JTextField txtPags;
    private javax.swing.JTextField txtTitulo;
    // End of variables declaration//GEN-END:variables
}