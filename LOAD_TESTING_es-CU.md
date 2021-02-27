# Guía para pruebas de carga

## Introducción
Esta guía lo ayudará a configurar una infraestructura para realizar pruebas de carga manualmente en un servidor de SORMAS (las pruebas de carga automáticas se pueden volver parte del ciclo de distribución en un momento posterior). Tenga en cuenta que estas pruebas están destinadas a medir el tiempo que requieren las solicitudes específicas para realizarse; no son pruebas de estrés de servidor. Configure un entorno de desarrollo local o un servidor de pruebas para ejecutar estas pruebas, ya que no es recomendable ejecutarlas en un sistema productivo.

## Instalar Gatling
Para ejecutar las pruebas proporcionadas en este repositorio, deberá instalar Gatling en su computadora. Siga las instrucciones en https://gatling.io/docs/current/installation ("Using the Bundle").

## Descargar simulaciones de prueba
Hemos creado simulaciones (que son básicamente escenarios de prueba) que prueban las acciones más críticas para el rendimiento en las aplicaciones de SORMAS:

* Abrir el tablero de control de vigilancia
* Abrir el directorio de casos
* Obtener todos los casos de un usuario móvil a través de la interfaz REST
* Obtener todas las personas de un usuario móvil a través de la interfaz REST
* Obtener todos los datos de infraestructura a través de la interfaz REST

Descargue el archivo [sormas_load_tests.zip](https://github.com/hzi-braunschweig/SORMAS-Project/blob/development/sormas_load_tests.zip) más reciente y extraiga su contenido en su directorio de Gatling.

## Ajustar la configuración de simulación
Abra el archivo ``SimulationConfig.scala`` en un editor de texto. Cambie el valor predeterminado de la variable ``serverUrl`` por la URL del servidor que desea probar. Si está utilizando una instalación nueva de SORMAS, puede dejar las variables ``mobileUsername`` y ``mobilePassword`` como están. De lo contrario, escriba el nombre de usuario y la contraseña de un usuario móvil (probablemente un funcionario de vigilancia) de su sistema que tenga acceso a tantos casos como sea posible. Finalmente, puede editar la variable ``numberOfUsers`` para determinar el número de solicitudes paralelas realizadas al ejecutar las pruebas. Dejarlas en 1 es una buena idea para averiguar si están pasando las pruebas; pero para verdaderas pruebas de carga en su servidor, debe incrementar este número para ver cómo funciona cuando varios usuarios están realizando acciones costosas al mismo tiempo.

## Ejecutar las pruebas de carga
Tenga en cuenta que para ejecutar los escenarios ``CaseDirectorySimulation`` y ``DashboardSimulation`` primero debe iniciar sesión en la aplicación web, porque la operación de inicio de sesión no es parte de las simulaciones actualmente. Las simulaciones se ejecutarán con éxito si no lo hace, pero los resultados no tendrán ninguna significación.

Una vez que todo esté configurado, navegue a la carpeta ``bin`` de su instalación de Gatling, y ejecute el archivo ``gatling.bat`` si está usando Windows, o el archivo ``gatling.sh`` si está usando Linux. Se abrirá una ventana de comandos que carga todas las simulaciones de su directorio de archivos de usuario. Elija la simulación que desea ejecutar escribiendo su número. Una vez ejecutada la simulación, puede encontrar sus resultados en la carpeta ``results``.
