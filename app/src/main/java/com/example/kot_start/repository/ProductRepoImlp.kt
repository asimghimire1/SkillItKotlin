package com.example.kot_start.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kot_start.model.ProductModel
import com.example.kot_start.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProductRepoImpl : ProductRepo {

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    val ref: DatabaseReference = database.getReference("products")

    override fun addProduct(
        model: ProductModel,
        callback: (Boolean, String) -> Unit
    ) {
        val id = ref.push().key.toString()
        model.productId = id

        ref.child(id).setValue(model).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Product added successfully")
            } else {
                callback(false, "${it.exception?.message}")

            }
        }
    }

    override fun deleteProduct(
        productId: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(productId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Product added successfully")
            } else {
                callback(false, "${it.exception?.message}")

            }
        }
    }

    override fun editProduct(
        model: ProductModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(model.productId).updateChildren(model.toMap()).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Product updated successfully")
            } else {
                callback(false, "${it.exception?.message}")

            }
        }
    }

    override fun getAllProduct(callback: (Boolean, String, List<ProductModel>?) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var allProducts = mutableListOf<ProductModel>()
                    for (data in snapshot.children) {
                        val products = data.getValue(ProductModel::class.java)
                        if (products != null) {
                            allProducts.add(products)
                        }
                    }
                    callback(true, "Product fetched successfully", allProducts)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, emptyList())
            }

        })
    }

    override fun getProductById(
        productId: String,
        callback: (Boolean, String, ProductModel?) -> Unit
    ) {
        ref.child(productId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val products = snapshot.getValue(ProductModel::class.java)
                    if (products != null) {
                        callback(true, "Product fetched successfully", products)
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }
}