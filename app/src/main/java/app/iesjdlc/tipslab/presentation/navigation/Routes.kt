package app.iesjdlc.tipslab.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    // Rutas de navegación
    @Serializable data object Splash : Route
    @Serializable data object AuthGraph : Route
    @Serializable data object MainGraph : Route

    // Grafo de autenticación
    @Serializable data object Login : Route
    @Serializable data object Signup : Route

    // Grafo principal (pestañas y pantallas una vez autenticado)
    @Serializable data object Home : Route
    @Serializable data object Explore : Route
    @Serializable data object CreateLifehack : Route
    @Serializable data object Saved : Route
    @Serializable data object Profile : Route

    // Pantallas de destino
    @Serializable data object Search : Route
    @Serializable data class LifehackDetail(val lifehackId: String) : Route
    @Serializable data class LifehacksByCategory(val categoryId: Int) : Route
    @Serializable data class EditLifehack(val lifehackId: String) : Route
    @Serializable data object EditProfile : Route
    @Serializable data class Camera(val allowVideo: Boolean = true) : Route
}