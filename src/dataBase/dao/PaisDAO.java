package dataBase.dao;

import dataBase.CustomConnection;
import dataBase.ConfigDataBase;
import java.sql.*;

/**
 * Clase encargada de consultar, insertar, eliminar y modificar la tabla Pais.
 * @author alfonso
 */

public class PaisDAO extends CustomConnection {
    
    /**
     * Crear un pais.
     * @param pais nombre del pais.
     * @return int con el Id del pais creado.
     */
    public int savePais(String pais) {
        connect();
        int paisId = 0;
        try {
            
            // Genera el nuevo id.
            sentenciaSQL = "SELECT MAX(PAIS_ID) + 1 FROM PAIS";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            resultSet = preparedStatement.executeQuery();
            
            // Guarda el nuevo id.
            if(resultSet.next()) {
                paisId = resultSet.getInt(1);
            }
            
            // Inserta mandando todos los datos, incluido en nuevo Id.
            sentenciaSQL = "INSERT INTO PAIS VALUES(?, ?, SYSDATE)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, paisId);    // Id (calculado).
            preparedStatement.setString(2, pais);   // Pais (provisto).
            preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "savePais");
            paisId = 0;
        } finally {
           disconnect();
        }
        return paisId;
    }
    
    /**
     * Modificar un pais.
     * @param pais datos del pais a modificar (id, nuevo nombre del pais).
     * @return int con el Id del pais modificado.
     */
    public int updatePais(Object[] pais)
    {       
        connect();
        int paisId = 0;
        try {
            sentenciaSQL = "UPDATE PAIS SET PAIS = ?, LAST_UPDATE = SYSDATE WHERE PAIS_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, pais[1].toString());    // Nuevo nombre pais.
            preparedStatement.setInt(2, (Integer) pais[0]);        // Id del pais.
            preparedStatement.executeUpdate();
            paisId = (int) pais[0];
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "updatePais");
            paisId = 0;
        } finally {
           disconnect();
        }
        return paisId;
    }
    
    /**
     * Consultar todos los paises.
     * @return Object[][] con los datos de todos los paises consultados (id, pais).
     */
    public Object[][] getAllPaises() {
        connect();
        Object[][] paises = null;
        int count = 0;
        try {
            
            // Numero de paises.
            sentenciaSQL = "SELECT COUNT(*) FROM PAIS";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            resultSet = preparedStatement.executeQuery();

            // Numero de paises (COUNT).
            if(resultSet.next()) {
                count = resultSet.getInt(1);
            }
                       
            sentenciaSQL = "SELECT PAIS_ID, PAIS FROM PAIS ORDER BY 2";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            resultSet = preparedStatement.executeQuery();

            // Agregar a todos los paises al arreglo (migrar de resultSet a paises).
            int i = 0;
            paises = new Object[count][2];
            while(resultSet.next()) {
                paises[i][0] = (resultSet.getInt(1));      // Id del pais.
                paises[i][1] = (resultSet.getString(2));   // Nombre del pais.
                i++;
            }
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getAllPaises");
            paises = null;
        } finally {
           disconnect();
        }
        return paises;
    }  

    /**
     * Consulta el pais para un id. 
     * @param paisId Id del pais a consultar.
     * @return Object[] con los datos del pais consultado (id, pais).
     */
    public Object[] getPaisById(int paisId)
    {
        connect();
        Object[] pais = null;
        try {
            sentenciaSQL = "SELECT PAIS_ID, PAIS FROM PAIS WHERE PAIS_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, paisId);
            resultSet = preparedStatement.executeQuery();

            // Llena el arreglo pais con el resultado.
            pais = new Object[2];
            while(resultSet.next()) {
                pais[0] = (resultSet.getInt(1));
                pais[1] = (resultSet.getString(2));
            }
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getPaisById");
            pais = null;
        } finally {
           disconnect();
        }
        return pais;
    }
        
    /**
     * Consultar paises con nombre parecido.
     * @param pais nombre del pais a consultar.
     * @return Object[][] con los datos de los paises consultados (id, pais).
     */
    public Object[][] getPaisesByNombre(String pais) {
       connect();
       Object[][] paises = null;
       pais = "%" + pais + "%";
       try {
            
            // Cuenta todos los paises que tengan un nombre parecido.
            sentenciaSQL = "SELECT COUNT(PAIS_ID) FROM PAIS WHERE UPPER (PAIS) LIKE UPPER(?)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, pais);
            resultSet = preparedStatement.executeQuery();

            int count = 0;
            if(resultSet.next()) {
                count = resultSet.getInt(1);
            }
           
            // Misma consulta, pero ahora puede ser guardada en el arreglo.
            sentenciaSQL = "SELECT PAIS_ID, PAIS FROM PAIS WHERE UPPER (PAIS) LIKE UPPER(?) ORDER BY PAIS";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, pais);
            resultSet = preparedStatement.executeQuery();
           
            int i = 0;
            paises = new Object[count][2];
            while(resultSet.next()) {
               paises[i][0] = (resultSet.getInt(1));
               paises[i][1] = (resultSet.getString(2));
               i++;
            }
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getPaisesByNombre");
            paises = null;
        } finally {
           disconnect();
       }
       return paises;
    }

    /**
     * Eliminar un pais.
     * @param paisId
     * @return int que representa el resultado de la transaccion.
     */
    public int deletePais(int paisId) {
        connect();
        int status = 0;
        try {
            
            // Borrar el pais.
            sentenciaSQL = "DELETE FROM PAIS WHERE PAIS_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, paisId);
            status = preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "deletePais");
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
