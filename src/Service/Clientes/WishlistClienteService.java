package Service.Clientes;

import Models.Wishlist;
import Repository.WishlistDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WishlistClienteService {

    private WishlistDAO wishlistDAO;

    public WishlistClienteService() {
        try {
            this.wishlistDAO = new WishlistDAO();
        } catch (SQLException e) {
            System.err.println("Error al inicializar WishlistDAO: " + e.getMessage());
            throw new RuntimeException("No se pudo inicializar WishlistDAO", e);
        }
    }

    public boolean crearWishlist(Wishlist wishlist) {
        return wishlistDAO.insertar(wishlist);
    }

    public Wishlist obtenerWishlistPorId(int id) {
        return wishlistDAO.obtenerPorId(id);
    }
    
    public Wishlist obtenerWishlistPorUsuarioId(int usuarioId){
        if (this.wishlistDAO == null) {
            System.err.println("WishlistDAO no está inicializado.");
            return null;
        }
        return wishlistDAO.obtenerPorUsuarioId(usuarioId);
    }

    public List<Wishlist> listarWishlists() {
        return wishlistDAO.obtenerTodos();
    }

    public boolean actualizarWishlist(Wishlist wishlist) {
        return wishlistDAO.actualizar(wishlist);
    }

    public boolean eliminarWishlist(int id) {
        return wishlistDAO.eliminar(id);
    }
}