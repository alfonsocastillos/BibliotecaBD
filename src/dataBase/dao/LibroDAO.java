package dataBase.dao;

import dataBase.CustomConnection;
import dataBase.ConfigDataBase;
import java.sql.*;
import java.util.Calendar;

/**
 * Clase encargada de operaciones a la base de datos en la tabla LIBRO
 * @author alfonso
 */
public class LibroDAO extends CustomConnection {
    
    /**
     * Regresa todos los libros con un titulo o genero parecido.
     * @param descripcion titulo o genero a buscar.
     * @return Object[][] con los datos de los libros consultados (LIBRO_ID, TITULO, EDICION, EDITORIAL, GENERO, ANIO, NUM_PAGINAS, IDIOMA).
     */
    public Object[][] getLibrosByDescripcion(String descripcion) {
        connect();
        Object[][] libros = null;
        descripcion = "%" + descripcion + "%";
        try {
            
            // Cuenta los registros en la base de datos.
            sentenciaSQL = "SELECT COUNT (LIBRO_ID) FROM (" +
                    "SELECT LIBRO_ID FROM LIBRO WHERE UPPER(TITULO) LIKE UPPER(?) " +
                    "UNION " +
                    "SELECT LIBRO_ID FROM LIBRO JOIN GENERO USING (GENERO_ID) WHERE UPPER(GENERO) LIKE UPPER(?))";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, descripcion);
            preparedStatement.setString(2, descripcion);
            resultSet = preparedStatement.executeQuery();
                
            int count = 0;
            if(resultSet.next()) {
                count = resultSet.getInt(1);
            }
            
            // Consulta los registros.
            sentenciaSQL = "SELECT LIBRO_ID, TITULO, EDICION, EDITORIAL, GENERO, ANIO, NUM_PAGINAS, IDIOMA FROM LIBRO JOIN IDIOMA USING (IDIOMA_ID) " +
                    "JOIN EDITORIAL USING (EDITORIAL_ID) JOIN GENERO USING (GENERO_ID) WHERE UPPER(TITULO) LIKE UPPER(?) OR UPPER(GENERO) LIKE UPPER(?) ORDER BY TITULO";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, descripcion);
            preparedStatement.setString(2, descripcion);
            resultSet = preparedStatement.executeQuery();
            
            // Recorre el result set para obtener los datos y asignarlos al arreglo.
            int i = 0;
            libros = new Object[count][8];
            while(resultSet.next()) {
                libros[i][0] = resultSet.getInt(1);         // Id del libro.
                libros[i][1] = resultSet.getString(2);      // Titulo del libro.
                libros[i][2] = resultSet.getInt(3);         // Edicion del libro.
                libros[i][3] = resultSet.getString(4);      // Editorial del libro.
                libros[i][4] = resultSet.getString(5);      // Genero del libro.
                Calendar calendar = Calendar.getInstance(); // Año de publicacion.
                calendar.setTime(resultSet.getDate(6));
                libros[i][5] = calendar.get(Calendar.YEAR);
                libros[i][6] = resultSet.getInt(7);         // Numero de paginas 
                libros[i][7] = resultSet.getString(8);      // Idioma del libro
                i++;
            }
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getLibrosByDescripcion");
            libros = null;
        } finally {
           disconnect();
        }
        return libros;
    }
    
    /**
     * Regresa los datos de un libro mediante su Id.
     * @param libroId Id del libro a consultar.
     * @return Object[] con los datos del libro consultado (LIBRO_ID, TITULO, EDICION, EDITORIAL, GENERO, ANIO, NUM_PAGINAS, IDIOMA, PAIS).
     */
    public Object[] getLibroById(int libroId) {
        connect();
        Object libro[] = null;
        try {
           
            // Consulta los registros.
            sentenciaSQL = "SELECT LIBRO_ID, TITULO, EDICION, EDITORIAL, GENERO, ANIO, NUM_PAGINAS, IDIOMA, PAIS FROM LIBRO JOIN IDIOMA USING (IDIOMA_ID) " +
                    "JOIN EDITORIAL USING (EDITORIAL_ID) JOIN GENERO USING (GENERO_ID) JOIN PAIS USING (PAIS_ID) WHERE LIBRO_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, libroId);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            
            // Recupera los valores
            libro = new Object[9];
            libro[0] = resultSet.getInt(1);             // Id.
            libro[1] = resultSet.getString(2);          // Titulo.
            libro[2] = resultSet.getInt(3);             // Edicion.
            libro[3] = resultSet.getString(4);          // Editorial. 
            libro[4] = resultSet.getString(5);          // Genero.
            Calendar calendar = Calendar.getInstance(); // Año.
            calendar.setTime(resultSet.getDate(6));
            libro[5] = calendar.get(Calendar.YEAR);
            libro[6] = resultSet.getInt(7);             // No. pags.
            libro[7] = resultSet.getString(8);          // Idioma.
            libro[8] = resultSet.getString(9);          // Pais.
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getLibroById");
            libro = null;
        } finally {
           disconnect();
        }
        return libro;
    }
    
    /**
     * Guarda un libro nuevo.
     * @param libro lista de datos del libro (ID, TITULO, EDICION, ANIO, NUM_PAGS, IDIOMA_ID, GENERO_ID, PAIS_ID EDITORIAL_ID).
     * @return int con el Id del nuevo libro.
     */
    public int saveLibro(Object[] libro) {
        connect();
        int libroId = 0;
        try {
            
            // Genera el nuevo Id
            sentenciaSQL = "SELECT LPAD(SUBSTR(EXTRACT(YEAR FROM SYSDATE), 3, 2), 2, '0') || LPAD(EXTRACT(MONTH FROM SYSDATE), 2, '0') || " +
                    "LPAD(NVL(MAX(TO_NUMBER(SUBSTR(LIBRO_ID, 5, 3))) + 1, 1), 2, '0') FROM LIBRO " +
                    "WHERE SUBSTR(LIBRO_ID, 1, 4) = LPAD(SUBSTR(EXTRACT(YEAR FROM SYSDATE), 3, 2), 2, '0') || LPAD(EXTRACT(MONTH FROM SYSDATE), 2, '0')";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            resultSet = preparedStatement.executeQuery();
            
            // Guarda el nuevo Id.
            if(resultSet.next()) {
                libroId = resultSet.getInt(1);
            }
            
            // Inserta mandando todos los datos, incluido en nuevo Id.
            sentenciaSQL = "Insert INTO LIBRO VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, libroId);                      // Id del libro.
            preparedStatement.setString(2, libro[1].toString());       // Titulo.
            preparedStatement.setInt(3, (Integer) libro[2]);           // Edicion.
            String repeated = new String(new char[4 - ((Integer) libro[3]).toString().length()]).replace("\0", "0");
            String date = repeated + ((Integer) libro[3]).toString() + "-01-01";
            preparedStatement.setDate(4, java.sql.Date.valueOf(date)); // Año.
            preparedStatement.setInt(5, (Integer) libro[4]);           // No. paginas.
            preparedStatement.setInt(6, (Integer) libro[5]);           // Id idioma.
            preparedStatement.setInt(7, (Integer) libro[6]);           // Id genero.
            preparedStatement.setInt(8, (Integer) libro[7]);           // Id pais.
            preparedStatement.setString(9, libro[8].toString());       // Id Editorial.
            preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveLibro");
            libroId = 0;
        } finally {
           disconnect();
        }
        return libroId;
    }
        
    /**
     * Actualiza un libro nuevo.
     * @param libro datos del libro (LIBRO_ID, TITULO, EDICION, ANIO, NUM_PAGS, IDIOMA_ID, GENERO_ID, PAIS_ID EDITORIAL_ID).
     * @return int con el Id del libro actualizado.
     */
    public int updateLibro(Object libro[]) {
       connect();
       int libroId = 0;
       try {
           
            // Actualiza los datos
            sentenciaSQL = "UPDATE LIBRO SET TITULO = ?, EDICION = ?, ANIO = ?, NUM_PAGINAS = ?, IDIOMA_ID = ?, GENERO_ID = ?, PAIS_ID = ?, EDITORIAL_ID = ? WHERE LIBRO_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, libro[1].toString());       // Titulo
            preparedStatement.setInt(2, (Integer) libro[2]);           // Edicion
            String repeated = new String(new char[4 - ((Integer) libro[3]).toString().length()]).replace("\0", "0");
            String date = repeated + ((Integer) libro[3]).toString() + "-01-01";
            preparedStatement.setDate(3, java.sql.Date.valueOf(date)); // Año 
            preparedStatement.setInt(4, (Integer) libro[4]);           // No. paginas
            preparedStatement.setInt(5, (Integer) libro[5]);           // Idioma_id
            preparedStatement.setInt(6, (Integer) libro[6]);           // Genero_id
            preparedStatement.setInt(7, (Integer) libro[7]);           // Pais_id
            preparedStatement.setString(8, libro[8].toString());       // Editorial_id
            preparedStatement.setInt(9, (Integer) libro[0]);           // Id del libro
            preparedStatement.executeUpdate();
            libroId = (int) libro[0];
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "updateLibro");
            libroId = 0;
        } finally {
           disconnect();
        }
        return libroId;
    }   
    
    /**
     * Borra un libro de la base de datos.
     * @param libroId
     * @return 
     */
    public int deleteLibro(int libroId) {
        
        // Borra primero su autoria.
        AutorDAO autorDAO = new AutorDAO();
        autorDAO.deleteAutoriaFromLibro(libroId);
        
        int state = 0;
        connect();
        try {
            sentenciaSQL = "DELETE FROM LIBRO WHERE LIBRO_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, libroId);
            state = preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "deleteLibro");
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