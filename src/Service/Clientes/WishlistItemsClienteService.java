package Service.Clientes;

import Models.WishlistItems;
import Repository.WishlistItemsDAO;

import java.util.List;

public class WishlistItemsClienteService {

    private WishlistItemsDAO wishlistItemsDAO;

    public WishlistItemsClienteService() {
        this.wishlistItemsDAO = new WishlistItemsDAO();
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

    public boolean actualizarItem(WishlistItems item) {
        return wishlistItemsDAO.actualizar(item);
    }

    public boolean eliminarItem(int id) {
        return wishlistItemsDAO.eliminar(id);
    }
}
