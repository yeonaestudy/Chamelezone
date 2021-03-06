package tk.yeonaeyong.shopinshop.view.mypage.myreview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_my_review.*
import tk.yeonaeyong.shopinshop.Injection
import tk.yeonaeyong.shopinshop.R
import tk.yeonaeyong.shopinshop.data.model.ReviewItem
import tk.yeonaeyong.shopinshop.ext.shortToast
import tk.yeonaeyong.shopinshop.network.room.entity.UserEntity
import tk.yeonaeyong.shopinshop.view.mypage.MoreButtonFragment
import tk.yeonaeyong.shopinshop.view.mypage.myreview.adapter.MyReviewRvAdapter
import tk.yeonaeyong.shopinshop.view.mypage.myreview.presenter.MyReviewContract
import tk.yeonaeyong.shopinshop.view.mypage.myreview.presenter.MyReviewPresenter
import tk.yeonaeyong.shopinshop.view.review.ReviewModifyActivity

class MyReviewActivity : AppCompatActivity(),
    MoreButtonFragment.OnModifyClickListener,
    MoreButtonFragment.OnDeleteClickListener,
    MyReviewContract.View {
    override lateinit var presenter: MyReviewContract.Presenter
    private val myReviewRvAdapter = MyReviewRvAdapter()
    private var placeNumber = 0
    private var reviewNumber = 0
    private var memberNumber: Int = 0
    lateinit var reviewItem: ReviewItem

    override fun getMember(user: UserEntity) {
        memberNumber = user.userNumber ?: 0
        presenter.getUserReview(memberNumber)
    }

    override fun getMemberCheck(response: Boolean) {
        presenter.getMember()
    }

    override fun showMyReviewList(reviewList: List<ReviewItem>) {
        if (reviewList.isNotEmpty()) {
            recycler_my_review.isVisible = true
            tv_message.isGone = true
            myReviewRvAdapter.addDataList(reviewList)
        } else {
            recycler_my_review.isGone = true
            tv_message.isVisible = true
            tv_message.text = getText(R.string.register_my_review)
        }
    }

    override fun showReviewDelete(message: String) {
        shortToast(R.string.review_delete)
    }

    override fun onModifyClick() {
        val intent = Intent(this, ReviewModifyActivity::class.java)
        if (::reviewItem.isInitialized)
            intent.putExtra(PLACE_NAME, reviewItem.name)
        intent.putExtra(PLACE_NUMBER, placeNumber)
        intent.putExtra(REVIEW_NUMBER, reviewNumber)
        intent.putExtra(MEMBER_NUMBER, memberNumber)
        startActivityForResult(intent, UPDATE_REQUEST)
    }

    override fun onDeleteClick() {
        presenter.deleteReview(placeNumber, reviewNumber, memberNumber)
        myReviewRvAdapter.removeData(reviewItem)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_review)
        setAdapter()

        presenter = MyReviewPresenter(
            Injection.reviewRepository(),
            Injection.memberRepository(),
            this
        )

        presenter.checkMember()

        myReviewRvAdapter.setOnClickListener(object : MyReviewRvAdapter.OnClickListener {
            override fun onClick(review: ReviewItem) {
                val placeName = review.name
                val reviewContent = review.content
                placeNumber = review.placeNumber
                reviewNumber = review.reviewNumber
                val intent = Intent(this@MyReviewActivity, MyReviewDetailActivity::class.java)
                intent.putExtra(PLACE_NUMBER, placeNumber)
                intent.putExtra(REVIEW_NUMBER, reviewNumber)
                intent.putExtra(PLACE_NAME, placeName)
                intent.putExtra(REVIEW_CONTENT, reviewContent)
                this@MyReviewActivity.startActivity(intent)
            }
        })

        myReviewRvAdapter.setMoreButtonListener(object : MyReviewRvAdapter.MoreButtonListener {
            override fun bottomSheetDialog(review: ReviewItem) {
                reviewItem = review
                placeNumber = review.placeNumber
                reviewNumber = review.reviewNumber
                showBottomSheet(placeNumber, reviewNumber)
            }
        })

        btn_back.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            UPDATE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK)
                    presenter.getUserReview(memberNumber)
            }
        }
    }

    private fun showBottomSheet(placeNumber: Int, reviewNumber: Int) {
        val bottomSheetDialogFragment = MoreButtonFragment()
        bottomSheetDialogFragment.show(supportFragmentManager, bottomSheetDialogFragment.tag)
    }

    private fun setAdapter() {
        recycler_my_review.layoutManager = LinearLayoutManager(this)
        recycler_my_review.adapter = myReviewRvAdapter
    }

    companion object {
        const val UPDATE_REQUEST = 102
        const val PLACE_NAME = "placeName"
        const val REVIEW_CONTENT = "content"
        const val PLACE_NUMBER = "placeNumber"
        const val REVIEW_NUMBER = "reviewNumber"
        const val MEMBER_NUMBER = "memberNumber"
    }
}