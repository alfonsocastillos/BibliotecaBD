/*
 * Clase encargada de consultar, insertar, eliminar y modificar la tabla Pais
 */
package dataBase.dao;

import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;

/**
 *
 * @author alfonso
 */

public class CredencialDAO extends Conexion {
    
    // Crear una Credencial
    // Modificar el método SaveCredencial para aceptar la fecha como parámetro
    public int SaveCredencial(String fechaRenovacion) {
        // Se conecta a la base de datos
        conectar();
        try {
            int id = 0;
            // genera el nuevo id            
            sentenciaSQL =  "SELECT MAX(CREDENCIAL_ID) + 1 FROM CREDENCIAL";
            ps = conn.prepareStatement(sentenciaSQL);            
            rs = ps.executeQuery();

            // guarda el nuevo id
            if (rs.next()){
                id = rs.getInt(1);
            }

            // inserta mandando todos los datos, incluido el nuevo id y la fecha proporcionada
            sentenciaSQL = "INSERT INTO CREDENCIAL VALUES(?, TO_DATE(?, 'DD/MM/YY'))";
            ps = conn.prepareStatement(sentenciaSQL);
            // Asigna los valores del arreglo            
            ps.setInt(1, id);              // Id (calculado)
            ps.setString(2, fechaRenovacion);  // Fecha de Renovación
            ps.executeUpdate();
            return id;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "SaveCredencial");
            return 0;
        }
        finally{
           desconectar();
        }
    }                        
}

