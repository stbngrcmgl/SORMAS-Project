

# Instalar un servidor de SORMAS para desarrollo
**Nota: Esta guía explica cómo configurar un servidor de SORMAS en sistemas Linux y Windows para desarrollo. Tenga en cuenta que no hay una configuración de base de datos porque el script asume el uso de la imagen Postgresql de Docker (vea [SORMAS-Docker](https://github.com/hzi-braunschweig/SORMAS-Docker)).**

## Contenido
* [Prerrequisitos](#prerequisites)
  * [Java 11](#java-11)
  * [ant](#ant)
  * [Base de datos Postgres](#postgres-database)
* [Servidor de SORMAS](#sormas-server)

## Tema Relacionado
* [Instalar un servidor de SORMAS](SERVER_SETUP.md)

## Prerrequisitos

### Java 11
Vea [Instalar Java](SERVER_SETUP.md#java-11)

SORMAS migró recientemente a Java 11. Necesitamos mantener la compatibilidad con Java 8 durante un período de transición. Por lo tanto,  
utilice las características del lenguaje Java 8 por ahora.

### Ant

Descargue e instale Ant; se puede hacer desde el [sitio de Ant](https://ant.apache.org/bindownload.cgi) o con paquetes de su distribución de Linux.

### Base de datos Postgres

Vea [Instalar Postgresql](SERVER_SETUP.md#postgres-database)

Alternativamente, puede usar la imagen de Docker disponible en el repositorio [SORMAS-Docker](https://github.com/hzi-braunschweig/SORMAS-Docker).

## Servidor de SORMAS

Instale su propio servidor Payara (vea [instalar el servidor de SORMAS](SERVER_SETUP.md#sormas-server)) o ejecute ``bash ./server-setup-dev-docker.sh``

Este script descargará Payara (si es necesario) e instalará SORMAS en el servidor de Payara.

Puede editar este script para cambiar rutas y puertos.

Otros pasos:
* **IMPORTANTE**: Ajuste la configuración de SORMAS para su país en /opt/domains/sormas/sormas.properties
* Ajuste la configuración de registro en ``${HOME}/opt/domains/sormas/config/logback.xml`` de acuerdo a sus necesidades (por ejemplo, configure y active el anexador de correo electrónico)
* Construya y despliegue aplicaciones (ear y war) con su IDE.

## Keycloak

Vea [Keycloak](SERVER_SETUP.md#keycloak-server) para saber cómo instalar Docker localmente.

Si está desarrollando activamente en Keycloak (temas, mecanismos de autenticación, traducciones, etc.), se recomienda instalar la variante independiente.

## Modo de depuración VAADIN

Para habilitar el [modo de depuración VAADIN](https://vaadin.com/docs/v8/framework/advanced/advanced-debug.html), vaya a ``sormas-ui/src/main/webapp/WEB-INF/web.xml`` y especifique ``productionMode`` como ``false``.
Asegúrese de no hacer commit a sus cambios en estos archivos, por ejemplo, usando .gitignore. Para acceder a la ventana de depuración, vaya a <url>/sormas-ui/?debug. Es posible que primero deba iniciar sesión una vez como administrador.

## Otros componentes

Vea [Instalar un servidor de SORMAS](SERVER_SETUP.md)
