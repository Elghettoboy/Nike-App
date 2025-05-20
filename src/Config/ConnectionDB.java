package Config; //paquete perteneciente al java package 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

        private final static String URL = "jdbc:postgresql://localhost:5432/nikedb"; //private no cualquiera puede entrar
        private final static String USER = "admin";                                  // final lo vuelve una constante   
        private final static String PASSWORD = "alessandro12";                       // static son atributos propios de la clase

        public static Connection getConn() throws SQLException { //funcion getConn

            try{
                return DriverManager.getConnection(URL, USER, PASSWORD); //se usa otra vez los atributos URL, USER, PASSWORD con la funcion getConnection

            }catch (SQLException e){
                throw new SQLException("Driver JDBC no encontrado", e); // la e es para mandar un mensaje
            }

    }
    public static void closeConnection(Connection connection){ //
        if(connection !=null){
            try{
                connection.close();

            } catch (SQLException e){
                System.out.println("Error al cerrar la conexion: " + e.getMessage());
            }
        }
    }
    }
