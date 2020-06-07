package com.aboesmail.omar.pharma.Database.product;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CartProduct cartProduct);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(CartProduct cartProduct);

    @Delete
    void delete(CartProduct cartProduct);

    @Query("DELETE  FROM products_table")
    void deleteAll();

    @Query("SELECT * FROM products_table ")
    LiveData<List<CartProduct>> getAllCartProducts();

    @Query("SELECT * FROM products_table WHERE id like (:id)")
    CartProduct getCartProductByID(String id);

}
