package Service.Clientes;

import Models.Usuarios;
import Repository.UsuariosDAO;

public class UsuarioClienteService {

    private UsuariosDAO usuariosDAO;

    public UsuarioClienteService() {
        this.usuariosDAO = new UsuariosDAO();
    }

    public boolean validarCredenciales(String correo, String contraseña) {
        Usuarios usuario = usuariosDAO.obtenerUsuarioPorCorreo(correo);
        if (usuario != null && usuario.getContraseña().equals(contraseña)) {
            return true;
        }
        return false;
    }

    public Usuarios obtenerUsuarioPorCorreo(String correo) {
        return usuariosDAO.obtenerUsuarioPorCorreo(correo);
    }

    public boolean registrarUsuario(Usuarios usuario) {
        if (usuariosDAO.obtenerUsuarioPorCorreo(usuario.getCorreo()) != null) {
            System.out.println("Ya existe un usuario con ese correo.");
            return false;
        }
        return usuariosDAO.insertar(usuario);
    }

    public boolean actualizarUsuario(Usuarios usuario) {
        return usuariosDAO.actualizar(usuario);
    }

    public boolean eliminarUsuario(int usuarioId) {
        return usuariosDAO.eliminar(usuarioId);
    }
}
