package com.example.pr20_rmp_gudochkina;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edName, edSName, edMail, edPhone, edAge, edCity;

    private Button btnSave, btnRead;

    private TextView tvOutput;

    private DatabaseReference myDataBase;
    private final String USER_KEY = "Users";

    private static final String TAG = "FirebaseDB";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        myDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

        btnSave.setOnClickListener(this);
        btnRead.setOnClickListener(this);
    }
    private void initViews() {
        edName = findViewById(R.id.edName);
        edSName = findViewById(R.id.edSName);
        edMail = findViewById(R.id.edMail);
        edPhone = findViewById(R.id.edPhone);
        edAge = findViewById(R.id.edAge);
        edCity = findViewById(R.id.edCity);

        btnSave = findViewById(R.id.btnSave);
        btnRead = findViewById(R.id.btnRead);

        tvOutput = findViewById(R.id.tvOutput);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSave) {
            saveDataToFirebase();
        } else if (v.getId() == R.id.btnRead) {
            readDataFromFirebase();
        }
    }

    private void saveDataToFirebase() {
        String name = edName.getText().toString().trim();
        String surname = edSName.getText().toString().trim();
        String email = edMail.getText().toString().trim();
        String phone = edPhone.getText().toString().trim();
        String ageStr = edAge.getText().toString().trim();
        String city = edCity.getText().toString().trim();

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() ||
                phone.isEmpty() || ageStr.isEmpty() || city.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля!", Toast.LENGTH_SHORT).show();
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Возраст должен быть числом!", Toast.LENGTH_SHORT).show();
            return;
        }

        User newUser = new User(name, surname, email, phone, age, city);

        myDataBase.push().setValue(newUser)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(MainActivity.this, "Данные успешно сохранены в Firebase!", Toast.LENGTH_SHORT).show();
                    clearInputFields();
                    Log.d(TAG, "Данные пользователя сохранены: " + newUser.toString());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Ошибка сохранения: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Ошибка сохранения данных", e);
                });
    }

    private void readDataFromFirebase() {
        tvOutput.setText("Загрузка данных из Firebase...");

        myDataBase.limitToLast(1).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot dataSnapshot = task.getResult();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            tvOutput.setText(user.toString());
                        } else {
                            tvOutput.setText("Не удалось прочитать данные пользователя.");
                        }
                    }
                } else {
                    tvOutput.setText("В базе данных Firebase нет записей.\nНажмите 'Сохранить' чтобы добавить.");
                }
            } else {
                tvOutput.setText("Ошибка загрузки данных: " + task.getException() +
                        "\nПроверьте подключение к Firebase.");
                Log.e(TAG, "Ошибка чтения данных", task.getException());
            }
        });
    }

    private void clearInputFields() {
        edName.setText("");
        edSName.setText("");
        edMail.setText("");
        edPhone.setText("");
        edAge.setText("");
        edCity.setText("");
        edName.requestFocus();
    }
}