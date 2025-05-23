package Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.Pago;
import Config.ConnectionDB;

public class PagoDAO {


    public boolean insertar(Pago pago) {
        String sql = "insert into tienda_deportiva.pagos (usuario_id, metodo_id, monto, fecha_pago, estado) values (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pago.getUsuarioId());
            stmt.setInt(2, pago.getMetodoId());
            stmt.setDouble(3, pago.getMonto());
            stmt.setTimestamp(4, pago.getFechaPago());
            stmt.setString(5, pago.getEstado());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar pago: " + e.getMessage());
            return false;
        }
    }

    public Pago obtenerPorId(int pagoId) {
        String sql = "select * from tienda_deportiva.pagos where pago_id = ?";
        try (Connection conn = ConnectionDB.getConn();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pagoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Pago pago = new Pago();
                pago.setPagoId(rs.getInt("pago_id"));
                pago.setUsuarioId(rs.getInt("usuario_id"));
                pago.setMetodoId(rs.getInt("metodo_id"));
                pago.setMonto(rs.getDouble("monto"));
                pago.setFechaPago(rs.getTimestamp("fecha_pago"));
                pago.setEstado(rs.getString("estado"));
                return pago;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener pago: " + e.getMessage());
        }
        return null;
    }

    public List<Pago> obtenerTodos() {
        List<Pago> lista = new ArrayList<>();
        String sql = "select * from tienda_deportiva.pagos";
        try (Connection conn = ConnectionDB.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pago pago = new Pago();
                pago.setPagoId(rs.getInt("pago_id"));
                pago.setUsuarioId(rs.getInt("usuario_id"));
                pago.setMetodoId(rs.getInt("metodo_id"));
                pago.setMonto(rs.getDouble("monto"));
                pago.setFechaPago(rs.getTimestamp("fecha_pago"));
                pago.setEstado(rs.getString("estado"));
                lista.add(pago);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar pagos: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Pago pago) {
        String sql = "update tienda_deportiva.pagos set usuario_id = ?, metodo_id = ?, monto = ?, fecha_pago = ?, estado = ? where pago_id = ?";
        try (Connection conn = ConnectionDB.getConn();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pago.getUsuarioId());
            stmt.setInt(2, pago.getMetodoId());
            stmt.setDouble(3, pago.getMonto());
            stmt.setTimestamp(4, pago.getFechaPago());
            stmt.setString(5, pago.getEstado());
            stmt.setInt(6, pago.getPagoId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar pago: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int pagoId) {
        String sql = "delete from tienda_deportiva.pagos where pago_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pagoId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar pago: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarPorUsuarioId(int usuarioId) {
        String sql = "DELETE FROM tienda_deportiva.pagos WHERE usuario_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) { 
            
            if (conn == null) { 
                System.out.println("Error: No se pudo obtener conexión de ConnectionDB.");
                return false;
            }

            stmt.setInt(1, usuarioId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar pagos por usuario_id: " + e.getMessage());
            return false;
        }
      
    }
}