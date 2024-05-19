package com.example.b11109050hw2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter

data class Attraction(val name: String, val imageRes: Int, val descriptionRes: Int, val mapUrl: String)

@Composable
fun AttractionList(navController: NavController, attractions: List<Attraction>) {
    LazyColumn {
        items(attractions) { attraction ->
            ListItem(attraction = attraction, navController = navController)
        }
    }
}

@Composable
fun ListItem(attraction: Attraction, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { navController.navigate("detail/${attraction.name}") }
    ) {
        Column(modifier = Modifier.padding(25.dp)) {
            Text(
                text = attraction.name,
                fontSize = 25.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun AttractionDetail(attraction: Attraction, navController: NavController) {
    Column(modifier = Modifier.padding(30.dp)) {
        Button(onClick = { navController.popBackStack() }) {
            Text("上一頁")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = attraction.name, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        val imagePainter = rememberAsyncImagePainter(attraction.imageRes)
        val context = LocalContext.current
        Image(
            painter = imagePainter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(id = attraction.descriptionRes), fontSize = 16.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(attraction.mapUrl))
            context.startActivity(intent)
        }) {
            Text("在 Google 地圖中查看")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun NavGraph(startDestination: String = "list", attractions: List<Attraction>) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable("list") {
            AttractionList(navController = navController, attractions = attractions)
        }
        composable(
            "detail/{attractionName}",
            arguments = listOf(navArgument("attractionName") { type = NavType.StringType })
        ) { backStackEntry ->
            val attractionName = backStackEntry.arguments?.getString("attractionName")
            val attraction = attractions.find { it.name == attractionName }
            attraction?.let { AttractionDetail(attraction = it, navController = navController) }
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val attractions = listOf(
                Attraction("La Tour Eiffel", R.drawable.la_tour_eiffel, R.string.la_tour_eiffel_description, "geo:48.858093, 2.294694"),
                Attraction("Taipei 101", R.drawable.taipei_101, R.string.taipei_101_description, "geo:25.033963, 121.564468"),
                Attraction("Torre di Pisa", R.drawable.torre_di_pisa, R.string.torre_di_pisa_description, "geo:43.722952, 10.396597"),
                Attraction("Taj Mhl", R.drawable.taj_mhl, R.string.taj_mhl_description, "geo:27.175144, 78.042142"),
                Attraction("Louvre Museum", R.drawable.louvre_museum, R.string.louvre_museum_description, "geo:48.860611, 2.337644"),
                Attraction("Giza Pyramid Complex", R.drawable.giza_pyramid_complex, R.string.giza_pyramid_complex_description, "geo:29.979234, 31.134202"),
                Attraction("Stonehenge", R.drawable.stonehenge, R.string.stonehenge_description, "geo:51.178882, -1.826215"),
                Attraction("Palace of Versailles", R.drawable.palace_of_versailles, R.string.palace_of_versailles_description, "geo:48.804865, 2.120355"),
                Attraction("The Little Mermaid", R.drawable.the_little_mermaid, R.string.the_little_mermaid_description, "geo:55.692908, 12.599283")
            )
            NavGraph(attractions = attractions)
        }
    }
}


