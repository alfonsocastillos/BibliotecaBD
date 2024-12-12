package gui.libro;

import dataBase.LibrosReportPDF;
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
 * Ventana que administra los libros.
 * @author alfonso
 */
public class Libros extends javax.swing.JInternalFrame {
    boolean editando = false;
    int libroId;
    
    // Daos que traen los datos de la DB.
    GeneroDAO generoDAO;
    IdiomaDAO idiomaDAO;
    LibroDAO libroDAO;
    AutorDAO autorDAO;
    PaisDAO paisDAO;
    EditorialDAO editorialDAO;
    
    // Objtos que guardaran los datos.
    Object[][] listaLibros;
    Object[][] autoresLista;
    
    // Ventanas para agregar Autores, Editoriales, Idiomas, Generos y Autorias
    AddAutores addAutor;        // Para agregar (o crear) Autores a la Autoria 
    AddIdioma addIdioma;        // Para crear, editar y eliminar Idiomas
    AddPais addPais;            // Para crear, editar y eliminar Paises
    AddEditorial addEditorial;  // Para crear, editar y eliminar Editoriales
    AddGenero addGenero;        // Para crear, editar y eliminar Generos
    
    /**
     * Constructor.
     */
    public Libros() {
        initComponents();
        libroId = 0;
        
        // Se instancian todos los DAO.
        generoDAO = new GeneroDAO();
        idiomaDAO = new IdiomaDAO();
        libroDAO = new LibroDAO();
        autorDAO = new AutorDAO();
        paisDAO = new PaisDAO();
        editorialDAO = new EditorialDAO();
        
        // Instanciando ventanas para agregar autores, idiomas, paises, editoriales y generos.
        addAutor = new AddAutores((java.awt.Frame)this.getParent(), true);
        addEditorial = new AddEditorial((java.awt.Frame)this.getParent(), true);
        addIdioma = new AddIdioma((java.awt.Frame)this.getParent(), true);
        addPais = new AddPais((java.awt.Frame)this.getParent(), true);
        addGenero = new AddGenero((java.awt.Frame)this.getParent(), true);
        
        setLocale(null);
        llenaDatos();
        cancelaEdit();
    }
    
    /**
     * Llena de datos las tablas a desplegar y los combobox.
     */
    private void llenaDatos() {
        llenadoIdiomas();
        llenadoPaises();
        llenadoEditoriales();
        LlenadoGeneros();
        llenaTablaLibros();
    }    
    
    /**
     * Popula el ComboBox de Idiomas disponibles.
     */
    private void llenadoIdiomas() {        
        cmbIdioma.removeAllItems();
        Object[][] idiomas = idiomaDAO.getAllIdiomas();
        for(Object[] idioma : idiomas) {
            
            // Llena los datos de Idioma en el combo.
            cmbIdioma.addItem(idioma[1].toString());
        }                             
    }   
    
    /**
     * Popula el ComboBox de paises disponibles.
     */
    private void llenadoPaises() {        
        cmbPais.removeAllItems();
        Object[][] paises = paisDAO.getAllPaises();
        for(Object[] pais : paises) {
            
            // Llena los datos de Paises en el combo.
            cmbPais.addItem(pais[1].toString());
        }
    }
    
    /**
     * Popula el ComboBox de Editoriales disponibles.
     */
    private void llenadoEditoriales() {
        cmbEditorial.removeAllItems();
        Object[][] editoriales = editorialDAO.getAllEditoriales();
        for(Object[] editorial : editoriales) {
            
            // Llena los datos de Editoriales en el combo.
            cmbEditorial.addItem(editorial[1].toString());
        }
    }
    
    /**
     * Popula el ComboBox de Generos disponibles.
     */
    private void LlenadoGeneros() {        
        cmbGenero.removeAllItems();
        Object[][] generos = generoDAO.getAllGeneros();
        for(Object[] genero : generos) {
            
            // llena los datos de Generos en el combo.
            cmbGenero.addItem(genero[1].toString());
        }
    }
    
    /**
     * Llena de datos la tabla que despliega todos los Libros.
     */
    private void llenaTablaLibros() {       
        listaLibros = libroDAO.getLibrosByDescripcion(txtFiltro.getText().trim());
        
        // Configuración de la tabla
        String[] columnasNombre = {"", "Libro", "Edicion", "Editorial", "Genero", "Año", "Páginas", "Idioma"};
        int[][] cellAlignment = {{0, javax.swing.SwingConstants.LEFT}};
        int[][] cellSize = {{0, 0},
                {1, 270},           // Libro
                {2, 40},            // Edicion
                {3, 180},           // Editorial
                {4, 95},            // Genero
                {5, 55},            // Año
                {6, 55},            // Paginas
                {7, 75}};           // Idioma  
        
        // Pone los datos en la tabla y quita la columna vacia del Id.
        UtilsTable.llenaTabla(cellAlignment, cellSize, columnasNombre, tblLibros, listaLibros);
        UtilsTable.quitarColumna(0, tblLibros);
        lblCantidad.setText(listaLibros.length + "");
    }
    
    /**
     * Llena la tabla de Autores en la Autoria de un Libro.
     */
    private void llenaTablaAutores() {
        autoresLista = autorDAO.getAutoresByLibroId(libroId);
        
        // Configuración de la tabla.
        String[] columnasNombre = {"", "Nombre"};
        int[][] cellAlignment = {{0, javax.swing.SwingConstants.LEFT}};
        int[][] cellSize = {{0, 0}, {1, 170}};
        UtilsTable.llenaTabla(cellAlignment, cellSize, columnasNombre, tblAutores, autoresLista);  
    }
      
    /**
     * Valida si se llenaron los datos del Libro antes de guardar.
     * @return boolean indicando si todos los datos estan llenos.
     */
    private boolean estanLlenos() {
        boolean llenos = true;
        if(txtTitulo.getText().trim().length() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Introduzca el titulo.", "Aviso", 2);
            txtTitulo.requestFocus();
            llenos = false;
        } else if(cmbIdioma.getSelectedIndex() < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un idioma.", "Aviso", 2);
            cmbIdioma.requestFocus();
            llenos = false;
        } else if(cmbPais.getSelectedIndex() < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un pais.", "Aviso", 2);
            cmbPais.requestFocus();
            llenos = false;
        } else if(cmbEditorial.getSelectedIndex() < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una editorial.", "Aviso", 2);
            cmbEditorial.requestFocus();
            llenos = false;
        } else if(txtAnio.getText().trim().length() == 0) {            
            javax.swing.JOptionPane.showMessageDialog(this, "Introduzca el año.", "Aviso", 2);            
            txtAnio.requestFocus();
            llenos = false;
        } else if(cmbGenero.getSelectedIndex() < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un genero.", "Aviso", 2);
            cmbGenero.requestFocus();
            llenos = false;
        } else if(txtEdicion.getText().trim().length() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Introduzca el número de páginas.", "Aviso", 2);
            txtEdicion.requestFocus();
            llenos = false;
        } else if(txtPags.getText().trim().length() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Introduzca el número de páginas.", "Aviso", 2);
            txtPags.requestFocus();
            llenos = false;
        }        
        return llenos;
    }
    
    /**
     * Cancela la edición y reinicia los controles a su valor predeterminado.
     */
    private void cancelaEdit() {
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
        libroId = 0;
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
        tblLibros = new javax.swing.JTable();
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
        tblAutores = new javax.swing.JTable();
        btnAddAutoria = new javax.swing.JButton();
        btnRmveAutoria = new javax.swing.JButton();
        btnAddLibro = new javax.swing.JButton();
        btnGuarda = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnCancela = new javax.swing.JButton();
        pnlTableList3 = new javax.swing.JPanel();
        btnEditIdioma = new javax.swing.JButton();
        pnlTableList4 = new javax.swing.JPanel();
        btnEditPais = new javax.swing.JButton();
        pnlTableList5 = new javax.swing.JPanel();
        btnEditEditorial = new javax.swing.JButton();
        pnlTableList6 = new javax.swing.JPanel();
        btnEditGenero = new javax.swing.JButton();
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
        pnlLibro.add(cmbPais, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 110, 290, -1));

        cmbIdioma.setNextFocusableComponent(cmbGenero);
        pnlLibro.add(cmbIdioma, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 290, -1));

        cmbEditorial.setNextFocusableComponent(cmbGenero);
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
        pnlLibro.add(txtAnio, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, 130, 20));

        pnlTableList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lista de libros", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scpTableList.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scpTableList.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblLibros.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tblLibros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblLibros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLibrosMouseClicked(evt);
            }
        });
        scpTableList.setViewportView(tblLibros);

        pnlTableList.add(scpTableList, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 790, 160));

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

        tblAutores.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tblAutores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scpTableList1.setViewportView(tblAutores);

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

        btnEditIdioma.setText("Editar idiomas");
        btnEditIdioma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditIdiomaActionPerformed(evt);
            }
        });
        pnlTableList3.add(btnEditIdioma, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 140, -1));

        pnlTableList4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Países", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnEditPais.setText("Editar países");
        btnEditPais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditPaisActionPerformed(evt);
            }
        });
        pnlTableList4.add(btnEditPais, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 140, -1));

        pnlTableList5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editoriales", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnEditEditorial.setText("Editar editoriales");
        btnEditEditorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditEditorialActionPerformed(evt);
            }
        });
        pnlTableList5.add(btnEditEditorial, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        pnlTableList6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Generos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnEditGenero.setText("Editar editoriales");
        btnEditGenero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditGeneroActionPerformed(evt);
            }
        });
        pnlTableList6.add(btnEditGenero, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

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

    /**
     * Abre una ventana para poder guardar o actualizar un Libro.
     * @param evt evento que dispara la funcion.
     */
    private void btnGuardaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardaActionPerformed
        
        //  Guarda o actualiza los datos.        
        if(estanLlenos()) {
            int idiomaId = (int) idiomaDAO.getIdiomasByNombre(cmbIdioma.getSelectedItem().toString())[0][0];
            int generoId = (int) generoDAO.getGenerosByNombre(cmbGenero.getSelectedItem().toString())[0][0];
            int paisId = (int) paisDAO.getPaisesByNombre(cmbPais.getSelectedItem().toString())[0][0];
            String editorialId = editorialDAO.getEditorialByNombre(cmbEditorial.getSelectedItem().toString())[0][0].toString();
            
            // guarda los valores de los controles en un arreglo de objetos 
            Object[] libro = new Object[9];                             // (TITULO, EDICION, ANIO, NUM_PAGS, IDIOMA_ID, GENERO_ID, PAIS_ID, EDITORIAL_ID)
            libro[0] = libroId;                                         // Id del libro.
            libro[1] = txtTitulo.getText().trim();                      // Titulo.
            libro[2] = Integer.valueOf(txtEdicion.getText().trim());    // Edicion. 
            libro[3] = Integer.valueOf(txtAnio.getText().trim());       // Anio.
            libro[4] = Integer.valueOf(txtPags.getText().trim());       // No. paginas.
            libro[5] = idiomaId;                                        // Id idioma.
            libro[6] = generoId;                                        // Id genero.
            libro[7] = paisId;                                          // Id pais.
            libro[8] = editorialId;                                     // Id editorial.
                        
            // Actualizando o creando.
            if(editando) {
                
                // Actualiza los datos y regresa el Id.
                libroId = libroDAO.updateLibro(libro);
                if(libroId != 0) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Datos actualizados.", "Información", 1);
                }                
            } else {
                
                // Guarda los datos  y regresa el Id.
                libroId = libroDAO.saveLibro(libro);
            }
            
            // Si el Id no es 0, procede a llenar los demas datos.
            if(libroId != 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "Datos guardados con éxito.", "Información", 1);
                llenaTablaLibros();
                UtilsTable.mueveTabla(UtilsTable.getRow(libroId, listaLibros), tblLibros);
                editando = true;
                btnCancela.setEnabled(true);
                txtTitulo.requestFocus();
                cancelaEdit();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar.", "Error", 1);
            }                
        }
    }//GEN-LAST:event_btnGuardaActionPerformed

    /**
     * Botón que consulta el registro selecionado de la tabla para editar.
     * @param evt evento que dispara la funcion.
     */
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if(tblLibros.getSelectedRow() < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila.", "Información", 1);
        } else {
            
            // Consulta los datos.
            Object[] libroEdit = libroDAO.getLibroById((int) UtilsTable.obtenerValor(tblLibros.getSelectedRow(), 0, tblLibros)); 
            if(libroEdit != null) {
                txtTitulo.setText(libroEdit[1].toString());            // Titulo.
                txtEdicion.setText(libroEdit[2].toString());           // Edicion.
                cmbEditorial.setSelectedItem(libroEdit[3].toString()); // Editorial.
                cmbGenero.setSelectedItem(libroEdit[4].toString());    // Genero.
                txtAnio.setText(libroEdit[5].toString());              // Anio.
                txtPags.setText(libroEdit[6].toString());              // No. pags.
                cmbIdioma.setSelectedItem(libroEdit[7].toString());    // Idioma.
                cmbPais.setSelectedItem(libroEdit[8].toString());      // Pais.
                libroId = (int) libroEdit[0];                     // Id.
                
                // Llena las tablas de autoria.
                llenaTablaAutores();
                
                // Indica que está editando.
                editando = true;
                btnCancela.setEnabled(true);
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed

    /**
     * Cancela la edición y reinicia controles y parametros.
     * @param evt evento que dispara la funcion.
     */
    private void btnCancelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelaActionPerformed
        cancelaEdit();
        tblLibros.clearSelection();
        llenaTablaAutores();
    }//GEN-LAST:event_btnCancelaActionPerformed

    /**
     * Borra el libro seleccionado.
     * @param evt evento que dispara la funcion.
     */
    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        if(tblLibros.getSelectedRow() < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila.", "Aviso", 2);
        } else {
          
            // Suena un beep y muestra un mensaje de confirmacion.
            java.awt.Toolkit.getDefaultToolkit().beep();
            int res = javax.swing.JOptionPane.showConfirmDialog(this, "¿Eliminar " + listaLibros[tblLibros.getSelectedRow()][1].toString() + "?",
                "Seleccione", JOptionPane.YES_NO_OPTION);
            
            // Si la respuesta es afirmativa, elimina el registro
            if(res == 0) {
                String msj = "";      
                
                libroId = (int) UtilsTable.obtenerValor(tblLibros.getSelectedRow(), 0, tblLibros);
                int ret = libroDAO.deleteLibro(libroId);
                if(ret == 1) {
                    msj = "Se eliminó el libro.";
                } else if(ret == 0) {
                    msj = "No se pudo eliminar por que tiene registros asignados o\nno fue seleccionado apropiadamente.";
                }
                
                // Suena un beep y muestra un mensaje.
                java.awt.Toolkit.getDefaultToolkit().beep();
                javax.swing.JOptionPane.showMessageDialog(this, msj, "Información", 1);
                
                // Reinicia controles y parametros.
                llenaTablaLibros();
                llenaTablaAutores();                
                cancelaEdit();
                txtTitulo.requestFocus();
            }        
        }
    }//GEN-LAST:event_btnBorrarActionPerformed

    /**
     * Limpia los campos del libro para poder escribir.
     * @param evt evento que dispara la funcion.
     */
    private void btnAddLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddLibroActionPerformed
        cancelaEdit();
        tblLibros.clearSelection();
        llenaTablaAutores();        
    }//GEN-LAST:event_btnAddLibroActionPerformed

    /**
     * Llena los campos con el Libro seleccionado.
     * @param evt evento que dispara la funcion.
     */
    private void tblLibrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLibrosMouseClicked
        // Muestra un ballon si es necesarios
        if(evt.getClickCount() == 2) {
            btnEditActionPerformed(null);
        }
    }//GEN-LAST:event_tblLibrosMouseClicked

    /**
     * Abre una ventana con el reporte de los Libros dentro de la app.
     * @param evt evento que dispara la funcion.
     */
    private void btnReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteActionPerformed
        VentanaReporteLibros reporteLibros = new VentanaReporteLibros();
        reporteLibros.setVisible(true);
    }//GEN-LAST:event_btnReporteActionPerformed

    /**
     * Crea un docuemento PDF con el reporte de los Libros en el lector PDF.
     * @param evt evento que dispara la funcion.
     */
    private void btnReportePDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportePDFActionPerformed
        LibrosReportPDF reporte = new LibrosReportPDF();
        reporte.generateReport();
    }//GEN-LAST:event_btnReportePDFActionPerformed
    
    /**
     * Llena la tabla de Libros cada que se presiona una tecla en el filtro.
     * @param evt evento que dispara la funcion.
     */
    private void txtFiltroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyReleased
        llenaTablaLibros();
    }//GEN-LAST:event_txtFiltroKeyReleased

    /**
     * Borra el texto del filtro y llena la tabla.
     * @param evt evento que dispara la funcion.
     */
    private void btnBorrarFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarFiltroActionPerformed
        txtFiltro.setText("");
        llenaTablaLibros();
    }//GEN-LAST:event_btnBorrarFiltroActionPerformed

    /**
     * Despliega un texto cuando se pone el mouse encima del botón.
     * @param evt evento que dispara la funcion.
     */
    private void btnAddLibroMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddLibroMouseEntered
        UtilsGUI.showBallonAviso(btnAddLibro, "Agregar nuevo libro.", false);
    }//GEN-LAST:event_btnAddLibroMouseEntered

    /**
     * Oculta el texto cuando el mouse deja de estar encima.
     * @param evt evento que dispara la funcion.
     */
    private void btnAddLibroMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddLibroMouseExited
        UtilsGUI.hideBallonTxt();
    }//GEN-LAST:event_btnAddLibroMouseExited

    /**
     * Cancela edición, reinicia los parámetros y limpia al cerrar la ventana.
     * @param evt evento que dispara la funcion.
     */
    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        cancelaEdit();
        tblLibros.clearSelection();
        txtFiltro.setText("");
        llenaTablaLibros();
        llenaTablaAutores();               
    }//GEN-LAST:event_formInternalFrameClosing
    
    /**
     * Borra a un Autor de un Libro.
     * @param evt evento que dispara la funcion.
     */
    private void btnRmveAutoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRmveAutoriaActionPerformed
        
        // Verifica si se selecciono un elemento de la tabla.
        if(tblAutores.getSelectedRow() < 0) {
            // Suena un beep y se muestra un mensaje.
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Seleccione un autor.", "Aviso", 2);
        } else {
            
            // Suena un beep y se muestra un mensaje de confirmacion.
            java.awt.Toolkit.getDefaultToolkit().beep();
            int res = javax.swing.JOptionPane.showConfirmDialog(this, "¿Eliminar " + autoresLista[tblAutores.getSelectedRow()][1].toString() + "?",
                "Seleccione", JOptionPane.YES_NO_OPTION);
            
            // Si la respuesta es afirmativa, elimina el registro.
            if(res == 0) {
                String msj = "";                
                String autor_id = tblAutores.getValueAt(tblAutores.getSelectedRow(), 0).toString();
                int ret = autorDAO.deleteAutoria(autor_id, libroId);
                if(ret != 1) {
                    
                    // Suena un beep y se muestra un mensaje de error.
                    msj = "No se pudo eliminar por que tiene egistros asignados.";                   
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    javax.swing.JOptionPane.showMessageDialog(this, msj, "Información", 1);
                }

                // Reinicia controles y parametros.
                llenaTablaAutores();
            }
        }
    }//GEN-LAST:event_btnRmveAutoriaActionPerformed

    /**
     * Abre una ventana para agregar a un autor a la autoria de un Libro.
     * @param evt evento que dispara la funcion.
     */
    private void btnAddAutoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddAutoriaActionPerformed
        if(!editando) {
            
            // Suena un beep y se muestra un mensaje.
            Toolkit.getDefaultToolkit().beep();            
            JOptionPane.showMessageDialog(this, "Para agregar autores primero guarde\nNo edite un libro.", "Aviso", 2);
        } else {
            addAutor.setLibroId(libroId);                                   
            addAutor.setLocationRelativeTo(this);
            addAutor.setVisible(true);
            
            // Cuando cierra la ventana agrega el autor seleccionada a la tabla de autores.
            llenaTablaAutores();
        }
    }//GEN-LAST:event_btnAddAutoriaActionPerformed

    /**
     * Recarga todos los combobox para mostrar informacion actualizada.
     * @param evt evento que dispara la funcion.
     */
    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        llenaDatos();
        cancelaEdit();
    }//GEN-LAST:event_btnRefreshActionPerformed

    /**
     * Abre una ventana para poder crear, editar y eliminar Paises.
     * @param evt evento que dispara la funcion.
     */
    private void btnEditPaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditPaisActionPerformed
        addPais.setLocationRelativeTo(this);
        addPais.setVisible(true);
        
        // Al cerrar, actualizar los paises disponibles.
        llenadoPaises();
        cancelaEdit();
    }//GEN-LAST:event_btnEditPaisActionPerformed

    /**
     * Abre una ventana para poder crear, editar y eliminar Editoriales.
     * @param evt evento que dispara la funcion.
     */
    private void btnEditEditorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditEditorialActionPerformed
        addEditorial.setLocationRelativeTo(this);
        addEditorial.setVisible(true);
        
        // Al cerrar, actualizar las Editoriales disponibles.
        llenadoEditoriales();
        cancelaEdit();
    }//GEN-LAST:event_btnEditEditorialActionPerformed

    /**
     * Abre una ventana para poder crear, editar y eliminar Generos.
     * @param evt evento que dispara la funcion.
     */
    private void btnEditGeneroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditGeneroActionPerformed
        addGenero.setLocationRelativeTo(this);
        addGenero.setVisible(true);
        
        // Al cerrar, actualizar los Generos disponibles.
        LlenadoGeneros();
        cancelaEdit();
    }//GEN-LAST:event_btnEditGeneroActionPerformed

    /**
     * Abre una ventana para poder crear, editar y eliminar Idiomas.
     * @param evt evento que dispara la funcion.
     */
    private void btnEditIdiomaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditIdiomaActionPerformed
        addIdioma.setLocationRelativeTo(this);
        addIdioma.setVisible(true);

        // Al cerrar, actualizar los Idiomas disponibles.
        llenadoIdiomas();
        cancelaEdit();
    }//GEN-LAST:event_btnEditIdiomaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddAutoria;
    private javax.swing.JButton btnAddLibro;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnBorrarFiltro;
    private javax.swing.JButton btnCancela;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnEditEditorial;
    private javax.swing.JButton btnEditGenero;
    private javax.swing.JButton btnEditIdioma;
    private javax.swing.JButton btnEditPais;
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
    private javax.swing.JTable tblAutores;
    private javax.swing.JTable tblLibros;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JTextField txtEdicion;
    private javax.swing.JTextField txtFiltro;
    private javax.swing.JTextField txtPags;
    private javax.swing.JTextField txtTitulo;
    // End of variables declaration//GEN-END:variables
}