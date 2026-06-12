package pucmm.itt363.grupo4;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ServidorWeb {

    @GetMapping("/")
    public String home(
            @RequestParam(value = "sensor", defaultValue = "temperatura") String sensor,
            @RequestParam(value = "cursor", required = false) Integer cursor,
            @RequestParam(value = "dir", defaultValue = "next") String dir,
            @RequestParam(value = "page", defaultValue = "1") int page) {

        String dbSensorId;
        if (sensor.equals("viento")) {
            dbSensorId = "velocidad-viento";
        } else {
            dbSensorId = sensor;
        }

        int pageSize = 15;
        List<String[]> lecturas = BaseDeDatos.obtenerLecturasPaginadasPorSensor(dbSensorId, cursor, dir, pageSize + 1);

        boolean tieneSiguiente = false;
        if (lecturas.size() > pageSize) {
            tieneSiguiente = true;
            if (cursor == null || dir.equals("next")) {
                lecturas.remove(lecturas.size() - 1);
            } else {
                lecturas.remove(0);
            }
        } else if (cursor != null && dir.equals("prev")) {
            tieneSiguiente = true;
        }

        boolean tieneAnterior = (page > 1);

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset='utf-8'>");
        html.append("<title>Estación Meteorológica - Grupo 4</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; background-color: #ffffff; color: #000000; padding: 20px; }");
        html.append(".top-bar { position: relative; width: 90%; margin: 10px auto 30px auto; }");
        html.append("h1 { text-align: center; margin: 0; font-size: 28px; color: #000000; }");
        html.append(".btn { padding: 8px 20px; font-size: 16px; border: 1px solid #000000; background-color: #e0e0e0; cursor: pointer; }");
        html.append(".btn:hover { background-color: #d0d0d0; }");
        html.append(".btn:disabled { background-color: #f5f5f5; color: #a0a0a0; border-color: #d0d0d0; cursor: not-allowed; }");
        html.append(".tab-menu { text-align: center; margin-bottom: 20px; }");
        html.append(".tab-btn { padding: 10px 20px; font-size: 15px; margin: 5px; cursor: pointer; border: 1px solid #000000; background-color: #ffffff; }");
        html.append(".tab-btn.active { font-weight: bold; background-color: #cccccc; }");
        html.append(".divider { border: 1px solid #000000; width: 90%; margin: 15px auto; }");
        html.append("table { width: 90%; border-collapse: collapse; margin: 20px auto; }");
        html.append("th, td { border: 1px solid #000000; padding: 10px; text-align: left; font-size: 15px; }");
        html.append("th { background-color: #e8e8e8; font-weight: bold; }");
        html.append(".pagination-container { display: flex; justify-content: space-between; align-items: center; width: 90%; margin: 20px auto; }");
        html.append(".pagination-info { font-size: 16px; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");

        html.append("<div class='top-bar'>");
        html.append("<h1>Estación Meteorológica - Grupo 4</h1>");
        html.append("<button class='btn' onclick='window.location.reload()'>Actualizar</button>");
        html.append("</div>");

        html.append("<div class='tab-menu'>");
        html.append("<button class='tab-btn ").append(sensor.equals("temperatura") ? "active" : "").append("' onclick='irASensor(\"temperatura\")'>Temperatura</button>");
        html.append("<button class='tab-btn ").append(sensor.equals("humedad") ? "active" : "").append("' onclick='irASensor(\"humedad\")'>Humedad</button>");
        html.append("<button class='tab-btn ").append(sensor.equals("presion") ? "active" : "").append("' onclick='irASensor(\"presion\")'>Presión</button>");
        html.append("<button class='tab-btn ").append(sensor.equals("viento") ? "active" : "").append("' onclick='irASensor(\"viento\")'>Viento</button>");
        html.append("<button class='tab-btn ").append(sensor.equals("lluvia") ? "active" : "").append("' onclick='irASensor(\"lluvia\")'>Lluvia</button>");
        html.append("</div>");

        html.append("<hr class='divider'>");

        html.append("<table>");
        html.append("<thead>");
        html.append("<tr>");
        html.append("<th>ID</th>");
        html.append("<th>Timestamp</th>");
        html.append("<th>Sensor</th>");
        html.append("<th>Valor</th>");
        html.append("<th>Descripción</th>");
        html.append("</tr>");
        html.append("</thead>");
        html.append("<tbody id='tabla-cuerpo'>");

        if (lecturas.isEmpty()) {
            html.append("<tr id='sin-datos'><td colspan='5' style='text-align: center;'>No hay datos registrados en esta pestaña.</td></tr>");
        } else {
            for (String[] fila : lecturas) {
                html.append("<tr>");
                html.append("<td>").append(fila[0]).append("</td>");
                html.append("<td>").append(fila[1]).append("</td>");
                html.append("<td>").append(fila[2]).append("</td>");
                html.append("<td>").append(fila[3]).append("</td>");
                html.append("<td>").append(fila[4]).append("</td>");
                html.append("</tr>");
            }
        }

        html.append("</tbody>");
        html.append("</table>");

        html.append("<hr class='divider'>");

        html.append("<div class='pagination-container'>");
        
        String deshabilitadoAnterior = tieneAnterior ? "" : "disabled";
        html.append("<button class='btn' onclick='irAnterior()' id='btn-anterior' ").append(deshabilitadoAnterior).append(">&lt; Anterior</button>");

        html.append("<div class='pagination-info'>");
        html.append("Página <span id='pag-actual'><u>").append(page).append("</u></span>");
        html.append("</div>");

        String deshabilitadoSiguiente = tieneSiguiente ? "" : "disabled";
        html.append("<button class='btn' onclick='irSiguiente()' id='btn-siguiente' ").append(deshabilitadoSiguiente).append(">Siguiente &gt;</button>");
        
        html.append("</div>");

        html.append("<script>");
        html.append("var sensorActivo = '").append(sensor).append("';");
        html.append("var paginaActual = ").append(page).append(";");
        html.append("var pageSize = ").append(pageSize).append(";");

        html.append("function irASensor(nuevoSensor) {");
        html.append("    window.location.href = '/?sensor=' + nuevoSensor;");
        html.append("}");

        html.append("function irAnterior() {");
        html.append("    if (paginaActual > 1) {");
        html.append("        var tbody = document.getElementById('tabla-cuerpo');");
        html.append("        if (tbody && tbody.rows.length > 0) {");
        html.append("            var firstRow = tbody.rows[0];");
        html.append("            var firstId = firstRow.cells[0].innerText;");
        html.append("            window.location.href = '/?sensor=' + sensorActivo + '&cursor=' + firstId + '&dir=prev&page=' + (paginaActual - 1);");
        html.append("        }");
        html.append("    }");
        html.append("}");

        html.append("function irSiguiente() {");
        html.append("    var tbody = document.getElementById('tabla-cuerpo');");
        html.append("    if (tbody && tbody.rows.length > 0) {");
        html.append("        var lastRow = tbody.rows[tbody.rows.length - 1];");
        html.append("        var lastId = lastRow.cells[0].innerText;");
        html.append("        window.location.href = '/?sensor=' + sensorActivo + '&cursor=' + lastId + '&dir=next&page=' + (paginaActual + 1);");
        html.append("    }");
        html.append("}");

        html.append("var socket = new WebSocket('ws://' + window.location.host + '/ws');");
        html.append("socket.onmessage = function(event) {");
        html.append("    var data = JSON.parse(event.data);");
        html.append("    var incomingSensor = data.sensorId === 'velocidad-viento' ? 'viento' : data.sensorId;");
        
        html.append("    if (incomingSensor === sensorActivo && paginaActual === 1) {");
        html.append("        var tbody = document.getElementById('tabla-cuerpo');");
        html.append("        var sinDatos = document.getElementById('sin-datos');");
        html.append("        if (sinDatos) {");
        html.append("            sinDatos.remove();");
        html.append("        }");
        html.append("        var row = tbody.insertRow(0);");
        html.append("        var cId = row.insertCell(0);");
        html.append("        var cTime = row.insertCell(1);");
        html.append("        var cSensor = row.insertCell(2);");
        html.append("        var cValor = row.insertCell(3);");
        html.append("        var cDesc = row.insertCell(4);");
        html.append("        cId.innerText = data.id;");
        html.append("        cTime.innerText = data.fecha;");
        html.append("        cSensor.innerText = data.nombreSensor;");
        html.append("        cValor.innerText = data.valorConUnidad;");
        html.append("        cDesc.innerText = data.descripcion;");
        html.append("        if (tbody.rows.length > pageSize) {");
        html.append("            tbody.deleteRow(tbody.rows.length - 1);");
        html.append("            document.getElementById('btn-siguiente').disabled = false;");
        html.append("        }");
        html.append("    }");
        html.append("};");
        html.append("</script>");

        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }
}
