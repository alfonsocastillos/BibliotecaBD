package dataBase.dao;

import dataBase.CustomConnection;
import dataBase.ConfigDataBase;
import java.sql.*;

/**
 * Clase encargada de consultar, insertar, eliminar y modificar la tabla Editorial.
 * @author alfonso
 */
public class EditorialDAO extends CustomConnection {
    
    /** 
     * Crear una editorial.
     * @param editorial nombre de la editorial.
     * @return String con el Id de la editorial creada.
     */
    public String saveEditorial(String editorial) {
        connect();
        String editorialId = "";
        try {
        
            // Genera el nuevo id (primera letra mas numero mas alto).
            sentenciaSQL = "SELECT SUBSTR(?, 1, 1) || LPAD(NVL(MAX(TO_NUMBER(SUBSTR(EDITORIAL_ID, 2, 3))) + 1, 1), 3, '0') FROM EDITORIAL " +
                    "WHERE SUBSTR(EDITORIAL_ID, 1, 1) LIKE UPPER(SUBSTR(?, 1, 1))";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, editorial);
            preparedStatement.setString(2, editorial);
            resultSet = preparedStatement.executeQuery();
            
            // Guarda el nuevo id.
            if(resultSet.next()) {
                editorialId = resultSet.getString(1);
            }
            
            // Inserta mandando todos los datos, incluido en nuevo id.
            sentenciaSQL = "INSERT INTO EDITORIAL VALUES(?, ?)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            // Asigna los valores del arreglo
            preparedStatement.setString(1, editorialId);    // Id (calculado).
            preparedStatement.setString(2, editorial);      // Genero (provisto).
            preparedStatement.executeUpdate();
            return editorialId;
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveEditorial");
            editorialId = "";
        } finally {
           disconnect();
        }
        return editorialId;
    }

    /**
     * Modificar una editorial.
     * @param editorial lista de datos de la editorial (Id, nuevo nombre).
     * @return String con el Id de la editorial actualizada.
     */
    public String updateEditorial(Object[] editorial) {
        connect();
        String editorialId = "";
        try {
            
           // Actualiza los datos.
           sentenciaSQL = "UPDATE EDITORIAL SET EDITORIAL = ? WHERE EDITORIAL_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, editorial[1].toString());    // Nuevo nombre editorial.
            preparedStatement.setString(2, editorial[0].toString());    // Id de la editorial.
            preparedStatement.executeUpdate();
            editorialId = editorial[0].toString();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "updateEditorial");
            editorialId = "";
        } finally {
           disconnect();
        }
        return editorialId;
    }

    /**
     * Consultar todas las editoriales.
     * @return Object[][] con la informacion (id, editorial) de las editoriales consultadas.
     */
    public Object[][] getAllEditoriales() {
       connect();
       Object[][] editoriales = null;
       int i = 0;
       int count = 0;
       try {

            // Numero de editoriales.
            sentenciaSQL = "SELECT COUNT(*) FROM EDITORIAL";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            resultSet = preparedStatement.executeQuery();

            // Numero de editoriales (COUNT).
            if(resultSet.next()) {
                count = resultSet.getInt(1);
            }

            // Arreglo de todas las editoriales (ID, editorial).
            editoriales = new Object[count][2];
            sentenciaSQL = "SELECT EDITORIAL_ID, EDITORIAL FROM EDITORIAL ORDER BY 2";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            resultSet = preparedStatement.executeQuery();
           
            // Agregar a todas las editoriales al arreglo (migrar de resultSet a editoriales)
            while(resultSet.next()) {
               editoriales[i][0] = resultSet.getString(1);
               editoriales[i][1] = resultSet.getString(2);
               i++;
            }
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getAllEditoriales");
            editoriales = null;
        } finally {
           disconnect();
        }
        return editoriales;
    }  

    /**
     * Consultar una editorial por id
     * @param editorialId Id de la editorial a consultar.
     * @return Object[] con los datos (Id, nombre) de la editorial
     */
    public Object[] getEditorialById(String editorialId) {
        connect();
        Object[] editorial = null;
        try {
            sentenciaSQL = "SELECT EDITORIAL_ID, EDITORIAL FROM EDITORIAL WHERE EDITORIAL_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, editorialId);
            resultSet = preparedStatement.executeQuery();

            // Llena el arreglo editorial con el resultado.
            editorial = new Object[2];
            while(resultSet.next()) {
                editorial[0] = (resultSet.getString(1));
                editorial[1] = (resultSet.getString(2));
            }
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getEditorialById");
            editorial = null;
        } finally {
           disconnect();
        }
        return editorial;
    }

    /**
     * Consultar todas las editoriales con un nombre parecido
     * @param nombre nombre a buscar.
     * @return Object[][] con la informacion (id, editorial) de las editoriales consultadas.
     */
    public Object[][] getEditorialByNombre(String nombre) {
       connect();
       Object[][] editoriales = null;
       int i = 0;
       int count = 0;
       nombre = "%" + nombre + "%";
       try {
            // Cuenta todas las editoriales que tengan un nombre parecido
            sentenciaSQL = "SELECT COUNT(EDITORIAL_ID) FROM EDITORIAL WHERE UPPER (EDITORIAL) LIKE UPPER(?)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, nombre);
            resultSet = preparedStatement.executeQuery();
           
            if(resultSet.next()) {
                count = resultSet.getInt(1);
            }
           
            // Misma consulta, pero ahora puede ser guardada en el arreglo.
            sentenciaSQL = "SELECT EDITORIAL_ID, EDITORIAL FROM EDITORIAL WHERE UPPER (EDITORIAL) LIKE UPPER(?) ORDER BY EDITORIAL";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, nombre);
            resultSet = preparedStatement.executeQuery();
            editoriales = new Object[count][2];
            while(resultSet.next()) {
                editoriales[i][0] = resultSet.getString(1);
                editoriales[i][1] = resultSet.getString(2);
                i++;
            }
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getEditorialByNombre");
            editoriales = null;
        } finally {
           disconnect();
        }
        return editoriales;
    }

    /**
     * Borrar una editorial
     * @param editorialId Id de la editorial a borrar.
     * @return int representando el resultado de la operacion.
     */
    public int deleteEditorial(String editorialId) {
        connect();
        int state = 0;
        try {
            
            // Borrar la editorial
            sentenciaSQL = "DELETE FROM EDITORIAL WHERE EDITORIAL_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, editorialId);
            state = preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "deleteEditorial");
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
