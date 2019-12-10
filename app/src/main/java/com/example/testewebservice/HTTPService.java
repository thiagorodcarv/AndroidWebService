package com.example.testewebservice;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class HTTPService extends AsyncTask<Void, Void, String> {

    private final String cepDestino;
    private final String cepOrigem = "60115282"; //CEP da XSEED
    private final String nVlPeso;
    private final String nVlComprimento;
    private final String nVlAltura;
    private final String nVlLargura;
    private final String nVlDiametro;
    private final String nCdServico;

    public HTTPService(String cepDestino,String nVlPeso,String nVlComprimento,String nVlAltura,String nVlLargura,String nVlDiametro,String nCdServico) {
        this.cepDestino = cepDestino;
        this.nVlPeso = nVlPeso;
        this.nVlComprimento = nVlComprimento;
        this.nVlAltura = nVlAltura;
        this.nVlLargura = nVlLargura;
        this.nVlDiametro = nVlDiametro;
        this.nCdServico = nCdServico;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resposta = "", preco = "", prazo = "", line;
        String erroMSG = "";
        boolean erro = false;
        boolean erroType = false;
        try {
            URL url = new URL("http://ws.correios.com.br/calculador/CalcPrecoPrazo.aspx?nCdEmpresa=08082650&sDsSenha=564321&sCe" +
                    "pOrigem="+cepOrigem+"&sCepDestino="+ cepDestino +"&nVlPeso="+nVlPeso+"&nCdFormato=1&nVlComprimento="+nVlComprimento+"&nVlAltura="+nVlAltura +
                    "&nVlLargura="+nVlLargura+"&sCdMaoPropria=n&nVlValorDeclarado=0&sCdAvisoRecebimento=n&nCdServico="+nCdServico+"&" +
                    "nVlDiametro="+nVlDiametro+"&StrRetorno=xml&nIndicaCalculo=3");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept","application/xml");
            connection.connect();

            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()){
                line = scanner.next();
                if (line.contains("<Valor>")){
                    String aux = line.substring(line.lastIndexOf("<Valor>")+7);
                    preco = aux.substring(0,aux.indexOf("</Valor>"));
                }
                if (line.contains("<PrazoEntrega>")){
                    String aux = line.substring(line.lastIndexOf("<PrazoEntrega>")+14);
                    prazo = aux.substring(0,aux.indexOf("</PrazoEntrega>"));
                }
                if (line.contains("<Erro>")){
                    String aux = line.substring(line.lastIndexOf("<Erro>")+6);
                    String erroSTR = aux.substring(0,aux.indexOf("</Erro>"));
                    if (!erroSTR.equals("0")){
                        erro = true;
                        erroType = true;
                    }
                }
                if (erroType){
                    if (line.contains("<MsgErro>")){
                        String aux = line.substring(line.lastIndexOf("<MsgErro>")+9);
                        erroMSG = erroMSG+aux;
                    }
                    else if (line.contains("</MsgErro>")){
                        erroMSG = erroMSG + line.substring(0,line.indexOf("</MsgErro"));
                        erroType = false;
                    }
                    else {
                        erroMSG = line;
                    }
                }
            }
            if (erro){
                resposta = erroMSG;
            }
            else {
                resposta = preco + "NO_ERROR" + prazo;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resposta;
    }
}
