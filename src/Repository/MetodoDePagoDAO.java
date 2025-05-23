package Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.MetodoDePago;
import Config.ConnectionDB;

public class MetodoDePagoDAO {

    private Connection conn;

    public MetodoDePagoDAO() throws SQLException {
        this.conn = ConnectionDB.getConn();
    }

    public boolean insertar(MetodoDePago metodo) {
        String sql = "insert into tienda_deportiva.metodos_pago (usuario_id, tipo, activo) values (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, metodo.getUsuarioId());
            stmt.setString(2, metodo.getTipo());
            stmt.setBoolean(3, metodo.isActivo());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar metodo de pago: " + e.getMessage());
            return false;
        }
    }

    public MetodoDePago obtenerPorId(int metodoId) {
        String sql = "select * from tienda_deportiva.metodos_pago where metodo_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, metodoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                MetodoDePago metodo = new MetodoDePago();
                metodo.setMetodoId(rs.getInt("metodo_id"));
                metodo.setUsuarioId(rs.getInt("usuario_id"));
                metodo.setTipo(rs.getString("tipo"));
                metodo.setActivo(rs.getBoolean("activo"));
                return metodo;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener metodo de pago: " + e.getMessage());
        }
        return null;
    }

    public List<MetodoDePago> obtenerTodos() {
        List<MetodoDePago> lista = new ArrayList<>();
        String sql = "select * from tienda_deportiva.metodos_pago";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                MetodoDePago metodo = new MetodoDePago();
                metodo.setMetodoId(rs.getInt("metodo_id"));
                metodo.setUsuarioId(rs.getInt("usuario_id"));
                metodo.setTipo(rs.getString("tipo"));
                metodo.setActivo(rs.getBoolean("activo"));
                lista.add(metodo);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar metodos de pago: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(MetodoDePago metodo) {
        String sql = "update tienda_deportiva.metodos_pago set usuario_id = ?, tipo = ?, activo = ? where metodo_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, metodo.getUsuarioId());
            stmt.setString(2, metodo.getTipo());
            stmt.setBoolean(3, metodo.isActivo());
            stmt.setInt(4, metodo.getMetodoId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar metodo de pago: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int metodoId) {
        String sql = "delete from tienda_deportiva.metodos_pago where metodo_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, metodoId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar metodo de pago: " + e.getMessage());
            return false;
        }
    }

    public List<MetodoDePago> obtenerPorUsuarioId(int usuarioId) {
        List<MetodoDePago> lista = new ArrayList<>();
        String sql = "select * from tienda_deportiva.metodos_pago where usuario_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MetodoDePago metodo = new MetodoDePago();
                metodo.setMetodoId(rs.getInt("metodo_id"));
                metodo.setUsuarioId(rs.getInt("usuario_id"));
                metodo.setTipo(rs.getString("tipo"));
                metodo.setActivo(rs.getBoolean("activo"));
                lista.add(metodo);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener metodos de pago por usuarioId: " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminarPorUsuarioId(int usuarioId) {
        String sql = "delete from tienda_deportiva.metodos_pago where usuario_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar métodos de pago por usuarioId: " + e.getMessage());
            return false;
        }
    }
}