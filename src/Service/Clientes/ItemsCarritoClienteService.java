package Service.Clientes;

import Models.ItemsCarrito;
// Quita ItemsCarritoConProducto y su DAO si ya no los usas o los manejas diferente
// import Models.ItemsCarritoConProducto; 
import Repository.ItemsCarritoDAO;
// import Repository.ItemsConCarritoDAO; 

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class ItemsCarritoClienteService {

    private ItemsCarritoDAO itemsCarritoDAO;
    // private ItemsConCarritoDAO joinDAO; // Si lo usas para vistas combinadas

    public ItemsCarritoClienteService() {
        try {
            this.itemsCarritoDAO = new ItemsCarritoDAO();
            // this.joinDAO = new ItemsConCarritoDAO(); // Si lo usas
        } catch (Exception e) { 
            System.err.println("Error al inicializar ItemsCarritoDAO: " + e.getMessage());
            throw new RuntimeException("No se pudo inicializar el servicio de items de carrito.", e);
        }
    }

    public boolean agregarOActualizarItem(ItemsCarrito itemAAgregar) {
        if (this.itemsCarritoDAO == null) {
            System.err.println("ItemsCarritoDAO no está inicializado.");
            return false;
        }

        ItemsCarrito itemExistente = itemsCarritoDAO.obtenerPorId(itemAAgregar.getCarritoId(), itemAAgregar.getProductoId());

        if (itemExistente != null) {
            int nuevaCantidad = itemExistente.getCantidad() + itemAAgregar.getCantidad();
            if (nuevaCantidad <= 0) { // Si la suma resulta en 0 o menos, eliminamos el item
                return itemsCarritoDAO.eliminar(itemAAgregar.getCarritoId(), itemAAgregar.getProductoId());
            } else {
                itemExistente.setCantidad(nuevaCantidad);
                return itemsCarritoDAO.actualizar(itemExistente);
            }
        } else {
            if (itemAAgregar.getCantidad() > 0) { // Solo insertar si la cantidad es positiva
                return itemsCarritoDAO.insertar(itemAAgregar);
            } else {
                // No insertar si la cantidad inicial es 0 o negativa para un item nuevo
                System.out.println("La cantidad para un nuevo item debe ser positiva.");
                return false; 
            }
        }
    }

    public ItemsCarrito obtenerItemPorId(int carritoId, int productoId) {
        if (this.itemsCarritoDAO == null) return null;
        return itemsCarritoDAO.obtenerPorId(carritoId, productoId);
    }

    public List<ItemsCarrito> obtenerItemsPorCarritoId(int carritoId) {
        if (this.itemsCarritoDAO == null) return new ArrayList<>();
        return itemsCarritoDAO.obtenerPorCarritoId(carritoId);
    }

    public List<ItemsCarrito> listarItems() {
        if (this.itemsCarritoDAO == null) return new ArrayList<>();
        return itemsCarritoDAO.obtenerTodos();
    }

    public boolean actualizarItem(ItemsCarrito item) {
        if (this.itemsCarritoDAO == null) return false;
        // Este método se usaría para cambiar explícitamente la cantidad a un nuevo valor, no para sumar.
        if (item.getCantidad() <= 0) { // Si la nueva cantidad es 0 o menos, eliminar el item.
            return itemsCarritoDAO.eliminar(item.getCarritoId(), item.getProductoId());
        }
        return itemsCarritoDAO.actualizar(item);
    }

    public boolean eliminarItem(int carritoId, int productoId) {
        if (this.itemsCarritoDAO == null) return false;
        return itemsCarritoDAO.eliminar(carritoId, productoId);
    }

    public boolean eliminarItemsPorCarritoId(int carritoId) {
        if (this.itemsCarritoDAO == null) return false;
        return itemsCarritoDAO.eliminarPorCarritoId(carritoId);
    }
}