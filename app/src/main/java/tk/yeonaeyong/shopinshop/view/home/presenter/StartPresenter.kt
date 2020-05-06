package tk.yeonaeyong.shopinshop.view.home.presenter

import tk.yeonaeyong.shopinshop.data.repository.member.MemberCallback
import tk.yeonaeyong.shopinshop.data.repository.member.MemberRepository
import tk.yeonaeyong.shopinshop.network.model.MemberResponse

class StartPresenter(
    private val repository: MemberRepository,
    private val view: StartContract.View
) : StartContract.Presenter {
    override fun userLogin(email: String, password: String) {
        repository.login(email, password, object :
            MemberCallback<MemberResponse> {
            override fun onSuccess(response: MemberResponse) {
                view.showMessage(true)
            }

            override fun onFailure(message: String) {
                view.showDialog(message)
            }

        }, object :
            MemberCallback<Boolean> {
            override fun onSuccess(response: Boolean) {

            }

            override fun onFailure(message: String) {

            }

        })
    }

    override fun logout() {
        repository.deleteLoginUser(object :
            MemberCallback<Boolean> {
            override fun onSuccess(response: Boolean) {
                view.showMessage(response)
            }

            override fun onFailure(message: String) {

            }
        })
    }
}