import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class GestionRecetas {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("1. Mostrar todas las recetas");
            System.out.println("2. Buscar receta por id");
            System.out.println("3. Buscar receta por nombre");
            System.out.println("4. Añadir receta");
            System.out.println("5. Añadir receta con transacción");
            System.out.println("6. Modificar tiempo y raciones");
            System.out.println("7. Eliminar receta");
            System.out.println("0. Salir");
            System.out.println("Elige una opción: ");

            opcion = sc.nextInt();
            sc.nextLine();

            if (opcion == 1) {
                mostrarTodasLasRecetas();

            } else if (opcion == 2) {
                System.out.println("Introduce el id de la receta que quieres buscar:");
                int id = sc.nextInt();
                sc.nextLine();

                buscarRecetaPorId(id);

            } else if (opcion == 3) {
                System.out.println("Introduce el nombre de la receta que quieras buscar:");
                String nombre = sc.nextLine();

                buscarRecetaPorNombre(nombre);

            } else if (opcion == 4) {
                System.out.println("Introduce el nombre de la nueva receta");
                String nombre = sc.nextLine();

                System.out.println("Introduce la categoría de la nueva receta:");
                String categoria = sc.nextLine();

                System.out.println("Introduce el tiempo de preparación de la nueva receta:");
                int tiempo = sc.nextInt();
                sc.nextLine();

                System.out.println("Introduce la dificultad de la nueva receta:");
                String dificultad = sc.nextLine();

                System.out.println("Introduce las raciones de la nueva receta:");
                int raciones = sc.nextInt();
                sc.nextLine();

                Receta receta = new Receta(nombre, categoria, tiempo, dificultad, raciones);
                añadirReceta(receta);

            } else if (opcion == 5) {
                System.out.println("Introduce el nombre de la nueva receta:");
                String nombre = sc.nextLine();

                System.out.println("Introduce la categoría de la nueva receta:");
                String categoria = sc.nextLine();

                System.out.println("Introduce el tiempo de preparación de la nueva receta:");
                int tiempo = sc.nextInt();
                sc.nextLine();

                System.out.println("Introduce la dificultad de la nueva receta:");
                String dificultad = sc.nextLine();

                System.out.println("Introduce las raciones de la nueva receta:");
                int raciones = sc.nextInt();
                sc.nextLine();

                Receta receta = new Receta(nombre, categoria, tiempo, dificultad, raciones);
                añadirRecetaConTransaccion(receta);

            } else if (opcion == 6) {
                System.out.println("Introduce el id de la receta que quieras modificar:");
                int id = sc.nextInt();

                System.out.println("Introduce el nuevo tiempo de preparación:");
                int tiempo = sc.nextInt();

                System.out.println("Introduce las nuevas raciones:");
                int raciones = sc.nextInt();
                sc.nextLine();

                modificarTiempoYRaciones(id, tiempo, raciones);

            } else if (opcion == 7) {
                System.out.println("Introduce el id de la receta que quieras eliminar:");
                int id = sc.nextInt();
                sc.nextLine();

                eliminarReceta(id);

            } else if (opcion == 0) {
                System.out.println("Has salido");
            } else {
                System.out.println("Pon una opción válida del menu");
            }
        }

        sc.close();
    }

    public static void mostrarTodasLasRecetas() {
        String sql = "SELECT id, nombre, categoria, tiempo_preparacion, dificultad, raciones FROM recetas";
        try (Connection con = ConexionBD.getConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println("Id: " + rs.getInt("id")
                        + " - Nombre: " + rs.getString("nombre")
                        + " - Categoría: " + rs.getString("categoria")
                        + " - Tiempo: " + rs.getInt("tiempo_preparacion") + " min"
                        + " - Dificultad: " + rs.getString("dificultad")
                        + " - Raciones: " + rs.getInt("raciones"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void buscarRecetaPorId(int id) {
        String sql = """
                SELECT id, nombre, categoria, tiempo_preparacion, dificultad, raciones
                FROM recetas
                WHERE id = ?
                """;
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Id: " + rs.getInt("id")
                            + " - Nombre: " + rs.getString("nombre")
                            + " - Categoría: " + rs.getString("categoria")
                            + " - Tiempo: " + rs.getInt("tiempo_preparacion") + " min"
                            + " - Dificultad: " + rs.getString("dificultad")
                            + " - Raciones: " + rs.getInt("raciones"));
                } else {
                    System.out.println("No se ha encontrado la receta");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void añadirReceta(Receta receta) {
        String sql = """
                INSERT INTO recetas (nombre, categoria, tiempo_preparacion, dificultad, raciones)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, receta.getNombre());
            pstmt.setString(2, receta.getCategoria());
            pstmt.setInt(3, receta.getTiempoPreparacion());
            pstmt.setString(4, receta.getDificultad());
            pstmt.setInt(5, receta.getRaciones());

            int filas = pstmt.executeUpdate();
            System.out.println("Filas insertadas: " + filas);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void modificarTiempoYRaciones(int id, int nuevoTiempo, int nuevasRaciones) {
        String sql = """
                UPDATE recetas
                SET tiempo_preparacion = ?, raciones = ?
                WHERE id = ?
                """;
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, nuevoTiempo);
            pstmt.setInt(2, nuevasRaciones);
            pstmt.setInt(3, id);

            int filas = pstmt.executeUpdate();
            System.out.println("Filas actualizadas: " + filas);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void eliminarReceta(int id) {
        String sql = """
                DELETE FROM recetas
                WHERE id = ?
                """;
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            System.out.println("Filas eliminadas: " + filas);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void añadirRecetaConTransaccion(Receta receta) {
        String sqlInsertar = """
                INSERT INTO recetas (nombre, categoria, tiempo_preparacion, dificultad, raciones)
                VALUES (?, ?, ?, ?, ?)
                """;
        String sqlActualizarEstadisticas = """
                UPDATE estadisticas
                SET total_recetas = total_recetas + 1
                WHERE id = 1
                """;

        Connection con = null;
        PreparedStatement pstmtInsertar = null;
        PreparedStatement pstmtEstadisticas = null;

        try {
            con = ConexionBD.getConexion();
            con.setAutoCommit(false);

            pstmtInsertar = con.prepareStatement(sqlInsertar);
            pstmtInsertar.setString(1, receta.getNombre());
            pstmtInsertar.setString(2, receta.getCategoria());
            pstmtInsertar.setInt(3, receta.getTiempoPreparacion());
            pstmtInsertar.setString(4, receta.getDificultad());
            pstmtInsertar.setInt(5, receta.getRaciones());
            pstmtInsertar.executeUpdate();

            pstmtEstadisticas = con.prepareStatement(sqlActualizarEstadisticas);
            pstmtEstadisticas.executeUpdate();

            con.commit();
            System.out.println("Transacción confirmada.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            try {
                if (con != null) {
                    con.rollback();
                    System.out.println("Transacción cancelada.");
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } finally {
            try {
                if (pstmtInsertar != null) {
                    pstmtInsertar.close();
                }
                if (pstmtEstadisticas != null) {
                    pstmtEstadisticas.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void buscarRecetaPorNombre(String nombre) {
        String sql = "{CALL buscarReceta(?)}";
        try (Connection con = ConexionBD.getConexion();
             CallableStatement cstmt = con.prepareCall(sql)) {

            cstmt.setString(1, nombre);
            try (ResultSet rs = cstmt.executeQuery()) {
                boolean encontrada = false;
                while (rs.next()) {
                    encontrada = true;
                    System.out.println("Id: " + rs.getInt("id")
                            + " - Nombre: " + rs.getString("nombre")
                            + " - Categoría: " + rs.getString("categoria")
                            + " - Tiempo: " + rs.getInt("tiempo_preparacion") + " min"
                            + " - Dificultad: " + rs.getString("dificultad")
                            + " - Raciones: " + rs.getInt("raciones"));
                }
                if (!encontrada) {
                    System.out.println("Receta no encontrada.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}