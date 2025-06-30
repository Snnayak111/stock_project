package com.example.stockproject.ui.exploreScreen


import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.stockproject.data.reponseModel.TopStocks
import com.example.stockproject.domain.uiResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    topGainers: uiResult<List<TopStocks>>,
    topLosers: uiResult<List<TopStocks>>,
    onSearchClicked: () -> Unit,
    onViewAllClicked: (Boolean) -> Unit,
    onDetailClicked: (String) -> Unit,
    onWatchListClicked: () -> Unit,
) {
    var showGainers by remember { mutableStateOf(true) }
    
    Scaffold(
        topBar = {
            SearchTopBar(onSearchClicked = onSearchClicked)
        },
        bottomBar = {
            CustomBottomAppBar(
                onHomeClicked = { showGainers = true },
                onWatchListClicked = onWatchListClicked,
                isHomeSelected = showGainers
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),

        ) {

            StockSection(
                title = "Top Gainers",
                icon = Icons.Default.TrendingUp,
                state = topGainers,
                onViewAllClicked = { onViewAllClicked(true) },
                onDetailClicked = onDetailClicked,
                onWatchListClicked = onWatchListClicked
            )




            Spacer(modifier = Modifier.height(24.dp))



            StockSection(
                title = "Top Losers",
                icon = Icons.Default.TrendingDown,
                state = topLosers,
                onViewAllClicked = { onViewAllClicked(false) },
                onDetailClicked = onDetailClicked,
                onWatchListClicked = onWatchListClicked
            )


        }
    }
}

@Composable
fun MarketOverviewCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Market Overview",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MarketStat(
                    label = "S&P 500",
                    value = "+1.2%",
                    isPositive = true
                )
                MarketStat(
                    label = "NASDAQ",
                    value = "+0.8%",
                    isPositive = true
                )
                MarketStat(
                    label = "DOW",
                    value = "-0.3%",
                    isPositive = false
                )
            }
        }
    }
}

@Composable
fun MarketStat(
    label: String,
    value: String,
    isPositive: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (isPositive) Color(0xFF0D7911) else Color.Red
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomAppBar(
    onHomeClicked: () -> Unit,
    onWatchListClicked: () -> Unit,
    isHomeSelected: Boolean
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.height(80.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarItem(
                icon = Icons.Default.Home,
                label = "Home",
                isSelected = isHomeSelected,
                onClick = onHomeClicked
            )
            
            BottomBarItem(
                icon = Icons.Default.Star,
                label = "Wishlist",
                isSelected = !isHomeSelected,
                onClick = onWatchListClicked
            )
        }
    }
}

@Composable
fun BottomBarItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(onSearchClicked: () -> Unit) {
    TopAppBar(
        title = { 
            Text(
                "Stock Explorer",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(
                onClick = onSearchClicked,
                modifier = Modifier
                    .padding(5.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        colors = androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

val isViewAllEnabled = mutableStateOf(true)
@Composable
fun StockSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    state: uiResult<List<TopStocks>>,
    onViewAllClicked: () -> Unit,
    onDetailClicked: (String) -> Unit,
    onWatchListClicked: () -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = if (title.contains("Gainers")) Color(0xFF0D7911) else Color.Red
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            TextButton(
                onClick = {
                    if (isViewAllEnabled.value) {
                        onViewAllClicked()
                    }
                },
                colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("View All")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (state) {
            is uiResult.Loading -> LoadingIndicator()
            is uiResult.Success -> {
                if (state.data.isNullOrEmpty()){
                    isViewAllEnabled.value=false
                    ErrorMessage(message = "No data found. Please try again later. You may have reached the usage limit.")
                }else{
                    isViewAllEnabled.value=true
                    StockList(
                        stocks = state.data,
                        onDetailClicked = onDetailClicked,
                        onWatchListClicked = onWatchListClicked
                    )
                }

            }
            is uiResult.Error -> ErrorMessage(message = state.message)
        }
    }
}

@Composable
fun StockList(
    stocks: List<TopStocks>,
    onDetailClicked: (String) -> Unit,
    onWatchListClicked: () -> Unit,
) {
    LazyVerticalGrid(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
        modifier = Modifier.height(280.dp),
        columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2)
    ) {
        items(stocks.subList(0,4)) { stock ->
            StockCard(
                stock = stock,
                onDetailClicked = { onDetailClicked(stock.ticker) },
                onWatchListClicked = { onWatchListClicked }
            )
        }
    }
}

@Composable
fun StockCard(
    stock: TopStocks,
    onDetailClicked: () -> Unit,
    onWatchListClicked: () -> Unit,
) {
    val isPositive = !stock.change_percentage.contains("-")
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDetailClicked() },
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

                Text(
                    text = stock.ticker,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )


            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$${stock.price}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isPositive) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = if (isPositive) Color(0xFF0D7911) else Color.Red
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stock.change_percentage,
                    color = if (isPositive) Color(0xFF0D7911) else Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))

        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ErrorMessage(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Error: $message",
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}