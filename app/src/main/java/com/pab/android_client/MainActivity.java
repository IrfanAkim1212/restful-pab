package com.pab.android_client;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private final List<Users> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userAdapter = new UserAdapter(userList, this);
        recyclerView.setAdapter(userAdapter);

        findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddUserDialog();
            }
        });

        fetchUsers();
    }

    private void showAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add User");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_user, null);
        final EditText editTextName = view.findViewById(R.id.editTextName);
        final EditText editTextEmail = view.findViewById(R.id.editTextEmail);
        final EditText editTextNim = view.findViewById(R.id.editTextNim);
        final EditText editTextKelas = view.findViewById(R.id.editTextKelas);

        builder.setView(view);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String nim = editTextNim.getText().toString();
                String kelas = editTextKelas.getText().toString();
                addUser(name, email, nim, kelas);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void updateUser(Users user) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.updateUser(user.getId(), user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Berhasil mengupdate user
                    Toast.makeText(MainActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                    // Refresh data setelah pembaruan
                    fetchUsers(); // Gantilah dengan metode yang Anda gunakan untuk mengambil data
                } else {
                    // Gagal mengupdate user
                    Toast.makeText(MainActivity.this, "Failed to update user: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Gagal melakukan request ke server
                Toast.makeText(MainActivity.this, "Failed to update user: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addUser(String name, String email, String nim, String kelas) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Users user = new Users(name, email, nim, kelas);
        Call<Void> call = apiService.insertUser(user);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "User added successfully", Toast.LENGTH_SHORT).show();
                    fetchUsers();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to add user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUsers() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Users>> call = apiService.getUsers();

        call.enqueue(new Callback<List<Users>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                if (response.isSuccessful()) {
                    userList.clear();
                    userList.addAll(response.body());
                    userAdapter.notifyDataSetChanged();
                } else {
                    Log.e("MainActivity", "Response error: " + response.errorBody().toString());
                    Toast.makeText(MainActivity.this, "Failed to fetch users: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Log.e("MainActivity", "Fetch error: ", t);
                Toast.makeText(MainActivity.this, "Failed to fetch users: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUser(int id, String name, String email, String nim, String kelas) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Users user = new Users(id, name, email, nim, kelas);
        Call<Void> call = apiService.updateUser(id, user);

        Log.d("MainActivity", "Updating user: " + id + ", " + name + ", " + email + ", " + nim + ", " + kelas);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("MainActivity", "User updated successfully");
                    Toast.makeText(MainActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                    fetchUsers();
                } else {
                    Log.e("MainActivity", "Response error: " + response.errorBody().toString());
                    Toast.makeText(MainActivity.this, "Failed to update user: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("MainActivity", "Update error: ", t);
                Toast.makeText(MainActivity.this, "Failed to update user: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showUpdateDialog(final Users user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update User");
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_update_user, (ViewGroup) findViewById(android.R.id.content), false);
        final EditText inputName = viewInflated.findViewById(R.id.editTextName);
        final EditText inputEmail = viewInflated.findViewById(R.id.editTextEmail);
        final EditText inputNim = viewInflated.findViewById(R.id.editTextNim);
        final EditText inputKelas = viewInflated.findViewById(R.id.editTextKelas);

        inputName.setText(user.getName());
        inputEmail.setText(user.getEmail());
        inputNim.setText(user.getNim());
        inputKelas.setText(user.getKelas());

        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String name = inputName.getText().toString();
                String email = inputEmail.getText().toString();
                String nim = inputNim.getText().toString();
                String kelas = inputKelas.getText().toString();
                updateUser(user.getId(), name, email, nim, kelas);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    public void refreshData() {
        fetchUsers();
    }
}
