package models;

import java.util.Date;

public class Operacao {
    private String tipo;
    private double valor;
    private Date data;
    private int conta;

    public Operacao(String tipo, double valor, int conta) {
        this.tipo = tipo;
        this.valor = valor;
        this.data = new Date();
        this.conta = conta;
    }

    public Operacao(String tipo, double valor, Date data, int conta) {
        this.tipo = tipo;
        this.valor = valor;
        this.data = data;
        this.conta = conta;
    }

    public String getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    public Date getData() {
        return data;
    }
    
    public int getConta() {
        return conta;
    }

    public String infoOperacao() {
        return "Operacao " + tipo + ", Valor: " + valor + ", Data: " + data.toString()+ ", Conta: " + conta +".";
    }
}
