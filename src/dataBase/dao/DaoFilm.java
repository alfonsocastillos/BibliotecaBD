/*
   Operaciones a la base de datos en la tabla pelicula
*/
package dataBase.dao;

import dataBase.Conexion;
import java.sql.*;
import oracle.jdbc.OracleTypes;

/* 
    Clase que realiza todas las transacciones a la base de datos, hereda de la
    clase conexión para ahorrar codigo
*/
public class DaoFilm extends Conexion {

    public Object [][] getFilms(){
        /* 
            Busca todas las peliculas, este metodo no se usa, solo está para demostar
            como se haría sin procedimientos almacenados 
        */
        // se conecta a la base de datos
        conectar();
        // crea un arreglo de beans para guardar los datos
        Object [][] films;
        // Contador
        int i = 0;
        // Para guardar la cantidad de registros
        int cant = 0;
        try{
            // Cuenta los registros en la base de datos
            sentenciaSQL = "SELECT COUNT (*) FROM FILM";
            ps = conn.prepareStatement(sentenciaSQL);
            rs = ps.executeQuery();
            
            if (rs.next()){
                cant = rs.getInt(1);
            }
            // Consulta los registros 
             films = new Object[cant][7];          
            sentenciaSQL  = "SELECT FILM_ID, TITLE, RELEASE_YEAR, " +
                            "LENGTH, RATING , CATEGORY, RENTAL_RATE " +
                            "FROM FILM " +
                            "JOIN LANGUAGE ON (ORIGINAL_LANGUAGE_ID = LANGUAGE_ID) " +
                            "JOIN RATING USING (RATING_ID) " +
                            "JOIN CATEGORY USING (CATEGORY_ID) " +
                            "ORDER BY TITLE";
            // prepara la sentencia 
            ps = conn.prepareStatement(sentenciaSQL);
            // Ejecuta la sentencia y la asigna al result set 
            rs = ps.executeQuery();
            // Recorre el result set para obtener los datos y asignarlos
            // al arreglo de beans
            while (rs.next()){
                films[i][0] = rs.getInt(1);
                films[i][1] = rs.getString(2);  
                films[i][2] = rs.getInt(3);  
                films[i][3] = rs.getInt(4);  
                films[i][4] = rs.getString(5);  
                films[i][5] = rs.getString(6);  
                films[i][6] = rs.getFloat(7);  
                i++;
            }           
            return films;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getFilms");
            return null;
        }
       finally{
           desconectar();           
       }
    }
    
    public Object [][] getFilmsByTitleDesc(String titleDesc){
        /* 
            Busca todas las peliculas, este metodo no se usa, solo está para demostar
            como se haría sin procedimientos almacenados 
        */
        // se conecta a la base de datos
        conectar();
        // crea un arreglo de beans para guardar los datos
        Object [][] films;
        // Contador
        int i = 0;
        // Para guardar la cantidad de registros
        int cant = 0;
        titleDesc = "%" + titleDesc + "%";
        try{
            // Cuenta los registros en la base de datos
            sentenciaSQL = "SELECT  COUNT (FILM_ID) FROM (" +
                            "SELECT FILM_ID " +
                            "FROM FILM " +
                            "JOIN LANGUAGE ON (ORIGINAL_LANGUAGE_ID = LANGUAGE_ID) " +
                            "JOIN RATING USING (RATING_ID) " +
                            "JOIN CATEGORY USING (CATEGORY_ID) " +
                            "WHERE UPPER(TITLE) LIKE UPPER(?)  " +
                            "UNION " +
                            "SELECT FILM_ID " +
                            "FROM FILM " +
                            "JOIN LANGUAGE ON (ORIGINAL_LANGUAGE_ID = LANGUAGE_ID) " +
                            "JOIN RATING USING (RATING_ID) " +
                            "JOIN CATEGORY USING (CATEGORY_ID) " +
                            "WHERE UPPER(CATEGORY.CATEGORY) LIKE UPPER(?))"; //agregar descipción ***
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, titleDesc);
            ps.setString(2, titleDesc);
            rs = ps.executeQuery();
            
            if (rs.next()){
                cant = rs.getInt(1);
            }
            // Consulta los registros 
             films = new Object[cant][7];          
            sentenciaSQL  = "SELECT FILM_ID, TITLE, RELEASE_YEAR," +
                            "LENGTH, RATING , CATEGORY, RENTAL_RATE " +
                            "FROM FILM " +
                            "JOIN LANGUAGE ON (ORIGINAL_LANGUAGE_ID = LANGUAGE_ID) " +
                            "JOIN RATING USING (RATING_ID) " +
                            "JOIN CATEGORY USING (CATEGORY_ID) " +
                            "WHERE UPPER(TITLE) LIKE UPPER(?)  " +
                            "UNION " +
                            "SELECT FILM_ID, TITLE, RELEASE_YEAR, " +
                            "LENGTH, RATING , CATEGORY, RENTAL_RATE " +
                            "FROM FILM " +
                            "JOIN LANGUAGE ON (ORIGINAL_LANGUAGE_ID = LANGUAGE_ID) " +
                            "JOIN RATING USING (RATING_ID) " +
                            "JOIN CATEGORY USING (CATEGORY_ID) " +
                            "WHERE UPPER(CATEGORY.CATEGORY) LIKE UPPER(?)  " +
                            "ORDER BY TITLE";
            // prepara la sentencia 
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, titleDesc);
            ps.setString(2, titleDesc);
            // Ejecuta la sentencia y la asigna al result set 
            rs = ps.executeQuery();
            // Recorre el result set para obtener los datos y asignarlos
            // al arreglo de beans
            while (rs.next()){
                films[i][0] = rs.getInt(1);
                films[i][1] = rs.getString(2);  
                films[i][2] = rs.getInt(3);  
                films[i][3] = rs.getInt(4);  
                films[i][4] = rs.getString(5);  
                films[i][5] = rs.getString(6);  
                films[i][6] = rs.getFloat(7);  
                i++;
            }           
            return films;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getFilms");
            return null;
        }
       finally{
           desconectar();           
       }
    }
    
    public Object[] getFilm(String id){
        // Consulta una pelicula buscando por su id
        // Se conecta a la base de datos
        conectar();
        Object film[];// Para guardar la cantidad de registros
        try{
           
            // Consulta los registros 
            film = new Object[11];          
            sentenciaSQL  = "SELECT  FILM_ID, TITLE, FILM.DESCRIPTION, RELEASE_YEAR, " +
                            "LANGUAGE, RENTAL_DURATION, LENGTH, RENTAL_RATE, " +
                            "REPLACEMENT_COST, RATING, CATEGORY " +
                            "FROM FILM " +
                            "JOIN LANGUAGE ON (ORIGINAL_LANGUAGE_ID = LANGUAGE_ID) " +
                            "JOIN RATING USING (RATING_ID) " +
                            "JOIN CATEGORY USING (CATEGORY_ID) " +
                            "WHERE FILM_ID = ?";
            // prepara la sentencia 
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, id);
            // Ejecuta la sentencia y la asigna al result set 
            rs = ps.executeQuery();
            rs.next();
            // Recupera los valores
            film[0] = rs.getString(1);
            film[1] = rs.getString(2);  
            film[2] = rs.getString(3);  
            film[3] = rs.getInt(4);  
            film[4] = rs.getString(5);
            film[5] = rs.getInt(6);
            film[6] = rs.getInt(7);  
            film[7] = rs.getFloat(8);  
            film[8] = rs.getFloat(9);                 
            film[9] = rs.getString(10);  
            film[10] = rs.getString(11);  
            return film;
       }
       catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getFilm");
            return null;
        }
       finally{
           desconectar();           
       }
    }
    
    public String saveFilm(Object[] film){
        // Guarda una pelicula
        // Se conecta a la base de datos
        conectar();
        try{
            String id="";
            // genera el nuevo id
            sentenciaSQL = "SELECT SUBSTR (EXTRACT(YEAR FROM SYSDATE),3,2)||LPAD(NVL(MAX(TO_NUMBER(SUBSTR(FILM_ID,3,3)))+1,1),3,'0') " +
                           "FROM FILM " +
                           "WHERE SUBSTR(FILM_ID,1,2) LIKE SUBSTR (EXTRACT(YEAR FROM SYSDATE),3,2)";
            ps = conn.prepareStatement(sentenciaSQL);
            rs = ps.executeQuery();
            // guarda el nuevo id
            if (rs.next()){
                id = rs.getString(1);
            }
            // inseta mandando todos los datos, incluido en nuevo id
            sentenciaSQL = "Insert INTO FILM VALUES(?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sentenciaSQL);
            // Asigna los valores del arreglo  
            ps.setString(1, id);
            ps.setString(2, film [1].toString());
            ps.setString(3,film [2].toString());
            ps.setInt(4,Integer.parseInt(film [3].toString()));
            ps.setInt(5,Integer.parseInt(film [4].toString()));
            ps.setInt(6,Integer.parseInt(film [5].toString()));
            ps.setFloat(7,Float.parseFloat(film [6].toString()));
            ps.setInt(8,Integer.parseInt(film [7].toString()));
            ps.setFloat(9,Float.parseFloat(film [8].toString()));
            ps.setInt(10,Integer.parseInt(film [9].toString()));
            ps.setInt(11,Integer.parseInt(film [10].toString()));
            ps.executeUpdate();
            return id;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveFilm");
            return null;
        }
       finally{
           desconectar();
       }
    }
    
    // Actualiza los datos 
    public String updateFilm(Object film[]){
        // se conecta a la base de datos
       conectar();
       try{
           // actualiza los datos
           sentenciaSQL = "UPDATE FILM SET " +
                          "TITLE = ?, " +
                          "DESCRIPTION = ?, " +
                          "RELEASE_YEAR = ?, " +
                          "ORIGINAL_LANGUAGE_ID = ?, " +
                          "RENTAL_DURATION = ?, " +
                          "RENTAL_RATE = ?, " +
                          "LENGTH = ?, " +
                          "REPLACEMENT_COST = ?, " +
                          "RATING_ID = ?, " +
                          "CATEGORY_ID = ? " +
                          "WHERE FILM_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            
            ps.setString(1, film[1].toString());
            ps.setString(2, film[2].toString());
            ps.setInt(3, Integer.parseInt(film[3].toString()));
            ps.setInt(4, Integer.parseInt(film[4].toString()));
            ps.setInt(5, Integer.parseInt(film[5].toString()));
            ps.setFloat(6, Float.parseFloat(film[6].toString()));
            ps.setInt(7, Integer.parseInt(film[7].toString()));
            ps.setFloat(8, Float.parseFloat(film[8].toString()));
            ps.setInt(9,Integer.parseInt(film[9].toString()));
            ps.setInt(10,Integer.parseInt(film[10].toString()));
            ps.setString(11, film[0].toString());
            ps.executeUpdate();
            return film[0].toString();
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "updateFilm");
            return null;
        }
       finally{
           desconectar();
       }
    }   
    
    public int deleteFilm(String id){
        // Borra primero el casting
        DaoActor casting = new DaoActor();
        int res = casting.deleteCastingFromFilm(id);
        
        // Borra primero el casting
        DaoLang doblaje = new DaoLang();
        int resD = doblaje.deleteDoblajeFromFilm(id);
        
        if (res == 1 && resD == 1){
            // Si borro el casting, borra una pelicula 
            // Conecta a la base de datos
            conectar();
            try{
                sentenciaSQL = "DELETE FROM FILM " +
                                "WHERE FILM_ID = ?";
                ps = conn.prepareStatement(sentenciaSQL);
                ps.setString(1, id);
                res = ps.executeUpdate();
                if (res == 1){
                    return 0;
                }            
                return 1;
            }
            catch (SQLException ex){
                System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                        "\n\n" + sentenciaSQL + "\n\nUbicación: " + "deleteFilm");
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
    
    // ***** Las mismas funciones con procedimientos almacenados    
    public Object [][] getFilmsPA(){
        // Busca todas las peliculas
        // se conecta a la base de datos
        conectar();
        // se crea un arreglo bidimencional para guardar los datos
        Object films[][];
        // Contador
        int i = 0;
        
        try{
            // Prepara la llamada al PA
            cstmt = conn.prepareCall("CALL GET_FILMS(?,?)");
            // Se asigna un cursor para retornar los datos
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            // Se asigna un entero para retornar la cantiad de registros
            cstmt.registerOutParameter (2, Types.INTEGER);
            // Ejecuta la llamada
            cstmt.execute();
            //Se obtiene el cursor en forma de ResultSe 
            rs = (ResultSet)cstmt.getObject(1);
            // Obtiene la cantidad de registros para crear el arreglo
            films = new Object[cstmt.getInt(2)][9];
            
            // Recorre el result set para obtener los datos y asignarlos
            // al arreglo
            while (rs.next()){
                films[i][0] = rs.getInt(1);
                films[i][1] = rs.getString(2);  
                films[i][2] = rs.getString(3);  
                films[i][3] = rs.getInt(4);  
                films[i][4] = rs.getString(5);  
                films[i][5] = rs.getInt(6);  
                films[i][6] = rs.getString(7);  
                films[i][7] = rs.getString(8);  
                films[i][8] = rs.getFloat(9);  
                i++;
            }           
            cstmt.close();
            return films;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getFilms");
            return null;
        }
        finally{
            desconectar();           
        }
    }
    
    public Object[] getFilmPA(int id){
        // Consulta una pelicula buscando por su id
        // Se conecta a la base de datos
        conectar();
        Object film[];
       try{            
            // Prepara la llamada al PA
            cstmt = conn.prepareCall("CALL GET_FILM(?,?,?,?,?,?,?,?,?,?,?)");
            // Asigna el id para buscar
            cstmt.setInt(1, id);
            // Se asigna un entero para retornar el id (es in out)
            cstmt.registerOutParameter(1, OracleTypes.INTEGER);
            // Se asigna los valores para retornar los datos
            cstmt.registerOutParameter (2, Types.VARCHAR);
            cstmt.registerOutParameter (3, Types.VARCHAR);
            cstmt.registerOutParameter (4, Types.INTEGER);
            cstmt.registerOutParameter (5, Types.VARCHAR);
            cstmt.registerOutParameter (6, Types.INTEGER);
            cstmt.registerOutParameter (7, Types.INTEGER);
            cstmt.registerOutParameter (8, Types.FLOAT);
            cstmt.registerOutParameter (9, Types.FLOAT);
            cstmt.registerOutParameter (10, Types.VARCHAR);
            cstmt.registerOutParameter (11, Types.VARCHAR);
            // Ejecuta la llamada
            cstmt.execute();
            // Recupera los valores
            film  = new Object[11];
            film[0] = cstmt.getInt(1);
            film[1] = cstmt.getString(2);  
            film[2] = cstmt.getString(3);  
            film[3] = cstmt.getInt(4);  
            film[4] = cstmt.getString(5);
            film[5] = cstmt.getInt(6);
            film[6] = cstmt.getInt(7);  
            film[7] = cstmt.getFloat(8);  
            film[8] = cstmt.getFloat(9);                 
            film[9] = cstmt.getString(10);  
            film[10] = cstmt.getString(11);  
            cstmt.close();
            return film;
       }
       catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getFilm");
            return null;
        }
       finally{
           desconectar();           
       }
    }
    
    public int saveFilmPA(Object[] film){
        // Guarda una pelicula
        // Se conecta a la base de datos
        conectar();
        try{
            int id;
            // Prepara la llamada al PA
            cstmt = conn.prepareCall("CALL SAVE_FILM(?,?,?,?,?,?,?,?,?,?,?)");
            // Asigna los valores del arreglo            
            cstmt.setString(1, film [1].toString());
            cstmt.setString(2,film [2].toString());
            cstmt.setInt(3,Integer.parseInt(film [3].toString()));
            cstmt.setInt(4,Integer.parseInt(film [4].toString()));
            cstmt.setInt(5,Integer.parseInt(film [5].toString()));
            cstmt.setFloat(6,Float.parseFloat(film [6].toString()));
            cstmt.setInt(7,Integer.parseInt(film [7].toString()));
            cstmt.setFloat(8,Float.parseFloat(film [8].toString()));
            cstmt.setInt(9,Integer.parseInt(film [9].toString()));
            cstmt.setInt(10,Integer.parseInt(film [10].toString()));
            // Asigna el id para retornar
            cstmt.registerOutParameter(11, OracleTypes.INTEGER);
            cstmt.execute();
            id = cstmt.getInt(11);
            return id;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveFilm");
            return 0;
        }
       finally{
           desconectar();
       }
    }
    
    // Actualiza los datos 
    public int updateFilmPA(Object film[]){
        // se conecta a la base de datos
       conectar();
       try{
            // Prepara la llamada al PA
            cstmt = conn.prepareCall("CALL UPDATE_FILM(?,?,?,?,?,?,?,?,?,?,?)");
            cstmt.setInt(1, Integer.parseInt(film[0].toString()));
            cstmt.setString(2, film[1].toString());
            cstmt.setString(3, film[2].toString());
            cstmt.setInt(4, Integer.parseInt(film[3].toString()));
            cstmt.setInt(5, Integer.parseInt(film[4].toString()));
            cstmt.setInt(6, Integer.parseInt(film[5].toString()));
            cstmt.setFloat(7, Float.parseFloat(film[6].toString()));
            cstmt.setInt(8, Integer.parseInt(film[7].toString()));
            cstmt.setFloat(9, Float.parseFloat(film[8].toString()));
            cstmt.setInt(10,Integer.parseInt(film[9].toString()));
            cstmt.setInt(11,Integer.parseInt(film[10].toString()));
            cstmt.execute();
            return Integer.parseInt(film[0].toString());
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "updateFilm");
            return 0;
        }
       finally{
           desconectar();
       }
    }   
    
    public int deleteFilmPA(String id){
        // Borra una pelicula 
        // Conecta a la base de datos
        conectar();
        try{
            // Prepara la llamada al PA
            cstmt = conn.prepareCall("CALL DELETE_FILM(?,?)");            
            cstmt.setString(1,id);
            cstmt.registerOutParameter(2, OracleTypes.INTEGER);
            cstmt.execute();
            return cstmt.getInt(2);
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "deleteFilm");
            return 2;
        }
       finally{
           desconectar();
       }
    }
}