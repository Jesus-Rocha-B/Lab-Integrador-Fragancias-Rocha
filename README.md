# Fragancias Rocha

Aplicación Android nativa desarrollada en **Kotlin** con **Jetpack Compose**, arquitectura **MVVM**, persistencia local **Offline-First** con **Room** y autenticación con **Firebase** para explorar un catálogo de perfumería consumiendo la API pública de **DummyJSON**.

---

## Características principales

- **Autenticación y roles (Firebase Auth + Cloud Firestore):**
  - Splash animado (`SplashScreen`) con logo institucional `FR`.
  - Inicio de sesión por correo y contraseña sobre collage oscuro de marcas (`LoginScreen`).
  - Registro de cuentas nuevas (`RegistroScreen`) asignando automáticamente rol `"usuario"`, diálogo de confirmación y retorno al Login con correo precargado.
  - Navegación y permisos diferenciados según rol (`Rol.USUARIO` vs `Rol.ADMIN` desde `usuarios/{uid}` en Firestore).
- **Catálogo e imágenes reales (`ListaScreen` / `ListaViewModel`):**
  - Consumo REST desde `https://dummyjson.com/products/category/fragrances` y detalle individual `products/{id}`.
  - Caché local en **Room** como fuente única de verdad: funciona sin conexión conservando aviso de estado offline cuando falla la red con datos previos.
  - Buscador compacto en tiempo real (`"Buscar perfumes..."`), selección múltiple de marcas con restablecimiento automático a `"Todos"` al marcar todas las disponibles, y filtros combinados de rango y ordenamiento por precio.
  - Carga de imágenes con **Coil** (`coil-compose`) en tarjetas de 2 columnas y botón animado de favoritos.
- **Favoritos aislados por perfil (`FavoritosScreen` / Room v2):**
  - Tabla relacional `favoritos_usuario (usuarioId, itemId)` vinculada reactivamente al UID activo de Firebase Auth mediante `AuthStateListener`, evitando que distintas cuentas compartan favoritos en el mismo dispositivo.
- **Detalle de fragancia (`DetalleScreen` / `DetalleViewModel`):**
  - Galería principal, calificación por estrellas, precio y descuento, descripción y ficha técnica (`Stock`, `Disponibilidad`, `Categoría`, `Envío`, `Garantía`, `Devoluciones`).
- **Perfil y Panel Admin (`PerfilScreen` / `AdminScreen`):**
  - **Perfil:** portada con collage de marcas, datos de cuenta en tarjeta oscura (correo y contraseña con interruptor visible/oculto) y cierre de sesión único.
  - **Panel Admin (`Rol.ADMIN`):** consola con métricas locales en vivo (`En caché local` y `Tus favoritos`), sincronización manual con notificación local (`NotificadorAndroid`) y limpieza de caché Room.
- **Navegación inferior (`BarraInferior`):**
  - Píldora deslizante animada en dorado champán, icono de frasco de perfume para `Catálogo` y orden **`Catálogo | Favoritos | Admin | Perfil`** (con `Perfil` en el extremo derecho para administradores).

---

## Arquitectura del proyecto

Patrón **MVVM** con flujo unidireccional de datos (`StateFlow` + `UiState`) e inyección manual mediante [`AppContainer`](app/src/main/java/com/tecsup/fragancias/AppContainer.kt):

```text
app/src/main/java/com/tecsup/fragancias/
├── AppContainer.kt
├── MainActivity.kt
├── data/
│   ├── auth/          # AuthRepository y FirebaseAuthRepository
│   ├── local/         # AppDatabase (v2 + MIGRACION_1_2), ItemDao, ItemEntity, FavoritoEntity
│   ├── mapper/        # Transformaciones DTO -> Entity -> Domain Model
│   ├── remote/        # RetrofitClient, ApiService y DTOs kotlinx.serialization
│   └── repository/    # Contrato ItemRepository e ItemRepositoryImpl (Offline-First)
├── domain/
│   └── model/         # Item, Sesion, Rol
├── notification/      # Contrato Notificador y NotificadorAndroid (NotificationCompat)
└── ui/
    ├── UiState.kt
    ├── admin/         # AdminScreen, AdminViewModel
    ├── components/    # BarraInferior, Cargando, ErrorView, FondoCollageMarcas, FraganciaCard
    ├── detalle/       # DetalleScreen, DetalleViewModel
    ├── favoritos/     # FavoritosScreen, FavoritosViewModel
    ├── lista/         # ListaScreen, ListaViewModel
    ├── login/         # SplashScreen, LoginScreen, LoginViewModel, RegistroScreen, RegistroViewModel, PerfilScreen
    ├── navigation/    # AppNav
    └── theme/         # Color, Theme, Type
```

---

## Tecnologías y versiones

| Área | Dependencia / Herramienta | Versión |
| --- | --- | --- |
| Build & Lenguaje | Android Gradle Plugin / Kotlin | `9.4.1` / `2.2.10` |
| SDK Android | `minSdk` / `targetSdk & compileSdk` / Java | `24` / `37` / `11` |
| UI | Jetpack Compose BOM / Material 3 | `2026.02.01` |
| Navegación y Estado | Navigation Compose / Lifecycle Compose | `2.8.4` / `2.11.0` |
| Red | Retrofit / OkHttp / Kotlinx Serialization | `2.11.0` / `4.12.0` / `1.7.3` |
| Base de datos local | Room (`runtime`, `ktx`, `ksp`) | `2.8.5` (`KSP 2.3.13`) |
| Backend Auth & Roles | Firebase BOM (`auth`, `firestore`) | `33.5.1` |
| Imágenes | Coil Compose | `2.7.0` |
| Testing | JUnit / `kotlinx-coroutines-test` | `4.13.2` / `1.10.2` |

---

## Configuración y ejecución

### Requisitos previos
1. Android Studio reciente con JDK 11+ y SDK 37 instalado.
2. Un proyecto en **Firebase Console** con:
   - **Authentication** habilitado con el proveedor *Correo electrónico / Contraseña*.
   - **Cloud Firestore** con la colección `usuarios` donde cada documento tiene el `uid` del usuario y los campos:
     - `rol`: `"usuario"` o `"admin"`
     - `nombre`: nombre visible del perfil
3. Descargar tu archivo `google-services.json` y ubicarlo en `app/google-services.json` *(ignorado por `.gitignore`)*.

### Comandos Gradle

```powershell
# Compilar código Kotlin en modo debug
.\gradlew.bat compileDebugKotlin --console=plain

# Generar APK debug
.\gradlew.bat assembleDebug

# Ejecutar pruebas unitarias (JUnit + fakes de repositorio y notificador)
.\gradlew.bat testDebugUnitTest --console=plain
```

### Pruebas unitarias incluidas (`ListaViewModelTest`)
Ubicadas en `app/src/test/java/com/tecsup/fragancias/ui/lista/ListaViewModelTest.kt` (7 pruebas ejecutadas en total con `ExampleUnitTest`, 0 fallidas, 0 omitidas):
1. `inicioExitoso_publicaItemsMarcasYNotifica`: valida carga inicial, listado de marcas y notificación local emitida.
2. `errorDeRedSinCache_muestraErrorYNoNotifica`: valida estado `UiState.Error` cuando no hay datos en caché.
3. `errorDeRedConCache_conservaListaYMuestraAvisoOffline`: valida modo sin conexión conservando caché y mostrando aviso offline.
4. `busquedaYSeleccionDeTodasLasMarcas_filtranYRestablecenATodos`: valida búsqueda combinada y retorno automático a `"Todos"` al seleccionar todas las marcas.
5. `filtrosPrecioYOrdenamiento_filtranYOrdenanCorrectamente`: valida rangos de precio, ordenamiento ascendente/descendente y restablecimiento.
6. `alternarFavorito_actualizaEstadoDelProductoEnLista`: valida actualización reactiva de favoritos.

---

## Capturas de pantalla

| Splash y Login | Catálogo y Filtros | Detalle y Perfil | Panel Admin |
| --- | --- | --- | --- |
| *(Agregar capturas en `docs/`)* | *(Agregar capturas en `docs/`)* | *(Agregar capturas en `docs/`)* | *(Agregar capturas en `docs/`)* |

---

## Autor y trazabilidad

- **Repositorio:** `https://github.com/Jesus-Rocha-B/Lab-Integrador-Fragancias-Rocha.git`
- **Bitácora de prompts con IA:** ver [`PROMPTS.md`](PROMPTS.md)
