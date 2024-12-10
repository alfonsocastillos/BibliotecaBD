package dataBase.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import dataBase.CustomConnection;
import dataBase.ConfigDataBase;
import java.sql.*;
   
/**
 * Clase encargada de consultar, insertar, eliminar y modificar tabla de prestamo.
 * @author alfonso
 */
public class PrestamoDAO extends CustomConnection {
    
    /**
     * Guarda un registro Prestamo.
     * @param prestamo datos del prestamo (Fecha de inicio, Fecha de entreha, Id del cliente, Id del empleado).
     * @return int con el Id del prestamo creado.
     */
    public int savePrestamo(Object[] prestamo) {
        connect();
        int prestamoId = 0;
        try {

            // Genera el nuevo Id.
            sentenciaSQL = "SELECT SUBSTR(EXTRACT(YEAR FROM SYSDATE), 3, 2) || LPAD(EXTRACT(MONTH FROM SYSDATE), 2, '0') || LPAD(EXTRACT(DAY FROM SYSDATE), 2,'0') || " +
                    "LPAD(NVL(MAX(TO_NUMBER(SUBSTR(PRESTAMO_ID, 7, 3))) + 1, 1), 3, '0') FROM PRESTAMO WHERE SUBSTR(PRESTAMO_ID, 1, 6) LIKE " +
                    "SUBSTR(EXTRACT(YEAR FROM SYSDATE), 3, 2) || LPAD(EXTRACT(MONTH FROM SYSDATE), 2, '0') || LPAD(EXTRACT(DAY FROM SYSDATE), 2, '0')";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            resultSet = preparedStatement.executeQuery();
            
            // Guarda el nuevo Id.
            String id = "";
            if(resultSet.next()) {
                id = resultSet.getString(1);
            }
            
            prestamoId = Integer.valueOf(id);
            sentenciaSQL = "INSERT INTO PRESTAMO VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, prestamoId);                    // Id del prestamo.
            preparedStatement.setString(2, prestamo[0].toString());     // Fecha inicio.
            preparedStatement.setString(3, prestamo[1].toString());     // Fecha entrega.
            preparedStatement.setInt(4, (int) prestamo[2]);             // Id del cliente. 
            preparedStatement.setString(5, prestamo[3].toString());     // Id del empleado.
            preparedStatement.executeUpdate();            
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "savePrestamo");
            prestamoId = 0;
        } finally {
            disconnect();
        }
        return prestamoId;
    }      
        
    /**
     * Consulta todos los prestamos de un cliente.
     * @param clienteId Id del cliente a consultar.
     * @return Object[][] con los datos de los prestamos encontrados (Titulo del libro, fecha de prestamo, fecha de entrega).
     */
    public Object[][] getPrestamosByCliente(int clienteId) {
        connect();
        Object[][] prestamos = null;
        try {
            sentenciaSQL = "SELECT COUNT(*) FROM PRESTAMO JOIN DETALLES_PRESTAMO USING (PRESTAMO_ID) WHERE CLIENTE_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, clienteId);
            resultSet = preparedStatement.executeQuery();

            int count = 0;
            while(resultSet.next()) {
                count = resultSet.getInt(1);
            }
            
            sentenciaSQL = "SELECT TITULO, FECHA_PRESTAMO, FECHA_ENTREGA FROM PRESTAMO JOIN DETALLES_PRESTAMO USING (PRESTAMO_ID)JOIN LIBRO USING (LIBRO_ID) " +
                    "WHERE CLIENTE_ID = ? ORDER BY 2";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, clienteId);
            resultSet = preparedStatement.executeQuery();
            
            int i = 0;
            prestamos = new Object[count][3];
            while(resultSet.next()) {
                prestamos[i][0] = resultSet.getString(1);               // Titulo del libro.
                                 
                DateTimeFormatter from_format = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.A");
                DateTimeFormatter to_format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDateTime fecha_prestamo = LocalDateTime.parse(resultSet.getString(2), from_format);
                prestamos[i][1] = to_format.format(fecha_prestamo);     // Fecha prestamo
                
                LocalDateTime fecha_entrega = LocalDateTime.parse(resultSet.getString(3), from_format);
                prestamos[i][2] = to_format.format(fecha_entrega);      // Fecha entrega
                i++;
            }
        } catch(SQLException ex ) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getPrestamosByCliente");
            prestamos = null;
        } finally {
            disconnect();
        }
        return prestamos;
    }
    
    /**
     * Borrar un prestamo.
     * @param prestamoId Id del prestamo a borrar.
     * @return int representando el resultado de la transaccion.
     */
    public int deletePrestamo(int prestamoId) {
        connect();
        int status = 0;
        try {
            
            // Borrar cualquier detalle relacionado.
            sentenciaSQL = "DELETE FROM DETALLES_PRESTAMO WHERE PRESTAMO_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, prestamoId);
            
            // Borrar el prestamo.
            sentenciaSQL = "DELETE FROM PRESTAMO WHERE PRESTAMO_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, prestamoId);
            status = preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "deletePrestamo");
            if(ex.getErrorCode() == 2292) {
                status = 0;
            } else {
                status = 2;
            }
        } finally {
           disconnect();
        }
        return status;
    }
}
