package models;

import java.util.Calendar;
import java.util.Date;

public class RendaFixa extends Conta{
    private double imposto;
    private double taxaFixa;
    private double rendimentoMensal;
    private double valorRendimentoMensal;

    public RendaFixa(Cliente cliente) {

        super(cliente);  
        
        if(validarValor(valorRendimentoMensal) == false){
            throw new Error("Não é possivel atribuir esse valor ao rendimento mensal");
        }else{
            this.valorRendimentoMensal = valorRendimentoMensal;
        }
        
        this.imposto = 0.15;
        this.taxaFixa = 20;
        this.rendimentoMensal = 0;
    }

    public RendaFixa(Cliente cliente, int numero, double saldo,double imposto, double taxaFixa, double rendimentoMensal, double valorRendimentoMensal) {
        super(cliente,numero, saldo);        
        this.imposto = imposto;
        this.taxaFixa = taxaFixa;
        this.rendimentoMensal = rendimentoMensal;
        this.valorRendimentoMensal = valorRendimentoMensal;        
    }

    public boolean validarValor(double valor){
        return(valor >= 0.005 && valor <= 0.0085);
    }

    public double getImposto() {
        return imposto;
    }


    public void setImposto(double imposto) {
        this.imposto = imposto;
    }


    public double getTaxaFixa() {
        return taxaFixa;
    }


    public void setTaxaFixa(double taxaFixa) {
        this.taxaFixa = taxaFixa;
    }


    public double getRendimentoMensal() {
        return rendimentoMensal;
    }


    public void setRendimentoMensal(double rendimentoMensal) {
        this.rendimentoMensal = rendimentoMensal;
    }

    public double getValorRendimentoMensal() {
        return valorRendimentoMensal;
    }

    public void setValorRendimentoMensal(double valorRendimentoMensal) {
        this.valorRendimentoMensal = valorRendimentoMensal;
    }

    public double sacar(double valorSacar){

        double saldo = super.getSaldo();

        if(valorSacar - saldo > 0){

            saldo -= (this.rendimentoMensal * this.imposto);

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
            
            double rendimento = saldo * valorRendimentoMensal;
            saldo += rendimento;

            super.setSaldo(saldo);
            this.rendimentoMensal = rendimento;

            extrato("Rendimento", rendimento);

            saldo -= taxaFixa;

            super.setSaldo(saldo);

            extrato("Cobranca Taxa", taxaFixa);
        }

        return this.rendimentoMensal;
    }

    public void extrato(String tipo, double valor){
        super.operacoes.add(new Operacao(tipo, valor));
    }


    @Override
    public String infoConta() {
        return "Renda Fixa: " + super.infoConta() + "Imposto: " + imposto + ", Taxa Fixa: " + taxaFixa + ", Rendimento Mensal: " + rendimentoMensal + "Valor do Rendimento Mensal" + valorRendimentoMensal + "\n";
    }
}
