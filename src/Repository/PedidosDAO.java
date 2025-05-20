package Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.Pedidos;

public class PedidosDAO extends BaseRepository {

    public boolean insertar(Pedidos pedido) {
        String sql = "insert into tienda_deportiva.pedidos (usuario_id, fecha_pedido, estado) values (?, ?, ?)";    
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, pedido.getUsuarioId());
                stmt.setTimestamp(2, pedido.getFechaPedido());
                stmt.setString(3, pedido.getEstado());
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                System.out.println("Error al insertar pedido: " + e.getMessage());
                return false;
            }
    }

    public Pedidos obtenerPorId(int pedidoId) { 
        String sql = "select * from  tienda_deportiva.pedidos where pedido_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        String sql = "select * from  tienda_deportiva.pedidos";

        try (Statement stmt = conn.createStatement();
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

    public boolean actualizar(Pedidos pedido) {
        String sql = "update tienda_deportiva.pedidos set usuario_id = ?, fecha_pedido = ?, estado = ? where pedido_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedido.getUsuarioId());
            stmt.setTimestamp(2, pedido.getFechaPedido());
            stmt.setString(3, pedido.getEstado());
            stmt.setInt(4, pedido.getPedidoId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar pedido: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int pedidoId) {
        String sql = "delete from tienda_deportiva.pedidos where pedido_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar pedido: " + e.getMessage());
            return false;
        }
    }   


}
