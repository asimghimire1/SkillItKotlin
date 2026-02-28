package com.example.kot_start.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kot_start.model.ProductModel
import com.example.kot_start.repository.ProductRepo
import com.example.kot_start.repository.UserRepo

class ProductViewModel(
    private val repo: ProductRepo
) : ViewModel() {



    fun addProduct(
        model: ProductModel,
        callback: (Boolean, String) -> Unit
    ) {
        repo.addProduct(model, callback)
    }

    fun updateProduct(
        model: ProductModel,
        callback: (Boolean, String) -> Unit
    ) {
        repo.updateProduct(model, callback)
    }

    fun deleteProduct(
        productId: String,
        callback: (Boolean, String) -> Unit
    ) {
        repo.deleteProduct(productId, callback)
    }



    private val _product = MutableLiveData<ProductModel?>()
    val product: MutableLiveData<ProductModel?> get() = _product

    private val _allProducts = MutableLiveData<List<ProductModel>?>()
    val allProducts: MutableLiveData<List<ProductModel>?> get() = _allProducts

    private val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> get() = _loading



    fun getProductById(productId: String) {
        repo.getProductById { success, message, data ->
            if (success) {
                _product.postValue(data)
            } else {
                _product.postValue(null)
            }
        }
    }

    fun getAllProduct() {
        _loading.postValue(true)
        repo.getAllProduct { success, message, data ->
            _loading.postValue(false)
            if (success) {
                _allProducts.postValue(data)
            } else {
                _allProducts.postValue(emptyList())
            }
        }
    }
}
