package org.bihe.finalproject.Activities;

import static org.bihe.finalproject.preferences.PreferencesManager.PREF_KEY_CURRENT_USER;
import static org.bihe.finalproject.preferences.PreferencesManager.PREF_KEY_LOGGED;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.bihe.finalproject.R;
import org.bihe.finalproject.adapters.TransactionAdapter;
import org.bihe.finalproject.data.db.runnables.DeleteTransactionRunnable;
import org.bihe.finalproject.data.db.runnables.GetTransactionsByUserNameRunnable;
import org.bihe.finalproject.databinding.ActivityProfileBinding;
import org.bihe.finalproject.model.Transaction;
import org.bihe.finalproject.model.User;
import org.bihe.finalproject.network.impl.TransactionServiceImpl;
import org.bihe.finalproject.preferences.PreferencesManager;
import org.bihe.finalproject.utils.ResultListener;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private TransactionAdapter adapter;
    private ExecutorService executorService;
    private TransactionServiceImpl transactionService;
    private User user;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        executorService = Executors.newCachedThreadPool();
        transactionService = new TransactionServiceImpl();

        preferencesManager = PreferencesManager.getInstance(this);
        setSupportActionBar(binding.toolbar);

        user = preferencesManager.getObj(PREF_KEY_CURRENT_USER, User.class);

        setProfile();
        getTransactionBasedOnUserName();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

    private void setProfile() {

        String userName = user.getUsername();
        String accountNum = user.getAccountNum();
        String cardNum = user.getCardNum();
        String cvv2 = user.getCvv2();
        float balance = user.getBalance();

        binding.userNameTv2.setText(userName);
        binding.accountNumberTv2.setText(accountNum);
        binding.cardNumberTv2.setText(cardNum);
        binding.cvv2Tv2.setText(cvv2);
        binding.currentBalanceTv2.setText(String.valueOf(balance));
    }

    private void getTransactionBasedOnUserName() {
        executorService.execute(new GetTransactionsByUserNameRunnable(getApplicationContext(), user.getUsername(), new ResultListener<List<Transaction>>() {
                    @Override
                    public void onSuccess(List<Transaction> dbTransactions) {
                        runOnUiThread(() -> {
                            adapter = new TransactionAdapter(ProfileActivity.this, dbTransactions, new TransactionAdapter.TransactionAdapterCallback() {

                                @Override
                                public void onItemClicked(int position) {
                                }

                                @Override
                                public void onItemLongClicked(Transaction transaction) {
                                    openDeleteDialog(transaction);
                                }
                            });
                            adapter.updateAll(dbTransactions);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));
                            binding.recyclerView.setAdapter(adapter);
                        });
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        runOnUiThread(() -> {
                            Toast.makeText(ProfileActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                        });
                    }
                })
        );
    }

    private void openDeleteDialog(Transaction transaction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_title);
        builder.setMessage(R.string.delete_warning);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> deleteTransaction(transaction));
        builder.setNegativeButton(R.string.no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteTransaction(Transaction transaction) {
        transactionService.deleteTransaction(transaction, new ResultListener<Transaction>() {
            @Override
            public void onSuccess(Transaction transaction) {
                executorService.execute(
                        new DeleteTransactionRunnable(getApplicationContext(), transaction, new ResultListener<Transaction>() {
                            @Override
                            public void onSuccess(Transaction transaction) {
                                runOnUiThread(() -> {
                                    adapter.remove(transaction);
                                    Toast.makeText(ProfileActivity.this, R.string.message_successful, Toast.LENGTH_SHORT).show();
                                });
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                runOnUiThread(() -> {
                                    Toast.makeText(ProfileActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                            }

                        })
                );
            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(ProfileActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        preferencesManager.put(PREF_KEY_LOGGED, false);

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}