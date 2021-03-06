# Hazelcast-Integration
An example of a Spring mvc project with Hibernate and Hazelcast integration

Este ejemplo es parte de una prueba técnica para InnovationStrategies. 
La solución que se ha implementado para ello ha sido una solución con Spring mvc y Hibernate como tecnología de persistencia. La base de datos es MySQL.
Para la carga de datos en memoria, su posterior distribución y filtrado se ha usado la tecnología Hazelcast (una plataforma escalable para la distribución de datos).
Aquí hubo la duda por varias implementaciones de la misma, pero teniendo en cuenta uno de los puntos claves de la prueba que es la rapidez con la que se cargan los datos en memoria al final he optado por una de ellas.
Las dos implementaciones que se tuvieron en cuenta fueron:

- Hibernate Second Level Cache. Cache de segundo nivel de hibernate con la implementación de hazelcast (L2).
- Implementación directa en la carga de datos de mapas de hazelcast (IMap)

Haciendo las pertinentes pruebas de tiempos, se llegó a la conclusión que para la misma configuración de hazelcast en el archivo hazelcast.xml (dentro del proyecto), la carga de datos en mapas de IMap es más rápida, además de la posterior recogida de los mismo para mostrarlos o filtrarlos.

Aunque desde mi punto de vista la integración con hibernate directa configurando los application-context del proyecto para que se use el cacheo de segundo nivel con la implementación de hazelcast y las entidades para que sean cacheadas es una solución más limpia y cómoda ya que cuando se accede la primera vez a la capa de datos, si los datos no están cacheados, hazelcast se encarga de hacerlo automáticamente al igual que cuando se recogen para la muestra o filtro de los mismos.

Información sobre el proyecto:

- En la clase UserServiceImpl.java se ha añadido la implementación de hazelcast ya también se han añadido comentarios para la consola para medir tiempos. Estos tiempos se miden para saber los tiempos de carga y demás de hazelcast. No se tienen en cuenta tiempos de carga de la vista en JSP.
- Dentro del proyecto hay un archivo src/main/resources/META-INF/config/database.properties en el que se puede configurar la base de datos, ususarios, conexiones y demás. Aunque con el script y la configuración que hay debería funcionar sin problemas y sin modificaciones ya que tiene el usuario default para base de datos user = 'root' y password=''. 
- En la raiz del proyecto se encuentra el archivo database-script.sql para cargar los 150k registros requeridos a la bd. 
- La carpeta IS es el proyecto que puede ser importado directamente a eclipse para ser usado con MAVEN para compilarlo y desplegarlo  haciendo un maven clean and maven install.
- También se puede usar el .war que hay dentro de IS/target para desplegar el proyecto sin necesidad de compilarlo.
- El contenedor usado en la implementación para desplegar la aplicación es TOMCAT. Específicamente ha sido probado con TOMCAT v8.0.
- El tiempo de carga de datos se puede ver en los comentarios antes mencionados. Aunque la mayor parte del tiempo de carga que percibe el usuario es en el rendering de la página JSP que toma mucho tiempo para pintar los 150K resultados en la tabla. Para mejorar esto podría ser paginado aunque no se ha implementado para cargar la JSP con todos los resultados.
- La aplicación una vez desplegada se puede acceder mediante la url http://localhost:8080/InnovationStrategies/ si es así como está configurada en el puerto e ip.




