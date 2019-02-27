package ru.tihomirov.todolist.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.tihomirov.todolist.R;


public class AddColumnFragment extends BaseTabFragment {

    public static AddColumnFragment getInstance(Context context) {
        Bundle args = new Bundle();
        AddColumnFragment fragment = new AddColumnFragment();

        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle("+");

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_column, container, false);

        return view;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
