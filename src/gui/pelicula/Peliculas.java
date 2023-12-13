package gui.pelicula;

import dataBase.ReportePeliculasPDF;
import dataBase.dao.DaoActor;
import dataBase.dao.DaoCategory;
import dataBase.dao.DaoFilm;
import dataBase.dao.DaoLang;
import dataBase.dao.DaoRate;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import tools.*;

/**
 *
 * @author Carlos
 * Ventana que administra las peliculas
 */
public class Peliculas extends javax.swing.JInternalFrame {
    boolean editando = false;
    String id;
    // Dao´s que traen los datos de la DB
    DaoCategory daoCategory;
    DaoLang daoLang;
    DaoRate daoRate;
    DaoFilm daoFilm;
    DaoActor daoActor;
    // Objtos que guardaran los datos    
    Object lenguajes[][];
    Object categories[][];
    Object rates[][];
    Object filmsLista [][];
    Object actorsLista [][];
    Object doblajeLista [][];
    // Ventana para agregar actores y doblaje
    AddActores addActores;
    AddDoblaje addDoblaje;

    
    public Peliculas() {
        // Constructor
        initComponents();
        // Para que escriba en la linea de abajo al llegar al final de la descripción
        txtDesc.setLineWrap(true);
        // Para que no corte las palabras en la descripción
        txtDesc.setWrapStyleWord(true);
        // se instancian todos los DAO
        daoCategory = new DaoCategory();
        daoLang = new DaoLang();
        daoFilm = new DaoFilm();
        daoRate = new DaoRate();
        daoActor = new DaoActor();
        // Instanciando ventanas para agregar actores y doblaje
        addActores = new AddActores((java.awt.Frame)this.getParent(), true);
        addDoblaje = new AddDoblaje((java.awt.Frame)this.getParent(), true);
        // localización de la ventana
        setLocale(null);
        // llena los datos
        llenaDatos();
    }
    
    private void llenaDatos(){
        // Llena combos y tabla de peliculas
        llenaLanguage();
        llenaCategory();
        llenaRate();
        llenaTablaPeliculas();
    }    
    
    private void llenaLanguage(){
        // Consulta los datos de idioma
        lenguajes = daoLang.getLanguages();
        for (Object[] lenguaje : lenguajes) {
            // llena los datos de idioma en el combo 
            cmbIdioma.addItem(lenguaje[1].toString());
        }
    }
    
    private void llenaCategory(){
        // Consulta los datos de categoria
        categories = daoCategory.getCategories();
        for (Object[] category : categories) {
            // llena los datos de categoria en el combo 
            cmbGenero.addItem(category[1].toString());
        }
    }
    
    private void llenaRate(){
        // Consulta los datos de clasificación
        rates = daoRate.getRates();
        for (Object[] rate : rates) {
            // llena los datos de clasificación en el combo 
            cmbClas.addItem(rate[1].toString());
        }
    }
    
    private void llenaTablaPeliculas(){       
        // consulta los datos de las peliculas
        filmsLista = daoFilm.getFilmsByTitleDesc(txtFiltro.getText().trim());
        // configuración de la tabla
        String[] T_FILMS = {"","Título", "Año", "Minutos", "Clasif.", "Categoría"};
        int[][] cellAlignment = {{0,javax.swing.SwingConstants.LEFT}};
        int[][] cellSize = {{0,0},
                            {1,270},
                            {2,40},
                            {3,55},
                            {4,50},
                            {4,55}};
        // pone los datos en la tabla
        UtilsTable.llenaTabla(tableList,filmsLista, T_FILMS, cellAlignment, cellSize);
        lblCantidad.setText(filmsLista.length + "");
    }
    
    private void llenaTablaActores(){       
        // consulta los datos de los actores de reparto
        actorsLista = daoActor.getActorsFilm(id);
        // configuración de la tabla
        String[] T_ACTORS = {"","Nombre"};
        int[][] cellAlignment = {{0,javax.swing.SwingConstants.LEFT}};
        int[][] cellSize = {{0,0},
                            {1,170}};
        // pone los datos en la tabla
        UtilsTable.llenaTabla(tableListActor,actorsLista, T_ACTORS, cellAlignment, cellSize);        
    }
    
    private void llenaTablaDoblaje(){       
        // consulta los datos de doblaje
        doblajeLista = daoLang.getLanguageByFilm(id);
        // configuración de la tabla
        String[] T_DOBLAJE = {"","Idioma"};
        int[][] cellAlignment = {{0,javax.swing.SwingConstants.LEFT}};
        int[][] cellSize = {{0,0},
                            {1,170}};
        // pone los datos en la tabla
        UtilsTable.llenaTabla(tableListDoblaje,doblajeLista, T_DOBLAJE, cellAlignment, cellSize);        
    }    
    
    private boolean estanLlenos(){
        // Valida si se llenaron los datos antes de guardar
        if (txtTitulo.getText().trim().length() == 0){
            javax.swing.JOptionPane.showMessageDialog(this, "Introdusca el titulo.","Aviso",2);
            txtTitulo.requestFocus();
            return false;
        }
        else if (txtDesc.getText().trim().length() == 0){
            javax.swing.JOptionPane.showMessageDialog(this, "Introdusca la descripción.","Aviso",2);
            txtDesc.requestFocus();
            return false;
        }
        else if (txtAnio.getText().trim().length() == 0){
            javax.swing.JOptionPane.showMessageDialog(this, "Introdusca el año.","Aviso",2);
            txtAnio.requestFocus();
            return false;
        }
        else if (txtDuracion.getText().trim().length() == 0){
            javax.swing.JOptionPane.showMessageDialog(this, "Introdusca la duración.","Aviso",2);
            txtDuracion.requestFocus();
            return false;
        }
        else if (txtRenta.getText().trim().length() == 0){
            javax.swing.JOptionPane.showMessageDialog(this, "Introdusca el costo de renta.","Aviso",2);
            txtRenta.requestFocus();
            return false;
        }
        else if (txtRempl.getText().trim().length() == 0){
            javax.swing.JOptionPane.showMessageDialog(this, "Introdusca el costo de remplazo.","Aviso",2);
            txtRempl.requestFocus();
            return false;
        }
        else if (txtDiasR.getText().trim().length() == 0){
            javax.swing.JOptionPane.showMessageDialog(this, "Introdusca los días de renta.","Aviso",2);
            txtDiasR.requestFocus();
            return false;
        }
        else
            return true;
    }
    
    private void cancelaEdit (){
        // Cancela la edición y reinicia los controles
        txtTitulo.setText("");
        txtAnio.setText("");
        txtDuracion.setText("");
        txtDesc.setText("");
        txtRenta.setText("");
        txtRempl.setText("");
        txtDiasR.setText("");
        cmbIdioma.setSelectedIndex(0);
        cmbGenero.setSelectedIndex(0);
        cmbClas.setSelectedIndex(0);
        editando = false;
        id = null;
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

        pnlPelicula = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        txtTitulo = new javax.swing.JTextField();
        lblDesc = new javax.swing.JLabel();
        scrllDesc = new javax.swing.JScrollPane();
        txtDesc = new javax.swing.JTextArea();
        lblAnio = new javax.swing.JLabel();
        txtAnio = new javax.swing.JTextField();
        lblDurac = new javax.swing.JLabel();
        txtDuracion = new javax.swing.JTextField();
        lblGenero = new javax.swing.JLabel();
        cmbGenero = new javax.swing.JComboBox<>();
        lblClas = new javax.swing.JLabel();
        cmbClas = new javax.swing.JComboBox<>();
        lblRenta = new javax.swing.JLabel();
        txtRenta = new javax.swing.JTextField();
        lblRempl = new javax.swing.JLabel();
        txtRempl = new javax.swing.JTextField();
        lblDiasR = new javax.swing.JLabel();
        txtDiasR = new javax.swing.JTextField();
        lblGenero1 = new javax.swing.JLabel();
        cmbIdioma = new javax.swing.JComboBox<>();
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
        tableListActor = new javax.swing.JTable();
        btnAddCast = new javax.swing.JButton();
        btnDelCast = new javax.swing.JButton();
        btnAddFilm = new javax.swing.JButton();
        btnGuarda = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnCancela = new javax.swing.JButton();
        pnlTableList3 = new javax.swing.JPanel();
        scpTableList2 = new javax.swing.JScrollPane();
        tableListDoblaje = new javax.swing.JTable();
        btnAddDoblaje = new javax.swing.JButton();
        btnDelDoblaje = new javax.swing.JButton();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setTitle("Películas");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/img/peliculas.png"))); // NOI18N
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

        pnlPelicula.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Películas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlPelicula.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblTitulo.setText("Título:");
        pnlPelicula.add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, 20));

        txtTitulo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlPelicula.add(txtTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 260, 20));

        lblDesc.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblDesc.setText("Descripción:");
        pnlPelicula.add(lblDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, 20));

        txtDesc.setColumns(20);
        txtDesc.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDesc.setRows(3);
        scrllDesc.setViewportView(txtDesc);

        pnlPelicula.add(scrllDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 300, 117));

        lblAnio.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblAnio.setText("Año:");
        pnlPelicula.add(lblAnio, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 30, -1, 20));

        txtAnio.setDocument(new InputType(InputType.TIPO_SOLO_NUMEROS, 4, false, false));
        txtAnio.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pnlPelicula.add(txtAnio, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 30, 40, 20));

        lblDurac.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblDurac.setText("Duración");
        pnlPelicula.add(lblDurac, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 30, -1, 20));

        txtDuracion.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDuracion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pnlPelicula.add(txtDuracion, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 30, 40, 20));

        lblGenero.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblGenero.setText("Género:");
        pnlPelicula.add(lblGenero, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 90, -1, 20));

        pnlPelicula.add(cmbGenero, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 90, 150, -1));

        lblClas.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblClas.setText("Clasificación:");
        pnlPelicula.add(lblClas, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 120, -1, 20));

        pnlPelicula.add(cmbClas, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 120, 150, -1));

        lblRenta.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblRenta.setText("Renta:");
        pnlPelicula.add(lblRenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 150, -1, 20));

        txtRenta.setDocument(new InputType(InputType.TIPO_SOLO_NUMEROS, 4, false, true));
        txtRenta.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtRenta.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pnlPelicula.add(txtRenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 150, 40, 20));

        lblRempl.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblRempl.setText("Remplazo:");
        pnlPelicula.add(lblRempl, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 150, -1, 20));

        txtRempl.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtRempl.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pnlPelicula.add(txtRempl, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 150, 40, 20));

        lblDiasR.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblDiasR.setText("Días de renta:");
        pnlPelicula.add(lblDiasR, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 180, -1, 20));

        txtDiasR.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDiasR.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pnlPelicula.add(txtDiasR, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 180, 40, 20));

        lblGenero1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblGenero1.setText("Idioma:");
        pnlPelicula.add(lblGenero1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 60, -1, 20));

        cmbIdioma.setNextFocusableComponent(cmbGenero);
        pnlPelicula.add(cmbIdioma, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 60, 150, -1));

        pnlTableList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lista de películas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
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

        pnlTableList.add(scpTableList, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 560, 160));

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
        btnBorrar.setToolTipText("Borrar pelicula");
        btnBorrar.setFocusable(false);
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });
        pnlTableList.add(btnBorrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 20, 40, 40));

        btnReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/reporte.png"))); // NOI18N
        btnReporte.setToolTipText("Mostrar reporte");
        btnReporte.setFocusable(false);
        btnReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteActionPerformed(evt);
            }
        });
        pnlTableList.add(btnReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 40, 40));

        btnReportePDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/pdf-icon.png"))); // NOI18N
        btnReportePDF.setToolTipText("Mostrar en PDF");
        btnReportePDF.setFocusable(false);
        btnReportePDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportePDFActionPerformed(evt);
            }
        });
        pnlTableList.add(btnReportePDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 20, 40, 40));

        btnBorrarFiltro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/cancelarm.png"))); // NOI18N
        btnBorrarFiltro.setToolTipText("Borrar filtro");
        btnBorrarFiltro.setFocusable(false);
        btnBorrarFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarFiltroActionPerformed(evt);
            }
        });
        pnlTableList.add(btnBorrarFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 28, 30, 30));

        jLabel2.setText("Numero de peliculas en catálogo:");
        pnlTableList.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, -1, 30));

        lblCantidad.setText("000");
        pnlTableList.add(lblCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 230, -1, 30));

        pnlTableList1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Reparto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scpTableList1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        tableListActor.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tableListActor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scpTableList1.setViewportView(tableListActor);

        pnlTableList1.add(scpTableList1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 170, 160));

        btnAddCast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/add.png"))); // NOI18N
        btnAddCast.setToolTipText("Agregar actor");
        btnAddCast.setFocusable(false);
        btnAddCast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddCastActionPerformed(evt);
            }
        });
        pnlTableList1.add(btnAddCast, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 30, 30));

        btnDelCast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/quitar.png"))); // NOI18N
        btnDelCast.setToolTipText("Borrar actor");
        btnDelCast.setFocusable(false);
        btnDelCast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelCastActionPerformed(evt);
            }
        });
        pnlTableList1.add(btnDelCast, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 30, 30));

        btnAddFilm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/addPel.png"))); // NOI18N
        btnAddFilm.setFocusable(false);
        btnAddFilm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAddFilmMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAddFilmMouseExited(evt);
            }
        });
        btnAddFilm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddFilmActionPerformed(evt);
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

        pnlTableList3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Doblaje", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scpTableList2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        tableListDoblaje.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tableListDoblaje.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scpTableList2.setViewportView(tableListDoblaje);

        pnlTableList3.add(scpTableList2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 170, 130));

        btnAddDoblaje.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/add.png"))); // NOI18N
        btnAddDoblaje.setToolTipText("Agregar idioma");
        btnAddDoblaje.setFocusable(false);
        btnAddDoblaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDoblajeActionPerformed(evt);
            }
        });
        pnlTableList3.add(btnAddDoblaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 30, 30));

        btnDelDoblaje.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/quitar.png"))); // NOI18N
        btnDelDoblaje.setToolTipText("Borrar idioma");
        btnDelDoblaje.setFocusable(false);
        btnDelDoblaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelDoblajeActionPerformed(evt);
            }
        });
        pnlTableList3.add(btnDelDoblaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 30, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlTableList, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlPelicula, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlTableList1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlTableList3, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAddFilm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(btnGuarda, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
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
                    .addComponent(btnAddFilm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuarda, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancela, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlPelicula, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                    .addComponent(pnlTableList3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlTableList1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlTableList, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardaActionPerformed
        //  Guarda y actualiza los datos        
        if (estanLlenos()){
            // guarda los valores de los controles en un arreglo de objetos
            Object [] film = new Object[11];
            film[1] = txtTitulo.getText().trim();
            film[2] = txtDesc.getText().trim();
            film[3] = Integer.parseInt(txtAnio.getText().trim());
            film[4] = Integer.parseInt(lenguajes[cmbIdioma.getSelectedIndex()][0].toString());
            film[5] = Integer.parseInt(txtDiasR.getText().trim());
            film[6] = Float.parseFloat(txtRenta.getText().trim());            
            film[7] = Integer.parseInt(txtDuracion.getText().trim());
            film[8] = Float.parseFloat(txtRempl.getText().trim());
            film[9] = rates[cmbClas.getSelectedIndex()][0];
            film[10] = categories[cmbGenero.getSelectedIndex()][0];
            
            // Si está actualizando
            if (editando){
                film[0] = id;
                // Actualiza los datos y regresa el id
                id = daoFilm.updateFilm(film);
                javax.swing.JOptionPane.showMessageDialog(this, "Datos actualizados.","Información",1);
            }
            // si está guardando un registro nuevo
            else{
                // Guarda los datos  y regresa el id
                id = daoFilm.saveFilm(film);
            }
            // Si el id n0 es nulo, procesde a llenar los demas datos
            if (id != null){                
                llenaTablaPeliculas();
                UtilsTable.mueveTabla(tableList, UtilsTable.getRow(filmsLista, id));
                editando = true;
                btnCancela.setEnabled(true);
                txtTitulo.requestFocus();
            }
            else
                javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar.","Error",1);
        }
    }//GEN-LAST:event_btnGuardaActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Botón que consulta el registro selecionado de la tabla para editar
        if (tableList.getSelectedRow() < 0)
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila.","Información",1);
        else{
            // Consulta los datos
            Object[] editFilm = daoFilm.getFilm(tableList.getValueAt(tableList.getSelectedRow(), 0).toString());
            if (editFilm != null){
                // Pone los datos consultados en los controles
                txtTitulo.setText(editFilm[1].toString());  // titulo
                txtDesc.setText(editFilm[2].toString());    // Desc
                txtAnio.setText(editFilm[3].toString());    //Año
                cmbIdioma.setSelectedItem(editFilm[4].toString());//leng
                txtDiasR.setText(editFilm[5].toString());    // días de renta            
                txtDuracion.setText(editFilm[6].toString());//length
                txtRenta.setText(editFilm[7].toString()); // Costo renta
                txtRempl.setText(editFilm[8].toString());   //Costo de remplazo
                cmbClas.setSelectedItem(editFilm[9].toString());//  Rate
                cmbGenero.setSelectedItem(editFilm[10].toString()); // cat              
                id = editFilm[0].toString();                        // id de pelicula
                // llena las tablas de reparto y doblaje
                llenaTablaActores();
                llenaTablaDoblaje();
                // indica que está editando
                editando = true;
                btnCancela.setEnabled(true);
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnCancelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelaActionPerformed
        // Cancela la edición  y reinicia controles y parametros
        cancelaEdit();
        tableList.clearSelection();
        llenaTablaActores();
        llenaTablaDoblaje();
    }//GEN-LAST:event_btnCancelaActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        // Eliminar un registro
        // si no selecciona fila, le avisa al usuario
        if (tableList.getSelectedRow() < 0)
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila.","Aviso",2);
        else{
            // suena un beep
            java.awt.Toolkit.getDefaultToolkit().beep();
            // pregunta si quiere eliminar el registro y camtura la respuesta
            int res = javax.swing.JOptionPane.showConfirmDialog(this, "¿Eliminar " + filmsLista[tableList.getSelectedRow()][1].toString() + "?",
                 "Seleccione", JOptionPane.YES_NO_OPTION);
            // evalua la respuesta 
            if (res == 0){
                String msj = "";
                // si la respuesta es afirmativa, elimina el registro
                int ret = daoFilm.deleteFilm(tableList.getValueAt(tableList.getSelectedRow(), 0).toString());
                if (ret == 0){
                    msj = "Se eliminó la pelicula.";
                }
                else if (ret == 1){
                    msj = "No se pudo eliminar por que tiene registros asignados.";
                }
                // suena un beep
                java.awt.Toolkit.getDefaultToolkit().beep();
                javax.swing.JOptionPane.showMessageDialog(this, msj,"Información",1);
                // Reinicia controles y parametros
                llenaTablaPeliculas();
                llenaTablaActores();
                llenaTablaDoblaje();
                cancelaEdit();
                txtTitulo.requestFocus();
            }        }
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnAddFilmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddFilmActionPerformed
        // Limpia para agragar un alumno
        cancelaEdit();
        tableList.clearSelection();
        llenaTablaActores();
        llenaTablaDoblaje();
    }//GEN-LAST:event_btnAddFilmActionPerformed

    private void tableListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListMouseClicked
        // Muestra el alumno seleccionado con dos click en la tabla
        // Muestra un ballon si es necesarios
        if (evt.getClickCount() == 2)
            btnEditActionPerformed(null);
    }//GEN-LAST:event_tableListMouseClicked

    private void btnReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteActionPerformed
        // genera reporte de peliculas
        VentanaReportePeliculas reportePeliculas = new VentanaReportePeliculas();
        reportePeliculas.setVisible(true);
    }//GEN-LAST:event_btnReporteActionPerformed

    private void btnReportePDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportePDFActionPerformed
        // genera y ejecuta el reporte en PDF
        ReportePeliculasPDF reporte = new ReportePeliculasPDF();
        reporte.genReport();
    }//GEN-LAST:event_btnReportePDFActionPerformed

    private void btnAddCastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCastActionPerformed
        // Agregar casting
        // Verifica si esta editando una pelicula
        if (!editando){
            // Suena un beep
            Toolkit.getDefaultToolkit().beep();
            // Muestra un mensage de aviso
            JOptionPane.showMessageDialog(this, "Para agregar actores primero guarde\no edite una pelicula.", "Aviso",2);          
        }
        else{           
            // Abre la ventana para agregar actores
            // Asigna el id de la pelicula
            addActores.setIdFilm(id);
            // Localizacvión de la ventana
            addActores.setLocationRelativeTo(this);
            // hace visible la ventana
            addActores.setVisible(true);        
            // cuando cierra la ventana agrega el actor seleccionada a la tabla de actores
            llenaTablaActores();
        }
    }//GEN-LAST:event_btnAddCastActionPerformed

    private void btnDelCastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelCastActionPerformed
        // Borra reparto
        // Verifica si se selecciono un elemento de la tabla
        if (tableListActor.getSelectedRow() < 0){
            // Suena un beep
            Toolkit.getDefaultToolkit().beep();
            // Muestra un mensage de aviso
            JOptionPane.showMessageDialog(this, "Seleccione un actor.", "Aviso",2);            
        }
        else{     
            // suena un beep
            java.awt.Toolkit.getDefaultToolkit().beep();
            // pregunta si quiere eliminar el registro y camtura la respuesta
            int res = javax.swing.JOptionPane.showConfirmDialog(this, "¿Eliminar " + actorsLista[tableListActor.getSelectedRow()][1].toString() + "?",
                 "Seleccione", JOptionPane.YES_NO_OPTION);
            // evalua la respuesta 
            if (res == 0){
                String msj = "";
                // si la respuesta es afirmativa, elimina el registro
                String idActor = tableListActor.getValueAt(tableListActor.getSelectedRow(), 0).toString();
                int ret = daoActor.deleteCasting(id,idActor);
                if (ret == 1){
                    msj = "No se pudo eliminar por que tiene egistros asignados.";
                    // suena un beep
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    javax.swing.JOptionPane.showMessageDialog(this, msj,"Información",1);
                }
                
                // Reinicia controles y parametros
                llenaTablaActores();
            }
        }
    }//GEN-LAST:event_btnDelCastActionPerformed

    private void txtFiltroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyReleased
        // Filtra
        llenaTablaPeliculas();
    }//GEN-LAST:event_txtFiltroKeyReleased

    private void btnBorrarFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarFiltroActionPerformed
        // Borra el filtro de busqyeda
        txtFiltro.setText("");
        llenaTablaPeliculas();
    }//GEN-LAST:event_btnBorrarFiltroActionPerformed

    private void btnAddFilmMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddFilmMouseEntered
        // Tool tip text de la librería ballontip
        UtilsGUI.showBallonAviso(btnAddFilm, "Agregar nueva pelicula.", false);
    }//GEN-LAST:event_btnAddFilmMouseEntered

    private void btnAddFilmMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddFilmMouseExited
        // Quitar ballontip
        UtilsGUI.hideBallonTxt();
    }//GEN-LAST:event_btnAddFilmMouseExited

    private void btnAddDoblajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDoblajeActionPerformed
        // Agregar doblaje
        // verifica si está editando
        if (!editando){
            // Suena un beep
            Toolkit.getDefaultToolkit().beep();
            // Muestra un mensage de aviso
            JOptionPane.showMessageDialog(this, "Para agregar doblaje primero guarde\no edite una pelicula.", "Aviso",2);          
        }
        else{           
            // Abre la ventana para agregar idioma
            // Asigna el id de la pelicula
            addDoblaje.setIdFilm(id);
            // Localización de la ventana
            addDoblaje.setLocationRelativeTo(this);
            // hace visible la ventana
            addDoblaje.setVisible(true);        
            // cuando cierra la ventana y agrega el idioma seleccionada a la tabla de actores
            llenaTablaDoblaje();
        }
    }//GEN-LAST:event_btnAddDoblajeActionPerformed

    private void btnDelDoblajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelDoblajeActionPerformed
        // Borra doblaje
        // Verifica si se selecciono un elemento de la tabla
        if (tableListDoblaje.getSelectedRow() < 0){
            // Suena un beep
            Toolkit.getDefaultToolkit().beep();
            // Muestra un mensage de aviso
            JOptionPane.showMessageDialog(this, "Seleccione un idioma.", "Aviso",2);            
        }
        else{     
            // suena un beep
            java.awt.Toolkit.getDefaultToolkit().beep();
            // pregunta si quiere eliminar el registro y camtura la tableListDoblaje
            int res = javax.swing.JOptionPane.showConfirmDialog(this, "¿Eliminar " + doblajeLista[tableListDoblaje.getSelectedRow()][1].toString() + "?",
                 "Seleccione", JOptionPane.YES_NO_OPTION);
            // evalua la respuesta 
            if (res == 0){
                String msj = "";
                // si la respuesta es afirmativa, elimina el registro
                int idIdioma = Integer.parseInt(tableListDoblaje.getValueAt(tableListDoblaje.getSelectedRow(), 0).toString());
                int ret = daoLang.deleteLanguageFilm(id,idIdioma);
                if (ret == 1){
                    msj = "No se pudo eliminar por que tiene egistros asignados.";
                    // suena un beep
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    javax.swing.JOptionPane.showMessageDialog(this, msj,"Información",1);
                }                
                // Reinicia controles y parametros
                llenaTablaDoblaje();
            }
        }
    }//GEN-LAST:event_btnDelDoblajeActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // limpia todo al cerrar la ventana
        // Cancela la edición  y reinicia controles y parametros
        cancelaEdit();
        tableList.clearSelection();
        txtFiltro.setText("");
        llenaTablaPeliculas();
        llenaTablaActores();
        llenaTablaDoblaje();       
    }//GEN-LAST:event_formInternalFrameClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddCast;
    private javax.swing.JButton btnAddDoblaje;
    private javax.swing.JButton btnAddFilm;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnBorrarFiltro;
    private javax.swing.JButton btnCancela;
    private javax.swing.JButton btnDelCast;
    private javax.swing.JButton btnDelDoblaje;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnGuarda;
    private javax.swing.JButton btnReporte;
    private javax.swing.JButton btnReportePDF;
    private javax.swing.JComboBox<String> cmbClas;
    private javax.swing.JComboBox<String> cmbGenero;
    private javax.swing.JComboBox<String> cmbIdioma;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblAnio;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblClas;
    private javax.swing.JLabel lblDesc;
    private javax.swing.JLabel lblDiasR;
    private javax.swing.JLabel lblDurac;
    private javax.swing.JLabel lblGenero;
    private javax.swing.JLabel lblGenero1;
    private javax.swing.JLabel lblRempl;
    private javax.swing.JLabel lblRenta;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTitulo1;
    private javax.swing.JPanel pnlPelicula;
    private javax.swing.JPanel pnlTableList;
    private javax.swing.JPanel pnlTableList1;
    private javax.swing.JPanel pnlTableList3;
    private javax.swing.JScrollPane scpTableList;
    private javax.swing.JScrollPane scpTableList1;
    private javax.swing.JScrollPane scpTableList2;
    private javax.swing.JScrollPane scrllDesc;
    private javax.swing.JTable tableList;
    private javax.swing.JTable tableListActor;
    private javax.swing.JTable tableListDoblaje;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JTextArea txtDesc;
    private javax.swing.JTextField txtDiasR;
    private javax.swing.JTextField txtDuracion;
    private javax.swing.JTextField txtFiltro;
    private javax.swing.JTextField txtRempl;
    private javax.swing.JTextField txtRenta;
    private javax.swing.JTextField txtTitulo;
    // End of variables declaration//GEN-END:variables
}