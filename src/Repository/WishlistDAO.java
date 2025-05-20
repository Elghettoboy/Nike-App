package Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;  
import Models.Wishlist;

public class WishlistDAO extends BaseRepository {

    public boolean insertar(Wishlist wishlist) {
        String sql = "insert into tienda_deportiva.wishlist (usuario_id) values (?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, wishlist.getUsuarioId());
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                System.out.println("Error al insertar wishlist: " + e.getMessage());
                return false;
            }
    }
    public Wishlist obtenerPorId(int wishlistId) { 
        String sql = "select * from  tienda_deportiva.wishlist where wishlist_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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

    public List<Wishlist> obtenerTodos() {
        List<Wishlist> lista = new ArrayList<>();
        String sql = "select * from  tienda_deportiva.wishlist";

        try (Statement stmt = conn.createStatement();
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

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        String sql = "delete from tienda_deportiva.wishlist where wishlist_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, wishlistId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar wishlist: " + e.getMessage());
            return false;
        }
    }

}
