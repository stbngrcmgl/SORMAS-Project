# ¿Cómo agregar una nueva enfermedad?

Esta guía explica cómo agregar una nueva enfermedad a SORMAS y cómo configurar los campos existentes para que estén disponibles u ocultos en los formularios de casos.

## 0. Preparación

Defina cuáles de los campos de datos existentes para casos y contactos deben estar disponibles para la nueva enfermedad.
La mejor forma de hacerlo es seguir estos pasos:

1. Descargue el diccionario de datos más reciente https://github.com/hzi-braunschweig/SORMAS-Project/blob/development/sormas-api/src/main/resources/doc/SORMAS_Data_Dictionary.xlsx
2. Revise todos los campos de persona, caso, contacto, y sus subentidades: síntomas, datos epidemiológicos, etc.
   El [POE para agregar nuevas enfermedades a SORMAS](SOP_DISEASES.md) explica esto detalladamente.
3. Si falta algún campo necesario para su enfermedad, consulte la guía [¿Cómo agregar un nuevo campo?](GUIDE_ADD_NEW_FIELD.md).
   
## I. Añadir la nueva enfermedad

1. Abra la clase enum Disease en el proyecto API y agregue la nueva enfermedad. Póngala en orden alfabético (con la excepción de "OTHER").
2. Agregue el nombre de la enfermedad al archivo de recursos de traducción enum.properties. También puede agregar un nombre corto.
3. **Muy importante**: Ahora hemos realizado un cambio en la API. ¡Las versiones anteriores ya no son compatibles!
   Cuando se envíen datos con la nueva enfermedad a un dispositivo móvil con una versión anterior, no se reconocerá el tipo de enfermedad y habrá excepciones en el dispositivo.
   Para evitar esto, se debe hacer lo siguiente:
   * Abra el método InfoProvider.getMinimumRequiredVersion.
   * Especifique la versión como la versión de desarrollo actual (sin el -SNAPSHOT). Puede encontrar la versión actual en el archivo de configuración maven pom.xml.
4. Agregue un color para la enfermedad al archivo de estilos disease.scss y al método CssStyles.getDisease en sormas-ui
5. SORMAS admite un modo de entrada de datos simplificado que se puede utilizar durante los brotes.
   Su propósito es reducir al mínimo la cantidad de campos que deben ingresar los usuarios para disminuir la carga de trabajo.
   Si desea que la enfermedad admita este modo, debe incluir la enfermedad en el método Disease.isSupportingOutbreakMode.

## II. Configurar los campos para la nueva enfermedad

De forma predeterminada, un caso de la nueva enfermedad solo usará campos marcados con "All" en el diccionario de datos.
Es probable que su enfermedad deba mostrar campos adicionales. Realice los siguientes pasos para todas las entidades afectadas:

1. Abra la clase de API de la entidad (por ejemplo, SymptomsDto) y revise todos los campos miembros.
2. Agregue la enfermedad a la anotación Diseases existente.

Eso es todo. Los formularios de la aplicación web y la aplicación de Android evaluarán automáticamente la anotación para decidir qué campos mostrar.
