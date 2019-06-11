package com.example.northuniversity.schoolteam.modules.train_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.northuniversity.schoolteam.R;

public class FunFragment extends Fragment {
    public static final String ARGS_PAGE = "args_page";
    private int mPage;

    public static FunFragment newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        FunFragment fragment = new FunFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARGS_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fun,container,false);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText("第"+mPage+"页");
        return view;
    }
    public static FunFragment newInstance(String param1) {
        FunFragment fragment = new FunFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }
}
