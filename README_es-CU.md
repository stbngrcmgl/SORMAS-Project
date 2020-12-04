<p align="center">
  <a href="https://sormas.org/">
    <img
      alt="SORMAS - Surveillance, Outbreak Response Management and Analysis System"
      src="logo.png"
      height="200"
    />
  </a>
  <br/>
  <a href="https://github.com/hzi-braunschweig/SORMAS-Project/blob/development/LICENSE"><img alt="License" src="https://img.shields.io/badge/license-GPL%20v3-blue"/></a> <a href="https://github.com/hzi-braunschweig/SORMAS-Project/releases/latest"><img alt="Latest Release" src="https://img.shields.io/github/v/release/hzi-braunschweig/SORMAS-Project"/></a> <img alt="Development Build Status" src="https://travis-ci.com/hzi-braunschweig/SORMAS-Project.svg?branch=development"/> <a href="https://gitter.im/SORMAS-Project"><img alt="Gitter" src="https://badges.gitter.im/SORMAS-Project/dev-support.svg"/></a>
</p>
<br/>

**SORMAS** (Surveillance Outbreak Response Management and Analysis System) es un sistema de eHealth de código abierto, compuesto por aplicaciones independientes para web y móviles, que está orientado a optimizar los procesos utilizados para monitorear la propagación de enfermedades infecciosas y responder a situaciones de brotes.

#### ¿Cómo funciona?
¡Puede probar SORMAS en nuestro servidor demostrativo en https://sormas.helmholtz-hzi.de!

#### ¿Cómo puedo participar?
Lea nuestro [*léame sobre contribuir*](CONTRIBUTING.md) y contáctenos en sormas@helmholtz-hzi.de, o únase a nuestro [chat de desarrolladores en Gitter](https://gitter.im/SORMAS-Project) para aprender cómo puede ayudar a impulsar el desarrollo de SORMAS y obtener soporte de desarrollo de nuestros desarrolladores principales. SORMAS es un proyecto impulsado por la comunidad, ¡y nos encantaría tenerle a bordo! Si desea contribuir al código, siga estrictamente la guía sobre el [*entorno de desarrollo*](DEVELOPMENT_ENVIRONMENT.md) para asegurar que todo esté configurado correctamente. Asegúrese también de haber leído las [*pautas para contribuir al desarrollo*](CONTRIBUTING.md#development-contributing-guidelines) antes de comenzar a desarrollar.

#### ¿Cómo puedo reportar un error o solicitar una característica?
[Cree un nuevo issue](https://github.com/hzi-braunschweig/SORMAS-Project/issues/new/choose) y lea la guía sobre [*enviar un problema*](CONTRIBUTING.md#submitting-an-issue) para obtener instrucciones más detalladas. ¡Apreciamos su ayuda!

#### ¿Qué navegadores y versiones de Android son compatibles?
SORMAS es oficialmente compatible con, y se prueba en, **navegadores basados ​​en Chromium** (como Google Chrome) y **Mozilla Firefox**, y todas las versiones de Android a partir de **Android 7.0** (Nougat). En principio, SORMAS debería poder utilizarse con todos los navegadores web compatibles con Vaadin 8 (Chrome, Firefox, Safari, Edge, Internet Explorer 11; consulte https://vaadin.com/faq).

#### ¿Existe documentación de la API ReST?
¡Si! Descargue la [última distribución](https://github.com/hzi-braunschweig/SORMAS-Project/releases/latest) y copie el contenido de /deploy/openapi/sormas-rest.yaml en un editor que genere una documentación visual de la API (por ejemplo, https://editor.swagger.io/).

<p align="center"><img src="https://user-images.githubusercontent.com/23701005/74659600-ebb8fc00-5194-11ea-836b-a7ca9d682301.png"/></p>

## Estructura del proyecto
El proyecto consta de los siguientes módulos:

- **sormas-api:** Lógica general y definiciones para el intercambio de datos entre la aplicación y el servidor
- **sormas-app:** La aplicación de Android
- **sormas-backend:** Servicios de entidad de servidor, fachadas, etc.
- **sormas-base:** Proyecto base que también contiene scripts de construcción
- **sormas-ear:** El ear necesario para construir la aplicación
- **sormas-rest:** La interfaz REST; vea [`sormas-rest/README.md`](sormas-rest/README.md)
- **sormas-ui:** La aplicación web
- **sormas-base/dependencies:** Dependencias que se desplegarán con el servidor payara
- **sormas-cargoserver:** Configuración para un servidor de desarrollo local usando maven-cargo

## Gestión del servidor

* [Instalar un servidor de SORMAS](SERVER_SETUP.md)
* [Actualizar un servidor de SORMAS](SERVER_UPDATE.md)
* [Personalizar un servidor de SORMAS](SERVER_CUSTOMIZATION.md)
