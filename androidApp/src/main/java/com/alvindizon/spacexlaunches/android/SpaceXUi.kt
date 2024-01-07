package com.alvindizon.spacexlaunches.android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvindizon.spacexlaunches.shared.entity.Links
import com.alvindizon.spacexlaunches.shared.entity.RocketLaunch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SpaceXLaunchesListScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val pullRefreshState =
        rememberPullRefreshState(refreshing = state.isLoading, onRefresh = viewModel::onPull)
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.app_title)) }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .pullRefresh(pullRefreshState),
            contentAlignment = Alignment.TopCenter
        ) {
            SpaceXLaunchesListUi(modifier = Modifier.fillMaxSize().padding(16.dp), launches = state.launches)
            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = state.isLoading,
                state = pullRefreshState
            )
        }

    }
}

@Composable
private fun SpaceXLaunchesListUi(modifier: Modifier = Modifier, launches: List<RocketLaunch>?) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        launches?.let {
            items(items = launches, key = { it.hashCode() }) {
                SpaceXLaunchCard(
                    modifier = Modifier.fillMaxWidth(),
                    launchName = it.missionName,
                    isSuccessful = it.launchSuccess ?: false,
                    launchYear = it.launchYear,
                    launchDetails = it.details.orEmpty()
                )
            }
        }
    }
}

@Composable
private fun SpaceXLaunchCard(
    modifier: Modifier = Modifier,
    launchName: String,
    isSuccessful: Boolean,
    launchYear: Int,
    launchDetails: String
) {
    val (statusColor, statusMessage) = if (isSuccessful) {
        Color(0xFF4BB543) to R.string.launch_status_successful
    } else {
        Color(0xFFFC100D) to R.string.launch_status_unsuccessful
    }
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(stringResource(id = R.string.launch_name_label, launchName))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(id = statusMessage), color = statusColor)
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(id = R.string.launch_year_label, launchYear))
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(id = R.string.launch_details_label, launchDetails))
        }
    }
}

@Preview
@Composable
fun SpaceXLaunchCardPreview() {
    SpaceXLaunchesTheme {
        SpaceXLaunchCard(
            launchName = "DemoSat",
            isSuccessful = true,
            launchYear = 2007,
            launchDetails = "Successful first stage burn and transition to second stage, maximum altitude 289 km, premature engine shutdown at T+7 min 30 s, Failed to reach orbit, Failed to recover first stage"
        )
    }
}

@Preview
@Composable
fun SpaceXLaunchesListUiPreview() {
    val launches = listOf(
        RocketLaunch(
            flightNumber = 1,
            details = "Successful first stage burn and transition to second stage, maximum altitude 289 km, premature engine shutdown at T+7 min 30 s, Failed to reach orbit, Failed to recover first stage",
            launchDateUTC = "2023-08-24T10:30:49Z",
            launchSuccess = true,
            links = Links(patch = null, article = null),
            missionName = "Demosat"
        ),
        RocketLaunch(
            flightNumber = 2,
            details = "Successful first stage burn and transition to second stage, maximum altitude 289 km, premature engine shutdown at T+7 min 30 s, Failed to reach orbit, Failed to recover first stage",
            launchDateUTC = "2023-08-24T10:30:49Z",
            launchSuccess = true,
            links = Links(patch = null, article = null),
            missionName = "Demosat"
        ),
        RocketLaunch(
            flightNumber = 3,
            details = "Successful first stage burn and transition to second stage, maximum altitude 289 km, premature engine shutdown at T+7 min 30 s, Failed to reach orbit, Failed to recover first stage",
            launchDateUTC = "2023-08-24T10:30:49Z",
            launchSuccess = false,
            links = Links(patch = null, article = null),
            missionName = "Demosat"
        ),
        RocketLaunch(
            flightNumber = 4,
            details = "Successful first stage burn and transition to second stage, maximum altitude 289 km, premature engine shutdown at T+7 min 30 s, Failed to reach orbit, Failed to recover first stage",
            launchDateUTC = "2023-08-24T10:30:49Z",
            launchSuccess = true,
            links = Links(patch = null, article = null),
            missionName = "Demosat"
        )
    )
    SpaceXLaunchesListUi(launches = launches)
}
