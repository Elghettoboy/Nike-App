package Service.Clientes;

import Models.Carrito;
import Repository.CarritoDAO;

import java.util.List;

public class CarritoClienteService {

    private CarritoDAO carritoDAO;

    public CarritoClienteService() {
        this.carritoDAO = new CarritoDAO();
    }

    public boolean crearCarrito(Carrito carrito) {
        // Validación: que el usuario tenga solo un carrito activo podría ir aquí si decides hacerlo.
        return carritoDAO.insertar(carrito);
    }

    public Carrito obtenerCarritoPorId(int carritoId) {
        return carritoDAO.obtenerPorId(carritoId);
    }

    public List<Carrito> obtenerTodosLosCarritos() {
        return carritoDAO.obtenerTodos();
    }

    public boolean actualizarCarrito(Carrito carrito) {
        return carritoDAO.actualizar(carrito);
    }

    public boolean eliminarCarrito(int carritoId) {
        return carritoDAO.eliminar(carritoId);
    }

    
    public Carrito obtenerCarritoPorUsuarioId(int usuarioId) {
        List<Carrito> carritos = carritoDAO.obtenerTodos();
        for (Carrito c : carritos) {
            if (c.getUsuarioId() == usuarioId) {
                return c; 
            }
        }
        return null;
    }
}
