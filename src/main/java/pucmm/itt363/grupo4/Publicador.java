package pucmm.itt363.grupo4;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Random;

public class Publicador {

    public static void main(String[] args) {
        try {
            String clientId = "grupo4-publicador-" + System.currentTimeMillis();

            MqttClient client = new MqttClient(Main.broker, clientId);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(Main.usuario);
            options.setPassword(Main.password.toCharArray());
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);

            client.connect(options);

            String[] estaciones = {"estacion-1", "estacion-2", "estacion-3"};
            Random random = new Random();

            while (true) {
                for (String estacion : estaciones) {

                    Sensor temperatura = new Sensor(estacion, "temperatura", String.valueOf(15 + random.nextInt(20)));
                    Sensor humedad = new Sensor(estacion, "humedad", String.valueOf(30 + random.nextInt(70)));
                    Sensor presion = new Sensor(estacion, "presion", String.valueOf(980 + random.nextInt(50)));
                    Sensor viento = new Sensor(estacion, "velocidad-viento", String.valueOf(random.nextInt(80)));
                    Sensor lluvia = new Sensor(estacion, "lluvia", String.valueOf(random.nextInt(20)));

                    Sensor[] sensores = {temperatura, humedad, presion, viento, lluvia};

                    for (Sensor sensor : sensores) {
                        String topic = sensor.getTopic();
                        String json = sensor.getJson();

                        MqttMessage message = new MqttMessage(json.getBytes());
                        message.setQos(Main.qos);

                        client.publish(topic, message);
                        System.out.println("Enviando Información Topic: " + topic);
                    }
                }

                Thread.sleep(1000);
            }

        } catch (Exception e) {
            System.out.println("Error en publicador");
            e.printStackTrace();
        }
    }
}