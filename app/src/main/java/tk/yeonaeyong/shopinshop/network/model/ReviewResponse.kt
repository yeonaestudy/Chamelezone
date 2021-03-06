package tk.yeonaeyong.shopinshop.network.model

import com.google.gson.annotations.SerializedName
import tk.yeonaeyong.shopinshop.data.model.ReviewItem
import tk.yeonaeyong.shopinshop.ext.Url.IMAGE_RESOURCE

data class ReviewResponse(
    @SerializedName("reviewNumber")
    val reviewNumber: Int,
    @SerializedName("memberNumber")
    val memberNumber: Int,
    @SerializedName("placeNumber")
    val placeNumber: Int,
    @SerializedName("nickName")
    val nickName: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("regiDate")
    val regiDate: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("savedImageName")
    val savedImageName: ArrayList<String>,
    @SerializedName("imageNumber")
    val imageNumber: ArrayList<Int>
) {
    fun toReviewItem(): ReviewItem {
        val image = IMAGE_RESOURCE + savedImageName.first()
        return ReviewItem(
            reviewNumber,
            placeNumber,
            memberNumber,
            name,
            nickName,
            regiDate,
            content,
            image,
            savedImageName,
            imageNumber
        )
    }
}