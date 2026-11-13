package com.example.sudokusolver.sudoku.presenetation.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.example.sudokusolver.showSplash
import com.example.sudokusolver.splash.data.remote.dto.UserDto
import org.jetbrains.compose.resources.stringResource
import sudokusolver.shared.generated.resources.Res
import sudokusolver.shared.generated.resources.choose_from_gallery
import sudokusolver.shared.generated.resources.guest
import sudokusolver.shared.generated.resources.loading
import sudokusolver.shared.generated.resources.open_profile_menu
import sudokusolver.shared.generated.resources.profile_photo
import sudokusolver.shared.generated.resources.sign_in_to_add_profile_photo
import sudokusolver.shared.generated.resources.sign_out
import sudokusolver.shared.generated.resources.take_a_photo
import sudokusolver.shared.generated.resources.upload_photo

// ───────────────────────────── Avatar (reusable) ─────────────────────────────

/**
 * Circular profile picture.
 * - Guest or no/blank [imageUrl]: draws the fallback directly (no network call at all).
 * - Otherwise loads with Coil; the same fallback shows while loading and if the download fails.
 */
@Composable
fun UserAvatar(
    imageUrl: String?,
    name: String,
    isGuest: Boolean,
    size: Dp,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val profilePhotoDescription = stringResource(Res.string.profile_photo)
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(colors.primaryContainer)
            .border(2.dp, colors.primary.copy(alpha = 0.4f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (isGuest || imageUrl.isNullOrBlank()) {
            AvatarFallback(name = name, isGuest = isGuest, size = size)
        } else {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = profilePhotoDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loading = { AvatarFallback(name, isGuest, size) },
                error = { AvatarFallback(name, isGuest, size) }
            )
        }
    }
}

/** Initial letter for signed-in users, a simple silhouette for guests. No image assets needed. */
@Composable
private fun AvatarFallback(name: String, isGuest: Boolean, size: Dp) {
    val tint = MaterialTheme.colorScheme.onPrimaryContainer
    val initial = name.trim().firstOrNull()?.uppercaseChar()

    if (isGuest || initial == null) {
        val color = tint.copy(alpha = 0.65f)
        Canvas(Modifier.fillMaxSize()) {
            val s = this.size
            // head
            drawCircle(
                color,
                radius = s.minDimension * 0.18f,
                center = Offset(s.width / 2, s.height * 0.38f)
            )
            // shoulders (the avatar's circle clip trims the bottom)
            drawCircle(
                color,
                radius = s.minDimension * 0.34f,
                center = Offset(s.width / 2, s.height * 1.0f)
            )
        }
    } else {
        val fontSize = with(LocalDensity.current) { (size * 0.42f).toSp() }
        Text(
            text = initial.toString(),
            color = tint,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

/** Small tappable avatar used in the screen header to open the drawer. */
@Composable
fun DrawerAvatarButton(
    user: UserDto?,
    isGuest: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val openProfileMenuDescription = stringResource(Res.string.open_profile_menu)
    UserAvatar(
        imageUrl = user?.imgUrl,
        name = user?.userName.orEmpty(),
        isGuest = isGuest,
        size = 40.dp,
        modifier = modifier
            .semantics { contentDescription = openProfileMenuDescription }
            .clip(CircleShape)
            .clickable(role = Role.Button, onClick = onClick)
    )
}

// ───────────────────────────── Drawer content ─────────────────────────────

@Composable
fun UserDrawerContent(
    user: UserDto?,
    isGuest: Boolean,
    editEnabled: Boolean,
    onUploadPhotoFromGallery: () -> Unit,
    onUploadPhotoFromCamera: () -> Unit,
    onSignOut: () -> Unit
) {
    var menuOpen by rememberSaveable { mutableStateOf(false) }
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            UserAvatar(
                imageUrl = user?.imgUrl,
                name = user?.userName.orEmpty(),
                isGuest = isGuest,
                size = 104.dp
            )

            if (isGuest) {
                Text(
                    text = stringResource(Res.string.sign_in_to_add_profile_photo),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            } else {

                Box {
                    FilledTonalButton(
                        onClick = { menuOpen = true },
                        enabled = editEnabled && user != null,
                        shape = RoundedCornerShape(50)
                    ) { Text(stringResource(Res.string.upload_photo)) }

                    DropdownMenu(
                        expanded = menuOpen,
                        onDismissRequest = { menuOpen = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.choose_from_gallery)) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.PhotoLibrary,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                menuOpen = false
                                onUploadPhotoFromGallery()
                            }
                        )
                        if (showSplash) {
                            DropdownMenuItem(
                                text = { Text(stringResource(Res.string.take_a_photo)) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Camera,
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    menuOpen = false
                                    onUploadPhotoFromCamera()
                                }
                            )
                        }
                    }
                }
            }

            Text(
                text = when {
                    isGuest -> stringResource(Res.string.guest)
                    user == null -> stringResource(Res.string.loading)
                    else -> user.userName
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            if (!isGuest && !user?.email.isNullOrBlank()) {
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            if (!isGuest && user != null) {
                Spacer(Modifier.height(8.dp))
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    StatChip("Streak", user.streak.toString(), Modifier.weight(1f))
//                    StatChip("Score", user.totalScore.toString(), Modifier.weight(1f))
//                }
                FilledTonalButton(
                    onClick = onSignOut,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.wrapContentWidth(),
                    ) {
                    Row()
                    {
                        Icon(
                            rememberVectorPainter(Icons.AutoMirrored.Filled.Logout),
                            contentDescription = null
                        )
                        Text(
                            stringResource(Res.string.sign_out),
                            fontWeight = FontWeight.W700,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatChip(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}