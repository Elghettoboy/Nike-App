package Service.Clientes;

import java.sql.SQLException;
import java.util.List;
import Models.Productos;
import Repository.ProductosDAO;

public class ProductosClienteService {

    private ProductosDAO productosDAO;

    public ProductosClienteService() {
        try {
            this.productosDAO = new ProductosDAO();
        } catch (SQLException e) {
            System.out.println("Error al inicializar ProductosDAO: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Crear un nuevo producto
    public boolean crearProducto(Productos producto) {
        return productosDAO.insertar(producto);
    }

    // Obtener un producto por ID
    public Productos obtenerProductoPorId(int id) {
        return productosDAO.obtenerPorId(id);
    }

    // Listar todos los productos
    public List<Productos> listarProductos() {
        return productosDAO.obtenerTodos();
    }

    // Actualizar un producto existente
    public boolean actualizarProducto(Productos producto) {
        return productosDAO.actualizar(producto);
    }

    // Eliminar un producto por ID
    public boolean eliminarProducto(int id) {
        return productosDAO.eliminar(id);
    }
     public List<Productos> listarProductosPaginados(int limite, int offset) {
        return productosDAO.obtenerProductosPaginados(limite, offset);
    }
}
