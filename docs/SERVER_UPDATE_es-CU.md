# Actualizar un servidor de SORMAS
Las distribuciones de SORMAS a partir de 1.21.0 contienen un script que actualiza y despliega automáticamente el servidor. Si está utilizando una versión anterior y, por lo tanto, necesita realizar una actualización manual del servidor, descargue los archivos de la distribución 1.21.0 y utilice los comandos especificados en el script server-update.sh.

## Preparativos
Nota: Puede omitir este paso si acaba de configurar su servidor de SORMAS y ya descargó la distribución más reciente.

* Obtenga los archivos de la distribución más reciente (deploy.zip) en https://github.com/hzi-braunschweig/SORMAS-Open/releases/latest
* Descomprima el archivo y copie/cargue su contenido en **/root/deploy/sormas/$(date +%F)**
        
		cd /root/deploy/sormas
		SORMAS_VERSION=1.y.z
		wget https://github.com/hzi-braunschweig/SORMAS-Project/releases/download/v${SORMAS_VERSION}/sormas_${SORMAS_VERSION}.zip
		unzip sormas_${SORMAS_VERSION}.zip
		mv deploy/ $(date +%F)
		rm sormas_${SORMAS_VERSION}.zip
## Actualización automática del servidor
* Navegue a la carpeta que contiene los archivos de despliegue descomprimidos:
``cd /root/deploy/sormas/$(date +%F)``
* Haga ejecutable el script de actualización:
``chmod +x server-update.sh``
* Opcional: Abra server-update.sh en un editor de texto para personalizar los valores de, por ejemplo, la ruta del dominio o el nombre de la base de datos. Solo necesita hacer esto si usó valores personalizados al configurar el servidor.
* Ejecute el script de actualización y siga las instrucciones:
``./server-update.sh``
* Si algo sale mal, abra el archivo de registro de actualización más reciente (ubicado por defecto en la carpeta "update-logs" del directorio de dominio) y busque los errores.

## Restaurar la base de datos
Si algo sale mal durante el proceso de actualización automática de la base de datos al desplegar el servidor, puede usar el siguiente comando para restaurar los datos:

``pg_restore --clean -U postgres -Fc -d sormas_db sormas_db_....dump``

## Inicios de sesión predeterminados
Estos son los usuarios predeterminados para la mayoría de los roles de usuario, destinados a ser utilizados en sistemas de desarrollo o demostración. En todos los casos, excepto el usuario admin, el nombre de usuario y la contraseña son idénticos. Asegúrese de desactivarlos o cambiar las contraseñas en los sistemas productivos.

### Admin
**Nombre de usuario:** admin  
**Contraseña:** sadmin

### Usuarios web
**Supervisor de vigilancia:** SurvSup  
**Supervisor de casos:** CaseSup  
**Supervisor de contactos:** ContSup  
**Supervisor de puntos de entrada:** PoeSup  
**Funcionario de laboratorio:** LabOff  
**Funcionario de eventos:** EveOff  
**Usuario nacional:** NatUser  
**Clínico nacional:** NatClin  

### Usuarios de aplicaciones móviles
**Funcionario de vigilancia:** SurvOff  
**Informante de hospital:** HospInf  
**Informante de punto de entrada:** PoeInf  

# Actualizar Keycloak

## Instalación independiente

Actualizar Keycloak 11 a 12 siguiendo los pasos de https://www.keycloak.org/docs/latest/upgrading/#_upgrading

1. Detenga el servidor anterior y asegúrese de eliminar cualquier conexión abierta a la base de datos
2. Respalde la base de datos *(una vez realizada la actualización, la versión anterior no se puede utilizar con la nueva versión de la base de datos.)*
3. Respalde la instalación anterior
4. Elimine `${UBICACIÓN_ANTERIOR_KEYCLOAK}/standalone/data/tx-object-store/`
5. Descargue la nueva instalación de Keycloak en https://www.keycloak.org/downloads
6. Copie el directorio `${NUEVA_UBICACIÓN_KEYCLOAK}/standalone/` de la instalación anterior sobre el directorio de la nueva instalación
7. Copie el módulo postgres de `${UBICACIÓN_ANTERIOR_KEYCLOAK}/modules/system/layers/keycloak/org/` al nuevo directorio de instalación
8. Copie los temas de SORMAS de `{UBICACIÓN_ANTERIOR_KEYCLOAK}/themes/` al nuevo directorio de instalación
9. Mientras la nueva instalación está detenida, ejecute `${NUEVA_UBICACIÓN_KEYCLOAK}/bin/jboss-cli.sh ----file=${NUEVA_UBICACIÓN_KEYCLOAK}/bin/migrate-standalone.cli` *(`.bat` para Windows)*
10. Inicie la nueva instalación de Keycloak con `${NUEVA_UBICACIÓN_KEYCLOAK}/bin/standalone.sh` *(`.bat` para Windows)*

## Instalación de Docker

La instalación de docker se actualiza automáticamente a la versión más reciente especificada en Dockerfile.

**Prerrequisitos:** Asegúrese de que la base de datos esté respaldada, porque una vez realizada la actualización, la nueva base de datos no se podrá utilizar con la versión anterior de Keycloak.

Para obtener más información, consulte la [Documentación de Keycloak Docker](https://github.com/hzi-braunschweig/SORMAS-Docker/blob/development/keycloak/README.md).