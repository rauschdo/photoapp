package de.rauschdo.photoapp.data

// possible addition, specific configuration dialogs for filter (e.g COLOR_FILTER, FLIP, ...)
enum class FilterType {
    INVERT,
    COLOR_FILTER,
    CONTRAST,
    GREYSCALE,
    SEPIA,
    GAMMA,
    POSTERIZE,
    BRIGHTNESS,
    BOOST_COLOR,
    FLIP,
    GRAIN,
    DIRTY,
    SNOW,
    REFLECTION;

    companion object {
        fun clickToApplyFilters() = listOf(
            INVERT,
            GREYSCALE,
            SEPIA,
            POSTERIZE,
            GRAIN,
            DIRTY,
            SNOW,
            REFLECTION
        )
    }
}