package dataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Clase encargada de la conexion y desconexion con la Base de datos.
 * @author Carlos Cortés Bazán
 */
public class CustomConnection {
    
    // Statement -> PreparedStatement -> CallableStatement.
    public Connection connection = null;        // Sesion con la BD.
    public ArrayList<Statement> statements;     // Arreglo de instrucciones SQL.
    public Statement statement;                 // Una sola instruccion SQL.
    public String sentenciaSQL;                 // Instruccion SQL en forma string.
    public ResultSet resultSet;                 // Tabla resultante de una consulta.
    public PreparedStatement preparedStatement; // Instruccion de SQL pre-compilada.
    public CallableStatement callableStatement; // Llama un proceso almacenado.
    
    /**
     * Realiza la conexion con la Base de Datos.
     */
    public void connect() {
        
        // Lee los controladores.
        loadDriver();
        connection = null;
        
        // Lista de sentencias.
        statements = new ArrayList();
        try {
            
            // Propiedades de la conexión.
            Properties props = new Properties();
            props.put("user", ConfigDataBase.JDBC_USER);
            props.put("password", ConfigDataBase.JDBC_PSW);
            
            // Obtiene la conexión usando la URL de la BD y las propiedades para conectarse.
            connection = DriverManager.getConnection(ConfigDataBase.JDBC_PROTOCOL, props);
            
            // Ejecuta las sentencias y guarda los resultados en la BD.
            connection.setAutoCommit(true);   
            
            // Crea la sentencia cuyos resultados son iterables (SCROLL_SENSITIVE) y alterables (UPDATABLE).
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statements.add(statement);
        } catch(SQLException sqle) {
            System.out.println(ConfigDataBase.DB_T_ERROR +  sqle.getSQLState() + ConfigDataBase.DB_NOCONECT);
            statement = null;
        }
    }
    
    /**
     * Desconecta la base de datos.
     */
    public void disconnect() {
        if(statements != null) {
            statements.clear();   
        }            
        try {
            if(statement != null) {
                statement.close();
            }                
            if(connection != null) {
                connection.close();
            }                
        } catch(SQLException sqle) {
            System.out.println(ConfigDataBase.DB_T_ERROR + sqle.getSQLState() + ConfigDataBase.DB_CLOSECON_ERR + " " + sqle.getMessage());
        }
    }   
    
    /**
     * Lee los controladores de Oracle.
     */
    private void loadDriver() {
        try {
            
            // Busca los drivers para Oracle y crea una instancia de su clase.
            Class.forName(ConfigDataBase.JDBC_DRIVER_ORACLE).newInstance();
        } catch(ClassNotFoundException cnfe) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ConfigDataBase.DB_DRIVERNO_REC);
        } catch(InstantiationException ie) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ConfigDataBase.DB_NOINST_DRIVER);
        } catch(IllegalAccessException iae) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ConfigDataBase.DB_NOACCESS_DRIVER);
        }
    }       
}