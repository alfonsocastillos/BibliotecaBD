/*
   Operaciones a la base de datos en la tabla cliente
*/
package dataBase.dao;

import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Arrays;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import dataBase.dao.DireccionDAO;
import dataBase.dao.CredencialDAO;

public class ClienteDAO extends Conexion {
     
    public Object [][] getCustomers(){
        // se conecta a la base de datos
        conectar();
        // crea un arreglo de beans para guardar los datos
        Object[][] customers; 
        // Contador
        int i = 0;
        int count = 0;
        
        try{
            sentenciaSQL = "SELECT COUNT(*) FROM CLIENTE";
            ps = conn.prepareStatement(sentenciaSQL);
            rs = ps.executeQuery();

            if (rs.next()){
                count = rs.getInt(1);
            }

            customers = new Object[count][4];
            sentenciaSQL  = "SELECT cliente_id, nombre, apellido_pat, apellido_mat FROM CLIENTE";           
            ps = conn.prepareStatement(sentenciaSQL);
            rs = ps.executeQuery();
            
            // Recorre el result set para obtener los datos y asignarlos
            // al arreglo de beans
            while (rs.next()){
                customers[i][0]=(rs.getString(1));
                customers[i][1]=(rs.getString(2));  
                customers[i][2]=(rs.getString(3)); 
                customers[i][3]=(rs.getString(4));
                i++;
            }      
            return customers;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getCustomers");
            return null;
        }
        finally{
            desconectar();           
        }
    }
    
    public Object [] getClienteById(int cliente_id){
        // se conecta a la base de datos
        conectar();
        // crea un arreglo de beans para guardar los datos
        Object[] customer = new Object[13];
        
        try{
            sentenciaSQL  = "SELECT CLIENTE_ID, NOMBRE, APELLIDO_PAT, APELLIDO_MAT, CORREO, PAIS, ESTADO, ALCALDIA, CP, CALLE, EXTERIOR, INTERIOR, NIVEL " +
                            "FROM CLIENTE " +
                            "JOIN DIRECCION USING (DIRECCION_ID) " +
                            "JOIN ESTADO USING (ESTADO_ID) " +
                            "JOIN PAIS USING (PAIS_ID) " +
                            "JOIN ESCOLARIDAD USING(ESCOLARIDAD_ID) " +
                            "WHERE CLIENTE_ID = ? ";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, cliente_id);
            rs = ps.executeQuery();
            
            // Recorre el result set para obtener los datos y asignarlos
            // al arreglo de beans
            while (rs.next()){
                customer[0] = rs.getInt(1);         // Id
                customer[1] = rs.getString(2);      // Nombre
                customer[2] = rs.getString(3);      // Apellido Paterno
                customer[3] = rs.getString(4);      // Apellido Materno
                customer[4] = rs.getString(5);      // Correo
                customer[5] = rs.getString(6);      // Pais
                customer[6] = rs.getString(7);      // Estado
                customer[7] = rs.getString(8);      // Alcaldia
                customer[8] = rs.getString(9);      // CP
                customer[9] = rs.getString(10);     // Calle
                customer[10] = rs.getString(11);    // Exterior
                customer[11] = rs.getString(12);    // Interior
                customer[12] = rs.getString(13);    // Escolaridad
            }      
            return customer;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getClienteById");
            return null;
        }
        finally{
            desconectar();           
        }
    }

            
    // Método para guardar un usuario
    public String saveCliente(Object[] cliente) {
    conectar();
        try {            
            sentenciaSQL = "SELECT MAX(CLIENTE_ID) FROM CLIENTE";
            ps = conn.prepareStatement(sentenciaSQL);
            rs = ps.executeQuery();
            String cliente_id = "";
            
            if (rs.next()){
               cliente_id = Integer.toString(rs.getInt(1) + 1);
            } 
            
            // Crear registro direccion para el cliente
            DireccionDAO dir_dao = new DireccionDAO();
            Object[] dir = {
                cliente[4], // Alcaldia
                cliente[5], // CP
                cliente[6], // Calle
                cliente[7], // Exterior
                cliente[8], // Interior
                cliente[9]  // Estado ID
            };
            String dir_id = dir_dao.SaveDireccion(dir);
            
            LocalDateTime ldt = LocalDateTime.now().plusYears(1);
            DateTimeFormatter date_format = DateTimeFormatter.ofPattern("dd/MM/yyyy");                
            String next_year = date_format.format(ldt);            

            CredencialDAO cred_dao = new CredencialDAO();
            int cred_id = cred_dao.SaveCredencial(next_year);
            
            // Resto del código para insertar el usuario en CLIENTE
            sentenciaSQL = "INSERT INTO CLIENTE VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sentenciaSQL);

            ps.setBigDecimal(1, new BigDecimal(cliente_id));                // Id (calculado)
            ps.setString(2, cliente[0].toString());                         // Nombre
            ps.setString(3, cliente[1].toString());                         // Apellido Paterno
            ps.setString(4, cliente[2].toString());                         // Apellido Materno
            ps.setString(5, cliente[3].toString());                         // Correo
            ps.setString(6, dir_id);                                        // Id de dirección (calculado)
            ps.setBigDecimal(7, new BigDecimal(cliente[10].toString()));    // Id de escolaridad 
            ps.setBigDecimal(8, new BigDecimal(cred_id));                   // Id de credencial

            ps.executeUpdate();
            
            return cliente_id;  // Regresa el nuevo ID de cliente
        } catch (SQLException ex) {
            // Manejo de excepciones...
            ex.printStackTrace();
            System.err.println("Error al insertar usuario: " + ex.getMessage());
            return null;
        } finally {
            desconectar();
        }
    }

    // Método para actualizar un usuario
    public String updateUsuario(Object[] usuario) {
        conectar();
        try {
            String sentenciaSQL = "UPDATE CLIENTE SET " +
                    "NOMBRE = ?, " +
                    "APELLIDO_PAT = ?, " +
                    "APELLIDO_MAT = ?, " +
                    "CORREO = ?, " +
                    "DIRECCION_ID = ?, " +
                    "ESCOLARIDAD_ID = ?, " +
                    "CREDENCIAL_ID = ? " +
                    "WHERE CLIENTE_ID = ?";
            PreparedStatement ps = conn.prepareStatement(sentenciaSQL);

            ps.setString(1, usuario[0].toString());  // Nuevo nombre
            ps.setString(2, usuario[1].toString());  // Nuevo apellido paterno
            ps.setString(3, usuario[2].toString());  // Nuevo apellido materno
            ps.setString(4, usuario[3].toString());  // Nuevo correo
            ps.setString(5, usuario[4].toString());  // Nuevo id de dirección
            ps.setString(6, usuario[5].toString());  // Nuevo id de escolaridad
            ps.setString(7, usuario[6].toString());  // Nuevo id de credencial
            ps.setString(8, usuario[7].toString());  // Id del usuario a modificar

            ps.executeUpdate();
            return usuario[7].toString();  // Regresa el id del usuario
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getSQLState() + "\n\n" + ex.getMessage() +
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "updateUsuario");
            return null;
        } finally {
            desconectar();
        }
    }

    // Método para consultar todas las direcciones de un usuario por estado
    public Object[][] getAllDireccionesByUsuario(String usuario_id) {
        conectar();
        Object[][] dirs;
        int i = 0;
        int count = 0;
        try {
            String sentenciaSQL = "SELECT COUNT(*) FROM CLIENTE WHERE CLIENTE_ID = ?";
            PreparedStatement ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, usuario_id);
            ResultSet rs = ps.executeQuery();

            // Verificar si el usuario existe
            if (rs.next()) {
                count = 1;  // El usuario existe, por lo que solo habrá una dirección
            }

            dirs = new Object[count][8];
            sentenciaSQL = "SELECT CLIENTE_ID, NOMBRE, APELLIDO_PAT, APELLIDO_MAT, CORREO, DIRECCION_ID, ESCOLARIDAD_ID " +
               "FROM CLIENTE WHERE CLIENTE_ID = ?";
            
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, usuario_id);
            rs = ps.executeQuery();

            while (rs.next()) {
                dirs[i][0] = rs.getString(1);  // Id del usuario
                dirs[i][1] = rs.getString(2);  // Nombre
                dirs[i][2] = rs.getString(3);  // Apellido Paterno
                dirs[i][3] = rs.getString(4);  // Apellido Materno
                dirs[i][4] = rs.getString(5);  // Correo
                dirs[i][5] = rs.getString(6);  // Id de dirección
                dirs[i][6] = rs.getString(7);  // Id de escolaridad
                dirs[i][7] = rs.getString(8);  // Id de credencial
                i++;
            }
            return dirs;
        } catch (SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY +
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getAllDireccionesByUsuario");
            return null;
        } finally {
            desconectar();
        }
    }
    
    // Regresa todos los clientes que coincidan con el filtro
    public Object [][] GetClientesByFilter(String filtro){
        // se conecta a la base de datos
        conectar();
        // crea un arreglo de beans para guardar los datos
        Object [][] clientes;
        // Contador
        int i = 0;
        // Para guardar la cantidad de registros
        int count = 0;
        filtro = "%" + filtro + "%";
        try{
            // Cuenta los registros en la base de datos
            sentenciaSQL =  "SELECT COUNT (CLIENTE_ID) FROM (" +
                                "SELECT CLIENTE_ID " +
                                "FROM CLIENTE " +
                                "WHERE UPPER(NOMBRE) LIKE UPPER(?) OR " +
                                "UPPER(APELLIDO_PAT) LIKE UPPER(?) OR " +
                                "UPPER(APELLIDO_MAT) LIKE UPPER(?) OR " +
                                "UPPER(CORREO) LIKE UPPER(?) OR " +
                                "CREDENCIAL_ID LIKE ?)";
                        
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, filtro);
            ps.setString(2, filtro);
            ps.setString(3, filtro);
            ps.setString(4, filtro);
            ps.setString(5, filtro);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
            // Consulta los registros 
            clientes = new Object[count][6];          
            // Cuenta los registros en la base de datos
            sentenciaSQL =  "SELECT CLIENTE_ID, NOMBRE, APELLIDO_PAT, APELLIDO_MAT, CORREO, CREDENCIAL_ID " +
                            "FROM CLIENTE " +
                            "WHERE UPPER(NOMBRE) LIKE UPPER(?) OR " +
                            "UPPER(APELLIDO_PAT) LIKE UPPER(?) OR " +
                            "UPPER(APELLIDO_MAT) LIKE UPPER(?) OR " +
                            "UPPER(CORREO) LIKE UPPER(?) OR " +
                            "CREDENCIAL_ID LIKE ? ORDER BY NOMBRE";
                        
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, filtro);
            ps.setString(2, filtro);
            ps.setString(3, filtro);
            ps.setString(4, filtro);
            ps.setString(5, filtro);
            rs = ps.executeQuery();
            // Recorre el result set para obtener los datos y asignarlos al arreglo de beans
            while (rs.next()) {
                clientes[i][0] = rs.getInt(1);                    // Id del cliente
                clientes[i][1] = rs.getString(2);                 // Nombre del cliente
                clientes[i][2] = rs.getString(3);                 // Apellido Paterno del cliente
                clientes[i][3] = rs.getString(4);                 // Apellido Materno del cliente
                clientes[i][4] = rs.getString(5);                 // Correo del cliente                    
                clientes[i][5] = rs.getInt(6);                    // Credencial del cliente               
                i++;
            }           
            return clientes;
        }
        catch (SQLException ex) {
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetClientesByFilter");
            return null;
        }
        finally{
           desconectar();           
        }
    }
    
    // Eliminar un estado
    public int DeleteCliente(int cliente_id){
        // Conecta a la base de datos
        conectar();
        try{
            // Buscar la direccion asociada con este cliente            
            sentenciaSQL =  "SELECT DIRECCION_ID FROM CLIENTE " +
                            "WHERE CLIENTE_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, cliente_id);
            rs = ps.executeQuery();
            
            String dir_id = "";
            if(rs.next()) {
                dir_id = rs.getString(1);
            }            
                                    
            // Borrar el cliente
            sentenciaSQL = "DELETE FROM CLIENTE " +
                            "WHERE CLIENTE_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, cliente_id);
            int res = ps.executeUpdate();
            
            // Borrar la direccion asociada con el cliente
            sentenciaSQL =  "DELETE FROM DIRECCION " +                            
                            "WHERE DIRECCION_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, dir_id);
            ps.executeUpdate();

            if (res == 1){
                return 0;
            }
            
            return 1;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "DeleteCliente");
            if (ex.getErrorCode() ==  2292)
                return 1;
            return 2;
        }
        finally{
           desconectar();
       }
    }
}