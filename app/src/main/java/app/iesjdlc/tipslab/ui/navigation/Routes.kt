package app.iesjdlc.tipslab.ui.navigation

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
    @Serializable data object HomeTab : Route
    @Serializable data object ExploreTab : Route
    @Serializable data object CreateLifehack : Route
    @Serializable data object SavedTab : Route
    @Serializable data object ProfileTab : Route

    // Pantallas de destino
    @Serializable data class LifehackDetail(val lifehackId: String) : Route
    @Serializable data class LifehacksByCategory(val categoryId: String) : Route
    @Serializable data class EditLifehack(val lifehackId: String) : Route
    @Serializable data object EditProfile : Route
}