package com.yeonae.chamelezone.view.place.presenter

import com.yeonae.chamelezone.data.repository.like.LikeCallback
import com.yeonae.chamelezone.data.repository.like.LikeRepository
import com.yeonae.chamelezone.data.repository.member.MemberCallback
import com.yeonae.chamelezone.data.repository.member.MemberRepository
import com.yeonae.chamelezone.data.repository.place.PlaceCallback
import com.yeonae.chamelezone.data.repository.place.PlaceRepository
import com.yeonae.chamelezone.network.model.LikeResponse
import com.yeonae.chamelezone.network.model.PlaceResponse
import com.yeonae.chamelezone.network.room.entity.UserEntity

class PlaceDetailPresenter(
    private val memberRepository: MemberRepository,
    private val placeRepository: PlaceRepository,
    private val likeRepository: LikeRepository,
    private val view: PlaceDetailContract.View
) :PlaceDetailContract.Presenter {
    override fun placeDetail(placeNumber: Int, memberNumber: Int?) {
        placeRepository.getPlaceDetail(placeNumber, memberNumber, object : PlaceCallback<PlaceResponse>{
            override fun onSuccess(response: PlaceResponse) {
                view.placeInfo(response)
            }

            override fun onFailure(message: String) {

            }

        })
    }

    override fun checkLogin() {
        memberRepository.checkLogin(object : MemberCallback<Boolean> {
            override fun onSuccess(response: Boolean) {
                view.showResultView(response)
            }

            override fun onFailure(message: String) {

            }

        })
    }

    override fun getUser() {
        memberRepository.getMember(object : MemberCallback<UserEntity>{
            override fun onSuccess(response: UserEntity) {
                view.deliverUserInfo(response)
            }

            override fun onFailure(message: String) {

            }

        })
    }

    override fun selectLike(memberNumber: Int, placeNumber: Int) {
        likeRepository.selectLike(memberNumber, placeNumber, object : LikeCallback<LikeResponse>{
            override fun onSuccess(response: LikeResponse) {
                view.showLikeMessage(response.toLikeStatusItem())
            }

            override fun onFailure(message: String) {

            }

        })
    }

    override fun deleteLike(memberNumber: Int, placeNumber: Int) {
        likeRepository.deleteLike(memberNumber, placeNumber, object : LikeCallback<LikeResponse>{
            override fun onSuccess(response: LikeResponse) {
                view.showDeleteLikeMessage(response.toLikeStatusItem())
            }

            override fun onFailure(message: String) {

            }

        })
    }
}