package com.insa.mygamelist.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.insa.mygamelist.data.Game
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.ui.component.GameDetails

// Ecran d'affichage des détails d'un jeu
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    id: Long,
    navController: NavHostController,
    favoriteGames: MutableState<Set<Long>>,
    filteredGames: List<Game>
) {
    val game = IGDB.games.find { id == it.id }

    val initialPage = filteredGames.indexOf(game)
    val pagerState = rememberPagerState(initialPage, pageCount = { filteredGames.size })

    Scaffold(topBar = {
        TopAppBar(title = { Text(filteredGames[pagerState.currentPage].name) },        // On met le titre du jeu actuel
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {      // Flèche de retour arrière
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            },
            actions = {
                val isFavorite = favoriteGames.value.contains(filteredGames[pagerState.currentPage].id)
                IconButton(onClick = {
                    favoriteGames.value =
                        if (isFavorite) {     // Si le jeu est en favori et qu'on clique sur l'icône
                            favoriteGames.value - filteredGames[pagerState.currentPage].id                // Alors le jeu n'est plus en favori
                        } else {
                            favoriteGames.value + filteredGames[pagerState.currentPage].id                // Sinon il est ajouté aux favoris
                        }
                }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,         // Si le jeu est en favori, l'icône favori est plein, sinon il est vide
                        contentDescription = "Favori"
                    )
                }
            }
        )
    }, modifier = Modifier.fillMaxSize()) { innerPadding ->
        HorizontalPager(state = pagerState, modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                page -> GameDetails(filteredGames[page].id)     //permet de changer le GameDetails lorsqu'on swipe
        }
    }
}