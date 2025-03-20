package com.insa.mygamelist.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.insa.mygamelist.data.Game
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.ui.component.GameCard

// Ecran d'affichage d'accueil, qui contient la liste de tous les jeux
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    favoriteGames: MutableState<Set<Long>>,
    isFavoriteSelected: Boolean,
    toggleIsFavoriteSelected: () -> Unit,
    searchText: String,
    setSearchText: (String) -> Unit,
    isSearchVisible: Boolean,
    toggleIsSearchVisible: () -> Unit,
    displayList: List<Game>
) {

    Scaffold(topBar = {
        TopAppBar(title = { Text("My Games List") },
            actions = {
                IconButton(onClick = toggleIsSearchVisible) {
                    Icon(
                        imageVector = if (isSearchVisible) Icons.Default.Close else Icons.Default.Search,  // en fonction de la valeur de isSearchVisible, l'icône sera une loupe ou une croix
                        contentDescription = "Show or hide the research"
                    )
                }
                IconButton(onClick = { toggleIsFavoriteSelected() }) {
                    Icon(
                        imageVector = if (isFavoriteSelected) Icons.Default.Favorite else Icons.Default.FavoriteBorder,  // en fonction de la valeur de isSearchVisible, l'icône sera une loupe ou une croix
                        contentDescription = "Show or hide the favorites"
                    )
                }
            })
    }, modifier = Modifier.fillMaxSize()) { innerPadding ->
        // Affichage conditionnel de la barre de recherche
        if (isSearchVisible) {
            TextField(
                value = searchText,
                onValueChange = { setSearchText(it) },
                placeholder = { Text("Search...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            )
        }

        if (displayList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .offset(y = if (isSearchVisible) 60.dp else 0.dp)
            )
            { // On affiche tous les jeux qui correspondent à la recherche ou au filtre favori
                items(displayList) { game ->
                    GameCard(game, navController, favoriteGames)
                }
            }

        }else{ // Enfin le cas où la recherche ne donne pas de résultat ou s'il n'y a aucun favori
            Text(
                text = "No match :(",
                modifier = Modifier
                    .padding(innerPadding)
                    .offset(y = 350.dp)
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic
            )
        }
    }
}