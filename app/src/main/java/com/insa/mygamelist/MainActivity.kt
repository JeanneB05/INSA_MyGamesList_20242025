package com.insa.mygamelist

import android.graphics.drawable.Icon
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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
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
import coil3.compose.AsyncImage
import com.insa.mygamelist.data.Game
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.data.Platform
import com.insa.mygamelist.data.Platform_Logo
import com.insa.mygamelist.ui.theme.MyGamesListTheme
import kotlinx.serialization.Serializable
import okhttp3.internal.notifyAll

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
            val navController = rememberNavController()

            MyGamesListTheme {
                NavHost(navController, startDestination = HomeRoute) {
                    composable<HomeRoute> {
                        HomeScreen(navController)
                    }
                    composable<GameRoute> { backStackEntry ->
                        val route = backStackEntry.toRoute<GameRoute>()
                        GameScreen(route.id,navController)
                    }
                }
            }
        }

    }

    // Méthode qui permet d'afficher les images des logos
    @Composable
    fun LogoDisplay(platform : Platform){
        AsyncImage(
            model = "https:" + IGDB.platform_logos.find({ platform.platform_logo == it.id })?.url,
            contentDescription = "Logos of the platforms",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(90.dp).padding(10.dp)
        )
    }

    // Permet d'afficher la case d'un jeu sur le HomeScreen
    @Composable
    fun GameCard(game : Game, navController: NavController){
        var isFavorite by rememberSaveable { mutableStateOf(false) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)
                .height(100.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable{
                    navController.navigate(GameRoute(game.id))
                }
                .background(Color(229, 224, 232)),
        ) {
            AsyncImage(
                model = "https:" + IGDB.covers.find({ game.cover == it.id })?.url,      // on récupère l'image
                contentDescription = "Cover of the game" + game.name,
                modifier = Modifier.size(90.dp).padding(10.dp)
            )
            Column(modifier = Modifier.width(250.dp).align(Alignment.CenterVertically)) {
                Text(
                    game.name,          // on affiche le nom du jeu
                    fontSize = 17.sp,
                    fontWeight = FontWeight.W600,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )

                val mygenres = "Genres : " + IGDB.genres.filter { it.id in game.genres }.joinToString(", ") { it.name }  // on récupère tous les genres associé au jeu et on les sépare avec une ,
                Text(
                    mygenres,                               // affiche les genres
                    maxLines = 1,                           // permet de mettre les genres sur une seule ligne
                    overflow = TextOverflow.Ellipsis        // permet de mettre les ... quand la liste de genres est trop longue
                )
            }
            Column(){
                IconButton(onClick = { isFavorite = !isFavorite }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,  // en fonction de la valeur de isSearchVisible, l'icone sera une loupe ou une croix
                        contentDescription = "Afficher/Cacher la recherche"
                    )
                }
            }
        }
    }

    // Ecran d'affichage d'accueil, qui contient la liste de tous les jeux
    @Composable
    fun HomeScreen(navController: NavHostController) {
        var isSearchVisible by rememberSaveable { mutableStateOf(false) }
        var searchText by rememberSaveable { mutableStateOf("") }

        // Filtrer la liste des jeux en fonction du texte recherché
        val filteredGames = IGDB.games.filter { game ->
            game.name.contains(searchText, ignoreCase = true) ||
                    IGDB.genres
                        .filter {game.genres.contains(it.id)}
                        .find {it.name.contains(searchText, ignoreCase = true)} != null ||
                    IGDB.platforms
                        .filter { game.platforms.contains(it.id)}
                        .find {it.name.contains(searchText, ignoreCase = true)} != null
        }

        Scaffold(topBar = {
            TopAppBar(colors = topAppBarColors(
                containerColor = Color(144,238,144),
                titleContentColor = Color.Black,
            ), title = { Text("My Games List") },
                actions = {
                    IconButton(onClick = { isSearchVisible = !isSearchVisible }) {
                        Icon(
                            imageVector = if (isSearchVisible) Icons.Default.Close else Icons.Default.Search,  // en fonction de la valeur de isSearchVisible, l'icone sera une loupe ou une croix
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

            }
            if(isSearchVisible) {
                if (filteredGames.isEmpty()) {
                    Text(
                        "No match found :(",
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
                            items = filteredGames,
                            itemContent = {                 //va itérer sur chaque jeu correspondant à la recherche
                                    game ->
                                //val game = IGDB.games[index]        // on récupère le jeu
                                GameCard(game, navController)       // on crée la carte du jeu
                            })
                    }
                }
            }else{
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .offset(y = if (isSearchVisible) 60.dp else 0.dp)
                ) {
                    items(
                        items = IGDB.games,
                        itemContent = {                 //va itérer sur chaque jeu correspondant à la recherche
                                game ->
                            //val game = IGDB.games[index]        // on récupère le jeu
                            GameCard(game, navController)       // on crée la carte du jeu
                        })
                }
            }
        }
    }

    // Ecran d'affichage des détails d'un jeu
    @Composable
    fun GameScreen(id : Long, navController: NavHostController) {
        val game = IGDB.games.find({ id == it.id })
        val name = game?.name ?: "Unfound"          //Obligé de mettre un Elvis car Text ne prend pas de type String? (type renvoyé par .find)
        val genres = IGDB.genres.filter { it.id in (game?.genres ?: listOf(String)) }.joinToString(", ") { it.name }  // on récupère tous les genres associé au jeu et on les sépare avec une ,
        val summary = game?.summary ?: "Unfound"
        val platforms = IGDB.platforms.filter { it.id in game?.platforms!! }

        Scaffold(topBar = {
            TopAppBar(colors = topAppBarColors(
                containerColor = Color(144,238,144),
                titleContentColor = Color.Black,
            ), title = { Text(name) },
                navigationIcon = { IconButton(onClick = {navController.navigateUp()}){      // flèche de retour arrière
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }})
        }, modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column (horizontalAlignment = Alignment.CenterHorizontally,
                modifier=Modifier.padding(innerPadding)){
                Text(
                    name,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.W600,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )

                AsyncImage(
                    model = "https:" + IGDB.covers.find({ game?.cover == it.id })?.url,
                    contentDescription = "Cover of the game" + name,
                    modifier = Modifier.size(250.dp).padding(10.dp)
                )

                Text(
                    genres,                               // affiche les genres
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic,
                    maxLines = 1,                           // permet de mettre les genres sur une seule ligne
                    overflow = TextOverflow.Ellipsis        // permet de mettre les ... quand la liste de genres est trop longue
                )

                LazyRow {
                    items(platforms.size) {         //on va itérer sur chaque plateforme associée au jeu
                            index ->
                        val platform = platforms[index]
                        LogoDisplay(platform)       // affichage du logo du jeu
                    }
                }

                Text(
                    summary,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}