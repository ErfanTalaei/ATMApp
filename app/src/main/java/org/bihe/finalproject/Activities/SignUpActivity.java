package org.bihe.finalproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.bihe.finalproject.R;
import org.bihe.finalproject.databinding.ActivitySignUpBinding;
import org.bihe.finalproject.model.User;
import org.bihe.finalproject.network.impl.UserServiceImpl;
import org.bihe.finalproject.utils.ResultListener;
import org.bihe.finalproject.utils.Validator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private User user;
    private ExecutorService executorService;
    private UserServiceImpl userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        executorService = Executors.newCachedThreadPool();
        userService = new UserServiceImpl();

        setListeners();
        setUpToolbar();
    }

    private void setListeners() {
        binding.signUpBtn.setOnClickListener(view -> {
            signUpUser();
        });
    }

    private void signUpUser() {
        removeErrors();
        user = createUserObjectFromInputs();
        if (isUserValid())
            signUpProcess(user);
    }

    private User createUserObjectFromInputs() {
        String userName = binding.userNameEdt.getText().toString().trim();
        String password = binding.passwordEdt.getText().toString().trim();
        String ageText = binding.ageEdt.getText().toString().trim();
        int age = TextUtils.isEmpty(ageText) ? -1 : Integer.parseInt(ageText);
        String phone = binding.phoneEdt.getText().toString().trim();
        String accountNumber = binding.accountNumberEdt.getText().toString().trim();
        String cardNumber = binding.cardNumberEdt.getText().toString().trim();
        String cvv2 = binding.cvv2Edt.getText().toString().trim();
        String expirationDate = binding.expirationDateEdt.getText().toString().trim();
        String balanceText = binding.currentBalanceEdt.getText().toString().trim();
        float balance = TextUtils.isEmpty(balanceText) ? -1 : Float.parseFloat(balanceText);
        return new User(userName, password, age, phone, accountNumber, cardNumber, cvv2, expirationDate, balance);
    }

    private boolean isUserValid() {
        boolean isValid = true;

        if (TextUtils.isEmpty(user.getUsername())) {
            binding.userNameEdt.setError(getString(R.string.error_invalid_user_name));
            isValid = false;
        }

        if (!Validator.isPasswordValid(user.getPassword())) {
            binding.passwordEdt.setError(getString(R.string.error_invalid_password));
            isValid = false;
        }

        String confirmPassword = binding.confirmPasswordEdt.getText().toString().trim();
        if (!Validator.isPasswordValid(confirmPassword)) {
            binding.confirmPasswordEdt.setError(getString(R.string.error_invalid_password));
            isValid = false;
        }

        if (!user.getPassword().equals(confirmPassword)) {
            binding.confirmPasswordEdt.setError(getString(R.string.error_unique_password));
            isValid = false;
        }

        if (!Validator.isAgeValid(user.getAge())) {
            binding.ageEdt.setError(getString(R.string.error_invalid_age));
            isValid = false;
        }

        if (!Validator.isPhoneValid(user.getPhone())) {
            binding.phoneEdt.setError(getString(R.string.error_invalid_phone));
            isValid = false;
        }

        if (!Validator.isAccountNumValid(user.getAccountNum())) {
            binding.accountNumberEdt.setError(getString(R.string.error_invalid_account_num));
            isValid = false;
        }

        if (!Validator.isCardNumValid(user.getCardNum())) {
            binding.cardNumberEdt.setError(getString(R.string.error_invalid_card_num));
            isValid = false;
        }

        if (!Validator.isCvv2Valid(user.getCvv2())) {
            binding.cvv2Edt.setError(getString(R.string.error_invalid_cvv2));
            isValid = false;
        }

        if (TextUtils.isEmpty(user.getExpirationDate())) {
            binding.expirationDateEdt.setError(getString(R.string.error_invalid_expiration_date));
            isValid = false;
        }

        if (!Validator.isBalanceOrAmountValid(user.getBalance())) {
            binding.currentBalanceEdt.setError(getString(R.string.current_balance));
            isValid = false;
        }


        return isValid;
    }

    private void signUpProcess(User user) {
        userService.signUpUser(user, new ResultListener<User>() {
            @Override
            public void onSuccess(User t) {
                user.setSessionToken(user.getSessionToken());
                user.setId(t.getId());
                Toast.makeText(SignUpActivity.this, R.string.message_successful, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(SignUpActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeErrors() {
        binding.userNameEdt.setError(null);
        binding.passwordEdt.setError(null);
        binding.confirmPasswordEdt.setError(null);
        binding.ageEdt.setError(null);
        binding.phoneEdt.setError(null);
        binding.accountNumberEdt.setError(null);
        binding.cardNumberEdt.setError(null);
        binding.cvv2Edt.setError(null);
        binding.expirationDateEdt.setError(null);
        binding.currentBalanceEdt.setError(null);
    }

    private void setUpToolbar() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}