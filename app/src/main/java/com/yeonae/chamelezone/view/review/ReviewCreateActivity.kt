package com.yeonae.chamelezone.view.review

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.kroegerama.imgpicker.BottomSheetImagePicker
import com.kroegerama.kaiteki.toast
import com.yeonae.chamelezone.R
import com.yeonae.chamelezone.data.repository.review.ReviewRepositoryImpl
import com.yeonae.chamelezone.data.source.remote.review.ReviewRemoteDataSourceImpl
import com.yeonae.chamelezone.ext.catchFocus
import com.yeonae.chamelezone.ext.glideImageUriSet
import com.yeonae.chamelezone.network.api.RetrofitConnection
import com.yeonae.chamelezone.view.review.presenter.ReviewContract
import com.yeonae.chamelezone.view.review.presenter.ReviewPresenter
import kotlinx.android.synthetic.main.activity_review_create.*
import kotlinx.android.synthetic.main.slider_item_image.*

class ReviewCreateActivity : AppCompatActivity(), BottomSheetImagePicker.OnImagesSelectedListener,
    ReviewContract.View {
    override lateinit var presenter: ReviewContract.Presenter


    override fun review(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        finish()
    }

    override fun onImagesSelected(uris: List<Uri>, tag: String?) {
        toast("$tag")

        uris.forEach { uri ->
            val iv = LayoutInflater.from(this).inflate(
                R.layout.slider_item_image,
                image_container,
                false
            ) as ImageView
            if (image_container.childCount < 4) {
                image_container.addView(iv)
                glideImageUriSet(uri, image_item.measuredWidth, image_item.measuredHeight, iv)
            }
            if (image_container.childCount <= 4) {
                btn_image_create.setOnClickListener {
                    pickMulti()
                    if (uris.isNotEmpty()) {
                        image_container.removeAllViews()
                        image_container.addView(iv)
                        glideImageUriSet(uri, image_item.measuredWidth, image_item.measuredHeight, iv)
                    }
                }
            }
        }
//
//        val intent = Intent()
//        intent.putExtra("imgResult", uris.toString())
//        setResult(Activity.RESULT_OK, intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_create)

        tv_title.catchFocus(this)
        btn_back.setOnClickListener {
            finish()
        }
        setupGUI()

        presenter = ReviewPresenter(
            ReviewRepositoryImpl.getInstance(
                ReviewRemoteDataSourceImpl.getInstance(RetrofitConnection.reviewService)
            ), this
        )

        btn_register.setOnClickListener {
            val content = "${edt_review.text}"

            presenter.reviewCreate("", "", "", content)
        }
    }

    private val permissionListener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            Toast.makeText(this@ReviewCreateActivity, "권한이 허용되었습니다", Toast.LENGTH_SHORT).show()
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
            Toast.makeText(
                this@ReviewCreateActivity,
                "권한이 거부되었습니다\n$deniedPermissions",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun pickMulti() {
        BottomSheetImagePicker.Builder(getString(R.string.file_provider))
            .multiSelect(2, 4)
            .multiSelectTitles(
                R.plurals.pick_multi,
                R.plurals.pick_multi_more,
                R.string.pick_multi_limit
            )
            .peekHeight(R.dimen.peekHeight)
            .columnSize(R.dimen.columnSize)
            .requestTag("사진이 선택되었습니다.")
            .show(supportFragmentManager)
    }

    private fun setupGUI() {
        btn_image_create.setOnClickListener {
            checkPermission()
            pickMulti()
        }
        btn_image_clear.setOnClickListener { image_container.removeAllViews() }
    }

    private fun checkPermission() {
        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setRationaleTitle(R.string.rationale_title)
            .setRationaleMessage(R.string.album_rationale_message)
            .setDeniedTitle(R.string.Permission_denied)
            .setDeniedMessage(R.string.permission_msg)
            .setGotoSettingButtonText(R.string.setting)
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check()
    }
}