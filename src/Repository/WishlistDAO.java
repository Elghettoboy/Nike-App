package Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.Wishlist;
import Config.ConnectionDB;

public class WishlistDAO {

    private WishlistItemsDAO wishlistItemsDAO;

    public WishlistDAO() throws SQLException {
        this.wishlistItemsDAO = new WishlistItemsDAO();
    }

    public boolean insertar(Wishlist wishlist) {
        String sql = "insert into tienda_deportiva.wishlist (usuario_id) values (?)";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, wishlist.getUsuarioId());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        wishlist.setWishlistId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Error al insertar wishlist: " + e.getMessage());
            return false;
        }
    }

    public Wishlist obtenerPorId(int wishlistId) {
        String sql = "select * from tienda_deportiva.wishlist where wishlist_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, wishlistId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Wishlist wishlist = new Wishlist();
                wishlist.setWishlistId(rs.getInt("wishlist_id"));
                wishlist.setUsuarioId(rs.getInt("usuario_id"));
                return wishlist;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener wishlist: " + e.getMessage());
        }
        return null;
    }
    
    public Wishlist obtenerPorUsuarioId(int usuarioId) {
        String sql = "SELECT * FROM tienda_deportiva.wishlist WHERE usuario_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Wishlist wishlist = new Wishlist();
                    wishlist.setWishlistId(rs.getInt("wishlist_id"));
                    wishlist.setUsuarioId(rs.getInt("usuario_id"));
                    return wishlist;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener wishlist por usuario_id: " + e.getMessage());
        }
        return null;
    }

    public List<Wishlist> obtenerTodos() {
        List<Wishlist> lista = new ArrayList<>();
        String sql = "select * from tienda_deportiva.wishlist";
        try (Connection conn = ConnectionDB.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Wishlist wishlist = new Wishlist();
                wishlist.setWishlistId(rs.getInt("wishlist_id"));
                wishlist.setUsuarioId(rs.getInt("usuario_id"));
                lista.add(wishlist);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar wishlists: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Wishlist wishlist) {
        String sql = "update tienda_deportiva.wishlist set usuario_id = ? where wishlist_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, wishlist.getUsuarioId());
            stmt.setInt(2, wishlist.getWishlistId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar wishlist: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int wishlistId) {
        boolean itemsEliminados = wishlistItemsDAO.eliminarPorWishlistId(wishlistId);
        if (!itemsEliminados) {
            System.out.println("Fallo al eliminar ítems para la wishlist_id: " + wishlistId + ". La eliminación de la wishlist se detendrá.");
            return false;
        }
        String sql = "delete from tienda_deportiva.wishlist where wishlist_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, wishlistId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar wishlist con id " + wishlistId + ": " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminarPorUsuarioId(int usuarioId) {
        Wishlist wishlistDelUsuario = obtenerPorUsuarioId(usuarioId); // Reutiliza el método que ya obtiene por usuario

        if (wishlistDelUsuario != null) {
            if (!eliminar(wishlistDelUsuario.getWishlistId())) { // Llama al método eliminar(wishlistId) que ya maneja items
                System.out.println("Fallo al eliminar la wishlist_id: " + wishlistDelUsuario.getWishlistId() + " para el usuario " + usuarioId);
                return false; // Detener si la wishlist del usuario no se puede eliminar
            }
        }
        // No es necesario un delete adicional aquí si la wishlist individual se borró
        return true;
    }
}