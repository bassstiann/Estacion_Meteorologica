package pucmm.itt363.grupo4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class BaseDeDatos {

    private static String url = "jdbc:postgresql://192.168.34.194:5432/estacion_meteorologica";
    private static String usuario = "clima_user";
    private static String clave = "sebastianybonnie12";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, usuario, clave);
    }

    public static void initDatabase() {
        System.out.println("Conectando e inicializando tablas en: " + url);
        
        String tablaEstacion = "CREATE TABLE IF NOT EXISTS estacion (" +
                "id VARCHAR(50) PRIMARY KEY" +
                ");";

        String tablaSensor = "CREATE TABLE IF NOT EXISTS sensor (" +
                "id VARCHAR(50) PRIMARY KEY," +
                "unidad VARCHAR(20)" +
                ");";

        String tablaLectura = "CREATE TABLE IF NOT EXISTS lectura (" +
                "id BIGSERIAL PRIMARY KEY," +
                "estacion_id VARCHAR(50) REFERENCES estacion(id)," +
                "sensor_id VARCHAR(50) REFERENCES sensor(id)," +
                "valor DOUBLE PRECISION," +
                "fecha TIMESTAMP" +
                ");";

        String agregarSensore1 = "INSERT INTO sensor (id, unidad) VALUES ('temperatura', '°C') ON CONFLICT (id) DO NOTHING;";
        String agregarSensore2 = "INSERT INTO sensor (id, unidad) VALUES ('humedad', '%') ON CONFLICT (id) DO NOTHING;";
        String agregarSensore3 = "INSERT INTO sensor (id, unidad) VALUES ('presion', 'hPa') ON CONFLICT (id) DO NOTHING;";
        String agregarSensore4 = "INSERT INTO sensor (id, unidad) VALUES ('velocidad-viento', 'km/h') ON CONFLICT (id) DO NOTHING;";
        String agregarSensore5 = "INSERT INTO sensor (id, unidad) VALUES ('lluvia', 'mm') ON CONFLICT (id) DO NOTHING;";

        try {
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            
            stmt.execute(tablaEstacion);
            stmt.execute(tablaSensor);
            stmt.execute(tablaLectura);
            
            stmt.execute(agregarSensore1);
            stmt.execute(agregarSensore2);
            stmt.execute(agregarSensore3);
            stmt.execute(agregarSensore4);
            stmt.execute(agregarSensore5);
            
            stmt.close();
            con.close();
            
            System.out.println("Tablas listas para usar!");
            
        } catch (SQLException e) {
            System.out.println("Error al crear las tablas en la base de datos.");
            e.printStackTrace();
        }
    }

    private static boolean existeEstacion(Connection con, String idEstacion) throws SQLException {
        String query = "SELECT id FROM estacion WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, idEstacion);
        ResultSet rs = ps.executeQuery();
        boolean existe = rs.next();
        rs.close();
        ps.close();
        return existe;
    }

    private static boolean existeSensor(Connection con, String idSensor) throws SQLException {
        String query = "SELECT id FROM sensor WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, idSensor);
        ResultSet rs = ps.executeQuery();
        boolean existe = rs.next();
        rs.close();
        ps.close();
        return existe;
    }

    public static void guardarLectura(String idEstacion, String idSensor, double valor, String fechaTexto) {
        try {
            Connection con = getConnection();

            if (!existeEstacion(con, idEstacion)) {
                String insertEst = "INSERT INTO estacion (id) VALUES (?)";
                PreparedStatement psEst = con.prepareStatement(insertEst);
                psEst.setString(1, idEstacion);
                psEst.executeUpdate();
                psEst.close();
                System.out.println("Nueva estacion agregada: " + idEstacion);
            }

            if (!existeSensor(con, idSensor)) {
                String insertSens = "INSERT INTO sensor (id, unidad) VALUES (?, ?)";
                PreparedStatement psSens = con.prepareStatement(insertSens);
                psSens.setString(1, idSensor);
                psSens.setString(2, obtenerUnidad(idSensor));
                psSens.executeUpdate();
                psSens.close();
                System.out.println("Nuevo sensor agregado: " + idSensor);
            }

            String insertLect = "INSERT INTO lectura (estacion_id, sensor_id, valor, fecha) VALUES (?, ?, ?, ?)";
            PreparedStatement psLect = con.prepareStatement(insertLect);
            psLect.setString(1, idEstacion);
            psLect.setString(2, idSensor);
            psLect.setDouble(3, valor);

            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date fechaParseada = formato.parse(fechaTexto);
            java.sql.Timestamp fechaSql = new java.sql.Timestamp(fechaParseada.getTime());
            psLect.setTimestamp(4, fechaSql);

            psLect.executeUpdate();
            psLect.close();
            con.close();

            System.out.println("Lectura guardada -> Estacion: " + idEstacion + ", Sensor: " + idSensor + ", Valor: " + valor);

        } catch (Exception e) {
            System.out.println("Hubo un error al guardar la lectura de " + idSensor + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String obtenerUnidad(String nombreSensor) {
        if (nombreSensor.equalsIgnoreCase("temperatura")) {
            return "°C";
        } else if (nombreSensor.equalsIgnoreCase("humedad")) {
            return "%";
        } else if (nombreSensor.equalsIgnoreCase("presion")) {
            return "hPa";
        } else if (nombreSensor.equalsIgnoreCase("velocidad-viento")) {
            return "km/h";
        } else if (nombreSensor.equalsIgnoreCase("lluvia")) {
            return "mm";
        } else {
            return "";
        }
    }

    public static java.util.List<String[]> obtenerUltimasLecturas() {
        java.util.List<String[]> lista = new java.util.ArrayList<>();
        String sql = "SELECT l.id, l.estacion_id, l.sensor_id, l.valor, s.unidad, l.fecha " +
                     "FROM lectura l " +
                     "JOIN sensor s ON l.sensor_id = s.id " +
                     "ORDER BY l.fecha DESC";
        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new String[]{
                    rs.getString("id"),
                    rs.getString("estacion_id"),
                    rs.getString("sensor_id"),
                    rs.getString("valor"),
                    rs.getString("unidad"),
                    rs.getString("fecha")
                });
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
