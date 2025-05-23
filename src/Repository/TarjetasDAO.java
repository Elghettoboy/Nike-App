package Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.Tarjetas;
import Config.ConnectionDB;

public class TarjetasDAO {
    
    
    public boolean insertar(Tarjetas tarjeta) {
        String sql = "insert into tienda_deportiva.tarjetas (metodo_id, ultimos_digitos, marca, fecha_expiracion, token) values (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionDB.getConn();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tarjeta.getMetodoId());
            stmt.setString(2, tarjeta.getUltimosDigitos());
            stmt.setString(3, tarjeta.getMarca());
            stmt.setString(4, tarjeta.getFechaExpiracion());
            stmt.setString(5, tarjeta.getToken());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar tarjeta: " + e.getMessage());
            return false;
        }
    }

    public Tarjetas obtenerPorId(int tarjetaId) {
        String sql = "select * from tienda_deportiva.tarjetas where tarjeta_id = ?";
        try (Connection conn = ConnectionDB.getConn();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tarjetaId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Tarjetas tarjeta = new Tarjetas();
                tarjeta.setTarjetaId(rs.getInt("tarjeta_id"));
                tarjeta.setMetodoId(rs.getInt("metodo_id"));
                tarjeta.setUltimosDigitos(rs.getString("ultimos_digitos"));
                tarjeta.setMarca(rs.getString("marca"));
                tarjeta.setFechaExpiracion(rs.getString("fecha_expiracion"));       
                tarjeta.setToken(rs.getString("token"));
                return tarjeta;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener tarjeta: " + e.getMessage());
        }
        return null;
    }

    public List<Tarjetas> obtenerTodos() {
        List<Tarjetas> lista = new ArrayList<>();
        String sql = "select * from tienda_deportiva.tarjetas";

        try (Connection conn = ConnectionDB.getConn();
            Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Tarjetas tarjeta = new Tarjetas();
                tarjeta.setTarjetaId(rs.getInt("tarjeta_id"));
                tarjeta.setMetodoId(rs.getInt("metodo_id"));
                tarjeta.setUltimosDigitos(rs.getString("ultimos_digitos"));
                tarjeta.setMarca(rs.getString("marca"));
                tarjeta.setFechaExpiracion(rs.getString("fecha_expiracion")); 
                tarjeta.setToken(rs.getString("token"));
                lista.add(tarjeta);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar tarjetas: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Tarjetas tarjeta) {
        String sql = "update tienda_deportiva.tarjetas set metodo_id = ?, ultimos_digitos = ?, marca = ?, fecha_expiracion = ?, token = ? where tarjeta_id = ?";

        try (Connection conn = ConnectionDB.getConn();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tarjeta.getMetodoId());
            stmt.setString(2, tarjeta.getUltimosDigitos());
            stmt.setString(3, tarjeta.getMarca());
            stmt.setString(4, tarjeta.getFechaExpiracion());
            stmt.setString(5, tarjeta.getToken());
            stmt.setInt(6, tarjeta.getTarjetaId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar tarjeta: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int tarjetaId) {
        String sql = "delete from tienda_deportiva.tarjetas where tarjeta_id = ?";

        try (Connection conn = ConnectionDB.getConn();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tarjetaId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar tarjeta: " + e.getMessage());
            return false;
        }
    }
    public boolean eliminarPorMetodoId(int metodoId) {
    String sql = "DELETE FROM tienda_deportiva.tarjetas WHERE metodo_id = ?";
    try (Connection conn = ConnectionDB.getConn();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, metodoId);
        stmt.executeUpdate();
        return true;
    } catch (SQLException e) {
        System.out.println("Error al eliminar tarjeta por metodo_id: " + e.getMessage());
        return false;
    }
}
public Tarjetas obtenerPorMetodoId(int metodoId) {
    String sql = "SELECT * FROM tienda_deportiva.tarjetas WHERE metodo_id = ?";
    try (Connection conn = ConnectionDB.getConn();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, metodoId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Tarjetas tarjeta = new Tarjetas();
            tarjeta.setTarjetaId(rs.getInt("tarjeta_id"));
            tarjeta.setMetodoId(rs.getInt("metodo_id"));
            tarjeta.setUltimosDigitos(rs.getString("ultimos_digitos"));
            tarjeta.setMarca(rs.getString("marca"));
            tarjeta.setFechaExpiracion(rs.getString("fecha_expiracion"));
            tarjeta.setToken(rs.getString("token"));
            return tarjeta;
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener tarjeta por metodo_id: " + e.getMessage());
    }
    return null;
}

public boolean actualizarPorMetodoId(Tarjetas tarjeta) {
    String sql = "UPDATE tienda_deportiva.tarjetas SET ultimos_digitos = ?, marca = ?, fecha_expiracion = ?, token = ? WHERE metodo_id = ?";
    try (Connection conn = ConnectionDB.getConn();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, tarjeta.getUltimosDigitos());
        stmt.setString(2, tarjeta.getMarca());
        stmt.setString(3, tarjeta.getFechaExpiracion());
        stmt.setString(4, tarjeta.getToken());
        stmt.setInt(5, tarjeta.getMetodoId());
        stmt.executeUpdate();
        return true;
    } catch (SQLException e) {
        System.out.println("Error al actualizar tarjeta por metodo_id: " + e.getMessage());
        return false;
    }
}

    
}
