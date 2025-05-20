package Models;

public class Envios {
    
    private int envioId;
    private int detalleId;
    private String codigoSeguimiento;
    private String estadoEnvio;
    
    public int getEnvioId() {
        return envioId;
    }
    public void setEnvioId(int envioId) {
        this.envioId = envioId;
    }
    public int getDetalleId() {
        return detalleId;
    }
    public void setDetalleId(int detalleId) {
        this.detalleId = detalleId;
    }
    public String getCodigoSeguimiento() {
        return codigoSeguimiento;
    }
    public void setCodigoSeguimiento(String codigoSeguimiento) {
        this.codigoSeguimiento = codigoSeguimiento;
    }
    public String getEstadoEnvio() {
        return estadoEnvio;
    }
    public void setEstadoEnvio(String estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }
    
    
    
    
}