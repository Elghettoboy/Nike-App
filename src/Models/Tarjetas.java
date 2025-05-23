package Models;

public class Tarjetas {
    private int tarjetaId;
    private int metodoId;
    private String ultimosDigitos;
    private String marca;
    private String fechaExpiracion;
    private String token;
    
    public int getTarjetaId() {
        return tarjetaId;
    }
    public void setTarjetaId(int tarjetaId) {
        this.tarjetaId = tarjetaId;
    }
    public int getMetodoId() {
        return metodoId;
    }
    public void setMetodoId(int metodoId) {
        this.metodoId = metodoId;
    }
    public String getUltimosDigitos() {
        return ultimosDigitos;
    }
    public void setUltimosDigitos(String ultimosDigitos) {
        this.ultimosDigitos = ultimosDigitos;
    }
    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }
    public String getFechaExpiracion() {
        return fechaExpiracion;
    }
    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
