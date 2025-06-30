package com.example.stockproject.ui.detailScreen


import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stockproject.data.WishlistEntity
import com.example.stockproject.data.WishlistStockEntity
import com.example.stockproject.data.reponseModel.CompanyOverview
import com.example.stockproject.domain.uiResult
import com.example.stockproject.ui.wishlistDetailScreen.WishlistDetailViewModel
import com.example.stockproject.ui.wishlistListScreen.WishlistListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockDetailScreen(
    symbol: String,
    viewModel: DetailViewModel = hiltViewModel(),
    wishlistViewModel: WishlistListViewModel = hiltViewModel(),
    wishlistDetailViewModel: WishlistDetailViewModel = hiltViewModel(),
    onBack: () -> Boolean
) {
    val companyOverview by viewModel.details.collectAsState()
    val wishlists by wishlistViewModel.wishlists.collectAsState()
    val chartState by viewModel.chartState.collectAsState()
    val selectedRange by viewModel.selectedRange.collectAsState()
    var showWishlistBottomSheet by remember { mutableStateOf(false) }
    var selectedWishlists by remember { mutableStateOf(setOf<Long>()) }

    LaunchedEffect(symbol) { viewModel.loadDetails(symbol) }
    LaunchedEffect(symbol, selectedRange) {
        viewModel.loadChartData(symbol, selectedRange)
    }

    // Check which wishlists already contain this stock
    LaunchedEffect(wishlists, symbol) {
        val wishlistIds = mutableSetOf<Long>()
        wishlists.forEach { wishlist ->
            val stocks = wishlistDetailViewModel.getStocks(wishlist.id).value
            if (stocks.any { it.symbol == symbol }) {
                wishlistIds.add(wishlist.id)
            }
        }
        selectedWishlists = wishlistIds
    }

    val allStocksPerWishlist = wishlists.map { wishlist ->
        wishlistDetailViewModel.getStocks(wishlist.id).collectAsState(initial = emptyList()).value
    }

    val isInAnyWishlist = remember(allStocksPerWishlist) {
        allStocksPerWishlist.flatten().any { it.symbol == symbol }
    }

val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        symbol,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (isInAnyWishlist) {
                                wishlists.forEach { wishlist ->
                                    wishlistDetailViewModel.removeStockFromWishlist(symbol, wishlist.id)
                                }
                                Toast.makeText(context, "stock removed from wishlist", Toast.LENGTH_SHORT).show()
                            } else {
                                showWishlistBottomSheet = true
                            }
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))

                    ) {
                        Icon(
                            imageVector = if (isInAnyWishlist) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Manage Wishlists",
                            tint = if (isInAnyWishlist)
                               MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        when (companyOverview) {
            is uiResult.Loading -> CenterLoadingIndicator()
            is uiResult.Success -> {
                if ((companyOverview as uiResult.Success<CompanyOverview>).data.name.isNullOrEmpty()){
                    ErrorMessage(
                        message = "Stock details not available",
                        modifier = Modifier.padding(innerPadding)
                    )
                    return@Scaffold
                }
                DetailContent(
                    company = (companyOverview as uiResult.Success<CompanyOverview>).data,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            is uiResult.Error -> ErrorMessage(
                message = (companyOverview as uiResult.Error).message,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    if (showWishlistBottomSheet) {
        WishlistBottomSheet(
            wishlists = wishlists,
            selectedWishlists = selectedWishlists,
            onWishlistSelectionChanged = { wishlistId, isSelected ->
                if (isSelected) {
                    // Single selection: clear all others and select only this one
                    selectedWishlists = setOf(wishlistId)
                } else {
                    // Deselect this wishlist
                    selectedWishlists = selectedWishlists - wishlistId
                }
            },
            onConfirm = {
                // Add/remove stock from wishlists
                wishlists.forEach { wishlist ->
                    val isCurrentlySelected = selectedWishlists.contains(wishlist.id)
                    val stocks = wishlistDetailViewModel.getStocks(wishlist.id).value
                    val isAlreadyInWishlist = stocks.any { it.symbol == symbol }

                    if (isCurrentlySelected && !isAlreadyInWishlist) {
                        // Add to wishlist
                        Toast.makeText(context, "stock added to ${wishlist.name}", Toast.LENGTH_SHORT).show()

                        val stock = WishlistStockEntity(
                            symbol = symbol,
                            name = (companyOverview as? uiResult.Success<CompanyOverview>)?.data?.name ?: symbol,
                            exchange = (companyOverview as? uiResult.Success<CompanyOverview>)?.data?.exchange ?: "",
                            currency = (companyOverview as? uiResult.Success<CompanyOverview>)?.data?.currency ?: "",
                            wishlistId = wishlist.id
                        )
                        wishlistDetailViewModel.addStockToWishlist(stock)
                    }
                }
                showWishlistBottomSheet = false
            },
            onDismiss = { showWishlistBottomSheet = false }
        )
    }
}

@Composable
fun DetailContent(
    company: CompanyOverview,
    modifier: Modifier = Modifier
) {
    val viewModel: DetailViewModel = hiltViewModel()
    val chartState by viewModel.chartState.collectAsState()
    val selectedRange by viewModel.selectedRange.collectAsState()
    val symbol = company.symbol
    // Load chart data for selected range
    LaunchedEffect(symbol, selectedRange) {
        viewModel.loadChartData(symbol, selectedRange)
    }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            StockHeader(
                company = company,
                chartState = chartState,
                selectedRange = selectedRange,
                onRangeSelected = { range ->
                    viewModel.setSelectedRange(range)
                }
            )
            Spacer(Modifier.height(24.dp))
        }

        item {
            // Performance Section
            PerformanceSection(company)
            Spacer(Modifier.height(24.dp))
        }

        item {
            // Fundamentals Section
            FundamentalsSection(company)
            Spacer(Modifier.height(24.dp))
        }

        item {
            // Company Info Section
            CompanyInfoSection(company)
        }
    }
}

@Composable
fun StockHeader(
    company: CompanyOverview,
    chartState: ChartUiState,
    selectedRange: ChartRange,
    onRangeSelected: (ChartRange) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {


            Text(
                text = company.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF0D7911)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${company.exchange} · ${company.currency}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = "₹2,104.90",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF0D7911)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "+1.05% (₹21.50)",
                    color = Color(0xFF0D7911),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(20.dp))
            // Chart area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                when {
                    chartState.isLoading -> {
                        CircularProgressIndicator()
                    }
                    chartState.error != null -> {
                        Text(
                            text = chartState.error ?: "Error",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    chartState.data.isNotEmpty() -> {
                        MinimalLineChart(
                            data = chartState.data,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp)
                        )
                    }
                    else -> {
                        Text(
                            text = "No data",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            // Range selector

            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val ranges = listOf(
                    ChartRange.OneDay to "1D",
                    ChartRange.OneWeek to "1W",
                    ChartRange.OneMonth to "1M",
                    ChartRange.OneYear to "1Y"
                )
                ranges.forEach { (range, label) ->
                    val selected = selectedRange == range
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) Color(0xFF22C55E) else Color(0xFFF6F6F6))
                            .clickable { onRangeSelected(range) }
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = label,
                            color = if (selected) Color.White else Color.Black,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }


        }
    }
}

@Composable
fun MinimalLineChart(
    data: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFF22C55E), // green
    backgroundColor: Color = Color(0xFFEFFCF4) // light green
) {
    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(24.dp))
            .padding(12.dp)
    ) {
        if (data.isNotEmpty()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val maxY = data.maxOrNull() ?: 1f
                val minY = data.minOrNull() ?: 0f
                val rangeY = maxY - minY
                val stepX = size.width / (data.size - 1).coerceAtLeast(1)
                val points = data.mapIndexed { i, value ->
                    Offset(
                        x = i * stepX,
                        y = size.height - ((value - minY) / (rangeY.takeIf { it > 0 } ?: 1f) * size.height)
                    )
                }
                val path = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    for (point in points.drop(1)) {
                        lineTo(point.x, point.y)
                    }
                }
                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(width = 4f, cap = StrokeCap.Round)
                )
            }
            // Y-axis labels (min and max)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 4.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text("%.2f".format(data.maxOrNull() ?: 0f), fontSize = 12.sp, color = Color.Gray)
                Text("%.2f".format(data.minOrNull() ?: 0f), fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun PerformanceSection(company: CompanyOverview) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Performance",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(Modifier.height(20.dp))
            
            // Two-column layout for performance metrics
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.height(240.dp)
            ) {
                item { MetricItem("Today's Low", "₹2,023.10", Color.Red) }
                item { MetricItem("Today's High", "₹2,126.80", Color(0xFF0D7911)) }
                item { MetricItem("52 Week Low", company.weekLow52, Color.Red) }
                item { MetricItem("52 Week High", company.weekHigh52, Color(0xFF0D7911)) }
                item { MetricItem("Open", "₹2,104.90", MaterialTheme.colorScheme.onSurface) }
                item { MetricItem("Prev. Close", "₹2,091.20", MaterialTheme.colorScheme.onSurface) }
                item { MetricItem("Volume", "27,45,762", MaterialTheme.colorScheme.onSurface) }
                item { MetricItem("Lower circuit", "₹1,635.40", Color.Red) }
                item { MetricItem("Upper circuit", "₹2,453.00", Color(0xFF0D7911)) }
            }
        }
    }
}

@Composable
fun FundamentalsSection(company: CompanyOverview) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Fundamentals",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(Modifier.height(20.dp))
            
            FundamentalsList(company)
        }
    }
}

@Composable
fun FundamentalsList(company: CompanyOverview) {
    val fundamentals = listOf(
        Triple("Mkt Cap", company.marketCap, MaterialTheme.colorScheme.onSurface),
        Triple("ROE", company.returnOnEquityTTM, Color(0xFF0D7911)),
        Triple("P/E Ratio(TTM)", company.peRatio, MaterialTheme.colorScheme.onSurface),
        Triple("EPS(TTM)", company.eps, Color(0xFF0D7911)),
        Triple("P/B Ratio", company.priceToBookRatio, MaterialTheme.colorScheme.onSurface),
        Triple("Div Yield", company.dividendYield, Color(0xFF0D7911)),
        Triple("Industry P/E", "54.66", MaterialTheme.colorScheme.onSurface),
        Triple("Book Value", company.bookValue, MaterialTheme.colorScheme.onSurface),
        Triple("Debt to Equity", "0.10", Color(0xFF0D7911)),
        Triple("Face Value", "₹5", MaterialTheme.colorScheme.onSurface)
    )
    
    Column {
        fundamentals.forEach { (label, value, color) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End,
                    color = color,
                    fontWeight = FontWeight.Medium
                )
            }
            
            if (fundamentals.indexOf(Triple(label, value, color)) < fundamentals.size - 1) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun CompanyInfoSection(company: CompanyOverview) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Company Information",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(Modifier.height(20.dp))
            
            InfoRow("Sector", company.sector)
            InfoRow("Industry", company.industry)
            InfoRow("Country", company.country)
            InfoRow("Fiscal Year End", company.fiscalYearEnd)
            InfoRow("Latest Quarter", company.latestQuarter)
            InfoRow("Website", company.officialSite ?: "N/A")
            
            Spacer(Modifier.height(20.dp))
            
            Text(
                text = "Description",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(Modifier.height(12.dp))
            
            Text(
                text = company.description,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = TextUnit.Unspecified
            )
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
            maxLines = 2
        )
    }
    
    Divider(
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
fun MetricItem(label: String, value: String, valueColor: Color) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = valueColor
        )
    }
}

@Composable
fun CenterLoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading stock details...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun ErrorMessage(message: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingDown,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Error",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistBottomSheet(
    wishlists: List<WishlistEntity>,
    selectedWishlists: Set<Long>,
    onWishlistSelectionChanged: (Long, Boolean) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp)
        ) {
            Text(
                text = "Add to Wishlists",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            if (wishlists.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No wishlists available",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Create a wishlist first",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.wrapContentHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item{
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    items(wishlists) { wishlist ->
                        WishlistItem(
                            wishlist = wishlist,
                            isSelected = selectedWishlists.contains(wishlist.id),
                            onSelectionChanged = { isSelected ->
                                onWishlistSelectionChanged(wishlist.id, isSelected)
                            }
                        )
                    }
                    item{
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.End),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Save")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun WishlistItem(
    wishlist: WishlistEntity,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectionChanged(!isSelected) },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = onSelectionChanged,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = wishlist.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
        }
    }
}