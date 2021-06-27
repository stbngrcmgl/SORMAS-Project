# Agregar encabezados de licencia

## Encabezado de licencia
Utilice el siguiente encabezado para todos los archivos de código fuente de nueva creación:

```
SORMAS® - Surveillance Outbreak Response Management & Analysis System
Copyright © 2016-2020 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <https://www.gnu.org/licenses/>.
```

## Eclipse
- Utilice la herramienta Releng de eclipse para agregar automáticamente encabezados de licencia a todos los archivos de código fuente relevantes (vea https://www.codejava.net/ides/eclipse/how-to-add-copyright-license-header-for-java-source-files-in-eclipse para una guía de uso)
- Después de instalar la herramienta, abra Window > Preferences > Copyright Tool, y pegue el encabezado de licencia de arriba en el área de texto de la plantilla
- Asegúrese de seleccionar "Replace all existing copyright comments with this copyright template" y especialmente "Skip over XML files" (para asegurar que los encabezados no se agreguen a, por ejemplo, archivos de build)
- Siempre que cree un nuevo archivo de código fuente: haga clic derecho en el archivo y seleccione "Fix Copyrights"

## Android Studio/IntelliJ
- Abra File > Settings > Editor > Copyright > Copyright Profiles
- Cree un nuevo perfil y pegue el encabezado de licencia de arriba en el área de texto Copyright
- Vuelva a las preferencias generales de Copyright y seleccione el nuevo perfil como "Default project copyright"
- (Opcional: Si el año ha cambiado, haga clic derecho en todos los proyectos que contengan código manual, y seleccione "Update Copyright...", seleccione "Custom Scope" y, en el menú desplegable, seleccione "Project Source Files"; haga clic en "Ok" y espere hasta que la licencia se haya agregado/cambiado en todos los archivos)
- Posteriormente, Android Studio agrega automáticamente la licencia a los archivos de nueva creación
