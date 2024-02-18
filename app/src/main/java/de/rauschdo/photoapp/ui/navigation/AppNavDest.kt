package de.rauschdo.photoapp.ui.navigation

enum class AppNavDest {

    Home,
    Editor,
    Camera
    ;

    fun destinationTarget(): String = when (this) {
        Home -> "home"
        Editor -> "editor"
        Camera -> "camera"
    }
}