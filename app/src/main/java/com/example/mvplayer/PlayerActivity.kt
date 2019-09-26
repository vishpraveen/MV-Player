package com.example.mvplayer

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.dash.DashChunkSource
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.Surface
import android.widget.ProgressBar
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioRendererEventListener
import com.google.android.exoplayer2.decoder.DecoderCounters
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.video.VideoRendererEventListener
import com.utils.Utility


class PlayerActivity : AppCompatActivity() {
    private val TAG = PlayerActivity::class.java.simpleName
    private var playerView: PlayerView? = null
    var progressbar: ProgressBar? = null
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    private val BANDWIDTH_METER = DefaultBandwidthMeter()

    private lateinit var componentListener: ComponentListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
//        ExoPlayer Listeners
        componentListener = ComponentListener()

        initUI()
    }

    inner class ComponentListener: Player.DefaultEventListener(), VideoRendererEventListener,
        AudioRendererEventListener {
        private val TAG = ComponentListener::class.java.simpleName
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            val stateString: String

            when(playbackState){
                Player.STATE_IDLE ->{
                    stateString = "Player.STATE_IDLE            -"
                }
                Player.STATE_BUFFERING ->{
                    stateString = "Player.STATE_BUFFERING       -"
                    progressbar?.visibility = View.VISIBLE
                }
                Player.STATE_READY ->{
                    stateString = "Player.STATE_READY           -"
                    progressbar?.visibility = View.GONE
                }
                Player.STATE_ENDED ->{
                    stateString = "Player.STATE_ENDED           -"
                }
                else ->{
                    stateString = "UNKNOWN_STATE                -"
                }
            }
            Utility.LogI(TAG, "changed state to " + stateString
                    + " playWhenReady: " + playWhenReady)
        }

        override fun onDroppedFrames(count: Int, elapsedMs: Long) {

        }

        override fun onVideoEnabled(counters: DecoderCounters?) {

        }

        override fun onVideoSizeChanged(
            width: Int,
            height: Int,
            unappliedRotationDegrees: Int,
            pixelWidthHeightRatio: Float
        ) {

        }

        override fun onVideoDisabled(counters: DecoderCounters?) {

        }

        override fun onVideoDecoderInitialized(
            decoderName: String?,
            initializedTimestampMs: Long,
            initializationDurationMs: Long
        ) {

        }

        override fun onVideoInputFormatChanged(format: Format?) {

        }

        override fun onRenderedFirstFrame(surface: Surface?) {

        }

        override fun onAudioSinkUnderrun(
            bufferSize: Int,
            bufferSizeMs: Long,
            elapsedSinceLastFeedMs: Long
        ) {

        }

        override fun onAudioEnabled(counters: DecoderCounters?) {

        }

        override fun onAudioInputFormatChanged(format: Format?) {

            Utility.LogI(TAG, "Format Changed: ${format.toString()}")
        }

        override fun onAudioSessionId(audioSessionId: Int) {

        }

        override fun onAudioDecoderInitialized(
            decoderName: String?,
            initializedTimestampMs: Long,
            initializationDurationMs: Long
        ) {

        }

        override fun onAudioDisabled(counters: DecoderCounters?) {

        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if ((Util.SDK_INT <= 23)) {
            initializePlayer()
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        playerView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    /*
    * release resources player has occupied
    * */
    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            playWhenReady = player!!.playWhenReady
            player!!.removeListener(componentListener)
            player!!.removeVideoDebugListener(componentListener)
            player!!.removeAudioDebugListener(componentListener)
            player!!.release()
            player = null
        }
    }

    private fun initializePlayer() {
        if (player == null) {
            // a factory to create an AdaptiveVideoTrackSelection base on Internet BandWidth
            val adaptiveTrackSelection: TrackSelection.Factory =
                AdaptiveTrackSelection.Factory(BANDWIDTH_METER)

            player = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(this),
                DefaultTrackSelector(adaptiveTrackSelection),
                DefaultLoadControl()
            )

//            register the callback events to the ExoPlayer
            player!!.addListener(componentListener)
            player!!.addVideoDebugListener(componentListener)
            player!!.addAudioDebugListener(componentListener)

            playerView!!.player = player
            player!!.playWhenReady = playWhenReady
            player!!.seekTo(currentWindow, playbackPosition)

            /*
            * Adding the media url to the player
            * */
            val uri = Uri.parse(getString(R.string.media_url_mp4))
            val mediaSource = buildMediaSource(uri)
            player!!.prepare(mediaSource, true, false)
        }
    }

    private fun buildMediaSource(uri: Uri?): MediaSource? {
        // these are reused for both media sources we create below
        val extractorsFactory = DefaultExtractorsFactory()
        val dataSourceFactory = DefaultHttpDataSourceFactory("user-agent")

//        Dash Media Source Link
        val dashUri = Uri.parse(getString(R.string.media_url_dash))
        val manifestDataSourceFactory = DefaultHttpDataSourceFactory("ua")
        val dashChunkSourceFactory = DefaultDashChunkSource.Factory(
            DefaultHttpDataSourceFactory("ua", BANDWIDTH_METER)
        )
        val dashSource = DashMediaSource.Factory(dashChunkSourceFactory,manifestDataSourceFactory).createMediaSource(dashUri)

//        .Mp4 Media Source Link
        val videoSource = ExtractorMediaSource.Factory(
            DefaultHttpDataSourceFactory("exoplayer-codelab")
        ).createMediaSource(uri)

//        .Mp3 Media Source Link
        val audioUri = Uri.parse(getString(R.string.song_mp3))
        val audioSource = ExtractorMediaSource.Factory(
            DefaultHttpDataSourceFactory("exoplayer-codelab")
        ).createMediaSource(audioUri)

        return ConcatenatingMediaSource(audioSource, videoSource)
        /* return ExtractorMediaSource.Factory(
             DefaultHttpDataSourceFactory("exoplayer-codelab")
         ).createMediaSource(uri)*/
    }

    private fun initUI() {
        playerView = findViewById(R.id.video_view)
        progressbar = findViewById(R.id.progressbar)
    }
}
