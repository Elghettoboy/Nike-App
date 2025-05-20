package Service.Clientes;

import java.util.List;
import Models.Pago;
import Repository.PagoDAO;

public class PagosClienteService {
    private PagoDAO dao = new PagoDAO();

    public boolean registrarPago(Pago pago) {
        return dao.insertar(pago);
    }

    public Pago obtenerPagoPorId(int pagoId) {
        return dao.obtenerPorId(pagoId);
    }

    public List<Pago> listarPagos() {
        return dao.obtenerTodos();
    }

    public boolean actualizarPago(Pago pago) {
        return dao.actualizar(pago);
    }

    public boolean eliminarPago(int pagoId) {
        return dao.eliminar(pagoId);
    }
}
