package org.bihe.finalproject.Activities;

import static org.bihe.finalproject.preferences.PreferencesManager.PREF_KEY_CURRENT_USER;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.bihe.finalproject.R;
import org.bihe.finalproject.adapters.TransactionAdapter;
import org.bihe.finalproject.data.db.runnables.InsertTransactionRunnable;
import org.bihe.finalproject.databinding.ActivityAddTransactionBinding;
import org.bihe.finalproject.model.Transaction;
import org.bihe.finalproject.model.User;
import org.bihe.finalproject.network.impl.TransactionServiceImpl;
import org.bihe.finalproject.preferences.PreferencesManager;
import org.bihe.finalproject.utils.Constant;
import org.bihe.finalproject.utils.ResultListener;
import org.bihe.finalproject.utils.Validator;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddTransactionActivity extends AppCompatActivity {
    private ActivityAddTransactionBinding binding;
    private ExecutorService executorService;
    private TransactionServiceImpl transactionService;
    private PreferencesManager preferencesManager;
    private User user;
    private Transaction transaction;
    private FloatingActionButton fab;
    private TransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTransactionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        fab = binding.fab;

        executorService = Executors.newCachedThreadPool();
        transactionService = new TransactionServiceImpl();
        transactionAdapter = new TransactionAdapter(AddTransactionActivity.this, new ArrayList<>());
        preferencesManager = PreferencesManager.getInstance(this);
        user = preferencesManager.getObj(PREF_KEY_CURRENT_USER, User.class);

        String transactionType = getIntent().getStringExtra(Constant.KEY_TRANSACTION_TYPE);
        setUpToolbar(transactionType);
        setUpForm(transactionType);
        getIntentData();
        if (transaction != null) {
            preFillData(transactionType);
        }
        setListeners(transactionType);
    }

    private void setUpToolbar(String transactionType) {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setTitle(transactionType);
    }

    private void setListeners(String transactionType) {
        binding.addBtn.setOnClickListener(view -> {
            addTransaction(transactionType);
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTransaction(transaction, transactionType);
            }
        });
    }

    private void addTransaction(String transactionType) {
        removeErrors();
        transaction = createTransactionObjectFromInputs(transactionType);
        if (isTransactionValid(transactionType)) {
            addingProcess(transaction);
        }
    }

    private Transaction createTransactionObjectFromInputs(String transactionType) {

        if ("CTC".equals(transactionType)) {

            binding.ctcOriginAccountNumEdt.setText(user.getAccountNum());
            String desCardNumb = binding.destinationCardNumEdt.getText().toString();
            String cvv2 = binding.cvv2Edt.getText().toString();
            String expirationDate = binding.expirationDateEdt.getText().toString();
            String stringAmount = binding.ctcAmountEdt.getText().toString().trim();
            float amount = TextUtils.isEmpty(stringAmount) ? -1 : Float.parseFloat(stringAmount);

            return new Transaction(user.getUsername(), transactionType, amount, user.getAccountNum(), desCardNumb, cvv2, expirationDate);
        } else {
            String stringAmount = binding.withdrawAmountEdt.getText().toString();
            float amount = TextUtils.isEmpty(stringAmount) ? -1 : Float.parseFloat(stringAmount);

            return new Transaction(user.getUsername(), transactionType, amount);
        }
    }

    private void addingProcess(Transaction transaction) {
        transactionService.insertTransaction(transaction, new ResultListener<Transaction>() {
            @Override
            public void onSuccess(Transaction dbTransaction) {
                insertToDatabase(dbTransaction);
            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(AddTransactionActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpForm(String transactionType) {
        if ("CTC".equals(transactionType)) {
            ConstraintLayout ctcLay = binding.ctcLay;
            ctcLay.setVisibility(View.VISIBLE);
        } else {
            ConstraintLayout ctcLay = binding.withdrawLay;
            ctcLay.setVisibility(View.VISIBLE);
        }
    }

    private void insertToDatabase(Transaction transaction) {
        executorService.execute(new InsertTransactionRunnable(getApplicationContext(), transaction, new ResultListener<Transaction>() {
            @Override
            public void onSuccess(Transaction dbTransactions) {
                runOnUiThread(() -> {
                    transactionAdapter.insert(dbTransactions);
                });
                Intent intent = new Intent(AddTransactionActivity.this, TransactionActivity.class);
                startActivity(intent);
                setResult();
            }

            @Override
            public void onError(Throwable throwable) {
                runOnUiThread(() -> {
                    Toast.makeText(AddTransactionActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }));
    }

    private void getIntentData() {
        transaction = (Transaction) getIntent().getSerializableExtra(Constant.KEY_TRANSACTION);
    }

    private void preFillData(String transactionType) {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setTitle(R.string.preview);
        binding.addBtn.setVisibility(View.INVISIBLE);

        if ("CTC".equals(transactionType)) {

            ConstraintLayout ctcLay = binding.ctcLay;
            ctcLay.setVisibility(View.VISIBLE);
            binding.ctcTransactionDateTv.setVisibility(View.VISIBLE);
            binding.ctcTransactionDateEdt.setVisibility(View.VISIBLE);
            binding.ctcTypeTv.setVisibility(View.VISIBLE);
            binding.ctcTypeEdt.setVisibility(View.VISIBLE);
            ctcLay.setEnabled(false);
            for (int i = 0; i < ctcLay.getChildCount(); i++) {
                View child = ctcLay.getChildAt(i);
                child.setEnabled(false);
            }

            binding.ctcOriginAccountNumEdt.setText(user.getAccountNum());
            binding.destinationCardNumEdt.setText(transaction.getDestinationCardNum());
            binding.cvv2Edt.setText(transaction.getCvv2());
            binding.expirationDateEdt.setText(user.getExpirationDate());
            binding.ctcAmountEdt.setText(String.valueOf(transaction.getAmount()));
            binding.ctcTransactionDateEdt.setText(transaction.getFormattedModifiedDate());
            binding.ctcTypeEdt.setText(transaction.getType());

        } else {
            ConstraintLayout withdrawLay = binding.withdrawLay;
            withdrawLay.setVisibility(View.VISIBLE);
            binding.withdrawTransactionDateTv.setVisibility(View.VISIBLE);
            binding.withdrawTransactionDateEdt.setVisibility(View.VISIBLE);
            binding.withdrawTypeTv.setVisibility(View.VISIBLE);
            binding.withdrawTypeEdt.setVisibility(View.VISIBLE);
            binding.withdrawAmountEdt.setVisibility(View.VISIBLE);
            binding.passwordTv.setVisibility(View.INVISIBLE);
            binding.passwordEdt.setVisibility(View.INVISIBLE);
            withdrawLay.setEnabled(false);
            for (int i = 0; i < withdrawLay.getChildCount(); i++) {
                View child = withdrawLay.getChildAt(i);
                child.setEnabled(false);
            }

            binding.withdrawAmountEdt.setText(String.valueOf(transaction.getAmount()));
            binding.withdrawTypeEdt.setText(transaction.getType());
            binding.withdrawTransactionDateEdt.setText(transaction.getFormattedModifiedDate());
        }

        fab.setVisibility(View.VISIBLE);
    }

    private void removeErrors() {
        binding.ctcOriginAccountNumEdt.setError(null);
        binding.destinationCardNumEdt.setError(null);
        binding.cvv2Edt.setError(null);
        binding.expirationDateEdt.setError(null);
        binding.ctcAmountEdt.setError(null);
        binding.withdrawAmountEdt.setError(null);
        binding.passwordEdt.setError(null);
        binding.cvv2Edt.setError(null);
    }

    private boolean isTransactionValid(String transactionType) {
        boolean isValid = true;

        if (transactionType.equals("CTC")) {

            if (!Validator.isAccountNumValid(transaction.getOriginAccountNum())) {
                binding.ctcOriginAccountNumEdt.setError(getString(R.string.error_invalid_account_num));
                isValid = false;
            }

            if (!user.getAccountNum().equals(transaction.getOriginAccountNum())) {
                binding.ctcOriginAccountNumEdt.setError(getString(R.string.error_unique_account_num));
                isValid = false;
            }

            if (!Validator.isCardNumValid(transaction.getDestinationCardNum())) {
                binding.destinationCardNumEdt.setError(getString(R.string.error_invalid_card_num));
                isValid = false;
            }

            if (!Validator.isCvv2Valid(transaction.getCvv2())) {
                binding.cvv2Edt.setError(getString(R.string.error_invalid_cvv2));
                isValid = false;
            }

            if (!user.getCvv2().equals(transaction.getCvv2())) {
                binding.cvv2Edt.setError(getString(R.string.error_unique_cvv2));
                isValid = false;
            }

            if (TextUtils.isEmpty(user.getExpirationDate())) {
                binding.expirationDateEdt.setError(getString(R.string.error_invalid_expiration_date));
                isValid = false;
            }

            if (!user.getExpirationDate().equals(transaction.getExpirationDate())) {
                binding.expirationDateEdt.setError(getString(R.string.error_unique_expiration_date));
                isValid = false;
            }

            if (!Validator.isBalanceOrAmountValid(transaction.getAmount())) {
                binding.ctcAmountEdt.setError(getString(R.string.error_invalid_amount));
                isValid = false;
            }

            if (user.getBalance() <= transaction.getAmount()) {
                binding.ctcAmountEdt.setError(getString(R.string.error_enough_balance));
                isValid = false;
            }
        } else {
            if (!Validator.isBalanceOrAmountValid(transaction.getAmount())) {
                binding.withdrawAmountEdt.setError(getString(R.string.error_invalid_amount));
                isValid = false;
            }

            if (user.getBalance() <= transaction.getAmount()) {
                binding.withdrawAmountEdt.setError(getString(R.string.error_enough_balance));
                isValid = false;
            }
            String transactionPassword = binding.passwordEdt.getText().toString().trim();
            if (!Validator.isPasswordValid(transactionPassword)) {
                binding.passwordEdt.setError(getString(R.string.error_invalid_password));
                isValid = false;
            }

            if (!user.getPassword().equals(transactionPassword)) {
                binding.passwordEdt.setError(getString(R.string.error_unique_password));
                isValid = false;
            }
        }
        return isValid;
    }

    private void shareTransaction(Transaction transaction, String transactionType) {
        String txtTransaction;
        if (transactionType.equals("CTC")) {
            txtTransaction = transaction.toString();
        } else {
            txtTransaction = transaction.toStringWithdraw();
        }
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, txtTransaction);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_transaction_txt)));
    }

    private void setResult() {
        Intent intent = new Intent();
        intent.putExtra(Constant.KEY_TRANSACTION, transaction);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        executorService.shutdown();
    }
}

