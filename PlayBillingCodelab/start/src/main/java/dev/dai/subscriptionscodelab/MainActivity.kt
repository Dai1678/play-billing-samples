/*
 * Copyright 2022 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.dai.subscriptionscodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import dev.dai.subscriptionscodelab.ui.MainViewModel
import dev.dai.subscriptionscodelab.ui.composable.LoadingScreen
import dev.dai.subscriptionscodelab.ui.theme.BasicsCodelabTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BasicsCodelabTheme {
                MainNavHost(viewModel = viewModel, activity = this)
            }
        }
    }
}

@Composable
private fun MainNavHost(viewModel: MainViewModel, activity: MainActivity) {
    // State variable passed into Billing connection call and set to true when
    val isBillingConnected =  TODO()

    // connections is established.

    if (isBillingConnected == false) {
        // When false connection to the billing library is not established yet,
        // so a loading screen is rendered.
        LoadingScreen()
    } else {
        // When true connection to the billing library is established,
        // so the subscription composables are rendered.

        // Collect available products to sale Flows from MainViewModel.


        // Collect current purchases Flows from MainViewModel.

        // Observe the ViewModel's destinationScreen object for changes in subscription status.

        // Load UI based on user's current subscription.

    }
}
