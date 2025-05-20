package Service.Clientes;

import Models.Productos;
import Repository.ProductosDAO;

import java.util.List;

public class ProductosClienteService {

    private ProductosDAO productosDAO;

    public ProductosClienteService() {
        this.productosDAO = new ProductosDAO();
    }

    public boolean crearProducto(Productos producto) {
        return productosDAO.insertar(producto);
    }

    public Productos obtenerProductoPorId(int id) {
        return productosDAO.obtenerPorId(id);
    }

    public List<Productos> listarProductos() {
        return productosDAO.obtenerTodos();
    }

    public boolean actualizarProducto(Productos producto) {
        return productosDAO.actualizar(producto);
    }

    public boolean eliminarProducto(int id) {
        return productosDAO.eliminar(id);
    }
}
