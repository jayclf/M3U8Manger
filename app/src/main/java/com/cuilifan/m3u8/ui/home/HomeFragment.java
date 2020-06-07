package com.cuilifan.m3u8.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cuilifan.m3u8.R;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        Button btFromUrl = rootView.findViewById(R.id.bt_from_url);
        Button btFromFile = rootView.findViewById(R.id.bt_from_file);

        MyClickListener clickListener = new MyClickListener();
        btFromUrl.setOnClickListener(clickListener);
        btFromFile.setOnClickListener(clickListener);
        return rootView;
    }

    private final class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_from_url:
                    // 弹出输入内容的对话框
                    showURLInputDialog();
                    break;
                case R.id.bt_from_file:

                    break;
            }
        }
    }

    private void showURLInputDialog() {
        final EditText editText = new EditText(getContext());
        editText.setHint(R.string.input_url);
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.title_url)
                .setView(editText)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取输入的URL
                        String url = editText.getText().toString().trim();
                        if (TextUtils.isEmpty(url)) {

                        }
                    }
                })
                .create()
                .show();
    }
}