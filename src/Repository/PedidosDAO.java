package Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import Config.ConnectionDB;
import Models.DetallesPedido;
import Models.Pedidos;

public class PedidosDAO {

    private DetallesPedidoDAO detallesPedidoDAO;

    public PedidosDAO() throws SQLException {
        this.detallesPedidoDAO = new DetallesPedidoDAO();
    }

    public boolean insertar(Pedidos pedido, Connection conn) throws SQLException {
        String sql = "INSERT INTO tienda_deportiva.pedidos (usuario_id, fecha_pedido, estado) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, pedido.getUsuarioId());
            stmt.setTimestamp(2, pedido.getFechaPedido());
            stmt.setString(3, pedido.getEstado());
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pedido.setPedidoId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean insertar(Pedidos pedido) {
        try (Connection conn = ConnectionDB.getConn()) {
            return insertar(pedido, conn);
        } catch (SQLException e) {
            System.out.println("Error al insertar pedido (auto-conexión): " + e.getMessage());
            return false;
        }
    }

    public Pedidos obtenerPorId(int pedidoId) {
        String sql = "select * from tienda_deportiva.pedidos where pedido_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Pedidos pedido = new Pedidos();
                pedido.setPedidoId(rs.getInt("pedido_id"));
                pedido.setUsuarioId(rs.getInt("usuario_id"));
                pedido.setFechaPedido(rs.getTimestamp("fecha_pedido"));
                pedido.setEstado(rs.getString("estado"));
                return pedido;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener pedido: " + e.getMessage());
        }
        return null;
    }

    public List<Pedidos> obtenerTodos() {
        List<Pedidos> lista = new ArrayList<>();
        String sql = "select * from tienda_deportiva.pedidos";
        try (Connection conn = ConnectionDB.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pedidos pedido = new Pedidos();
                pedido.setPedidoId(rs.getInt("pedido_id"));
                pedido.setUsuarioId(rs.getInt("usuario_id"));
                pedido.setFechaPedido(rs.getTimestamp("fecha_pedido"));
                pedido.setEstado(rs.getString("estado"));
                lista.add(pedido);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar pedidos: " + e.getMessage());
        }
        return lista;
    }

    public List<Pedidos> obtenerTodosPorUsuarioId(int usuarioId) {
        List<Pedidos> lista = new ArrayList<>();
        String sql = "SELECT * FROM tienda_deportiva.pedidos WHERE usuario_id = ? ORDER BY fecha_pedido DESC";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pedidos pedido = new Pedidos();
                    pedido.setPedidoId(rs.getInt("pedido_id"));
                    pedido.setUsuarioId(rs.getInt("usuario_id"));
                    pedido.setFechaPedido(rs.getTimestamp("fecha_pedido"));
                    pedido.setEstado(rs.getString("estado"));
                    lista.add(pedido);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener pedidos por usuario_id: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Pedidos pedido, Connection conn) throws SQLException {
        String sql = "update tienda_deportiva.pedidos set usuario_id = ?, fecha_pedido = ?, estado = ? where pedido_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedido.getUsuarioId());
            stmt.setTimestamp(2, pedido.getFechaPedido());
            stmt.setString(3, pedido.getEstado());
            stmt.setInt(4, pedido.getPedidoId());
            stmt.executeUpdate();
            return true;
        }
    }

    public boolean actualizar(Pedidos pedido) {
        try (Connection conn = ConnectionDB.getConn()) {
            return actualizar(pedido, conn);
        } catch (SQLException e) {
            System.out.println("Error al actualizar pedido (auto-conexión): " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int pedidoId) {
        boolean detallesEliminados = detallesPedidoDAO.eliminarPorPedidoId(pedidoId);
        if (!detallesEliminados) {
            System.out.println("Fallo al eliminar detalles para el pedido_id: " + pedidoId + ". La eliminación del pedido se detendrá.");
            return false;
        }

        String sql = "delete from tienda_deportiva.pedidos where pedido_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar pedido con id " + pedidoId + ": " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarPorUsuarioId(int usuarioId) {
        List<Pedidos> pedidosDelUsuario = obtenerTodosPorUsuarioId(usuarioId);
        for (Pedidos pedido : pedidosDelUsuario) {
            boolean detallesEliminados = detallesPedidoDAO.eliminarPorPedidoId(pedido.getPedidoId());
            if (!detallesEliminados) {
                System.out.println("Fallo al eliminar detalles para el pedido_id: " + pedido.getPedidoId() + ". La eliminación de pedidos del usuario se detendrá.");
                return false;
            }
        }
        String sqlDeletePedidos = "DELETE FROM tienda_deportiva.pedidos WHERE usuario_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sqlDeletePedidos)) {
            stmt.setInt(1, usuarioId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar pedidos (después de borrar detalles) por usuario_id: " + e.getMessage());
            return false;
        }
    }
    public List<Pedidos> obtenerPedidosConDetalles() {
    List<Pedidos> pedidosConDetalles = new ArrayList<>();
    String sql = """
        SELECT p.pedido_id, p.usuario_id, p.fecha_pedido, p.estado,
               dp.detalle_id, dp.producto_id, dp.cantidad, dp.precio
        FROM tienda_deportiva.pedidos p
        INNER JOIN tienda_deportiva.detalles_pedido dp ON p.pedido_id = dp.pedido_id
        ORDER BY p.pedido_id
        """;
    try (Connection conn = ConnectionDB.getConn();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        Map<Integer, Pedidos> pedidosMap = new LinkedHashMap<>();
        while (rs.next()) {
            int pedidoId = rs.getInt("pedido_id");
            Pedidos pedido = pedidosMap.get(pedidoId);
            if (pedido == null) {
                pedido = new Pedidos();
                pedido.setPedidoId(pedidoId);
                pedido.setUsuarioId(rs.getInt("usuario_id"));
                pedido.setFechaPedido(rs.getTimestamp("fecha_pedido"));
                pedido.setEstado(rs.getString("estado"));
                // Asumiendo que tu modelo Pedidos tiene setDetalles y getDetalles
                if (pedido.getDetalles() == null) { // Asegurar que la lista de detalles está inicializada
                    pedido.setDetalles(new ArrayList<>());
                }
                pedidosMap.put(pedidoId, pedido);
            }
            DetallesPedido detalle = new DetallesPedido();
            detalle.setDetalleId(rs.getInt("detalle_id"));
            detalle.setPedidoId(pedidoId);
            detalle.setProductoId(rs.getInt("producto_id"));
            detalle.setCantidad(rs.getInt("cantidad"));
            detalle.setPrecio(rs.getDouble("precio"));
            pedido.getDetalles().add(detalle);
        }
        pedidosConDetalles.addAll(pedidosMap.values());
    } catch (SQLException e) {
        System.out.println("Error al obtener pedidos con detalles: " + e.getMessage());
    }
    return pedidosConDetalles;
}
}