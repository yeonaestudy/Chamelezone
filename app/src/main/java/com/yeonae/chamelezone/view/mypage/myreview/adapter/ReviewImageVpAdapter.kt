package com.yeonae.chamelezone.view.mypage.myreview.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.viewpager.widget.PagerAdapter
import com.yeonae.chamelezone.R
import com.yeonae.chamelezone.data.model.ReviewItem
import com.yeonae.chamelezone.ext.glideImageSet
import com.yeonae.chamelezone.view.mypage.myreview.MyReviewDetailActivity.Companion.PLACE_NUMBER
import com.yeonae.chamelezone.view.mypage.myreview.MyReviewDetailActivity.Companion.REVIEW_NUMBER
import com.yeonae.chamelezone.view.mypage.myreview.MyReviewImageDetailActivity
import kotlinx.android.synthetic.main.slider_image.view.*

class ReviewImageVpAdapter(private val images: List<String>, private val placeNumber: Int, private val reviewNumber: Int) : PagerAdapter() {
    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setItemClickListener(clickListener: OnItemClickListener) {
        itemClickListener = clickListener
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean =
        view == obj

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view =
            LayoutInflater.from(container.context)
                .inflate(R.layout.slider_item_myreview_image, container, false)

        view.post {
            view.image_view.glideImageSet(images[position], view.measuredWidth, view.measuredHeight)
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) =
        container.invalidate()

    override fun getCount(): Int =
        images.size

}