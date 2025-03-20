package com.insa.mygamelist.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.insa.mygamelist.MainActivity.GameRoute
import com.insa.mygamelist.data.Game
import com.insa.mygamelist.data.IGDB

// Permet de créer la case d'un jeu sur le HomeScreen
@Composable
fun GameCard(game: Game, navController: NavController, favoriteGames: MutableState<Set<Long>>) {
    val isFavorite = favoriteGames.value.contains(game.id)      // On vérifie si le jeu est enregistré en tant que favori
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(10.dp)
            .height(100.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { navController.navigate(GameRoute(game.id)) }      // Permet de passer sur le GameScreen du jeu
            .background(MaterialTheme.colorScheme.surfaceVariant) // Applique la couleur
    ) {
        // Affichage de l'image à gauche
        AsyncImage(
            model = "https:" + IGDB.covers.find { game.cover == it.id }?.url,      // On récupère l'image
            contentDescription = "Cover of the game" + game.name,
            modifier = Modifier
                .size(90.dp)
                .padding(10.dp)
        )
        Column(modifier = Modifier
            .width(230.dp)
            .align(Alignment.CenterVertically)) {
            // Affichage du nome du jeu au milieu
            Text(
                game.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.W600,
                style = TextStyle(textDecoration = TextDecoration.Underline)
            )
            // Affichage des genres du jeu en-dessous de son nom
            val mygenres = "Genres : " + IGDB.genres.filter { it.id in game.genres }
                .joinToString(", ") { it.name }  // on récupère tous les genres associé au jeu et on les sépare avec une ,
            Text(
                mygenres,                               // affiche les genres
                maxLines = 1,                           // permet de mettre les genres sur une seule ligne
                overflow = TextOverflow.Ellipsis        // permet de mettre les ... quand la liste de genres est trop longue
            )
        }
        Column {
            // Affichage du bouton "favori" à droite
            IconButton(onClick = {
                favoriteGames.value = if (isFavorite) {
                    favoriteGames.value - game.id
                } else {
                    favoriteGames.value + game.id
                }
            }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,  // en fonction de la valeur de isFavorite, l'icône sera un coeur rempli ou non
                    contentDescription = "Favorite"
                )
            }
        }
    }
}