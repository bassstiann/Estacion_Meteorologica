package pucmm.itt363.grupo4;

public class FormateadorLectura {

    public static String obtenerNombreSensor(String sensorId, String estacionId) {
        String s = sensorId.toLowerCase();
        if (s.equals("temperatura")) {
            if (estacionId.equalsIgnoreCase("estacion-1")) {
                return "Temp. 1";
            }
            if (estacionId.equalsIgnoreCase("estacion-2")) {
                return "Temp. 2";
            }
            if (estacionId.equalsIgnoreCase("estacion-3")) {
                return "Temp. 3";
            }
            return "Temp.";
        }
        if (s.equals("humedad")) {
            return "Humedad";
        }
        if (s.equals("presion")) {
            return "Presión";
        }
        if (s.equals("velocidad-viento")) {
            return "Viento";
        }
        if (s.equals("lluvia")) {
            return "Lluvia";
        }
        if (s.equals("gas")) {
            return "Gas";
        }
        return sensorId;
    }

    public static String obtenerValorConUnidad(double valor, String sensorId) {
        String s = sensorId.toLowerCase();
        if (s.equals("temperatura")) {
            return valor + " °C";
        }
        if (s.equals("humedad")) {
            return valor + " %";
        }
        if (s.equals("presion")) {
            return valor + " hPa";
        }
        if (s.equals("velocidad-viento")) {
            return valor + " km/h";
        }
        if (s.equals("lluvia")) {
            return valor + " mm";
        }
        if (s.equals("gas")) {
            return valor + " ppm";
        }
        return String.valueOf(valor);
    }

    public static String obtenerDescripcion(double valor, String sensorId) {
        String s = sensorId.toLowerCase();
        if (s.equals("temperatura")) {
            if (valor < 18) {
                return "Temperatura Baja";
            }
            if (valor <= 25) {
                return "Temperatura Normal";
            }
            return "Temperatura Alta";
        }
        if (s.equals("humedad")) {
            if (valor < 40) {
                return "Humedad Seca";
            }
            if (valor <= 70) {
                return "Humedad Normal";
            }
            return "Humedad Alta";
        }
        if (s.equals("presion")) {
            if (valor < 1010) {
                return "Presión Baja";
            }
            if (valor <= 1015) {
                return "Presión Estable";
            }
            return "Presión Alta";
        }
        if (s.equals("velocidad-viento")) {
            if (valor < 20) {
                return "Viento Leve";
            }
            if (valor <= 50) {
                return "Viento Moderado";
            }
            return "Viento Fuerte";
        }
        if (s.equals("lluvia")) {
            if (valor == 0) {
                return "Sin Lluvia";
            }
            if (valor <= 5) {
                return "Lluvia Ligera";
            }
            return "Lluvia Fuerte";
        }
        if (s.equals("gas")) {
            if (valor < 300) {
                return "Nivel Normal";
            }
            if (valor <= 500) {
                return "Nivel Moderado";
            }
            return "Nivel Alto";
        }
        return "Lectura Normal";
    }
}
