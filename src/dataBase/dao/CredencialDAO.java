package dataBase.dao;

import dataBase.CustomConnection;
import dataBase.ConfigDataBase;
import java.sql.*;

/**
 * Clase encargada de consultar, insertar, eliminar y modificar la tabla Credencial.
 * @author alfonso
 */

public class CredencialDAO extends CustomConnection {
    
    /**
     * Crear una Credencial.
     * @param fechaRenovacion fecha en formato dd/MM/yyyy en la que expira la credencial.
     * @return int con el Id de la credencial creada.
     */
    public int saveCredencial(String fechaRenovacion) {
        connect();
        int credId = 0;
        try {
            
            // Genera el nuevo id.
            sentenciaSQL = "SELECT MAX(CREDENCIAL_ID) + 1 FROM CREDENCIAL";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            resultSet = preparedStatement.executeQuery();

            // Guarda el nuevo id.
            if(resultSet.next()) {
                credId = resultSet.getInt(1);
            }

            // Inserta mandando todos los datos, incluido el nuevo id y la fecha proporcionada.
            sentenciaSQL = "INSERT INTO CREDENCIAL VALUES(?, TO_DATE(?, 'DD/MM/YY'))";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            
            // Asigna los valores del arreglo.
            preparedStatement.setInt(1, credId);              // Id (calculado).
            preparedStatement.setString(2, fechaRenovacion);  // Fecha de Renovación.
            preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveCredencial");
            credId = 0;
        } finally {
           disconnect();
        }
        return credId;
    }
}

