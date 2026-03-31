package com.example.ourcart.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.ourcart.data.dao.CartDao;
import com.example.ourcart.data.dao.CartItemDao;
import com.example.ourcart.data.dao.UserDao;
import com.example.ourcart.data.entities.Cart;
import com.example.ourcart.data.entities.CartItem;
import com.example.ourcart.data.entities.Converters;
import com.example.ourcart.data.entities.User;
import com.example.ourcart.data.entities.UserCartCrossRef;

@Database(entities = {
        CartItem.class,
        Cart.class,
        User.class,
        UserCartCrossRef.class},
        version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract CartItemDao cartItemDao();
    public abstract CartDao cartDao();
    public abstract UserDao userDao();
    private static volatile AppDatabase INSTANCE;
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "ourcart_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}