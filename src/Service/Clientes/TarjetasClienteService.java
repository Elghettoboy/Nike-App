package Service.Clientes;
import java.util.List;

import Models.Tarjetas;
import Repository.TarjetasDAO;



public class TarjetasClienteService {
    private TarjetasDAO tarjetasDAO;

    public TarjetasClienteService() {
        this.tarjetasDAO = new TarjetasDAO();
    }

    public boolean crearTarjeta(Tarjetas tarjeta) {
        return tarjetasDAO.insertar(tarjeta);
    }

    public Tarjetas obtenerTarjetaPorId(int id) {
        return tarjetasDAO.obtenerPorId(id);
    }

    public List<Tarjetas> listarTarjetas() {
        return tarjetasDAO.obtenerTodos();
    }

    public boolean actualizarTarjeta(Tarjetas tarjeta) {
        return tarjetasDAO.actualizar(tarjeta);
    }

    public boolean eliminarTarjeta(int id) {
        return tarjetasDAO.eliminar(id);
    }   
    

}
