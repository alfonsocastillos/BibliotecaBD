package dataBase.dao;

import dataBase.CustomConnection;
import dataBase.ConfigDataBase;
import java.sql.*;

/**
 * Operaciones a la base de datos en la tabla empleado.
 * @author alfonso
 */
public class EmpleadoDAO extends CustomConnection {
    
    /**
     * Consulta un empleado por su Usuario y contraseña.
     * @param usr el usuario del empleado.
     * @param psw la contraseña del empleado.
     * @return Object[] con los datos del empleado (Id, Sucursal Id, Nombre de la sucursal, Nombre, Apellido Paterno + Apellido Materno).
     */
    public Object[] getEmpleadoByUsr(String usr, char psw[]) {
        connect();
        Object[] employee = null;
        try {
            sentenciaSQL = "SELECT EMPLEADO_ID, SUCURSAL_ID, SUCURSAL.NOMBRE, EMPLEADO.NOMBRE, APELLIDO_PAT || ' ' || APELLIDO_MAT FROM EMPLEADO " +
                "JOIN SUCURSAL USING (SUCURSAL_ID) WHERE USUARIO LIKE ? AND CONTRASENIA LIKE ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, usr);
            preparedStatement.setString(2, String.valueOf(psw));
            resultSet = preparedStatement.executeQuery();
            employee = new Object[5];
            if(resultSet.next()) {
                
                // Recupera los valores
                employee[0] = resultSet.getString(1);
                employee[1] = resultSet.getString(2);
                employee[2] = resultSet.getString(3);
                employee[3] = resultSet.getString(4);
                employee[4] = resultSet.getString(5);
            } else {
                employee[0] = 0;
            }
        } catch(SQLException ex) {
           System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getEmpleadoByUsr");
           employee = null;
        } finally {
           disconnect();
        }
        return employee;
    }
}