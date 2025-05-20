package Repository;

import java.sql.Connection;
import java.sql.SQLException;

import Config.ConnectionDB;

public abstract class BaseRepository {
protected Connection conn;

public BaseRepository() {
    try {
        conn = ConnectionDB.getConn();
    } catch (Exception e) {
        throw new RuntimeException("Falla en la conexión", e);
    }
}

public void close() {
    if (conn != null) {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}

}


