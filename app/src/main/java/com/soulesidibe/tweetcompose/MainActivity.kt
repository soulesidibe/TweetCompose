package com.soulesidibe.tweetcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.textInputServiceFactory
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
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
        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            HeaderContent(tweet)
            Spacer(modifier = Modifier.height(4.dp))
            TweetDataContent(tweet)
            Spacer(modifier = Modifier.height(10.dp))
            FooterContent(
                tweet, modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp)
            )
        }
    }
}

@Composable
fun TweetDataContent(tweet: Tweet) {
    Column {
        Text(
            text = messageFormatter(tweet.data.message),
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        TweetMedia(tweet.media)
    }
}

@Composable
fun TweetMedia(media: Media) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 16.dp)
    ) {
        Image(
            painter = rememberCoilPainter(
                request = media.content.first(),
                fadeIn = true,
            ),
            contentDescription = stringResource(R.string.image_content_desc),
        )
    }
}

@Preview(name = "Content", showBackground = true)
@Composable
fun PreviewTweetDataContent() {
    TweetDataContent(getTheTweet())
}

@Composable
fun HeaderContent(tweet: Tweet, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            modifier = Modifier
                .size(48.dp, 48.dp)
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
            Spacer(modifier = Modifier.height(4.dp))
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
fun FooterContent(tweet: Tweet, modifier: Modifier = Modifier) {
    val constrainsSet = ConstraintSet {
        val dateAppNameRef = createRefFor("dateAppName")
        val firstSeparator = createRefFor("separator_1")
        val likeAndRetweets = createRefFor("likesAndRetweets")
        val secondSeparator = createRefFor("separator_2")
        val actionsRef = createRefFor("actions")

        constrain(dateAppNameRef) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }

        val margin = 10.dp
        constrain(firstSeparator) {
            top.linkTo(dateAppNameRef.bottom, margin)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(likeAndRetweets) {
            top.linkTo(firstSeparator.bottom, margin)
            start.linkTo(parent.start)
        }

        constrain(secondSeparator) {
            top.linkTo(likeAndRetweets.bottom, margin)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(actionsRef) {
            top.linkTo(secondSeparator.bottom, margin)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
    }
    ConstraintLayout(constraintSet = constrainsSet, modifier = modifier) {
        Row(
            modifier = Modifier.layoutId("dateAppName"),
            horizontalArrangement = Arrangement.Start
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.caption
            ) {
                Text(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = LightDateColor)) {
                        append(tweet.data.time)
                    }
                })
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "·")
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.secondary
                        )
                    ) {
                        append(tweet.data.client)
                    }

                })
            }
        }

        val spacerModifier = Modifier
            .background(color = LightDateColor.copy(alpha = 0.4f))
            .fillMaxWidth()
            .height(0.3.dp)
        Spacer(modifier = spacerModifier.layoutId("separator_1"))

        Row(
            modifier = Modifier
                .layoutId("likesAndRetweets")
                .padding(top = 4.dp, bottom = 4.dp)
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.caption
            ) {
                Text(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${tweet.data.retweets} ")
                    }
                    withStyle(style = SpanStyle(color = LightDateColor)) {
                        append(stringResource(id = R.string.str_retweets))
                    }
                })

                Spacer(modifier = Modifier.width(4.dp))

                Text(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${tweet.data.likes} ")
                    }
                    withStyle(style = SpanStyle(color = LightDateColor)) {
                        append(stringResource(id = R.string.str_likes))
                    }
                })
            }
        }

        Spacer(modifier = spacerModifier.layoutId("separator_2"))

        Row(modifier = Modifier.layoutId("actions")) {

        }
    }
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
    HeaderContent(tweet = getTheTweet())
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
                .height(0.4.dp)
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