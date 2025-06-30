package com.example.stockproject.ui.wishlistDetailScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stockproject.data.WishlistEntity
import com.example.stockproject.ui.wishlistListScreen.WishlistListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    viewModel: WishlistListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onWatchlistStockClicked: (Long, String) -> Unit,
    onBack: () -> Boolean
) {
    val wishlists by viewModel.wishlists.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var newWishlistName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Wishlists") },
                actions = {
                    IconButton(onClick = { showCreateDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Create Wishlist")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {onBack()}) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (wishlists.isEmpty()) {
            EmptyWishlistState(
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(wishlists) { wishlist ->
                    WishlistCard(
                        wishlist = wishlist,
                        onWishlistClicked = { onWatchlistStockClicked(wishlist.id,wishlist.name) },
                        onDeleteClicked = { viewModel.deleteWishlist(wishlist) }
                    )
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateWishlistDialog(
            wishlistName = newWishlistName,
            onWishlistNameChange = { newWishlistName = it },
            onConfirm = {
                if (newWishlistName.isNotBlank()) {
                    viewModel.createWishlist(newWishlistName)
                    newWishlistName = ""
                    showCreateDialog = false
                }
            },
            onDismiss = {
                newWishlistName = ""
                showCreateDialog = false
            }
        )
    }
}

@Composable
fun WishlistCard(
    wishlist: WishlistEntity,
    onWishlistClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onWishlistClicked() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = wishlist.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ID: ${wishlist.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDeleteClicked) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete Wishlist",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = "View Wishlist",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun EmptyWishlistState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Wishlists Yet",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Create your first wishlist to start tracking stocks",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun CreateWishlistDialog(
    wishlistName: String,
    onWishlistNameChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Wishlist") },
        text = {
            OutlinedTextField(
                value = wishlistName,
                onValueChange = onWishlistNameChange,
                label = { Text("Wishlist Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = wishlistName.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}