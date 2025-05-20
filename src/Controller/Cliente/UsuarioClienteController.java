package Controller.Cliente;

import Models.Usuarios;
import Service.Clientes.UsuarioClienteService;

public class UsuarioClienteController {

    private UsuarioClienteService usuarioService;

    public UsuarioClienteController() {
        this.usuarioService = new UsuarioClienteService();
    }

    public boolean login(String correo, String contraseña) {
        return usuarioService.validarCredenciales(correo, contraseña);
    }

    public Usuarios obtenerUsuario(String correo) {
        return usuarioService.obtenerUsuarioPorCorreo(correo);
    }

    public boolean registrar(Usuarios usuario) {
        return usuarioService.registrarUsuario(usuario);
    }

    public boolean actualizar(Usuarios usuario) {
        return usuarioService.actualizarUsuario(usuario);
    }

    public boolean eliminar(int usuarioId) {
        return usuarioService.eliminarUsuario(usuarioId);
    }
}
