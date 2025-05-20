package Service.Clientes;

import Models.Wishlist;
import Repository.WishlistDAO;

import java.util.List;

public class WishlistClienteService {

    private WishlistDAO wishlistDAO;

    public WishlistClienteService() {
        this.wishlistDAO = new WishlistDAO();
    }

    public boolean crearWishlist(Wishlist wishlist) {
        return wishlistDAO.insertar(wishlist);
    }

    public Wishlist obtenerWishlistPorId(int id) {
        return wishlistDAO.obtenerPorId(id);
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
