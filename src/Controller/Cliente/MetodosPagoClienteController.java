package Controller.Cliente;

import Models.MetodoDePago;
import Service.Clientes.MetodosPagoClienteService;

import java.util.List;

public class MetodosPagoClienteController {

    private MetodosPagoClienteService metodoService;

    public MetodosPagoClienteController() {
        this.metodoService = new MetodosPagoClienteService();
    }

    public boolean agregarMetodo(MetodoDePago metodo) {
        return metodoService.agregarMetodo(metodo);
    }

    public MetodoDePago obtenerMetodoPorId(int metodoId) {
        return metodoService.obtenerMetodoPorId(metodoId);
    }

    public List<MetodoDePago> listarMetodos() {
        return metodoService.listarMetodos();
    }

    public boolean actualizarMetodo(MetodoDePago metodo) {
        return metodoService.actualizarMetodo(metodo);
    }

    public boolean eliminarMetodo(int metodoId) {
        return metodoService.eliminarMetodo(metodoId);
    }
}
