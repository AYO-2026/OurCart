package com.example.ourcart.data.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "users",
        indices = {
        @Index(value = "email", unique = true),
        @Index(value = "nickname", unique = true)
})
public class User {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String email;
    public String nickname;
    public String password;

}
