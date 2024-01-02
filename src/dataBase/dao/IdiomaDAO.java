/*
 * Clase encargada de consultar, insertar, eliminar y modificar la tabla Idioma
 */
package dataBase.dao;

import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;

/**
 *
 * @author alfonso
 */

public class IdiomaDAO extends Conexion {
    
    // Crear un idioma
    public int SaveIdioma(String idioma) 
    {
        // Se conecta a la base de datos
        conectar();
        try{
            int id = 0;
            // genera el nuevo id            
            sentenciaSQL =  "SELECT MAX(IDIOMA_ID) + 1 FROM IDIOMA";
            ps = conn.prepareStatement(sentenciaSQL);            
            rs = ps.executeQuery();
            
            // guarda el nuevo id
            if (rs.next()){
                id = rs.getInt(1);
            }
            
            // inserta mandando todos los datos, incluido en nuevo id
            sentenciaSQL = "INSERT INTO IDIOMA VALUES(?, ?, SYSDATE)";
            ps = conn.prepareStatement(sentenciaSQL);
            // Asigna los valores del arreglo            
            ps.setInt(1, id);           // Id (calculado)
            ps.setString(2, idioma);    // Idioma (provisto)            
            ps.executeUpdate();
            return id;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "SaveIdioma");
            return 0;
        }
        finally{
           desconectar();
       }

    }
    
    // Modificar un idioma (id, idioma)
    public int UpdateIdioma(Object[] idioma)
    {       
        // se conecta a la base de datos
       conectar();
       try{
           // actualiza los datos
           sentenciaSQL = "UPDATE IDIOMA SET " +
                          "IDIOMA = ?, " +
                          "LAST_UPDATE = SYSDATE " +
                          "WHERE IDIOMA_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            
            ps.setString(1, idioma[1].toString());  // Nuevo nombre idioma
            ps.setInt(2, (Integer) idioma[0]);      // Id del idioma
            ps.executeUpdate();
            return (Integer) idioma[0];             // Regresa el id del idioma
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "UpdateIdioma");
            return 0;
        }
        finally{
           desconectar();
       }
    }
    
    // Consultar todos los idiomas (id, idioma)
    public Object [][] GetAllIdiomas(){
        conectar();
        Object [][] idiomas;
        int i = 0;
        int count = 0;
        try{
            sentenciaSQL = "SELECT COUNT(*) FROM IDIOMA";    // Numero de idiomas           
            ps = conn.prepareStatement(sentenciaSQL);        // Convierte el str a un sentencia utilizable en SQL           
            rs = ps.executeQuery();                          // Resultado de la consulta
           
            // Numero de idiomas (COUNT)
            if (rs.next()){
                count = rs.getInt(1);
            }

            // Arreglo de todos los idiomas (ID, idioma)
            idiomas = new Object[count][2];           
            sentenciaSQL  = "SELECT IDIOMA_ID, IDIOMA FROM IDIOMA ORDER BY 2";           
            ps = conn.prepareStatement(sentenciaSQL);
            rs = ps.executeQuery();
           
            // Agregar a todos los autores al arreglo (migrar de rs a actores)
            while (rs.next()){
               idiomas[i][0] = (rs.getInt(1));
               idiomas[i][1] = (rs.getString(2));  
               i++;
            }           
           return idiomas;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetAllIdiomas");
            return null;
        }
        finally{
           desconectar();           
       }
    }  

    // Consulta el idioma para un id (id, idioma)
    public Object[] GetIdiomaById(int idioma_id)
    {
        conectar();
        Object[] idioma = new Object[2];
       
        try{
                    
            sentenciaSQL  = "SELECT IDIOMA_ID, IDIOMA FROM IDIOMA WHERE IDIOMA_ID = ?";           
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, idioma_id);    // Reemplaza el parámetro index (simbolo ?) con el str x
            rs = ps.executeQuery();

            // Llena el arreglo actor con el resultado
            while (rs.next()){
                idioma[0] = (rs.getInt(1));
                idioma[1] = (rs.getString(2));  
            }           
            return idioma;
        }
         catch (SQLException ex){
             System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                     "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetIdiomaById");
             return null;
        }
        finally{
           desconectar();           
        }

    }
    
    // Regresa el idioma (ID, idioma) de un libro
    public Object[] GetIdiomaByLibro(int id_libro){
        // Conecta a la base de datos
       conectar();        
       try{
            Object[] idioma = new Object[2];
            sentenciaSQL  =  "SELECT IDIOMA_ID, IDIOMA " +
                             "FROM LIBRO " +
                             "JOIN IDIOMA USING (IDIOMA_ID) " +
                             "WHERE LIBRO_ID = ?";           
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, id_libro);
            rs = ps.executeQuery();

            // Recorre el result set para obtener los datos y asignarlos
            // al arreglo
            while (rs.next()){
                idioma[0] = rs.getInt(1);
                idioma[1] = rs.getString(2);
            }     
            return idioma;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetIdiomaByLibro");
            return null;
        }
       finally{
           desconectar();           
       }
    }
    
    // Consultar todos los libros de un idioma
    public Object[][] GetLibrosByIdioma(int idioma_id){
       conectar();
       Object[][] libros;
       int i = 0;
       int count = 0;
       try{
           // Primero se cuenta el numero de autores en el libro
           sentenciaSQL =   "SELECT COUNT (IDIOMA_ID) " +
                            "FROM IDIOMA " +
                            "JOIN LIBRO USING (IDIOMA_ID) " +
                            "WHERE IDIOMA_ID = ? ";

            ps = conn.prepareStatement(sentenciaSQL);   // prepara la sentencia 
            ps.setInt(1, idioma_id);
            rs = ps.executeQuery();                     // Ejecuta la sentencia y la asigna al result set    
           
            if (rs.next()){
                count = rs.getInt(1);
             }

            libros = new Object[count][2];           
            sentenciaSQL  =  "SELECT LIBRO_ID, TITULO " +
                             "FROM LIBRO " +
                             "WHERE IDIOMA_ID = ? " +
                             "ORDER BY 2";   // Ordenar por la segunda columna     
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, idioma_id);
            rs = ps.executeQuery();
            while (rs.next()){
                libros[i][0] = (rs.getInt(1));       // Id del libro
                libros[i][1] = (rs.getString(2));    // Titulos del libro
                i++;
            }           
            return libros;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetLibrosByIdioma");
            return null;
        }
        finally{
           desconectar();           
        }
    }
    
    // Consultar idiomas con nombre parecido (id, idioma)
    public Object[][] GetIdiomasByNombre(String idioma){
        conectar();
        Object[][] idiomas;
        int i = 0;
        int count = 0;
        idioma = "%" + idioma + "%";
        try{
            // Cuenta todos los actores que tengan un nombre parecido
            sentenciaSQL =   "SELECT COUNT(IDIOMA_ID) " +
                             "FROM IDIOMA " +
                             "WHERE UPPER (IDIOMA) LIKE UPPER(?)";   // Todos los idiomas que tengan un nombre parecido


             ps = conn.prepareStatement(sentenciaSQL);   // prepara la sentencia 
             ps.setString(1, idioma);
             rs = ps.executeQuery();                     // Ejecuta la sentencia y la asigna al result set         

            if (rs.next()){
                count = rs.getInt(1);
            }

            // Misma consulta, pero ahora puede ser guardada en el arreglo
            idiomas = new Object[count][2];           
            sentenciaSQL =   "SELECT IDIOMA_ID, IDIOMA " +
                             "FROM IDIOMA " +
                             "WHERE UPPER (IDIOMA) LIKE UPPER(?)";

            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, idioma);
            rs = ps.executeQuery();

            while (rs.next()){
                idiomas[i][0] = (rs.getInt(1));
                idiomas[i][1] = (rs.getString(2));
                i++;
            }           
           return idiomas;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetIdiomasByNombre");
            return null;
        }
        finally{
           desconectar();           
       }
    }

    // Eliminar un idioma
    public int DeleteIdioma(int idioma_id){
        // Conecta a la base de datos
        conectar();
        try{
            // Cambiar a NULL el idioma de cualquier libro con el idioma a borrar
            sentenciaSQL =  "UPDATE LIBRO " +
                            "SET IDIOMA_ID = NULL " +
                            "WHERE IDIOMA_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, idioma_id);
            ps.executeUpdate();
            
            // Borrar el idioma
            sentenciaSQL = "DELETE FROM IDIOMA " +
                            "WHERE IDIOMA_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, idioma_id);
            int res = ps.executeUpdate();
            if (res == 1){
                return 0;
            }
            
            return 1;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "DeleteIdioma");
            if (ex.getErrorCode() ==  2292)
                return 1;
            return 2;
        }
        finally{
           desconectar();
       }
    }
}
