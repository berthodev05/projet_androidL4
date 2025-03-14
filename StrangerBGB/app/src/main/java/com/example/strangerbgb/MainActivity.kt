package com.example.strangerbgb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.strangerbgb.ui.theme.StrangerBGBTheme
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StrangerBGBTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    StangerBookApp()
                }
            }
        }
    }
}

@Composable
fun StangerBookApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("home") {
            HomeScreen()
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate("home") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.personne),
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "StrangerBook",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun HomeScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            TopBar()
        }

        items(dummyPosts) { post ->
            PostItem(post = post)
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.LightGray,
                thickness = 1.dp
            )
        }

        item {
            UserProfile()
        }
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(onClick = { /* Action Home */ }) {
            Text(text = "Home", style = MaterialTheme.typography.bodyLarge)
        }
        TextButton(onClick = { /* Action Messages */ }) {
            Text(text = "Messages", style = MaterialTheme.typography.bodyLarge)
        }
        TextButton(onClick = { /* Action Notifications */ }) {
            Text(text = "Notifications", style = MaterialTheme.typography.bodyLarge)
        }

    }
}


@Composable
fun UserProfile() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.personne),
            contentDescription = "User Profile",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "John Doe",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Développeur Android passionné. Aime le café et le code.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
fun PostItem(post: Post) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = post.profilusers),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(width = 2.dp, color = Color.LightGray, shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = post.author,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = post.timestamp,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFFF8C00))
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = post.title,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Image(
            painter = painterResource(id = post.image),
            contentDescription = "Post Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Text(
                text = "❤\uFE0F J’aime ${post.likes} ",
                modifier = Modifier.padding(top = 12.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "\uD83D\uDCAC ${post.comments} Com...",
                modifier = Modifier.padding(top = 12.dp)
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = "➡\uFE0F ${post.shares} Part...",
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

val dummyPosts = listOf(
    Post(1, "John Doe", "Il y a 5 minutes", "Super film !", R.drawable.father, 60, 4, 7, R.drawable.grass),
    Post(2, "Jane Smith", "Il y a 1 heure", "Une aventure épique !", R.drawable.vecna, 90, 12, 5, R.drawable.roller),
    Post(3, "Alice Wonder", "Il y a 10 minutes", "Expérience incroyable !", R.drawable.grass, 120, 22, 15, R.drawable.personne),
    Post(4, "Charlie Brown", "Il y a 30 minutes", "J’adore ce livre !", R.drawable.fireworks, 80, 10, 6, R.drawable.fireworks),
    Post(5, "Lucas Noir", "Il y a 2 heures", "Nouvelle photo !", R.drawable.bats, 110, 8, 9, R.drawable.personne),
    Post(6, "Emma Blue", "Il y a 45 minutes", "J’ai testé une recette.", R.drawable.haircut, 70, 14, 10, R.drawable.bats)
)

data class Post(val id: Int, val author: String, val timestamp: String, val title: String, val image: Int, val likes: Int, val comments: Int, val shares: Int, val profilusers: Int)
