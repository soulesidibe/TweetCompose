package com.soulesidibe.tweetcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.soulesidibe.tweetcompose.ui.theme.*

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

fun getTheTweet(isVerified: Boolean = true): Tweet {
    val imageLink = "https://pbs.twimg.com/profile_images/1168932726461935621/VRtfrDXq_400x400.png"
    val user = User(
        displayName = "Android Developers",
        handle = "@AndroidDev",
        image = imageLink,
        isVerified = isVerified
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
        Column(modifier = modifier) {
            HeaderContent(tweet, modifier = Modifier.height(88.dp))
            Spacer(modifier = Modifier.height(16.dp))
            TweetDataContent(tweet)
            FooterContent(tweet)
        }
    }
}

@Composable
fun TweetDataContent(tweet: Tweet) {
    Text(
        text = messageFormatter(tweet.data.message),
        color = Color.Black,
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
        fontSize = 23.sp
    )
}

@Composable
fun HeaderContent(tweet: Tweet, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Image(
            modifier = Modifier
                .size(56.dp, 56.dp)
                .weight(1f),
            painter = rememberCoilPainter(tweet.user.image),
            contentDescription = stringResource(R.string.image_content_desc),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(4f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            DisplayNameAndVerified(tweet.user)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = tweet.user.handle, color = LightDateColor)
        }
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(id = R.string.content_desc_tweet_options),
            tint = TweetOptionsColorLight
        )

    }
}

@Composable
fun FooterContent(tweet: Tweet) {

}

@Composable
fun DisplayNameAndVerified(user: User) {

    val constraintSet = ConstraintSet {
        val displayName = createRefFor("display_name")
        val verified = createRefFor("verified")

        constrain(displayName) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            bottom.linkTo(parent.bottom)
        }
        if (user.isVerified) {
            constrain(verified) {
                start.linkTo(displayName.end, 8.dp)
                top.linkTo(displayName.top)
                bottom.linkTo(displayName.bottom)
            }
        }

    }
    ConstraintLayout(constraintSet = constraintSet) {
        Text(
            text = user.displayName,
            modifier = Modifier.layoutId("display_name"),
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        if (user.isVerified) {
            Icon(
                modifier = Modifier.layoutId("verified"),
                painter = painterResource(id = R.drawable.ic_twitter_verified_badge),
                contentDescription = stringResource(id = R.string.content_desc_verified_Account),
                tint = MaterialTheme.colors.secondary
            )
        }
    }
}

@Preview(name = "Header", showBackground = true, heightDp = 88)
@Composable
fun PreviewHeaderContent() {
    HeaderContent(tweet = getTheTweet(false))
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
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = TopBarSeparator)
        )
    }
}

//@Preview
//@Composable
//fun PreviewTweetScreen() {
//    TweetScreen(tweet = getTheTweet())
//}

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