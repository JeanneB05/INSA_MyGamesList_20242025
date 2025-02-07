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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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

    // Ecran d'affichage d'accueil, qui contient la liste de tous les jeux
    @Composable
    fun HomeScreen(navController: NavHostController) {
        Scaffold(topBar = {
            TopAppBar(colors = topAppBarColors(
                containerColor = Color(144,238,144),
                titleContentColor = Color.Black,
            ), title = { Text("My Games List") })
        }, modifier = Modifier.fillMaxSize()) { innerPadding ->
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(IGDB.games.size) {                 //va itérer sur le nb de jeu
                        index ->
                    val game = IGDB.games[index]        // on récupère le jeu
                    GameCard(game, navController)       // on crée la carte du jeu
                }
            }
        }
    }

    // Ecran d'affichage des détails d'un jeu
    @Composable
    fun GameScreen(id : Long, navController: NavHostController) {
        val game = IGDB.games.find({ id == it.id })
        val name = game?.name ?: "Unfound"          //Obligé de mettre un Elvis car Text ne prend pas de type String? (type renvoyé par .find)
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
            Row (modifier=Modifier.padding(innerPadding)){
                Text(id.toString())
            }
        }
        /*Button(onClick = { onNavigateToHomePage() }) {
            Text("Go to Profile")
        }*/
    }

    // Permet d'afficher la case d'un jeu sur le HomeScreen
    @Composable
    fun GameCard(game : Game, navController: NavController){
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
            Column() {
                Text(
                    game.name,          // on affiche le nom du jeu
                    fontSize = 17.sp,
                    fontWeight = FontWeight.W600,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
                //var g = "Genres : "
                //for (elt in IGDB.games[0].genres) {
                //    g += IGDB.genres.find({ elt == it.id })?.name + ", "
                //}
                //g = g.dropLast(2)
                // -> ce n'est pas très propre, en meilleur Kotlin ça donne la ligne juste en dessous

                val mygenres = "Genres : " + IGDB.genres.filter { it.id in game.genres }.joinToString(", ") { it.name }  // on récupère tous les genres associé au jeu et on les sépare avec une ,
                Text(
                    mygenres,                               // affiche les genres
                    maxLines = 1,                           // permet de mettre les genres sur une seule ligne
                    overflow = TextOverflow.Ellipsis        // permet de mettre les ... quand la liste de genres est trop longue
                )
            }
        }
    }
}