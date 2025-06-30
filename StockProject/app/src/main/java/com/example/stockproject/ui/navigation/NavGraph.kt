package com.example.stockproject.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stockproject.ui.detailScreen.DetailViewModel
import com.example.stockproject.ui.detailScreen.StockDetailScreen
import com.example.stockproject.ui.exploreScreen.ExploreScreen
import com.example.stockproject.ui.exploreScreen.ExploreViewModel
import com.example.stockproject.ui.searchScreen.SearchScreen
import com.example.stockproject.ui.searchScreen.SearchViewModel
import com.example.stockproject.ui.viewAllScreen.ViewAllScreen
import com.example.stockproject.ui.viewAllScreen.ViewAllViewModel
import com.example.stockproject.ui.wishlistDetailScreen.WishlistDetailViewModel
import com.example.stockproject.ui.wishlistDetailScreen.WishlistScreen
import com.example.stockproject.ui.wishlistListScreen.WishlistDetailScreen
import com.example.stockproject.ui.wishlistListScreen.WishlistListViewModel
import kotlinx.serialization.Serializable

@Composable
fun navGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavRoots.Explore
    ) {

        val onSearchClicked ={
            navController.navigate(NavRoots.Search)
        }
        val onViewAllClicked = {isGainer: Boolean ->
            navController.navigate(NavRoots.ViewAllStock(isGainer))
        }
        val onDetailClicked = {symbol: String ->
            navController.navigate(NavRoots.Detail(symbol))
        }
        val onWatchListClicked = {
            navController.navigate(NavRoots.WatchList)
        }
        val onWatchlistStockClicked = {watchListId: Long, watchListName: String ->
            navController.navigate(NavRoots.WatchListStock(watchListId,watchListName))
        }
        val onBackClicked = {
            navController.popBackStack()
        }
        composable<NavRoots.Explore> {
            val vm: ExploreViewModel = hiltViewModel()
            val topGainers = vm.gainers.collectAsStateWithLifecycle()
            val topLosers = vm.losers.collectAsStateWithLifecycle()
            ExploreScreen(
                topGainers = topGainers.value,
                topLosers = topLosers.value,
                onSearchClicked = onSearchClicked,
                onViewAllClicked = onViewAllClicked,
                onDetailClicked = onDetailClicked,
                onWatchListClicked = onWatchListClicked,
            )
        }
        composable<NavRoots.Search> {
            val vm: SearchViewModel = hiltViewModel()
            SearchScreen(
                viewModel = vm,
                onBackClicked = onBackClicked,
                onDetailClicked = onDetailClicked,
            )
        }
        composable<NavRoots.ViewAllStock> {
            val vm: ViewAllViewModel = hiltViewModel()
            ViewAllScreen(
                isGainer = it.arguments?.getBoolean("isGainer") ?: false,
                viewModel = vm,
                onBack = onBackClicked,
                onDetailClicked = onDetailClicked
            )
        }
        composable<NavRoots.Detail> {
            val vm: DetailViewModel = hiltViewModel()
            val symbol = it.arguments?.getString("symbol") ?: ""
            StockDetailScreen(
                symbol = symbol,
                viewModel = vm,
                onBack = onBackClicked
            )
        }
        composable<NavRoots.WatchList> {
            val vm: WishlistListViewModel = hiltViewModel()
            WishlistScreen(onWatchlistStockClicked=onWatchlistStockClicked, onBack = onBackClicked)
        }
        composable<NavRoots.WatchListStock> {
            val vm: WishlistDetailViewModel = hiltViewModel()
            val watchListId = it.arguments?.getLong("watchListId") ?: 0L
            val watchListName = it.arguments?.getString("watchListName") ?: ""
            WishlistDetailScreen(
                wishlistId = watchListId,
                wishlistName = watchListName,
                viewModel = vm,
                onBackClicked = onBackClicked,
                onStockClicked = onDetailClicked
            )
        }
    }
}

@Serializable
sealed class NavRoots {
    @Serializable
    object Explore : NavRoots()
    @Serializable
    object Search : NavRoots()
    @Serializable
    data class Detail(val symbol: String) : NavRoots()
    @Serializable
    object WatchList : NavRoots()
    @Serializable
    data class WatchListStock(val watchListId: Long,val watchListName: String) : NavRoots()
    @Serializable
    data class ViewAllStock(val isGainer: Boolean) : NavRoots()
}