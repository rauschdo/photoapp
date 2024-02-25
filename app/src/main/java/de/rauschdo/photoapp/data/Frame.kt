package de.rauschdo.photoapp.data

import androidx.annotation.DrawableRes
import de.rauschdo.photoapp.R

enum class Frame(@DrawableRes val resourceId: Int?) {
    NONE(null),
    DEFAULT(R.drawable.frame0),
    SAND(R.drawable.frame1),
    REDDISH(R.drawable.frame2),
    FISH(R.drawable.frame3),
    FLOWER(R.drawable.frame4),
    STRIPE_DIAGONAL(R.drawable.frame5),
    SHAPES_VERTICAL(R.drawable.frame6)
}