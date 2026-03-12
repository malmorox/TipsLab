# TipsLab

Somos una aplicación Android desarrollada como TFG que permite a los usuarios publicar, organizar y descubrir `life hacks` (consejos prácticos del día a día) en **formato vídeo** con **descripción textual**.

Los contenidos se **clasifican por categorías** (comida, hogar, tecnología…) y la app incluye **búsqueda**, **filtrado**, **guardado de favoritos** y **acceso rápido a los consejos más recientes y valorados por la comunidad**.

## Tecnologías utilizadas
![Kotlin](https://img.shields.io/badge/Kotlin-B125EA?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=Jetpack%20Compose&logoColor=white)
![Firebase](https://img.shields.io/badge/firebase-ffca28?style=for-the-badge&logo=firebase&logoColor=black)
![Supabase](https://img.shields.io/badge/Supabase-3FCF8E.svg?style=for-the-badge&logo=Supabase&logoColor=white)

## Implementación técnica y uso de librerías

| **Librería** | **Propósito / uso en la aplicación** |
| --- | --- |
| **[Coil](https://coil-kt.github.io/coil/)** | Carga de imágenes de forma asíncrona y gestión automática de caché para mejorar el rendimiento de la interfaz. |
| **[Compressor](https://github.com/zetbaitsu/Compressor)** | Compresión de imágenes antes de subirlas a Supabase Storage para reducir tamaño de archivo y consumo de ancho de banda. |
| **[YouTube Player](https://github.com/PierfrancescoSoffritti/android-youtube-player)** | Integración de reproductor para mostrar vídeos de YouTube embebidos dentro de la aplicación |
| **[Media3 ExoPlayer](https://developer.android.com/media/media3?hl=es-419)** | Reproducción de vídeos propios almacenados en Supabase Storage con control completo sobre el streaming y la reproducción. |

## Arquitectura MVVM

El proyecto sigue el patrón `Model-View-ViewModel`, organizando el código en capas con responsabilidades bien definidas.
```
┌─────────────────────────────────┐
│              VIEW               │
│     Pantallas y composables     │
└────────────────┬────────────────┘
                 │ observa estado
                 ▼
┌─────────────────────────────────┐
│           VIEWMODEL             │
│     Estado de UI · lógica de    │
│          presentación           │
└──────┬─────────────────┬────────┘
       │                 │
       │ (simple)        │ (complejo)
       ▼                 ▼
┌────────────┐  ┌─────────────────┐
│            │  │    USE CASES    │
│ REPOSITORY │◄─┤ Orquesta varios │
│            │  │  repositories   │
└──────┬─────┘  └────────┬────────┘
       │                 │
       └────────┬────────┘
                │ accede a
                ▼
┌─────────────────────────────────┐
│             MODEL               │
│   Entidades de dominio · DTOs   │
│            Mappers              │
└─────────────────────────────────┘
```

### VIEW

Contiene las pantallas y composables de Jetpack Compose. Su única responsabilidad es renderizar el estado que expone el ViewModel y capturar las acciones del usuario. No contiene lógica de negocio.

### VIEWMODEL

Gestiona el estado de la UI y actúa como intermediario entre la vista y los datos. Según la complejidad de la operación, puede comunicarse de dos formas:
Directamente con un Repository cuando la operación es simple y solo involucra una fuente de datos.
A través de un UseCase cuando la operación requiere coordinar múltiples repositorios o una lógica más elaborada.

### USE CASES (uso condicional)

Se emplean únicamente cuando la lógica lo justifica, principalmente cuando es necesario combinar datos de varios repositories o transformar la información antes de entregarla al ViewModel. Por ejemplo, construir un Lifehack completo puede requerir tirar de datos de usuarios, categorías y medios a la vez (esa orquestación se aloja aquí). También es donde se aplican los Mappers en estos casos complejos.

### REPOSITORY

Abstrae el origen de los datos. Cada repository se comunica con Supabase o Firebase y expone los datos al resto de la app. En operaciones simples, también puede encargarse de transformar los DTOs a entidades de dominio mediante los Mappers.

### MODEL

Es la capa más interna y la única que no depende de ninguna otra. Se divide en tres elementos:

**Domain**: entidades puras de negocio (Lifehack, User, Category) que usa el resto de la app.

**DTOs**: objetos que representan la estructura exacta que Firestore.

**Mappers**: funciones de conversión entre DTOs y entidades de dominio. Se aplican en el Repository en casos simples, y en el UseCase cuando la construcción del objeto requiere combinar varias fuentes.

## Equipo de desarrollo
<table>
  <tr>
    <td align="center">
      <a href="https://github.com/aiitttor">
        <img src="https://avatars.githubusercontent.com/u/235867954?v=4" width="100px"/><br/>
        <b>Aitor Fuertes</b>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/malmorox">
        <img src="https://avatars.githubusercontent.com/u/73890028?v=4" width="100px"/><br/>
        <b>Marcos Almorox</b>
      </a>
    </td>
  </tr>
</table>
