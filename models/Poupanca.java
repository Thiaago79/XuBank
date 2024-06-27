package models;

import java.util.Date;
import java.util.Calendar;

public class Poupanca extends Conta {
    private double rendimentoMensal;

    public Poupanca(Cliente cliente) {
        super(cliente);
        this.rendimentoMensal = 0;
    }

    public Poupanca(Cliente cliente, int numero, double saldo) {
        super(cliente,numero, saldo); 
        this.rendimentoMensal = 0;
    }

    public double getRendimentoMensal() {
        return rendimentoMensal;
    }

    public void setRendimentoMensal(double rendimentoMensal) {
        this.rendimentoMensal = rendimentoMensal;
    }

    public double sacar(double valorSacar){

        double saldo = super.getSaldo();

        if(valorSacar - saldo > 0){
            saldo -= valorSacar;
        }else if(saldo == 0){
            throw new Error("Conta sem saldo");
        }else{
            throw new Error("Valor a sacar maior que o saldo");
        }

        super.setSaldo(saldo);
        extrato("Saque", valorSacar);

        return saldo;
    }

    public double depositar(double valorSacar){
        
        double saldo = super.getSaldo();

        saldo += valorSacar;

        super.setSaldo(saldo);
        extrato("Deposito", valorSacar);

        return saldo;
    }

    public double rendimentoMensal(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int diaDoMes = calendar.get(Calendar.DAY_OF_MONTH);        

        if(diaDoMes == 1){
            double saldo = super.getSaldo();
            
            double rendimento = saldo * 0.6;
            saldo += rendimento;

            super.setSaldo(saldo);
            this.rendimentoMensal = rendimento;

            extrato("Rendimento", rendimento);
        }

        return this.rendimentoMensal;
    }

    public void extrato(String tipo, double valor){
        super.operacoes.add(new Operacao(tipo, valor));
    }

    @Override
    public String infoConta() {
        return "Conta Poupanca: " + super.infoConta() + ", Rendimento Mensal: " + rendimentoMensal + "\n";
    }
}
