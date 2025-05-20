package Service.Clientes;

import java.util.List;
import Models.MetodoDePago;
import Repository.MetodoDePagoDAO;

public class MetodosPagoClienteService {
    private MetodoDePagoDAO dao = new MetodoDePagoDAO();

    public boolean agregarMetodo(MetodoDePago metodo) {
        return dao.insertar(metodo);
    }

    public MetodoDePago obtenerMetodoPorId(int metodoId) {
        return dao.obtenerPorId(metodoId);
    }

    public List<MetodoDePago> listarMetodos() {
        return dao.obtenerTodos();
    }

    public boolean actualizarMetodo(MetodoDePago metodo) {
        return dao.actualizar(metodo);
    }

    public boolean eliminarMetodo(int metodoId) {
        return dao.eliminar(metodoId);
    }
}
