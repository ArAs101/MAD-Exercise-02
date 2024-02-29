package com.example.movieappmad24

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.movieappmad24.models.Movie
import com.example.movieappmad24.models.getMovies
import com.example.movieappmad24.ui.theme.MovieAppMAD24Theme


class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppMAD24Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(movies = getMovies())
                }
            }
        }
    }
}

@Composable
fun MainScreen(movies: List<Movie>) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MovieAppTopBar()
        },
        bottomBar = {
            MovieAppBottomBar()
        }
    ) { innerPadding ->
        ExpandableCards(movies = movies, paddingValues = innerPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieAppTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text("Movie App")
        },
        colors = topAppBarColors(
            containerColor = Color.Red
        )
    )
}

@Composable
fun MovieAppBottomBar() {
    NavigationBar(
        containerColor = Color.LightGray
    ) {
        NavigationBarItem(selected = false,
            onClick = { /*TODO*/ },
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") }
        )
        NavigationBarItem(selected = false,
            onClick = { /*TODO*/ },
            icon = {
                Icon(
                    Icons.Default.Star,
                    contentDescription = "Watchlist"
                )
            },
            label = { Text("Watchlist") }
        )
    }
}

@Composable
fun ExpandableCards(movies: List<Movie>, paddingValues: PaddingValues) {
    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        items(movies) { movie ->
            var expandedState by remember { mutableStateOf(false) }
            val rotationState by animateFloatAsState(
                targetValue = if (expandedState) 180f else 0f, label = ""
            )
            Card(
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 200,
                            easing = LinearOutSlowInEasing
                        )
                    )
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = ShapeDefaults.Medium,
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .clickable { expandedState = !expandedState }
                        .padding(16.dp)
                ) {
                    Box {
                        AsyncImage(
                            model = movie.images[1],
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop,
                        )

                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Add to Favorites",
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = movie.title,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand for more details",
                            modifier = Modifier
                                .rotate(rotationState)
                                .padding(8.dp)
                        )
                    }

                    if (expandedState) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Director: ${movie.director}")
                            Text(text = "Released: ${movie.year}")
                            Text(text = "Genres: ${movie.genre}")
                            Text(text = "Actors: ${movie.actors}")
                            Text(text = "Rating: ${movie.rating}")
                            Divider(
                                thickness = 1.dp,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            Text(text = "Plot: ${movie.plot}")
                        }
                    }
                }
            }
        }
    }
}