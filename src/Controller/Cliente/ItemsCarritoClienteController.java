package Controller.Cliente;

import Models.ItemsCarrito;
import Service.Clientes.ItemsCarritoClienteService;
import java.util.List;
import java.util.ArrayList;

public class ItemsCarritoClienteController {

    private ItemsCarritoClienteService itemsCarritoService;

    public ItemsCarritoClienteController() {
        try {
            this.itemsCarritoService = new ItemsCarritoClienteService();
        } catch (RuntimeException e) {
            System.err.println("Error al inicializar ItemsCarritoClienteService: " + e.getMessage());
            throw e;
        }
    }

    public boolean agregarOActualizarItemAlCarrito(ItemsCarrito item) {
        if (this.itemsCarritoService == null) return false;
        return itemsCarritoService.agregarOActualizarItem(item);
    }

    public ItemsCarrito obtenerItemPorId(int carritoId, int productoId) {
        if (this.itemsCarritoService == null) return null;
        return itemsCarritoService.obtenerItemPorId(carritoId, productoId);
    }

    public List<ItemsCarrito> obtenerItemsPorCarritoId(int carritoId) {
        if (this.itemsCarritoService == null) return new ArrayList<>();
        return itemsCarritoService.obtenerItemsPorCarritoId(carritoId);
    }

    public boolean actualizarItem(ItemsCarrito item) {
        if (this.itemsCarritoService == null) return false;
        return itemsCarritoService.actualizarItem(item);
    }

    public boolean eliminarItem(int carritoId, int productoId) {
        if (this.itemsCarritoService == null) return false;
        return itemsCarritoService.eliminarItem(carritoId, productoId);
    }
    
    public boolean eliminarTodosLosItemsDeUnCarrito(int carritoId) {
        if (this.itemsCarritoService == null) return false;
        return itemsCarritoService.eliminarItemsPorCarritoId(carritoId);
    }
    
   
}