package com.zfml.bookworm.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zfml.bookworm.R
import com.zfml.bookworm.component.DisplayAlertDialog
import com.zfml.bookworm.component.ProgressBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToAddBookScreen:() -> Unit,
    navigateToAddBookScreenWithArg: (bookId: String) -> Unit,
) {
    val booksUiState by viewModel.booksUiState.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var openSignOutDialog by remember { mutableStateOf(false) }

    NavigationDrawer(
        drawerState = drawerState ,
        userName = "" ,
        userImage = "" ,
        onSignOutClicked = { openSignOutDialog = true }) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Book Worm")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Navigation Drawer Icon"
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = navigateToAddBookScreen) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Icon")
                }
            },
            content = {padding ->

                if(booksUiState.isLoading) {
                    ProgressBar()
                }
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ){
                    if(booksUiState.books.isNotEmpty()) {
                        LazyColumn() {
                            items(booksUiState.books.size) {
                                val book = booksUiState.books[it]
                                BookItem(
                                    book = book,
                                    onClicked = {
                                        navigateToAddBookScreenWithArg(it)
                                    }

                                )
                            }
                        }
                    }
                }
            }

        )
    }

    if(openSignOutDialog) {
        DisplayAlertDialog(
            title = "Sign Out",
            description = "Are you sure you want to sign Out",
            openDialog = openSignOutDialog ,
            onClosedDialog = { openSignOutDialog = false },
            onConfirmClicked = {
                     viewModel.signOut()
                     openSignOutDialog = false
            },
            onDismissClicked = { openSignOutDialog = false }
        )
    }


}

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    userName: String?,
    userImage: String?,
    onSignOutClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = Shapes().small,
                content = {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                            ,
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(64.dp)
                                ,
                                model = userImage,
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text  = userName ?: "",
                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }

                    NavigationDrawerItem(
                        label = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ){
//                                Image(
//                                    painter = painterResource(id = R.drawable.ic_google_logo),
//                                    contentDescription = "Google Logo"
//                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text ="Sign Out"
                                )
                            }
                        },
                        selected = false,
                        onClick = onSignOutClicked)
                }
            )
        },
        content = content
    )

}