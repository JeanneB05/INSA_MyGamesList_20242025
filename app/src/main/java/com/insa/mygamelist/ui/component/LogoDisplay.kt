package com.insa.mygamelist.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.data.Platform

// Méthode qui permet d'afficher les images des logos
@Composable
fun LogoDisplay(platform: Platform) {
    AsyncImage(
        model = "https:" + (IGDB.platform_logos.find { platform.platform_logo == it.id }?.url?.replace(
            "jpg",
            "png"
        ) ?: "//commons.wikimedia.org/wiki/File:No_Image_Available.jpg"),       // On récupère l'image et s'il n'y en a pas, on met une image not found
        contentDescription = "Logos of the platform" + platform.name,
        modifier = Modifier
            .size(90.dp)
            .padding(10.dp)
    )
}