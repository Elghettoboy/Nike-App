package Service.Clientes;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import Config.ConnectionDB;
import Models.MetodoDePago;
import Models.Usuarios;
import Repository.MetodoDePagoDAO;
import Repository.TarjetasDAO;
import Repository.UsuariosDAO;
import Repository.CarritoDAO;
import Repository.PedidosDAO;
import Repository.PagoDAO;
import Repository.WishlistDAO;
import Repository.ReviewDAO;

public class UsuarioClienteService {

    private UsuariosDAO usuariosDAO;
    private MetodoDePagoDAO metodoDePagoDAO;
    private TarjetasDAO tarjetasDAO;
    private CarritoDAO carritoDAO;
    private PedidosDAO pedidosDAO;
    private PagoDAO pagosDAO;
    private WishlistDAO wishlistDAO;
    private ReviewDAO reviewDAO;

    public UsuarioClienteService() {
        try {
            this.usuariosDAO = new UsuariosDAO();
            this.metodoDePagoDAO = new MetodoDePagoDAO();
            this.tarjetasDAO = new TarjetasDAO();
            this.carritoDAO = new CarritoDAO(); // Su constructor puede lanzar SQLException
            this.pedidosDAO = new PedidosDAO(); // Su constructor puede lanzar SQLException
            this.pagosDAO = new PagoDAO();     // Asume que su constructor puede lanzar SQLException si usa conexión de instancia
            this.wishlistDAO = new WishlistDAO(); // Su constructor puede lanzar SQLException
            this.reviewDAO = new ReviewDAO();   // Su constructor puede lanzar SQLException
        } catch (SQLException e) {
            System.err.println("Error al inicializar DAOs en UsuarioClienteService: " + e.getMessage());
            throw new RuntimeException("Error de inicialización del servicio de usuario.", e);
        }
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
        Connection localConn = null;
        try {
            localConn = ConnectionDB.getConn();
            if (localConn == null) {
                System.out.println("No se pudo obtener la conexión a la base de datos para la transacción.");
                return false;
            }
            localConn.setAutoCommit(false);

            // Eliminar Reviews
            boolean reviewsEliminadas = reviewDAO.eliminarPorUsuarioId(usuarioId);
            if (!reviewsEliminadas) {
                localConn.rollback();
                System.out.println("Error al eliminar las reviews del usuario. Se hizo rollback.");
                return false;
            }

            // Eliminar Wishlists (y sus ítems)
            boolean wishlistsEliminadas = wishlistDAO.eliminarPorUsuarioId(usuarioId);
            if (!wishlistsEliminadas) {
                localConn.rollback();
                System.out.println("Error al eliminar las wishlists del usuario. Se hizo rollback.");
                return false;
            }

            // Eliminar Pagos
            boolean pagosEliminados = pagosDAO.eliminarPorUsuarioId(usuarioId);
            if (!pagosEliminados) {
                localConn.rollback();
                System.out.println("Error al eliminar los pagos del usuario. Se hizo rollback.");
                return false;
            }

            // Eliminar Pedidos (y sus detalles)
            boolean pedidosEliminados = pedidosDAO.eliminarPorUsuarioId(usuarioId);
            if (!pedidosEliminados) {
                localConn.rollback();
                System.out.println("Error al eliminar los pedidos del usuario. Se hizo rollback.");
                return false;
            }

            // Eliminar Carritos (y sus ítems)
            boolean carritosEliminados = carritoDAO.eliminarPorUsuarioId(usuarioId);
            if (!carritosEliminados) {
                localConn.rollback();
                System.out.println("Error al eliminar los carritos del usuario. Se hizo rollback.");
                return false;
            }

            // Eliminar Métodos de Pago (y sus tarjetas)
            List<MetodoDePago> metodosDelUsuario = metodoDePagoDAO.obtenerPorUsuarioId(usuarioId);
            for (MetodoDePago metodo : metodosDelUsuario) {
                boolean tarjetaEliminada = tarjetasDAO.eliminarPorMetodoId(metodo.getMetodoId());
                if (!tarjetaEliminada) {
                    System.out.println("Advertencia: No se eliminó/encontró tarjeta para metodo_id: " + metodo.getMetodoId());
                }
            }
            boolean metodosEliminados = metodoDePagoDAO.eliminarPorUsuarioId(usuarioId);
            if (!metodosEliminados) {
                localConn.rollback();
                System.out.println("Error al eliminar los métodos de pago del usuario. Se hizo rollback.");
                return false;
            }

            // Finalmente, eliminar el usuario
            boolean usuarioEliminado = usuariosDAO.eliminar(usuarioId);
            if (!usuarioEliminado) {
                localConn.rollback();
                System.out.println("Error al eliminar el usuario. Se hizo rollback.");
                return false;
            }

            localConn.commit();
            System.out.println("Perfil de usuario y todos sus datos asociados eliminados correctamente.");
            return true;

        } catch (SQLException e) {
            System.out.println("Error en la transacción de eliminación de perfil: " + e.getMessage());
            if (localConn != null) {
                try {
                    localConn.rollback();
                    System.out.println("Rollback ejecutado debido a SQLException.");
                } catch (SQLException ex) {
                    System.out.println("Error al intentar hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (localConn != null) {
                try {
                    localConn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.out.println("Error al restaurar auto-commit: " + e.getMessage());
                }
            }
        }
    }
}