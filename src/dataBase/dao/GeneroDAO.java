/*
 * Clase encargada de consultar, insertar, eliminar y modificar la tabla Genero
 */
package dataBase.dao;

import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;

/**
 *
 * @author alfonso
 */

public class GeneroDAO extends Conexion {
    
    // Crear un genero
    public String SaveGenero(String genero) 
    {
        // Se conecta a la base de datos
        conectar();
        try{
            String id = "";
            // genera el nuevo id            
            sentenciaSQL =  "SELECT MAX(GENERO_ID) + 1 FROM GENERO";
            ps = conn.prepareStatement(sentenciaSQL);            
            rs = ps.executeQuery();
            
            // guarda el nuevo id
            if (rs.next()){
                id = rs.getString(1);
            }
            
            // inserta mandando todos los datos, incluido en nuevo id
            sentenciaSQL = "INSERT INTO GENERO VALUES(?, ?, SYSDATE)";
            ps = conn.prepareStatement(sentenciaSQL);
            // Asigna los valores del arreglo            
            ps.setString(1, id);                    // Id (calculado)
            ps.setString(2, genero);                // Genero (provisto)            
            ps.executeUpdate();
            return id;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "SaveGenero");
            return null;
        }
        finally{
           desconectar();
       }

    }
    
    // Modificar un genero
    public String UpdateGenero(Object[] genero)
    {       
        // se conecta a la base de datos
       conectar();
       try{
           // actualiza los datos
           sentenciaSQL = "UPDATE GENERO SET " +
                          "GENERO = ?, " +
                          "LAST_UPDATE = SYSDATE " +
                          "WHERE GENERO_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            
            ps.setString(1, genero[1].toString());  // Nuevo nombre genero
            ps.setString(2, genero[0].toString());  // Id del genero
            ps.executeUpdate();
            return genero[0].toString();            // Regresa el id del genero
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "UpdateGenero");
            return null;
        }
        finally{
           desconectar();
       }
    }
    
    // Consultar todos los idiomas
    public Object [][] GetAllGeneros(){
       conectar();
       Object [][] generos;
       int i = 0;
       int count = 0;
       try{
           sentenciaSQL = "SELECT COUNT(*) FROM GENERO";    // Numero de generos           
           ps = conn.prepareStatement(sentenciaSQL);        // Convierte el str a un sentencia utilizable en SQL           
           rs = ps.executeQuery();                          // Resultado de la consulta
           
           // Numero de generos (COUNT)
           if (rs.next()){
               count = rs.getInt(1);
           }
           
           // Arreglo de todos los generos (ID, genero)
           generos = new Object[count][2];           
           sentenciaSQL  = "SELECT GENERO_ID, GENERO FROM GENERO ORDER BY 2";           
           ps = conn.prepareStatement(sentenciaSQL);
           rs = ps.executeQuery();
           
           // Agregar a todos los generos al arreglo (migrar de rs a generos)
           while (rs.next()){
               generos[i][0] = (rs.getString(1));
               generos[i][1] = (rs.getString(2));  
               i++;
            }           
           return generos;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetAllGeneros");
            return null;
        }
       finally{
           desconectar();           
       }
    }  

    // Consulta el genero para un id
    public Object[] GetGeneroById(String genero_id)
    {
        conectar();
        Object [] genero = new Object[2];
       
        try{                    
            sentenciaSQL  = "SELECT GENERO_ID, GENERO FROM GENERO WHERE GENERO_ID = ?";           
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, genero_id);    // Reemplaza el parámetro index (simbolo ?) con el str x
            rs = ps.executeQuery();

            // Llena el arreglo actor con el resultado
            while (rs.next()){
                genero[0] = (rs.getString(1));
                genero[1] = (rs.getString(2));  
            }           
            return genero;
        }
         catch (SQLException ex){
             System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                     "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetGeneroById");
             return null;
        }
        finally{
           desconectar();           
        }

    }
    
    // Consultar todos los libros de un genero
    public Object [] GetLibrosByGenero(String genero_id){
       conectar();
       Object [] libros;
       int i = 0;
       int count = 0;
       try{
           // Primero se cuenta el numero de libros por genero
           sentenciaSQL =   "SELECT COUNT (GENERO_ID) " +
                            "FROM GENERO " +
                            "JOIN LIBRO USING (GENERO_ID) " +
                            "WHERE GENERO_ID = ? ";

            ps = conn.prepareStatement(sentenciaSQL);   // prepara la sentencia 
            ps.setString(1, genero_id);
            rs = ps.executeQuery();                     // Ejecuta la sentencia y la asigna al result set    
           
           if (rs.next()){
               count = rs.getInt(1);
            }
           
           libros = new Object[count][2];           
           sentenciaSQL  =  "SELECT TITULO " +
                            "FROM LIBRO " +
                            "WHERE GENERO_ID = ? " +
                            "ORDER BY 2";   // Ordenar por la segunda columna     
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, genero_id);
           rs = ps.executeQuery();
           while (rs.next()){
               libros[i] = (rs.getString(1));  // Titulos del libro
               i++;
           }           
           return libros;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetLibrosByGenero");
            return null;
        }
        finally{
           desconectar();           
        }
    }
    
    // Consultar el genero de un libro
    public String GetGeneroByLibro(String id_libro){
        // Conecta a la base de datos
       conectar();
       String genero = "";
       // Contador
       try{
           sentenciaSQL  =  "SELECT GENERO " +
                            "FROM LIBRO " +
                            "JOIN GENERO USING (GENERO_ID) " +
                            "WHERE LIBRO_ID = ?";           
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, id_libro);
           rs = ps.executeQuery();
              
           // Recorre el result set para obtener los datos y asignarlos al arreglo
           while (rs.next()){
               genero = rs.getString(1);
           }     
           return genero;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetGeneroByLibro");
            return null;
        }
        finally{
           desconectar();           
        }
    }
    
    // Consultar generos con nombre parecido
    public Object [] GetGenerosByNombre(String genero){
       conectar();
       Object [] generos;
       int i = 0;
       int count = 0;
       genero = "%" + genero + "%";
       try{
           // Cuenta todos los actores que tengan un nombre parecido
           sentenciaSQL =   "SELECT COUNT(GENERO_ID)" +
                            "FROM GENERO" +
                            "WHERE UPPER (GENERO) LIKE UPPER(?)";   // Todos los idiomas que tengan un nombre parecido
                            
            
            ps = conn.prepareStatement(sentenciaSQL);   // prepara la sentencia 
            ps.setString(1, genero);
            rs = ps.executeQuery();                     // Ejecuta la sentencia y la asigna al result set         
           
           if (rs.next()){
               count = rs.getInt(1);
           }
           
           // Misma consulta, pero ahora puede ser guardada en el arreglo
           generos = new Object[count];           
           sentenciaSQL =   "SELECT GENERO" +
                            "FROM GENERO" +
                            "WHERE UPPER (GENERO) LIKE UPPER(?)";
           
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, genero);
           rs = ps.executeQuery();
           
           while (rs.next()){
               generos[i] = (rs.getString(1));
               i++;
           }           
           return generos;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetGenerosByNombre");
            return null;
        }
        finally{
           desconectar();           
       }
    }

    // Eliminar un genero
    public int DeleteGenero(String genero_id){
        // Conecta a la base de datos
        conectar();
        try{
            // Cambiar a NULL el genero de cualquier libro con el genero a borrar
            sentenciaSQL =  "UPDATE LIBRO" +
                            "SET GENERO_ID = NULL " +
                            "WHERE GENERO_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, genero_id);
            ps.executeUpdate();
            
            // Borrar el idioma
            sentenciaSQL = "DELETE FROM GENERO " +
                            "WHERE GENERO_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, genero_id);
            int res = ps.executeUpdate();
            if (res == 1){
                return 0;
            }
            
            return 1;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "DeleteGenero");
            if (ex.getErrorCode() ==  2292)
                return 1;
            return 2;
        }
        finally{
           desconectar();
       }
    }
}