package com.example.sudokusolver.sudoku.presenetation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.sudokusolver.sudoku.presenetation.view.DrawerAvatarButton
import com.example.sudokusolver.sudoku.presenetation.view.UserDrawerContent
import com.example.sudokusolver.navigation.Screen
import com.example.sudokusolver.rememberPlatformCameraLauncher
import com.example.sudokusolver.showSplash
import com.example.sudokusolver.sudoku.presenetation.event.SudokuEvent
import com.example.sudokusolver.sudoku.presenetation.state.SudokuState
import com.example.sudokusolver.sudoku.presenetation.view.ControlsPanel
import com.example.sudokusolver.sudoku.presenetation.view.EmptyState
import com.example.sudokusolver.sudoku.presenetation.view.MediaButtons
import com.example.sudokusolver.sudoku.presenetation.view.SudokuBoard
import com.example.sudokusolver.sudoku.presenetation.viewmodel.SudokuViewModel
import org.jetbrains.compose.resources.stringResource
import sudokusolver.shared.generated.resources.Res
import sudokusolver.shared.generated.resources.sudoku_solver
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.util.toImageBitmap
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

// ───────────────────────────── Entry point ─────────────────────────────

@Composable
fun SudokuScreen(
    modifier: Modifier = Modifier,
    viewModel: SudokuViewModel = koinViewModel(),
    navBackStack: NavBackStack<NavKey>,
    email: String,
    isGuest: Boolean
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val colors = MaterialTheme.colorScheme
    val backdrop = Brush.verticalGradient(
        listOf(colors.primaryContainer.copy(alpha = 0.45f), colors.background, colors.background)
    )
    LaunchedEffect(Unit) {
        if (!isGuest) {
            navBackStack.remove(Screen.Login)
        }
        viewModel.loadUserInfo(isGuest)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SudokuEvent.SignOut -> {
                    navBackStack.removeLastOrNull()
                    navBackStack.add(Screen.Login)
                }
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackBarHostState.showSnackbar(it)
            viewModel.onErrorShowed()
        }
    }


    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val galleryLauncher = rememberFilePickerLauncher(type = FileKitType.Image) { file ->
        if (file != null) scope.launch {
            viewModel.updateProfilePhoto(file.readBytes())
        }
    }
    val cameraLauncher = rememberPlatformCameraLauncher { file ->
        if (file != null) scope.launch { viewModel.updateProfilePhoto(file.readBytes()) }
    }
    ModalNavigationDrawer(
        drawerState = drawerState, drawerContent = {
            UserDrawerContent(
                user = state.userInfo,
                isGuest = isGuest,
                editEnabled = !state.isLoading,
                onUploadPhotoFromGallery = { scope.launch { drawerState.close() }; galleryLauncher.launch() },
                onUploadPhotoFromCamera = { scope.launch { drawerState.close() }; cameraLauncher() },
                onSignOut = viewModel::signOut
            )
        }) {
        Scaffold(
            modifier = modifier.fillMaxSize().background(backdrop),
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(snackBarHostState) }) { padding ->
            SudokuScreenContent(
                state = state,
                modifier = Modifier.fillMaxSize().padding(padding),
                onUploadGridBitmap = viewModel::getGridFromMobile,
                onUploadGridBytes = viewModel::getGridFromWeb,
                onSelectCell = viewModel::selectCell,
                onAddNumber = viewModel::addAction,
                onUndo = viewModel::undoAction,
                onErase = viewModel::eraseAction,
                onHint = viewModel::requestHint,
                onSolve = viewModel::solve,
                isGuest = isGuest,
                onOpenDrawer = { scope.launch { drawerState.open() } })
        }
    }
}


@Composable
private fun SudokuScreenContent(
    state: SudokuState,
    modifier: Modifier = Modifier,
    onUploadGridBitmap: (ImageBitmap) -> Unit,
    onUploadGridBytes: (ByteArray) -> Unit,
    onSelectCell: (Int, Int) -> Unit,
    onAddNumber: (Int) -> Unit,
    onUndo: () -> Unit,
    onErase: () -> Unit,
    onHint: () -> Unit,
    onSolve: () -> Unit,
    isGuest: Boolean,
    onOpenDrawer: () -> Unit
) {
    val grid = state.sudokuGrid
    val scope = rememberCoroutineScope()

    val galleryLauncher = rememberFilePickerLauncher(type = FileKitType.Image) { file ->
        if (file != null) scope.launch {
            if (showSplash) {
                onUploadGridBitmap(file.toImageBitmap())
            } else {
                onUploadGridBytes(file.readBytes())
            }
        }

    }

    val cameraLauncher = rememberPlatformCameraLauncher { file ->
        if (file != null) scope.launch {
            if (showSplash) {
                onUploadGridBitmap(file.toImageBitmap())
            } else {
                onUploadGridBytes(file.readBytes())
            }
        }

    }

    val openGallery: () -> Unit = { galleryLauncher.launch() }
    val openCamera: () -> Unit = { cameraLauncher() }
    val enabled = !state.isLoading

    BoxWithConstraints(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        val wide = maxWidth >= 700.dp || (maxWidth >= 520.dp && maxWidth > maxHeight * 1.3f)
        if (grid == null) {
            DrawerAvatarButton(
                state.userInfo, isGuest, onOpenDrawer, Modifier.align(Alignment.TopStart)
            )
        }
        when {
            grid == null -> EmptyState(
                isLoading = state.isLoading,
                modifier = Modifier.widthIn(max = 1100.dp).fillMaxSize(),
                onGallery = openGallery,
                onCamera = openCamera
            )

            wide -> Row(
                modifier = Modifier.widthIn(max = 1100.dp).fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                BoxWithConstraints(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    SudokuBoard(
                        grid = grid,
                        state = state,
                        modifier = Modifier.size(minOf(maxWidth, maxHeight)),
                        onSelectCell = onSelectCell
                    )
                }
                Surface(
                    modifier = Modifier.width(300.dp),
                    shape = RoundedCornerShape(28.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 3.dp,
                    shadowElevation = 6.dp
                ) {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // wide panel: replace the title Text with
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            DrawerAvatarButton(state.userInfo, isGuest, onOpenDrawer)
                            Text(
                                stringResource(Res.string.sudoku_solver),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        MediaButtons(
                            enabled = enabled,
                            stacked = true,
                            compact = true,
                            modifier = Modifier.fillMaxWidth(),
                            onGallery = openGallery,
                            onCamera = openCamera
                        )
                        ControlsPanel(
                            state = state,
                            twoByTwo = true,
                            onAddNumber = onAddNumber,
                            onUndo = onUndo,
                            onErase = onErase,
                            onHint = onHint,
                            onSolve = onSolve
                        )
                    }
                }
            }

            else -> Column(
                modifier = Modifier.widthIn(max = 1100.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DrawerAvatarButton(state.userInfo, isGuest, onOpenDrawer)
                    Text(
                        stringResource(Res.string.sudoku_solver),
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    MediaButtons(
                        enabled = enabled,
                        compact = true,
                        onGallery = openGallery,
                        onCamera = openCamera
                    )
                }
                Spacer(Modifier.height(12.dp))
                BoxWithConstraints(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    SudokuBoard(
                        grid = grid,
                        state = state,
                        modifier = Modifier.size(minOf(maxWidth, maxHeight)),
                        onSelectCell = onSelectCell
                    )
                }
                Spacer(Modifier.height(12.dp))
                ControlsPanel(
                    state = state,
                    twoByTwo = false,
                    onAddNumber = onAddNumber,
                    onUndo = onUndo,
                    onErase = onErase,
                    onHint = onHint,
                    onSolve = onSolve
                )
            }
        }
    }
}


