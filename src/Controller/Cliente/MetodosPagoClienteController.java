package Controller.Cliente;

import Models.MetodoDePago;
import Models.Tarjetas;
import Service.Clientes.MetodosPagoClienteService;
import Repository.TarjetasDAO;

import java.sql.SQLException;
import java.util.List;

public class MetodosPagoClienteController {

    private MetodosPagoClienteService metodoService;
    private TarjetasDAO tarjetasDAO;

    public MetodosPagoClienteController() throws SQLException {
        this.metodoService = new MetodosPagoClienteService();
        this.tarjetasDAO = new TarjetasDAO();
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
        try {
            // Elimina primero la tarjeta asociada, si existe
            tarjetasDAO.eliminarPorMetodoId(metodoId);
            // Luego elimina el método de pago
            return metodoService.eliminarMetodo(metodoId);
        } catch (Exception e) {
            System.out.println("Error al eliminar método de pago y su tarjeta: " + e.getMessage());
            return false;
        }
    }


    public boolean agregarTarjetaParaMetodo(int metodoPagoId, String titular, String numero, String vencimiento, String cvv) {
        try {
            Tarjetas tarjeta = new Tarjetas();
            tarjeta.setMetodoId(metodoPagoId);
            tarjeta.setUltimosDigitos(numero);
            tarjeta.setMarca(titular);

        
            tarjeta.setFechaExpiracion(vencimiento);
            tarjeta.setToken(cvv);

            return tarjetasDAO.insertar(tarjeta);

        } catch (Exception e) {
            System.out.println("Error al insertar tarjeta: " + e.getMessage());
            return false;
        }
    }

    public Tarjetas obtenerTarjetaPorMetodo(int metodoPagoId) {
        try {
            return tarjetasDAO.obtenerPorMetodoId(metodoPagoId);
        } catch (Exception e) {
            System.out.println("Error al obtener tarjeta: " + e.getMessage());
            return null;
        }
    }

    public boolean editarTarjeta(int metodoPagoId, String titular, String numero, String vencimiento, String cvv) {
        try {
            Tarjetas tarjeta = new Tarjetas();
            tarjeta.setMetodoId(metodoPagoId);
            tarjeta.setUltimosDigitos(numero);
            tarjeta.setMarca(titular);

           

            tarjeta.setFechaExpiracion(vencimiento);
            tarjeta.setToken(cvv);

            return tarjetasDAO.actualizarPorMetodoId(tarjeta);
        } catch (Exception e) {
            System.out.println("Error al editar tarjeta: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarTarjeta(int metodoPagoId) {
        try {
            return tarjetasDAO.eliminarPorMetodoId(metodoPagoId);
        } catch (Exception e) {
            System.out.println("Error al eliminar tarjeta: " + e.getMessage());
            return false;
        }
    }
}
