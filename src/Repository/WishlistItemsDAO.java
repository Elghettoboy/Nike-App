package Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.WishlistItems;
import Config.ConnectionDB;

public class WishlistItemsDAO {

    public WishlistItemsDAO() throws SQLException {
        // Constructor puede quedar vacío si todos los métodos usan try-with-resources para ConnectionDB.getConn()
        // o si vas a pasar una conexión a los métodos para transacciones.
        // Si mantienes 'private Connection conn;' inicializado aquí, asegúrate de su gestión.
        // Por consistencia con los DAOs refactorizados, quitamos 'this.conn'.
    }

    public boolean insertar(WishlistItems item) {
        String sql = "insert into tienda_deportiva.wishlist_items (wishlist_id, producto_id) values (?, ?)";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getWishlistId());
            stmt.setInt(2, item.getProductoId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar wishlist item: " + e.getMessage());
            return false;
        }
    }

    public WishlistItems obtenerPorId(int wishitemId) {
        String sql = "select * from tienda_deportiva.wishlist_items where wishitem_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, wishitemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                WishlistItems item = new WishlistItems();
                item.setWishitemId(rs.getInt("wishitem_id"));
                item.setWishlistId(rs.getInt("wishlist_id"));
                item.setProductoId(rs.getInt("producto_id"));
                return item;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener wishlist item: " + e.getMessage());
        }
        return null;
    }

    public List<WishlistItems> obtenerTodos() {
        List<WishlistItems> lista = new ArrayList<>();
        String sql = "select * from tienda_deportiva.wishlist_items";
        try (Connection conn = ConnectionDB.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                WishlistItems item = new WishlistItems();
                item.setWishitemId(rs.getInt("wishitem_id"));
                item.setWishlistId(rs.getInt("wishlist_id"));
                item.setProductoId(rs.getInt("producto_id"));
                lista.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar wishlist items: " + e.getMessage());
        }
        return lista;
    }
    
    public List<WishlistItems> obtenerPorWishlistId(int wishlistId) {
        List<WishlistItems> lista = new ArrayList<>();
        String sql = "SELECT * FROM tienda_deportiva.wishlist_items WHERE wishlist_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, wishlistId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    WishlistItems item = new WishlistItems();
                    item.setWishitemId(rs.getInt("wishitem_id"));
                    item.setWishlistId(rs.getInt("wishlist_id"));
                    item.setProductoId(rs.getInt("producto_id"));
                    lista.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar wishlist_items por wishlist_id: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(WishlistItems item) {
        String sql = "update tienda_deportiva.wishlist_items set wishlist_id = ?, producto_id = ? where wishitem_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getWishlistId());
            stmt.setInt(2, item.getProductoId());
            stmt.setInt(3, item.getWishitemId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar wishlist item: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int wishitemId) {
        String sql = "delete from tienda_deportiva.wishlist_items where wishitem_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, wishitemId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar wishlist item: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarPorWishlistId(int wishlistId) {
        String sql = "DELETE FROM tienda_deportiva.wishlist_items WHERE wishlist_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, wishlistId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar wishlist_items por wishlist_id: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarPorProductoId(int productoId) {
        String sql = "DELETE FROM tienda_deportiva.wishlist_items WHERE producto_id = ?";
        try (Connection conn = ConnectionDB.getConn();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productoId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar wishlist_items por producto_id: " + e.getMessage());
            return false;
        }
    }
}