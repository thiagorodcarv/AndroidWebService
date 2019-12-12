package com.example.testewebservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private EditText cepDestino;
    private EditText pesoMercadoria;
    private EditText comprimentoMercadoria;
    private EditText larguraMercadoria;
    private EditText alturaMercadoria;
    private EditText diametroMercadoria;
    private TextView precoFrete;
    private TextView prazoEntrega;
    private Spinner spinnerServico;
    private String tipoDeServico;
    String switer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cepDestino = findViewById(R.id.cepDestino);
        pesoMercadoria = findViewById(R.id.pesoMercadoria);
        comprimentoMercadoria = findViewById(R.id.comprimentoMercadoria);
        larguraMercadoria = findViewById(R.id.larguraMercadoria);
        alturaMercadoria = findViewById(R.id.alturaMercadoria);
        diametroMercadoria = findViewById(R.id.diametroMercadoria);
        precoFrete = findViewById(R.id.precoFrete);
        prazoEntrega = findViewById(R.id.prazoEntrega);
        spinnerServico = findViewById(R.id.spinner);

        spinnerServico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switer = parent.getItemAtPosition(position).toString();
                switch (position){
                    case 0:
                }
                if (switer.equals("SEDEX Varejo")) {
                    tipoDeServico = "40010";
                }
                else if (switer.equals("SEDEX a Cobrar Varejo")){
                    tipoDeServico = "40045";
                }

                else if (switer.equals("SEDEX 10 Varejo")){
                    tipoDeServico = "40215";
                }

                else if (switer.equals("SEDEX Hoje Varejo")){
                    tipoDeServico = "40290";
                }
                else if (switer.equals("PAC Varejo")) {
                    tipoDeServico = "41106 ";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void calcularFrete(View view){
//        Toast.makeText(this, switer+" nº correspondente: "+tipoDeServico, Toast.LENGTH_SHORT).show();
        String cep = cepDestino.getText().toString();
        String peso = pesoMercadoria.getText().toString();
        String comprimento = comprimentoMercadoria.getText().toString();
        String altura = alturaMercadoria.getText().toString();
        String largura = larguraMercadoria.getText().toString();
        String diametro = diametroMercadoria.getText().toString();

        if (cep.length()>0&&!cep.equals("")&&cep.length()==8){
            HTTPService service = new HTTPService(cep,peso,comprimento,altura,largura,diametro,tipoDeServico);
            precoFrete.setVisibility(View.VISIBLE);
            try {
                String resposta = service.execute().get();
                if (resposta.contains("NO_ERROR")){
                    prazoEntrega.setVisibility(View.VISIBLE);
                    precoFrete.setText(resposta.split("NO_ERROR")[0]+" reais");
                    prazoEntrega.setText(resposta.split("NO_ERROR")[1]+" dias");
                }
                else {
                    precoFrete.setText("Processo retornou erro do tipo:" +resposta);
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
