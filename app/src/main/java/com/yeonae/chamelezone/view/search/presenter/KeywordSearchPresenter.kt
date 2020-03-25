package com.yeonae.chamelezone.view.search.presenter

import com.yeonae.chamelezone.data.model.PlaceItem
import com.yeonae.chamelezone.data.repository.place.PlaceCallBack
import com.yeonae.chamelezone.data.repository.place.PlaceRepository
import com.yeonae.chamelezone.network.model.KeywordResponse
import com.yeonae.chamelezone.network.model.PlaceResponse

class KeywordSearchPresenter(
    private val repository: PlaceRepository,
    private val view: KeywordSearchContract.View
) : KeywordSearchContract.Presenter {
    override fun getKeyword() {
        repository.getKeyword(object : PlaceCallBack<List<KeywordResponse>> {
            override fun onSuccess(response: List<KeywordResponse>) {
                view.showKeywordList(response)
            }

            override fun onFailure(message: String) {

            }

        })
    }

    override fun searchByKeyword(keyword: String) {
        repository.getSearchByKeyword(keyword, object : PlaceCallBack<List<PlaceResponse>> {
            override fun onSuccess(response: List<PlaceResponse>) {
                val placeItem = mutableListOf<PlaceItem>()
                for (i in response.indices) {
                    placeItem.add(response[i].toPlaceItem(response[i]))
                }
                view.showPlaceList(placeItem)
            }

            override fun onFailure(message: String) {
                view.showMessage(message)
            }

        })
    }
}