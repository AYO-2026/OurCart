package com.example.ourcart.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ourcart.data.entities.CartItem;

import java.util.List;

@Dao
public interface CartItemDao {

    @Insert
    void insert(CartItem item);

    @Update
    void update(CartItem item);

    @Delete
    void delete(CartItem item);

    @Query("SELECT * FROM cart_items")
    List<CartItem> getAllItems();
}
