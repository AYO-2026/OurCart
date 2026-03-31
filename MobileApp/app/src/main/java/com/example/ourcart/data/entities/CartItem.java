package com.example.ourcart.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_items",
        foreignKeys = {
                @ForeignKey(
                        entity = Cart.class,
                        parentColumns = "id",
                        childColumns = "cartId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "id",
                        childColumns = "boughtBy",
                        onDelete = ForeignKey.SET_NULL
                )},
        indices = {
                @Index("cartId"),
                @Index("boughtBy")
        }
)
public class CartItem {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public double quantity;
    public Unit unit;
    public boolean isBought = false;
    public long cartId;
    public Long boughtBy = null;
    public Double price = null;

}
