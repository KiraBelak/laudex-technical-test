# 📌 Sistema de Gestión de Solicitudes de Crédito

## 🌐 Links
- [Demo en vivo](https://salmon-island-08ebe3310.4.azurestaticapps.net)
- [Repositorio Frontend](https://github.com/KiraBelak/credit-app)
- [Repositorio Backend](https://github.com/KiraBelak/laudex-technical-test)
- [Demo en vivo backend](https://apilaudex-bkc5adfja8ewhuc9.mexicocentral-01.azurewebsites.net)

## 🚀 Descripción
Este proyecto es una aplicación Full Stack para la gestión de solicitudes de crédito, que permite a los usuarios:

✨ Funcionalidades principales:
- ✅ Crear una nueva solicitud de crédito
- ✅ Consultar el estado de sus solicitudes
- ✅ Actualizar una solicitud existente
- ✅ Eliminar una solicitud

El sistema está compuesto por un Backend en Spring Boot, una Base de Datos en PostgreSQL, y un Frontend en Angular.

## 🛠️ Tecnologías Utilizadas

### Backend:
- Spring Boot (Java 17)
- Spring Data JPA
- JWT para autenticación
- PostgreSQL
- Maven

### Frontend:
- Angular (v19)
- TypeScript
- Angular Material y Tailwind CSS
- HttpClient para consumir la API
- Formularios Reactivos

## 🔐 Credenciales de prueba
- Usuario: `kira`
- Contraseña: `password`

## 📦 Instalación y Configuración

1. Clona el repositorio:
   ```bash
   git clone https://github.com/KiraBelak/laudex-technical-test.git
   ```

2. Instala las dependencias:
   ```bash
   cd laudex-technical-test
   mvn clean install
   ```


3. Configura el entorno:
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```

4. Inicia la aplicación:
   ```bash
   mvn spring-boot:run
   ```

