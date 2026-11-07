# Spring Boot : Application PetClinic

Proyecto base del laboratorio de Pruebas Unitarias con Base de Datos.

## Stack

- Java 21
- Spring Boot 3.5.x (Data JPA, Data REST, Web)
- H2 en memoria (perfil `h2`) y MySQL (perfil `mysql`)
- JUnit 5 + Mockito
- Lombok + MapStruct

## Base de datos

`src/main/resources/schema.sql` y `src/main/resources/data.sql` crean y cargan las tablas:

| Tabla | Entidad | Servicio | Prueba |
|---|---|---|---|
| vets | Vet | VetService | VetServiceTest |
| owners | Owner | OwnerService | OwnerServiceTest |
| specialties | Specialty | SpecialtyService | SpecialtyServiceTest |
| visits | Visit | VisitService | VisitServiceTest |
| types | PetType | PetTypeService | TypeServiceTest |
| vet_specialties | VetSpecialty | VetSpecialtyService | VetSpecialtyServiceTest |

El perfil activo por defecto es `h2`, de modo que las pruebas se ejecutan sin instalar ningun motor de base de datos.

## Ejecutar las pruebas

Todas:

```
.\mvnw.cmd test
```

Una clase (un caso del laboratorio):

```
.\mvnw.cmd test -Dtest=VetServiceTest
.\mvnw.cmd test -Dtest=OwnerServiceTest
.\mvnw.cmd test -Dtest=SpecialtyServiceTest
.\mvnw.cmd test -Dtest=VisitServiceTest
.\mvnw.cmd test -Dtest=TypeServiceTest
.\mvnw.cmd test -Dtest=VetSpecialtyServiceTest
```

Un metodo:

```
.\mvnw.cmd test "-Dtest=VetServiceTest#testCreateVet"
```

Los reportes quedan en `target/surefire-reports`.

## Ejecutar la aplicacion

```
.\mvnw.cmd spring-boot:run
```

Con MySQL:

```
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=mysql"
```
