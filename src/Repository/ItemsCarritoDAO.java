package Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.ItemsCarrito;

public class ItemsCarritoDAO  extends BaseRepository {
    
    public boolean insertar(ItemsCarrito item) {
        String sql = "insert into tienda_deportiva.items_carrito (carrito_id, producto_id, cantidad) values (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, item.getCarritoId());
                stmt.setInt(2, item.getProductoId());
                stmt.setInt(3, item.getCantidad());
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                System.out.println("Error al insertar item carrito: " + e.getMessage());
                return false;
            }
    }

    public ItemsCarrito obtenerPorId(int carritoId) { 
        String sql = "select * from  tienda_deportiva.items_carrito where carrito_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carritoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ItemsCarrito item = new ItemsCarrito();
                item.setCarritoId(rs.getInt("carrito_id"));
                item.setProductoId(rs.getInt("producto_id"));
                item.setCantidad(rs.getInt("cantidad"));
                return item;
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener item carrito: " + e.getMessage());
        }
        return null;
    }

    public List<ItemsCarrito> obtenerTodos() {
        List<ItemsCarrito> lista = new ArrayList<>();
        String sql = "select * from  tienda_deportiva.items_carrito";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ItemsCarrito item = new ItemsCarrito();
                item.setCarritoId(rs.getInt("carrito_id"));
                item.setProductoId(rs.getInt("producto_id"));
                item.setCantidad(rs.getInt("cantidad"));
                lista.add(item);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar items carrito: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(ItemsCarrito item) {
        String sql = "update tienda_deportiva.items_carrito set carrito_id = ?, producto_id = ?, cantidad = ? where carrito_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getCarritoId());
            stmt.setInt(2, item.getProductoId());
            stmt.setInt(3, item.getCantidad());
            stmt.setInt(4, item.getCarritoId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar item carrito: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int carritoId) {
        String sql = "delete from tienda_deportiva.items_carrito where carrito_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carritoId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar item carrito: " + e.getMessage());
            return false;
        }
    }   

}
