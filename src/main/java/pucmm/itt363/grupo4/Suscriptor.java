package pucmm.itt363.grupo4;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Suscriptor {

    public static void main(String[] args) {
        BaseDeDatos.initDatabase();
        SpringApplication.run(Suscriptor.class, args);
        iniciarMqtt();
    }

    @org.springframework.context.annotation.Bean
    public org.springframework.web.socket.server.standard.ServerEndpointExporter serverEndpointExporter() {
        return new org.springframework.web.socket.server.standard.ServerEndpointExporter();
    }

    private static void iniciarMqtt() {
        try {
            String clientId = "grupo4-suscriptor";
            MqttClient client = new MqttClient(Main.broker, clientId);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(Main.usuario);
            options.setPassword(Main.password.toCharArray());
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);

            client.setCallback(new SuscriptorCallback());

            client.connect(options);
            client.subscribe(Main.topicSub, Main.qos);

            System.out.println("Suscriptor conectado");
            System.out.println("Suscrito a: " + Main.topicSub);
            System.out.println("Esperando mensajes...");
        } catch (Exception e) {
            System.out.println("Error en suscriptor");
            e.printStackTrace();
        }
    }
}