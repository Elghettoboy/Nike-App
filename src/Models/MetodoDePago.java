package Models;

public class MetodoDePago {
    
    private int metodoId;
    private int usuarioId;
    private String tipo;
    private boolean activo;
    
    public int getMetodoId() {
        return metodoId;
    }
    public void setMetodoId(int metodoId) {
        this.metodoId = metodoId;
    }
    public int getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    

}
