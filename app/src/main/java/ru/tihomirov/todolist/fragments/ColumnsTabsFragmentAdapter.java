package ru.tihomirov.todolist.fragments;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import ru.tihomirov.todolist.R;
import ru.tihomirov.todolist.managers.DBHelper;

import static ru.tihomirov.todolist.Constants.ID;
import static ru.tihomirov.todolist.Constants.LOG_TAG;
import static ru.tihomirov.todolist.Constants.NAME;

public class ColumnsTabsFragmentAdapter extends FragmentPagerAdapter {

    private Map<Integer, BaseTabFragment> tabs;
    private Context context;
    public static int last = 0;

    private DBHelper dbHelper;

    public ColumnsTabsFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);

        this.context = context;
        initTabsMap(context);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }


    private void initTabsMap(Context context) {
        tabs = new HashMap<>();

        dbHelper = new DBHelper(context);
        Cursor c = dbHelper.queryInColumns();

        last = 0;
        int i = 0;

        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex(ID);
            int nameColIndex = c.getColumnIndex(NAME);

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) +
                                ", name = " + c.getString(nameColIndex));

                tabs.put(i, ColumnFragment.getInstance(context, c.getInt(idColIndex),
                        c.getString(nameColIndex)));


                last = i;
                i ++;

            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, context.getResources().getString(R.string.zero_rows));
        c.close();
        dbHelper.close();


        tabs.put(i, AddColumnFragment.getInstance(context));
    }
}
