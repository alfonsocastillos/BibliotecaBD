package dataBase.dao;

import dataBase.CustomConnection;
import dataBase.ConfigDataBase;
import java.sql.*;

/**
 * Clase encargada de consultar, insertar, eliminar y modificar la tabla Idioma,
 * @author alfonso
 */

public class IdiomaDAO extends CustomConnection {
   
    /**
     * Crear un idioma.
     * @param idioma nombre del idioma.
     * @return int con el Id del idioma creado.
     */
    public int saveIdioma(String idioma) {
        connect();
        int idiomaId = 0;
        try {
            
            // Genera el nuevo Id.
            sentenciaSQL = "SELECT MAX(IDIOMA_ID) + 1 FROM IDIOMA";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            resultSet = preparedStatement.executeQuery();
            
            // Guarda el nuevo Id.
            if(resultSet.next()) {
                idiomaId = resultSet.getInt(1);
            }
            
            // Inserta mandando todos los datos, incluido en nuevo Id.
            sentenciaSQL = "INSERT INTO IDIOMA VALUES(?, ?, SYSDATE)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, idiomaId);  // Id (calculado).
            preparedStatement.setString(2, idioma); // Idioma (provisto).
            preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveIdioma");          
            idiomaId = 0;
        } finally {
           disconnect();
        }
        return idiomaId;
    }
    
    /**
     * Modificar un idioma.
     * @param idioma datos del idioma a modificar (Id, nuevo nombre del idioma).
     * @return int con el Id del idioma modificado.
     */
    public int updateIdioma(Object[] idioma) {
       connect();
       int idiomaId = 0;
       try {
            sentenciaSQL = "UPDATE IDIOMA SET IDIOMA = ?, LAST_UPDATE = SYSDATE WHERE IDIOMA_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, idioma[1].toString());   // Nuevo nombre idioma.
            preparedStatement.setInt(2, (Integer) idioma[0]);       // Id del idioma.
            preparedStatement.executeUpdate();
            idiomaId = (int) idioma[0];
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "updateIdioma");
            idiomaId = 0;
        } finally {
           disconnect();
        }
        return idiomaId;
    }
    
    /**
     * Consultar todos los idiomas.
     * @return Object[][] con los datos de todos los idiomas consultados (id, idioma).
     */
    public Object[][] getAllIdiomas() {
        connect();
        Object[][] idiomas = null;
        try {
            // Numero de idiomas.
            sentenciaSQL = "SELECT COUNT(*) FROM IDIOMA";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            resultSet = preparedStatement.executeQuery();
           
            // Numero de idiomas (COUNT).
            int count = 0;
            if(resultSet.next()) {
                count = resultSet.getInt(1);
            }

            // Arreglo de todos los idiomas (Id, idioma).
            sentenciaSQL = "SELECT IDIOMA_ID, IDIOMA FROM IDIOMA ORDER BY 2";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            resultSet = preparedStatement.executeQuery();
           
            // Agregar a todos los autores al arreglo (migrar de resultSet a actores)
            int i = 0;
            idiomas = new Object[count][2];
            while(resultSet.next()) {
               idiomas[i][0] = (resultSet.getInt(1));
               idiomas[i][1] = (resultSet.getString(2));
               i++;
            }
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getAllIdiomas");
            idiomas = null;
        } finally {
           disconnect();
        }
        return idiomas;
    }  

    /**
     * Consulta el idioma para un Id.
     * @param idiomaId Id del idioma a consultar.
     * @return Object[] con los datos del idioma (Id, idioma).
     */
    public Object[] getIdiomaById(int idiomaId)
    {
        connect();
        Object[] idioma = null;
        try {
            sentenciaSQL = "SELECT IDIOMA_ID, IDIOMA FROM IDIOMA WHERE IDIOMA_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, idiomaId);
            resultSet = preparedStatement.executeQuery();

            // Llena el arreglo idioma con el resultado.
            idioma = new Object[2];
            while(resultSet.next()) {
                idioma[0] = (resultSet.getInt(1));
                idioma[1] = (resultSet.getString(2));
            }
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getIdiomaById");
            idioma = null;
        } finally {
           disconnect();
        }
        return idioma;
    }
            
    /**
     * Consultar idiomas con nombre parecido.
     * @param idioma nombre del idioma.
     * @return Object[][] con los datos de los idiomas consultados (Id, idioma).
     */
    public Object[][] getIdiomasByNombre(String idioma) {
        connect();
        Object[][] idiomas = null;
        idioma = "%" + idioma + "%";
        try {
            
            // Cuenta todos los idiomas que tengan un nombre parecido.
            sentenciaSQL = "SELECT COUNT(IDIOMA_ID) FROM IDIOMA WHERE UPPER (IDIOMA) LIKE UPPER(?)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, idioma);
            resultSet = preparedStatement.executeQuery();

            int count = 0;
            if(resultSet.next()) {
                count = resultSet.getInt(1);
            }

            // Misma consulta, pero ahora puede ser guardada en el arreglo.
            sentenciaSQL = "SELECT IDIOMA_ID, IDIOMA FROM IDIOMA WHERE UPPER (IDIOMA) LIKE UPPER(?) ORDER BY IDIOMA";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, idioma);
            resultSet = preparedStatement.executeQuery();

            int i = 0;
            idiomas = new Object[count][2];
            while(resultSet.next()) {
                idiomas[i][0] = (resultSet.getInt(1));
                idiomas[i][1] = (resultSet.getString(2));
                i++;
            }
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getIdiomasByNombre");
            idiomas = null;
        } finally {
           disconnect();
        }
        return idiomas;
    }

    /**
     * Eliminar un idioma.
     * @param idiomaId
     * @return int representando el resultado de la transaccion.
     */
    public int deleteIdioma(int idiomaId) {
        connect();
        int status = 0;
        try {
            
            // Borrar el idioma.
            sentenciaSQL = "DELETE FROM IDIOMA WHERE IDIOMA_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, idiomaId);
            status = preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "deleteIdioma");
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
