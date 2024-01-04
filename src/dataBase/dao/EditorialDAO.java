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

public class EditorialDAO extends Conexion {
    // Crear una editorial
    public String SaveEditorial(String editorial) 
    {
        // Se conecta a la base de datos
        conectar();
        try{
            String id = "";
            // genera el nuevo id (primera letra mas numero mas alto)       
            sentenciaSQL =  "SELECT SUBSTR(?, 1, 1) || LPAD(NVL(MAX(TO_NUMBER(SUBSTR(EDITORIAL_ID, 2, 3))) + 1, 1), 3, '0') " +
                            "FROM EDITORIAL " +
                            "WHERE SUBSTR(EDITORIAL_ID, 1, 1) LIKE UPPER(SUBSTR(?, 1, 1))";
            
            ps = conn.prepareStatement(sentenciaSQL);            
            ps.setString(1, editorial);
            ps.setString(2, editorial);
            rs = ps.executeQuery();
            
            // guarda el nuevo id
            if (rs.next()){
                id = rs.getString(1);
            }
            
            // inserta mandando todos los datos, incluido en nuevo id
            sentenciaSQL = "INSERT INTO EDITORIAL VALUES(?, ?)";
            ps = conn.prepareStatement(sentenciaSQL);
            // Asigna los valores del arreglo            
            ps.setString(1, id);                       // Id (calculado)
            ps.setString(2, editorial);                // Genero (provisto)            
            ps.executeUpdate();
            return id;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "SaveEditorial");
            return null;
        }
        finally{
           desconectar();
       }

    }

    // Modificar una editorial
    public String UpdateEditorial(Object[] editorial)
    {       
        // se conecta a la base de datos
        conectar();
        try{
           // actualiza los datos
           sentenciaSQL = "UPDATE EDITORIAL SET " +
                          "EDITORIAL = ? " +                          
                          "WHERE EDITORIAL_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            
            ps.setString(1, editorial[1].toString());  // Nuevo nombre editorial
            ps.setString(2, editorial[0].toString());  // Id de la editorial
            ps.executeUpdate();
            return editorial[0].toString();            // Regresa el id de la editorial
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "UpdateEditorial");
            return null;
        }
        finally{
           desconectar();
       }
    }

    // Consultar todas las editoriales (id, editorial)
    public Object [][] GetAllEditoriales(){
       conectar();
       Object [][] editoriales;
       int i = 0;
       int count = 0;
       try{
           sentenciaSQL = "SELECT COUNT(*) FROM EDITORIAL";     // Numero de editoriales
           ps = conn.prepareStatement(sentenciaSQL);            // Convierte el str a un sentencia utilizable en SQL           
           rs = ps.executeQuery();                              // Resultado de la consulta
           
           // Numero de editoriales (COUNT)
           if (rs.next()){
               count = rs.getInt(1);
           }
           
           // Arreglo de todas las editoriales (ID, editorial)
           editoriales = new Object[count][2];           
           sentenciaSQL  = "SELECT EDITORIAL_ID, EDITORIAL FROM EDITORIAL ORDER BY 2";           
           ps = conn.prepareStatement(sentenciaSQL);
           rs = ps.executeQuery();
           
           // Agregar a todas las editoriales al arreglo (migrar de rs a editoriales)
           while (rs.next()){
               editoriales[i][0] = (rs.getString(1));
               editoriales[i][1] = (rs.getString(2));  
               i++;
            }           
           return editoriales;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetAllEditoriales");
            return null;
        }
       finally{
           desconectar();           
       }
    }  

    // Consultar una editorial por id
    public Object[] GetEditorialById(String editorial_id)
    {
        conectar();
        Object [] editorial = new Object[2];
       
        try{                    
            sentenciaSQL  = "SELECT EDITORIAL_ID, EDITORIAL FROM EDITORIAL WHERE EDITORIAL_ID = ?";           
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, editorial_id);    // Reemplaza el parámetro index (simbolo ?) con el str x
            rs = ps.executeQuery();

            // Llena el arreglo actor con el resultado
            while (rs.next()){
                editorial[0] = (rs.getString(1));
                editorial[1] = (rs.getString(2));  
            }           
            return editorial;
        }
         catch (SQLException ex){
             System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                     "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetEditorialById");
             return null;
        }
        finally{
           desconectar();           
        }

    }

    // Consultar la editorial de un libro
    public String GetEditorialByLibro(String id_libro){
        // Conecta a la base de datos
        conectar();
        String editorial = "";
        // Contador
        try{
            sentenciaSQL  = "SELECT EDITORIAL " +
                            "FROM LIBRO " +
                            "JOIN EDITORIAL USING (EDITORIAL_ID) " +
                            "WHERE LIBRO_ID = ?";           
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, id_libro);
           rs = ps.executeQuery();
              
           // Recorre el result set para obtener los datos y asignarlos al arreglo
           while (rs.next()){
               editorial = rs.getString(1);
           }     
           return editorial;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetEditorialByLibro");
            return null;
        }
        finally{
           desconectar();           
        }
    }
    
    // Consultar todos los libros con una editorial
    public Object [] GetLibrosByEditorial(String editorial_id){
       conectar();
       Object [] libros;
       int i = 0;
       int count = 0;
       try{
           // Primero se cuenta el numero de libros por editorial
           sentenciaSQL =   "SELECT COUNT (EDITORIAL_ID) " +
                            "FROM EDITORIAL " +
                            "JOIN LIBRO USING (EDITORIAL_ID) " +
                            "WHERE EDITORIAL_ID = ? ";

            ps = conn.prepareStatement(sentenciaSQL);   // prepara la sentencia 
            ps.setString(1, editorial_id);
            rs = ps.executeQuery();                     // Ejecuta la sentencia y la asigna al result set    
           
           if (rs.next()){
               count = rs.getInt(1);
            }
           
           libros = new Object[count][2];           
           sentenciaSQL  =  "SELECT TITULO " +
                            "FROM LIBRO" +
                            "WHERE EDITORIAL_ID = ? " +
                            "ORDER BY 2";   // Ordenar por la segunda columna     
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, editorial_id);
           rs = ps.executeQuery();
           while (rs.next()){
               libros[i] = (rs.getString(1));  // Titulos del libro
               i++;
           }           
           return libros;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetLibrosByEditorial");
            return null;
        }
        finally{
           desconectar();           
        }
    }

    // Consultar todas las editoriales con un nombre parecido
    public Object[][] GetEditorialByNombre(String editorial){
       conectar();
       Object[][] editoriales;
       int i = 0;
       int count = 0;
       editorial = "%" + editorial + "%";
       try{
           // Cuenta todos los actores que tengan un nombre parecido
           sentenciaSQL =   "SELECT COUNT(EDITORIAL_ID)" +
                            "FROM EDITORIAL " +
                            "WHERE UPPER (EDITORIAL) LIKE UPPER(?)";   // Todos las editoriales que tengan un nombre parecido
                            
            
            ps = conn.prepareStatement(sentenciaSQL);   // prepara la sentencia 
            ps.setString(1, editorial);
            rs = ps.executeQuery();                     // Ejecuta la sentencia y la asigna al result set         
           
           if (rs.next()){
               count = rs.getInt(1);
           }
           
           // Misma consulta, pero ahora puede ser guardada en el arreglo
           editoriales = new Object[count][2];           
           sentenciaSQL =   "SELECT EDITORIAL_ID, EDITORIAL " +
                            "FROM EDITORIAL " +
                            "WHERE UPPER (EDITORIAL) LIKE UPPER(?)" +
                            "ORDER BY EDITORIAL";
           
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, editorial);
           rs = ps.executeQuery();
           
           while (rs.next()){
               editoriales[i][0] = (rs.getString(1));
               editoriales[i][1] = (rs.getString(2));
               i++;
           }           
           return editoriales;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetEditorialByNombre");
            return null;
        }
        finally{
           desconectar();           
       }
    }

    // Borrar una editorial
    public int DeleteEditorial(String editorial_id){
        // Conecta a la base de datos
        conectar();
        try{            
            // Borrar la editorial
            sentenciaSQL = "DELETE FROM EDITORIAL " +
                            "WHERE EDITORIAL_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, editorial_id);
            int res = ps.executeUpdate();
            if (res == 1){
                return 0;
            }
            
            return 1;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "DeleteEditorial");
            if (ex.getErrorCode() ==  2292)
                return 1;
            return 2;
        }
        finally{
           desconectar();
       }
    }

}
