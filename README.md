# 📢 ClasifiTrenque  
---

## 📖 1. Descripción General  
**ClasifiTrenque** es una plataforma web de publicidad local para la ciudad, donde se publican **prestadores de servicios** como plomeros, gasistas, albañiles, maestros particulares y otros oficios.   

---

## 🎯 2. Objetivos  
- Brindar un **espacio digital moderno** para que prestadores locales se publiciten.  
- Permitir a los habitantes de la ciudad encontrar fácilmente prestadores **confiables y calificados**.  
- Facilitar la interacción mediante **comentarios y calificaciones** de usuarios.  
- Ofrecer **estadísticas útiles** para la administración del sistema.  

---

## 🚀 Características principales  
- 🔐 **Autenticación y Autorización:** JWT con Spring Security.  
- 👤 **Gestión de Usuarios:** CRUD completo con validaciones.  
- 🏢 **Gestión de Proveedores:** Alta, edición, baja lógica, búsqueda avanzada y filtrado por categorías.  
- 📂 **Categorías:** Organización jerárquica de los servicios/productos publicados.  
- 📊 **Dashboard Administrativo:** Métricas de usuarios, proveedores, categorías y calificaciones.  
- 📝 **Comentarios y Calificaciones:** Los clientes pueden dejar feedback sobre proveedores.  
- 🔎 **Búsqueda Avanzada:** Filtros por nombre, categoría y calificación con paginación.  
- 📈 **Reportes y Métricas:** Estadísticas de uso, conteo por categorías, promedio de calificaciones.  
- 🌐 **Frontend Responsivo:** Interfaz desarrollada en React con componentes modulares.  

---

## 🛠️ Tecnologías utilizadas  

### **Backend**  
- ☕ **Java 21**  
- ⚡ **Spring Boot 3.5**  
- 🔒 **Spring Security + JWT**  
- 🗄️ **Spring Data JPA + Hibernate**  
- 🐬 **MySQL**  
- 📑 **Swagger (OpenAPI 3.0)** para documentación de APIs  
- 🧪 **JUnit 5 + Mockito** para pruebas unitarias  
- 📝 **SLF4J + Logback** para gestión de logs estructurados  

### **Frontend**  
- ⚛️ **React 18 + Vite**  
- 🎨 **Bootstrap 5** + CSS customizado  
- 📊 **Chart.js** para gráficos estadísticos  
- 🔗 **Axios** para comunicación con el backend  
- 🔍 **React Query** para manejo de cache y peticiones  

---

## 🏗️ Arquitectura  

### **Backend (Spring Boot)**  
Arquitectura **en capas**, inspirada en **DDD (Domain-Driven Design)** y siguiendo principios de **Clean Architecture**:  

- **Domain:** Entidades y reglas de negocio (`User`, `Role`, `ResetToken`).  
- **Application (Use Cases):** Interfaces que definen la lógica de negocio (`service/usecase`).  
- **Service (Implementations):** Implementaciones concretas de los casos de uso, mapeadores y servicios externos (ej. `EmailService`).  
- **Infrastructure:** Persistencia y acceso a datos (repositorios JPA).  
- **Presentation (Controller):** Controladores REST y DTOs que exponen la API al frontend.  
 

📂 Ejemplo de estructura:  
```
com.example.backend
 ├── users
 │   ├── controller
 │   ├── service
 │   │   ├── usecase
 │   │   └── implementation
 │   ├── domain
 │   └── infrastructure
 ├── providers
 ├── categories
 └── comments
```

### **Frontend (React)**  
Estructura modular y reutilizable:  

```
src/
 ├── components/        # Componentes reutilizables (cards, modals, tablas)
 ├── modules/           # Modulos del sistema(comments, home, dashboard)
 ├── services/          # Axios para APIs
 ├── hooks/             # Custom hooks para lógica desacoplada
 ├── context/           # Context API (auth, estado global)
 └── assets/            # Recursos estáticos
```

---

## 🧩 Principios, Patrones y Buenas Prácticas  

El desarrollo de **ClasifiTrenque** se fundamenta en estándares de ingeniería de software modernos, con el objetivo de garantizar **calidad, mantenibilidad y escalabilidad** del código.  

### 🔹 Principios de Diseño  
- **SOLID:** aplicado en toda la capa de negocio para asegurar responsabilidad única, extensibilidad y bajo acoplamiento.  
- **Separation of Concerns (SoC):** separación clara entre frontend y backend, y entre capas dentro de cada uno de ellos.  
- **Domain-Driven Design (DDD):** enfoque basado en el dominio con capas bien definidas: `Domain`, `Application`, `Infrastructure`, `Presentation`.  

### 🔹 Patrones de Diseño  
- **Repository Pattern:** implementado con JPA para desacoplar la lógica de persistencia.  
- **DTO Pattern:** garantiza separación entre entidades de dominio y objetos de transporte de datos.  
- **Use Case Pattern:** define interfaces en `service/usecase` para mantener la lógica de negocio independiente de la infraestructura.  
- **Factory Pattern:** utilizado en mapeadores para transformar entidades ↔ DTOs.  
- **Dependency Injection (IoC):** gestionado por el contenedor de Spring para desacoplar dependencias.  

### 🔹 Buenas Prácticas  
- **Control de versiones con Git** (flujo basado en GitFlow: `main`, `develop`, `feature/*`).  
- **Convenciones de nombres en inglés** para código y en español para mensajes visibles al usuario.  
- **Pruebas unitarias** con JUnit + Mockito.  
- **Documentación automática con Swagger/OpenAPI.**  
- **Logs estructurados** con SLF4J + Logback.  
- **Gestión de variables sensibles** con `.env`, evitando exponer credenciales en repositorios.
- **Manejo centralizado de errores:** uso de `@ControllerAdvice` y `@ExceptionHandler` para capturar excepciones y devolver respuestas claras y consistentes al frontend.  


---

## 🧪 Testing  

- **JUnit + Mockito:** pruebas unitarias y de integración.  
- **Cobertura estimada:** alrededor del 80%.  
- **Estrategia:** las pruebas se desarrollaron en paralelo a la implementación de las funcionalidades principales.  

---

## 📜 Licencia  

El código de **ClasifiTrenque** es **código abierto y de uso libre**, pudiendo ser **adaptado a cualquier ciudad**.  
Se autoriza su modificación y reutilización con fines educativos, comunitarios o comerciales,  
siempre respetando el reconocimiento a los autores originales.  

---
