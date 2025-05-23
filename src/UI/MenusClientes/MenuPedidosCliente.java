package UI.MenusClientes;

import Controller.Cliente.PedidosClienteController;
import Controller.Cliente.ProductosClienteController;
import Models.Pedidos;
import Models.DetallesPedido;
import Models.Productos;
import Models.Usuarios;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuPedidosCliente {
    private static final Scanner sc = new Scanner(System.in);

    public static void limpiarpantalla() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("bash", "-c", "clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; ++i) System.out.println();
        }
    }

    public static void pausa() {
        System.out.println("\nPresiona ENTER para continuar...");
        sc.nextLine();
    }

    private static void mostrarProductosParaSeleccion(List<Productos> productos) {
        System.out.println("--- Productos Disponibles (Página Actual) ---");
        if (productos.isEmpty()) {
            return;
        }
        for (Productos p : productos) {
            System.out.println(String.format("ID: %-5d | Nombre: %-30s | Precio: $%-8.2f | Stock: %d",
                    p.getProductoId(),
                    p.getNombre(),
                    p.getPrecio(),
                    p.getStock()));
        }
        System.out.println("-------------------------------------------");
    }

    private static void realizarPedidoUI(PedidosClienteController pedidosController, ProductosClienteController productosController, Usuarios usuarioLogueado) {
        limpiarpantalla();
        System.out.println("------ REALIZAR NUEVO PEDIDO ------");

        List<DetallesPedido> itemsParaEstePedido = new ArrayList<>();
        boolean seguirAgregandoItems = true;

        while (seguirAgregandoItems) {
            System.out.println("\nSeleccione un producto para agregar al pedido:");

            int productosPorPagina = 10;
            int offsetActual = 0;
            boolean quiereVerMasPaginas = true;
            boolean algunProductoMostrado = false;

            while (quiereVerMasPaginas) {
                List<Productos> paginaDeProductos = productosController.listarProductosPaginados(productosPorPagina, offsetActual);

                if (paginaDeProductos.isEmpty()) {
                    if (offsetActual == 0) {
                        System.out.println("No hay productos disponibles en el catálogo para agregar.");
                        pausa();
                        return;
                    } else {
                        System.out.println("--- No hay más productos para mostrar ---");
                    }
                    quiereVerMasPaginas = false;
                } else {
                    mostrarProductosParaSeleccion(paginaDeProductos);
                    algunProductoMostrado = true;
                    offsetActual += paginaDeProductos.size();

                    if (paginaDeProductos.size() == productosPorPagina) {
                        System.out.print("Ver más productos (S) / Ingresar ID de producto directamente (cualquier otra tecla o ID): ");
                        String respuesta = sc.nextLine().trim();
                        if (!respuesta.equalsIgnoreCase("S")) {
                            quiereVerMasPaginas = false;
                        }
                    } else {
                        System.out.println("--- Fin de la lista de productos ---");
                        quiereVerMasPaginas = false;
                    }
                }
            }

            if (!algunProductoMostrado && itemsParaEstePedido.isEmpty()) {
                // Ya no es necesario hacer nada aquí si no se mostraron productos
            }

            System.out.print("\nIngrese el ID del producto a agregar (o 0 para finalizar este pedido): ");
            int idProductoSeleccionado;
            try {
                idProductoSeleccionado = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ID de producto inválido. Intente de nuevo.");
                continue;
            }

            if (idProductoSeleccionado == 0) {
                seguirAgregandoItems = false;
                continue;
            }

            Productos productoInfo = productosController.obtenerProductoPorId(idProductoSeleccionado);
            if (productoInfo == null) {
                System.out.println("Producto con ID " + idProductoSeleccionado + " no encontrado. Intente de nuevo.");
                continue;
            }

            System.out.print("Cantidad para '" + productoInfo.getNombre() + "' (Stock: " + productoInfo.getStock() + "): ");
            int cantidad;
            try {
                cantidad = Integer.parseInt(sc.nextLine());
                if (cantidad <= 0) {
                    System.out.println("La cantidad debe ser un número positivo.");
                    continue;
                }
                if (cantidad > productoInfo.getStock()) {
                    System.out.println("No hay suficiente stock. Solo hay " + productoInfo.getStock() + " unidades.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Cantidad inválida.");
                continue;
            }

            double precioUnitario = productoInfo.getPrecio();
            DetallesPedido nuevoItem = new DetallesPedido();
            nuevoItem.setProductoId(productoInfo.getProductoId());
            nuevoItem.setCantidad(cantidad);
            nuevoItem.setPrecio(precioUnitario);
            itemsParaEstePedido.add(nuevoItem);

            System.out.println("'" + productoInfo.getNombre() + "' (" + cantidad + " uds) agregado al pedido actual.");
            System.out.println("--------------------------------------------------");

            System.out.print("¿Desea agregar OTRO producto a este pedido? (S/N): ");
            if (!sc.nextLine().trim().equalsIgnoreCase("S")) {
                seguirAgregandoItems = false;
            }
        }

        if (itemsParaEstePedido.isEmpty()) {
            System.out.println("Pedido vacío. No se ha creado ningún pedido.");
        } else {
            Pedidos pedidoAGuardar = new Pedidos();
            pedidoAGuardar.setUsuarioId(usuarioLogueado.getUsuarioId());
            pedidoAGuardar.setFechaPedido(new Timestamp(System.currentTimeMillis()));
            pedidoAGuardar.setEstado("pendiente");

            System.out.println("\n------ RESUMEN DEL PEDIDO ------");
            double totalGeneral = 0;
            for(DetallesPedido item : itemsParaEstePedido) {
                Productos pInfo = productosController.obtenerProductoPorId(item.getProductoId());
                String nombreProd = (pInfo != null) ? pInfo.getNombre() : "ID "+item.getProductoId();
                double subtotal = item.getCantidad() * item.getPrecio();
                totalGeneral += subtotal;
                System.out.println(String.format("- %s (Cant: %d, P.Unit: $%.2f, Subt: $%.2f)",
                        nombreProd, item.getCantidad(), item.getPrecio(), subtotal));
            }
            System.out.println("---------------------------------");
            System.out.println(String.format("TOTAL DEL PEDIDO: $%.2f", totalGeneral));
            System.out.println("---------------------------------");

            System.out.print("¿Confirmar y crear este pedido? (S/N): ");
            if (sc.nextLine().trim().equalsIgnoreCase("S")) {
                if (pedidosController.agregarPedido(pedidoAGuardar, itemsParaEstePedido)) {
                    System.out.println("Pedido creado exitosamente.");
                } else {
                    System.out.println("Error al crear el pedido.");
                }
            } else {
                System.out.println("Creación de pedido cancelada.");
            }
        }
        pausa();
    }

    private static void mostrarListaPedidosUsuario(PedidosClienteController pedidosController, int usuarioId) {
        List<Pedidos> pedidos = pedidosController.listarPedidosPorUsuario(usuarioId);
        if (pedidos.isEmpty()) {
            System.out.println("No tienes pedidos registrados.");
        } else {
            System.out.println("------ MIS PEDIDOS ------");
            for (Pedidos p : pedidos) {
                System.out.println("Pedido ID: " + p.getPedidoId() +
                        " | Fecha: " + p.getFechaPedido() +
                        " | Estado: " + p.getEstado());
            }
            System.out.println("-------------------------");
        }
    }

    public static void mostrarMenuPedidos(PedidosClienteController pedidosController, ProductosClienteController productosController, Usuarios usuario) {
        while (true) {
            limpiarpantalla();
            System.out.println("------ MENÚ PEDIDOS ------");
            System.out.println("1. Ver mis pedidos");
            System.out.println("2. Ver detalle de un pedido");
            System.out.println("3. Realizar pedido");
            System.out.println("4. Eliminar pedido");
            System.out.println("0. Regresar al menú principal");
            System.out.print("\nElige una opción: ");
            try {
                int opc = Integer.parseInt(sc.nextLine());
                switch (opc) {
                    case 1:
                        limpiarpantalla();
                        mostrarListaPedidosUsuario(pedidosController, usuario.getUsuarioId());
                        pausa();
                        break;
                    case 2:
                        limpiarpantalla();
                        System.out.println("--- VER DETALLE DE PEDIDO ---");
                        List<Pedidos> pedidosParaDetalle = pedidosController.listarPedidosPorUsuario(usuario.getUsuarioId());
                        if (pedidosParaDetalle.isEmpty()) {
                            System.out.println("No tienes pedidos para ver detalles.");
                        } else {
                            mostrarListaPedidosUsuario(pedidosController, usuario.getUsuarioId()); // Muestra la lista
                            System.out.print("\nIngresa el ID del pedido que deseas ver: ");
                            int idPedido;
                            try {
                                idPedido = Integer.parseInt(sc.nextLine());
                                Pedidos pedido = pedidosController.obtenerPedidoPorId(idPedido);
                                if (pedido != null && pedido.getUsuarioId() == usuario.getUsuarioId()) {
                                    limpiarpantalla(); // Limpiar antes de mostrar detalles
                                    System.out.println("--- DETALLE DEL PEDIDO ID: " + idPedido + " ---");
                                    pedidosController.mostrarPedidoConDetalles(idPedido);
                                } else if (pedido != null && pedido.getUsuarioId() != usuario.getUsuarioId()) {
                                    System.out.println("Este pedido no te pertenece.");
                                } else {
                                    System.out.println("Pedido no encontrado.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("ID de pedido inválido.");
                            }
                        }
                        pausa();
                        break;
                    case 3:
                        realizarPedidoUI(pedidosController, productosController, usuario);
                        break;
                    case 4:
                        limpiarpantalla();
                        System.out.println("--- ELIMINAR PEDIDO ---");
                        List<Pedidos> pedidosParaEliminar = pedidosController.listarPedidosPorUsuario(usuario.getUsuarioId());
                        if (pedidosParaEliminar.isEmpty()) {
                            System.out.println("No tienes pedidos para eliminar.");
                        } else {
                            mostrarListaPedidosUsuario(pedidosController, usuario.getUsuarioId()); // Muestra la lista
                            System.out.print("\nID del pedido a eliminar: ");
                            int eliminarId;
                            try {
                                eliminarId = Integer.parseInt(sc.nextLine());
                                Pedidos pedidoAEliminar = pedidosController.obtenerPedidoPorId(eliminarId);
                                if (pedidoAEliminar != null && pedidoAEliminar.getUsuarioId() == usuario.getUsuarioId()) {
                                    System.out.print("¿Estás seguro que deseas eliminar el Pedido ID: " + eliminarId + "? (S/N): ");
                                    if (sc.nextLine().trim().equalsIgnoreCase("S")) {
                                        if (pedidosController.eliminarPedido(eliminarId)) {
                                            System.out.println("Pedido eliminado correctamente.");
                                        } else {
                                            System.out.println("No se pudo eliminar el pedido.");
                                            System.out.println("(Asegúrate de que el pedido exista y que no haya problemas al eliminar sus detalles).");
                                        }
                                    } else {
                                        System.out.println("Eliminación cancelada.");
                                    }
                                } else if (pedidoAEliminar != null && pedidoAEliminar.getUsuarioId() != usuario.getUsuarioId()){
                                    System.out.println("Este pedido no te pertenece y no puedes eliminarlo.");
                                } else {
                                    System.out.println("Pedido no encontrado para eliminar.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("ID de pedido inválido.");
                            }
                        }
                        pausa();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Opción no válida.");
                        pausa();
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingresa un número.");
                pausa();
            }
        }
    }
}