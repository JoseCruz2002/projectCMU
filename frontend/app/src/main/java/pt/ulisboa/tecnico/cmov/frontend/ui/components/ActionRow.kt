package pt.ulisboa.tecnico.cmov.frontend.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ActionRow(
    actions: List<Action>,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    Row(modifier = modifier) {
        actions.forEach { action ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = action.onClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = containerColor,
                        contentColor = contentColor
                    )
                ) {
                    Icon(imageVector = action.icon, contentDescription = action.label)
                }
                Text(
                    text = action.label,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

data class Action(
    val icon: ImageVector,
    val label: String,
    val onClick: () -> Unit
)

@Preview(showBackground = true)
@Composable
fun PreviewActionRow() {
    val actions = listOf(
        Action(
            icon = Icons.Default.Favorite,
            label = "Like",
            onClick = {}
        ),
        Action(
            icon = Icons.AutoMirrored.Default.Comment,
            label = "Comment Long",
            onClick = {}
        ),
        Action(
            icon = Icons.Default.Share,
            label = "Share",
            onClick = {}
        )
    )

    ActionRow(actions = actions)
}
