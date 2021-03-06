package tk.yeonaeyong.shopinshop.view.mypage.myplace.presenter

import tk.yeonaeyong.shopinshop.data.model.PlaceItem
import tk.yeonaeyong.shopinshop.network.room.entity.UserEntity

interface MyPlaceContract {

    interface View {
        var presenter: Presenter
        fun showMyPlaceList(response: List<PlaceItem>)
        fun showUserInfo(user: UserEntity)
        fun showMessage(message: String)
        fun showDeleteResult(response: Boolean)
    }

    interface Presenter {
        fun getMyPlaceList(memberNumber: Int)
        fun getUser()
        fun deletePlace(placeNumber: Int, memberNumber: Int)
    }
}