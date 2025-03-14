package com.insa.mygamelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.paging.Pager
import androidx.paging.PagingConfig
import coil3.compose.AsyncImage
import com.insa.mygamelist.data.Game
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.data.Platform
import com.insa.mygamelist.ui.theme.MyGamesListTheme
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

@Serializable
object HomeRoute
@Serializable
data class GameRoute(val id : Long)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        IGDB.load(this)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()                         // Permet de naviguer entre les deux pages de l'appli
            val favoriteGames = remember { mutableStateOf(setOf<Long>()) }      // Permet de garder les mêmes favoris entre les deux pages de l'appli

            MyGamesListTheme {
                NavHost(navController, startDestination = HomeRoute) {
                    composable<HomeRoute> {
                        HomeScreen(navController, favoriteGames)
                    }
                    composable<GameRoute> { backStackEntry ->
                        val route = backStackEntry.toRoute<GameRoute>()
                        GameScreen(route.id,navController, favoriteGames)
                    }
                }
            }
        }

    }

    // Méthode qui permet d'afficher les images des logos
    @Composable
    fun LogoDisplay(platform : Platform){
        AsyncImage(
            model = "https:" + IGDB.platform_logos.find{ platform.platform_logo == it.id }?.url,       // On récupère l'image
            contentDescription = "Logos of the platforms",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(90.dp).padding(10.dp)
        )
    }

    // Permet de créer la case d'un jeu sur le HomeScreen
    @Composable
    fun GameCard(game: Game, navController: NavController, favoriteGames: MutableState<Set<Long>>){
        val isFavorite = favoriteGames.value.contains(game.id)      // On vérifie si le jeu est enregistré en tant que favori
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)
                .height(100.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable{navController.navigate(GameRoute(game.id))}      // Permet de passer sur le GameScreen du jeu
                .background(Color(229, 224, 232)),
        ) {
            // Affichage de l'image à gauche
            AsyncImage(
                model = "https:" + IGDB.covers.find{ game.cover == it.id }?.url,      // On récupère l'image
                contentDescription = "Cover of the game" + game.name,
                modifier = Modifier.size(90.dp).padding(10.dp)
            )
            Column(modifier = Modifier.width(250.dp).align(Alignment.CenterVertically)) {
                // Affichage du nome du jeu au milieu
                Text(
                    game.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.W600,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
                // Affichage des genres du jeu en-dessous de son nom
                val mygenres = "Genres : " + IGDB.genres.filter { it.id in game.genres }.joinToString(", ") { it.name }  // on récupère tous les genres associé au jeu et on les sépare avec une ,
                Text(
                    mygenres,                               // affiche les genres
                    maxLines = 1,                           // permet de mettre les genres sur une seule ligne
                    overflow = TextOverflow.Ellipsis        // permet de mettre les ... quand la liste de genres est trop longue
                )
            }
            Column{
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
                        contentDescription = "Favori"
                    )
                }
            }
        }
    }

    // Ecran d'affichage d'accueil, qui contient la liste de tous les jeux
    @Composable
    fun HomeScreen(navController: NavHostController, favoriteGames: MutableState<Set<Long>>) {
        var isSearchVisible by rememberSaveable { mutableStateOf(false) }       // Permet de savoir si l'icône de recherche est activée ou non
        var searchText by rememberSaveable { mutableStateOf("") }               // Contiendra le texte de recherche rentré par l'utilisateur
        var isFavoriteSelected by rememberSaveable { mutableStateOf(false) }       // Permet de savoir si on veut afficher la liste des favoris ou non

        // Filtre la liste des jeux en fonction du texte recherché et de si l'affichage des favoris est demandé
        val filteredGames = IGDB.games.filter { game ->
            val matchesSearch = game.name.contains(searchText, ignoreCase = true) ||
                    IGDB.genres.any { it.id in game.genres && it.name.contains(searchText, ignoreCase = true) } ||
                    IGDB.platforms.any { it.id in game.platforms && it.name.contains(searchText, ignoreCase = true) }

            val isFavorite = favoriteGames.value.contains(game.id)

            matchesSearch && (!isFavoriteSelected || isFavorite)
        }

        Scaffold(topBar = {
            TopAppBar(colors = topAppBarColors(
                containerColor = Color(144,238,144),
                titleContentColor = Color.Black,
            ), title = { Text("My Games List") },
                actions = {
                    IconButton(onClick = { isSearchVisible = !isSearchVisible }) {
                        Icon(
                            imageVector = if (isSearchVisible) Icons.Default.Close else Icons.Default.Search,  // en fonction de la valeur de isSearchVisible, l'icône sera une loupe ou une croix
                            contentDescription = "Afficher/Cacher la recherche"
                        )
                    }
                    IconButton(onClick = { isFavoriteSelected = !isFavoriteSelected }) {
                        Icon(
                            imageVector = if (isFavoriteSelected) Icons.Default.Favorite else Icons.Default.FavoriteBorder,  // en fonction de la valeur de isSearchVisible, l'icône sera une loupe ou une croix
                            contentDescription = "Afficher/Cacher la recherche"
                        )
                    }
                })
        }, modifier = Modifier.fillMaxSize()) { innerPadding ->
            if (isSearchVisible) {      // Affichage conditionnel de la barre de recherche
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Rechercher...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                )
                if (filteredGames.isEmpty()) {
                    Text(
                        text = "No match found :(",
                        modifier = Modifier.padding(innerPadding).offset(x = 10.dp, y = 70.dp),
                        fontStyle = FontStyle.Italic
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .offset(y = if (isSearchVisible) 60.dp else 0.dp)
                    ) {
                        items(
                            items = filteredGames,            // Va itérer sur chaque jeu correspondant à la recherche
                            itemContent = {game -> GameCard(game, navController, favoriteGames)}      // On crée la carte du jeu
                        )
                    }
                }
            }else{
                LazyColumn (modifier = Modifier.padding(innerPadding)){
                    if (!isFavoriteSelected){
                        items(IGDB.games.size){
                                index ->
                            val game =IGDB.games[index]
                            GameCard(game, navController, favoriteGames)
                        }
                    }else{
                        items(favoriteGames.value.size){
                                index ->
                            val game = IGDB.games.find{favoriteGames.value.toList()[index] == it.id}
                            if (game != null) {
                                GameCard(game, navController, favoriteGames)
                            }
                        }
                    }
                }
            }
        }
    }

    // Ecran d'affichage des détails d'un jeu
    @Composable
    fun GameScreen(id : Long, navController: NavHostController, favoriteGames: MutableState<Set<Long>>) {
        val game = IGDB.games.find{ id == it.id }
        val name = game?.name ?: "Unfound"          //Obligé de mettre un Elvis car Text ne prend pas de type String? (type renvoyé par .find)
        val genres = IGDB.genres.filter { it.id in (game?.genres ?: listOf(String)) }.joinToString(", ") { it.name }  // on récupère tous les genres associé au jeu et on les sépare avec une ,
        val summary = game?.summary ?: "Unfound"
        val platforms = IGDB.platforms.filter { it.id in game?.platforms!! }

        Scaffold(topBar = {
            TopAppBar(colors = topAppBarColors(
                containerColor = Color(144,238,144),
                titleContentColor = Color.Black,
            ), title = { Text(name) },
                navigationIcon = { IconButton(onClick = {navController.navigateUp()}){      // Flèche de retour arrière
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }},
                actions = {
                    val isFavorite = favoriteGames.value.contains(id)
                    IconButton(onClick = {
                        favoriteGames.value = if (isFavorite) {     // Si le jeu est en favori et qu'on clique sur l'icône
                            favoriteGames.value - id                // Alors le jeu n'est plus en favori
                        } else {
                            favoriteGames.value + id                // Sinon il est ajouté aux favoris
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
            Column (horizontalAlignment = Alignment.CenterHorizontally, modifier=Modifier.padding(innerPadding)){
                // Affichage du nom du jeu en haut de la page
                Text(
                    text = name,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.W600,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )

                // Affichage de l'image du jeu en dessous
                AsyncImage(
                    model = "https:" + IGDB.covers.find{ game?.cover == it.id }?.url,
                    contentDescription = "Cover of the game $name",
                    modifier = Modifier.size(250.dp).padding(10.dp)
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
    }
}