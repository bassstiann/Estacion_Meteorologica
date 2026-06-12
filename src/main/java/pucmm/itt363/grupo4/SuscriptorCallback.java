package pucmm.itt363.grupo4;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SuscriptorCallback implements MqttCallback {

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Se perdió la conexión");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String texto = new String(message.getPayload());

        System.out.println("--------------- Mensaje Recibido [" + topic + "] -----------------");
        System.out.println("Mensaje Recibido: " + texto);

        try {
            String[] parts = topic.split("/");
            if (parts.length >= 6 && parts[2].equals("estacion") && parts[4].equals("sensores")) {
                String estacionId = parts[3];
                String sensorNombre = parts[5];

                JsonObject jsonObject = JsonParser.parseString(texto).getAsJsonObject();
                double valor = jsonObject.get("valor").getAsDouble();
                String fecha = jsonObject.get("fecha").getAsString();

                long id = BaseDeDatos.guardarLectura(estacionId, sensorNombre, valor, fecha);
                if (id > 0) {
                    JsonObject wsMsg = new JsonObject();
                    wsMsg.addProperty("id", id);
                    wsMsg.addProperty("fecha", fecha);
                    wsMsg.addProperty("nombreSensor", FormateadorLectura.obtenerNombreSensor(sensorNombre, estacionId));
                    wsMsg.addProperty("valorConUnidad", FormateadorLectura.obtenerValorConUnidad(valor, sensorNombre));
                    wsMsg.addProperty("descripcion", FormateadorLectura.obtenerDescripcion(valor, sensorNombre));
                    WebSocketServidor.enviarATodos(wsMsg.toString());
                }
            } else {
                System.out.println("El tópico no cumple con el formato esperado. No se persistió.");
            }
        } catch (Exception e) {
            System.err.println("Error procesando o persistiendo mensaje MQTT:");
            e.printStackTrace();
        }

        System.out.println("--------------- Fin Mensaje Recibido [" + topic + "] -----------------");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }
}