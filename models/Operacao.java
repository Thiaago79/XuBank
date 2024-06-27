package models;

import java.util.Date;

public class Operacao {
    private String tipo;
    private double valor;
    private Date data;

    public Operacao(String tipo, double valor) {
        this.tipo = tipo;
        this.valor = valor;
        this.data = new Date();
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }

    public String infoOperacao() {
        return "Operacao: Tipo = " + tipo + ", Valor = " + valor + ", Data = " + data.toString();
    }
}
