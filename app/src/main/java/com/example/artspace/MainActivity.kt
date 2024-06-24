package com.example.artspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artspace.ui.theme.ArtSpaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtSpaceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ArtSpaceLayout()
                }
            }
        }
    }
}

data class Artwork (
    val imageId: Int,
    val title: Int,
    val authorYear: Int,
    var isFavorite: MutableState<Boolean> = mutableStateOf(false)
)

object  ArtRepository {
    val artworks = mutableListOf(
        Artwork(R.drawable.artwork_1, R.string.artwork_title_1, R.string.artist_year_1),
        Artwork(R.drawable.artwork_2, R.string.artwork_title_2, R.string.artist_year_2),
        Artwork(R.drawable.artwork_3, R.string.artwork_title_3, R.string.artist_year_3),
        Artwork(R.drawable.artwork_4, R.string.artwork_title_4, R.string.artist_year_4),
        Artwork(R.drawable.artwork_5, R.string.artwork_title_5, R.string.artist_year_5)
    )

    fun toggleFavorite(index: Int) {
        val artwork = artworks[index]
        artwork.isFavorite.value = !artwork.isFavorite.value
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ArtSpaceLayout(modifier: Modifier = Modifier) {
    var artworkIndex by remember { mutableStateOf(0) }
    val totalArtworks = 4

    Column (
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = modifier.width(8.dp))
            Text(text = "\uD83C\uDFA8", fontSize = 24.sp)
            Text(text = "\uD83D\uDD8C\uFE0F", fontSize = 24.sp)
        }
        AnimatedContent(
            targetState = artworkIndex,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally { width -> width } + fadeIn() with
                            slideOutHorizontally { width -> -width } + fadeOut()
                } else {
                    slideInHorizontally { width -> -width } + fadeIn() with
                            slideOutHorizontally { width -> width } + fadeOut()
                }
            },
            label = ""
        ) {
            targetState ->
            val currentArtwork = ArtRepository.artworks[targetState]
            ArtSpaceComponent(
                artwork = currentArtwork,
                onFavoriteClick = { ArtRepository.toggleFavorite(targetState) })
        }
        Row (
            modifier = modifier.padding(start = 12.dp, end = 12.dp)
        )
        {
            Button(
                onClick = {
                  if (artworkIndex <= 0) {
                      artworkIndex = 4
                  } else {
                      artworkIndex--
                  }
                },
                modifier = modifier.weight(1f)
            ) {
                Text(text = "Previous")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (artworkIndex >= totalArtworks) {
                        artworkIndex = 0
                    } else {
                        artworkIndex++
                    }},
                modifier = modifier.weight(1f)
            ) {
                Text(text = "Next")
            }
        }
    }
}

@Composable
fun ArtSpaceComponent(
    artwork: Artwork,
    onFavoriteClick: (Artwork) -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (modifier = modifier.padding(bottom = 16.dp)) {
            val iconId = if (artwork.isFavorite.value) R.drawable.favorite_filled else R.drawable.favorite_outline
            Image(
                painter = painterResource(id = iconId),
                contentDescription = null,
                modifier = modifier.clickable(onClick = { onFavoriteClick(artwork) }).size(32.dp)
            )
        }
        Surface (
            shadowElevation = 6.dp
        ) {
            Box(modifier = modifier.padding(12.dp)) {
                Image (
                    painter = painterResource(id = artwork.imageId),
                    contentDescription = null
                )
            }
        }
        Column (
            modifier = modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 0.dp)
                .background(color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f))
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = artwork.title),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 24.sp,
                fontWeight = FontWeight.Light
            )
            Spacer(modifier = modifier.height(8.dp))
            Text(
                text = stringResource(id = artwork.authorYear),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    ArtSpaceTheme {
        ArtSpaceLayout()
    }
}