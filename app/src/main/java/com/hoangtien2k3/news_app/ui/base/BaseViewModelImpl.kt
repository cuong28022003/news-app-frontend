package com.hoangtien2k3.news_app.ui.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hoangtien2k3.news_app.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseViewModelImpl : BaseViewModel, ViewModel() {
    override fun <T> performAction(
        resultLiveData: MutableLiveData<Resource<T>>,
        apiCall: () -> Call<T>
    ) {
        resultLiveData.value = Resource.Loading()
        val call = apiCall()
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    Log.d("API", "Response received: ${response.body()}")
                    val responseData = response.body()
                    responseData?.let {
                        resultLiveData.value = Resource.Success(it)
                    }
                } else {
                    Log.d("API", "Error response: ${response.code()} - ${response.message()}")
                    Log.d("API", "Response body: ${response.errorBody()?.string()}")
                    resultLiveData.value = Resource.Error("Request failed")
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                Log.d("Network", "Network error")
                resultLiveData.value = Resource.Error("Network error")
            }
        })
    }
}