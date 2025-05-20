package Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.Envios;


public class EnviosDAO extends BaseRepository {

    public boolean insertar(Envios envio) {
        String sql = "insert into tienda_deportiva.envios (detalle_id, codigo_seguimiento, estado_envio) values (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, envio.getDetalleId());
                stmt.setString(2, envio.getCodigoSeguimiento());
                stmt.setString(3, envio.getEstadoEnvio());
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                System.out.println("Error al insertar envio: " + e.getMessage());
                return false;
            }
    }

    public Envios obtenerPorId(int envioId) { 
        String sql = "select * from  tienda_deportiva.envios where envio_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, envioId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Envios envio = new Envios();
                envio.setEnvioId(rs.getInt("envio_id"));
                envio.setDetalleId(rs.getInt("detalle_id"));
                envio.setCodigoSeguimiento(rs.getString("codigo_seguimiento"));
                envio.setEstadoEnvio(rs.getString("estado_envio"));
                return envio;
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener envio: " + e.getMessage());
        }
        return null;
    }

    public List<Envios> obtenerTodos() {
        List<Envios> lista = new ArrayList<>();
        String sql = "select * from  tienda_deportiva.envios";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Envios envio = new Envios();
                envio.setEnvioId(rs.getInt("envio_id"));
                envio.setDetalleId(rs.getInt("detalle_id"));
                envio.setCodigoSeguimiento(rs.getString("codigo_seguimiento"));
                envio.setEstadoEnvio(rs.getString("estado_envio"));
                lista.add(envio);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar envios: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Envios envio) {
        String sql = "update tienda_deportiva.envios set detalle_id = ?, codigo_seguimiento = ?, estado_envio = ? where envio_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, envio.getDetalleId());
            stmt.setString(2, envio.getCodigoSeguimiento());
            stmt.setString(3, envio.getEstadoEnvio());
            stmt.setInt(4, envio.getEnvioId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar envio: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int envioId) {
        String sql = "delete from tienda_deportiva.envios where envio_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, envioId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar envio: " + e.getMessage());
            return false;
        }
    }   

}
