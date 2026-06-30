package com.example.pr20_rmp_gudochkina;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edName, edSName, edMail, edPhone, edAge, edCity;

    private Button btnSave;

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;

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
        setupFirebaseListener();
    }
    private void initViews() {
        edName = findViewById(R.id.edName);
        edSName = findViewById(R.id.edSName);
        edMail = findViewById(R.id.edMail);
        edPhone = findViewById(R.id.edPhone);
        edAge = findViewById(R.id.edAge);
        edCity = findViewById(R.id.edCity);

        btnSave = findViewById(R.id.btnSave);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList);
        recyclerView.setAdapter(userAdapter);
    }

    private void setupFirebaseListener() {
        myDataBase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    userAdapter.addUser(user);
                    Log.d(TAG, getString(R.string.log_user_added, user.toString()));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, getString(R.string.log_firebase_listener_error), error.toException());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSave) {
            saveDataToFirebase();
        }
    }

    private void saveDataToFirebase() {
        String name = edName.getText().toString().trim();
        String surname = edSName.getText().toString().trim();
        String email = edMail.getText().toString().trim();
        String phone = edPhone.getText().toString().trim();
        String ageStr = edAge.getText().toString().trim();
        String city = edCity.getText().toString().trim();

        // Валидация всех полей
        String errorMessage = validateInput(name, surname, email, phone, ageStr, city);
        if (errorMessage != null) {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        User newUser = new User(name, surname, email, phone, age, city);

        myDataBase.push().setValue(newUser)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(MainActivity.this, R.string.success_save, Toast.LENGTH_SHORT).show();
                    clearInputFields();
                    Log.d(TAG, getString(R.string.log_user_saved, newUser.toString()));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this,
                            getString(R.string.error_save, e.getMessage()),
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, getString(R.string.log_save_data_error), e);
                });
    }

    private String validateInput(String name, String surname, String email, String phone, String ageStr, String city) {
        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() ||
                phone.isEmpty() || ageStr.isEmpty() || city.isEmpty()) {
            return getString(R.string.error_fill_all_fields);
        }

        if (isInvalidText(name)) {
            return getString(R.string.error_invalid_name);
        }

        if (isInvalidText(surname)) {
            return getString(R.string.error_invalid_surname);
        }

        if (!isValidEmail(email)) {
            return getString(R.string.error_invalid_email);
        }

        if (!isValidPhone(phone)) {
            return getString(R.string.error_invalid_phone);
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            return getString(R.string.error_age_not_number);
        }

        if (!isValidAge(age)) {
            return getString(R.string.error_invalid_age);
        }

        if (isInvalidText(city)) {
            return getString(R.string.error_invalid_city);
        }

        return null;
    }

    private boolean isInvalidText(String text) {
        String trimmed = text.trim();
        return trimmed.length() < 2 || trimmed.length() > 35
                || !trimmed.matches("^[A-Za-zА-Яа-яЁё\\s\\-]+$");
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private boolean isValidAge(int age) {
        return age >= 1 && age <= 120;
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