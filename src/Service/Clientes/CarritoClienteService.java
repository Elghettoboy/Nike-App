package Service.Clientes;

import Models.Carrito;
import Repository.CarritoDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarritoClienteService {

    private CarritoDAO carritoDAO;

    public CarritoClienteService() {
        try {
            this.carritoDAO = new CarritoDAO();
        } catch (SQLException e) {
            System.err.println("Error al inicializar CarritoDAO en CarritoClienteService: " + e.getMessage());
            throw new RuntimeException("No se pudo inicializar el servicio de carrito.", e);
        }
    }

    public boolean crearCarrito(Carrito carrito) {
        if (this.carritoDAO == null) return false;
        return carritoDAO.insertar(carrito);
    }

    public Carrito obtenerCarritoPorId(int carritoId) {
        if (this.carritoDAO == null) return null;
        return carritoDAO.obtenerPorId(carritoId);
    }

    public List<Carrito> obtenerTodosLosCarritos() {
        if (this.carritoDAO == null) return new ArrayList<>();
        return carritoDAO.obtenerTodosLosCarritos();
    }

    public boolean actualizarCarrito(Carrito carrito) {
        if (this.carritoDAO == null) return false;
        return carritoDAO.actualizar(carrito);
    }

    public boolean eliminarCarrito(int carritoId) {
        if (this.carritoDAO == null) return false;
        return carritoDAO.eliminar(carritoId);
    }

    public Carrito obtenerCarritoActivoPorUsuarioId(int usuarioId) {
        if (this.carritoDAO == null) return null;
        return carritoDAO.obtenerActivoPorUsuarioId(usuarioId);
    }
}