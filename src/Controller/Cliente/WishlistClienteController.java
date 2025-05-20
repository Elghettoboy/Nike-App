package Controller.Cliente;
import Models.Wishlist;
import Service.Clientes.WishlistClienteService;
import java.util.List;

public class WishlistClienteController {
    private WishlistClienteService wishlistClienteService;

    public WishlistClienteController() {
        this.wishlistClienteService = new WishlistClienteService();
    }

    public boolean crearWishlist(Wishlist wishlist) {
        return wishlistClienteService.crearWishlist(wishlist);
    }

    public Wishlist obtenerWishlistPorId(int id) {
        return wishlistClienteService.obtenerWishlistPorId(id);
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
