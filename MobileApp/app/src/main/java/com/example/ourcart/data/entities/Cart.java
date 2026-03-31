package com.example.ourcart.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "carts",
        foreignKeys =
            @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "creatorId",
                onDelete = ForeignKey.CASCADE
            ),
        indices = {
            @Index("creatorId")
        }
)
public class Cart {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public long creatorId;

    public String name;
}