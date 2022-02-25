package com.example.pictureinpicture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pictureinpicture.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val videOneUrl = "https://cdn.videvo.net/videvo_files/video/free/2013-09/large_watermarked/AbstractRotatingCubesVidevo_preview.mp4"
    private val videTwoUrl = "https://cdn.videvo.net/videvo_files/video/free/2012-10/large_watermarked/hd1967_preview.mp4"
    private val videThreeUrl = "https://cdn.videvo.net/videvo_files/video/premium/video0288/large_watermarked/_DJ31_preview.mp4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVideoOne.setOnClickListener{
            playVideo(videOneUrl)
        }
        binding.btnVideoTwo.setOnClickListener{
            playVideo(videTwoUrl)
        }
        binding.btnVideoThree.setOnClickListener{
            playVideo(videThreeUrl)
        }
    }

    private fun playVideo(url: String){
        val intent = Intent()
        intent.setClass(this , PIPActivity::class.java)
        intent.putExtra("videoURL", url)
        startActivity(intent)
    }
}