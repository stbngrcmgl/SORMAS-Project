# Contribuir

## Tabla de contenido

* [Enviar un problema](#submitting-an-issue)
* [Contribuir al proyecto](#contributing-to-the-project)
* [Contribuir al código](#contributing-to-the-code)

## Enviar un problema

Lea las siguientes orientaciones y sígalas al enviar nuevos problemas. Esto nos permite procesar su solicitud lo más rápido posible. Asegúrese de utilizar siempre las plantillas que se proporcionan automáticamente al crear un problema.

Si desea reportar un **problema de seguridad**, siga nuestra guía para [*Divulgación responsable*](SECURITY.md).

**Importante:** Siempre que cree un nuevo problema, **primero busque problemas similares en el repositorio**, para evitar duplicados. Puede hacerlo manualmente, o utilizando la funcionalidad de búsqueda del header y limitando sus resultados al repositorio de SORMAS.

* [Informe de error](#bug-report)
* [Solicitud de cambio](#change-request)
* [Solicitud de característica](#feature-request)

### Informe de error

Al enviar un informe de error, proporcionarnos la mayor cantidad de información posible nos ayuda a identificar la raíz del problema y solucionarlo lo más rápido posible. Idealmente, necesitaríamos los siguientes detalles:

* **Descripción del error:** Una descripción de qué sucedió exactamente, dónde sucedió y bajo qué circunstancias. Proporcione tantos detalles como sea posible.
* **Pasos para reproducir:** Si es posible, proporcione instrucciones paso a paso sobre el orden de las acciones que realizó antes de que ocurriera el error. Esto nos ayuda a reproducirlo en nuestro sistema.
* **Comportamiento esperado:** Describa rápidamente lo que cree que debería haber sucedido en lugar del error que obtuvo.
* **Capturas de pantalla:** Si es posible, haga al menos una captura de pantalla del error e inclúyala en su informe de error. Puede hacer esto simplemente arrastrando y soltando el archivo de imagen en la plantilla que está rellenando.
* **Detalles del sistema:** Díganos qué dispositivo estaba usando, en qué versión de SORMAS ocurrió el error y, dependiendo de si estaba usando la aplicación para móviles o la aplicación web, su versión de Android o el navegador web.
* **Información adicional:** Si hay algo más que desee agregar a su solicitud, puede ponerlo aquí.

### Solicitud de cambio

Al enviar una solicitud de cambio o una solicitud de característica, la descripción detallada de la característica que le gustaría que agreguemos o modifiquemos asegurará que nuestra discusión interna sea más fácil y nos comuniquemos con usted lo más rápido posible.

* **Descripción de la característica:** Describa la característica que le gustaría que cambiemos, tal como está en SORMAS en este momento, para que sepamos el tema del problema.
* **Descripción del problema:** Díganos por qué desea que cambiemos la característica, y qué cree que está mal con la forma en que está diseñada actualmente.
* **Cambio propuesto:** Describa con el mayor detalle posible cómo le gustaría que cambiemos la característica.
* **Posibles alternativas:** Si es posible, proporcione soluciones alternativas en caso de que el cambio propuesto no se pueda implementar por algún motivo.
* **Información adicional:** Si hay algo más que desee agregar a su solicitud, puede ponerlo aquí.

### Solicitud de característica

Al enviar una solicitud de cambio o una solicitud de característica, la descripción detallada de la característica que le gustaría que agreguemos o modifiquemos asegurará que nuestra discusión interna sea más fácil y nos comuniquemos con usted lo más rápido posible.

* **Descripción de la situación:** Describa por qué se requiere la característica propuesta, y cómo exactamente SORMAS no puede hacer lo que usted desea que haga en este momento.
* **Descripción de la característica:** Describa con el mayor detalle posible cómo debería verse la característica propuesta, y qué debería hacer.
* **Posibles alternativas:** Si es posible, proporcione soluciones alternativas en caso de que la característica propuesta no se pueda implementar de la forma en que la describió por algún motivo.
* **Información adicional:** Si hay algo más que desee agregar a su solicitud, puede ponerlo aquí.

## Contribuir al proyecto

Incluso si no es desarrollador, hay muchas cosas en las que puede ayudarnos. Si hay algo que le gustaría hacer, y no encuentra instrucciones aquí, ¡contáctenos en sormas@helmholtz-hzi.de, y háganos saber cómo podemos ayudarlo!

* [Traducir SORMAS](I18N.md)
* [Definir nuevas enfermedades](SOP_DISEASES.md)

## Contribuir al código

Si está interesado en participar en el desarrollo de SORMAS, puede utilizar las siguientes guías para comenzar. Si tiene problemas para configurar su entorno de desarrollo o no sabe en qué puede trabajar, ¡no dude en contactarnos en sormas@helmholtz-hzi.de!

* [Configurar su entorno local](DEVELOPMENT_ENVIRONMENT.md)
* [Realizar pruebas de carga en un servidor de SORMAS](LOAD_TESTING.md)
* [Agregar encabezados de licencia](ADDING_LICENSE.md)
* [¿Cómo agregar una nueva enfermedad?](GUIDE_ADD_NEW_DISEASE.md)
* [¿Cómo agregar un nuevo campo?](GUIDE_ADD_NEW_FIELD.md)

### Orientaciones para contribuir al desarrollo 

1. Utilice el formateador de código de eclipse (Ctrl+Shift+F) y el formateador de código de Android Studio para el proyecto **sormas-app**. Para no olvidar esto, use acciones de guardado [para su IDE](DEVELOPMENT_ENVIRONMENT.md).
2. Reglas para líneas en blanco (que no se pueden aplicar mediante formato automático):
    - Use una línea en blanco después de una definición de método (pero generalmente no para métodos de una línea como gets/sets o delegaciones).
	- Use una línea en blanco para separar declaraciones dentro de un bloque de código cuando comience un nuevo bloque lógico.
	- No use líneas en blanco después de cada declaración.
    - No use línea en blanco después de la última declaración de un bloque, sino } con la sangría adecuada en la siguiente línea.
    - No use línea en blanco entre dos }.
3. Puede usar ``//@formatter:off`` y ``//@formatter:on`` para encapsular un bloque de código donde el formateo automatizado sea perjudicial para la legibilidad. Trate de usar esto con poca frecuencia, y utilice la sangría adecuada de todos modos.
4. Reglas para comentarios de código:
    - Separe código y comentario: Coloque el comentario antes de la declaración que desea explicar.
5. Cada commit debe estar relacionado con un solo issue en Github y tener una referencia a este issue, así como una breve descripción de lo que se ha hecho en este commit:
   > #61 - added model to define classification, apply automatic case classification whenever a field value changes
6. Cada pull request debe estar relacionada con un solo issue (si es posible). 

### Product Backlog de SORMAS

El tablero **Product Backlog** se utiliza para planificar, refinar y priorizar los tickets para los próximos sprints. 
El orden de arriba a abajo en cada columna refleja la prioridad del producto. El Product Owner es responsable de poner los tickets en el Backlog y mantener actualizada la información de los tickets.

El Product Backlog contiene las siguientes columnas:
* **Backlog:** Issues que han sido identificados por el Product Owner para ser resueltos en los próximos sprints. Puede haber una columna para cada Scrum Team si se ajusta a las necesidades.
* **Sprint n:** Contiene tickets elegidos por el Product Owner para realizar en el sprint nombrado. Se utilizan notas de texto o columnas separadas para separar los issues entre los Scrum Teams. Da un pronóstico de lo que podría suceder en el próximo sprint, y es el punto de partida para la Sprint Planning. Cada ticket que el Development Team no elige para su Sprint Backlog debe ser movido de vuelta a la columna Backlog o un sprint más adelante.
* **Done:** Los tickets que se cierran (generalmente resueltos en el sprint en ejecución) se mueven aquí **automáticamente**. El orden ya no representa la prioridad aquí.


### Sprint Backlog de SORMAS

El tablero **Sprint Backlog** existe para cada Scrum Team y está segmentado en las siguientes categorías:

* **Backlog:** Issues que el Development Team seleccionó para resolver en el sprint actual, pero para los cuales aún no ha comenzado el trabajo. El orden de arriba a abajo en esta columna refleja la prioridad dada por el Product Owner en el momento de la Sprint Planning.
* **In Progress:** Issues que se han asignado a un colaborador, y para los cuales ha comenzado el trabajo.
* **Waiting:** Issues para los cuales ha comenzado el trabajo, y que se han suspendido, por ejemplo, porque requieren acción o retroalimentación de un colaborador externo.
* **Review:** Issues que se han resuelto, pero aún no han sido revisados ​​por otro colaborador. El estado del ticket suele ser **Open**, pero también se permite **Closed** si no es necesario cambiar o fusionar el código.
* **Testing:** Issues que se han revisado y fusionado con la rama **development** para ser probados y verificados en una instancia TEST central. El estado del ticket debe ser **Closed**.
* **Done:** Issues que se han resuelto, revisado, y satisfacen la Definition of Done. El estado del ticket debe ser **Closed**.

El flujo de trabajo general es que cada vez que un colaborador comienza a trabajar en un issue, se **asigna** el issue a sí mismo, y manualmente **mueve el issue** de **Backlog** a **In Progress**. 
Las transiciones a **Waiting** y **Review** también deben realizarse manualmente. Cuando el desarrollador termina todo el trabajo (no es necesario cambiar o fusionar el código, se establece un milestone), el ticket se debe cerrar para pasar automáticamente a **Testing**.
Los tickets aprobados deben moverse manualmente de **Testing** a **Done*.

El proyecto de GitHub se ha configurado para mover **automáticamente** los issues que se cierran a **Testing**, y los issues que se reabren a **In Progress**.

El Development Team es responsable de mantener actualizados los tickets en este tablero, y de asignar el milestone apropiado en el que se va a publicar el trabajo.


### Administrar dependencias

Para administrar bibliotecas de Java como dependencias, Maven las administra y se enumeran en *sormas-base/pom.xml*.  El propósito de una administración centralizada es tener una visión general de las bibliotecas utilizadas y adaptarse a las nuevas versiones.

1. **Módulos de Payara**: Provistos por Payara en *{payara-home}/glassfish/modules* y utilizados en esa versión por otras bibliotecas.
2. **Bibliotecas de dominio**: Provistas en el dominio Payara bajo *{dominio-payara}/lib* para ser utilizables por artefactos desplegados (ear, war). Deben estar enumeradas en *sormas-base/dependencies/serverlibs.pom*. Usualmente para bibliotecas auxiliares que varios artefactos necesitan.
3. **Dependencias de compilación**: Incluidas en los artefactos respectivos que necesiten la dependencia explícitamente. Por lo general, para dependencias que se necesitan singularmente en un artefacto.
4. **Bibliotecas de prueba**: Bibliotecas utilizadas en pruebas automatizadas en uno o más módulos.

Debido a la herramienta independiente de administración de builds Gradle para *sormas-app*, existe una lista redundante de dependencias de compilación en *sormas-app/app/build.gradle*.


### Solución de problemas de Eclipse

Desafortunadamente, al usar eclipse junto con Payara Tools, hay una serie de problemas de despliegue que puede encontrar. Ejemplos de estos son:

* ClassDefNotFoundExceptions después de desplegar los artefactos e iniciar sesión en la aplicación web
* Mensajes de error en eclipse que dicen que el despliegue falló

Hay un par de cosas que puede hacer para solucionar estos problemas:

* Haga una actualización Maven para todos los proyectos
* Detenga y reinicie el servidor
* Vuelva a desplegar los artefactos del servidor

Si el problema ocurrió justo después de extraer nuevo código de GitHub, probablemente la apuesta más segura es comenzar con la actualización Maven. Para la mayoría de los otros problemas, volver a desplegar o, si es necesario, reiniciar el servidor debería ser suficiente.

Cuando tenga problemas como este - `An internal error occurred during: "Polling news feeds".  javax/xml/bind/JAXBContext` - desactive el ajuste `Window --> Preferences --> General --> News --> "Enable automatic news polling"` (puede suceder al ejecutar Eclipse con JDK 11).
