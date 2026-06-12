package pucmm.itt363.grupo4;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/ws")
public class WebSocketServidor {

    private static final Set<Session> sesiones = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void alAbrir(Session sesion) {
        sesiones.add(sesion);
    }

    @OnClose
    public void alCerrar(Session sesion) {
        sesiones.remove(sesion);
    }

    @OnMessage
    public void alRecibir(String mensaje, Session sesion) {
    }

    public static void enviarATodos(String mensaje) {
        synchronized (sesiones) {
            for (Session sesion : sesiones) {
                try {
                    if (sesion.isOpen()) {
                        sesion.getBasicRemote().sendText(mensaje);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
