package com.yeonae.chamelezone.view.place

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.yeonae.chamelezone.R
import com.yeonae.chamelezone.adapter.ImageViewPagerAdapter
import com.yeonae.chamelezone.view.place.adapter.PlaceDetailPagerAdapter
import kotlinx.android.synthetic.main.activity_place_detail.*

class PlaceDetailActivity : AppCompatActivity() {


    companion object{
        private const val PLACE_NAME = "placeName"
        private const val PLACE_NUMBER = "placeNumber"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        vp.post {
            vp.layoutParams = vp.layoutParams.apply {
                height = ((vp.parent as ViewGroup).width / 3) * 2
            }
            setupView()
        }

    }

    private fun setupView(){
        val placeName = intent.getStringExtra(PLACE_NAME)
        val placeNumber = intent.getIntExtra(PLACE_NUMBER, 0)
        tv_place_name.text = placeName

        btn_back.setOnClickListener {
            finish()
        }

        val imageAdapter = ImageViewPagerAdapter()
        vp.adapter = imageAdapter
        tab_layout.setupWithViewPager(vp, true)

        val fragmentAdapter = PlaceDetailPagerAdapter(supportFragmentManager, placeNumber)
        viewpager_detail.adapter = fragmentAdapter
        tabs_detail.setupWithViewPager(viewpager_detail)
    }
}