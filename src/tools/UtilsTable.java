/*
 * Clase: UtilsTable.java
 * Fecha: 15/08/2013
 *
 * Desarrollador: Carlos Cortés Bazán
 * 
 * Descripción: Clase que contiene utilerías para el tratabineto de JTables.
 *
 * Nota: debido a que ésta clase es estática, no es necesario crear un objeto
 *       de ella, solo se usa directamente.
 *
 */


package tools;

import java.awt.Font;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class UtilsTable {
   
    // Funciones para tablas
   public static int getRow(Object datos [][], int id) {
        int row = 0;
        for (int i = 0; i < datos.length ;i++)
            if(Integer.parseInt(datos[i][0].toString())==id) {
                row = i;
                break;
            }
        return row;
    }
   
   public static int getRow(Object datos [][], String id) {
        int row = 0;
        for (int i = 0; i < datos.length ;i++)
            if(datos[i][0].toString().equals(id)) {
                row = i;
                break;
            }
        return row;
    }
    // Mueve la tabla para que se muestre el contenido
    public static int mueveTabla(javax.swing.JTable tabla, int a) {
        // Cuando de agrega un registro
        //Selecciona el registro
        tabla.setRowSelectionInterval(a,a);
        //Mueve el panel en la fila establecida
        tabla.scrollRectToVisible(tabla.getCellRect(a,0,true));
        return tabla.getSelectedRow();
    }
    
    // Pone el contenido en la tabla
    public static void llenaTabla(javax.swing.JTable tabla, Object[][] datos, String[] titulos, int[][] cellAlignment,
             int[][] cellSize) {
        tabla.setModel(new javax.swing.table.DefaultTableModel(datos,titulos)
        {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        //Selecci?n sencilla de la tabla
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Para evitar que se puedan mover las columnas de la tabla
        tabla.getTableHeader().setReorderingAllowed(false);
        // Para evitar que se les pueda cambiar el tamaño a las columnas de la tabla
        tabla.getTableHeader().setResizingAllowed(false);
        
        // Establece el alineamiento de texto para cada celda
        for (int i = 0; i< cellAlignment.length; i++)
            cellAlignment(tabla,cellAlignment[i][0],cellAlignment[i][1]);

        //Ancho de las columas
        for (int i = 0; i< cellSize.length; i++)
            columnSize(tabla,cellSize[i][0],cellSize[i][1]);
        
        // Ancho de los renglones
        for (int i = 0; i< tabla.getRowCount(); i++)
            rowSize(tabla,i,20);
        
        tabla.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 12));
     }
  
    public static void limpiaTabla(JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
         for (int i = 0; i < tabla.getRowCount(); i++) {
           modelo.removeRow(i);
           i-=1;
       }
    }
    
    public static void quitarColumna(JTable tabla, int indice) {
        TableColumnModel tbc = tabla.getColumnModel();
        tbc.removeColumn(tbc.getColumn(indice));
    }
    
    public static Object obtenerValor(JTable tabla, int fila, int columna) {
        return tabla.getModel().getValueAt(fila, columna);
    }
    
    public static void agregarFila(JTable tabla, Object[] valor) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.addRow(valor);
    }
    
    public static void quitarFila(JTable tabla, int fila) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.removeRow(fila);
    }
           
    
    // Asigna el ancho de la columna
    private static void columnSize(javax.swing.JTable tabla,int column, int size) {
        // Da tamaño a las columnas de la tabla     
        tabla.getColumnModel().getColumn(column).setMaxWidth(size);
        tabla.getColumnModel().getColumn(column).setMinWidth(size);
        tabla.getColumnModel().getColumn(column).setPreferredWidth(size);
    }
    
    // Alto de las filas 
    private static void rowSize(javax.swing.JTable tabla, int row, int size) {
        // Da tamaño a las columnas de la tabla     
        tabla.setRowHeight(row, size);
    }
    
    // Alineación del texto en las celdas
    private static void cellAlignment(javax.swing.JTable tabla,int column, int cellAlignment) {
        javax.swing.table.DefaultTableCellRenderer tcr = new javax.swing.table.DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(cellAlignment);
        tabla.getColumnModel().getColumn(column).setCellRenderer(tcr);
    }
}