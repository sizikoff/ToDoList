package ru.tihomirov.todolist.managers;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PhoneDataStorage {

    private static final String APP_PREFERENCES = "UserSettingsToDoListApp";

    private static SharedPreferences mSharedPreferences;


    /** Пустой конструктор */
    public PhoneDataStorage() {
    }

    /**
     *  Сохраняет переданные данные в файл на устройсте пользователя.
     *  @param context Передается Activity класса, из которого вызывается эта функция.
     *  @param key Ключ-идентификатор по которому сохраняется значение.
     *  @param value Значение, сохраняемое по ключу.
     */
    public static void saveText(Context context, String key, String value) {
        mSharedPreferences = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        /** Открытие файла для изменения */
        SharedPreferences.Editor ed = mSharedPreferences.edit();
        ed.putString(key, value);

        /** Подтверждение внесенных изменений, закрытие файла */
        ed.apply();
    }


    /**
     * Загружает значение из файла по полученному ключу.
     * @param context Передается Activity класса, из которого вызывается эта функция.
     * @param key Ключ-идентификатор, по которому ищется значение.
     * @return Найденное значение по переданному ключу.
     */
    public static String loadText(Context context, String key) {
        mSharedPreferences = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        return mSharedPreferences.getString(key, "");
    }

    /**
     * Удаляет значение из файла по ключу.
     * @param context Передается Activity класса, из которого вызывается эта функция.
     * @param key Ключ-идентификатор, по которому удаляется значение.
     */
    public static void deleteText(Context context, String key) {
        mSharedPreferences = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        /** Открытие файла для изменения */
        SharedPreferences.Editor ed = mSharedPreferences.edit();
        ed.putString(key, "");

        /** Подтверждение внесенных изменений, закрытие файла */
        ed.apply();
    }

}
