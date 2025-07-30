package com.cremcash.eloan.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.text.NumberFormat;
import java.util.Locale;

public class TextWatcherHelper {
    public static void setRequiredValidation(EditText editText, String errorMessage) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().toString().trim().isEmpty()) {
                    editText.setError(errorMessage);
                } else {
                    editText.setError(null);
                }
            }
        });
    }

    public static void setRequiredValidation(AutoCompleteTextView view, String errorMessage) {
        view.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (view.getText().toString().trim().isEmpty()) {
                    view.setError(errorMessage);
                } else {
                    view.setError(null);
                }
            }
        });
    }

    public static void setAmountFormatter(EditText editText, String errorMessage) {
        editText.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                editText.removeTextChangedListener(this);

                String input = s.toString();

                if (input.trim().isEmpty()) {
                    editText.setError(errorMessage);
                    current = "";
                } else {
                    editText.setError(null);

                    String cleanString = input.replaceAll(",", "");

                    try {
                        long parsed = Long.parseLong(cleanString);
                        String formatted = NumberFormat.getInstance(Locale.US).format(parsed);

                        if (!formatted.equals(current)) {
                            current = formatted;
                            editText.setText(formatted);
                            editText.setSelection(formatted.length());
                        }

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                editText.addTextChangedListener(this);
            }
        });
    }

}
