package com.yeonae.chamelezone.view.mypage.presenter

import com.yeonae.chamelezone.data.repository.member.MemberCallBack
import com.yeonae.chamelezone.data.repository.member.MemberRepository
import com.yeonae.chamelezone.network.model.MemberResponse
import com.yeonae.chamelezone.network.room.entity.UserEntity

class UserModifyPresenter(
    private val repository: MemberRepository,
    private val view: UserModifyContract.View
) : UserModifyContract.Presenter {
    override fun getUser() {
        repository.getMember(object : MemberCallBack<UserEntity>{
            override fun onSuccess(response: UserEntity) {
                view.showUserInfo(response)
            }

            override fun onFailure(message: String) {

            }

        })
    }

    override fun updateMember(
        memberNumber: Int,
        password: String,
        nickName: String,
        phone: String
    ) {
        repository.updateMember(memberNumber, password, nickName, phone, object : MemberCallBack<Boolean>{
            override fun onSuccess(response: Boolean) {
                view.showMessage(response)
            }

            override fun onFailure(message: String) {

            }

        }, object : MemberCallBack<Boolean>{
            override fun onSuccess(response: Boolean) {

            }

            override fun onFailure(message: String) {

            }

        })
    }
}