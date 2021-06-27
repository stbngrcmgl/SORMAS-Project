

# Instalar un servidor de SORMAS
**Nota: Esta guía explica cómo instalar un servidor de SORMAS en sistemas Linux y Windows, esto último para uso en sistemas de desarrollo solamente. Tenga en cuenta también que ciertas partes del script de configuración no se ejecutarán en Windows.**

## Contenido
* [Prerrequisitos](#prerequisites)
  * [Java 11](#java-11)
  * [Base de datos Postgres](#postgres-database)
* [Servidor de SORMAS](#sormas-server)
* [Servidor Keycloak](#keycloak-server)
* [Configuración del servidor web](#web-server-setup)
  * [Servidor web Apache](#apache-web-server)
  * [Cortafuegos](#firewall)
  * [Servidor de correo Postfix](#postfix-mail-server)
  * [Probar la configuración del servidor](#testing-the-server-setup)
* [Entorno de software R](#r-software-environment)
* [Solución de problemas](#troubleshooting)

## Temas relacionados
* [Crear una aplicación para un servidor demo](DEMO_APP.md)
* [Repositorio Docker de SORMAS](https://github.com/hzi-braunschweig/SORMAS-Docker)

## Prerrequisitos

### Java 11

* Descargue e instale Java 11 **JDK** (no JRE) para su sistema operativo. Sugerimos utilizar Zulu OpenJDK: https://www.azul.com/downloads/zulu/
  * **Linux**: https://docs.azul.com/zulu/zuludocs/#ZuluUserGuide/PrepareZuluPlatform/AttachAPTRepositoryUbuntuOrDebianSys.htm
        
		sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 0xB1998361219BD9C9
		sudo apt-add-repository 'deb https://repos.azul.com/zulu/deb/ stable main'
		sudo apt-get update
		sudo apt-get install zulu11
  * **Windows**: Para entornos de prueba y desarrollo, sugerimos descargar y ejecutar el instalador de Java 11 **JDK** para sistemas cliente de 32 o 64 bits (según su sistema).
* Puede verificar su versión de Java desde el shell/línea de comandos usando: ``java -version``

### Base de datos Postgres

* Instale PostgreSQL (actualmente 9.5, 9.6 o 10) en su sistema (los manuales para todos los sistemas operativos se pueden encontrar aquí: https://www.postgresql.org/download)
* Especifique **max_connections = 288** y **max_prepared_transactions = 256** (como mínimo, la suma de todos los grupos de conexiones) en ``postgresql.conf`` (por ejemplo, ``/etc/postgresql/10.0/main/postgresql.conf``; ``C:/Program Files/PostgreSQL/10.0/data``) - asegure que la propiedad no esté comentada
* Instale la extensión "temporal tables" para Postgres (https://github.com/arkhipov/temporal_tables)
    * **Windows**: Descargue la versión más reciente para su versión de Postgres: https://github.com/arkhipov/temporal_tables/releases/latest, luego copie la DLL del proyecto en el directorio lib de PostgreSQL, y los archivos .sql y .control en el directorio share\extension.	
    * **Linux** (vea https://github.com/arkhipov/temporal_tables#installation):
        * ``sudo apt-get install libpq-dev``
        * ``sudo apt-get install postgresql-server-dev-all``
        * ``sudo apt install pgxnclient``
        * Chequee GCC: ``gcc --version`` e instale si no está presente
        * ``sudo pgxn install temporal_tables``
        * Los paquetes se pueden eliminar después
	   
## Servidor de SORMAS	

* Obtenga el build más reciente de SORMAS descargando el archivo ZIP de la distribución más reciente en GitHub: https://github.com/hzi-braunschweig/SORMAS-Open/releases/latest 
* **Linux**:
  * Descomprima el archivo, copie/cargue su contenido en **/root/deploy/sormas/$(date +%F)**, y haga ejecutable el script de configuración.
        
		cd /root/deploy/sormas
		SORMAS_VERSION=1.y.z
		wget https://github.com/hzi-braunschweig/SORMAS-Project/releases/download/v${SORMAS_VERSION}/sormas_${SORMAS_VERSION}.zip
		unzip sormas_${SORMAS_VERSION}.zip
		mv deploy/ $(date +%F)
		rm sormas_${SORMAS_VERSION}.zip
		chmod +x $(date +%F)/server-setup.sh
* **Windows**:
  * Descargue e instale Git para Windows. Esto proporcionará una emulación de bash que puede usar para ejecutar el script de configuración: https://gitforwindows.org/
  * Descomprima el archivo ZIP (por ejemplo, en su directorio de descargas)
  * Abra Git Bash y navegue hasta el subdirectorio de configuración
* Opcional: Abra ``server-setup.sh`` en un editor de texto para personalizar las rutas de instalación, el acceso a la base de datos, y los puertos del servidor. Los puertos predeterminados son 6080 (HTTP), 6081 (HTTPS) y 6048 (admin). **Importante:** No cambie el nombre de usuario de la base de datos. El nombre predefinido es usado en las declaraciones ejecutadas en la base de datos.
* Configure la base de datos y un dominio Payara para SORMAS ejecutando el script de configuración: ``sudo -s ./server-setup.sh`` Presione Enter cuando se solicite
* **IMPORTANTE**: Verifique que el script se ejecutó correctamente. Si algo sale mal, debe solucionar el problema (o pedir ayuda); después elimine el directorio de dominio creado, y vuelva a ejecutar el script.
* **IMPORTANTE**: Ajuste la configuración de SORMAS para su país en /opt/domains/sormas/sormas.properties
* Ajuste la configuración de registro en ``/opt/domains/sormas/config/logback.xml`` de acuerdo a sus necesidades (por ejemplo, configure y active el anexador de correo electrónico)
* Linux: [Actualice el dominio de SORMAS](SERVER_UPDATE.md)

## Servidor Keycloak

Keycloak se puede configurar de dos formas:
* como contenedor de Docker (para usar el enfoque Keycloak solamente)
* como instalación independiente (para desarrollar en Keycloak, por ejemplo, temas, SPIs)

### Keycloak como contenedor Docker
*Para hacer solo cuando SORMAS ya está instalado en la máquina como una instalación independiente.*

*Para la configuración completa de Docker, vea el repositorio [SORMAS-Docker](https://github.com/hzi-braunschweig/SORMAS-Docker/tree/keycloak-integration).*

**Prerrequisitos**
* Servidor de SORMAS instalado
* PostgreSQL instalado
* Docker instalado
* Abra y edite [keycloak-setup.sh](sormas-base/setup/keycloak/keycloak-setup.sh) con los valores reales de su sistema *(en Windows utilice Git Bash)*.

**Configuración**
* Ejecute [keycloak-setup.sh](sormas-base/setup/keycloak/keycloak-setup.sh)
* Actualice el archivo `sormas.properties` en el dominio de SORMAS con la propiedad `authentication.provider=KEYCLOAK`


### Keycloak como instalación independiente

**Prerrequisitos**
* Servidor de SORMAS instalado
* PostgreSQL instalado

**Configuración**

Configuración de Keycloak como instalación independiente [Guía de instalación y configuración del servidor](https://www.keycloak.org/docs/11.0/server_installation/#installation)
* Configure Keycloak con la [Configuración de base de datos relacional](https://www.keycloak.org/docs/11.0/server_installation/#_database) de la base de datos PostgreSQL
* Configure un usuario administrador
* Copie el contenido de la carpeta `themes` en `${KEYCLOAK_HOME}/themes` [Desplegar temas](https://www.keycloak.org/docs/11.0/server_development/#deploying-themes)
* Despliegue `sormas-keycloak-service-provider` [Usar Keycloak Deployer](https://www.keycloak.org/docs/11.0/server_development/#using-the-keycloak-deployer)
* Actualice el archivo [SORMAS.json](sormas-base/setup/keycloak/SORMAS.json) reemplazando los siguientes marcadores de posición: `${SORMAS_SERVER_URL}`, `${KEYCLOAK_SORMAS_UI_SECRET}`, `${KEYCLOAK_SORMAS_BACKEND_SECRET}`, `${KEYCLOAK_SORMAS_REST_SECRET}`
* Cree el ámbito de SORMAS importando [SORMAS.json](sormas-base/setup/keycloak/SORMAS.json), vea [Crear un nuevo ámbito](https://www.keycloak.org/docs/11.0/server_admin/#_create-realm)
* Actualice los clientes `sormas-*` generando nuevos secretos para ellos
* Actualice la configuración de correo electrónico del ámbito para permitir el envío de correos electrónicos a los usuarios

Para actualizar el servidor de SORMAS, ejecute los siguientes comandos
```shell script
${ASADMIN} set-config-property --propertyName=payara.security.openid.clientSecret --propertyValue=${KEYCLOAK_SORMAS_UI_SECRET} --source=domain
${ASADMIN} set-config-property --propertyName=payara.security.openid.clientId --propertyValue=sormas-ui --source=domain
${ASADMIN} set-config-property --propertyName=payara.security.openid.scope --propertyValue=openid --source=domain
${ASADMIN} set-config-property --propertyName=payara.security.openid.providerURI --propertyValue=http://localhost:${KEYCLOAK_PORT}/keycloak/auth/realms/SORMAS --source=domain
${ASADMIN} set-config-property --propertyName=sormas.rest.security.oidc.json --propertyValue="{\"realm\":\"SORMAS\",\"auth-server-url\":\"http://localhost:${KEYCLOAK_PORT}/auth\",\"ssl-required\":\"external\",\"resource\":\"sormas-rest\",\"credentials\":{\"secret\":\"${KEYCLOAK_SORMAS_REST_SECRET}\"},\"confidential-port\":0,\"principal-attribute\":\"preferred_username\",\"enable-basic-auth\":true}" --source=domain
${ASADMIN} set-config-property --propertyName=sormas.backend.security.oidc.json --propertyValue="{\"realm\":\"SORMAS\",\"auth-server-url\":\"http://localhost:${KEYCLOAK_PORT}/auth/\",\"ssl-required\":\"external\",\"resource\":\"sormas-backend\",\"credentials\":{\"secret\":\"${KEYCLOAK_SORMAS_BACKEND_SECRET}\"},\"confidential-port\":0}" --source=domain
```
donde:
* `${ASADMIN}` - representa la ubicación de `${PAYARA_HOME}\bin\asadmin`
* `${KEYCLOAK_PORT}` - el puerto en que se ejecutará keycloak
* `${KEYCLOAK_SORMAS_UI_SECRET}` - es el secreto generado en Keycloak para el cliente `sormas-ui`
* `${KEYCLOAK_SORMAS_REST_SECRET}` - es el secreto generado en Keycloak para el cliente `sormas-rest`
* `${KEYCLOAK_SORMAS_BACKEND_SECRET}` - es el secreto generado en Keycloak para el cliente `sormas-backend`

Luego actualice el archivo `sormas.properties` en el dominio de SORMAS con la propiedad `authentication.provider=KEYCLOAK`

### Conectar Keycloak a una instancia de SORMAS que ya esté en ejecución

*después de configurar Keycloak como una de las opciones descritas anteriormente*

Si Keycloak es configurado junto a una instancia de SORMAS que ya está en ejecución, estos son los pasos a seguir para asegurar que los usuarios ya existentes puedan acceder al sistema:
1. Cree manualmente un usuario administrador en Keycloak para el ámbito SORMAS [Creating a user](https://www.keycloak.org/docs/11.0/getting_started/index.html#creating-a-user) *(el nombre de usuario debe ser igual al nombre de usuario del administrador en SORMAS)*
2. Inicie sesión en SORMAS y active el botón **Sincronizar usuarios** de la página **Usuarios**
3. Esto sincronizará a los usuarios con Keycloak manteniendo sus contraseñas originales - consulte [Proveedor de servicios Keycloak de SORMAS](sormas-keycloak-service-provider/README.md) para obtener más información

### Configuración de Keycloak

Más información sobre la configuración predeterminada y cómo personalizar aquí [Keycloak](sormas-base/doc/keycloak.md)

## Configuración del servidor web

### Servidor web Apache
**Nota: Esto no es necesario para los sistemas de desarrollo.** Cuando esté usando SORMAS en un entorno de producción, debe usar un servidor http como Apache 2 en lugar de poner el servidor Payara en la primera línea.
A continuación, algunas cosas que debe hacer para configurar el servidor Apache como proxy:

* Active todos los módulos necesarios:

		a2enmod ssl
		a2enmod rewrite
		a2enmod proxy
		a2enmod proxy_http
		a2enmod headers
* Cree un nuevo sitio /etc/apache2/sites-available/url.de.su.servidor.sormas.conf (por ejemplo, sormas.org.conf)
* Fuerce conexiones con seguridad SSL: redirija de http a https:

		<VirtualHost *:80>
			ServerName url.de.su.servidor.sormas
			RewriteEngine On
			RewriteCond %{HTTPS} !=on
			RewriteRule ^/(.*) https://url.de.su.servidor.sormas/$1 [R,L]
		</VirtualHost>
		<IfModule mod_ssl.c>
		<VirtualHost *:443>
			ServerName url.de.su.servidor.sormas
			...
		</VirtualHost>
		</IfModule>
* Configure el registro:

        ErrorLog /var/log/apache2/error.log
        LogLevel warn
        LogFormat "%h %l %u %t \"%r\" %>s %b _%D_ \"%{User}i\"  \"%{Connection}i\"  \"%{Referer}i\" \"%{User-agent}i\"" combined_ext
        CustomLog /var/log/apache2/access.log combined_ext
* Configuración de clave SSL:

        SSLEngine on
        SSLCertificateFile    /etc/ssl/certs/url.de.su.servidor.sormas.crt
        SSLCertificateKeyFile /etc/ssl/private/url.de.su.servidor.sormas.key
        SSLCertificateChainFile /etc/ssl/certs/url.de.su.servidor.sormas.ca-bundle
		
        # deshabilitar cifrados débiles y antiguos TLS/SSL
        SSLProtocol all -SSLv3 -TLSv1 -TLSv1.1
        SSLCipherSuite ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-ECDSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-ECDSA-CHACHA20-POLY1305:ECDHE-RSA-CHACHA20-POLY1305:DHE$
        SSLHonorCipherOrder off		
* Agregue un proxy pass al puerto local:

		ProxyRequests Off
		ProxyPass /sormas-ui http://localhost:6080/sormas-ui
		ProxyPassReverse /sormas-ui http://localhost:6080/sormas-ui
		ProxyPass /sormas-rest http://localhost:6080/sormas-rest
		ProxyPassReverse /sormas-rest http://localhost:6080/sormas-rest
* Configure los ajustes de seguridad:

		Header always set X-Content-Type-Options "nosniff"
		Header always set X-Xss-Protection "1; mode=block"
		# Deshabilitar el almacenamiento en caché
		Header always set Cache-Control "no-cache, no-store, must-revalidate, private"
		Header always set Pragma "no-cache"
		
		Header always set Content-Security-Policy \
            "default-src 'none'; \
            object-src 'self'; \
            script-src 'self' 'unsafe-inline' 'unsafe-eval'; \
            connect-src https://fonts.googleapis.com https://fonts.gstatic.com 'self'; \
            img-src *; \
            style-src 'self' https://fonts.googleapis.com 'unsafe-inline'; \
            font-src https://fonts.gstatic.com 'self'; \
            frame-src 'self'; \
            worker-src 'self'; \
            manifest-src 'self'; \
            frame-ancestors 'self'		

		# El header Content-Type estaba vacío o ausente.
		# Asegure que cada página establezca el valor de tipo de contenido específico y apropiado para el contenido que se entrega.
		AddType application/vnd.ms-fontobject    .eot
		AddType application/x-font-opentype      .otf
		AddType image/svg+xml                    .svg
		AddType application/x-font-ttf           .ttf
		AddType application/font-woff            .woff
* Active la compresión de la salida (¡muy importante!): 

        <IfModule mod_deflate.c>
                AddOutputFilterByType DEFLATE text/plain text/html text/xml
                AddOutputFilterByType DEFLATE text/css text/javascript
                AddOutputFilterByType DEFLATE application/json
                AddOutputFilterByType DEFLATE application/xml application/xhtml+xml
                AddOutputFilterByType DEFLATE application/javascript application/x-javascript
                DeflateCompressionLevel 1
        </IfModule></code>

* Proporcione la apk de Android:

        Options -Indexes
		AliasMatch "/downloads/sormas-(.*)" "/var/www/sormas/downloads/sormas-$1"

* Para la configuración de seguridad de Apache 2, sugerimos los siguientes ajustes (``/etc/apache2/conf-available/security.conf``):

		ServerTokens Prod
		ServerSignature Off
		TraceEnable Off

		Header always set Strict-Transport-Security "max-age=15768000; includeSubDomains; preload"
		Header unset X-Frame-Options
		Header always set X-Frame-Options SAMEORIGIN
		Header unset Referrer-Policy
		Header always set Referrer-Policy "same-origin"
		Header edit Set-Cookie "(?i)^((?:(?!;\s?HttpOnly).)+)$" "$1;HttpOnly"
		Header edit Set-Cookie "(?i)^((?:(?!;\s?Secure).)+)$" "$1;Secure"

		Header unset X-Powered-By
		Header unset Server
		
		
* Si necesita actualizar la configuración del sitio mientras el servidor se está ejecutando, use el siguiente comando para publicar los cambios sin volver a cargar:

        apache2ctl graceful
		
### Cortafuegos

* El servidor solo debe publicar los puertos que son necesarios. Para SORMAS, son los puertos 80 (HTTP) y 443 (HTTPS). Además, necesitará el puerto SSH para acceder al servidor con fines administrativos.
* Sugerimos utilizar UFW (Uncomplicated Firewall) que proporciona una interfaz sencilla para iptables:

		sudo apt-get install ufw
		sudo ufw default deny incoming
		sudo ufw default allow outgoing
		sudo ufw allow ssh
		sudo ufw allow http
		sudo ufw allow https
		sudo ufw enable

### Servidor de correo Postfix

* Instale postfix y mailutils:

		apt install aptitude
		aptitude install postfix
		-> elija "satelite system"
		apt install mailutils
	
* Configure su sistema:

		nano /etc/aliases
		-> añada "root: ingrese-su@correo-electrónico-de-soporte-aquí.com"
		nano /opt/domains/sormas/config/logback.xml
		-> asegure que el anexador "EMAIL_ERROR" esté activado y envíe a su dirección de correo electrónico

### Probar la configuración del servidor

Utilice SSL Labs para probar la configuración de seguridad de su servidor: https://www.ssllabs.com/ssltest

## Entorno de software R

Para habilitar los diagramas de red de enfermedades en el tablero de control de contactos, se requieren R y varios paquetes de extensión.
Después, el ejecutable Rscript debe configurarse en el archivo ``sormas.properties``.
Esto se puede hacer de manera conveniente ejecutando el script de configuración de R desde el archivo ZIP de SORMAS (vea [Servidor de SORMAS](#sormas-server)):

* Si la instalación de SORMAS se ha personalizado, puede ser necesario ajustar las rutas de instalación en ``r-setup.sh``, usando un editor de texto.
* Ejecute el script de configuración de R:

	chmod +x r-setup.sh
	./r-setup.sh
	
* Siga las instrucciones del script.

## Configuración del certificado SORMAS to SORMAS

Para poder comunicar con otras instancias de SORMAS, hay algunos pasos adicionales que se deben seguir, a fin de configurar el certificado y el truststore. Consulte la [guía relacionada](GUIDE_SORMAS2SORMAS_CERTIFICATE.md) para obtener instrucciones detalladas sobre la configuración de SORMAS to SORMAS.
<br/>

## Solución de problemas

### Problema: El inicio de sesión falla

Compruebe que la tabla de usuarios tiene una entrada correspondiente. Si no es así, la inicialización de la base de datos que se realiza al desplegar sormas-ear.ear probablemente tuvo un error.

### Problema: El servidor no tiene memoria

Los servidores antiguos se configuraron con un tamaño de memoria de menos de 2048MB. Puede cambiar esto usando los siguientes comandos:

	/opt/payara-172/glassfish/bin/asadmin --port 6048 delete-jvm-options -Xmx512m
	/opt/payara-172/glassfish/bin/asadmin --port 6048 delete-jvm-options -Xmx1024m
	/opt/payara-172/glassfish/bin/asadmin --port 6048 create-jvm-options -Xmx2048m

Alternativa: Puede editar los ajustes directamente en domain.xml, en el directorio config del dominio de SORMAS. Simplemente busque ``Xmx`` - debe haber dos entradas que cambiar.
