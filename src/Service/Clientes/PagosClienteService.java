package Service.Clientes;

import java.util.List;
import Models.Pago;
import Repository.PagoDAO;

public class PagosClienteService {

    private PagoDAO dao;

    public PagosClienteService() {
        this.dao = new PagoDAO();
    }

    // Registrar un nuevo pago
    public boolean registrarPago(Pago pago) {
        return dao.insertar(pago);
    }

    // Obtener un pago por su ID
    public Pago obtenerPagoPorId(int pagoId) {
        return dao.obtenerPorId(pagoId);
    }

    // Listar todos los pagos
    public List<Pago> listarPagos() {
        return dao.obtenerTodos();
    }

    // Actualizar un pago existente
    public boolean actualizarPago(Pago pago) {
        return dao.actualizar(pago);
    }

    // Eliminar un pago por ID
    public boolean eliminarPago(int pagoId) {
        return dao.eliminar(pagoId);
    }
}
