package pucmm.itt363.grupo4;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ServidorWeb {

    @GetMapping("/")
    public String home() {
        List<String[]> lecturas = BaseDeDatos.obtenerUltimasLecturas();
        
        List<String[]> temperatura = new ArrayList<>();
        List<String[]> humedad = new ArrayList<>();
        List<String[]> presion = new ArrayList<>();
        List<String[]> viento = new ArrayList<>();
        List<String[]> lluvia = new ArrayList<>();

        for (String[] fila : lecturas) {
            String sensor = fila[2].toLowerCase();
            if (sensor.equals("temperatura")) {
                temperatura.add(fila);
            } else if (sensor.equals("humedad")) {
                humedad.add(fila);
            } else if (sensor.equals("presion")) {
                presion.add(fila);
            } else if (sensor.equals("velocidad-viento")) {
                viento.add(fila);
            } else if (sensor.equals("lluvia")) {
                lluvia.add(fila);
            }
        }

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset='utf-8'>");
        html.append("<title>Proyecto Estación Meteorológica</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; background-color: #ffffff; color: #000000; padding: 20px; }");
        html.append("h1 { text-align: center; color: #333333; }");
        html.append(".tab-menu { text-align: center; margin-bottom: 20px; }");
        html.append(".tab-btn { padding: 10px 15px; font-size: 14px; margin: 5px; cursor: pointer; }");
        html.append(".tab-btn.active { font-weight: bold; background-color: #cccccc; }");
        html.append(".tab-content { display: none; margin: 0 auto; width: 90%; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 10px; }");
        html.append("th, td { border: 1px solid #000000; padding: 8px; text-align: left; }");
        html.append("th { background-color: #f2f2f2; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<h1>Estación Meteorológica - Grupo 4</h1>");
        
        html.append("<div class='tab-menu'>");
        html.append("<button class='tab-btn active' onclick=\"verTab(event, 'sec-temperatura')\">Temperatura</button>");
        html.append("<button class='tab-btn' onclick=\"verTab(event, 'sec-humedad')\">Humedad</button>");
        html.append("<button class='tab-btn' onclick=\"verTab(event, 'sec-presion')\">Presión</button>");
        html.append("<button class='tab-btn' onclick=\"verTab(event, 'sec-viento')\">Viento</button>");
        html.append("<button class='tab-btn' onclick=\"verTab(event, 'sec-lluvia')\">Lluvia</button>");
        html.append("</div>");

        agregarSeccionSensor(html, "temperatura", "Lecturas de Temperatura", temperatura, "°C", true);
        agregarSeccionSensor(html, "humedad", "Lecturas de Humedad", humedad, "%", false);
        agregarSeccionSensor(html, "presion", "Lecturas de Presión", presion, "hPa", false);
        agregarSeccionSensor(html, "viento", "Lecturas de Velocidad del Viento", viento, "km/h", false);
        agregarSeccionSensor(html, "lluvia", "Lecturas de Lluvia", lluvia, "mm", false);
        
        html.append("<script>");
        html.append("function verTab(evt, nombreTab) {");
        html.append("    var divs = document.getElementsByClassName('tab-content');");
        html.append("    for (var i = 0; i < divs.length; i++) {");
        html.append("        divs[i].style.display = 'none';");
        html.append("    }");
        html.append("    var btns = document.getElementsByClassName('tab-btn');");
        html.append("    for (var i = 0; i < btns.length; i++) {");
        html.append("        btns[i].classList.remove('active');");
        html.append("    }");
        html.append("    document.getElementById(nombreTab).style.display = 'block';");
        html.append("    evt.currentTarget.classList.add('active');");
        html.append("}");
        html.append("</script>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    private void agregarSeccionSensor(StringBuilder html, String id, String titulo, List<String[]> datos, String unidad, boolean activo) {
        String style = activo ? "block" : "none";
        html.append("<div id='sec-").append(id).append("' class='tab-content' style='display: ").append(style).append(";'>");
        html.append("<h2>").append(titulo).append(" (").append(unidad).append(")</h2>");
        if (datos.isEmpty()) {
            html.append("<p>No hay datos registrados.</p>");
        } else {
            html.append("<table>");
            html.append("<thead><tr><th>ID</th><th>Estación</th><th>Valor</th><th>Unidad</th><th>Fecha</th></tr></thead>");
            html.append("<tbody>");
            for (String[] fila : datos) {
                html.append("<tr>");
                html.append("<td>").append(fila[0]).append("</td>");
                html.append("<td>").append(fila[1]).append("</td>");
                html.append("<td>").append(fila[3]).append("</td>");
                html.append("<td>").append(fila[4]).append("</td>");
                html.append("<td>").append(fila[5]).append("</td>");
                html.append("</tr>");
            }
            html.append("</tbody>");
            html.append("</table>");
        }
        html.append("</div>");
    }
}
