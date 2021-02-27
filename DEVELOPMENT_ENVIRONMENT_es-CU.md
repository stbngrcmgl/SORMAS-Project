
# Entorno de desarrollo de SORMAS

## Servidor
- Instale [su servidor local](SERVER_SETUP.md)
- O instale su servidor de desarrollo local usando [maven cargo](sormas-cargoserver/README.md)
- Alternativamente, se podría usar [SERVER_DEV_SETUP.md](SERVER_DEV_SETUP.md) (en este momento no es recomendable)

## Git
- Instale [Git para su sistema operativo](https://git-scm.com/downloads)
- Recomendación: Instale un cliente de Git como [TortoiseGit](https://tortoisegit.org/) si no desea manejar el control de versiones desde la línea de comandos, o por separado para los proyectos de Eclipse y Android Studio
- Abra Git Bash y ejecute el comando <code>git config --global branch.development.rebase true</code> (lo que garantiza que se use rebase al hacer pull, en lugar de merge)

## Java
- Descargue e instale Java 11 **JDK** (no JRE) para su sistema operativo. Sugerimos utilizar Zulu OpenJDK: https://www.azul.com/downloads/zulu/
  * **Linux**: https://docs.azul.com/zulu/zuludocs/#ZuluUserGuide/PrepareZuluPlatform/AttachAPTRepositoryUbuntuOrDebianSys.htm
        
		sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 0xB1998361219BD9C9
		sudo apt-add-repository 'deb https://repos.azul.com/zulu/deb/ stable main'
		sudo apt-get update
		sudo apt-get install zulu11
  - **Windows**: Para entornos de prueba y desarrollo, sugerimos descargar y ejecutar el instalador de Java 11 **JDK** para sistemas cliente de 32 o 64 bits (según su sistema).

## Eclipse
- Instale la versión más reciente de Eclipse, la mejor opción es [Eclipse IDE for Enterprise Java Developers](https://www.eclipse.org/downloads/packages).
- Establezca el JRE predeterminado de Eclipse como el Zulu Java SDK instalado: [Assigning the default JRE for the workbench ](https://help.eclipse.org/kepler/index.jsp?topic=%2Forg.eclipse.jdt.doc.user%2Ftasks%2Ftask-assign_default_jre.htm)
- Clone el repositorio SORMAS-Open e importe los proyectos a Eclipse
	- Si utiliza Eclipse para clonar, seleccione "File -> Import -> Git -> Projects from Git", y continúe hasta que se le pida crear un nuevo proyecto a partir del repositorio clonado; haga clic en Cancel, y use "File -> Import -> Maven -> Existing Maven Projects" para importar los proyectos a su espacio de trabajo
	- Si clona el repositorio desde la línea de comandos o un cliente Git, obviamente solo necesita realizar el último paso
- Instale [Payara Tools](https://marketplace.eclipse.org/content/payara-tools)
- Instale el [complemento Vaadin para Eclipse](https://marketplace.eclipse.org/content/vaadin-plugin-eclipse) (no es necesario instalar el diseñador de interfaz de usuario comercial)
- Agregue un servidor Payara a Eclipse, e ingrese las credenciales que especificó al configurar el servidor
- Configure el formateo automático del código ("Window -> Preferences"):
    - Vaya a "Java -> Code Style -> Formatter", importe ``sormas-base/java-formatter-profile.xml``, y aplique.
    - Vaya a "Java -> Code Style -> Organize Imports", importe ``sormas-base/java-importorder-profile.importorder``, "Number of imports needed for .*" = ``99``, "Number of static imports needed for .*" = ``99``, "Do not create import for types starting with a lowercase letter" = ``checked``, y aplique.
    - Vaya a "Java -> Editor -> Save Actions", active "Perform the selected actions on save", "Format source code" con "Format all lines", "Organize imports", y aplique.

### Pasos adicionales
- Haga una copia de "build.properties.example", que está en "sormas-base", cámbiele el nombre a "build.properties", y establezca "glassfish.domain.root" como la ubicación del dominio sormas que está en la carpeta "glassfish/domains" dentro de la instalación de payara
- Arrastre el archivo "build.xml", que está en "sormas-base", a la vista de Ant en Eclipse
  - O bien: Ejecute "Maven install" en el proyecto sormas-base
  - O: Ejecute el script de ant "install [default]" (esto necesita una instalación maven en su sistema con la variable M2_HOME especificada)
  - Después: Ejecute el script de ant "deploy-serverlibs"
- Resalte todos los proyectos de Eclipse y seleccione "Maven -> Update Project" en el menú contextual; realice la actualización para todos los proyectos
- Inicie el servidor Glassfish y despliegue "sormas-ear", "sormas-rest", y "sormas-ui" arrastrando los respectivos proyectos al servidor, o bien use la función "Add and Remove..." haciendo clic derecho en el servidor.
- Abra su navegador y escriba "http://localhost:6080/sormas-ui" o "https://localhost:6081/sormas-ui" para comprobar si todo se configuró correctamente (y para utilizar la aplicación)

## IntelliJ
- Instale la edición Ultimate más reciente de IntelliJ
- Configure el SDK del proyecto para usar el Zulu Java 8 SDK instalado
- Clone el repositorio SORMAS-Project y abra el proyecto en IntelliJ
	- asegure que en "File -> Project Structure -> Modules" se reconozcan todos los módulos (excepto la aplicación de Android, que no debe agregarse), si no, agregue los módulos con +
- Asegure que en "File -> Settings -> Plugins" las integraciones de Glassfish y Ant estén habilitadas (busque en la pestaña "Installed")
- Instale el complemento Vaadin (¡no el complemento Vaadin Designer!)
- Haga una copia de "build.properties.example", que está en "sormas-base", cámbiele el nombre a "build.properties", y establezca "glassfish.domain.root" como la ubicación del dominio sormas que está en la carpeta "glassfish/domains" dentro de la instalación de payara
- Ejecute "Maven install" en el proyecto sormas-base
- Agregue un servidor Payara a IntelliJ:
	- vaya a "Run -> Edit configurations"
	- agregue una nueva configuración y seleccione entre las plantillas Glassfish server
	- seleccione el directorio payara5 para servidor de aplicaciones, y nombre el campo de servidor de aplicaciones Payara5
	- especifique el dominio del servidor, y las credenciales de la configuración del servidor
	- agregue "http://localhost:6080/sormas-ui" en la sección de abrir el navegador, y marque la casilla de verificación After launch
	- en la pestaña Deployment agregue los artefactos "sormas-ear", "sormas-rest", y "sormas-ui"
	- en la pestaña Logs, agregue un nuevo registro cuya ubicación apunte al registro del dominio (por ejemplo: payara5\glassfish\domains\sormas\logs\server.log)
	- en la pestaña Startup/Connection, asegúrese de no pasar variables de entorno (es un error abierto actualmente en intellij) - ignore la advertencia de que la configuración de depuración no es correcta
	- edite su configuración de dominio ..\payara5\glassfish\domains\sormas\config\domain.xml y asegure que el nodo java-config contenga:
	 ``<java-config classpath-suffix="" debug-enabled="true" debug-options="-agentlib:jdwp=transport=dt_socket,address=6009,server=n,suspend=y" ...``
- Abra la ventana de Ant, haga clic en el ícono "+", y seleccione el archivo build.xml del proyecto sormas-base
- Ejecute los scripts "install" y "deploy-serverlibs" en este orden
- Establezca el directorio de trabajo predeterminado para configuraciones de ejecución: Run -> Edit Configurations -> Templates -> Application -> especifique el valor de *Working directory* como ``$MODULE_WORKING_DIR$``
- **Configure el formateo del código:**
	- deshabilite "Optimize imports on the fly" (Editor -> General -> Auto Import)
	- instale Eclipse Code Formatter para IntelliJ (https://plugins.jetbrains.com/plugin/6546-eclipse-code-formatter)
	- abra la configuración del complemento (Other Settings -> Eclipse Code Formatter) y seleccione "Use the Eclipse Code Formatter"
	- en "Eclipse Formatter config file", seleccione ``sormas-base/java-formatter-profile.xml``
	- marque optimizar las importaciones y, para "Import order", seleccione ``sormas-base/java-importorder-profile.importorder``
	- **Importante:** seleccione "Do not format other file types by IntelliJ formatter"
	- vaya a Preferences -> Editor -> Code style -> Java -> Imports: especifique los cantidades de clases y nombres estáticos para importar con * to 99
	- para IntelliJ, el formateo del código generalmente se realiza con Ctrl+Alt+L. Para el formateo automático, se recomienda utilizar el complemento Save Actions y marcar las tres primeras casillas de verificación en "General" y las dos primeras casillas de verificación en "Formatting Actions" (https://plugins.jetbrains.com/plugin/7642-save-actions)

## Android Studio
**Nota: Solo es necesario para el desarrollo de la aplicación de Android de SORMAS
* Instale la versión más reciente de Android Studio (para evitar errores, inicie la instalación con derechos de administrador)
* Inicie la aplicación
* Para evitar errores, asegúrese de que la ruta del SDK de Android no contenga espacios en blanco
	* La ruta se puede editar en ``Tools -> SDK Manager -> Android SDK Location``
* Abra Android Studio e importe el módulo "sormas-app" de SORMAS-Project
* Cree un archivo keystore.properties en sormas-app (use keystore.properties.example como referencia; solo es necesario para el despliegue de la aplicación).
* Construya el proyecto de Android Studio ejecutando el gradle build (esto se puede hacer automáticamente)
* Agregue un emulador con la versión de SDK entre las propiedades minSdkVersion y targetSdkVersion de build.gradle
* En el primer inicio de la aplicación, ingrese la URL del servicio rest de Sormas para la URL del servidor: http://10.0.2.2:6080/sormas-rest/ (vea: https://developer.android.com/studio/run/emulator-networking)
* Configure el formateo del código:
	- instale Eclipse Code Formatter para Android Studio (https://plugins.jetbrains.com/plugin/6546-eclipse-code-formatter)
	- abra la configuración del complemento (Other Settings -> Eclipse Code Formatter) y seleccione "Use the Eclipse Code Formatter"
	- en "Eclipse Formatter config file", seleccione ``sormas-base/java-formatter-profile.xml``
	- marque optimizar las importaciones y, para "Import order", seleccione ``sormas-base/java-importorder-profile.importorder``
	- **Importante:** seleccione "Do not format other file types by IntelliJ formatter"
	- vaya a Preferences -> Editor -> Code style -> Java -> Imports: especifique los cantidades de clases y nombres estáticos para importar con * to 99
	- para Android Studio, el formateo del código generalmente se realiza con Ctrl+Alt+L. Para el formateo automático, se recomienda utilizar el complemento Save Actions (https://plugins.jetbrains.com/plugin/7642-save-actions)
