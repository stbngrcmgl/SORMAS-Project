# Crear una aplicación demo para un servidor demo de SORMAS

**Importante**: Esto solo es aplicable si ha [configurado su propio servidor de SORMAS](SERVER_SETUP.md) para fines **demostrativos** y desea que los usuarios accedan fácilmente al mismo.

## Paso 1: Ajuste sormas-app.properties
1. Abra el archivo apk de la distribución de SORMAS con un editor de zip (por ejemplo, 7zip).
2. Extraiga sormas-app.properties y abra el archivo para editar.
3. Establezca server.url.default como la URL de la interfaz ReST de su servidor de SORMAS.
4. Establezca user.name.default y user.password.default para el usuario demo (debe ser un informante o funcionario).
5. Sobrescriba sormas-app.properties en el apk con su versión modificada.

## Paso 2: Firme el archivo apk modificado
Dado que el archivo apk fue cambiado, se debe firmar de nuevo.\\
**Importante**: Cuando cambia y firma el archivo apk, ¡este ya no es compatible con el archivo apk original para la actualización automática de la aplicación! Si aún desea que esto funcione, siempre debe firmar las nuevas versiones usando el mismo keystore, y poner el archivo apk modificado en su servidor de SORMAS para la actualización automática de la aplicación.\\

1. Cree un keystore usando keytool: <code>keytool -genkey -v -keystore mi-clave-demo.jks -keyalg RSA -keysize 2048 -validity 10000 -alias mi-alias</code>
   > Nota: keytool se encuentra en el directorio bin/ en su JDK. Para encontrar su JDK desde Android Studio, seleccione File > Project Structure, haga clic en SDK Location, y verá la ubicación del JDK. 
2. Descargue uber-apk-signer: https://github.com/patrickfav/uber-apk-signer/releases. 
   > Nota: Esta es la forma conveniente de hacerlo. También puede obtener un SDK de Android y seguir las instrucciones que se dan aquí: https://developer.android.com/studio/publish/app-signing#signing-manually
3. Firme el archivo apk: <code>java -jar uber-apk-signer.jar --ks mi-clave-demo.jks -ksAlias mi-alias --alowResign --apks sormas-version-demo.apk</code>
   > Vea también: https://github.com/patrickfav/uber-apk-signer#command-line-interface