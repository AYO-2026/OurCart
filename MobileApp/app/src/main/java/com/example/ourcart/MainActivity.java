package com.example.ourcart;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ourcart.data.AppDatabase;
import com.example.ourcart.data.dao.CartItemDao;
import com.example.ourcart.data.entities.CartItem;
import com.example.ourcart.data.entities.Unit;

import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    // Змінні для наших елементів інтерфейсу
    private EditText editTextName;
    private EditText editTextQuantity;
    private Spinner spinnerUnit;
    private Button buttonAdd;

    private CartItemDao cartItemDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        buttonAdd = findViewById(R.id.buttonAdd);

        AppDatabase db = AppDatabase.getInstance(this);
        cartItemDao = db.cartItemDao();

        buttonAdd.setOnClickListener(v -> saveItemToDatabase());
    }

    private void saveItemToDatabase() {
        // Зчитуємо текст і видаляємо зайві пробіли по краях
        String name = editTextName.getText().toString().trim();
        String quantityStr = editTextQuantity.getText().toString().trim();
        String selectedUnitStr = spinnerUnit.getSelectedItem().toString();

        // Базова перевірка: якщо користувач нічого не ввів, показуємо попередження
        if (name.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(this, "Будь ласка, заповніть всі поля", Toast.LENGTH_SHORT).show();
            return;
        }

        // Перетворюємо текст у потрібні типи даних
        double quantity = Double.parseDouble(quantityStr);
        Unit unit = Unit.valueOf(selectedUnitStr); // Перетворюємо текст зі спінера в наш Enum

        // Створюємо новий товар
        CartItem item = new CartItem();
        item.name = name;
        item.quantity = quantity;
        item.unit = unit;
        item.isBought = false;

        // Зберігаємо в базу даних. Це ОБОВ'ЯЗКОВО треба робити у фоновому потоці
        Executors.newSingleThreadExecutor().execute(() -> {
            cartItemDao.insert(item);

            // Коли дані збережено, повертаємось у головний потік, щоб оновити інтерфейс
            runOnUiThread(() -> {
                // Показуємо маленьке повідомлення внизу екрана
                Toast.makeText(this, "Товар додано!", Toast.LENGTH_SHORT).show();

                // Очищаємо поля для наступного товару
                editTextName.setText("");
                editTextQuantity.setText("");
                editTextName.requestFocus(); // Повертаємо курсор у перше поле
            });
        });
    }
}