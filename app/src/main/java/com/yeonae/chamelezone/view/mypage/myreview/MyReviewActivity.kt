package com.yeonae.chamelezone.view.mypage.myreview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yeonae.chamelezone.Injection
import com.yeonae.chamelezone.R
import com.yeonae.chamelezone.data.model.ReviewItem
import com.yeonae.chamelezone.network.room.entity.UserEntity
import com.yeonae.chamelezone.view.mypage.MoreButtonFragment
import com.yeonae.chamelezone.view.mypage.myreview.adapter.MyReviewRvAdapter
import com.yeonae.chamelezone.view.mypage.myreview.presenter.MyReviewContract
import com.yeonae.chamelezone.view.mypage.myreview.presenter.MyReviewPresenter
import com.yeonae.chamelezone.view.review.ReviewModifyActivity
import kotlinx.android.synthetic.main.activity_my_review.*

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
        memberNumber.let { presenter.getUserReview(it) }
    }

    override fun getMemberCheck(response: Boolean) {
        presenter.getMember()
    }

    override fun showMyReviewList(reviewList: List<ReviewItem>) {
        myReviewRvAdapter.addData(reviewList)
    }

    override fun showReviewDelete(message: String) {
        Toast.makeText(this, "리뷰가 삭제되었습니다", Toast.LENGTH_LONG).show()
    }

    override fun onModifyClick() {
        val intent = Intent(this, ReviewModifyActivity::class.java)
        if (::reviewItem.isInitialized)
            intent.putExtra(PLACE_NAME, reviewItem.name)
        startActivity(intent)
    }

    override fun onDeleteClick() {
        presenter.deleteReview(placeNumber, reviewNumber, memberNumber)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_review)
        setAdapter()

        presenter = MyReviewPresenter(
            Injection.reviewRepository(), Injection.memberRepository(), this
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
                placeNumber = review.placeNumber
                reviewNumber = review.reviewNumber
                showBottomSheet(placeNumber, reviewNumber)
            }
        })

        btn_back.setOnClickListener {
            finish()
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
        const val PLACE_NAME = "placeName"
        const val REVIEW_CONTENT = "content"
        const val PLACE_NUMBER = "placeNumber"
        const val REVIEW_NUMBER = "reviewNumber"
    }
}