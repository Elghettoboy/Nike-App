package Controller.Cliente;
import Models.Tarjetas;
import Service.Clientes.TarjetasClienteService;
import java.util.List;

public class TarjetasClienteController {
    private TarjetasClienteService tarjetasClienteService;

    public TarjetasClienteController() {
        this.tarjetasClienteService = new TarjetasClienteService();
    }

    public boolean crearTarjeta(Tarjetas tarjetas) {
        return tarjetasClienteService.crearTarjeta(tarjetas);
    }

    public Tarjetas obtenerTarjetaPorId(int id) {
        return tarjetasClienteService.obtenerTarjetaPorId(id);
    }

    public List<Tarjetas> listarTarjetas() {
        return tarjetasClienteService.listarTarjetas();
    }

    public boolean actualizarTarjeta(Tarjetas tarjetas) {
        return tarjetasClienteService.actualizarTarjeta(tarjetas);
    }

    public boolean eliminarTarjeta(int id) {
        return tarjetasClienteService.eliminarTarjeta(id);
    }   

}
