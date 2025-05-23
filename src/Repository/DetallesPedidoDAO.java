package Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.DetallesPedido;
import Config.ConnectionDB;

public class DetallesPedidoDAO {

    public DetallesPedidoDAO() throws SQLException {
    }

    public boolean insertar(DetallesPedido detalle, Connection conn) throws SQLException {
        String sql = "insert into tienda_deportiva.detalles_pedido (pedido_id, producto_id, cantidad, precio) values (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, detalle.getPedidoId());
            stmt.setInt(2, detalle.getProductoId());
            stmt.setInt(3, detalle.getCantidad());
            stmt.setDouble(4, detalle.getPrecio());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        detalle.setDetalleId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    public boolean insertar(DetallesPedido detalle) {
        try (Connection conn = ConnectionDB.getConn()) {
            return insertar(detalle, conn);
        } catch (SQLException e) {
            System.out.println("Error al insertar detalle pedido (auto-conexión): " + e.getMessage());
            return false;
        }
    }
    
    public DetallesPedido obtenerPorId(int detalleId) {
        String sql = "select * from tienda_deportiva.detalles_pedido where detalle_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detalleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                DetallesPedido detalle = new DetallesPedido();
                detalle.setDetalleId(rs.getInt("detalle_id"));
                detalle.setPedidoId(rs.getInt("pedido_id"));
                detalle.setProductoId(rs.getInt("producto_id"));
                detalle.setCantidad(rs.getInt("cantidad"));
                detalle.setPrecio(rs.getDouble("precio"));
                return detalle;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener detalle pedido: " + e.getMessage());
        }
        return null;
    }

    public List<DetallesPedido> obtenerTodos() {
        List<DetallesPedido> lista = new ArrayList<>();
        String sql = "select * from tienda_deportiva.detalles_pedido";
        try (Connection conn = ConnectionDB.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                DetallesPedido detalle = new DetallesPedido();
                detalle.setDetalleId(rs.getInt("detalle_id"));
                detalle.setPedidoId(rs.getInt("pedido_id"));
                detalle.setProductoId(rs.getInt("producto_id"));
                detalle.setCantidad(rs.getInt("cantidad"));
                detalle.setPrecio(rs.getDouble("precio"));
                lista.add(detalle);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar detalles pedido: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(DetallesPedido detalle, Connection conn) throws SQLException {
        String sql = "update tienda_deportiva.detalles_pedido set pedido_id = ?, producto_id = ?, cantidad = ?, precio = ? where detalle_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detalle.getPedidoId());
            stmt.setInt(2, detalle.getProductoId());
            stmt.setInt(3, detalle.getCantidad());
            stmt.setDouble(4, detalle.getPrecio());
            stmt.setInt(5, detalle.getDetalleId());
            stmt.executeUpdate();
            return true;
        }
    }
    
    public boolean actualizar(DetallesPedido detalle) {
         try (Connection conn = ConnectionDB.getConn()) {
            return actualizar(detalle, conn);
        } catch (SQLException e) {
            System.out.println("Error al actualizar detalle pedido (auto-conexión): " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int detalleId) {
        String sql = "delete from tienda_deportiva.detalles_pedido where detalle_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detalleId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar detalle pedido: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarPorPedidoId(int pedidoId) {
        String sql = "DELETE FROM tienda_deportiva.detalles_pedido WHERE pedido_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar detalles_pedido por pedido_id: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarPorProductoId(int productoId) {
        String sql = "DELETE FROM tienda_deportiva.detalles_pedido WHERE producto_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productoId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar detalles_pedido por producto_id: " + e.getMessage());
            return false;
        }
    }
}