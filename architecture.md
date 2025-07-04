structure hexagonale / modulaire

kredika_app/
├── api/            <-- Contrôleurs REST (ports d'entrée)
├── application/    <-- Cas d’usage, services métiers
├── domain/         <-- Modèle métier (entités, DTOs, interfaces)
├── infrastructure/ <-- Implémentations techniques (JPA, WebClient, Keycloak, Orange/Wave)
├── common/         <-- Utilitaires, gestion des erreurs, réponses standard
└── config/         <-- Configuration (Security, Swagger, Auditing, etc.)


Contient les contrôleurs REST (ports d’entrée) et les objets de requête/réponse.
api/
└── controller/
    ├── UserController.java
    ├── ProductController.java
└── dto/
    ├── request/
    ├── response/


Contient les services métier (use cases) et la logique métier orchestrée.
application/
    └──interfaces
        └──service/
            ├── UserService.java       <-- Interface métier
    └─impl/
        └── UserServiceImpl.java

Noyau fonctionnel : entités JPA, DTOs métier, interfaces d’abstraction.
domain/
    └── model/
        ├── entity/
        ├── dto/
    └── repository/
        └── UserRepository.java     <-- Interface abstraite
    └── mapper/
        └── UserMapper.java         <-- MapStruct

Implémentations concrètes (JPA, WebClient, Keycloak, Intégrations tierces).
infrastructure/
    └── persistence/
        └── repository/
            └── JpaUserRepository.java
    └── external/
        ├── payment/
            ├── OrangeMoneyClient.java
            └── WaveClient.java
        └── auth/
            └── KeycloakService.java

Code transversal : réponses standardisées, exceptions, enums, constantes.
common/
    └── exception/
        └── CustomException.java
    └── response/
        └── CustomResponse.java
    └── util/
        └── DateUtils.java, EmailValidator.java
    └── enums/
        └── UserStatus.java

config/
└── config/
    ├── WebSecurityConfig.java
    ├── SwaggerConfig.java
    ├── JpaAuditingConfig.java
    ├── KeycloakConfig.java


