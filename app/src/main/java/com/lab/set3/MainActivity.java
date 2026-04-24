package com.lab.set3;

// Name: Emre ARACI
// Student ID: 56905
// Lab: SET 3 - Calculator

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private TextView expressionDisplay;
    private String currentInput = "";
    private double previousResult = 0;
    private String currentOperator = "";
    private boolean justPressedEquals = false;
    private boolean hasError = false;
    private String expression = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);
        expressionDisplay = findViewById(R.id.expressionDisplay);

        int[] digitIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
        for (int i = 0; i < digitIds.length; i++) {
            final String digit = String.valueOf(i);
            findViewById(digitIds[i]).setOnClickListener(v -> onDigit(digit));
        }

        findViewById(R.id.btnDot).setOnClickListener(v -> onDot());
        findViewById(R.id.btnAdd).setOnClickListener(v -> onOperator("+"));
        findViewById(R.id.btnSub).setOnClickListener(v -> onOperator("-"));
        findViewById(R.id.btnMul).setOnClickListener(v -> onOperator("*"));
        findViewById(R.id.btnDiv).setOnClickListener(v -> onOperator("/"));
        findViewById(R.id.btnPow).setOnClickListener(v -> onOperator("^"));
        findViewById(R.id.btnEq).setOnClickListener(v -> onEquals());
        findViewById(R.id.btnEq2).setOnClickListener(v -> onEquals());
        findViewById(R.id.btnC).setOnClickListener(v -> onClear());
        findViewById(R.id.btnBin).setOnClickListener(v -> onBase(2));
        findViewById(R.id.btnOct).setOnClickListener(v -> onBase(8));
        findViewById(R.id.btnHex).setOnClickListener(v -> onBase(16));
        findViewById(R.id.btnHistory).setOnClickListener(v -> {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    private void onDigit(String digit) {
        if (hasError) return;
        if (justPressedEquals) {
            currentInput = "";
            previousResult = 0;
            currentOperator = "";
            expression = "";
            justPressedEquals = false;
        }
        currentInput += digit;
        expression += digit;
        display.setText(currentInput);
        expressionDisplay.setText(expression);
    }

    private void onDot() {
        if (hasError) return;
        if (!currentInput.contains(".")) {
            if (currentInput.isEmpty()) currentInput = "0";
            currentInput += ".";
            expression += ".";
            display.setText(currentInput);
            expressionDisplay.setText(expression);
        }
    }

    private void onOperator(String op) {
        if (hasError) return;
        justPressedEquals = false;

        if (!currentInput.isEmpty()) {
            double current = Double.parseDouble(currentInput);
            if (!currentOperator.isEmpty()) {
                previousResult = calculate(previousResult, current, currentOperator);
                if (Double.isInfinite(previousResult) || Double.isNaN(previousResult)) {
                    display.setText("Error");
                    hasError = true;
                    return;
                }
                display.setText(formatResult(previousResult));
            } else {
                previousResult = current;
            }
            currentInput = "";
        }

        expression += " " + op + " ";
        currentOperator = op;
        expressionDisplay.setText(expression);
    }

    private void onEquals() {
        if (hasError) return;
        if (currentInput.isEmpty() && currentOperator.isEmpty()) return;
        if (currentInput.isEmpty()) return;

        double current = Double.parseDouble(currentInput);
        double result = calculate(previousResult, current, currentOperator);

        if (Double.isInfinite(result) || Double.isNaN(result)) {
            display.setText("Error");
            hasError = true;
            return;
        }

        String fullExpression = formatResult(previousResult) + " " + currentOperator + " " + formatResult(current);
        HistoryActivity.history.add(fullExpression + " = " + formatResult(result));
        display.setText(formatResult(result));
        expressionDisplay.setText(fullExpression + " =");
        previousResult = result;
        currentInput = "";
        currentOperator = "";
        expression = "";
        justPressedEquals = true;
    }

    private void onClear() {
        currentInput = "";
        previousResult = 0;
        currentOperator = "";
        expression = "";
        justPressedEquals = false;
        hasError = false;
        display.setText("0");
        expressionDisplay.setText("");
    }

    private void onBase(int base) {
        if (hasError) return;
        String text = display.getText().toString();
        try {
            long value = (long) Double.parseDouble(text);
            switch (base) {
                case 2: display.setText(Long.toBinaryString(value)); break;
                case 8: display.setText(Long.toOctalString(value)); break;
                case 16: display.setText(Long.toHexString(value).toUpperCase()); break;
            }
        } catch (NumberFormatException e) {
            display.setText("Error");
        }
    }

    private double calculate(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return b == 0 ? Double.POSITIVE_INFINITY : a / b;
            case "^": return Math.pow(a, b);
            default: return b;
        }
    }

    private String formatResult(double result) {
        if (result == (long) result) {
            return String.valueOf((long) result);
        }
        return String.valueOf(result);
    }

    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") ||
                token.equals("*") || token.equals("/") || token.equals("^");
    }

    private int precedence(String op) {
        switch (op) {
            case "+": case "-": return 1;
            case "*": case "/": return 2;
            case "^": return 3;
            default: return 0;
        }
    }
}