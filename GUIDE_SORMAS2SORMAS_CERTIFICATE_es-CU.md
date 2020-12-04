# ¿Cómo crear y agregar certificados?

Esta guía explica cómo:
 * crear un nuevo certificado autofirmado, utilizado para la comunicación SORMAS a SORMAS
 * configurar el archivo de lista de direcciones de servidor
 * agregar certificados de otras instancias de SORMAS al truststore local
 * agregar otros servidores a la lista de servidores locales
 * manejar certificados ssl autofirmados en sistemas de prueba
   
### Prerrequisitos

Se necesita Java, porque keytool se utiliza para la importación de certificados. <br/>
Vea [Instalar Java](SERVER_SETUP.md#java-11)

### Usar el script de generación de certificados

1. Ejecute ``bash ./s2s-generate-cert.sh``
2. Si la variable de entorno ``SORMAS2SORMAS_DIR`` no está disponible, el script buscará ``/opt/sormas2sormas`` por defecto. 
   Si no encuentra esa ubicación, le pedirá que proporcione la ruta del directorio *sormas2sormas*.
3. Si la variable de entorno ``SORMAS_DOMAIN_DIR`` no está disponible, el script buscará ``/opt/domains/sormas`` por defecto.<br>
   Si no encuentra esa ubicación, le pedirá que proporcione la ruta del *directorio de dominio de sormas*.
   >Si no tiene una instalación local de sormas, por ejemplo, si está utilizando el entorno de docker, 
   >puede especificar cualquier directorio existente y, finalizado el script, encontrará allí un archivo ``sormas.properties``,
   >que contiene la configuración necesaria que se debe agregar al archivo ``sormas.properties`` de su instalación
4. Para la generación del certificado, se necesitan los siguientes datos:
   un identificador de la *Organización*, el nombre de la *Organización*, el nombre de host del servidor de SORMAS, el puerto **https** del servidor, una contraseña para el keystore del certificado, y una contraseña para el usuario REST que se usará al compartir datos a través de la api REST. Estos se pueden configurar en variables de entorno (recomendado), o proporcionarse manualmente a medida que se ejecuta el script.

    * el identificador de la variable de entorno de la *Organización* se debe llamar ``SORMAS_ORG_ID``. 
    Esta variable también se utiliza como *Common Name* (CN) del certificado<br/>
    **Importante**: para Alemania, este valor debe ser el Code Site SurvNet de SORMAS (por ejemplo, 2.99.1.01. si el Code Site normal es 1.99.1.01.). <br/>
    * el identificador de la variable de entorno del nombre de la *Organización* (O) se debe llamar ``SORMAS_ORG_NAME``.<br/>
    **Importante**: para Alemania, este valor debe ser el nombre del Departamento de Salud (Gesundheitsamt) 
    al que se asignará la instancia de SORMAS. <br/>
    por ejemplo, *GA Musterhausen*
    * la variable de nombre de host debe llamarse ``SORMAS_HOST_NAME``. <br/>
    por ejemplo, *sormas.gesundheitsamt-musterhausen.de* 
    * la variable de entorno del puerto https debe llamarse ``SORMAS_HTTPS_PORT``. Si no se encuentra, se le pedirá que la proporcione. 
    Si presiona Enter sin escribir un número de puerto, se usará el 443 predeterminado.
    * La variable de entorno de contraseña debe llamarse ``SORMAS_S2S_CERT_PASS``. Tenga en cuenta que la contraseña debe tener 
    al menos 6 caracteres, o se le pedirá una nueva.
    * la variable de entorno de contraseña de usuario REST debe llamarse ``SORMAS_S2S_REST_PASSWORD``.
    Tenga en cuenta que la contraseña debe tener al menos 12 caracteres, o se le pedirá una nueva.
    
5. Después de proporcionar los datos solicitados, se generarán los archivos del certificado. <br/>
   El certificado generado tiene una vigencia de 3 años. 
   Los archivos del certificado estarán disponibles en el directorio raíz de SORMAS, en la carpeta ``/sormas2sormas``.
6. También se generará un archivo CSV que contiene los datos de acceso para esta instancia en la carpeta ``/sormas2sormas``.
   Se llamará ``{nombre de host}-server-access-data.csv``.
   El archivo contendrá el identificador de la organización, el nombre de la organización, el nombre de host, y la contraseña de usuario REST.<br/>
7. El archivo ``.p12`` generado no debe compartirse con terceros. <br/>
   El archivo ``.crt`` generado será verificado y compartido con otras instancias de SORMAS, de las cuales esta instancia
   podrá solicitar datos. Inversamente, para permitir que otras instancias de SORMAS soliciten datos de esta instancia, 
   sus archivos de certificado se deben obtener y agregar al truststore local. El archivo ``server-access-data.csv``
   también deberá compartirse para que los datos de acceso de esta instancia sean conocidos por otras instancias. 
   Se pueden encontrar más detalles en la siguiente sección.
8. El script especificará automáticamente las propiedades relevantes en el archivo ``sormas.properties``.

### Agregar un nuevo certificado al truststore

Para permitir que otras instancias de SORMAS envíen y reciban datos de esta instancia, sus certificados deben agregarse 
al truststore de esta instancia. Además, los datos de acceso de otras instancias deben agregarse a la lista de servidores 
locales. Para completar esta configuración, siga los siguientes pasos:
1. Ejecute ``bash ./s2s-import-to-truststore.sh``
2. Si la variable de entorno ``SORMAS2SORMAS_DIR`` no está disponible, el script buscará ``/opt/sormas2sormas`` por defecto. 
   Si no encuentra esa ubicación, le pedirá que proporcione la ruta del directorio *sormas2sormas*.
3. Si la variable de entorno ``SORMAS_DOMAIN_DIR`` no está disponible, el script buscará ``/opt/domains/sormas`` por defecto.
   Si no encuentra esa ubicación, le pedirá que proporcione la ruta del *directorio de dominio de sormas*.
   >Si no tiene una instalación local de sormas, por ejemplo, si está utilizando el entorno de docker, 
   >puede especificar cualquier directorio existente y, finalizado el script, encontrará allí un archivo ``sormas.properties``
   >que contiene la configuración necesaria que se debe agregar al archivo ``sormas.properties`` de su instalación
 
4. Si ``sormas2sormas.truststore.p12`` no se encuentra en la carpeta ``/sormas2sormas``, será creado. 
    La contraseña del truststore se puede proporcionar en una variable de entorno ``SORMAS_S2S_TRUSTSTORE_PASS``.
    * Si dicha variable de entorno no está disponible, la contraseña del truststore se buscará en el archivo 
    ``sormas.properties``.
    * Si no se encuentra allí, se le pedirá que proporcione la contraseña del truststore.
    * El script especificará automáticamente las propiedades relevantes en el archivo ``sormas.properties``.
5. Si el archivo de lista de direcciones de servidor, ``server-list.csv``, no se encuentra en la carpeta ``/sormas2sormas``, también será creado.
6. Se le pedirá que proporcione el *nombre de host* de la organización cuyo certificado se está importando. 
   Si el certificado se generó con el script `s2s-generate-cert.sh`, el identificador se puede encontrar al principio del archivo.
   Este certificado debe estar en la carpeta ``/sormas2sormas``. 
7. Después de proporcionar los datos solicitados, el certificado se importará al truststore.
8. El contenido del archivo ``server-access-data.csv`` proporcionado junto al certificado será copiado al archivo ``server-list.csv``.  
9. Ahora puede eliminar los archivos ``.crt`` y ``server-access-data.csv``.

10. *Opcional para sistemas de prueba y otros sistemas con certificados ssl autofirmados* <br>
    Debe importar el certificado SSL del otro servidor al ``cacerts.jks`` de su dominio de sormas.
    * Para obtener el certificado SSL puede usar ``openssl`` <br>
        por ejemplo
        ```shell script
        openssl s_client -showcerts -servername sormas.gesundheitsamt-musterhausen.de -connect sormas.gesundheitsamt-musterhausen.de:443 </dev/null | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > certificate.cer        
        ```
    * Para importar el certificado SSL puede usar ``keytool`` <br>
        por ejemplo
        ```shell script
        keytool -importcert -trustcacerts -noprompt -keystore /opt/domains/sormas/config/cacerts.jks -alias sormas_dev -storepass changeit -file certificate.cer
        ```
        Tenga en cuenta que el alias solo se puede usar una vez.
        
Una vez que se genera el certificado y se importa al menos otro certificado, 
en algunas páginas de la aplicación verá un nuevo recuadro con un botón *Share* e información sobre compartir.
  