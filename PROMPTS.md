# Registro de Prompts y Flujo con IA — Rama `mejora-ia`

Este documento registra las iteraciones asistidas por IA realizadas sobre la base funcional (`main`) en la rama `mejora-ia` para elevar el diseño UI/UX, corregir lógica de negocio (favoritos por perfil, filtros combinados) y añadir pruebas unitarias sin alterar los contratos de arquitectura ni las interfaces (`ItemRepository`, `AuthRepository`, `Notificador`).

---

## 1. Principios y restricciones mantenidas en todos los prompts

- **Arquitectura MVVM pura:** las pantallas en `ui/` solo observan `StateFlow` mediante `collectAsStateWithLifecycle()` y delegan eventos al `ViewModel` correspondiente.
- **Sin librerías prohibidas:** sin Hilt/Dagger, sin librerías de red alternativas a Retrofit/OkHttp, con inyección manual en `AppContainer`.
- **Seguridad y privacidad:** `app/google-services.json` excluido de Git en `.gitignore`; sin credenciales ni llaves expuestas en código fuente.
- **Validación incremental:** compilación verificada en cada etapa con `.\gradlew.bat compileDebugKotlin` y suite de pruebas unitarias con `.\gradlew.bat testDebugUnitTest`.

---

## 2. Bitácora cronológica de prompts y decisiones

### Iteración 1 — Splash animado, Login con collage y Registro de usuario estándar
- **Prompt resumido:**
  > Implementar Splash animado con ondas negras y el logo PNG `FR` de Fragancias Rocha, pantalla de Login por correo y contraseña (sin botón de Google) usando el collage de marcas como fondo superior, y pantalla de Registro (`RegistroScreen`) que cree cuentas exclusivamente con rol `"usuario"`, muestre una alerta de creación exitosa y redirija al Login precargando el correo.
- **Resultado implementado:**
  - `SplashScreen` con animación de escala, opacidad y ondas en `Canvas`.
  - `LoginScreen` + `RegistroScreen` + `RegistroViewModel` con validaciones de nombre, correo y contraseña, toggle de visibilidad con icono de ojo y botón de retorno con flecha.
  - `FirebaseAuthRepository` guarda el documento en `usuarios/{uid}` con `rol = "usuario"` y `nombre`, cerrando sesión tras el registro para forzar el inicio controlado desde Login.
- **Ajustes realizados:** se evitó texto envuelto en múltiples líneas dentro de botones usando `maxLines = 1` y `softWrap = false`, y se unificó la paleta en negro obsidiana (`#09090B`), tarjetas oscuras (`#1A1A1E`), marfil claro (`#FCFAF7`) y dorado champán (`#D4B475`), evitando tonos morados o grises planos.

### Iteración 2 — Imágenes de fragancias, tarjetas de catálogo y pantalla de Detalle
- **Prompt resumido:**
  > Integrar carga de imágenes reales desde la API (`thumbnail`/`images`) en el catálogo y detalle usando Coil, rediseñar `FraganciaCard` mostrando imagen arriba con corazón en la esquina superior derecha, nombre del perfume arriba de la marca y precio debajo; mejorar `DetalleScreen` con header limpio, rating visual en estrellas, precio con descuento, descripción y ficha técnica con efectos de entrada.
- **Resultado implementado:**
  - Se añadió `coil-compose` (`2.7.0`) en `gradle/libs.versions.toml` y `app/build.gradle.kts`.
  - Rediseño de `FraganciaCard` en grilla de 2 columnas con animación elástica en el botón de favorito.
  - Rediseño de `DetalleScreen` con `AnimatedContent`, `FilaEstrellas` (estrellas completas, media estrella y vacías), porcentaje de descuento y tarjeta única de especificaciones sin botones duplicados de favorito.

### Iteración 3 — Buscador compacto, filtros múltiples de marca y filtros de precio
- **Prompt resumido:**
  > Hacer el buscador más delgado estilo píldora oscura (`"Buscar perfumes..."`), separar filtros en dos botones redondeados (`"Todos"` para marcas y `"Precio"` para rangos y ordenamiento), permitir selección múltiple de marcas de modo que al seleccionar todas las marcas disponibles regrese automáticamente a `"Todos"`, y mantener la estética dorada champán y bordes redondeados en las opciones activas.
- **Resultado implementado:**
  - En `ListaViewModel`: `marcasSeleccionadas: StateFlow<Set<String>>`, `RangoPrecio` (`TODOS`, `MENOR_70`, `ENTRE_70_100`, `MAYOR_100`), `OrdenPrecio` (`NINGUNO`, `MENOR_A_MAYOR`, `MAYOR_A_MENOR`), bandera reactiva `hayFiltrosActivos` y lógica en `toggleMarca()` para normalizar a `emptySet()` al marcar todas las marcas disponibles.
  - En `ListaScreen`: `BuscadorCompacto` de `44.dp` con `BasicTextField`, `BotonFiltroPill`, `ModalBottomSheet` con `rememberModalBottomSheetState(skipPartiallyExpanded = true)`, fila fija de título con píldora `"Restablecer"` animada para evitar saltos de layout, y estado vacío centrado cuando no hay resultados.

### Iteración 4 — Barra de navegación inferior, Perfil con portada y Panel Admin
- **Prompt resumido:**
  > Crear barra inferior animada inspirada en apps modernas con píldora deslizante en dorado champán, etiquetas visibles (`Catálogo`, `Favoritos`, `Admin`, `Perfil`), dejando `Perfil` siempre en el extremo derecho para administradores, e icono personalizado de frasco de perfume para `Catálogo`. Quitar duplicación de nombre, rol y doble botón de cierre de sesión en `PerfilScreen`; usar portada con collage y logo de usuario en círculo; rediseñar `AdminScreen` como consola clara con métricas y acciones explicadas.
- **Resultado implementado:**
  - `BarraInferior`: vectores `IconoPerfumeActivo` / `IconoPerfumeInactivo` dibujados con `ImageVector.Builder`, píldora animada con resorte y márgenes ampliados (`64.dp` internos + padding vertical `14.dp`/`12.dp`).
  - `PerfilScreen`: portada de `190.dp` con `FondoCollageMarcas`, avatar circular (`Person`) en marco dorado, nombre único bajo el avatar, tarjeta oscura (`Datos de la cuenta` y valores en blanco puro `BlancoPuro`) con correo, contraseña revelable y un único botón `"Cerrar sesión"`.
  - `AdminScreen`: tarjeta de identidad `ADMIN`, dos tarjetas KPI (`En caché local` y `Tus favoritos`), banner animado de estado y tarjeta `"Operaciones de sincronización"` para `Sincronizar catálogo` y `Limpiar caché local`.

### Iteración 5 — Aislamiento de favoritos por usuario autenticado en Room
- **Prompt resumido:**
  > Corregir que los favoritos marcados por un usuario estándar aparezcan también al iniciar sesión como administrador; cada cuenta debe conservar sus propios favoritos de forma independiente.
- **Resultado implementado:**
  - Se creó la entidad `FavoritoEntity` (tabla `favoritos_usuario` con clave compuesta `(usuarioId, itemId)`) y se actualizó `AppDatabase` a `version = 2` con `MIGRACION_1_2`.
  - `AppContainer` expone `uidFlow` mediante `FirebaseAuth.AuthStateListener` e `ItemRepositoryImpl` combina `dao.todos()` con `dao.observarIdsFavoritos(uid)` usando `flatMapLatest`, manteniendo intacta la interfaz pública `ItemRepository`.

### Iteración 6 — Pruebas unitarias de `ListaViewModel` (`R12`)
- **Prompt resumido:**
  > Agregar `kotlinx-coroutines-test` y crear pruebas unitarias sobre `ListaViewModel` usando un `ItemRepository` falso y un `Notificador` falso para validar carga inicial, errores de red con y sin caché, búsqueda + auto-reinicio de marcas, filtros de precio/orden y alternado de favoritos.
- **Resultado implementado:**
  - `ListaViewModelTest` con `MainDispatcherRule` (`UnconfinedTestDispatcher`) y 6 casos de prueba verificados con Gradle (`BUILD SUCCESSFUL`).
