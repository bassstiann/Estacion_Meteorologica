package pucmm.itt363.grupo4;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Sensor {

    public String estacionId;
    public String nombre;
    public String valor;
    public String fecha;

    public Sensor(String estacionId, String nombre, String valor) {
        this.estacionId = estacionId;
        this.nombre = nombre;
        this.valor = valor;
        this.fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public String getTopic() {
        return Main.topicBase + "/" + estacionId + "/sensores/" + nombre;
    }

    public String getJson() {
        return "{\"valor\":\"" + valor + "\",\"fecha\":\"" + fecha + "\"}";
    }
}