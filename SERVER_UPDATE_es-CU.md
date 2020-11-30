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
