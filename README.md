Intrucciones para ejecutar el proyecto:

Se debe realizar el pull a la rama dev, la cual es la rama estable del proyecto, en ella se debe crear el archivo application-dev.properties y en ella incluír el dataSource para permitir la conexión
a la base de datos, cabe aclarar que el proyecto se encuentra configurado con las dependencias para usar la base de datos MySQL.

Se debe agregar una propiedad dentro del application-dev.properties, con el siguiente nombre: security.api-key, esta se reutilizará en el filtro de solicitudes. En las solicitudes que se vayan a realizar
se debe incluir el header X-API-KEY con el valor configurado en el application-dev.properties de lo contrario, el servicio retornará un 401.

---TEST---
Para validar las pruebas unitarias realizadas a este servicio, se debe realizar un checkout a la rama test/unit-tests, en ella se encuentran todas las pruebas unitarias realizadas al componente, se puede
proceder a correr las pruebas con el coverage activo para de esta forma validar la cobertura alcanzada en el componente.
