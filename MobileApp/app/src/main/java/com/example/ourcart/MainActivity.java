package com.example.ourcart;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ourcart.data.AppDatabase;
import com.example.ourcart.data.dao.CartDao;
import com.example.ourcart.data.dao.CartItemDao;
import com.example.ourcart.data.dao.UserDao;
import com.example.ourcart.data.entities.Cart;
import com.example.ourcart.data.entities.CartItem;
import com.example.ourcart.data.entities.Unit;
import com.example.ourcart.data.entities.User;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    // --- Елементи для CartItem (Товар) ---
    private EditText editTextName;
    private EditText editTextQuantity;
    private Spinner spinnerUnit;
    private EditText editTextCartId;
    private Button buttonAdd;

    // --- Елементи для User (Користувач) ---
    private EditText editTextUserName;
    private EditText editTextUserEmail;
    private Button buttonAddUser;

    // --- Елементи для Cart (Кошик) ---
    private EditText editTextCartName;
    private EditText editTextCartUserId;
    private Button buttonAddCart;

    // --- DAO ---
    private CartItemDao cartItemDao;
    private UserDao userDao;
    private CartDao cartDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ініціалізація БД
        AppDatabase db = AppDatabase.getInstance(this);
        cartItemDao = db.cartItemDao();
        userDao = db.userDao();
        cartDao = db.cartDao();

        // ⚠️ ЗАПУСК АВТОЗАПОВНЕННЯ БАЗИ ДАНИХ
        // УВАГА: Після першого запуску додатку і появи повідомлення "БАЗУ ЗАПОВНЕНО!"
        // обов'язково поставте дві скісні риски // перед цим рядком:


        // --- Прив'язка UI для CartItem ---
        editTextName = findViewById(R.id.editTextName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        editTextCartId = findViewById(R.id.editTextCartId);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(v -> saveItemToDatabase());

        // --- Прив'язка UI для User ---
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextUserEmail = findViewById(R.id.editTextUserEmail);
        buttonAddUser = findViewById(R.id.buttonAddUser);
        buttonAddUser.setOnClickListener(v -> saveUserToDatabase());

        // --- Прив'язка UI для Cart ---
        editTextCartName = findViewById(R.id.editTextCartName);
        editTextCartUserId = findViewById(R.id.editTextCartUserId);
        buttonAddCart = findViewById(R.id.buttonAddCart);
        buttonAddCart.setOnClickListener(v -> saveCartToDatabase());
    }

    // --- Збереження товару вручну ---
    private void saveItemToDatabase() {
        fillDatabaseWithTestData();
        String name = editTextName.getText().toString().trim();
        String quantityStr = editTextQuantity.getText().toString().trim();
        String selectedUnitStr = spinnerUnit.getSelectedItem().toString();
        String cartIdStr = editTextCartId.getText().toString().trim();

        if (name.isEmpty() || quantityStr.isEmpty() || cartIdStr.isEmpty()) {
            Toast.makeText(this, "Заповніть всі поля товару", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            long cartId = Long.parseLong(cartIdStr);
            Unit unit = Unit.valueOf(selectedUnitStr);

            CartItem item = new CartItem();
            item.name = name;
            item.quantity = quantity;
            item.unit = unit;
            item.isBought = false;
            item.cartId = cartId;

            Executors.newSingleThreadExecutor().execute(() -> {
                cartItemDao.insert(item);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Товар додано!", Toast.LENGTH_SHORT).show();
                    editTextName.setText("");
                    editTextQuantity.setText("");
                    editTextName.requestFocus();
                });
            });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Помилка формату чисел", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            runOnUiThread(() -> Toast.makeText(this, "Помилка! Кошика з таким ID не існує.", Toast.LENGTH_LONG).show());
        }
    }

    // --- Збереження користувача вручну ---
    private void saveUserToDatabase() {
        String name = editTextUserName.getText().toString().trim();
        String email = editTextUserEmail.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Введіть ім'я користувача", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();
        user.nickname = name; // Якщо у вас змінна називається nickname, замініть на user.nickname
        user.email = email;

        Executors.newSingleThreadExecutor().execute(() -> {
            userDao.insert(user);
            runOnUiThread(() -> {
                Toast.makeText(this, "Користувача створено!", Toast.LENGTH_SHORT).show();
                editTextUserName.setText("");
                editTextUserEmail.setText("");
            });
        });
    }

    // --- Збереження кошика вручну ---
    private void saveCartToDatabase() {
        String cartName = editTextCartName.getText().toString().trim();
        String userIdStr = editTextCartUserId.getText().toString().trim();

        if (cartName.isEmpty() || userIdStr.isEmpty()) {
            Toast.makeText(this, "Введіть назву кошика та ID автора", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            long creatorId = Long.parseLong(userIdStr);

            Cart cart = new Cart();
            cart.name = cartName;
            cart.creatorId = creatorId;

            Executors.newSingleThreadExecutor().execute(() -> {
                cartDao.insert(cart);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Кошик створено!", Toast.LENGTH_SHORT).show();
                    editTextCartName.setText("");
                    editTextCartUserId.setText("");
                });
            });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "ID автора має бути цифрою", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            runOnUiThread(() -> Toast.makeText(this, "Помилка! Юзера з таким ID не існує.", Toast.LENGTH_LONG).show());
        }
    }

    // =========================================================
    // СПЕЦІАЛЬНИЙ МЕТОД ДЛЯ АВТОМАТИЧНОГО ЗАПОВНЕННЯ БАЗИ ДАНИХ
    // =========================================================
    private void fillDatabaseWithTestData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {

                // 1. Створюємо 20 користувачів (Англійські імена)
                String[] userNames = {"Alex", "Maria", "John", "Emma", "Andrew", "Victoria", "David", "Natalie", "Michael", "Julia", "Daniel", "Sarah", "Matthew", "Kate", "William", "Olivia", "James", "Sophia", "Robert", "Emily"};
                for (int i = 0; i < 20; i++) {
                    User u = new User();
                    u.nickname = userNames[i];
                    u.email = "user" + (i + 1) + "@testmail.com";
                    userDao.insert(u);
                }

                // 2. Створюємо 20 кошиків (Англійські категорії)
                String[] cartNames = {"Groceries", "Office Supplies", "Weekend Trip", "Renovation", "Party", "Sports Gear", "Pharmacy", "Car Accessories", "Clothes", "Fishing", "Cosmetics", "Pet Supplies", "Books", "Electronics", "Garden", "Tools", "Kitchen", "Gifts", "Hobbies", "Other"};
                for (int i = 0; i < 20; i++) {
                    Cart c = new Cart();
                    c.name = cartNames[i];
                    c.creatorId = i + 1; // ID від 1 до 20
                    cartDao.insert(c);
                }

                // 3. Створюємо 30 товарів (Англійські продукти)
                String[] itemNames = {"Milk", "Bread", "Cheese", "Sausage", "Apples", "Bananas", "Sugar", "Salt", "Olive Oil", "Butter", "Tea", "Coffee", "Pasta", "Rice", "Oatmeal", "Eggs", "Tomatoes", "Cucumbers", "Potatoes", "Carrots", "Onions", "Garlic", "Cookies", "Candies", "Orange Juice", "Water", "Yogurt", "Sour Cream", "Ice Cream", "Chocolate"};
                Unit safeUnit = Unit.values()[0];

                for (int i = 0; i < 30; i++) {
                    CartItem item = new CartItem();
                    item.name = itemNames[i];
                    item.quantity = 2 * i;
                    item.unit = safeUnit;
                    item.isBought = (i % 3 == 0);
                    item.cartId = (i % 10) + 1; // Розкидаємо по перших 10 кошиках
                    cartItemDao.insert(item);
                }

                runOnUiThread(() -> {
                    Toast.makeText(this, "DATABASE FILLED! Added 70 records.", Toast.LENGTH_LONG).show();
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Fill Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}