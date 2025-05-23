package Service.Clientes;


import java.util.List;

import Models.Tarjetas;
import Repository.TarjetasDAO;

public class TarjetasClienteService {
    private TarjetasDAO tarjetasDAO;

    public TarjetasClienteService() {
        this.tarjetasDAO = new TarjetasDAO();
    }
    /**
     * Crear una nueva tarjeta
     */
    public boolean crearTarjeta(Tarjetas tarjeta) {
        return tarjetasDAO.insertar(tarjeta);
    }

    /**
     * Obtener tarjeta por ID
     */
    public Tarjetas obtenerTarjetaPorId(int id) {
        return tarjetasDAO.obtenerPorId(id);
    }

    /**
     * Listar todas las tarjetas
     */
    public List<Tarjetas> listarTarjetas() {
        return tarjetasDAO.obtenerTodos();
    }

    /**
     * Actualizar tarjeta existente
     */
    public boolean actualizarTarjeta(Tarjetas tarjeta) {
        return tarjetasDAO.actualizar(tarjeta);
    }

    /**
     * Eliminar tarjeta por ID
     */
    public boolean eliminarTarjeta(int id) {
        return tarjetasDAO.eliminar(id);
    }   
}
