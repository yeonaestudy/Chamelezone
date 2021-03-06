package tk.yeonaeyong.shopinshop.view.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_place.view.*
import tk.yeonaeyong.shopinshop.R
import tk.yeonaeyong.shopinshop.ext.Url.IMAGE_RESOURCE
import tk.yeonaeyong.shopinshop.ext.glideImageSet
import tk.yeonaeyong.shopinshop.network.model.PlaceResponse
import tk.yeonaeyong.shopinshop.util.distanceByDegree

class HomePlaceRvAdapter :
    RecyclerView.Adapter<HomePlaceRvAdapter.Holder>() {
    private val placeList = arrayListOf<PlaceResponse>()
    private lateinit var itemClickListener: OnItemClickListener
    private lateinit var likeButtonListener: LikeButtonListener
    private var distanceCalculator = ""
    private lateinit var place: PlaceResponse
    private var currentLatitude = 0.0
    private var currentLongitude = 0.0

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, place: PlaceResponse)
    }

    interface LikeButtonListener {
        fun onLikeClick(placeResponse: PlaceResponse, isChecked: Boolean)
    }

    fun setItemClickListener(clickListener: OnItemClickListener) {
        itemClickListener = clickListener
    }

    fun setLikeButtonListener(listener: LikeButtonListener) {
        likeButtonListener = listener
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val placeImg = itemView.place_img
        private val placeName = itemView.place_name
        private val keyword = itemView.keyword

        @SuppressLint("SetTextI18n")
        fun bind(place: PlaceResponse) {
            val latitude = place.latitude.toDouble()
            val longitude = place.longitude.toDouble()

            placeName.text = place.name
            place.keywordName.forEach {
                if (it == place.keywordName[0]) {
                    keyword.text = it
                } else {
                    keyword.text = "${keyword.text}, $it"
                }
            }

            if (place.savedImageName.isNotEmpty()) {
                val image = IMAGE_RESOURCE + place.savedImageName[0]
                placeImg.glideImageSet(image, placeImg.measuredWidth, placeImg.measuredHeight)
            }

            itemView.setOnClickListener {
                itemClickListener.onItemClick(itemView, adapterPosition, place)
            }

            itemView.apply {
                btn_like.isChecked = place.likeStatus
                if (place.memberNumber != 0) {
                    btn_like.setOnClickListener {
                        likeButtonListener.onLikeClick(place, btn_like.isChecked)
                    }
                }
                distanceCalculator =
                    distanceByDegree(currentLatitude, currentLongitude, latitude, longitude)
                if (currentLatitude == 0.0 || currentLongitude == 0.0) {
                    distance.text = "0km"
                } else {
                    distance.text = distanceCalculator + "km"
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(placeList[position])
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    fun replaceDataDistance(lat: Double, log: Double) {
        currentLatitude = lat
        currentLongitude = log
        notifyItemRangeChanged(0, itemCount, PAYLOAD_ITEM_CHANGE)
    }

    fun addData(addDataList: List<PlaceResponse>) {
        placeList.clear()
        placeList.addAll(addDataList)
        notifyDataSetChanged()
    }

    companion object {
        const val PAYLOAD_ITEM_CHANGE = 1
    }
}