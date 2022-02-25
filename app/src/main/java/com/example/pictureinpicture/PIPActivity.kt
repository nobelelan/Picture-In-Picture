package com.example.pictureinpicture
import android.app.PictureInPictureParams
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.view.View
import android.widget.MediaController
import androidx.appcompat.app.ActionBar
import com.example.pictureinpicture.databinding.ActivityPipactivityBinding

class PIPActivity : AppCompatActivity() {

    lateinit var binding: ActivityPipactivityBinding

    private var videoUri: Uri? = null
    private var pictureInPictureParamsBuilder: PictureInPictureParams.Builder? = null
    private var actionBar: ActionBar? = null
    val TAG: String = "PIP_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPipactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init actionbar
        actionBar = supportActionBar

        // get intent with url and pass in function to play video
        setVideoView(intent)

        // picture in picture params requires android oreo or above
        pictureInPictureParamsBuilder = PictureInPictureParams.Builder()

        // handle click, enter PIP
        binding.btnPip.setOnClickListener{
            pictureInPictureMode()
        }

    }

    private fun setVideoView(intent: Intent?) {
        val videoURL = intent?.getStringExtra("videoURL")
        Log.d(TAG, "setVideoView: $videoURL")

        // media controller for video controls
        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding.videoView)

        videoUri = Uri.parse(videoURL)

        //set media controller to video view
        binding.videoView.setMediaController(mediaController)
        // set video uri to video view
        binding.videoView.setVideoURI(videoUri)

        //add video prepare listener
        binding.videoView.setOnPreparedListener { mp ->
            //video is prepared, play
            Log.d(TAG, "Video Prepared, playing...")
            mp.start()
        }
    }

    private fun pictureInPictureMode(){
        //requires android O or higher
        Log.d(TAG, "pictureInPictureMode: Try to enter in PIP mode")
        val aspectRatio = Rational(binding.videoView.width, binding.videoView.height)
        pictureInPictureParamsBuilder?.setAspectRatio(aspectRatio)?.build()
        enterPictureInPictureMode(pictureInPictureParamsBuilder!!.build())
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        pictureInPictureMode()
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode){
            Log.d(TAG, "onPictureInPictureModeChanged: Entered PIP")
            //hid pip button and action bar
            binding.btnPip.visibility = View.GONE
            actionBar?.hide()
        }else{
            Log.d(TAG, "onPictureInPictureModeChanged: Exited PIP")
            binding.btnPip.visibility = View.VISIBLE
            actionBar?.show()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        //when 1st video is playing and entered in PIP, clicked 2nd video, play 2nd video
        Log.d(TAG, "onNewIntent: Play new video")
        setVideoView(intent)
    }

    override fun onStop() {
        super.onStop()
        if (binding.videoView.isPlaying){
            binding.videoView.stopPlayback()
        }
    }
}