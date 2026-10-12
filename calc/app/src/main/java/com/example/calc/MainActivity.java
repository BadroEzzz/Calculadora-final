package com.example.calc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;





public class MainActivity extends AppCompatActivity {
    private double valor = 0;
    private String operador = "";
    private boolean finalitzat = false;
    private double memoria = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        setContentView(R.layout.activity_main);
        TextView consola = findViewById(R.id.consola);



        View.OnClickListener btnNum = v -> {
            Button btnPulsado = (Button) v;

            if (finalitzat) {
                finalitzat = false;
                valor = 0;
                operador = "";
                consola.setText("");
            }

            if (btnPulsado.getText().toString().equals(".")
                    && consola.getText().toString().contains(".")) {
                return;
            }

            consola.append(btnPulsado.getText().toString());
        };


        View.OnClickListener operacio = v -> {


            int idMem = v.getId();
            if (idMem == R.id.mplus || idMem == R.id.mrest
                    || idMem == R.id.mc || idMem == R.id.mr) {

                if (idMem == R.id.mc) {
                    memoria = 0;
                    return;
                }

                if (idMem == R.id.mr) {
                    String m = String.valueOf(memoria);
                    if (m.endsWith(".0")) {
                        m = m.substring(0, m.length() - 2);
                    }

                    consola.setText(m);
                    return;
                }

                String txt = consola.getText().toString();
                if (txt.isEmpty() || txt.equals("Error")) {
                    return;
                }
                try {
                    double num = Double.parseDouble(txt);
                    if (idMem == R.id.mplus) {
                        memoria += num;
                    } else {
                        memoria -= num;
                    }
                    consola.setText("");
                } catch (NumberFormatException e) {

                }
                return;
            }


            int id = v.getId();

            if (id == R.id.sum) {
                setOperador(consola, "+");
            } else if (id == R.id.rest) {
                setOperador(consola, "-");
            } else if (id == R.id.multiply) {
                setOperador(consola, "X");
            } else if (id == R.id.divide) {
                setOperador(consola, "/");
            } else if (id == R.id.result) {
                calcular(consola);
            } else if (id == R.id.clear) {
                valor = 0;
                operador = "";
                finalitzat = false;
                consola.setText("");
            }
        };


        findViewById(R.id.sum).setOnClickListener(operacio);
        findViewById(R.id.rest).setOnClickListener(operacio);
        findViewById(R.id.divide).setOnClickListener(operacio);
        findViewById(R.id.multiply).setOnClickListener(operacio);
        findViewById(R.id.clear).setOnClickListener(operacio);
        findViewById(R.id.result).setOnClickListener(operacio);


        findViewById(R.id.mplus).setOnClickListener(operacio);
        findViewById(R.id.mrest).setOnClickListener(operacio);
        findViewById(R.id.mc).setOnClickListener(operacio);
        findViewById(R.id.mr).setOnClickListener(operacio);


        findViewById(R.id.num0).setOnClickListener(btnNum);
        findViewById(R.id.num1).setOnClickListener(btnNum);
        findViewById(R.id.num2).setOnClickListener(btnNum);
        findViewById(R.id.num3).setOnClickListener(btnNum);
        findViewById(R.id.num4).setOnClickListener(btnNum);
        findViewById(R.id.num5).setOnClickListener(btnNum);
        findViewById(R.id.num6).setOnClickListener(btnNum);
        findViewById(R.id.num7).setOnClickListener(btnNum);
        findViewById(R.id.num8).setOnClickListener(btnNum);
        findViewById(R.id.num9).setOnClickListener(btnNum);
        findViewById(R.id.dot).setOnClickListener(btnNum);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        if (savedInstanceState != null) {
            consola.setText(savedInstanceState.getString("texto", ""));
            valor = savedInstanceState.getDouble("valor", 0);
            operador = savedInstanceState.getString("operador", "");
            finalitzat = savedInstanceState.getBoolean("finalitzat", false);
            memoria = savedInstanceState.getDouble("memoria", 0);
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView consola = findViewById(R.id.consola);
        outState.putString("texto", consola.getText().toString());
        outState.putDouble("valor", valor);
        outState.putString("operador", operador);
        outState.putBoolean("finalitzat", finalitzat);
        outState.putDouble("memoria", memoria);


    }

    private void setOperador(TextView consola, String op) {
        String texto = consola.getText().toString();

        if (texto.equals("Error") || texto.endsWith(".")) return;

        if (texto.isEmpty()) {
            if (!operador.isEmpty()) {
                operador = op;
            }
            return;
        }

        double num = Double.parseDouble(texto);

        if (!operador.isEmpty()) {
            Double res = operar(num);
            if (res == null) {
                consola.setText("Error");
                operador = "";
                finalitzat = true;
                return;
            }
            num = res;
        }

        valor = num;
        operador = op;
        finalitzat = false;
        consola.setText("");
    }

    private void calcular(TextView consola) {
        String actual = consola.getText().toString();
        if (actual.isEmpty() || actual.equals(".") || actual.equals("Error") || operador.isEmpty()) {
            return;
        }

        Double res = operar(Double.parseDouble(actual));
        if (res == null) {
            consola.setText("Error");
        } else {
            consola.setText(formatear(res));
        }
        operador = "";
        finalitzat = true;
    }

    private Double operar(double num) {
        switch (operador) {
            case "X": return valor * num;
            case "+": return valor + num;
            case "-": return valor - num;
            case "/":
                if (num == 0) return null;
                return valor / num;
        }
        return num;
    }

    private String formatear(double d) {
        d = Math.round(d * 10000000000.0) / 10000000000.0;

        String s = String.valueOf(d);
        if (s.endsWith(".0")) {
            return s.substring(0, s.length() - 2);
        } else {
            return s;
        }
    }
}
