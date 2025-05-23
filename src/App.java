import Config.ConnectionDB;
import UI.MenuLogin;

import java.sql.Connection;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        try {
            // Verificar conexión a la base de datos (opcional, solo para testeo inicial)
            Connection conn = ConnectionDB.getConn();
            System.out.println("Conexión exitosa a la base de datos.");
            ConnectionDB.closeConnection(conn); // se cierra porque DAOs usarán su propia conexión

            // Iniciar el sistema mostrando el menú de login
            MenuLogin.mostrarMenuLogin();

        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos:");
            System.out.println(e.getMessage());
        }
    }
}
