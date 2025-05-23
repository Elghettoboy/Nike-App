package Controller.Cliente;

import Models.Wishlist;
import Service.Clientes.WishlistClienteService;
import java.util.List;
import java.util.ArrayList;

public class WishlistClienteController {
    private WishlistClienteService wishlistClienteService;

    public WishlistClienteController() {
        try {
            this.wishlistClienteService = new WishlistClienteService();
        } catch (RuntimeException e) {
            System.err.println("Error al inicializar WishlistClienteService: " + e.getMessage());
            throw e;
        }
    }

    public boolean crearWishlist(Wishlist wishlist) {
        return wishlistClienteService.crearWishlist(wishlist);
    }

    public Wishlist obtenerWishlistPorId(int id) {
        return wishlistClienteService.obtenerWishlistPorId(id);
    }
    
    public Wishlist obtenerWishlistPorUsuarioId(int usuarioId) {
        if (this.wishlistClienteService == null) {
            System.err.println("WishlistClienteService no está inicializado.");
            return null;
        }
        return wishlistClienteService.obtenerWishlistPorUsuarioId(usuarioId);
    }

    public List<Wishlist> listarWishlists() {
        return wishlistClienteService.listarWishlists();
    }

    public boolean actualizarWishlist(Wishlist wishlist) {
        return wishlistClienteService.actualizarWishlist(wishlist);
    }

    public boolean eliminarWishlist(int id) {
        return wishlistClienteService.eliminarWishlist(id);
    }
}