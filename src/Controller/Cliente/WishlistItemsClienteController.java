package Controller.Cliente;

import Models.WishlistItems;
import Service.Clientes.WishlistItemsClienteService;

import java.util.List;

public class WishlistItemsClienteController {

    private WishlistItemsClienteService wishlistService;

    public WishlistItemsClienteController() {
        this.wishlistService = new WishlistItemsClienteService();
    }

    public boolean agregarItemAWishlist(WishlistItems item) {
        return wishlistService.agregarItemAWishlist(item);
    }

    public WishlistItems obtenerItemPorId(int id) {
        return wishlistService.obtenerItemPorId(id);
    }

    public List<WishlistItems> listarItems() {
        return wishlistService.listarItems();
    }

    public boolean actualizarItem(WishlistItems item) {
        return wishlistService.actualizarItem(item);
    }

    public boolean eliminarItem(int id) {
        return wishlistService.eliminarItem(id);
    }
}
