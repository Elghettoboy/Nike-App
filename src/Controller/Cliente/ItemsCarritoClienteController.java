package Controller.Cliente;
import Models.ItemsCarrito;
import Service.Clientes.ItemsCarritoClienteService;
import java.util.List;

public class ItemsCarritoClienteController {
    private ItemsCarritoClienteService itemsCarritoService;

    public ItemsCarritoClienteController() {
        this.itemsCarritoService = new ItemsCarritoClienteService();
    }

    public boolean agregarItem(ItemsCarrito item) {
        return itemsCarritoService.agregarItem(item);
    }

    public ItemsCarrito obtenerItemPorCarrito(int itemId) {
        return itemsCarritoService.obtenerItemPorCarritoId(itemId);
    }

    public List<ItemsCarrito> listarItemsCarrito() {
        return itemsCarritoService.listarItems();
    }

    public boolean actualizarItemCarrito(ItemsCarrito item) {
        return itemsCarritoService.actualizarItem(item);
    }

    public boolean eliminarItemCarrito(int itemId) {
        return itemsCarritoService.eliminarItem(itemId);
    }   

}
