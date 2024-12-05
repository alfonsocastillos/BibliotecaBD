/*
   Clase encargada de consultar, insertar, eliminar y modificar tabla de prestamo
 */
package dataBase.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;
   
public class PrestamoDAO extends Conexion { 
    
    public int SavePrestamo(Object[] prestamo) {        
        conectar();
        try {
            String id = null;
         
            // genera el nuevo id
            sentenciaSQL = "SELECT SUBSTR (EXTRACT(YEAR FROM SYSDATE),3,2)||LPAD(EXTRACT(MONTH FROM SYSDATE),2,'0') || LPAD(EXTRACT(DAY FROM SYSDATE),2,'0')||LPAD(NVL(MAX(TO_NUMBER(SUBSTR(PRESTAMO_ID,7,3)))+1,1),3,'0') \n" +
                           " FROM PRESTAMO\n" +
                           " WHERE SUBSTR(PRESTAMO_ID,1,6) LIKE SUBSTR (EXTRACT(YEAR FROM SYSDATE),3,2)||LPAD(EXTRACT(MONTH FROM SYSDATE),2,'0') || LPAD(EXTRACT(DAY FROM SYSDATE),2,'0')";
            ps = conn.prepareStatement(sentenciaSQL);
            rs = ps.executeQuery();
            
            // guarda el nuevo id
            if (rs.next()) {
                id = rs.getString(1);
            }
            
            sentenciaSQL = "INSERT INTO PRESTAMO VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, Integer.valueOf(id));          // Id del prestamo
            ps.setString(2, prestamo[0].toString());    // Fecha inicio
            ps.setString(3, prestamo[1].toString());    // Fecha entrega
            ps.setInt(4, (int) prestamo[2]);            // Id del cliente 
            ps.setString(5, prestamo[3].toString());    // Id del empleado
            ps.executeUpdate();
            return Integer.valueOf(id);
        }
        catch (SQLException ex) {
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveLibro");
            return 0;
        }
        finally{
            desconectar();
        }
    }   

    // Modificar un prestamo
    public String UpdatePrestamo(Object[] Pres)
    {       
        conectar(); // se conecta a la base de datos
        try{
           // actualiza los datos
           sentenciaSQL =   "UPDATE PRESTAMO SET " +
                            "FECHA_PRESTAMO = ?, " +
                            "FECHA_ENTREGA = ?, " +
                            "CLIENTE_ID = ?, " +
                            "EMPLEADO_ID = ?, " +
                            "WHERE PRESTAMO_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, Pres[1].toString());  // Nueva fecha de prestamo
            ps.setString(2, Pres[2].toString());  // nueva fecha de entrega 
            ps.setString(3, Pres[3].toString());  // nuevo cliente ID
            ps.setString(4, Pres[4].toString());  // nuevo empleado ID
            ps.setString(5, Pres[1].toString());  // nuevo prestamo ID
            ps.executeUpdate();
           
            return Pres[0].toString();               // Regresa el id del prestamo
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "UpdatePrestamo");
            return null;
        }
        finally{
           desconectar();
       }
    }

    // Consultar todos los prestamos por genero
    public Object [][] GetAllPrestamosByGenero(String genero_id){
        conectar();
        Object [][] pres;
        int i = 0;
        int count = 0;
        try{
            sentenciaSQL = "SELECT COUNT(*) FROM PRESTAMOS WHERE GENERO_ID = ?";     // Numero de prestamos en ese genero
            ps = conn.prepareStatement(sentenciaSQL);     // Convierte el str a un sentencia utilizable en SQL        
            ps.setString(1, genero_id);        // Nuevo numero 
            rs = ps.executeQuery();                          // Resultado de la consulta

            // Numero de prestamos en ese genero (COUNT)
            if (rs.next()){
                count = rs.getInt(1);
            }

            // Arreglo de todos los prestamos  (ID, fecha_prestamo, fecha_entrega, cliente_ID, empleado_ID, genero id)
            pres = new Object[count][7];           
            sentenciaSQL  = "SELECT PRESTAMO_ID, FECHA_PRESTAMO, FECHA_ENTREGA, CLIENTE_ID, EMPLEADO_ID, GENERO_ID " +
                            "FROM PRESTAMO WHERE GENERO_ID = ? ORDER BY 2";           
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, genero_id);
            rs = ps.executeQuery();

            // Agregar a todos los prestamos  al arreglo (migrar de rs a pres)
            while (rs.next()){
                pres[i][0] = (rs.getString(1));     // Id
                pres[i][1] = (rs.getString(2));     // fecha_prestamo
                pres[i][2] = (rs.getString(3));     // fecha_entrega
                pres[i][3] = (rs.getString(4));     // Cliente_id
                pres[i][4] = (rs.getString(5));     // empleado_id
                pres[i][5] = (rs.getString(6));     // genero_id
                i++;
            }           
            return pres;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetAllPrestamosByGenero");
            return null;
        }
        finally{
           desconectar();           
       }
    }  

    // Consultar un prestamo  por id
    public Object[] GetPrestamoById(String prestamo_id)
    {
        conectar();
        Object [] prestamo = new Object[7];
       
        try{                    
            sentenciaSQL  = "SELECT PRESTAMO_ID, FECHA_PRESTAMO, FECHA_ENTREGA, CLIENTE_ID, EMPLEADO_ID, FROM PRESTAMO " + 
                            "WHERE PRESTAMO_ID = ?";           
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, prestamo_id);    // Reemplaza el parámetro index (simbolo ?) con el str x
            rs = ps.executeQuery();

            // Llena el arreglo con el resultado
            while (rs.next()){
                prestamo[0] = (rs.getString(1));
                prestamo[1] = (rs.getString(2));  
                prestamo[2] = (rs.getString(3));
                prestamo[3] = (rs.getString(4));  
                prestamo[4] = (rs.getString(5));
             }           
            return prestamo;
        }
         catch (SQLException ex){
             System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                     "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetPrestamoById");
             return null;
        }
        finally{
           desconectar();           
        }
    }
    
    // Consultar un prestamo  por id
    public Object[][] GetPrestamosByCliente(int cliente_id) {
        conectar();
        try{                    
            sentenciaSQL  = "SELECT COUNT(*) FROM PRESTAMO " +
                            "JOIN DETALLES_PRESTAMO USING (PRESTAMO_ID) " +
                            "WHERE CLIENTE_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, cliente_id);
            rs = ps.executeQuery();

            int count = 0;
            while(rs.next()) {
                count = rs.getInt(1);                
            }
            
            Object[][] prestamos = new Object[count][3];
            sentenciaSQL =  "SELECT TITULO, FECHA_PRESTAMO, FECHA_ENTREGA FROM PRESTAMO " +
                            "JOIN DETALLES_PRESTAMO USING (PRESTAMO_ID)" +
                            "JOIN LIBRO USING (LIBRO_ID) " +
                            "WHERE CLIENTE_ID = ? ORDER BY 2";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, cliente_id);
            rs = ps.executeQuery();
            
            int i = 0;
            while(rs.next()) {
                prestamos[i][0] = rs.getString(1);                      // Titulo del libro     
                                 
                DateTimeFormatter from_format = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.A");
                DateTimeFormatter to_format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDateTime fecha_prestamo = LocalDateTime.parse(rs.getString(2), from_format);
                prestamos[i][1] = to_format.format(fecha_prestamo);   // Fecha prestamo
                
                LocalDateTime fecha_entrega = LocalDateTime.parse(rs.getString(3), from_format);
                prestamos[i][2] = to_format.format(fecha_entrega);    // Fecha entrega
                i++;
            }           
            return prestamos;
        }
        catch (SQLException ex ){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetPrestamoById");
            return null;
        }
        finally {
            desconectar();           
        }
    }
    
    // Borrar un prestamo
    public int DeletePrestamo(int prestamo_id){
        conectar(); // Conecta a la base de datos
        try{
            // Borrar cualquier detalle relacionado
            sentenciaSQL =  "DELETE FROM DETALLES_PRESTAMO " +
                            "WHERE PRESTAMO_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, prestamo_id);
            
            // Borrar el prestamo
            sentenciaSQL = "DELETE FROM PRESTAMO " +
                            "WHERE PRESTAMO_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, prestamo_id);
            int res = ps.executeUpdate();
            if (res == 1){
                return 0;
            }
            return 1;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "DeletePrestamo");
            if (ex.getErrorCode() ==  2292)
                return 1;
            return 2;
        }
        finally{
           desconectar();
       }
    }
}
