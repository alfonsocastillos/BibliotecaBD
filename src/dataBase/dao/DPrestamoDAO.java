/*
 Operaciones a la base de datos en la tabla detalles de prestamo
 */
package dataBase.dao;

import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;

public class DPrestamoDAO extends Conexion { // Para llamar procedimientos almacenados
    
       public int saveDPrestamo (String idPrestamo, int idLibro){ // Guarda un prestamo y  se conecta a la base de datos
        conectar();
        try{

            sentenciaSQL = " INSERT INTO DETALLES_PRESTAMO VALUES (?,?)";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, idPrestamo);
            ps.setInt(2, idLibro);
            ps.executeUpdate();
            return 1;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveDPrestamo");
            return 0;
        }
       finally{
           desconectar();
       }
    }  
        
    // Modificar detalles de prestamo
    public String UpdateDPrestamo(Object[] DPres)
    {       
        conectar(); // se conecta a la base de datos
        try{
           // actualiza los datos
           sentenciaSQL =   "UPDATE DPRESTAMO SET " +
                            "LIBRO_ID = ?, " +
                            "PRESTAMO_ID = ?, " +
                            "WHERE DPRESTAMO_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, DPres[1].toString());  // libro id
            ps.setString(2, DPres[2].toString());  // prestamo id
            ps.setString(3, DPres[3].toString());  // DPrestamo ID

            ps.executeUpdate();
           
            return DPres[0].toString();               // Regresa el id del detalle del prestamo
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "UpdateDPrestamo");
            return null;
        }
        finally{
           desconectar();
       }
    }

    // Consultar todos los detalles de prestamos
    public Object [][] GetAllDPrestamosByGenero(String genero_id){
        conectar();
        Object [][] dpres;
        int i = 0;
        int count = 0;
        try{
            sentenciaSQL = "SELECT COUNT(*) FROM DPRESTAMOS WHERE GENERO_ID = ?";     // Numero de detalles de prestamos en ese genero
            ps = conn.prepareStatement(sentenciaSQL);     // Convierte el str a un sentencia utilizable en SQL        
            ps.setString(1, genero_id);        // Nuevo numero 
            rs = ps.executeQuery();                          // Resultado de la consulta

            // Numero de detalles de prestamos en ese genero (COUNT)
            if (rs.next()){
                count = rs.getInt(1);
            }

            // Arreglo de todos los detalles de prestamos  (ID, libro id, prestamo id, genero id)
            dpres = new Object[count][7];           
            sentenciaSQL  = "SELECT DPRESTAMO_ID, LIBRO_ID, PRESTAMO_ID, GENERO_ID " +
                            "FROM DPRESTAMO WHERE GENERO_ID = ? ORDER BY 2";           
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, genero_id);
            rs = ps.executeQuery();

            // Agregar a todos los detalles de prestamos  al arreglo (migrar de rs a pres)
            while (rs.next()){
                dpres[i][0] = (rs.getString(1));     // Id
                dpres[i][1] = (rs.getString(2));     // libro id
                dpres[i][2] = (rs.getString(3));     // prestamo id
                dpres[i][3] = (rs.getString(4));     // genero id 
                i++;
            }           
            return dpres;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetAllDPrestamosByGenero");
            return null;
        }
        finally{
           desconectar();           
       }
    }  

    // Consultar un detalle de prestamo  por id
    public Object[] GetDPrestamoById(String dprestamo_id)
    {
        conectar();
        Object [] dprestamo = new Object[7];
       
        try{                    
            sentenciaSQL  = "SELECT DPRESTAMO_ID, LIBRO_ID, PRESTAMO_ID, FROM DPRESTAMO " + 
                            "WHERE DPRESTAMO_ID = ?";           
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, dprestamo_id);    // Reemplaza el parámetro index (simbolo ?) con el str x
            rs = ps.executeQuery();

            // Llena el arreglo con el resultado
            while (rs.next()){
                dprestamo[0] = (rs.getString(1));
                dprestamo[1] = (rs.getString(2));  
                dprestamo[2] = (rs.getString(3));
                dprestamo[3] = (rs.getString(4));  
                dprestamo[4] = (rs.getString(5));
            }           
            return dprestamo;
        }
         catch (SQLException ex){
             System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                     "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetDPrestamoById");
             return null;
        }
        finally{
           desconectar();           
        }

    }
    
    // Borrar un detalle de prestamo
    public int DeleteDPrestamo(String dprestamo_id){
        conectar(); // Conecta a la base de datos
        try{
            // Cambiar a NULL el detalle de prestamo de cualquier cliente con el detalle a borrar
            sentenciaSQL =  "UPDATE CLIENTE" +
                            "SET DPRESTAMO_ID = NULL " +
                            "WHERE DPRESTAMO_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, dprestamo_id);
            ps.executeUpdate();   
            return 1;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "DeleteDPrestamo");
            if (ex.getErrorCode() ==  2292)
                return 1;
            return 2;
        }
        finally{
           desconectar();
       }
    }      
}
