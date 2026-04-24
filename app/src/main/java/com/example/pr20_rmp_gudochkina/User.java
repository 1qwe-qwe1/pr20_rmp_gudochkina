package com.example.pr20_rmp_gudochkina;

import androidx.annotation.NonNull;

public class User {
    private String name;
    private String surname;
    private String email;
    private String phone;
    private int age;
    private String city;


    public User() {
    }

    public User(String name, String surname, String email, String phone, int age, String city) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.city = city;
    }


    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public int getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @NonNull
    @Override
    public String toString() {
        return "Имя: " + name + "\n" +
                "Фамилия: " + surname + "\n" +
                "Email: " + email + "\n" +
                "Телефон: " + phone + "\n" +
                "Возраст: " + age + "\n" +
                "Город: " + city;
    }
}
