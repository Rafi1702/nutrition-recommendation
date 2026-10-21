package com.example.nutritiontracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritiontracker.data.local.LocalTokenDataSource
import com.example.nutritiontracker.data.utils.NetworkException
import com.example.nutritiontracker.domain.usecase.GetUserProfileUseCase
import com.example.nutritiontracker.ui.authentication.page.AuthPath
import com.example.nutritiontracker.ui.home.page.HomePath
import com.example.nutritiontracker.ui.profile.page.ProfileNav
import com.example.nutritiontracker.ui.theme.Spacing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class EntryScreenUiState {
    data object Initial : EntryScreenUiState()
    data class Navigate(val route: String) : EntryScreenUiState()
    data class Error(val error: Throwable) : EntryScreenUiState()
}

@HiltViewModel
class EntryScreenViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val localTokenDataSource: LocalTokenDataSource
) : ViewModel() {

    private val _entryNavigationState =
        MutableStateFlow<EntryScreenUiState>(EntryScreenUiState.Initial)

    val entryNavigationState = _entryNavigationState.asStateFlow()

    init {
        initEntry()
    }

    fun initEntry() {
        viewModelScope.launch {
            val (accessToken, refreshToken) = localTokenDataSource.getUserToken()

            if (accessToken == null || refreshToken == null) {
                return@launch _entryNavigationState.update { EntryScreenUiState.Navigate(AuthPath.AUTH) }
            }

            val userProfile = getUserProfileUseCase()

            userProfile.fold(
                onSuccess = { profile ->
                    if (profile != null) {
                        return@launch _entryNavigationState.update {
                            EntryScreenUiState.Navigate(
                                HomePath.HOME
                            )
                        }
                    }
                    return@launch _entryNavigationState.update { EntryScreenUiState.Navigate(ProfileNav.USER_PROFILE) }
                },
                onFailure = { error ->
                    if(error !is NetworkException.Unauthorized){
                        return@launch _entryNavigationState.update { EntryScreenUiState.Error(error) }
                    }
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EntryScreen(entryScreenUiState: EntryScreenUiState = EntryScreenUiState.Initial) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.m, Alignment.CenterVertically)
    ) {
        when (entryScreenUiState) {
            is EntryScreenUiState.Initial -> {
                CircularProgressIndicator()
            }
            is EntryScreenUiState.Error -> {
                Text(entryScreenUiState.error.message ?: "Unexpected Error")
            }

            else -> {}
        }


    }
}