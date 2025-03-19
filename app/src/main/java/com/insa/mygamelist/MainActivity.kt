package com.insa.mygamelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.ui.screen.GameScreen
import com.insa.mygamelist.ui.screen.HomeScreen
import com.insa.mygamelist.ui.theme.MyGamesListTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {

    @Serializable
    object HomeRoute

    @Serializable
    data class GameRoute(val id: Long)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        IGDB.load(this)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()                         // Permet de naviguer entre les deux pages de l'appli
            val favoriteGames = remember { mutableStateOf(setOf<Long>()) }      // Permet de garder les mêmes favoris entre les deux pages de l'appli
            var searchText by rememberSaveable { mutableStateOf("") }               // Contiendra le texte de recherche rentré par l'utilisateur
            var isFavoriteSelected by rememberSaveable { mutableStateOf(false) }       // Permet de savoir si on veut afficher la liste des favoris ou non
            var isSearchVisible by rememberSaveable { mutableStateOf(false) }

            // Filtre de recherche par nom, genre et plateforme compatible
            val filteredGames = IGDB.games.filter { game ->
                game.name.contains(searchText, ignoreCase = true) ||
                        IGDB.genres
                            .filter {game.genres.contains(it.id)}
                            .find {it.name.contains(searchText, ignoreCase = true)} != null ||
                        IGDB.platforms
                            .filter { game.platforms.contains(it.id)}
                            .find {it.name.contains(searchText, ignoreCase = true)} != null
            }

            // Sélection des jeux à afficher selon les filtres recherche et favoris
            val displayList = when {
                isSearchVisible && searchText.isNotEmpty() && isFavoriteSelected ->
                    filteredGames.filter { it.id in favoriteGames.value } // Filtre favoris + Recherche
                isSearchVisible && searchText.isNotEmpty() ->
                    filteredGames // Pas de filtre favoris + Recherche
                isFavoriteSelected ->
                    IGDB.games.filter { it.id in favoriteGames.value } // Filtre favoris + Pas Recherche
                else ->
                    IGDB.games // Pas de filtre favoris + Pas Recherche
            }

            MyGamesListTheme {
                NavHost(navController, startDestination = HomeRoute) {
                    composable<HomeRoute> {
                        HomeScreen(
                            navController = navController,
                            favoriteGames = favoriteGames,
                            isFavoriteSelected = isFavoriteSelected,
                            toggleIsFavoriteSelected = { isFavoriteSelected = !isFavoriteSelected },    // Permet de changer la valeur de isFavoriteSelected après action de l'utilisateur
                            searchText = searchText,
                            setSearchText = { newSearchText -> searchText = newSearchText },        // Permet de changer la valeur de searchText après action de l'utilisateur
                            isSearchVisible = isSearchVisible,
                            toggleIsSearchVisible = { isSearchVisible = !isSearchVisible },
                            displayList = displayList
                        )
                    }
                    composable<GameRoute> { backStackEntry ->
                        val route = backStackEntry.toRoute<GameRoute>()
                        GameScreen(route.id, navController, favoriteGames, displayList)
                    }
                }
            }
        }

    }



}