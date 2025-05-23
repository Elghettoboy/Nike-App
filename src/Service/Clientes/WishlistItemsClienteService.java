package Service.Clientes;

import Models.WishlistItems;
import Repository.WishlistItemsDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WishlistItemsClienteService {
    private WishlistItemsDAO wishlistItemsDAO;

    public WishlistItemsClienteService() {
        try {
            this.wishlistItemsDAO = new WishlistItemsDAO();
        } catch (SQLException e) {
            System.err.println("Error al inicializar WishlistItemsDAO: " + e.getMessage());
            throw new RuntimeException("No se pudo inicializar el servicio de items de wishlist.", e);
        }
    }

    public boolean agregarItemAWishlist(WishlistItems item) {
        return wishlistItemsDAO.insertar(item);
    }

    public WishlistItems obtenerItemPorId(int id) {
        return wishlistItemsDAO.obtenerPorId(id);
    }

    public List<WishlistItems> listarItems() {
        return wishlistItemsDAO.obtenerTodos();
    }
    
    public List<WishlistItems> listarItemsPorWishlistId(int wishlistId) {
        if (this.wishlistItemsDAO == null) {
            System.err.println("WishlistItemsDAO no está inicializado.");
            return new ArrayList<>();
        }
        return wishlistItemsDAO.obtenerPorWishlistId(wishlistId);
    }

    public boolean actualizarItem(WishlistItems item) {
        return wishlistItemsDAO.actualizar(item);
    }

    public boolean eliminarItem(int id) {
        return wishlistItemsDAO.eliminar(id);
    }
}