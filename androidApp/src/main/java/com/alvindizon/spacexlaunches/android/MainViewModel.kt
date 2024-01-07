package com.alvindizon.spacexlaunches.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.alvindizon.spacexlaunches.shared.SpaceXSDK
import com.alvindizon.spacexlaunches.shared.entity.RocketLaunch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SpaceXLaunchesUiState(
    val isLoading: Boolean = false,
    val launches: List<RocketLaunch>? = null,
    val errorMessage: String? = null
) {
    companion object {
        val Empty = SpaceXLaunchesUiState()
    }
}
class MainViewModel(private val spaceXSDK: SpaceXSDK): ViewModel() {

    private val _uiState = MutableStateFlow(SpaceXLaunchesUiState.Empty)
    val uiState: StateFlow<SpaceXLaunchesUiState> = _uiState

    init {
        fetchLaunches(false)
    }

    fun onPull() {
        fetchLaunches(true)
    }

    private fun fetchLaunches(forceReload: Boolean) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            runCatching {
                spaceXSDK.getLaunches(forceReload)
            }.onSuccess {
                _uiState.update { state -> state.copy(launches = it, isLoading = false) }
            }.onFailure {
                _uiState.update { state -> state.copy(errorMessage = it.localizedMessage, isLoading = false) }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val databaseDriverFactory = (this[APPLICATION_KEY] as SpaceXLaunchesApp).databaseDriverFactory
                val spaceXSdk = SpaceXSDK(databaseDriverFactory)
                MainViewModel(spaceXSdk)
            }
        }
    }
}