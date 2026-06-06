# Proyecto de Estacion Meteorologica - Grupo 4

Hola, este es el proyecto de la estacion meteorologica. 

Para correrlo en VS Code:
1. Abre esta carpeta en Visual Studio Code.
2. Busca abajo a la izquierda la pestaña de Java Projects.
3. Busca donde dice Referenced Libraries y dale al boton de mas (+).
4. Selecciona todas las librerias .jar que estan dentro de la carpeta lib.
5. Abre el archivo Suscriptor.java y dale al boton de Run.

Si quieres correrlo por consola en Windows:
- Crea una carpeta llamada bin
- Compilar:
  javac -encoding UTF-8 -cp "lib/*" -d bin src/main/java/pucmm/itt363/grupo4/*.java
- Ejecutar:
  java -cp "bin;lib/*" pucmm.itt363.grupo4.Suscriptor

Si es en Linux o Mac:
- Ejecutar:
  java -cp "bin:lib/*" pucmm.itt363.grupo4.Suscriptor
