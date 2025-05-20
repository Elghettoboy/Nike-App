package Controller.Cliente;
import Models.Pago;
import Service.Clientes.PagosClienteService;
import java.util.List;


public class PagosClienteController {
    private PagosClienteService pagosService;

    public PagosClienteController() {
        this.pagosService = new PagosClienteService();
    }

    public boolean agregarPago(Pago pago) {
        return pagosService.registrarPago(pago);
    }

    public Pago obtenerPagoPorId(int pagoId) {
        return pagosService.obtenerPagoPorId(pagoId);
    }

    public List<Pago> listarPagos() {
        return pagosService.listarPagos();
    }

    public boolean actualizarPago(Pago pago) {
        return pagosService.actualizarPago(pago);
    }

    public boolean eliminarPago(int pagoId) {
        return pagosService.eliminarPago(pagoId);
    }   

}
