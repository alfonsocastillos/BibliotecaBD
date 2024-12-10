package dataBase.dao;

import dataBase.CustomConnection;
import dataBase.ConfigDataBase;
import java.sql.*;

/**
 * Clase encargada de consultar, insertar, eliminar y modificar la tabla Escolaridad.
 * @author alfonso
 */
public class EscolaridadDAO extends CustomConnection {
    
    /**
     * Crear una Escolaridad
     * @param escolaridad nombre de la escolaridad.
     * @return int con el Id de la escolaridad creada.
     */
    public int saveEscolaridad(String escolaridad) {
        connect();
        int escolaridadId = 0;
        try {
            
            // Genera el nuevo id.
            sentenciaSQL = "SELECT MAX(ESCOLARIDAD_ID) + 1 FROM ESCOLARIDAD";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            resultSet = preparedStatement.executeQuery();
            
            // Guarda el nuevo id.
            if(resultSet.next()) {
                escolaridadId = resultSet.getInt(1);
            }
            
            // Inserta mandando todos los datos, incluido en nuevo id.
            sentenciaSQL = "INSERT INTO ESCOLARIDAD VALUES(?, ?)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            
            // Asigna los valores.
            preparedStatement.setInt(1, escolaridadId);     // Id (calculado).
            preparedStatement.setString(2, escolaridad);    // Escolaridad (provisto).
            preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveEscolaridad");
            escolaridadId = 0;
        } finally {
           disconnect();
        }
        return escolaridadId;
    }
    
    /**
     * Modificar una escolaridad.
     * @param escolaridad datos de la escolaridad (Id, nuevo nombre).
     * @return int con el Id de la escolaridad modificada.
     */
    public int updateEscolaridad(Object[] escolaridad) {
        connect();
        int escolaridadId = 0;
        try {
           
            // Actualiza los datos.
            sentenciaSQL = "UPDATE ESCOLARIDAD SET NIVEL = ? WHERE ESCOLARIDAD_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, escolaridad[1].toString());  // Nuevo nombre Escolaridad.
            preparedStatement.setInt(2, (int) escolaridad[0]);          // Id del Escolaridad.
            preparedStatement.executeUpdate();
            escolaridadId = (int) escolaridad[0];
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "updateEscolaridad");
            escolaridadId = 0;
        } finally {
           disconnect();
        }
        return escolaridadId;
    }
    
    /**
     * Consultar todos las escolaridades.
     * @return Object[][] con los datos de todas las escolaridades (Id, Escolaridad).
     */
    public Object[][] getAllEscolaridad() {
        connect();
        Object[][] escolaridades = null;
        try {
            
            // Numero de escolaridades.
            sentenciaSQL = "SELECT COUNT(*) FROM ESCOLARIDAD";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            resultSet = preparedStatement.executeQuery();

            // Numero de escolaridades (COUNT).
            int count = 0;
            if(resultSet.next()) {
                count = resultSet.getInt(1);
            }

            // Arreglo de todas las escolaridades (Id, Escolaridad).
            escolaridades = new Object[count][2];
            sentenciaSQL = "SELECT ESCOLARIDAD_ID, NIVEL FROM ESCOLARIDAD ORDER BY 2";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            resultSet = preparedStatement.executeQuery();

            // Agregar a todas las escolaridades al arreglo.
            int i = 0;
            while(resultSet.next()) {
                escolaridades[i][0] = (resultSet.getInt(1));      // Id de la ESCOLARIDAD.
                escolaridades[i][1] = (resultSet.getString(2));   // Nombre de la ESCOLARIDAD.
                i++;
            }
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getAllEscolaridad");
            escolaridades = null;
        } finally {
           disconnect();
        }
        return escolaridades;
    }
    
    /**
     * Consulta la escolaridad para un Id.
     * @param escolaridadId Id de la escolaridad a consultar.
     * @return Object[] con los datos de la escolaridad (Id, Escolaridad).
     */
    public Object[] getEscolaridadById(int escolaridadId) {
        connect();
        Object[] escolaridad = null;
        try {
            sentenciaSQL = "SELECT ESCOLARIDAD_ID, NIVEL FROM ESCOLARIDAD WHERE ESCOLARIDAD_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, escolaridadId);
            resultSet = preparedStatement.executeQuery();

            // Llena el arreglo pais con el resultado.
            escolaridad = new Object[2];
            while(resultSet.next()) {
                escolaridad[0] = resultSet.getInt(1);
                escolaridad[1] = resultSet.getString(2);
            }           
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getEscolaridadById");
            escolaridad = null;
        } finally {
           disconnect();
        }
        return escolaridad;
    }
                    
     // Consultar paises con nombre parecido (id, pais)
    /**
     * Consultar escolaridades con nombre parecido.
     * @param escolaridad nombre de la escolaridad.
     * @return Object[][] con los datos de las escolaridades consultadas (Id, Escolaridad).
     */
    public Object[][] getEscolaridadByNombre(String escolaridad) {
       connect();
       Object[][] escolaridades = null;
       escolaridad = "%" + escolaridad + "%";
       try {
           
            // Cuenta todas las escolaridades que tengan un nombre parecido.
            sentenciaSQL = "SELECT COUNT(ESCOLARIDAD_ID) FROM ESCOLARIDAD WHERE UPPER (NIVEL) LIKE UPPER(?)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, escolaridad);
            resultSet = preparedStatement.executeQuery();

            int count = 0;
            if(resultSet.next()) {
                count = resultSet.getInt(1);
            }
           
            // Misma consulta, pero ahora puede ser guardada en el arreglo.
            escolaridades = new Object[count][2];
            sentenciaSQL = "SELECT ESCOLARIDAD_ID, NIVEL FROM ESCOLARIDAD WHERE UPPER (NIVEL) LIKE UPPER(?) ORDER BY NIVEL";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, escolaridad);
            resultSet = preparedStatement.executeQuery();
           
            int i = 0;
            while(resultSet.next()) {
               escolaridades[i][0] = (resultSet.getInt(1));
               escolaridades[i][1] = (resultSet.getString(2));
               i++;
            }
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getEscolaridadByNombre");
            escolaridades = null;
        } finally {
           disconnect();
        }
        return escolaridades;
    }
        
    /**
     * Elimina una escolaridad
     * @param escolaridadId Id de la escolaridad a eliminar.
     * @return int representando el resultado de la transaccion.
     */
    public int deleteEscolaridad(int escolaridadId) {
        connect();
        int state = 0;
        try {
            
            // Borrar la escolaridad.
            sentenciaSQL = "DELETE FROM ESCOLARIDAD WHERE ESCOLARIDAD_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, escolaridadId);
            state = preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "deleteEscolaridad");
            if(ex.getErrorCode() == 2292) {
                state = 0;
            } else {
                state = 2;
            }
        } finally {
           disconnect();
        }
        return state;
    }
}
