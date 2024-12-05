/*
 * Clase encargada de consultar, insertar, eliminar y modificar la tabla Autor
*/

package dataBase.dao;

import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;

/**
 *
 * @author alfonso
 */
public class AutorDAO extends Conexion {    
    // Obtiene un autor mediante su ID (id, nombre, apellido, pais)
    public Object[] GetAutorById(String id){
       conectar();
       Object [] autor = new Object[4];
       int i = 0;
       
       try{
                    
           sentenciaSQL =   "SELECT AUTOR_ID, NOMBRE, APELLIDO_PAT, PAIS " +
                            "FROM AUTOR " +
                            "JOIN PAIS USING (PAIS_ID) " +
                            "WHERE AUTOR_ID = ?";           
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, id); // Reemplaza el parámetro index (simbolo ?) con el str x
           rs = ps.executeQuery();
           
           // Llena el arreglo actor con el resultado
           while (rs.next()){
               autor[0] = (rs.getString(1));
               autor[1] = (rs.getString(2));  
               autor[2] = (rs.getString(3));  
               autor[3] = (rs.getString(4));
               i++;
           }           
           return autor;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetAutorById");
            return null;
        }
       finally{
           desconectar();           
       }
    }
    
    // Regresa una lista de todos los autores de un libro (id, nombre + apellido)
    public Object [][] GetAutoresByLibroId(int id){
       conectar();
       Object [][] autores;
       int i = 0;
       int count = 0;
       try{
           // Primero se cuenta el numero de autores en el libro
           sentenciaSQL =   "SELECT COUNT (AUTOR_ID) " +
                            "FROM AUTOR " +
                            "JOIN AUTORIA USING (AUTOR_ID) " +
                            "JOIN LIBRO USING (LIBRO_ID) " +
                            "WHERE LIBRO_ID = ? ";

            ps = conn.prepareStatement(sentenciaSQL);   // prepara la sentencia 
            ps.setInt(1, id);
            rs = ps.executeQuery();                     // Ejecuta la sentencia y la asigna al result set    
           
           if (rs.next()){
               count = rs.getInt(1);
           }
           
           autores = new Object[count][2];           
           sentenciaSQL  =  "SELECT AUTOR_ID, NOMBRE || ' ' || APELLIDO_PAT " +
                            "FROM AUTOR " +
                            "JOIN AUTORIA USING (AUTOR_ID) " +
                            "JOIN LIBRO USING (LIBRO_ID) " +
                            "WHERE LIBRO_ID = ? " +
                            "ORDER BY 2";   // Ordenar por la segunda columna     
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setInt(1, id);
           rs = ps.executeQuery();
           while (rs.next()){
               autores[i][0] = (rs.getString(1));
               autores[i][1] = (rs.getString(2));  
               i++;
           }           
           return autores;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetAutoresByLibroId");
            return null;
        }
       finally{
           desconectar();           
       }
    }

    // Obtiene todos los autores cuyo nombre o apellido se parezcan a nombre_apellido
    public Object [][] GetAutoresByNombreApellido(String nombre_apellido){
       conectar();
       Object [][] autores;
       int i = 0;
       int count = 0;
       nombre_apellido = "%" + nombre_apellido + "%";
       try{
           // Cuenta todos los actores que tengan un nombre parecido
           sentenciaSQL =   "SELECT COUNT(AUTOR_ID) " +
                            "FROM " +
                            "(SELECT AUTOR_ID " +
                            "FROM AUTOR " +
                            "WHERE UPPER (NOMBRE) LIKE UPPER(?) " +     // Todos los que tengan un nombre parecido
                            "UNION " +                                  // Union
                            "SELECT AUTOR_ID " +                        // Todos los que tengan un apellido parecido
                            "FROM AUTOR " +
                            "WHERE UPPER (APELLIDO_PAT) LIKE UPPER(?))";
            
            ps = conn.prepareStatement(sentenciaSQL);   // prepara la sentencia 
            ps.setString(1, nombre_apellido);
            ps.setString(2, nombre_apellido);
            rs = ps.executeQuery();                     // Ejecuta la sentencia y la asigna al result set         
           
            if (rs.next()){
                count = rs.getInt(1);
            }
           
            // Misma consulta, pero ahora puede ser guardada en el arreglo
            autores = new Object[count][2];                      
            sentenciaSQL =  "SELECT AUTOR_ID, NOMBRE || ' ' || APELLIDO_PAT " +
                            "FROM AUTOR " +
                            "WHERE UPPER (NOMBRE) LIKE UPPER(?) " +             // Todos los que tengan un nombre parecido
                            "UNION " +                                          // O
                            "SELECT AUTOR_ID, NOMBRE || ' ' || APELLIDO_PAT " + // Todos los que tengan un apellido parecido
                            "FROM AUTOR " +
                            "WHERE UPPER (APELLIDO_PAT) LIKE UPPER(?) " +
                            "ORDER BY 2";  
           
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, nombre_apellido);
           ps.setString(2, nombre_apellido);
           rs = ps.executeQuery();
           
           while (rs.next()){
               autores[i][0] = (rs.getString(1));     // Id del autor
               autores[i][1] = (rs.getString(2));     // Nombre y apellido del autor
               i++;
           }           
           return autores;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetAutoresByNombreApellido");
            return null;
        }
       finally{
           desconectar();           
       }
    }
    
    // Genera un nuevo autor (junto con su ID)
    public String SaveAutor(Object[] autor){
        // Se conecta a la base de datos
        conectar();
        try{
            String id = "";
            // genera el nuevo id            
            // Primera letra de su pais + (el numero mas alto + 1)
            sentenciaSQL =  "SELECT SUBSTR (?, 1, 1) || LPAD(NVL(MAX(TO_NUMBER(SUBSTR(AUTOR_ID, 2, 3))) + 1, 1), 3, '0') " +
                            "FROM AUTOR " +
                            "WHERE SUBSTR (AUTOR_ID, 1, 1) LIKE SUBSTR (?, 1, 1)";
            ps = conn.prepareStatement(sentenciaSQL);
            PaisDAO pais_dao = new PaisDAO();
            ps.setString(1, pais_dao.GetPaisById((Integer) autor[2])[1].toString());   // Pais del autor
            ps.setString(2, pais_dao.GetPaisById((Integer) autor[2])[1].toString());   // Pais del autor
            rs = ps.executeQuery();
            
            // guarda el nuevo id
            if (rs.next()){
                id = rs.getString(1);
            }
            
            // inserta mandando todos los datos, incluido en nuevo id
            sentenciaSQL = "INSERT INTO AUTOR VALUES(?, ?, ?, SYSDATE, ?)";
            ps = conn.prepareStatement(sentenciaSQL);
            // Asigna los valores del arreglo            
            ps.setString(1, id);                    // Id (calculado)
            ps.setString(2, autor[0].toString());   // Nombre (provisto)
            ps.setString(3, autor[1].toString());   // Apellido (provisto)
            ps.setInt(4, (Integer) autor[2]);       // Pais id (provisto)
            ps.executeUpdate();
            return id;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "SaveAutor");
            return null;
        }
        finally{
           desconectar();
       }
    }

    // Asocia a un autor con un libro (autor, libro)
    public int SaveAutoria(Object[] autoria){
        // Guarda el par autor_id, libro_id
        // Se conecta a la base de datos
        conectar();
        try{                                    
            sentenciaSQL = "INSERT INTO AUTORIA VALUES(?, ?)";   // inserta mandando todos los datos, incluido en nuevo id
            ps = conn.prepareStatement(sentenciaSQL);
            // Asigna los valores del arreglo            
            ps.setString(1, autoria[0].toString()); // Autor id
            ps.setInt(2, (Integer) autoria[1]);     // Libro id
            ps.executeUpdate();
            return 1;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "SaveAutoria");
            return 0;
        }
       finally{
           desconectar();
       }
    }

    // Actualiza la informacion de un autor (Id, Nombre, Apellido, Pais ID)
    public String UpdateAutor(Object autor[]){
       // se conecta a la base de datos
       conectar();
       try{
           // actualiza los datos
           sentenciaSQL = "UPDATE AUTOR SET " +
                          "NOMBRE = ?, " +
                          "APELLIDO_PAT = ?, " +
                          "PAIS_ID = ?, " +
                          "LAST_UPDATE = SYSDATE " +
                          "WHERE AUTOR_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            
            ps.setString(1, autor[1].toString());   // Nuevo nombre del autor
            ps.setString(2, autor[2].toString());   // Nuevo apellido del autor
            ps.setInt(3, (Integer) autor[3]);       // Nuevo pais del autor
            ps.setString(4, autor[0].toString());   // Id del autor
            ps.executeUpdate();
            return autor[0].toString();
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "UpdateAutor");
            return null;
        }
        finally{
           desconectar();
       }
    }   

    // Borra a un autor de la base de datos
    public int DeleteAutor(String id){
        // Conecta a la base de datos
        conectar();
        try{
            sentenciaSQL = "DELETE FROM AUTOR " +
                            "WHERE AUTOR_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, id);
            int res = ps.executeUpdate();
            if (res == 1){
                return 0;
            }
            
            return 1;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "DeleteAutor");
            if (ex.getErrorCode() ==  2292)
                return 1;
            return 2;
        }
       finally{
           desconectar();
       }
    }
    
    // Borra a un autor de un libro
    public int DeleteAutoria(String autor_id, int libro_id){
        // Conecta a la base de datos
        conectar();
        try{
            sentenciaSQL = "DELETE FROM AUTORIA " +
                            "WHERE LIBRO_ID = ? AND " +
                            "AUTOR_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, libro_id);
            ps.setString(2, autor_id);
            int res = ps.executeUpdate();
            if (res == 1){
                return 0;
            }
            
            return 1;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "DeleteAutoria");
            if (ex.getErrorCode() ==  2292)
                return 1;
            return 2;
        }
        finally{
           desconectar();
       }
    }
    
    // Borra todos los autores de un libro
    public int DeleteAutoriaFromLibro(int libro_id){
        // Conecta a la base de datos
        conectar();
        try{
            sentenciaSQL = "DELETE FROM AUTORIA " +
                            "WHERE LIBRO_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, libro_id);
            int res = ps.executeUpdate();
            if (res == 1){
                return 0;
            }
            
            return 1;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "DeleteAutoriaFromLibro");
            if (ex.getErrorCode() ==  2292)
                return 1;
            return 2;
        }
        finally{
           desconectar();
       }
    }

}
