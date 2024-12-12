package dataBase.dao;

import dataBase.CustomConnection;
import dataBase.ConfigDataBase;
import java.sql.*;

/**
 * Operaciones a la base de datos en la tabla detalles de prestamo.
 * @author alfonso
 */
public class DetallePrestamoDAO extends CustomConnection { 
    
    /**
     * Guarda la relacion prestamo-libro.
     * @param idPrestamo Id del Prestamo con el que se relaciona el libro.
     * @param idLibro Id del Libro a relacionar con el prestamo.
     * @return int con el resultado de la operacion.
     */
    public int saveDetallePrestamo(int idPrestamo, int idLibro) {
        connect();
        int state;
        try {
            sentenciaSQL = "INSERT INTO DETALLES_PRESTAMO VALUES (?,?)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, idLibro);
            preparedStatement.setInt(2, idPrestamo);
            preparedStatement.executeUpdate();
            state = 1;
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveDetallePrestamo");
            state = 0;
        } finally {
            disconnect();
        }
        return state;
    }
}
