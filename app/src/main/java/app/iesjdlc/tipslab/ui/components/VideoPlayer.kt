package app.iesjdlc.tipslab.ui.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun VideoPlayer(
    context: Context,
    videoUrl: String
) {
    val player = ExoPlayer.Builder(context).build()

    val mediaItem = MediaItem.fromUri(videoUrl)

    player.setMediaItem(mediaItem)
    player.prepare()
    player.play()

    val playerView = PlayerView(context)
    playerView.player = player
}