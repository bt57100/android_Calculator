package calculatrice.guoi.ece.fr.calculator;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class Calculator extends AppCompatActivity implements View.OnClickListener {

    private static Button buttonEqual;
    private static TextView resultView;

    private static String operand1;
    private static boolean op1IsSet;
    private static String operand2;
    private static String operator;
    private static double result;

    private static Handler handler;
    private Thread calculateThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        Button button0 = (Button) findViewById(R.id.button0);
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);
        Button button6 = (Button) findViewById(R.id.button6);
        Button button7 = (Button) findViewById(R.id.button7);
        Button button8 = (Button) findViewById(R.id.button8);
        Button button9 = (Button) findViewById(R.id.button9);
        Button buttonPlus = (Button) findViewById(R.id.buttonPlus);
        Button buttonMinus = (Button) findViewById(R.id.buttonMinus);
        Button buttonDiv = (Button) findViewById(R.id.buttonDiv);
        Button buttonMul = (Button) findViewById(R.id.buttonMul);
        Button buttonC = (Button) findViewById(R.id.buttonC);
        Button buttonDot = (Button) findViewById(R.id.buttonDot);
        resultView = (TextView) findViewById(R.id.result);
        buttonEqual = (Button) findViewById(R.id.buttonEqual);

        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonPlus.setOnClickListener(this);
        buttonMinus.setOnClickListener(this);
        buttonDiv.setOnClickListener(this);
        buttonMul.setOnClickListener(this);
        buttonC.setOnClickListener(this);
        buttonDot.setOnClickListener(this);
        buttonEqual.setOnClickListener(this);

        operand1 = "";
        operand2 = "";
        operator = "";
        op1IsSet = false;

        handler = new Handler();
        calculateThread = (Thread) getLastNonConfigurationInstance();
        if (calculateThread != null && calculateThread.isAlive()) {
            buttonEqual.setClickable(false);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button0:
                addOperand("0");
                break;
            case R.id.button1:
                addOperand("1");
                break;
            case R.id.button2:
                addOperand("2");
                break;
            case R.id.button3:
                addOperand("3");
                break;
            case R.id.button4:
                addOperand("4");
                break;
            case R.id.button5:
                addOperand("5");
                break;
            case R.id.button6:
                addOperand("6");
                break;
            case R.id.button7:
                addOperand("7");
                break;
            case R.id.button8:
                addOperand("8");
                break;
            case R.id.button9:
                addOperand("9");
                break;
            case R.id.buttonDot:
                addOperand(".");
                break;
            case R.id.buttonPlus:
                addOperator("+");
                break;
            case R.id.buttonMinus:
                addOperator("-");
                break;
            case R.id.buttonMul:
                addOperator("*");
                break;
            case R.id.buttonDiv:
                addOperator("/");
                break;
            case R.id.buttonC:
                clear();
                resultView.setText("0");
                break;
            case R.id.buttonEqual:
                if((!operand1.contentEquals("")) && (!operand2.contentEquals("")) && (!operator.contentEquals(""))) {
                    buttonEqual.setClickable(false);
                    calculateThread = new calculateThread();
                    calculateThread.start();
                }
                break;
        }
    }

    public void addOperand(String value) {
        if(op1IsSet) {
            if((!value.contentEquals(".")) || (!operand2.contains("."))) {
                operand2 += value;
            }
        } else {
            if((!value.contentEquals(".")) || (!operand1.contains("."))) {
                operand1 += value;
            }
        }
        resultView.setText(operand1 + operator + operand2);
    }

    public void addOperator(String value) {
        if((operand1.isEmpty())&&(value.contentEquals("-"))) {
            operand1 += value;
        } else if ((operand2.isEmpty())&&(value.contentEquals("-"))&&(op1IsSet)) {
            operand2 += value;
        } else {
            operator = value;
            op1IsSet = true;
        }
        resultView.setText(operand1 + operator + operand2);
    }

    public static void clear() {
        operand1 = "";
        operand2 = "";
        operator = "";
        op1IsSet = false;
    }

    public static void calculate() throws IOException {
        double op1 = Double.parseDouble(operand1);
        double op2 = Double.parseDouble(operand2);
        switch (operator) {
            case "+":
                result = op1 + op2;
                break;
            case "-":
                result = op1 - op2;
                break;
            case "*":
                result = op1 * op2;
                break;
            case "/":
                result = op1 / op2;
                break;
        }
        clear();
        operand1 = String.valueOf(result);
    }

    static public class calculateThread extends Thread {
        @Override
        public void run() {
            try {
                calculate();
                handler.post(new MyRunnable());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static public class MyRunnable implements Runnable {
        public void run() {
            buttonEqual.setClickable(true);
            resultView.setText(String.valueOf(result));
        }
    }
}

