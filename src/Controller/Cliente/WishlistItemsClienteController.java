package Controller.Cliente;

import Models.WishlistItems;
import Service.Clientes.WishlistItemsClienteService;
import java.util.List;
import java.util.ArrayList;


public class WishlistItemsClienteController {

    private WishlistItemsClienteService wishlistItemsService;

    public WishlistItemsClienteController() {
        try {
            this.wishlistItemsService = new WishlistItemsClienteService();
        } catch (RuntimeException e) {
            System.err.println("Error al inicializar WishlistItemsClienteService: " + e.getMessage());
            throw e; 
        }
    }

    public boolean agregarItemAWishlist(WishlistItems item) {
        return wishlistItemsService.agregarItemAWishlist(item);
    }

    public WishlistItems obtenerItemPorId(int id) {
        return wishlistItemsService.obtenerItemPorId(id);
    }

    public List<WishlistItems> listarItems() {
        return wishlistItemsService.listarItems();
    }
    
    public List<WishlistItems> listarItemsPorWishlistId(int wishlistId) {
        if (this.wishlistItemsService == null) {
            System.err.println("WishlistItemsClienteService no está inicializado.");
            return new ArrayList<>();
        }
        return wishlistItemsService.listarItemsPorWishlistId(wishlistId);
    }

    public boolean actualizarItem(WishlistItems item) {
        return wishlistItemsService.actualizarItem(item);
    }

    public boolean eliminarItem(int id) {
        return wishlistItemsService.eliminarItem(id);
    }
}