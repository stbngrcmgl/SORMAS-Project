# ¿Cómo agregar un nuevo campo?

Esta guía explica cómo agregar un nuevo campo sin formato al esquema de datos de SORMAS.
**No** explica cómo agregar campos de lista, nuevas secciones, o conceptos a SORMAS.

**Importante:** Esta es la primera versión de esta guía. Póngase en contacto si algo no está claro o si tiene sugerencias sobre cómo mejorar la guía, el código fuente o la arquitectura subyacente.

### Ejemplos de casos de uso

* Se necesita un síntoma para una enfermedad específica (por ejemplo, dolor de cabeza)
* Un campo con detalles epidemiológicos adicionales sobre un caso (por ejemplo, contacto con un tipo especial de animal)

## 0. Preparación (!)

1. Asegúrese de que el campo no esté ya en el sistema. 
   SORMAS tiene muchos campos de datos, y muchos de ellos solo se usan para algunas enfermedades y están ocultos para otras. 
   La mejor manera de asegurarse es abrir el diccionario de datos y revisar los campos existentes de todas las secciones de datos relacionadas: https://github.com/hzi-braunschweig/SORMAS-Project/blob/development/sormas-api/src/main/resources/doc/SORMAS_Data_Dictionary.xlsx
2. Defina claramente el campo:
   * Nombre y descripción
   * Tipo de campo: texto sin formato, valores predefinidos (enumeración), fecha, hora, número
   * Ejemplos de valores
   * ¿Quién puede ingresar el campo?
   * ¿Quién puede leer el campo?
3. [Configure su entorno de desarrollo local](DEVELOPMENT_ENVIRONMENT.md)
   
## I. Agregar el campo a la API de SORMAS

La API de SORMAS es el corazón del esquema de datos. En consecuencia, aquí es donde debe comenzar.

1. Identifique la clase donde debe agregarse el campo. En la mayoría de los escenarios, solo necesitará revisar CaseDataDto.java y todos los campos utilizados allí, por ejemplo, SymptomsDto.
2. Agregue el campo como miembro privado de la clase con métodos get y set. Además, un static final String que se utilizará como constante para identificar el campo.
3. Si el campo tiene valores predefinidos, agregue una enumeración en el paquete de la clase. Mire una de las enumeraciones existentes como referencia.
4. Agregue el título a captions.properties y la descripción a description.properties en los recursos del proyecto. Para las enumeraciones, agregue todos los valores a enum.properties.
   ```
   Symptoms.soreThroat = Sore throat/pharyngitis
   ```
5. Cuando haga adiciones/cambios de claves en ``captions.properties``, ``strings.properties`` o ``validations.properties`` debe ejecutar ``I18nConstantGenerator`` (ejecutar como ... Aplicación Java) para actualizar las clases Constants correspondientes.
6. *Muy importante*: Ahora hemos realizado oficialmente un cambio en la API, lo que probablemente signifique que las versiones anteriores ya no son totalmente compatibles.
   Cuando datos con el nuevo campo son enviados a un dispositivo móvil con una versión anterior, esta no sabrá sobre el campo y los datos se perderán en el dispositivo.
   Cuando los datos se envíen de vuelta al servidor, el campo vacío puede sobrescribir los datos existentes, y ahora también se pierden en el servidor.
   Para evitar esto, se debe hacer lo siguiente:
   * Abra el método InfoProvider.getMinimumRequiredVersion.
   * Establezca la versión como la versión de desarrollo actual (sin el -SNAPSHOT). Puede encontrar la versión actual en el archivo de configuración maven pom.xml.
   
## II. Agregar el campo al backend de SORMAS

El backend de SORMAS es responsable de conservar todos los datos en la base de datos de los servidores, y de hacer accesibles esos datos.
En consecuencia, es necesario extender la lógica de persistencia con el nuevo campo.

1. Identifique la clase de entidad que corresponde a la clase de API donde se agregó el campo (por ejemplo, Case.java).
2. Agregue el campo como miembro privado de la clase de entidad con métodos get y set.
3. Agregue la anotación JPA correcta al método get (revise otros campos para ver ejemplos).
   ```
	@Enumerated(EnumType.STRING)
	public SymptomState getSoreThroat() {
		return soreThroat;
	}
   ```

Además de esto, el archivo sormas_schema.sql en sormas-base/sql debe extenderse:

4. Desplácese hasta el final y agregue un nuevo bloque schema_version. 
   Comienza con un comentario que contiene la fecha y una breve información sobre los cambios y la id de issue de github, y termina con "INSERT INTO schema_version..." donde la versión debe incrementarse.
   ```
   -- 2019-02-20 Additional signs and symptoms #938
   
   INSERT INTO schema_version (version_number, comment) VALUES (131, 'Additional signs and symptoms #938');
   ```
5. Dentro de este bloque, agregue una nueva columna a la tabla que corresponda a la entidad donde se agregó el nuevo campo en sormas-backend.
   Puede desplazarse hacia arriba para ver ejemplos de esto para los diferentes tipos de campos. Tenga en cuenta que el nombre de la columna es todo en minúsculas.
   ```
   ALTER TABLE symptoms ADD COLUMN sorethroat varchar(255);
   ```
6. Asegúrese de agregar también la columna a la correspondiente tabla de historial en la base de datos.
7. Actualice los valores predeterminados si es necesario.
8. ¡Intente ejecutar el SQL en su sistema!

Ahora debemos asegurar que los datos del nuevo campo sean intercambiados entre las clases de entidad del backend y los objetos de transferencia de datos de la API.

9.  Identifique la clase *FacadeEjb para la entidad (por ejemplo, CaseFacadeEjb).
10. Extienda los métodos toDto y fromDto/fillOrBuildEntity para intercambiar datos entre la clase de API y la clase de entidad del backend que se conserva.
    ```
	target.setSoreThroat(source.getSoreThroat());
    ```
Ahora debemos asegurar que la exportación detallada exporte los datos del nuevo campo.

11. Identifique la correspondiente *ExportDto (por ejemplo, CaseExportDto)
12. Agregue el campo como miembro privado de la clase dto con métodos get y set.
13. Agregue la anotación @Order en el método get del nuevo campo
    ```
    @Order(33)
    public SymptomState getSoreThroat() {
        return soreThroat;
    }
    ```
    > **NOTA**:  Los números @Order deben ser únicos; incremente el orden de los métodos get siguientes si los hay.
14. Inicialice el nuevo campo en el constructor.
15. Agregue el nuevo campo en la lista de selección del método `getExportList` de *FacadeEJB
    ```
    cq.multiselect(
        ...,
        caseRoot.get(Case.SORE_THROAT),
        ...
    )
    ``` 
    > **NOTA**: Asegure que el orden de los campos en la lista de selección corresponda con el orden de los argumentos en el constructor de la clase *ExportDto  
### III. Agregar el campo a la interfaz de usuario de SORMAS

La interfaz de usuario de SORMAS es la aplicación web que utilizan supervisores, usuarios de laboratorio, instancias nacionales y otros.
Aquí tenemos que extender el formulario donde se debe mostrar y editar el campo. Tenga en cuenta que la aplicación web utiliza el mismo formulario para los modos de lectura y escritura, por lo que el campo solo debe agregarse una vez.

1. Identifique la clase Form donde se debe mostrar el nuevo campo. Ejemplos son SymptomsForm.class, CaseDataForm.class o EpiDataForm.class.
2. Agregue el nuevo campo a la definición de diseño HTML en la parte superior de la clase de formulario. Los formularios utilizan diseños de columnas basados ​​en la biblioteca CSS de arranque (bootstrap).
   ```
   LayoutUtil.fluidRowLocs(SymptomsDto.TEMPERATURE, SymptomsDto.TEMPERATURE_SOURCE) +
   ```
3. Vaya al método addFields del formulario y agregue el campo. 
   Puede agregarlo sin definir un tipo de campo de interfaz de usuario; esto usará un tipo de campo de interfaz de usuario predeterminado según el tipo del campo de datos (vea SormasFieldGroupFieldFactory):
   ```
   addFields(EpiDataDto.WATER_BODY, EpiDataDto.WATER_BODY_DETAILS, EpiDataDto.WATER_SOURCE);
   ```
   O puede definir el tipo de campo de interfaz de usuario que se debe usar, y proporcionar inicialización adicional para el campo:
   ```
   ComboBox region = addField(CaseDataDto.REGION, ComboBox.class);
   region.addItems(FacadeProvider.getRegionFacade().getAllAsReference());
   ```   
4. La clase FieldHelper proporciona métodos para hacer que el campo sea visible, obligatorio o de solo lectura en forma condicional, según los valores de otros campos.
5. Por último, pruebe la aplicación web para comprobar si todo funciona como se espera.

### IV. Agregar el campo a la aplicación de Android de SORMAS

La aplicación de Android de SORMAS se sincroniza con el servidor mediante la interfaz ReST de SORMAS. La aplicación tiene su propia base de datos para conservar todos los datos del usuario para uso sin conexión. Por tanto, es necesario extender las clases de entidad utilizadas por la aplicación.

1. Identifique la clase de entidad en el subpaquete de backend de sormas-app.
2. Agregue el campo como miembro privado de la clase de entidad con métodos get y set.
3. Agregue la anotación JPA u ORM-lite correcta al miembro privado (revise otros campos para ver ejemplos).
   Nota: En el futuro, esto puede reemplazarse utilizando Android Room.
4. Identifique la clase *DtoHelper para la entidad (por ejemplo, CaseDtoHelper).
5. Extienda los métodos fillInnerFromAdo y fillInnerFromDto para intercambiar datos entre la clase de API y la clase de entidad de la aplicación que se conserva.

SORMAS permite a los usuarios actualizar desde versiones anteriores de la aplicación. Por lo tanto, es necesario agregar el SQL necesario al método onUpgrade de la clase DatabaseHelper.

6. Incremente la variable DATABASE_VERSION en la clase DatabaseHelper.
7. Vaya al final del método onUpgrade y agregue un nuevo bloque case que defina cómo actualizar a la nueva versión.
8. Ejecute el SQL necesario utilizando el DAO (objeto de acceso a la base de datos) de la clase de entidad. 
   En su mayoría, puede usar el mismo SQL que se utilizó para agregar el campo al backend de SORMAS. El nombre de la columna debe coincidir con el nombre del campo en la clase de entidad (no todo en minúsculas).
   ```   
	getDao(Symptoms.class).executeRaw("ALTER TABLE symptoms ADD COLUMN soreThroat varchar(255);");
   ```   
					
La aplicación de SORMAS tiene fragmentos separados que se utilizan para actividades de lectura y edición. Cada fragmento se divide en el archivo de diseño xml y la clase java que contiene su lógica.

9. Identifique el archivo de diseño xml del fragmento de edición donde se debe agregar el campo. Por ejemplo, /res/layout/fragment_symptoms_edit_layout.xml
10. Agregue el campo al diseño. Use los campos existentes como referencia. Nuestros componentes personalizados de Android agregan automáticamente títulos y descripciones al campo.
    ```
    <de.symeda.sormas.app.component.controls.ControlSwitchField
        android:id="@+id/symptoms_soreThroat"
        app:enumClass="@{symptomStateClass}"
        app:slim="true"
        app:value="@={data.soreThroat}"
        style="@style/ControlSingleColumnStyle" />
    ```
    Tenga en cuenta que esto viene con vinculación de datos automática. Utilice "@={...}" para campos de edición y "@{...}" para campos de lectura.
11. Identifique la clase java del fragmento de edición. Por ejemplo, SíntomasEditFragment.java
12. Agregue las inicializaciones de campo necesarias al método onAfterLayoutBinding existente. Si es necesario, puede preparar los datos en el método prepareFragmentData, mientras el fragmento todavía se está cargando.
   Dado que hay muchos casos de uso, revise las clases existentes.
13. Haga lo mismo con el fragmento de lectura, y posiblemente también cree el fragmento.
14. Finalmente, pruebe la aplicación para verificar si todo funciona como se espera. Asegure que los datos ingresados ​​en el dispositivo Android son sincronizados correctamente y también aparecen en el servidor.

Ahora ha terminado :-)