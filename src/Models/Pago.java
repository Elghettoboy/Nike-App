package Models;

public class Pago {
    
    private int pagoId;
    private int usuarioId;
    private int metodoId;
    private double monto;
    private java.sql.Timestamp fechaPago;
    private String estado;
    
    public int getPagoId() {
        return pagoId;
    }
    public void setPagoId(int pagoId) {
        this.pagoId = pagoId;
    }
    public int getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    public int getMetodoId() {
        return metodoId;
    }
    public void setMetodoId(int metodoId) {
        this.metodoId = metodoId;
    }
    public double getMonto() {
        return monto;
    }
    public void setMonto(double monto) {
        this.monto = monto;
    }
    public java.sql.Timestamp getFechaPago() {
        return fechaPago;
    }
    public void setFechaPago(java.sql.Timestamp fechaPago) {
        this.fechaPago = fechaPago;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    
    
}
