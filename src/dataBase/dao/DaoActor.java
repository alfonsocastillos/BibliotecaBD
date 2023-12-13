/*
   Operaciones a la base de datos en la tabla clasificación
*/
package dataBase.dao;

import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;

public class DaoActor extends Conexion {
    
    public Object [][] getActors(){
       conectar();
       Object [][] actors;
       int i = 0;
       int cant = 0;
       try{
           sentenciaSQL = "SELECT COUNT(*) FROM ACTOR"; // Numero de actores
           // Convierte el str a un sentencia utilizable en SQL
           ps = conn.prepareStatement(sentenciaSQL); 
           // Resultado de la consulta
           rs = ps.executeQuery();  
           
           // Numero de actores (COUNT)
           if (rs.next()){
               cant = rs.getInt(1);
           }
           
           // Arreglo de todos los actores (ID, Nombre Completo)
           actors = new Object[cant][2];           
           sentenciaSQL  = "SELECT ACTOR_ID, FIRST_NAME || ' ' || LAST_NAME FROM ACTOR ORDER BY 2";           
           ps = conn.prepareStatement(sentenciaSQL);
           rs = ps.executeQuery();
           // Agregar a todos los actores al arreglo (migrar de rs a actores)
           while (rs.next()){
               actors[i][0]=(rs.getString(1));
               actors[i][1]=(rs.getString(2));  
               i++;
           }           
           return actors;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getActors");
            return null;
        }
       finally{
           desconectar();           
       }
    }
    
    public Object [] getActorById(String id){
       conectar();
       Object [] actor = new Object[3];
       int i = 0;
       int cant = 0;
       try{
                    
           sentenciaSQL  = "SELECT ACTOR_ID, FIRST_NAME, LAST_NAME FROM ACTOR WHERE ACTOR_ID = ?";           
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, id); // Reemplaza el parámetro index (simbolo ?) con el str x
           rs = ps.executeQuery();
           // Llena el arreglo actor con el resultado
           while (rs.next()){
               actor[0]=(rs.getString(1));
               actor[1]=(rs.getString(2));  
               actor[2]=(rs.getString(3));  
               i++;
           }           
           return actor;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getActors");
            return null;
        }
       finally{
           desconectar();           
       }
    }
    
    // Regresa una lista de todos los actores en una pelicula 
    public Object [][] getActorsFilm(String id){
       conectar();
       Object [][] actors;
       int i = 0;
       int cant = 0;
       try{
           // Primero se cuenta el numero de actores en la pelicula
           sentenciaSQL =   "SELECT COUNT (ACTOR_ID) " +
                            "FROM ACTOR " +
                            "JOIN CASTING USING (ACTOR_ID) " +
                            "JOIN FILM USING (FILM_ID) " +
                            "WHERE FILM_ID = ? ";

            // prepara la sentencia 
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, id);
            // Ejecuta la sentencia y la asigna al result set    
            rs = ps.executeQuery();
           
           if (rs.next()){
               cant = rs.getInt(1);
           }
           
           actors = new Object[cant][2];           
           sentenciaSQL  =  "SELECT ACTOR_ID, FIRST_NAME || ' ' || LAST_NAME " +
                            "FROM ACTOR " +
                            "JOIN CASTING USING (ACTOR_ID) " +
                            "JOIN FILM USING (FILM_ID) " +
                            "WHERE FILM_ID = ? " +
                            "ORDER BY 2";   // Ordenar por la segunda columna     
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, id);
           rs = ps.executeQuery();
           while (rs.next()){
               actors[i][0]=(rs.getString(1));
               actors[i][1]=(rs.getString(2));  
               i++;
           }           
           return actors;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getActors");
            return null;
        }
       finally{
           desconectar();           
       }
    }
    
    public Object [][] getActorsByFirstLastName(String firstLastName){
       conectar();
       Object [][] actors;
       int i = 0;
       int cant = 0;
       firstLastName = "%" + firstLastName + "%";
       try{
           // Cuenta todos los actores que tengan un nombre parecido
           sentenciaSQL =   "SELECT COUNT(ACTOR_ID) " +
                            "FROM " +
                            "(SELECT ACTOR_ID " +
                            "FROM ACTOR " +
                            "WHERE UPPER (FIRST_NAME) LIKE UPPER(?) " + // Todos los que tengan un nombre pareciso
                            "UNION " +                                  // Union
                            "SELECT ACTOR_ID " +                        // Todos los que tengan un apellido parecido
                            "FROM ACTOR " +
                            "WHERE UPPER (LAST_NAME) LIKE UPPER(?))";

            // prepara la sentencia 
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, firstLastName);
            ps.setString(2, firstLastName);
            // Ejecuta la sentencia y la asigna al result set    
            rs = ps.executeQuery();
           
           if (rs.next()){
               cant = rs.getInt(1);
           }
           
           // Misma consulta, pero ahora puede ser guardada en el arreglo
           actors = new Object[cant][2];           
           sentenciaSQL  =  "SELECT ACTOR_ID, FIRST_NAME || ' ' || LAST_NAME " +
                            "FROM ACTOR " +
                            "WHERE UPPER (FIRST_NAME) LIKE UPPER(?) " +
                            "UNION " +
                            "SELECT ACTOR_ID, FIRST_NAME || ' ' || LAST_NAME " +
                            "FROM ACTOR " +
                            "WHERE UPPER (LAST_NAME) LIKE UPPER(?) " +
                            "ORDER BY 2";         
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, firstLastName);
           ps.setString(2, firstLastName);
           rs = ps.executeQuery();
           while (rs.next()){
               actors[i][0]=(rs.getString(1));
               actors[i][1]=(rs.getString(2));  
               i++;
           }           
           return actors;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getActorsByFirstLastName");
            return null;
        }
       finally{
           desconectar();           
       }
    }
    
    public String saveActor(Object[] actor){
        // Guarda un actor
        // Se conecta a la base de datos
        conectar();
        try{
            String id = "";
            // genera el nuevo id
                // Busca el id más alto existente y le suma 1
            sentenciaSQL = "SELECT SUBSTR (EXTRACT(YEAR FROM SYSDATE),3,2)||LPAD(EXTRACT(MONTH FROM SYSDATE),2,'0') || LPAD(NVL(MAX(TO_NUMBER(SUBSTR(ACTOR_ID,5,3)))+1,1),3,'0') " +
                           "FROM ACTOR " +
                           "WHERE SUBSTR(ACTOR_ID,1,4) LIKE SUBSTR (EXTRACT(YEAR FROM SYSDATE),3,2)||LPAD(EXTRACT(MONTH FROM SYSDATE),2,'0')";
            ps = conn.prepareStatement(sentenciaSQL);
            rs = ps.executeQuery();
            // guarda el nuevo id
            if (rs.next()){
                id = rs.getString(1);
            }
            // inseta mandando todos los datos, incluido en nuevo id
            sentenciaSQL = "INSERT INTO ACTOR VALUES(?,?, ?)";
            ps = conn.prepareStatement(sentenciaSQL);
            // Asigna los valores del arreglo            
            ps.setString(1, id);
            ps.setString(2, actor [0].toString());
            ps.setString(3,actor [1].toString());
            ps.executeUpdate();
            return id;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveActor");
            return null;
        }
       finally{
           desconectar();
       }
    }
    
    public int saveCasting(Object[] cast){
        // Guarda el par film_id, actor_id
        // Se conecta a la base de datos
        conectar();
        try{                        
            // inseta mandando todos los datos, incluido en nuevo id
            sentenciaSQL = "INSERT INTO CASTING VALUES(?,?)";
            ps = conn.prepareStatement(sentenciaSQL);
            // Asigna los valores del arreglo            
            ps.setString(1, cast [0].toString());
            ps.setString(2, cast [1].toString());
            ps.executeUpdate();
            return 1;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveCasting");
            return 0;
        }
       finally{
           desconectar();
       }
    }
    
    // Actualiza los datos 
    public String update(Object film[]){
        // se conecta a la base de datos
       conectar();
       try{
           // actualiza los datos
           sentenciaSQL = "UPDATE ACTOR SET " +
                          "FIRST_NAME = ?, " +
                          "LAST_NAME = ? " +
                          "WHERE ACTOR_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            
            ps.setString(1, film[1].toString());
            ps.setString(2, film[2].toString());
            ps.setString(3, film[0].toString());
            ps.executeUpdate();
            return film[0].toString();
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "update en actor");
            return null;
        }
       finally{
           desconectar();
       }
    }   
    
    public int delete(String id){
        // Borra un actor 
        // Conecta a la base de datos
        conectar();
        try{
            sentenciaSQL = "DELETE FROM ACTOR " +
                            "WHERE ACTOR_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, id);
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
    
     public int deleteCasting(String idFilm, String idActor){
        // Borra un actor 
        // Conecta a la base de datos
        conectar();
        try{
            sentenciaSQL = "DELETE FROM CASTING " +
                            "WHERE FILM_ID = ? AND " +
                            "ACTOR_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, idFilm);
            ps.setString(2, idActor);
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
     
     public int deleteCastingFromFilm(String idFilm){
        // Borra un actor 
        // Conecta a la base de datos
        conectar();
        try{
            sentenciaSQL = "DELETE FROM CASTING " +
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
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "deleteaCTOR");
            if (ex.getErrorCode() ==  2292)
                return 1;
            return 2;
        }
       finally{
           desconectar();
       }
    }
}