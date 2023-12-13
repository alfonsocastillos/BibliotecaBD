/*
   Operaciones a la base de datos en la tabla idioma
*/
package dataBase.dao;

import dataBase.Conexion;
import java.sql.*;

/* 
    Clase que realiza todas las transacciones a la base de datos, hereda de la
    clase conexión para ahorrar codigo
*/

public class DaoLang extends Conexion {

    
    public Object [][] getLanguages(){
        // Obtiene todos lo idiomas
        // Conecta a la base de datos
       conectar();
       // Variable para guardar los idiomas
       Object[][] languajes;
       // Contador
       int i = 0;
       int cant = 0;
       try{
           // consulta
           sentenciaSQL = "SELECT COUNT(*) FROM LANGUAGE";
           // prepara la consulta
           ps = conn.prepareStatement(sentenciaSQL);
           // ejecuta la consulta
           rs = ps.executeQuery();
           // Ontiene la cantidad de registros
           if (rs.next()){
               cant = rs.getInt(1);
           }
           
           // consulta los datos
           languajes = new Object[cant][2];
           sentenciaSQL  = "SELECT  LANGUAGE_ID, LANGUAGE FROM LANGUAGE";           
           ps = conn.prepareStatement(sentenciaSQL);
           rs = ps.executeQuery();
              
           // Recorre el result set para obtener los datos y asignarlos
           // al arreglo
           while (rs.next()){
               languajes[i][0] = rs.getInt(1);
               languajes[i][1] = rs.getString(2);  
               i++;
           }     
           return languajes;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getLanguages");
            return null;
        }
       finally{
           desconectar();           
       }
    }
    
    public Object [][] getLanguageByFilm(String idFilm){
        // Obtiene todos lo idiomas
        // Conecta a la base de datos
       conectar();
       // Variable para guardar los idiomas
       Object[][] languajes;
       // Contador
       int i = 0;
       int cant = 0;
       try{
           // consulta
           sentenciaSQL = "SELECT COUNT (LANGUAGE_ID) " +
                           "FROM LANGUAGE " +
                           "JOIN LANGUAGE_FILM USING (LANGUAGE_ID) " +
                           "WHERE FILM_ID = ?";
           // prepara la consulta
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, idFilm);
           // ejecuta la consulta
           rs = ps.executeQuery();
           // Ontiene la cantidad de registros
           if (rs.next()){
               cant = rs.getInt(1);
           }
           // consulta los datos
           languajes = new Object[cant][2];
           sentenciaSQL  = "SELECT LANGUAGE_ID, LANGUAGE " +
                           "FROM LANGUAGE  " +
                           "JOIN LANGUAGE_FILM USING (LANGUAGE_ID) " +
                           "WHERE FILM_ID = ? " +
                           "ORDER BY LANGUAGE";           
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, idFilm);
           rs = ps.executeQuery();
              
           // Recorre el result set para obtener los datos y asignarlos
           // al arreglo
           while (rs.next()){
               languajes[i][0] = rs.getInt(1);
               languajes[i][1] = rs.getString(2);  
               i++;
           }     
           return languajes;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getLanguageByFilm");
            return null;
        }
       finally{
           desconectar();           
       }
    }
    
    public int saveDoblaje(Object[] doblaje){
        // Guarda una pelicula
        // Se conecta a la base de datos
        conectar();
        try{                        
            // inseta mandando todos los datos, incluido en nuevo id
            sentenciaSQL = "INSERT INTO LANGUAGE_FILM VALUES(?,?)";
            ps = conn.prepareStatement(sentenciaSQL);
            // Asigna los valores del arreglo            
            ps.setInt(1, Integer.parseInt(doblaje [0].toString()));
            ps.setString(2, doblaje [1].toString());
            ps.executeUpdate();
            return 1;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveDoblaje");
            return 0;
        }
       finally{
           desconectar();
       }
    }
    
    public int deleteLanguageFilm  (String idFilm, int idLag){
        // Borra un actor 
        // Conecta a la base de datos
        conectar();
        try{
            sentenciaSQL = "DELETE FROM LANGUAGE_FILM " +
                            "WHERE FILM_ID = ? AND " +
                            "LANGUAGE_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, idFilm);
            ps.setInt(2, idLag);
            int res = ps.executeUpdate();
            if (res == 1){
                return 0;
            }
            
            return 1;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "deleteaCTOR");
            if (ex.getErrorCode() ==  2292)
                return 1;
            return 2;
        }
       finally{
           desconectar();
       }
    }
    
    public int deleteDoblajeFromFilm(String idFilm){
        // Borra un actor 
        // Conecta a la base de datos
        conectar();
        try{
            sentenciaSQL = "DELETE FROM LANGUAGE_FILM " +
                            "WHERE FILM_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, idFilm);
            int res = ps.executeUpdate();
            if (res == 1){
                return 0;
            }       
            return 1;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "deleteDoblajeFromFilm");
            if (ex.getErrorCode() ==  2292)
                return 1;
            return 2;
        }
       finally{
           desconectar();
       }
    }
}