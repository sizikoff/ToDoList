package ru.tihomirov.todolist.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.tihomirov.todolist.R;
import ru.tihomirov.todolist.fragments.ColumnsTabsFragmentAdapter;
import ru.tihomirov.todolist.managers.DBHelper;

import static ru.tihomirov.todolist.Constants.EXIST;
import static ru.tihomirov.todolist.fragments.ColumnsTabsFragmentAdapter.last;
import static ru.tihomirov.todolist.managers.PhoneDataStorage.loadText;
import static ru.tihomirov.todolist.managers.PhoneDataStorage.saveText;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.viewPagerColumns)
    ViewPager viewPager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private DBHelper dbHelper;
    private String columnName;
    private String currentColumnName;
    private String prevCurrentColumnName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        dbHelper = new DBHelper(this);

        initToolbar(MainActivity.this, toolbar, getResources().getString(R.string.app_name));

        if (!checkCookieId()) {
            saveText(MainActivity.this, EXIST, getResources().getString(R.string.ok));
            dbHelper.addBase();
        }

        dbHelper.close();

        initTabs();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_column, menu);
        return true;
    }



    private void initTabs() {
        final ColumnsTabsFragmentAdapter adapter
                = new ColumnsTabsFragmentAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        final boolean[] dialogOpen = {false};

        final LinearLayout[] tabStrip = {((LinearLayout) tabLayout.getChildAt(0))};

        tabStrip[0].getChildAt(last + 1).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (!dialogOpen[0]) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(getResources().getString(R.string.add_column));

                    final EditText input = new EditText(MainActivity.this);
                    input.setHint(getResources().getString(R.string.enter_column_name));
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input)
                            .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    columnName = input.getText().toString();

                                    if (columnName.length() != 0) {
                                        dbHelper = new DBHelper(MainActivity.this);
                                        dbHelper.insertInColumns(columnName);
                                        dbHelper.close();

                                        Intent intent = new Intent(MainActivity.this,
                                                MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                        MainActivity.this.startActivity(intent);
                                        finish();
                                    } else dialog.cancel();

                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                    alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            dialogOpen[0] = false;
                        }
                    });
                    dialogOpen[0] = true;
                }

                return true;
            }
        });

        if (adapter.getCount() < 4) tabLayout.setTabMode(TabLayout.MODE_FIXED);
        else tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_change:

                currentColumnName = tabLayout.getTabAt(viewPager.getCurrentItem()).getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getResources().getString(R.string.change_column_name));

                final EditText inputName = new EditText(MainActivity.this);

                inputName.setText(currentColumnName);
                inputName.setInputType(InputType.TYPE_CLASS_TEXT);

                builder.setView(inputName)
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                prevCurrentColumnName = currentColumnName;
                                currentColumnName = inputName.getText().toString();

                                if (currentColumnName.length() != 0) {

                                    dbHelper = new DBHelper(MainActivity.this);
                                    dbHelper.updateNameInColumns(currentColumnName,
                                            prevCurrentColumnName);
                                    dbHelper.close();

                                    Intent intent = new Intent(MainActivity.this,
                                            MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    MainActivity.this.startActivity(intent);
                                    finish();

                                } else dialog.cancel();

                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });


                AlertDialog alert = builder.create();
                alert.show();

                break;
            case R.id.donate:
                Toast.makeText(this, "adfasdfasdasdas", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkCookieId() {
        return loadText(MainActivity.this, EXIST).length() != 0;
    }

    public static void initToolbar(AppCompatActivity activity, Toolbar toolbar, String title) {

        toolbar.setTitle(title);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });

        activity.setSupportActionBar(toolbar);
    }
}
