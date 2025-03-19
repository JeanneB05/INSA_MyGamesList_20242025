package com.insa.mygamelist.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.insa.mygamelist.data.IGDB

// Contient tous les détails d'un jeu, qui seront affichés dans le GameScreen
@Composable
fun GameDetails(id: Long) {
    // On récupère toutes les données associées au jeu et on les met au bon type
    val game = IGDB.games.find { id == it.id }
    val name = game?.name ?: "Unfound"          //Obligé de mettre un Elvis car Text ne prend pas de type String? (type renvoyé par .find)
    val genres = IGDB.genres.filter { it.id in (game?.genres ?: listOf(String)) }.joinToString(", ") { it.name }  // on récupère tous les genres associé au jeu et on les sépare avec une ,
    val summary = game?.summary ?: "Unfound"
    val platforms = IGDB.platforms.filter { it.id in game?.platforms!! }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
    ) {
        // Affichage du nom du jeu en haut de la page
        Text(
            text = name,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            fontSize = 17.sp,
            fontWeight = FontWeight.W600,
            style = TextStyle(textDecoration = TextDecoration.Underline)
        )

        // Affichage de l'image du jeu en dessous
        AsyncImage(
            model = "https:" + IGDB.covers.find { game?.cover == it.id }?.url,
            contentDescription = "Cover of the game $name",
            modifier = Modifier
                .size(250.dp)
                .padding(10.dp)
        )

        // Affichage des genres du jeu en dessous
        Text(
            text = genres,
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
            fontStyle = FontStyle.Italic,
            maxLines = 1,                           // Permet de mettre les genres sur une seule ligne
            overflow = TextOverflow.Ellipsis        // Permet de mettre les ... quand la liste de genres est trop longue
        )

        // Affichage des logos des plateformes sur lesquelles le jeu est diponible en dessous
        LazyRow {
            items(platforms.size) {         // On va itérer sur chaque plateforme associée au jeu
                    index ->
                val platform = platforms[index]
                LogoDisplay(platform)       // Affichage du logo du jeu
            }
        }

        // Affichage du résumé du jeu en dessous
        Text(
            text = summary,
            modifier = Modifier.padding(10.dp)
        )
    }
}