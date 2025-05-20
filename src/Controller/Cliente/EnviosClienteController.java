package Controller.Cliente;
import Models.Envios;
import Service.Clientes.EnviosClienteService;
import java.util.List;

public class EnviosClienteController {
    private EnviosClienteService enviosService;

    public EnviosClienteController() {
        this.enviosService = new EnviosClienteService();
    }

    public boolean agregarEnvio(Envios envio) {
        return enviosService.agregarEnvio(envio);
    }

    public Envios obtenerEnvioPorId(int envioId) {
        return enviosService.obtenerEnvioPorId(envioId);
    }

    public List<Envios> listarEnvios() {
        return enviosService.listarEnvios();
    }

    public boolean actualizarEnvio(Envios envio) {
        return enviosService.actualizarEnvio(envio);
    }

    public boolean eliminarEnvio(int envioId) {
        return enviosService.eliminarEnvio(envioId);
    }   
        

}
