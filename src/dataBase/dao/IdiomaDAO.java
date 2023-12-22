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
    public String SaveIdioma(String idioma) 
    {
        // Se conecta a la base de datos
        conectar();
        try{
            String id = "";
            // genera el nuevo id            
            sentenciaSQL =  "SELECT MAX(IDIOMA_ID) + 1 FROM IDIOMA";
            ps = conn.prepareStatement(sentenciaSQL);            
            rs = ps.executeQuery();
            
            // guarda el nuevo id
            if (rs.next()){
                id = rs.getString(1);
            }
            
            // inserta mandando todos los datos, incluido en nuevo id
            sentenciaSQL = "INSERT INTO IDIOMA VALUES(?, ?, SYSDATE)";
            ps = conn.prepareStatement(sentenciaSQL);
            // Asigna los valores del arreglo            
            ps.setString(1, id);                    // Id (calculado)
            ps.setString(2, idioma);                // Idioma (provisto)            
            ps.executeUpdate();
            return id;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "SaveIdioma");
            return null;
        }
        finally{
           desconectar();
       }

    }
    
    // Modificar un idioma
    public String UpdateIdioma(Object[] idioma)
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
            ps.setString(2, idioma[0].toString());  // Id del idioma
            ps.executeUpdate();
            return idioma[0].toString();            // Regresa el id del idioma
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "UpdateIdioma");
            return null;
        }
        finally{
           desconectar();
       }
    }
    
    // Consultar todos los idiomas
    public Object [][] GetAllIdiomas(){
       conectar();
       Object [][] idiomas;
       int i = 0;
       int count = 0;
       try{
           sentenciaSQL = "SELECT COUNT(*) FROM IDIOMA";    // Numero de actores           
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
               idiomas[i][0]=(rs.getString(1));
               idiomas[i][1]=(rs.getString(2));  
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

    // Consulta el idioma para un id
    public Object[] GetIdiomaById(int idioma_id)
    {
        conectar();
        Object [] idioma = new Object[2];
       
        try{
                    
            sentenciaSQL  = "SELECT IDIOMA_ID, IDIOMA FROM IDIOMA WHERE IDIOMA_ID = ?";           
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, String.valueOf(idioma_id));    // Reemplaza el parámetro index (simbolo ?) con el str x
            rs = ps.executeQuery();

            // Llena el arreglo actor con el resultado
            while (rs.next()){
                idioma[0] = (rs.getString(1));
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
    
    // Consultar todos los libros de un idioma
    public Object [] GetLibrosByIdioma(int idioma_id){
       conectar();
       Object [] libros;
       int i = 0;
       int count = 0;
       try{
           // Primero se cuenta el numero de autores en el libro
           sentenciaSQL =   "SELECT COUNT (IDIOMA_ID) " +
                            "FROM IDIOMA " +
                            "JOIN LIBRO USING (IDIOMA_ID) " +
                            "WHERE IDIOMA_ID = ? ";

            ps = conn.prepareStatement(sentenciaSQL);   // prepara la sentencia 
            ps.setString(1, String.valueOf(idioma_id));
            rs = ps.executeQuery();                     // Ejecuta la sentencia y la asigna al result set    
           
           if (rs.next()){
               count = rs.getInt(1);
            }
           
           libros = new Object[count][2];           
           sentenciaSQL  =  "SELECT TITULO " +
                            "FROM LIBRO" +
                            "WHERE IDIOMA_ID = ? " +
                            "ORDER BY 2";   // Ordenar por la segunda columna     
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, String.valueOf(idioma_id));
           rs = ps.executeQuery();
           while (rs.next()){
               libros[i] =(rs.getString(1));  // Titulos del libro
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
    
    // Consultar idiomas con nombre parecido
    public Object [] GetIdiomasByNombre(String idioma){
       conectar();
       Object [] idiomas;
       int i = 0;
       int count = 0;
       idioma = "%" + idioma + "%";
       try{
           // Cuenta todos los actores que tengan un nombre parecido
           sentenciaSQL =   "SELECT COUNT(IDIOMA_ID)" +
                            "FROM IDIOMA" +
                            "WHERE UPPER (IDIOMA) LIKE UPPER(?)";   // Todos los idiomas que tengan un nombre parecido
                            
            
            ps = conn.prepareStatement(sentenciaSQL);   // prepara la sentencia 
            ps.setString(1, idioma);
            rs = ps.executeQuery();                     // Ejecuta la sentencia y la asigna al result set         
           
           if (rs.next()){
               count = rs.getInt(1);
           }
           
           // Misma consulta, pero ahora puede ser guardada en el arreglo
           idiomas = new Object[count];           
           sentenciaSQL =   "SELECT IDIOMA" +
                            "FROM IDIOMA" +
                            "WHERE UPPER (IDIOMA) LIKE UPPER(?)";
           
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, idioma);
           rs = ps.executeQuery();
           
           while (rs.next()){
               idiomas[i] = (rs.getString(1));
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
            sentenciaSQL = "DELETE FROM IDIOMA " +
                            "WHERE IDIOMA_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, String.valueOf(idioma_id));
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

    // Implementar borrar todos los libros que tengan un idioma ???
}