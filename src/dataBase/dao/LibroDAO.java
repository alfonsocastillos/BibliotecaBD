/*
   Operaciones a la base de datos en la tabla LIBRO
*/
package dataBase.dao;

import dataBase.Conexion;
import java.sql.*;
import oracle.jdbc.OracleTypes;

/* 
    Clase que realiza todas las transacciones a la base de datos, hereda de la
    clase conexión para ahorrar codigo
*/
public class LibroDAO extends Conexion {

    // Regresa todos los libros (LIBRO_ID, TITULO, EDICION, EDITORIAL, GENERO, ANIO, NUM_PAGINAS, IDIOMA)
    public Object [][] GetAllLibros(){
        // se conecta a la base de datos
        conectar();
        // crea un arreglo de beans para guardar los datos
        Object [][] libros;
        // Contador
        int i = 0;
        // Para guardar la cantidad de registros
        int count = 0;
        try{
            // Cuenta los registros en la base de datos
            sentenciaSQL = "SELECT COUNT (*) FROM LIBRO";
            ps = conn.prepareStatement(sentenciaSQL);
            rs = ps.executeQuery();
            
            if (rs.next()){
                count = rs.getInt(1);
            }
            // Consulta los registros 
            libros = new Object[count][8];    
            sentenciaSQL =  "SELECT LIBRO_ID, TITULO, EDICION, EDITORIAL, GENERO, ANIO, NUM_PAGINAS, IDIOMA " +
                            "FROM LIBRO " +
                            "JOIN EDITORIAL USING (EDITORIAL_ID) " +
                            "JOIN IDIOMA USING (IDIOMA_ID) " +
                            "JOIN GENERO USING (GENERO_ID)" +
                            "ORDER BY TITULO;"; 

            // prepara la sentencia 
            ps = conn.prepareStatement(sentenciaSQL);
            // Ejecuta la sentencia y la asigna al result set 
            rs = ps.executeQuery();
            // Recorre el result set para obtener los datos y asignarlos al arreglo de beans
            while (rs.next()){
                libros[i][0] = rs.getString(1);      // Id del libro
                libros[i][1] = rs.getString(2);      // Titulo del libro
                libros[i][2] = rs.getInt(3);         // Edicion del libro
                libros[i][3] = rs.getString(4);      // Editorial del libro
                libros[i][4] = rs.getString(5);      // Genero del libro
                libros[i][5] = rs.getInt(6);         // Año de publicacion del libro
                libros[i][6] = rs.getInt(7);         // Numero de paginas 
                libros[i][7] = rs.getString(8);      // Idioma del libro
                i++;
            }           
            return libros;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetAllLibros");
            return null;
        }
       finally{
           desconectar();           
       }
    }
    
    // Regresa todos los libros con un titulo o genero parecido (LIBRO_ID, TITULO, EDICION, EDITORIAL, GENERO, ANIO, NUM_PAGINAS, IDIOMA)
    public Object [][] GetLibrosByDescripcion(String descripcion){
        // se conecta a la base de datos
        conectar();
        // crea un arreglo de beans para guardar los datos
        Object [][] libros;
        // Contador
        int i = 0;
        // Para guardar la cantidad de registros
        int count = 0;
        descripcion = "%" + descripcion + "%";
        try{
            // Cuenta los registros en la base de datos
            sentenciaSQL =  "SELECT COUNT (LIBRO_ID) FROM (" +
                            "    SELECT LIBRO_ID  " +
                            "    FROM LIBRO " +
                            "    JOIN IDIOMA USING (IDIOMA_ID) " +
                            "    JOIN EDITORIAL USING (EDITORIAL_ID) " +
                            "    JOIN GENERO USING (GENERO_ID) " +
                            "    WHERE UPPER(TITULO) LIKE UPPER('?') " +
                            "UNION " +
                            "    SELECT LIBRO_ID " +
                            "    FROM LIBRO " +
                            "    JOIN IDIOMA USING (IDIOMA_ID) " +
                            "    JOIN EDITORIAL USING (EDITORIAL_ID) " +
                            "    JOIN GENERO USING (GENERO_ID) " +
                            "    WHERE UPPER(GENERO) LIKE UPPER('?'))";
                        
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, descripcion);
            ps.setString(2, descripcion);
            rs = ps.executeQuery();
            
            if (rs.next()){
                count = rs.getInt(1);
            }
            // Consulta los registros 
            libros = new Object[count][7];          
            sentenciaSQL =  "SELECT LIBRO_ID, TITULO, EDICION, EDITORIAL, GENERO, ANIO, NUM_PAGINAS, IDIOMA " +
                            "FROM LIBRO " +
                            "JOIN IDIOMA USING (IDIOMA_ID) " +
                            "JOIN EDITORIAL USING (EDITORIAL_ID) " +
                            "JOIN GENERO USING (GENERO_ID) " +
                            "WHERE UPPER(TITULO) LIKE UPPER(?) " +
                            "OR UPPER(GENERO) LIKE UPPER(?) " +
                            "ORDER BY TITULO";
            // prepara la sentencia 
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, descripcion);
            ps.setString(2, descripcion);            
            // Ejecuta la sentencia y la asigna al result set 
            rs = ps.executeQuery();
            // Recorre el result set para obtener los datos y asignarlos al arreglo de beans
            while (rs.next()){
                libros[i][0] = rs.getString(1);      // Id del libro
                libros[i][1] = rs.getString(2);      // Titulo del libro
                libros[i][2] = rs.getInt(3);         // Edicion del libro
                libros[i][3] = rs.getString(4);      // Editorial del libro
                libros[i][4] = rs.getString(5);      // Genero del libro
                libros[i][5] = rs.getInt(6);         // Año de publicacion del libro
                libros[i][6] = rs.getInt(7);         // Numero de paginas 
                libros[i][7] = rs.getString(8);      // Idioma del libro  
                i++;
            }           
            return libros;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetLibrosByDescripcion");
            return null;
        }
       finally{
           desconectar();           
       }
    }
    
    // Regresa la info de un libro mediante su id (LIBRO_ID, TITULO, EDICION, EDITORIAL, GENERO, ANIO, NUM_PAGINAS, IDIOMA, PAIS)
    public Object[] GetLibroById(int libro_id){
        conectar();
        Object libro[];// Para guardar la cantidad de registros
        try{
           
            // Consulta los registros 
            libro = new Object[9];          
            sentenciaSQL =  "SELECT LIBRO_ID, TITULO, EDICION, EDITORIAL, GENERO, ANIO, NUM_PAGINAS, IDIOMA, PAIS " +
                            "FROM LIBRO " +
                            "JOIN IDIOMA USING (IDIOMA_ID) " +
                            "JOIN EDITORIAL USING (EDITORIAL_ID) " +
                            "JOIN GENERO USING (GENERO_ID) " +
                            "JOIN PAIS USING (PAIS_ID) " +
                            "WHERE LIBRO_ID = ?;";
            // prepara la sentencia 
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, libro_id);
            // Ejecuta la sentencia y la asigna al result set 
            rs = ps.executeQuery();
            rs.next();
            // Recupera los valores
            libro[0] = rs.getInt(1);    // Id
            libro[1] = rs.getString(2); // Titulo 
            libro[2] = rs.getInt(3);    // Edicion
            libro[3] = rs.getString(4); // Editorial 
            libro[4] = rs.getString(5); // Genero
            libro[5] = rs.getInt(6);    // Año
            libro[6] = rs.getInt(7);    // No. pags
            libro[7] = rs.getString(8); // Idioma 
            libro[8] = rs.getString(9); // Pais      
            return libro;
       }
       catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetLibroById");
            return null;
        }
       finally{
           desconectar();           
       }
    }
    
    // Guarda un libro nuevo (ID, TITULO, EDICION, ANIO, NUM_PAGS, IDIOMA_ID, GENERO_ID, PAIS_ID EDITORIAL_ID)
    public int SaveLibro(Object[] libro){
        // Guarda una pelicula
        // Se conecta a la base de datos
        conectar();
        try{
            int id = 0;
            // genera el nuevo id
            sentenciaSQL =  "SELECT LPAD(SUBSTR(EXTRACT(YEAR FROM SYSDATE), 3, 2), 2, '0') || LPAD(EXTRACT(MONTH FROM SYSDATE), 2, '0') || LPAD(NVL(MAX(TO_NUMBER(SUBSTR(LIBRO_ID, 5, 3))) + 1, 1), 3, '0') " +
                            "FROM LIBRO " +
                            "WHERE SUBSTR(LIBRO_ID, 1, 4) = LPAD(SUBSTR(EXTRACT(YEAR FROM SYSDATE), 3, 2), 2, '0') || LPAD(EXTRACT(MONTH FROM SYSDATE), 2, '0')";
            ps = conn.prepareStatement(sentenciaSQL);
            rs = ps.executeQuery();
            // guarda el nuevo id
            if (rs.next()){
                id = rs.getInt(1);
            }
            // inseta mandando todos los datos, incluido en nuevo id
            sentenciaSQL = "Insert INTO LIBRO VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sentenciaSQL);
            // Asigna los valores del arreglo  
            ps.setInt(1, id);                       // Id del libro
            ps.setString(2, libro[1].toString());   // Titulo
            ps.setInt(3, (Integer) libro[2]);       // Edicion
            ps.setInt(4, (Integer) libro[3]);       // Año 
            ps.setInt(5, (Integer) libro[4]);       // No. paginas
            ps.setInt(6, (Integer) libro[5]);       // Idioma_id
            ps.setInt(7, (Integer) libro[6]);       // Genero_id
            ps.setInt(8, (Integer) libro[7]);       // Pais_id
            ps.setString(9, libro[8].toString());   // Editorial_id
            ps.executeUpdate();
            return id;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "SaveLibro");
            return 0;
        }
       finally{
           desconectar();
       }
    }
    
    // Actualiza un libro nuevo (LIBRO_ID, TITULO, EDICION, ANIO, NUM_PAGS, IDIOMA_ID, GENERO_ID, PAIS_ID EDITORIAL_ID)
    public int UpdateLibro(Object libro[]){
        // se conecta a la base de datos
       conectar();
       try{
           // actualiza los datos
           sentenciaSQL = "UPDATE LIBRO SET " +
                          "TITULO = ?, " +
                          "EDICION = ?, " +
                          "ANIO = ?, " +
                          "NUM_PAGINAS = ?, " +
                          "IDIOMA_ID = ?, " +
                          "GENERO_ID = ?, " +
                          "PAIS_ID = ?, " +
                          "EDITORIAL_ID = ?, " +                          
                          "WHERE LIBRO_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            
            ps.setString(1, libro[1].toString());   // Titulo
            ps.setInt(2, (Integer) libro[2]);       // Edicion
            ps.setInt(3, (Integer) libro[3]);       // Año 
            ps.setInt(4, (Integer) libro[4]);       // No. paginas
            ps.setInt(5, (Integer) libro[5]);       // Idioma_id
            ps.setInt(6, (Integer) libro[6]);       // Genero_id
            ps.setInt(7, (Integer) libro[7]);       // Pais_id
            ps.setString(8, libro[8].toString());   // Editorial_id
            ps.setInt(9, (Integer) libro[0]);       // Id del libro
            ps.executeUpdate();
            
            return (Integer) libro[0];              // Regresar el id
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "UpdateLibro");
            return 0;
        }
        finally{
           desconectar();
       }
    }   
    
    public int DeleteLibro(String libro_id){
        // Borra primero su autoria
        AutorDAO autor_dao = new AutorDAO();
        int res = autor_dao.DeleteAutoriaFromLibro(libro_id);
        
        // Si se borro con exito la autoria, borrar el libro
        if (res == 1){
            // Conecta a la base de datos
            conectar();
            try{
                sentenciaSQL = "DELETE FROM LIBRO " +
                                "WHERE LIBRO_ID = ?";
                ps = conn.prepareStatement(sentenciaSQL);
                ps.setString(1, libro_id);
                res = ps.executeUpdate();
                if (res == 1){
                    return 0;
                }            
                return 1;
            }
            catch (SQLException ex){
                System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                        "\n\n" + sentenciaSQL + "\n\nUbicación: " + "DeleteLibro");
                if (ex.getErrorCode() ==  2292)
                    return 1;
                return 2;
            }
            finally{
               desconectar();
           }
        }
        else
            return 0;
    }

}