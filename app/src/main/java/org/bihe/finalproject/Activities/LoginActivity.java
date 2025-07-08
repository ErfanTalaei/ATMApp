package org.bihe.finalproject.Activities;

import static org.bihe.finalproject.preferences.PreferencesManager.PREF_KEY_CURRENT_USER;
import static org.bihe.finalproject.preferences.PreferencesManager.PREF_KEY_LOGGED;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.bihe.finalproject.R;
import org.bihe.finalproject.data.db.runnables.GetUserRunnable;
import org.bihe.finalproject.data.db.runnables.InsertUserRunnable;
import org.bihe.finalproject.databinding.ActivityLoginBinding;
import org.bihe.finalproject.model.User;
import org.bihe.finalproject.network.impl.UserServiceImpl;
import org.bihe.finalproject.preferences.PreferencesManager;
import org.bihe.finalproject.utils.ResultListener;
import org.bihe.finalproject.utils.Validator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private PreferencesManager preferencesManager;
    private ExecutorService executorService;
    private UserServiceImpl userService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        executorService = Executors.newCachedThreadPool();
        userService = new UserServiceImpl();

        setListeners();
        preferencesManager = PreferencesManager.getInstance(this);

    }

    private void setListeners() {
        binding.signUpBtn.setOnClickListener(v -> onSignUpClicked());
        binding.signInBtn.setOnClickListener(v -> onLoginClicked());
    }

    private void onSignUpClicked() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void onLoginClicked() {
        removeErrors();
        String username = binding.userNameEdt.getText().toString().trim();
        String password = binding.passwordEdt.getText().toString().trim();

        if (isInputValid(username, password)) {
            loginIfAccountExist(username, password);
        }
    }

    private void removeErrors() {
        binding.userNameEdt.setError(null);
        binding.passwordEdt.setError(null);
    }

    private boolean isInputValid(String username, String password) {
        boolean isValid = true;

        if (username.isEmpty()) {
            binding.userNameEdt.setError(getString(R.string.error_invalid_user_name));
            isValid = false;
        }

        if (!Validator.isPasswordValid(password)) {
            binding.passwordEdt.setError(getString(R.string.error_invalid_password));
            isValid = false;
        }

        return isValid;
    }

    private void loginIfAccountExist(String username, String password) {

        executorService.execute(new GetUserRunnable(getApplicationContext(), username, password, new ResultListener<User>() {
            @Override
            public void onSuccess(User user) {
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, getString(R.string.message_successful), Toast.LENGTH_SHORT).show();
                    login(user);
                });
            }

            @Override
            public void onError(Throwable throwable) {
                runOnUiThread(() -> {
                    logInProcess(username, password);
                });
            }
        }));
    }

    private void logInProcess(String username, String password) {
        userService.loginUser(username, password, new ResultListener<User>() {
            @Override
            public void onSuccess(User user) {
                insertToDatabase(user);
            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(LoginActivity.this, R.string.error_invalid_login, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertToDatabase(User user) {
        executorService.execute(
                new InsertUserRunnable(getApplicationContext(), user, new ResultListener<User>() {
                    @Override
                    public void onSuccess(User user) {
                        runOnUiThread(() -> login(user));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, R.string.error_invalid_login, Toast.LENGTH_SHORT).show());
                    }
                })
        );
    }

    private void login(User user) {
        preferencesManager.put(PREF_KEY_LOGGED, true);
        preferencesManager.putObj(PREF_KEY_CURRENT_USER, user);
        Intent intent = new Intent(LoginActivity.this, TransactionActivity.class);
        startActivity(intent);
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}