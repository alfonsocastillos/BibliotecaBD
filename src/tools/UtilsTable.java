package tools;

import java.awt.Font;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 * Clase que contiene utilerías para el tratabineto de JTables
 * @author carlos
 */
public class UtilsTable {
  
    /**
     * Busca la fila cuya primera columna es el Id proporcionado.
     * @param id Id que se busca.
     * @param datos Datos de la fila.
     * @return Int con el indice de la fila.
     */
    public static int getRow(int id, Object[][] datos) {
        int row = 0;
        for(int i = 0; i < datos.length ;i++) {
            if(Integer.parseInt(datos[i][0].toString())==id) {
                row = i;
                break;
            }
        }           
        return row;
    }
   
    /**
     * Busca la fila cuya primera columna es el Id proporcionado.
     * @param id Id que se busca.
     * @param datos Datos de la fila.
     * @return Int con el indice de la fila.
     */
    public static int getRow(String id, Object[][] datos) {
        int row = 0;
        for(int i = 0; i < datos.length ;i++) {
            if(datos[i][0].toString().equals(id)) {
                row = i;
                break;
            }
        }            
        return row;
    }
    
    /**
     * Mueve la tabla para que se muestre el contenido.
     * @param row Indice de la fila a mostrar
     * @param tabla Tabla a mover.
     * @return 
     */
    public static int mueveTabla(int row, javax.swing.JTable tabla) {
        tabla.setRowSelectionInterval(row,row);

        //Mueve el panel en la fila establecida.
        tabla.scrollRectToVisible(tabla.getCellRect(row,0,true));
        return tabla.getSelectedRow();
    }
    
    /**
     * Pone el contenido en la tabla.
     * @param cellAlignment Alineacion del texto dentro de la tabla.
     * @param cellSize Ancho de cada columna.
     * @param titulos Titulo de cada columna.
     * @param tabla Tabla que llenar.
     * @param datos Informacion con la cual llenar la tabla.
     */
    public static void llenaTabla(int[][] cellAlignment, int[][] cellSize, String[] titulos, javax.swing.JTable tabla, Object[][] datos) {
        tabla.setModel(new javax.swing.table.DefaultTableModel(datos,titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        
        // Configuracion de la tabla.
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setResizingAllowed(false);
        
        // Establece el alineamiento de texto para cada celda
        for(int i = 0; i < cellAlignment.length; i++) {
            cellAlignment(cellAlignment[i][0], cellAlignment[i][1], tabla);
        }            

        //Ancho de las columas.
        for(int i = 0; i < cellSize.length; i++) {
            columnSize(cellSize[i][0], cellSize[i][1], tabla);
        }
                    
        // Ancho de los renglones.
        for(int i = 0; i< tabla.getRowCount(); i++) {
            rowSize(i, 20, tabla);
        }                    
        tabla.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 12));
     }
  
    /**
     * Elimina todas las filas de una tabla.
     * @param tabla Tabla a limpiar.
     */
    public static void limpiaTabla(JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
         for(int i = 0; i < tabla.getRowCount(); i++) {
           modelo.removeRow(i);
           i-=1;
       }
    }
    
    /**
     * Elimina una columna de la visualizacion de la tabla.
     * @param indice Indice de la columna a quitar.
     * @param tabla Tabla que contiene los datos.
     */
    public static void quitarColumna(int indice, JTable tabla) {
        TableColumnModel tbc = tabla.getColumnModel();
        tbc.removeColumn(tbc.getColumn(indice));
    }
    
    /**
     * Obtiene el valor de una celda especifica de la tabla.
     * @param fila Indice de la fila a consultar.
     * @param columna Indice de la columna.
     * @param tabla Tabla a consultar.
     * @return Object con el valor consultado.
     */
    public static Object obtenerValor(int fila, int columna, JTable tabla) {
        return tabla.getModel().getValueAt(fila, columna);
    }
    
    /**
     * Agrega una fila a la tabla. 
     * @param tabla Tabla a la cual agregar la informacion.
     * @param valor Valores a agregar.
     */
    public static void agregarFila(JTable tabla, Object[] valor) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.addRow(valor);
    }
    
    /**
     * Quita una fila de la tabla.
     * @param fila Indice de la fila a quitar.
     * @param tabla Tabla de la cual quitar la fila.
     */
    public static void quitarFila(int fila, JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.removeRow(fila);
    }
               
    /**
     * Asigna el ancho de la columna.
     * @param column Indice de la columna.
     * @param size Ancho de la columna.
     * @param tabla Tabla a la que pertenecen las columnas.
     */
    private static void columnSize(int column, int size, javax.swing.JTable tabla) {
        tabla.getColumnModel().getColumn(column).setMaxWidth(size);
        tabla.getColumnModel().getColumn(column).setMinWidth(size);
        tabla.getColumnModel().getColumn(column).setPreferredWidth(size);
    }
    
    /**
     * Asigna el alto de las filas.
     * @param row Indice de la fila.
     * @param size Alto de la columna.
     * @param tabla Tabla a la que pertenecen las filas.
     */
    private static void rowSize(int row, int size, javax.swing.JTable tabla) {
        tabla.setRowHeight(row, size);
    }
    
    /**
     * Establece la alineacion del texto en las celdas.
     * @param column Indice de la columna a establecer.
     * @param cellAlignment Alineacion deseada.
     * @param tabla Tabla a la que pertenecen las celdas.
     */
    private static void cellAlignment(int column, int cellAlignment, javax.swing.JTable tabla) {
        javax.swing.table.DefaultTableCellRenderer tcr = new javax.swing.table.DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(cellAlignment);
        tabla.getColumnModel().getColumn(column).setCellRenderer(tcr);
    }
}