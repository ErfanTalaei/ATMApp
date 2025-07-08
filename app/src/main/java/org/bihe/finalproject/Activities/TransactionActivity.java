package org.bihe.finalproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.bihe.finalproject.R;
import org.bihe.finalproject.adapters.TransactionAdapter;
import org.bihe.finalproject.data.db.runnables.GetAllTransactionsRunnable;
import org.bihe.finalproject.databinding.ActivityTransactionBinding;
import org.bihe.finalproject.model.Transaction;
import org.bihe.finalproject.preferences.PreferencesManager;
import org.bihe.finalproject.utils.Constant;
import org.bihe.finalproject.utils.ResultListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionActivity extends AppCompatActivity {

    private ActivityTransactionBinding binding;
    private PreferencesManager preferencesManager;
    private TransactionAdapter adapter;
    private List<Transaction> transactions = new ArrayList<>();
    private ExecutorService executorService;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;


    private final ActivityResultLauncher<Intent> transactionActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        adapter.update();
                    }
                }
            });
    private final ActivityResultLauncher<Intent> addTransactionActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        adapter.itemInsertedOnTop();
                        binding.recyclerView.smoothScrollToPosition(0);
                    }
                }
            });

    private final ActivityResultLauncher<Intent> profileActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        setUpRecyclerView();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        executorService = Executors.newCachedThreadPool();

        bottomNavigationView = binding.bottomNavigationView;
        fab = binding.fab;

        preferencesManager = PreferencesManager.getInstance(this);
        setSupportActionBar(binding.toolbar);

        setListeners();
        setUpRecyclerView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

    private void setListeners() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.transaction) {
                goToTransactionScreen();
                return true;
            } else if (item.getItemId() == R.id.profile) {
                goToProfileScreen();
                return true;
            }
            return false;
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenAddDialogBox();
            }
        });

    }

    private void setUpRecyclerView() {
        executorService.execute(new GetAllTransactionsRunnable(getApplicationContext(), new ResultListener<List<Transaction>>() {
            @Override
            public void onSuccess(List<Transaction> dbTransactions) {
                runOnUiThread(() -> {
                    transactions = dbTransactions;
                    adapter = new TransactionAdapter(TransactionActivity.this, transactions, new TransactionAdapter.TransactionAdapterCallback() {

                        @Override
                        public void onItemClicked(int position) {
                            goToPreviewScreen(position);
                        }

                        @Override
                        public void onItemLongClicked(Transaction transaction) {
                        }
                    });
                    binding.recyclerView.setLayoutManager(new GridLayoutManager(TransactionActivity.this, 1));
                    adapter.updateAll(dbTransactions);
                    binding.recyclerView.setAdapter(adapter);
                    adapter.update();
                });
            }

            @Override
            public void onError(Throwable throwable) {
                runOnUiThread(() -> Toast.makeText(TransactionActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }));
    }

    private void goToTransactionScreen() {
        Intent intent = new Intent(this, TransactionActivity.class);
        transactionActivityResultLauncher.launch(intent);
    }

    private void goToProfileScreen() {
        Intent intent = new Intent(this, ProfileActivity.class);
        profileActivityResultLauncher.launch(intent);
    }

    private void goToPreviewScreen(int position) {
        Intent intent = new Intent(this, AddTransactionActivity.class);
        Transaction transaction = transactions.get(position);
        if (transaction == null) {
            return;
        }
        if (transaction.getType().equals("CTC")) {
            intent.putExtra(Constant.KEY_TRANSACTION_TYPE, getString(R.string.ctc));
        } else {
            intent.putExtra(Constant.KEY_TRANSACTION_TYPE, getString(R.string.withdraw));
        }
        intent.putExtra(Constant.KEY_TRANSACTION, transactions.get(position));
        addTransactionActivityResultLauncher.launch(intent);
    }

    private void OpenAddDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.transaction_type);
        View view = getLayoutInflater().inflate(R.layout.dialog_box, null);
        Spinner spinner = view.findViewById(R.id.transaction_type_sp);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.arrays_type, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        builder.setPositiveButton(R.string.accept, (dialog, which) -> {
            Intent intent = new Intent(this, AddTransactionActivity.class);
            if (spinner.getSelectedItem().toString().equals("CTC")) {
                intent.putExtra(Constant.KEY_TRANSACTION_TYPE, getString(R.string.ctc));
            } else if (spinner.getSelectedItem().toString().equals("Withdraw")) {
                intent.putExtra(Constant.KEY_TRANSACTION_TYPE, getString(R.string.withdraw));
            }
            addTransactionActivityResultLauncher.launch(intent);
        });

        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        builder.setView(view);
        builder.show();
    }
}