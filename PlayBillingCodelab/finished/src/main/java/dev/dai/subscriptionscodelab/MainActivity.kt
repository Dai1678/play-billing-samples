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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.compose.rememberNavController
import dev.dai.subscriptionscodelab.Constants.MONTHLY_BASIC_PLANS_TAG
import dev.dai.subscriptionscodelab.Constants.MONTHLY_PREMIUM_PLANS_TAG
import dev.dai.subscriptionscodelab.Constants.PREPAID_BASIC_PLANS_TAG
import dev.dai.subscriptionscodelab.Constants.PREPAID_PREMIUM_PLANS_TAG
import dev.dai.subscriptionscodelab.Constants.YEARLY_BASIC_PLANS_TAG
import dev.dai.subscriptionscodelab.Constants.YEARLY_PREMIUM_PLANS_TAG
import dev.dai.subscriptionscodelab.ui.ButtonModel
import dev.dai.subscriptionscodelab.ui.MainState
import dev.dai.subscriptionscodelab.ui.MainViewModel
import dev.dai.subscriptionscodelab.ui.composable.LoadingScreen
import dev.dai.subscriptionscodelab.ui.composable.SubscriptionNavigationComponent
import dev.dai.subscriptionscodelab.ui.composable.UserProfile
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
    // connections is established.
    val isBillingConnected by viewModel.billingConnectionState.observeAsState()

    if (isBillingConnected == false) {
        // When false connection to the billing library is not established yet,
        // so a loading screen is rendered.
        LoadingScreen()
    } else {
        // When true connection to the billing library is established,
        // so the subscription composables are rendered.
        val navController = rememberNavController()

        // Collect available products to sale Flows from MainViewModel.
        val productsForSale by viewModel.productsForSaleFlows.collectAsState(
            initial = MainState()
        )

        // Collect current purchases Flows from MainViewModel.
        val currentPurchases by viewModel.currentPurchasesFlow.collectAsState(
            initial = listOf()
        )

        // Observe the ViewModel's destinationScreen object for changes in subscription status.
        val screen by viewModel.destinationScreen.observeAsState()

        // Load UI based on user's current subscription.
        when (screen) {
            // User has a Basic Prepaid subscription
            // the corresponding profile is loaded.
            MainViewModel.DestinationScreen.BASIC_PREPAID_PROFILE_SCREEN -> {
                UserProfile(
                    buttonModels =
                    listOf(
                        ButtonModel(R.string.topup_message) {
                            productsForSale.basicProductDetails?.let {
                                viewModel.buy(
                                    productDetails = it,
                                    currentPurchases = null,
                                    tag = PREPAID_BASIC_PLANS_TAG,
                                    activity = activity
                                )
                            }
                        },
                        ButtonModel(R.string.convert_to_basic_monthly_message) {
                            productsForSale.basicProductDetails?.let {
                                viewModel.buy(
                                    productDetails = it,
                                    currentPurchases = currentPurchases,
                                    tag = MONTHLY_BASIC_PLANS_TAG,
                                    activity = activity
                                )
                            }
                        },
                        ButtonModel(R.string.convert_to_basic_yearly_message) {
                            productsForSale.basicProductDetails?.let {
                                viewModel.buy(
                                    productDetails = it,
                                    currentPurchases = currentPurchases,
                                    tag = YEARLY_BASIC_PLANS_TAG,
                                    activity = activity
                                )
                            }
                        },
                    ),
                    tag = PREPAID_BASIC_PLANS_TAG,
                    profileTextStringResource = null
                )
            }
            // User has a renewable basic subscription
            // the corresponding profile is loaded.
            MainViewModel.DestinationScreen.BASIC_RENEWABLE_PROFILE -> {
                UserProfile(
                    buttonModels =
                    listOf(
                        ButtonModel(R.string.monthly_premium_upgrade_message) {
                            productsForSale.premiumProductDetails?.let {
                                viewModel.buy(
                                    productDetails = it,
                                    currentPurchases = currentPurchases,
                                    tag = MONTHLY_PREMIUM_PLANS_TAG,
                                    activity = activity
                                )
                            }
                        },
                        ButtonModel(R.string.yearly_premium_upgrade_message) {
                            productsForSale.premiumProductDetails?.let {
                                viewModel.buy(
                                    productDetails = it,
                                    currentPurchases = currentPurchases,
                                    tag = YEARLY_PREMIUM_PLANS_TAG,
                                    activity = activity
                                )
                            }
                        },
                        ButtonModel(R.string.prepaid_premium_upgrade_message) {
                            productsForSale.premiumProductDetails?.let {
                                viewModel.buy(
                                    productDetails = it,
                                    currentPurchases = currentPurchases,
                                    tag = PREPAID_PREMIUM_PLANS_TAG,
                                    activity = activity
                                )
                            }
                        }
                    ),
                    tag = null,
                    profileTextStringResource = R.string.basic_sub_message
                )
            }
            // User has a prepaid Premium subscription
            // the corresponding profile is loaded.
            MainViewModel.DestinationScreen.PREMIUM_PREPAID_PROFILE_SCREEN -> {
                UserProfile(
                    buttonModels =
                    listOf(
                        ButtonModel(R.string.topup_message) {
                            productsForSale.premiumProductDetails?.let {
                                viewModel.buy(
                                    productDetails = it,
                                    currentPurchases = null,
                                    tag = PREPAID_PREMIUM_PLANS_TAG,
                                    activity = activity
                                )
                            }
                        },
                        ButtonModel(R.string.convert_to_premium_monthly_message) {
                            productsForSale.premiumProductDetails?.let {
                                viewModel.buy(
                                    productDetails = it,
                                    currentPurchases = currentPurchases,
                                    tag = MONTHLY_PREMIUM_PLANS_TAG,
                                    activity = activity
                                )
                            }
                        },
                        ButtonModel(R.string.convert_to_premium_yearly_message) {
                            productsForSale.premiumProductDetails?.let {
                                viewModel.buy(
                                    productDetails = it,
                                    currentPurchases = currentPurchases,
                                    tag = YEARLY_PREMIUM_PLANS_TAG,
                                    activity = activity
                                )
                            }
                        },
                    ),
                    tag = PREPAID_PREMIUM_PLANS_TAG,
                    profileTextStringResource = null
                )
            }
            // User has a renewable Premium subscription
            // the corresponding profile is loaded.
            MainViewModel.DestinationScreen.PREMIUM_RENEWABLE_PROFILE -> {
                UserProfile(
                    listOf(
                        ButtonModel(R.string.monthly_basic_downgrade_message) {
                            productsForSale.basicProductDetails?.let {
                                viewModel.buy(
                                    productDetails = it,
                                    currentPurchases = currentPurchases,
                                    tag = MONTHLY_BASIC_PLANS_TAG,
                                    activity = activity
                                )
                            }
                        },
                        ButtonModel(R.string.yearly_basic_downgrade_message) {
                            productsForSale.basicProductDetails?.let {
                                viewModel.buy(
                                    productDetails = it,
                                    currentPurchases = currentPurchases,
                                    tag = YEARLY_BASIC_PLANS_TAG,
                                    activity = activity
                                )
                            }
                        },
                        ButtonModel(R.string.prepaid_basic_downgrade_message) {
                            productsForSale.basicProductDetails?.let {
                                viewModel.buy(
                                    productDetails = it,
                                    currentPurchases = currentPurchases,
                                    tag = PREPAID_BASIC_PLANS_TAG,
                                    activity = activity
                                )
                            }
                        }
                    ),
                    tag = null,
                    profileTextStringResource = R.string.premium_sub_message
                )
            }
            // User has no current subscription - the subscription composable
            // is loaded.
            MainViewModel.DestinationScreen.SUBSCRIPTIONS_OPTIONS_SCREEN -> {
                SubscriptionNavigationComponent(
                    productsForSale = productsForSale,
                    navController = navController,
                    viewModel = viewModel
                )
            }

            else -> Unit
        }
    }
}
