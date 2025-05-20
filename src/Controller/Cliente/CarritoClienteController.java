package Controller.Cliente;

import Models.Carrito;
import Service.Clientes.CarritoClienteService;
import java.util.List;

public class CarritoClienteController {

    private CarritoClienteService carritoService;

    public CarritoClienteController() {
        this.carritoService = new CarritoClienteService();
    }

    public boolean crearCarrito(Carrito carrito) {
        return carritoService.crearCarrito(carrito);
    }

    public Carrito obtenerCarritoPorId(int carritoId) {
        return carritoService.obtenerCarritoPorId(carritoId);
    }

    public List<Carrito> obtenerTodosLosCarritos() {
        return carritoService.obtenerTodosLosCarritos();
    }

    public boolean actualizarCarrito(Carrito carrito) {
        return carritoService.actualizarCarrito(carrito);
    }

    public boolean eliminarCarrito(int carritoId) {
        return carritoService.eliminarCarrito(carritoId);
    }

    public Carrito obtenerCarritoPorUsuarioId(int usuarioId) {
        return carritoService.obtenerCarritoPorUsuarioId(usuarioId);
    }
}
