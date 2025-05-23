package UI.MenusClientes;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import Models.MetodoDePago;

import Models.Usuarios;
import Controller.Cliente.MetodosPagoClienteController;

public class MenuMetodoDePagoCliente {
    private static final Scanner sc = new Scanner(System.in);
    private static final MetodosPagoClienteController metodoController;

    static {
        MetodosPagoClienteController tempController = null;
        try {
            tempController = new MetodosPagoClienteController();
        } catch (java.sql.SQLException e) {
            System.err.println("Error al inicializar el controlador de métodos de pago: " + e.getMessage());
      
        }
        metodoController = tempController;
    }

    public static void limpiarpantalla() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("bash", "-c", "clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; ++i) System.out.println(); // Fallback
        }
    }

    public static void pausa() {
        System.out.println("\nPresiona ENTER para continuar...");
        sc.nextLine();
    }

    /**
     * Lista los métodos de pago del usuario actual.
     *
     * @param usuario       El usuario actual.
     * @param mensajePrevio Un mensaje opcional para mostrar antes de la lista.
     * @return true si se mostraron métodos, false si no hay o error.
     */
    private static boolean listarMetodosDePagoUsuarioParaSeleccion(Usuarios usuario, String mensajePrevio) {
        if (metodoController == null) {
            System.out.println("El controlador de métodos de pago no está inicializado.");
            return false;
        }

        List<MetodoDePago> todosMetodos = metodoController.listarMetodos(); // Assumes this gets all methods
        List<MetodoDePago> metodosDelUsuario = new ArrayList<>();

        if (todosMetodos != null) {
            for (MetodoDePago m : todosMetodos) {
                if (m.getUsuarioId() == usuario.getUsuarioId()) {
                    metodosDelUsuario.add(m);
                }
            }
        }

        if (metodosDelUsuario.isEmpty()) {
            System.out.println("No tienes métodos de pago registrados.");
            return false;
        }

        if (mensajePrevio != null && !mensajePrevio.isEmpty()) {
            System.out.println(mensajePrevio);
        }
        System.out.println("\n------ TUS MÉTODOS DE PAGO ------");
        for (MetodoDePago m : metodosDelUsuario) {
            System.out.println("ID: " + m.getMetodoId());
            System.out.println("Tipo: " + m.getTipo());
            System.out.println("Activo: " + (m.isActivo() ? "Sí" : "No"));
            System.out.println("---------------------------");
        }
        return true;
    }


    public static void mostrarMenuMetodoDePago(Usuarios usuario) {
        if (metodoController == null) {
            System.out.println("Error crítico: El controlador de métodos de pago no está disponible.");
            pausa();
            return;
        }

        while (true) {
            limpiarpantalla();
            System.out.println("------ MENÚ MÉTODOS DE PAGO ------");
            System.out.println("1. Ver todos los métodos de pago (tus métodos)");
            System.out.println("2. Ver método de pago por ID");
            System.out.println("3. Eliminar método de pago");
            System.out.println("4. Agregar nuevo método de pago (Tipo Tarjeta)"); // Clarified menu
            System.out.println("5. Gestionar tarjetas de métodos de pago");
            System.out.println("0. Volver al menú principal");
            System.out.print("\nElige una opción: ");

            try {
                int opc = Integer.parseInt(sc.nextLine());

                switch (opc) {
                    case 1: // Ver todos mis métodos de pago
                        limpiarpantalla();
                        System.out.println("------ MIS MÉTODOS DE PAGO REGISTRADOS ------");
                        listarMetodosDePagoUsuarioParaSeleccion(usuario, null);
                        pausa();
                        break;

                    case 2: // Ver método de pago por ID
                        limpiarpantalla();
                        System.out.println("------ VER MÉTODO DE PAGO POR ID ------");
                        boolean tieneMetodos = listarMetodosDePagoUsuarioParaSeleccion(usuario,
                                "Selecciona un método de la siguiente lista:");
                        if (!tieneMetodos) {
                            pausa();
                            break;
                        }
                        System.out.print("\nDe la lista anterior, ingrese el ID del método de pago que desea ver: ");
                        int idBuscar;
                        try {
                            idBuscar = Integer.parseInt(sc.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("ID inválido. Debe ser un número.");
                            pausa();
                            break;
                        }
                        MetodoDePago metodo = metodoController.obtenerMetodoPorId(idBuscar);
                        if (metodo != null && metodo.getUsuarioId() == usuario.getUsuarioId()) {
                            System.out.println("\n--- Detalles del Método de Pago ---");
                            System.out.println("ID: " + metodo.getMetodoId());
                            System.out.println("Tipo: " + metodo.getTipo());
                            System.out.println("Activo: " + (metodo.isActivo() ? "Sí" : "No"));
                            System.out.println("---------------------------------");
                        } else {
                            System.out.println("Método de pago no encontrado con ID " + idBuscar + " o no te pertenece.");
                        }
                        pausa();
                        break;

                    case 3: // Eliminar método de pago
                        limpiarpantalla();
                        System.out.println("------ ELIMINAR MÉTODO DE PAGO ------");
                        boolean tieneMetodosParaEliminar = listarMetodosDePagoUsuarioParaSeleccion(usuario,
                                "Selecciona un método de la siguiente lista para eliminar:");
                        if (!tieneMetodosParaEliminar) {
                            pausa();
                            break;
                        }
                        System.out.print("\nDe la lista anterior, ID del método a eliminar: ");
                        int idEliminar;
                         try {
                            idEliminar = Integer.parseInt(sc.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("ID inválido. Debe ser un número.");
                            pausa();
                            break;
                        }
                        MetodoDePago metodoEliminar = metodoController.obtenerMetodoPorId(idEliminar);
                        if (metodoEliminar != null && metodoEliminar.getUsuarioId() == usuario.getUsuarioId()) {
                            System.out.println("\nMétodo a eliminar: ID " + metodoEliminar.getMetodoId() + ", Tipo: "
                                    + metodoEliminar.getTipo() + ", Activo: " + (metodoEliminar.isActivo() ? "Sí" : "No"));
                            System.out.print("¿Estás seguro? (S/N): ");
                            if (sc.nextLine().trim().equalsIgnoreCase("S")) {
                                boolean eliminado = metodoController.eliminarMetodo(idEliminar);
                                System.out.println(
                                        eliminado ? "Método eliminado correctamente." : "No se pudo eliminar el método.");
                                if (!eliminado) {
                                    System.out.println(
                                            "Asegúrate de que no esté en uso. Si es de tipo 'Tarjeta', verifica que no tenga tarjetas asociadas o elimínalas primero desde la opción 5.");
                                }
                            } else {
                                System.out.println("Eliminación cancelada.");
                            }
                        } else {
                            System.out.println("Método no encontrado con ID " + idEliminar + " o no te pertenece.");
                        }
                        pausa();
                        break;

                    case 4: // Agregar nuevo método de pago (Tipo Tarjeta)
                        limpiarpantalla();
                        System.out.println("------ AGREGAR NUEVO MÉTODO DE PAGO (TIPO TARJETA) ------");

                        String tipoMetodoFijo = "tarjeta"; // El tipo es fijo
                        System.out.println("Se creará un nuevo método de pago de tipo: '" + tipoMetodoFijo + "'.");
                        System.out.print("Este método le permitirá asociar los detalles de una tarjeta más adelante.");
                        System.out.print("\n¿Desea continuar y agregar este método de pago? (S/N): ");
                        String confirmacion = sc.nextLine().trim();

                        if (confirmacion.equalsIgnoreCase("S")) {
                            MetodoDePago nuevoMetodo = new MetodoDePago();
                            nuevoMetodo.setUsuarioId(usuario.getUsuarioId());
                            nuevoMetodo.setTipo(tipoMetodoFijo); // Tipo fijado a "Tarjeta"
                            nuevoMetodo.setActivo(true);         // Por defecto activo

                            if (metodoController.agregarMetodo(nuevoMetodo)) {
                                System.out.println("\n¡Método de pago de tipo '" + tipoMetodoFijo + "' agregado exitosamente!");
                                // Asumimos que el ID se setea en nuevoMetodo por el controller si la operación fue exitosa y el ID es autogenerado.
                                // Si no, el usuario tendría que listarlos para ver el ID.
                                if (nuevoMetodo.getMetodoId() > 0) { // Chequeo opcional si el ID se actualiza
                                     System.out.println("ID del nuevo método: " + nuevoMetodo.getMetodoId());
                                }
                                System.out.println("Ahora puedes ir a 'Gestionar tarjetas de métodos de pago' (opción 5)");
                                System.out.println("para añadir los detalles específicos de la tarjeta a este método.");
                            } else {
                                System.out.println("\nError al agregar el método de pago.");
                                System.out.println("Es posible que haya ocurrido un problema interno o que ya exista un método de pago base similar.");
                            }
                        } else {
                            System.out.println("\nAgregar método de pago cancelado.");
                        }
                        pausa();
                        break;

                    case 5: // Gestionar tarjetas de métodos de pago
                        limpiarpantalla();
                        System.out.println("------ GESTIONAR TARJETAS DE UN MÉTODO DE PAGO ------");
                        
                        List<MetodoDePago> todosMetodosParaTarjetas = metodoController.listarMetodos();
                        List<MetodoDePago> metodosTarjetaUsuario;

                        if (todosMetodosParaTarjetas != null) {
                            metodosTarjetaUsuario = todosMetodosParaTarjetas.stream()
                                    .filter(m -> m.getUsuarioId() == usuario.getUsuarioId()
                                            && m.getTipo().equalsIgnoreCase("Tarjeta"))
                                    .collect(Collectors.toList());
                        } else {
                            metodosTarjetaUsuario = new ArrayList<>();
                        }

                        if (metodosTarjetaUsuario.isEmpty()) {
                            System.out.println("\nNo tienes métodos de pago de tipo 'Tarjeta' para gestionar.");
                            System.out.println("Puedes agregar uno desde la opción 4 del menú anterior.");
                            pausa();
                            break;
                        }
                        System.out.println("\nMétodos de pago de tipo 'Tarjeta' que te pertenecen:");
                        for (MetodoDePago mt : metodosTarjetaUsuario) {
                            System.out.println(
                                    "ID: " + mt.getMetodoId() + " (Activo: " + (mt.isActivo() ? "Sí" : "No") + ")");
                        }
                        System.out.print("\nIngresa el ID del método de pago 'Tarjeta' a gestionar (de la lista anterior): ");
                        int metodoId;
                        try {
                            metodoId = Integer.parseInt(sc.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("ID inválido. Debe ser un número.");
                            pausa();
                            break;
                        }

                        MetodoDePago metodoTarjetaSeleccionado = null;
                        for(MetodoDePago mt : metodosTarjetaUsuario){
                            if(mt.getMetodoId() == metodoId){
                                metodoTarjetaSeleccionado = mt;
                                break;
                            }
                        }

                        if (metodoTarjetaSeleccionado == null) {
                            System.out.println("El ID " + metodoId + " no corresponde a un método de pago de tipo 'Tarjeta' válido que te pertenezca.");
                            pausa();
                            break;
                        }
                        mostrarSubmenuTarjeta(metodoTarjetaSeleccionado.getMetodoId());
                        break;

                    case 0:
                        return;

                    default:
                        System.out.println("Opción no válida. Intenta de nuevo.");
                        pausa();
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingresa un número para la opción del menú.");
                pausa();
            } catch (Exception e) {
                System.err.println("Ocurrió un error inesperado en el Menú Métodos de Pago: " + e.getMessage());
                e.printStackTrace();
                pausa();
            }
        }
    }

    public static void agregarTarjetaSubmenu(int metodoPagoId) {
        System.out.println("\n---- Agregar Datos de Tarjeta ----");
        var tarjetaExistente = metodoController.obtenerTarjetaPorMetodo(metodoPagoId);
        if (tarjetaExistente != null) {
            System.out.println("Ya existe una tarjeta asociada a este método de pago (ID: " + metodoPagoId + ").");
            System.out.println("Si deseas agregar una nueva, primero elimina la existente o edítala.");
            return;
        }

        System.out.print("Nombre del titular: ");
        String titular = sc.nextLine().trim();
        if (titular.isEmpty()) { System.out.println("El titular no puede estar vacío."); return; }

        System.out.print("Número de tarjeta (completo): ");
        String numero = sc.nextLine().trim();
         if (numero.isEmpty()) { System.out.println("El número de tarjeta no puede estar vacío."); return; }


        System.out.print("Fecha de vencimiento (MM/AA): ");
        String vencimiento = sc.nextLine().trim();
         if (vencimiento.isEmpty()) { System.out.println("La fecha de vencimiento no puede estar vacía."); return; }


        System.out.print("CVV: ");
        String cvv = sc.nextLine().trim();
        if (cvv.isEmpty()) { System.out.println("El CVV no puede estar vacío."); return; }


        boolean tarjetaAgregada = metodoController.agregarTarjetaParaMetodo(
                metodoPagoId, titular, numero, vencimiento, cvv);

        if (tarjetaAgregada) {
            System.out.println("Tarjeta asociada correctamente al método de pago ID: " + metodoPagoId);
        } else {
            System.out.println("Error al asociar la tarjeta. Verifica los datos e inténtalo de nuevo.");
        }
    }

    public static void mostrarSubmenuTarjeta(int metodoPagoId) {
        while (true) {
            limpiarpantalla();
            System.out.println("---- GESTIÓN DE TARJETA (Para Método ID: " + metodoPagoId + ") ----");
            System.out.println("1. Ver datos de tarjeta");
            System.out.println("2. Agregar/Asociar tarjeta");
            System.out.println("3. Editar tarjeta");
            System.out.println("4. Eliminar tarjeta asociada");
            System.out.println("0. Volver al menú de métodos de pago");
            System.out.print("Elige una opción: ");
            String opcion = sc.nextLine().trim();

            switch (opcion) {
                case "1": // Ver datos de tarjeta
                    var tarjeta = metodoController.obtenerTarjetaPorMetodo(metodoPagoId);
                    if (tarjeta != null) {
                        System.out.println("\n--- Datos de la Tarjeta Asociada ---");
                        System.out.println("Titular: " + (tarjeta.getMarca() != null ? tarjeta.getMarca() : "N/D"));
                        System.out.println("Número: **** **** **** " + (tarjeta.getUltimosDigitos() != null ? tarjeta.getUltimosDigitos() : "XXXX"));
                        System.out.println("Vencimiento: " + (tarjeta.getFechaExpiracion() != null ? tarjeta.getFechaExpiracion() : "N/D"));
                        System.out.println("------------------------------------");
                    } else {
                        System.out.println("No hay ninguna tarjeta asociada a este método de pago.");
                    }
                    pausa();
                    break;
                case "2": // Agregar tarjeta
                    agregarTarjetaSubmenu(metodoPagoId);
                    pausa();
                    break;
                case "3": // Editar tarjeta
                    System.out.println("\n---- Editar Datos de Tarjeta ----");
                    System.out.println("Ingrese los nuevos valores. Escriba 'CANCELAR' en cualquier campo para abortar.");
                    System.out.println("Dejar un campo vacío y presionar ENTER lo enviará como vacío (podría borrar el dato existente o no ser modificado).");

                    var tarjetaInfoActual = metodoController.obtenerTarjetaPorMetodo(metodoPagoId);
                    if (tarjetaInfoActual == null) {
                        System.out.println("No hay tarjeta para editar. Usa la opción 'Agregar/Asociar tarjeta' primero.");
                        pausa();
                        break;
                    }

                    String titularActualDisplay = (tarjetaInfoActual.getMarca() != null ? tarjetaInfoActual.getMarca() : "No establecido");
                    System.out.print("Nuevo titular (Actual: " + titularActualDisplay + "): ");
                    String nuevoTitular = sc.nextLine().trim();
                    if (nuevoTitular.equalsIgnoreCase("CANCELAR")) { System.out.println("Edición cancelada."); pausa(); break; }

                    System.out.print("Nuevo número de tarjeta (completo) (Actual termina en: ...." + (tarjetaInfoActual.getUltimosDigitos() != null ? tarjetaInfoActual.getUltimosDigitos() : "XXXX") + "): ");
                    String nuevoNumero = sc.nextLine().trim();
                    if (nuevoNumero.equalsIgnoreCase("CANCELAR")) { System.out.println("Edición cancelada."); pausa(); break; }

                    String fechaActualDisplay = (tarjetaInfoActual.getFechaExpiracion() != null ? tarjetaInfoActual.getFechaExpiracion() : "No establecido");
                    System.out.print("Nueva fecha de vencimiento (MM/AA) (Actual: " + fechaActualDisplay + "): ");
                    String nuevaFecha = sc.nextLine().trim();
                    if (nuevaFecha.equalsIgnoreCase("CANCELAR")) { System.out.println("Edición cancelada."); pausa(); break; }

                    System.out.print("Nuevo CVV: ");
                    String nuevoCvv = sc.nextLine().trim();
                    if (nuevoCvv.equalsIgnoreCase("CANCELAR")) { System.out.println("Edición cancelada."); pausa(); break; }

                    boolean editada = metodoController.editarTarjeta(metodoPagoId, nuevoTitular, nuevoNumero, nuevaFecha, nuevoCvv);
                    System.out.println(editada ? "Solicitud de edición de tarjeta enviada." : "No se pudo enviar la solicitud de edición.");
                    if(editada) System.out.println("La actualización final dependerá de la validación de los datos y las reglas del sistema.");
                    pausa();
                    break;
                case "4": // Eliminar tarjeta asociada
                    var tarjetaParaEliminar = metodoController.obtenerTarjetaPorMetodo(metodoPagoId);
                    if (tarjetaParaEliminar == null) {
                        System.out.println("No hay tarjeta asociada para eliminar.");
                        pausa();
                        break;
                    }
                    System.out.println("Se eliminarán los datos de la tarjeta asociada al método de pago ID: " + metodoPagoId);
                    System.out.print("¿Estás seguro? (S/N): ");
                    if (sc.nextLine().trim().equalsIgnoreCase("S")) {
                        boolean eliminada = metodoController.eliminarTarjeta(metodoPagoId);
                        System.out.println(eliminada ? "Datos de la tarjeta eliminados correctamente." : "No se pudieron eliminar los datos de la tarjeta.");
                    } else {
                        System.out.println("Eliminación cancelada.");
                    }
                    pausa();
                    break;
                case "0":
                    return; 
                default:
                    System.out.println("Opción no válida.");
                    pausa();
            }
        }
    }
}