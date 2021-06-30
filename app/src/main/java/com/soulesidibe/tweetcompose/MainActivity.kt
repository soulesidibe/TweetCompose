package com.soulesidibe.tweetcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.soulesidibe.tweetcompose.ui.theme.StatusBarLightColor
import com.soulesidibe.tweetcompose.ui.theme.TopBarSeparator
import com.soulesidibe.tweetcompose.ui.theme.TweetComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TweetComposeTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = MaterialTheme.colors.isLight

                SideEffect {
                    // Update all of the system bar colors to be transparent, and use
                    // dark icons if we're in light theme
                    systemUiController.setSystemBarsColor(
                        color = if (useDarkIcons) StatusBarLightColor else Color.Black,
                        darkIcons = useDarkIcons
                    )

                    // setStatusBarsColor() and setNavigationBarsColor() also exist
                }

                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    TweetScreen(getTheTweet())
                }
            }
        }
    }


}

fun getTheTweet(): Tweet {
    val imageLink = "https://pbs.twimg.com/profile_images/1168932726461935621/VRtfrDXq_400x400.png"
    val user = User(
        displayName = "Android Developers",
        handle = "@AndroidDev",
        image = imageLink,
        isVerified = true
    )
    val media = Media(listOf("https://pbs.twimg.com/media/E5AurCOXoAolXo3?format=jpg&name=large"))
    val tweetData = TweetData(
        message = """
                🚀#JetpackCompose supports Material Components and Material Theming out of the box! 

                #ADBpodcast Ep.168 covers how to build reusable components with slot APIs, when to use CompositionLocals, and what the future holds for support of Material You → https://goo.gle/3jrmElc
            """.trimIndent(),
        time = "1:00 AM · Jun 29, 2021",
        client = "Sprinklr",
        retweets = 21,
        likes = 132
    )
    return Tweet(user = user, media = media, data = tweetData)
}

@Composable
fun TweetScreen(tweet: Tweet, modifier: Modifier = Modifier) {
    Scaffold(topBar = { TopBar() }) {
        Column {
            HeaderContent(tweet)
            TweetDataContent(tweet)
            FooterContent(tweet)
        }
    }
}

@Composable
fun FooterContent(tweet: Tweet) {

}

@Composable
fun TweetDataContent(tweet: Tweet) {

}

@Composable
fun HeaderContent(tweet: Tweet) {

}

@Composable
private fun TopBar() {
    Column {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.background,
            elevation = 0.dp,
            title = {
                Text(text = stringResource(R.string.str_topbar_title))
            },
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        tint = MaterialTheme.colors.secondary,
                        contentDescription = stringResource(R.string.content_desc_arrow_back)
                    )
                }
            }
        )
        Spacer(modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(color = TopBarSeparator))
    }
}

@Preview
@Composable
fun PreviewTweetScreen() {
    TweetScreen(tweet = getTheTweet())
}

data class Tweet(val user: User, val media: Media, val data: TweetData)
data class User(
    val displayName: String,
    val handle: String,
    val image: String,
    val isVerified: Boolean
)

data class Media(val content: List<String>)
data class TweetData(
    val message: String,
    val time: String,
    val client: String,
    val retweets: Int,
    val likes: Int
)