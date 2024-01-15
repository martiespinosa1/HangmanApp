package com.example.hagmanapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hagmanapp.ui.theme.HagmanAppTheme

val customFontFamily1 = FontFamily(Font(R.font.schoolbell))
val customFontFamily2 = FontFamily(Font(R.font.walter))

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HagmanAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navigationController = rememberNavController()
                    NavHost(
                        navController = navigationController,
                        startDestination = Routes.Splash.route
                    ) {
                        composable(Routes.Splash.route) { SplashScreen(navigationController) }
                        composable(Routes.Menu.route) { Menu(navigationController) }
                        composable(Routes.Game.route,
                            arguments = listOf(navArgument("selectedDifficulty") { type = NavType.StringType })
                        ) {
                                backStackEntry ->
                            Game(
                                navigationController,
                                backStackEntry.arguments?.getString("selectedDifficulty").orEmpty()
                            )
                        }
                        composable(Routes.Result.route,
                            arguments = listOf(
                                navArgument("hasWon") { type = NavType.BoolType },
                                navArgument("palabra") { type = NavType.StringType },
                                navArgument("intentos") { type = NavType.IntType },
                                navArgument("fallos") { type = NavType.IntType }
                            )
                        ) {
                                backStackEntry ->
                            Result(
                                navigationController,
                                backStackEntry.arguments?.getBoolean("hasWon") ?: false,
                                backStackEntry.arguments?.getString("palabra") ?: "",
                                backStackEntry.arguments?.getInt("intentos") ?: 0,
                                backStackEntry.arguments?.getInt("fallos") ?: 0,
                                backStackEntry.arguments?.getString("diff") ?: "Medium"
                            )
                        }
                    }
                }
            }
        }
    }
}

