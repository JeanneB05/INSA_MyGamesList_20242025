package com.insa.mygamelist

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
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
import coil3.compose.AsyncImage
import com.insa.mygamelist.data.Game
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.ui.theme.MyGamesListTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        IGDB.load(this)

        enableEdgeToEdge()
        setContent {

            MyGamesListTheme {
                Scaffold(topBar = {
                    TopAppBar(colors = topAppBarColors(
                        containerColor = Color(144,238,144),
                        titleContentColor = Color.Black,
                    ), title = { Text("My Games List") })
                }, modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LazyColumn (modifier = Modifier.padding(innerPadding)){
                        items(IGDB.games.size) {
                            index ->
                                val game = IGDB.games[index]
                                GameCard(game)
                        }
                    }


                }
            }
        }


    }

    @Composable
    fun GameCard(game : Game){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)
                .height(100.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(229, 224, 232))
        ) {
            AsyncImage(
                model = "https:" + IGDB.covers.find({ game.cover == it.id })?.url,
                contentDescription = "Cover of the game" + game.name,
                modifier = Modifier.size(90.dp).padding(10.dp)
            )
            Column() {
                Text(
                    game.name,
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

                val mygenres = "Genres : " + IGDB.genres.filter { it.id in game.genres }.joinToString(", ") { it.name }
                Text(
                    mygenres,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}