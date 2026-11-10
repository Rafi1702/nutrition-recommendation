package com.example.nutritiontracker.ui.consumelog.page

import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.nutritiontracker.ui.consumelog.viewmodel.ConsumeLogDetailViewModel

object ConsumeLogNav {
    const val BASE_PATH = "/consume"
    const val CONSUME_LIST = "/consume/list"
    const val CONSUME_DETAIL_ROUTE = "/consume/detail/{id}"

    fun createDetailRoute(id: String): String = "/consume/detail/$id"
}

fun NavGraphBuilder.consumeGraph(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {}
) {
    composable(
        route = ConsumeLogNav.CONSUME_DETAIL_ROUTE,
        arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
            }
        )
    ) {
        val consumeLogDetailViewModel: ConsumeLogDetailViewModel = hiltViewModel()
        ConsumeLogDetail(viewModel = consumeLogDetailViewModel, onNavigateBack = onNavigateBack)
    }
}
