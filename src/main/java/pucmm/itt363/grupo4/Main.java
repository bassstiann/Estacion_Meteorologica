package pucmm.itt363.grupo4;

public class Main {

    public static String broker = "tcp://mqtt.eict.ce.pucmm.edu.do:1883";
    public static String usuario = "itt363-grupo4";
    public static String password = "Kshq8UuveRLC";

    public static String topicBase = "/itt363-grupo4/estacion";
    public static String topicSub = "/itt363-grupo4/estacion/+/sensores/#";

    public static int qos = 0;

    public static void main(String[] args) {
        System.out.println("Proyecto MQTT Grupo 4");
    }
}