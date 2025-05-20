package Service.Clientes;

import java.util.List;
import Models.ItemsCarrito;
import Repository.ItemsCarritoDAO;

public class ItemsCarritoClienteService {

    private ItemsCarritoDAO dao = new ItemsCarritoDAO();

    public boolean agregarItem(ItemsCarrito item) {
        return dao.insertar(item);
    }

    public ItemsCarrito obtenerItemPorCarritoId(int carritoId) {
        return dao.obtenerPorId(carritoId);
    }

    public List<ItemsCarrito> listarItems() {
        return dao.obtenerTodos();
    }

    public boolean actualizarItem(ItemsCarrito item) {
        return dao.actualizar(item);
    }

    public boolean eliminarItem(int carritoId) {
        return dao.eliminar(carritoId);
    }
}
