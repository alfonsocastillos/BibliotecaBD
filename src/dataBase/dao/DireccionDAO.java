package dataBase.dao;

import dataBase.CustomConnection;
import dataBase.ConfigDataBase;
import java.sql.*;

/**
 * Clase encargada de consultar, insertar, eliminar y modificar la tabla Direccion.
 * @author alfonso
 */
public class DireccionDAO extends CustomConnection {
    
    /**
     * Crear una direccion con los datos.
     * @param dir lista de datos de la direccion a crear (Alcaldia, CP, Calle, Num Exterior, Num Interior, Id del estado).
     * @return Id de la direccion creada.
     */
    public String saveDireccion(Object[] dir) {
        connect();
        String dirId = "";
        try {
            
            // Genera el nuevo id (año + mes + 3 num).
            sentenciaSQL = "SELECT LPAD(SUBSTR(EXTRACT(YEAR FROM SYSDATE), 3, 2), 2, '0') || LPAD(EXTRACT(MONTH FROM SYSDATE), 2, '0') || " +
                    "LPAD(NVL(MAX(TO_NUMBER(SUBSTR(DIRECCION_ID, 5, 3))) + 1, 1), 3, '0') FROM DIRECCION " +
                    "WHERE SUBSTR(DIRECCION_ID, 1, 4) = LPAD(SUBSTR(EXTRACT(YEAR FROM SYSDATE), 3, 2), 2, '0') || LPAD(EXTRACT(MONTH FROM SYSDATE), 2, '0')";            
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            resultSet = preparedStatement.executeQuery();
            
            // Guarda el nuevo id.
            if(resultSet.next()) {
                dirId = resultSet.getString(1);
            }
            
            // Inserta mandando todos los datos, incluido en nuevo id.
            sentenciaSQL = "INSERT INTO DIRECCION VALUES(?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            
            // Asigna los valores del arreglo.
            preparedStatement.setString(1, dirId);                      // Id (calculado).
            preparedStatement.setString(2, dir[0].toString());          // Alcaldia (provisto).
            preparedStatement.setString(3, dir[1].toString());          // CP (provisto).
            preparedStatement.setString(4, dir[2].toString());          // Calle (provisto).
            preparedStatement.setString(5, dir[3].toString());          // Numero exterior (provisto).
            preparedStatement.setString(6, dir[4].toString());          // Numero interior (provisto).
            preparedStatement.setInt(7, (int) dir[5]);                  // Id del estado (provisto).
            preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveDireccion");         
            dirId = "";
        } finally {
           disconnect();
        }
        return dirId;
    }
    
    /**
     * Actualiza una direccion con los datos.
     * @param clienteId Id del cliente al que pertenece esta direccion.
     * @param dir lista de datos de la direccion a crear (Id, Alcaldia, CP, Calle, Num Exterior, Num Interior, Id del estado).
     * @return boolean denotando el exito de la operacion.
     */
    public boolean updateDireccion(int clienteId, Object[] dir) {
        connect();
        boolean exito;
        try {
    
            // Inserta mandando todos los datos, incluido en nuevo id.
            sentenciaSQL = "UPDATE DIRECCION SET ALCALDIA = ?, CP = ?, CALLE = ?, EXTERIOR = ?, INTERIOR = ?, ESTADO_ID = ? WHERE DIRECCION_ID = " + 
                    "(SELECT DIRECCION_ID FROM CLIENTE WHERE CLIENTE_ID = ?)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            
            // Asigna los valores del arreglo.
            preparedStatement.setString(1, dir[0].toString());          // Alcaldia (provisto).
            preparedStatement.setString(2, dir[1].toString());          // CP (provisto).
            preparedStatement.setString(3, dir[2].toString());          // Calle (provisto).
            preparedStatement.setString(4, dir[3].toString());          // Numero exterior (provisto).
            preparedStatement.setString(5, dir[4].toString());          // Numero interior (provisto).
            preparedStatement.setInt(6, (int) dir[5]);                  // Id del estado (provisto).
            preparedStatement.setInt(7, clienteId);                     // Id del cliente (provisto).
            preparedStatement.executeUpdate();
            exito = true;
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveDireccion");         
            exito = false;
        } finally {
           disconnect();
        }
        return exito;
    }
}
