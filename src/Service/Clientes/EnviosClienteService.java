package Service.Clientes;

import java.util.List;
import Models.Envios;
import Repository.EnviosDAO;

public class EnviosClienteService {
    private EnviosDAO enviosDAO;

    public EnviosClienteService() {
        this.enviosDAO = new EnviosDAO();
    }

    public boolean agregarEnvio(Envios envio) {
        return enviosDAO.insertar(envio);
    }

    public Envios obtenerEnvioPorId(int envioId) {
        return enviosDAO.obtenerPorId(envioId);
    }

    public List<Envios> listarEnvios() {
        return enviosDAO.obtenerTodos();
    }

    public boolean actualizarEnvio(Envios envio) {
        return enviosDAO.actualizar(envio);
    }

    public boolean eliminarEnvio(int envioId) {
        return enviosDAO.eliminar(envioId);
    }
}
