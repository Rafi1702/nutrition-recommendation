package com.example.nutritiontracker.ui.consumelog.page

import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.nutritiontracker.ui.consumelog.viewmodel.ConsumeLogDetailViewModel

sealed class ConsumeLogNav(val route: String) {
    companion object {
        const val BASE_PATH = "/consume"
    }

    data object ConsumeList : ConsumeLogNav("/$BASE_PATH/list")
    data class ConsumeLogDetail(val consumeLogId: String) :
        ConsumeLogNav("/$BASE_PATH/detail/$consumeLogId")

}


fun NavGraphBuilder.consumeGraph(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {}
) {
    composable(
        route = ConsumeLogNav.ConsumeLogDetail("{id}").route,
        arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
            })
    ) { entry ->
        val consumeLogDetailViewModel: ConsumeLogDetailViewModel = hiltViewModel()
        ConsumeLogDetail(consumeLogDetailViewModel, onNavigateBack)
    }
}


