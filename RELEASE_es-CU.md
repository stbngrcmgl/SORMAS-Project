# Crear una distribución

## Flujo de trabajo de distribución

Como flujo de trabajo de distribución, utilizamos el flujo de trabajo Gitflow (vea https://www.atlassian.com/git/tutorials/comparing-workflows#gitflow-workflow).

Para distribuir una nueva versión de este proyecto, integramos el <code>jgitflow-maven-plugin</code> (vea https://bitbucket.org/atlassian/jgit-flow/wiki/Home).

Pasos para construir una nueva versión:

1. Haga checkout a la branch <code>development</code>.
2. Ejecute <code>mvn install -Pwith-app</code>.
3. Si el build fue exitoso, ejecute <code>mvn jgitflow:release-start jgitflow:release-finish -Pwith-app,with-dep-poms</code>.
	- Se le pedirá la versión de distribución. Deje esto vacío para mantener la versión de la snapshot actual como número de versión de la distribución (<code>-SNAPSHOT</code> será eliminado por jgitflow-maven-plugin).
	- Se le pedirá la próxima versión de desarrollo. Deje esto vacío y el complemento incrementará el número de distribución micro (<code>1.0.1-SNAPSHOT</code> se convierte en <code>1.0.2-SNAPSHOT</code>). Si desea modificar la versión, escriba, por ejemplo, <code>1.1.0-SNAPSHOT</code>.

4. El resultado es que el estado actual de la branch <code>development</code> se fusiona con la branch <code>master</code> (sin -SNAPSHOT), etiquetado como <code>releases/version-1.0.1</code> y la versión de desarrollo se incrementa automáticamente.

### Números de versión

Números de versión = mayor.menor.micro

Para la generación correcta de códigos de versión de Android, las distribuciones deben ser al menos distribuciones menores. Las distribuciones micro son reservadas para revisiones de una distribución publicada.
- Finalización de una distribución: Incrementar el número mayor o menor
- Finalización de una revisión (fusionada directamente con la branch <code>master</code>): Incrementar el número micro

### Código de versión de Android

El <code>versionCode</code> para la aplicación de Android es generado automáticamente por la versión del proyecto.
La convención para el versionCode <code>aaabbbccd</code> (generado de la versión <code>aaa.bbb.cc</code>) es:
  - d: un dígito para SNAPSHOT (0), Release Candidates (RC1 a RC8 = 1..8) o Final Release (9)
  - cc: dos dígitos para distribuciones micro (con ceros a la izquierda)
  - bbb: tres dígitos para distribuciones menores (con ceros a la izquierda)
  - aaa: distribuciones mayores (si a > 0)


### Configuración local para jgitflow-maven-plugin

El <code>jgitflow-maven-plugin</code> necesita credenciales para git, que se configuran como variables en <code>sormas-base/pom.xml</code>. 
Para usarlo, debe configurar esto en su .m2/settings.xml (o pasarlo como argumentos al ejecutar el complemento).

        <profiles>
                <profile>
                        <id>github-config</id>
                        <!-- Para jgitflow-maven-plugin en github.com -->
                        <properties>
                                <github.sormas.user>miNombreDeUsuario</github.sormas.user>
                                <github.sormas.password>miContraseña</github.sormas.password>
                        </properties>
                </profile>
        </profiles>

        <activeProfiles>
                <activeProfile>github-config</activeProfile>
        </activeProfiles>

### Realizar pruebas de carga y cargar los resultados

Cuando se haya creado una distribución y la nueva versión de SORMAS se haya desplegado en el servidor usado para pruebas de carga, consulte la [guía de pruebas de carga](LOAD_TESTING.md) para realizarlas, idealmente varias veces con diferentes cantidades de usuarios especificadas en el archivo SimulationConfig.scala (por ejemplo, 1, 20, 100). Navegue a la carpeta de resultados, cambie el nombre a las carpetas que contienen las pruebas individuales (por ejemplo, NOMBRE-DE-LA-SIMULACION_CANTIDAD-DE-USUARIOS_VERSION-DE-SORMAS) y comprímalas. Anexe el archivo resultante a la distribución en GitHub, así como un archivo PDF que contenga una descripción general de los resultados (vea una distribución anterior para un ejemplo de cómo hacer esto; deberá abrir las simulaciones individuales y obtener los valores de los respectivos archivos index.html).
