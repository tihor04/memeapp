package com.example.memeshareupdated
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


var currentimageurl:String?=null
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadmeme()
    }

    fun nextmeme(view: View) {
        loadmeme()
    }
    fun sharememe(view: View) {
    val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT,"hey, chechout this funny meme! $currentimageurl")

intent.type="plain/text"
        val chooser =Intent.createChooser(intent,"share meme using...")

        startActivity(intent)

    }

    private fun loadmeme(){

        //to show the progressbar the meme is being loaded??
        var progressbar:ProgressBar=findViewById(R.id.progress)
        progressbar.visibility=View.VISIBLE

        val url = "https://meme-api.herokuapp.com/gimme"


        //making a jsonobject request form the redit server//


        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
            val image:ImageView= findViewById(R.id.memeimage)
                currentimageurl=response.getString("url")
                Log.d("success","image url=$url")

                Glide.with(this).load(currentimageurl).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressbar.visibility=View.GONE
                        return false

                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressbar.visibility=View.GONE
                        return false
                    }
                }).into(image)
            },
            { error ->
                Log.d("fail","failed")
            }
        )

        //add it to the request queue//
        //the mysngleton class helps the app to send the request one at a time//
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)


    }
}