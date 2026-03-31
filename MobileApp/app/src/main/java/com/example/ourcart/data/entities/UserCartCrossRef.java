package com.example.ourcart.data.entities;

import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName = "user_cart_cross_ref",
        primaryKeys = {"userId", "cartId"},
        indices = {@Index("cartId")}
)
public class UserCartCrossRef {
    public long userId;
    public long cartId;
}