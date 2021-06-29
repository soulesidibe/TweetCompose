package com.soulesidibe.tweetcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.soulesidibe.tweetcompose.ui.theme.TweetComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TweetComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    TweetScreen(getTheTweet()) {

                    }
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
fun TweetScreen(tweet: Tweet, modifier: Modifier = Modifier, content: @Composable () -> Unit) {

}

@Preview
@Composable
fun PreviewTweetScreen() {
    TweetScreen(tweet = getTheTweet()) {

    }
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