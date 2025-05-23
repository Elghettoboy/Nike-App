package Controller.Cliente;

import Models.Productos;
import Service.Clientes.ProductosClienteService;
import java.util.List;

public class ProductosClienteController {
    private ProductosClienteService productosService;

    public ProductosClienteController() {
        
        this.productosService = new ProductosClienteService();
    }

    public boolean agregarProducto(Productos producto) {
        return productosService.crearProducto(producto);
    }

    public Productos obtenerProductoPorId(int productoId) {
        return productosService.obtenerProductoPorId(productoId);
    }

    public List<Productos> listarProductos() {
        return productosService.listarProductos();
    }

    // NUEVO MÉTODO PARA PAGINACIÓN
    public List<Productos> listarProductosPaginados(int limite, int offset) {
        return productosService.listarProductosPaginados(limite, offset);
    }

    public boolean actualizarProducto(Productos producto) {
        return productosService.actualizarProducto(producto);
    }

    public boolean eliminarProducto(int productoId) {
        return productosService.eliminarProducto(productoId);
    }
}