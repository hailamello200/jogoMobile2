package com.example.hash.user;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class DateMaskWatcher implements TextWatcher {
    private static final String DATE_MASK = "##/##/####"; // Define a máscara desejada
    private static final char PLACEHOLDER = '#';

    private final EditText editText;

    public DateMaskWatcher(EditText editText) {
        this.editText = editText;
        this.editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
        // Nada a fazer aqui
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        // Nada a fazer aqui
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // Remove o TextWatcher para evitar chamadas recursivas
        editText.removeTextChangedListener(this);

        // Obtém o texto atual do EditText
        String text = editable.toString();

        // Remove todos os caracteres não numéricos
        String cleanText = text.replaceAll("[^\\d]", "");

        // Formata a data usando a máscara
        StringBuilder formattedText = new StringBuilder();
        int index = 0;
        for (char maskChar : DATE_MASK.toCharArray()) {
            if (index < cleanText.length()) {
                if (maskChar == PLACEHOLDER) {
                    formattedText.append(cleanText.charAt(index));
                    index++;
                } else {
                    formattedText.append(maskChar);
                }
            }
        }

        // Atualiza o texto no EditText
        editText.setText(formattedText.toString());

        // Define o cursor na posição correta
        editText.setSelection(formattedText.length());

        // Reanexa o TextWatcher
        editText.addTextChangedListener(this);
    }
}
