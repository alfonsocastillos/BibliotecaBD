package dataBase.dao;

import dataBase.CustomConnection;
import dataBase.ConfigDataBase;
import java.sql.*;

/**
 * Clase encargada de consultar, insertar, eliminar y modificar la tabla Genero.
 * @author alfonso
 */
public class GeneroDAO extends CustomConnection {
    
    /**
     * Crear un genero.
     * @param genero nombre del genero a crear.
     * @return int con el Id del genero creado.
     */
    public int saveGenero(String genero) {
        connect();
        int generoId = 0;
        try {

            // Genera el nuevo id
            sentenciaSQL = "SELECT MAX(GENERO_ID) + 1 FROM GENERO";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            resultSet = preparedStatement.executeQuery();
            
            // Guarda el nuevo id
            if(resultSet.next()) {
                generoId = resultSet.getInt(1);
            }
            
            // Inserta mandando todos los datos, incluido en nuevo id.
            sentenciaSQL = "INSERT INTO GENERO VALUES(?, ?, SYSDATE)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            
            // Asigna los valores del arreglo.
            preparedStatement.setInt(1, generoId);      // Id (calculado).
            preparedStatement.setString(2, genero);     // Genero (provisto).
            preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveGenero");
            generoId = 0;
        } finally {
           disconnect();
        }
        return generoId;
    }
    
    /**
     * Modificar un genero.
     * @param genero datos del genero (Id, nuevo nombre).
     * @return int con Id del genero modificado.
     */
    public int updateGenero(Object[] genero) {
       connect();
       int generoId = 0;
       try {
           
            // Actualiza los datos.
            sentenciaSQL = "UPDATE GENERO SET GENERO = ?, LAST_UPDATE = SYSDATE WHERE GENERO_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, genero[1].toString());   // Nuevo nombre genero.
            preparedStatement.setInt(2, (int) genero[0]);       // Id del genero
            preparedStatement.executeUpdate(); 
            generoId = (int) genero[0];
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "updateGenero");
            generoId = 0;
        } finally {
           disconnect();
        }
       return generoId;
    }
    
    /**
     * Consultar todos los idiomas.
     * @return Object[][] con los datos de los generos consultados (id, genero).
     */
    public Object[][] getAllGeneros() {
        connect();
        Object[][] generos = null;
        try {
            
            // Numero de generos.
            sentenciaSQL = "SELECT COUNT(*) FROM GENERO";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            resultSet = preparedStatement.executeQuery();

            // Numero de generos (COUNT)
            int count = 0;
            if(resultSet.next()) {
                count = resultSet.getInt(1);
            }

            // Arreglo de todos los generos (ID, genero).
            sentenciaSQL = "SELECT GENERO_ID, GENERO FROM GENERO ORDER BY 2";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            resultSet = preparedStatement.executeQuery();

            // Agregar a todos los generos al arreglo (migrar de resultSet a generos)
            int i = 0;
            generos = new Object[count][2];
            while(resultSet.next()) {
                generos[i][0] = (resultSet.getInt(1));
                generos[i][1] = (resultSet.getString(2));
                i++;
            }
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getAllGeneros");
            generos = null;
        } finally {
           disconnect();
        }
        return generos;
    }  

    /**
     * Consulta el genero para un id.
     * @param generoId Id del genero a consultar.
     * @return Object[] con los datos del genero (Id, Genero).
     */
    public Object[] getGeneroById(int generoId) {
        connect();
        Object[] genero = null;
        try {
            sentenciaSQL = "SELECT GENERO_ID, GENERO FROM GENERO WHERE GENERO_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, generoId);
            resultSet = preparedStatement.executeQuery();

            // Llena el arreglo genero con el resultado.
            genero = new Object[2];
            while(resultSet.next()) {
                genero[0] = (resultSet.getInt(1));
                genero[1] = (resultSet.getString(2));
            }
        }
        catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getGeneroById");
            genero = null;
        } finally {
           disconnect();
        }
        return genero;
    }        
        
    /**
     * Consultar generos con nombre parecido.
     * @param genero nombre del genero.
     * @return Object[][] con los datos de los generos consultados (Id, Genero).
     */
    public Object[][] getGenerosByNombre(String genero) {
       connect();
       Object[][] generos = null;
       genero = "%" + genero + "%";
       try {
           
            // Todos los generos que tengan un nombre parecido.
            sentenciaSQL = "SELECT COUNT(GENERO_ID) FROM GENERO WHERE UPPER (GENERO) LIKE UPPER(?)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, genero);
            resultSet = preparedStatement.executeQuery();

            int count = 0;
            if(resultSet.next()) {
                count = resultSet.getInt(1);
            }

            // Misma consulta, pero ahora puede ser guardada en el arreglo.
            generos = new Object[count][2];
            sentenciaSQL = "SELECT GENERO_ID, GENERO FROM GENERO WHERE UPPER (GENERO) LIKE UPPER(?) ORDER BY GENERO";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, genero);
            resultSet = preparedStatement.executeQuery();

            int i = 0;
            while(resultSet.next()) {
                generos[i][0] = (resultSet.getInt(1));
                generos[i][1] = (resultSet.getString(2));
                i++;
            }
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getGenerosByNombre");
            generos = null;
        } finally {
           disconnect();
        }
        return generos;
    }

    /**
     * Eliminar un genero.
     * @param generoId Id del genero a eliminar
     * @return int representando el resultado de la transaccion.
     */
    public int deleteGenero(int generoId) {
        connect();
        int state = 0;
        try {
            
            // Borrar el genero.
            sentenciaSQL = "DELETE FROM GENERO WHERE GENERO_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, generoId);
            state = preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "deleteGenero");
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