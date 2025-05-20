import UI.MenuLogin;
import Config.ConnectionDB;

import java.sql.Connection;
import java.sql.SQLException;


public class App {

    public static void main(String[] args) {
        try {
            // Intentar conectar a la base de datos
            Connection conn = ConnectionDB.getConn();
            System.out.println("Conexión exitosa a la base de datos.");

            // Cerrar la conexión inicial (ya se reutiliza dentro de DAOs)
            ConnectionDB.closeConnection(conn);

            // Mostrar el menú login
            MenuLogin.mostrarMenuLogin();

        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos:");
            System.out.println(e.getMessage());
        }
    }
}

