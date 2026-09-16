· GestionStockCalsomatu 
Sistema de Gestión de Stock e Inventario Almacén Industrial / Naval
GestionStockCalsomatu es una solución de software diseñada para el control, trazabilidad y gestión de existencias en almacenes del sector industrial y naval. 
El sistema permite administrar el flujo de consumibles, EPIs, herramientas de soldadura, tubos y repuestos técnicos críticos necesarios para la operativa diaria en taller y obra naval.

· Arquitectura y Diseño
El proyecto está desarrollado en Java aplicando una arquitectura modular enfocada en la consistencia de datos y la gestión eficiente de inventario:
Estructura por Capas: Separación de responsabilidades entre gestión de entidades de dominio, lógica de control de stock e interfaz.
Modelado de Inventario: Control de referencias, categorías de material industrial, ubicaciones de almacén y umbrales mínimos de stock.
Garantía de Trazabilidad: Registro de entradas, salidas y asignación de materiales a trabajos u operarios técnicos.

· Stack Tecnológico
Lenguaje: Java 17+
Persistencia / Base de Datos: Relacional (MySQL)
Gestión de Proyecto: Maven
Entorno de Desarrollo: IntelliJ IDEA

· Funcionalidades Principales
Gestión de Artículos y Catálogo: Alta, modificación y categorización de herramientas, consumibles y equipamiento.
Control de Stock en Tiempo Real: Monitorización de unidades disponibles, entradas de proveedor y salidas a taller.
Alertas de Reaprovisionamiento: Detección de artículos por debajo del stock mínimo de seguridad para evitar paradas en producción.
Histórico de Movimientos: Trazabilidad de qué material se retira, cuándo y para qué trabajo u orden se asigna.

· Estado del Proyecto (En Desarrollo Activo 🛠️)
El proyecto se encuentra en fase de desarrollo e integración. Las siguientes áreas están en proceso de implementación y ajuste:
Módulo de Movimientos (/stock/movimientos): Lógica de auditoría de entradas/salidas y ajustes manuales de inventario.
Módulo de Catálogo (/articulos): Definición completa de atributos técnicos y proveedores asociados.
Consultas y Alertas (/reportes): Generación de listados de necesidad de compra y rotación de stock.

· Ejecución Local
Requisitos Previos:
JDK 17 o superior
MySQL 8.0+
Pasos de Instalación:
Clonar el repositorio:
git clone https://github.com/Lestajorge/GestionStockCalsomatu.git
cd GestionStockCalsomatu
Configurar las credenciales de la base de datos en el archivo de configuración correspondiente.
Compilar y ejecutar la aplicación desde tu IDE o mediante línea de comandos.

· Contexto del Proyecto
Proyecto enfocado en la resolución de problemas reales de control de inventario en entornos de fabricación e industria auxiliar naval, 
sirviendo como demostración de diseño de software y gestión de persistencia en Java.

· Autor
Jorge Lesta - Desarrollador Backend & Estudiante DAM
GitHub: @Lestajorge
