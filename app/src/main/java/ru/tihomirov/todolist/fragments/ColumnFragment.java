package ru.tihomirov.todolist.fragments;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.tihomirov.todolist.R;
import ru.tihomirov.todolist.alarm.RebootService;
import ru.tihomirov.todolist.activities.MainActivity;
import ru.tihomirov.todolist.adapters.TaskListAdapter;
import ru.tihomirov.todolist.managers.DBHelper;
import ru.tihomirov.todolist.models.Task;

import static ru.tihomirov.todolist.Constants.COLUMN_ID;
import static ru.tihomirov.todolist.Constants.CONTENT_TEXT;
import static ru.tihomirov.todolist.Constants.CONTENT_TITLE;
import static ru.tihomirov.todolist.Constants.DESCRIPTION;
import static ru.tihomirov.todolist.Constants.DONE;
import static ru.tihomirov.todolist.Constants.ID;
import static ru.tihomirov.todolist.Constants.LOG_TAG;
import static ru.tihomirov.todolist.Constants.NAME;
import static ru.tihomirov.todolist.Constants.TICKER;

public class ColumnFragment extends BaseTabFragment {

    private int columnId;
    private String cardName;
    private String cardDescription;

    private List<Task> data;
    private ArrayList<String> cids;

    private DatePickerDialog datePicker;

    private int mHour, mMinute;

    private DBHelper dbHelper;

    public static ColumnFragment getInstance(Context context, int columnId, String columnName) {
        Bundle args = new Bundle();
        ColumnFragment fragment = new ColumnFragment();

        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setColumnId(columnId);

        fragment.setTitle(columnName);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_column, container, false);

        data = new ArrayList<>();
        cids = new ArrayList<>();

        FloatingActionButton addCardButton = (FloatingActionButton) view.findViewById(R.id.btnAddCard);
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                /**
                 * Использует для получения даты.
                 */
                final Calendar newCalendar = Calendar.getInstance();

                LinearLayout.LayoutParams  params =
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(dp2px(20), dp2px(20), dp2px(20), dp2px(20));

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(getResources().getString(R.string.add_task));

                final EditText inputName = new EditText(context);
                inputName.setLayoutParams(params);
                inputName.setHint(getResources().getString(R.string.enter_task_name));
                inputName.setInputType(InputType.TYPE_CLASS_TEXT);
                layout.addView(inputName);

                final EditText inputDesc = new EditText(context);
                inputDesc.setLayoutParams(params);
                inputDesc.setHint(getResources().getString(R.string.enter_task_description));
                layout.addView(inputDesc);

                LinearLayout.LayoutParams  paramsDate =
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                paramsDate.setMargins(dp2px(25), dp2px(20), dp2px(20), dp2px(20));

                final TextView inputDate = new TextView(context);
                inputDate.setTextSize(16);
//                inputDate.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                inputDate.setLayoutParams(paramsDate);
                inputDate.setHint(getResources().getString(R.string.enter_task_date));


                /**
                 * Требуется для дальнейшего преобразования даты в строку.
                 */
                @SuppressLint("SimpleDateFormat") final SimpleDateFormat dateFormat
                        = new SimpleDateFormat("dd-MM-yyyy");
                final String[] date = {""};

                /**
                 * Создает объект и инициализирует обработчиком события выбора даты и данными для даты по умолчанию.
                 */
                datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    // функция onDateSet обрабатывает шаг 2: отображает выбранные нами данные в элементе EditText
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newCal = Calendar.getInstance();
                        newCal.set(year, monthOfYear, dayOfMonth);

                        date[0] = "";
                        date[0] += dateFormat.format(newCal.getTime());
                        inputDate.setText(dateFormat.format(newCal.getTime()));
                    }
                },
                        newCalendar.get(Calendar.YEAR),
                        newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH));

                mHour = newCalendar.get(Calendar.HOUR_OF_DAY);
                mMinute = newCalendar.get(Calendar.MINUTE);

                final TimePickerDialog timePicker = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                date[0] = date[0].substring(0, 10);

                                if (minute < 10 && hourOfDay < 10) {
                                    date[0] += " 0" + hourOfDay + ":0" + minute;
                                } else if (minute < 10) {
                                    date[0] += " " + hourOfDay + ":0" + minute;
                                } else if (hourOfDay < 10) {
                                    date[0] += " 0" + hourOfDay + ":" + minute;
                                } else {
                                    date[0] += " " + hourOfDay + ":" + minute;
                                }
                                inputDate.setText(date[0]);
                            }
                        }, mHour, mMinute, false);

                timePicker.setCancelable(false);
                layout.addView(inputDate);

                inputDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        datePicker.show();
                    }
                });

                datePicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        timePicker.show();
                    }
                });

                builder.setView(layout)
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cardName = inputName.getText().toString();
                                cardDescription = inputDesc.getText().toString();

                                if (cardName.length() != 0) {

                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                    long timeInMilliseconds = 0;

                                    try {
                                        Date mDate = sdf.parse(date[0]);
                                        timeInMilliseconds = mDate.getTime();
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    dbHelper = new DBHelper(context);
                                    String newTaskId = String.valueOf(dbHelper.insertInTask(cardName,
                                            cardDescription, columnId,
                                            timeInMilliseconds));

                                    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                    Intent intentNotification = new Intent(context, RebootService.class);

                                    intentNotification.putExtra(TICKER, "To do list: " + cardName);
                                    intentNotification.putExtra(CONTENT_TITLE, "Задача: " + cardName);
                                    intentNotification.putExtra(CONTENT_TEXT, cardDescription);
                                    intentNotification.putExtra(ID, Integer.parseInt(newTaskId));

                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                                            intentNotification, PendingIntent.FLAG_UPDATE_CURRENT);

                                    am.set(AlarmManager.RTC_WAKEUP, timeInMilliseconds, pendingIntent);


                                    Intent intent = new Intent(context,
                                            MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                    context.startActivity(intent);
                                    getActivity().finish();

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
            }
        });

        dbHelper = new DBHelper(context);

        Cursor c = dbHelper.queryInTasks();
        int i = 0;

        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idTaskIndex = c.getColumnIndex(ID);
            int columnIdTaskIndex = c.getColumnIndex(COLUMN_ID);
            int nameTaskIndex = c.getColumnIndex(NAME);
            int descriptionTaskIndex = c.getColumnIndex(DESCRIPTION);
            int doneTaskIndex = c.getColumnIndex(DONE);

            do {

                if (columnId == c.getInt(columnIdTaskIndex)) {
                    data.add(new Task(
                            c.getInt(idTaskIndex), columnId, i, c.getString(nameTaskIndex),
                            c.getString(descriptionTaskIndex), c.getInt(doneTaskIndex) == 1
                    ));
                }
            } while (c.moveToNext());
        } else Log.d(LOG_TAG, getResources().getString(R.string.zero_rows));

        c.close();
        dbHelper.close();

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recyclerViewColumn);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(new TaskListAdapter(data, getContext()));


        return view;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

}
