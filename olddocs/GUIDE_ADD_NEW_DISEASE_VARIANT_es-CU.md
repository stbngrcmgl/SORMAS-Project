# ¿Cómo agregar una nueva variante de enfermedad?

Esta guía explica cómo agregar una nueva variante de enfermedad a SORMAS.

## I. Preparación de los datos

El primer paso es obtener los identificadores de las enfermedades.
Se puede encontrar una lista exhaustiva dentro de la enum ```de.symeda.sormas.api.Disease```.

## II. Conservar los datos

1. Ingrese a pgAdmin o al shell de postgreSQL para poder ejecutar las siguientes consultas en la base de datos de PostgreSQL.
2. Reemplace la variable ```{{ DISEASE ID }}``` en la consulta siguiente con el ID de enfermedad tomado de la enum de Java indicada en el paso I.
3. Reemplace la variable ```{{ VARIANT NAME }}``` en la consulta siguiente con el nombre de la variante de enfermedad.
4. Reemplace ```[...]``` con todas las otras filas que necesite agregar.
5. Ejecute la consulta generada.
6. Verifique que la tabla ```diseasevariant``` fue bien completada con sus datos.

Esta es la consulta SQL a ejecutar:

```sql
INSERT INTO diseasevariant (id, uuid, creationdate, changedate, disease, name)
VALUES
  (nextval('entity_seq'), gen_random_uuid(), now(), now(), '{{ DISEASE ID }}', '{{ VARIANT NAME }}'),
  [...];
```

Este es un ejemplo de la consulta con datos de muestra:

```sql
INSERT INTO diseasevariant (id, uuid, creationdate, changedate, disease, name)
VALUES
  (nextval('entity_seq'), gen_random_uuid(), now(), now(), 'YELLOW_FEVER', 'Yellow Fever Variant 1'),
  (nextval('entity_seq'), gen_random_uuid(), now(), now(), 'YELLOW_FEVER', 'Yellow Fever Variant 2'),
  (nextval('entity_seq'), gen_random_uuid(), now(), now(), 'DENGUE', 'Dengue Variant 1'),
  (nextval('entity_seq'), gen_random_uuid(), now(), now(), 'MALARIA', 'Malaria Variant 1'),
  (nextval('entity_seq'), gen_random_uuid(), now(), now(), 'MALARIA', 'Malaria Variant 2');
```

Si PostgreSQL protesta por la función desconocida ```gen_random_uuid()```, significa que la extensión ```pgcrypto``` no está habilitada.
Para habilitarla, inicie sesión con una cuenta de superadmin en su servidor PostgreSQL y ejecute la consulta siguiente: 

```sql
create extension pgcrypto;
```

Eso es todo. Ahora los formularios de la aplicación web y la aplicación de Android tendrán llenos sus campos ```Disease variant```.
