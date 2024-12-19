# BibliotecaBD
## Acerca de
Este es el repositorio correspondiente al proyecto final para la materia Base de Datos. El diseño de la aplicación está basado en el provisto por el profesor Carlos Cortes Bazan, con varias modificaciones para adaptarlo a la base de datos diseañada.  
Requiere de la base de datos correspondiente (provista en su totalidad dentro de este mismo repositorio).  
La aplicación emula la administración de una biblioteca, completa con registros para sucursales, empleados, libros, usuarios, préstamos, direcciones, editoriales, entre otros.
Los detalles de los campos y relaciones entre estos tipos de registros se describen en el diagrama relacional provisto.  
Para una mejor experiencia de usuario, la aplicación divide los registros en tres secciones principales: Préstamos, Libros y Clientes, cada una con la posibilidad de consultar, editar, agregar y eliminar registros de distintos tipos.  
Debido a que la vista de la información en grandes cantidades puede ser difícil dentro de la aplicación, también se permtie al usuario generar reportes en ventanas independientes o, incluso, en
archivos PDF.  
## Sobre montar la BD
El archivo ./src/dataBase/Database/build.sql se encarga de crear la estructura de la BD, sus usuarios y roles, así como de exportar toda la información contenida al momento de redactar este documento. El único requerimiento para montar la BD es contar con Oracle SQL.
## Dependencias
* Jasper Reports 5.6.0  
* Oracle Database JDBC driver 7
