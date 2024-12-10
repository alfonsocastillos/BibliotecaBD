package dataBase.dao;

import dataBase.CustomConnection;
import dataBase.ConfigDataBase;
import java.sql.*;

/**
 * Clase encargada de consultar, insertar, eliminar y modificar la tabla Autor.
 * @author alfonso
 */
public class AutorDAO extends CustomConnection {
    
    /**
     * Obtiene los datos de un autor (id, nombre, apellido, pais) mediante su ID.
     * @param autorId Id del autor a consultar.
     * @return Object[] con los datos del autor.
     */
    public Object[] getAutorById(String autorId) {
        connect();
        Object[] autor = null;
        try {
            sentenciaSQL = "SELECT AUTOR_ID, NOMBRE, APELLIDO_PAT, PAIS FROM AUTOR JOIN PAIS USING (PAIS_ID) WHERE AUTOR_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, autorId); 
            resultSet = preparedStatement.executeQuery();
           
            // Llena el arreglo actor con el resultado.
            int i = 0;
            autor = new Object[4];
            while(resultSet.next()) {
                autor[0] = (resultSet.getString(1));
                autor[1] = (resultSet.getString(2));
                autor[2] = (resultSet.getString(3));
                autor[3] = (resultSet.getString(4));
                i++;
            }
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getAutorById");
            autor = null;
        } finally {
            disconnect();
        }
        return autor;
    }
    
    /**
     * Regresa una lista de todos los autores de un libro (id, nombre + apellido).
     * @param libroId Id del libro a consultar.
     * @return Object[][] lista de todos los autores participantes en el libro.
     */
    public Object[][] getAutoresByLibroId(int libroId) {
        connect();
       Object[][] autores = null;
        int i = 0;
        int count = 0;
        try {

            // Primero se cuenta el numero de autores en el libro.
            sentenciaSQL = "SELECT COUNT (AUTOR_ID) FROM AUTOR JOIN AUTORIA USING (AUTOR_ID) JOIN LIBRO USING (LIBRO_ID) WHERE LIBRO_ID = ? ";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, libroId);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                count = resultSet.getInt(1);
            }

            autores = new Object[count][2];
            sentenciaSQL = "SELECT AUTOR_ID, NOMBRE || ' ' || APELLIDO_PAT FROM AUTOR JOIN AUTORIA USING (AUTOR_ID) JOIN LIBRO USING (LIBRO_ID) WHERE LIBRO_ID = ? ORDER BY 2";   
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, libroId);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                autores[i][0] = resultSet.getString(1);
                autores[i][1] = resultSet.getString(2);
                i++;
            }
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getAutoresByLibroId");
            autores = null;
        } finally {
            disconnect();
        }
        return autores;
    }

    /**
     * Obtiene todos los autores (Id, nombre/apellido) cuyo nombre o apellido se parezcan a nombre_apellido.
     * @param nombreApellido nombre o apellido por el que se quiere filtrar.
     * @return Object[][] la lista de autores.
     */
    public Object[][] getAutoresByNombreApellido(String nombreApellido) {
       connect();
       Object[][] autores = null;
       int i = 0;
       int count = 0;
       nombreApellido = "%" + nombreApellido + "%";
       try {
           
            // Cuenta todos los actores que tengan un nombre parecido.
            sentenciaSQL = "SELECT COUNT(AUTOR_ID) FROM " +
                    "(SELECT AUTOR_ID FROM AUTOR WHERE UPPER (NOMBRE) LIKE UPPER(?) " +
                    "UNION " +
                    "SELECT AUTOR_ID FROM AUTOR WHERE UPPER (APELLIDO_PAT) LIKE UPPER(?))";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, nombreApellido);
            preparedStatement.setString(2, nombreApellido);
            resultSet = preparedStatement.executeQuery();
           
            if(resultSet.next()) {
                count = resultSet.getInt(1);
            }
           
            // Misma consulta, pero ahora puede ser guardada en el arreglo.
            autores = new Object[count][2];
            sentenciaSQL = "SELECT AUTOR_ID, NOMBRE || ' ' || APELLIDO_PAT FROM AUTOR WHERE UPPER (NOMBRE) LIKE UPPER(?) " +
                    "UNION " +
                    "SELECT AUTOR_ID, NOMBRE || ' ' || APELLIDO_PAT FROM AUTOR WHERE UPPER (APELLIDO_PAT) LIKE UPPER(?) " +
                    "ORDER BY 2";
           preparedStatement = connection.prepareStatement(sentenciaSQL);
           preparedStatement.setString(1, nombreApellido);
           preparedStatement.setString(2, nombreApellido);
           resultSet = preparedStatement.executeQuery();
           
            while(resultSet.next()) {
                autores[i][0] = (resultSet.getString(1));     // Id del autor.
                autores[i][1] = (resultSet.getString(2));     // Nombre y apellido del autor.
                i++;
            }           
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getAutoresByNombreApellido");
            autores = null;
        } finally {
            disconnect();
        }
        return autores;
    }
    
    /**
     * Genera un nuevo autor (junto con su ID).
     * @param autor lista de datos del autor (Nombre, Apellido, id de su pais).
     * @return String con el Id del autor creado.
     */
    public String saveAutor(Object[] autor) {
        
        // Se conecta a la base de datos.
        connect();
        String autorId = "";
        try {
            
            // Genera el nuevo id: Primera letra de su pais + (el numero mas alto + 1).
            sentenciaSQL = "SELECT SUBSTR (?, 1, 1) || LPAD(NVL(MAX(TO_NUMBER(SUBSTR(AUTOR_ID, 2, 3))) + 1, 1), 3, '0') FROM AUTOR " +
                    "WHERE SUBSTR(AUTOR_ID, 1, 1) LIKE SUBSTR (?, 1, 1)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            PaisDAO paisDAO = new PaisDAO();
            preparedStatement.setString(1, paisDAO.getPaisById((Integer) autor[2])[1].toString());
            preparedStatement.setString(2, paisDAO.getPaisById((Integer) autor[2])[1].toString());
            resultSet = preparedStatement.executeQuery();
            
            // Guarda el nuevo id.
            if(resultSet.next()) {
                autorId = resultSet.getString(1);
            }
            
            // Inserta mandando todos los datos, incluido en nuevo id.
            sentenciaSQL = "INSERT INTO AUTOR VALUES(?, ?, ?, SYSDATE, ?)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            
            // Asigna los valores del arreglo.
            preparedStatement.setString(1, autorId);                    // Id (calculado).
            preparedStatement.setString(2, autor[0].toString());   // Nombre (provisto).
            preparedStatement.setString(3, autor[1].toString());   // Apellido (provisto).
            preparedStatement.setInt(4, (Integer) autor[2]);       // Pais id (provisto).
            preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveAutor");
            autorId = "";
        } finally {
           disconnect();
        }
        return autorId;
    }

    /**
     * Asocia a un autor con un libro (autor, libro).
     * @param autoria pareja autor-libro. 
     * @return int representando el resultado de la transaccion.
     */
    public int saveAutoria(Object[] autoria) {
        connect();
        int status = 0;
        try {
            sentenciaSQL = "INSERT INTO AUTORIA VALUES(?, ?)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            
            // Asigna los valores del arreglo.
            preparedStatement.setString(1, autoria[0].toString()); // Autor id.
            preparedStatement.setInt(2, (Integer) autoria[1]);     // Libro id.
            preparedStatement.executeUpdate();
            
            // Transaccion completada con exito.
            status = 1;
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveAutoria");
            status = 0;
        } finally {
           disconnect();
        }
        return status;
    }

    /**
     * Actualiza la informacion de un autor.
     * @param autor lista de los datos del autor (Id, Nombre, Apellido, Pais ID).
     * @return String con el Id del autor actualizado.
     */
    public String updateAutor(Object autor[]) {
       connect();
       String autorId = "";
       try {
           
            // Actualiza los datos.
            sentenciaSQL = "UPDATE AUTOR SET NOMBRE = ?, APELLIDO_PAT = ?, PAIS_ID = ?, LAST_UPDATE = SYSDATE WHERE AUTOR_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, autor[1].toString());   // Nuevo nombre del autor.
            preparedStatement.setString(2, autor[2].toString());   // Nuevo apellido del autor.
            preparedStatement.setInt(3, (Integer) autor[3]);       // Nuevo pais del autor.
            preparedStatement.setString(4, autor[0].toString());   // Id del autor.
            preparedStatement.executeUpdate();
            autorId =  autor[0].toString();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "updateAutor");
            autorId = "";
        } finally {
           disconnect();
        }
        return autorId;
    }   
    
    /**
     * Borra a un autor de la base de datos
     * @param autorId Id del autor a eliminar.
     * @return int representando el resultado de la transaccion. 
     */
    public int deleteAutor(String autorId) {
        connect();
        int state = 0; 
        try {
            sentenciaSQL = "DELETE FROM AUTOR WHERE AUTOR_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, autorId);
            
            // Un valor de 1 indica que la operacion fue exitosa.
            state = preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "deleteAutor");
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
       
    /**
     * Borra a un autor de un libro.
     * @param autorId Id del autor a borrar de la relacion.
     * @param libroId Id del libro a borrar de la relacion.
     * @return int representando el resultado de la transaccion.
     */
    public int deleteAutoria(String autorId, int libroId) {
        connect();
        int state = 0;
        try {
            sentenciaSQL = "DELETE FROM AUTORIA WHERE LIBRO_ID = ? AND AUTOR_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, libroId);
            preparedStatement.setString(2, autorId);
            
            // Un valor de 1 indica que la operacion fue exitosa.
            state = preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "deleteAutoria");
            if(ex.getErrorCode() ==  2292) {
                state = 0;
            } else {
                state = 2;
            }     
        } finally {
           disconnect();
        }
        return state;
    }
    
    /**
     * Borra todos los autores de un libro-
     * @param libroId Id del libro del cual borrar la relacion con autores.
     * @return int representando el resultado de la transaccion.
     */
    public int deleteAutoriaFromLibro(int libroId) {
        connect();
        int state = 0;
        try {
            sentenciaSQL = "DELETE FROM AUTORIA WHERE LIBRO_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, libroId);
            
            // Un valor de 0 o 1 indica que la operacion fue exitosa.
            state = preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "deleteAutoriaFromLibro");
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
