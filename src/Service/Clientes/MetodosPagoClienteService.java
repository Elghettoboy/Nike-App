package Service.Clientes;

import java.sql.SQLException;
import java.util.List;
import Models.MetodoDePago;
import Repository.MetodoDePagoDAO;

public class MetodosPagoClienteService {

    private MetodoDePagoDAO dao;

    // Constructor, puede lanzar SQLException si el DAO lo hace
    public MetodosPagoClienteService() {
        try {
            this.dao = new MetodoDePagoDAO();
        } catch (SQLException e) {
            System.out.println("Error al inicializar MetodoDePagoDAO: " + e.getMessage());
            // Aquí puedes lanzar un RuntimeException o manejarlo según convenga
            throw new RuntimeException(e);
        }
    }

    // Agrega un nuevo método de pago
    public boolean agregarMetodo(MetodoDePago metodo) {
        return dao.insertar(metodo);
    }

    // Obtiene un método de pago por su ID
    public MetodoDePago obtenerMetodoPorId(int metodoId) {
        return dao.obtenerPorId(metodoId);
    }

    // Lista todos los métodos de pago
    public List<MetodoDePago> listarMetodos() {
        return dao.obtenerTodos();
    }

    // Actualiza un método de pago existente
    public boolean actualizarMetodo(MetodoDePago metodo) {
        return dao.actualizar(metodo);
    }

    // Elimina un método de pago por su ID
    public boolean eliminarMetodo(int metodoId) {
        return dao.eliminar(metodoId);
    }
}
