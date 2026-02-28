package com.example.kot_start.repository;

import android.R
import com.example.kot_start.model.ProductModel
import com.example.kot_start.model.UserModel

interface ProductRepo {

    fun addProduct(
        model: ProductModel,
        callback: (Boolean, String) -> Unit
    )

    fun deleteProduct(productId: String, callback: (Boolean, String) -> Unit)

    fun editProduct(

        model: ProductModel,
        callback: (Boolean, String) -> Unit
    )

    fun getAllProduct(callback: (Boolean, String, List<ProductModel>?) -> Unit)

    fun getProductById(productId: String, callback: (Boolean, String, ProductModel?) -> Unit)

}
//        var id= ref.push().key.toString()
