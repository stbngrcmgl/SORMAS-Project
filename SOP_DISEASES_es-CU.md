# POE para agregar nuevas enfermedades a SORMAS
Este archivo define el POE (Procedimiento operativo estándar) que se debe seguir al solicitar que el equipo de desarrollo central agregue nuevas enfermedades al sistema. Responder a todas las preguntas de esta guía asegurará que podamos integrar nuevas enfermedades en SORMAS lo más rápido posible.

## Contenido
1. [Descargar el diccionario de datos](#step-1-download-the-data-dictionary)
2. [Definir los detalles básicos de la enfermedad](#step-2-define-basic-disease-details)
3. [Definir campos de caso existentes](#step-3-define-existing-case-fields)
4. [Definir campos de persona existentes](#step-4-define-existing-person-fields)
5. [Definir los síntomas relevantes](#step-5-define-the-relevant-symptoms)
6. [Definir los datos epidemiológicos relevantes](#step-6-define-the-relevant-epidemiological-data)
7. [Definir las condiciones de salud](#step-7-define-health-conditions)
8. [Definir nuevos campos en otras áreas](#step-8-define-new-fields-in-other-areas)
9. [Proporcionar criterios de clasificación de casos](#step-9-provide-case-classification-criteria)
10. [Proporcionar información adicional](#step-10-provide-additional-information)
11. [Enviar su definición de enfermedad a los desarrolladores de SORMAS](#step-11-send-your-disease-definition-to-the-sormas-developers)

## Guía
### Paso 1: Descargar el diccionario de datos
Descargue el [Diccionario de datos](https://github.com/hzi-braunschweig/SORMAS-Project/raw/development/sormas-api/src/main/resources/doc/SORMAS_Data_Dictionary.xlsx) más reciente de este repositorio y ábralo. Nunca utilice una versión del Diccionario de datos que haya descargado anteriormente, ya que es muy probable que su contenido haya cambiado en el ínterin.

Utilizará el Diccionario de datos para definir todos los detalles de la nueva enfermedad. Asegúrese de marcar cada adición o cambio (por ejemplo, coloreando el texto o el fondo de la fila en un rojo sutil) para que no perdamos la información que ha proporcionado.

### Paso 2: Definir los detalles básicos de la enfermedad
Abra la pestaña **Case** del Diccionario de datos y desplácese hacia abajo hasta las tablas que tienen un fondo azul. Estas tablas definen *enumeraciones*, que son básicamente tipos de datos con valores fijos. Los ejemplos incluyen las diferentes clasificaciones de un caso, el género de una persona, o las enfermedades que se utilizan en SORMAS. Busque la tabla de enumeración **Disease** (consulte la columna *Type*) y agregue una nueva fila. Ingrese los siguientes detalles:

* El **nombre de la enfermedad** en la columna *Caption*
* Opcionalmente, si la enfermedad tiene un nombre largo, un **nombre corto o abreviatura** en la columna *Short*

Utilice la columna *Description* para responder las siguientes preguntas:

* ¿Tiene la enfermedad **seguimiento de contactos**?
  * En caso afirmativo, ¿durante **cuántos días** se debe realizar el seguimiento de contactos?

### Paso 3: Definir campos de caso existentes
Mire las filas en la primera tabla de la pestaña **Case** (que tiene un fondo gris). Esta tabla define todos los campos que se muestran en la pestaña *Información del caso* en la aplicación SORMAS. La columna *Caption* define el nombre del campo tal como se muestra en la interfaz de usuario, mientras que la columna *Diseases* especifica qué enfermedades utilizan este campo. Agregue el nombre (o, si está disponible, el nombre corto) de su nueva enfermedad en la columna "New disease" de cada fila que represente un campo que sea relevante para su nueva enfermedad y coloree.

### Paso 4: Definir campos de persona existentes
Abra la pestaña **Person** y repita el paso 3 para la primera tabla que contiene los campos que definen los detalles de una persona en SORMAS.

### Paso 5: Definir los síntomas relevantes
Abra la pestaña **Symptoms** que enumera todos los síntomas que se utilizan actualmente en SORMAS. Esta es una lista muy larga y tendrá que revisar cada fila y definir si este síntoma es relevante para su nueva enfermedad o no. 

Es posible que su nueva enfermedad tenga uno o más síntomas que actualmente no forman parte de SORMAS. En ese caso, debe agregar una nueva fila para cada uno de estos síntomas al final de la tabla y proporcionar el **nombre del síntoma** en la columna *Caption*.

La mayoría de los síntomas en SORMAS son simples campos *Sí/No/Desconocido* donde *Sí* significa que el síntoma está presente, *No* que el síntoma no está presente, y *Desconocido* que no hay información sobre si el síntoma está presente o no. Si su síntoma puede definirse con este patrón, no tiene que especificar nada más. Sin embargo, si su síntoma es más complejo (por ejemplo, hay una serie de valores predefinidos entre los que el usuario debe elegir), proporcione todos los detalles necesarios sobre cómo los usuarios deben especificar el síntoma en la columna *Description*.

### Paso 6: Definir los datos epidemiológicos relevantes
Abra la pestaña **Epidemiological data** que enumera todos los campos que se utilizan para recolectar información sobre los antecedentes epidemiológicos del caso, por ejemplo, si estuvo presente en entierros, tuvo contacto con un caso confirmado o con animales. Repita el paso 3 para todas las filas de la primera tabla y agregue filas nuevas si su nueva enfermedad requiere información que no se encuentra actualmente en SORMAS. Dado que es probable que los campos nuevos de esta pestaña sean más complejos que los síntomas básicos, asegúrese de definir la mayor cantidad de información posible sobre cómo deberían funcionar en la columna *Description*.

### Paso 7: Definir las condiciones de salud
Abra la pestaña **Health conditions** que contiene una lista de condiciones preexistentes que no son síntomas de la enfermedad, pero siguen siendo relevantes, especialmente para la gestión de casos en un hospital. Repita el paso 3 para todas las filas de la primera tabla y agregue nuevas filas si existen condiciones de salud relevantes para su nueva enfermedad que aún no forman parte de SORMAS. Como siempre con los campos nuevos, asegúrese de proporcionar todos los detalles relevantes en la columna *Description*.

### Paso 8: Definir nuevos campos en otras áreas
Es posible que su enfermedad requiera que se recolecte más información que aún no es compatible con SORMAS, por ejemplo, nuevos detalles sobre la persona, información específica sobre su hospitalización, o incluso campos muy importantes que debieran ir directamente a la información del caso. Puede emplear el mismo proceso que utilizó para definir nuevos campos de síntomas, condiciones de salud, o datos epidemiológicos abriendo la pestaña en cuestión y agregando nuevas filas a la primera tabla. 

---

En este punto, ha terminado todas las definiciones necesarias en el Diccionario de datos. Guarde su trabajo y prepare un correo electrónico con el archivo del Diccionario de datos adjunto. Sin embargo, no envíe este correo electrónico antes de seguir los pasos restantes, ya que todavía hay algunos detalles necesarios para completar la especificación de su nueva enfermedad.

---

### Paso 9: Proporcionar criterios de clasificación de casos
De manera óptima, al definir una nueva enfermedad, también debe especificar los criterios que SORMAS debe utilizar para clasificar automáticamente el caso como sospechoso, probable o confirmado. Para hacer esto de una manera que sea compatible con el sistema que usamos, necesitará acceso a un sistema de SORMAS en ejecución (por ejemplo, el servidor experimental que puede encontrar en https://sormas.org). Inicie sesión como cualquier usuario (por ejemplo, el usuario predeterminado en el servidor experimental), abra la sección *About* en el menú principal y abra el documento *Case Classification Rules (HTML)*. Defina los criterios de clasificación de forma similar al sistema utilizado en este documento. Si está disponible, también puede enviarnos un documento oficial de la OMS o su CCE nacional que especifique los criterios de clasificación.

### Paso 10: Proporcionar información adicional
Si todavía hay cosas que son necesarias para implementar correctamente la nueva enfermedad en SORMAS (es posible que nos solicite crear un área completamente nueva para los casos, o puede haber mecánicas muy complejas que necesitan muchas más especificaciones), indíquenos tantos detalles sobre ellas como sea posible. Simplemente escriba toda esa información en su correo electrónico.

### Paso 11: Enviar su definición de enfermedad al equipo de SORMAS
Envíe su correo electrónico con el archivo actualizado del Diccionario de datos, los criterios de clasificación de casos, y sus notas adicionales a sormas@helmholtz-hzi.de. ¡Felicitaciones, su trabajo está hecho! Ahora deberíamos tener toda la información que necesitamos para integrar su enfermedad en SORMAS. Si hay algo que no está claro o si necesitamos detalles adicionales, nos pondremos en contacto con usted lo antes posible. ¡Muchas gracias por contribuir a SORMAS y ayudarnos a combatir la propagación de tantas enfermedades como sea posible!
