package com.example.nutritiontracker.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritiontracker.R
import com.example.nutritiontracker.data.local.LocalTokenDataSource
import com.example.nutritiontracker.data.utils.NetworkException
import com.example.nutritiontracker.domain.AppException
import com.example.nutritiontracker.domain.model.UserProfileData
import com.example.nutritiontracker.domain.model.UserProfileWithAllergens
import com.example.nutritiontracker.domain.usecase.user_profile.GetUserProfileWithAllergensUseCase
import com.example.nutritiontracker.ui.authentication.page.AuthPath
import com.example.nutritiontracker.ui.home.page.HomePath
import com.example.nutritiontracker.ui.profile.page.ProfileNav
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.typography
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.ConnectException
import javax.inject.Inject


//specific class just for entry screen state
sealed class EntryScreenUiState {
    data object Initial : EntryScreenUiState()
    data class Navigate(
        val route: String,
        val data: UserProfileData<UserProfileWithAllergens>? = null
    ) : EntryScreenUiState()

    data class Error(val error: Throwable) : EntryScreenUiState()
}

@HiltViewModel
class EntryScreenViewModel @Inject constructor(
    private val getUserProfileWithAllergensUseCase: GetUserProfileWithAllergensUseCase,
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
            _entryNavigationState.update { EntryScreenUiState.Initial }
            val (accessToken, refreshToken) = localTokenDataSource.getUserToken()

            delay(3000)
            if (accessToken == null || refreshToken == null) {
                return@launch _entryNavigationState.update { EntryScreenUiState.Navigate(AuthPath.AUTH) }
            }

            val userProfile = getUserProfileWithAllergensUseCase()

            userProfile.fold(
                onSuccess = { profileWithAllergens ->
                    profileWithAllergens?.let {
                        if (profileWithAllergens.fromLocal) return@launch _entryNavigationState.update {
                            EntryScreenUiState.Navigate(
                                ProfileNav.USER_PROFILE,
                                data = profileWithAllergens
                            )
                        }

                        return@launch _entryNavigationState.update {
                            EntryScreenUiState.Navigate(
                                HomePath.BASE_HOME_PATH,
                                profileWithAllergens,
                            )
                        }
                    }
                    return@launch _entryNavigationState.update {
                        EntryScreenUiState.Navigate(
                            ProfileNav.USER_PROFILE,
                        )
                    }
                },
                onFailure = { error ->
                    if (error !is NetworkException.Unauthorized) {
                        return@launch _entryNavigationState.update { EntryScreenUiState.Error(error) }
                    }
                }
            )
        }
    }

    fun getUserProfile() {
        viewModelScope.launch {

        }
    }
}


@Composable
fun EntryScreen(
    entryScreenUiState: EntryScreenUiState = EntryScreenUiState.Initial,
    onRefresh: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.m, Alignment.CenterVertically)
    ) {
        when (entryScreenUiState) {
            is EntryScreenUiState.Error -> {
                Log.d("[DEBUG]", "${entryScreenUiState.error}")
                if (entryScreenUiState.error is AppException.Network.ConnectionFailed) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(
                            Spacing.l,
                            Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_error_system),
                            contentDescription = "server_error_icon"
                        )
                        Text(
                            "${stringResource(R.string.system_error_label)} :(",
                            style = typography.titleLarge
                        )
                        Button(onClick = {
                            onRefresh()
                        }, shape = RoundedCornerShape(8.dp)) {
                            Text("Refresh")
                        }
                    }
                }
            }

            else -> {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EntryScreenError() {
    NutritionTrackerTheme {
        EntryScreen(EntryScreenUiState.Error(ConnectException("...")))
    }
}