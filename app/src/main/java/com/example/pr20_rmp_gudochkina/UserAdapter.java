package com.example.pr20_rmp_gudochkina;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private final List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.tvUserName.setText(String.format(holder.itemView.getContext().getString(R.string.format_name), user.getName()));
        holder.tvUserSurname.setText(String.format(holder.itemView.getContext().getString(R.string.format_surname), user.getSurname()));
        holder.tvUserEmail.setText(String.format(holder.itemView.getContext().getString(R.string.format_email), user.getEmail()));
        holder.tvUserPhone.setText(String.format(holder.itemView.getContext().getString(R.string.format_phone), user.getPhone()));
        holder.tvUserAge.setText(String.format(holder.itemView.getContext().getString(R.string.format_age), user.getAge()));
        holder.tvUserCity.setText(String.format(holder.itemView.getContext().getString(R.string.format_city), user.getCity()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void addUser(User user) {
        userList.add(user);
        notifyItemInserted(userList.size() - 1);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserSurname, tvUserEmail, tvUserPhone, tvUserAge, tvUserCity;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserSurname = itemView.findViewById(R.id.tvUserSurname);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvUserPhone = itemView.findViewById(R.id.tvUserPhone);
            tvUserAge = itemView.findViewById(R.id.tvUserAge);
            tvUserCity = itemView.findViewById(R.id.tvUserCity);
        }
    }
}