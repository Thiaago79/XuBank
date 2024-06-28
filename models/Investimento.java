package models;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Investimento extends Conta {
    private double imposto;
    private double taxaFixa;
    private double rendimentoMensal;
    private double valorRendimentoMensal;

    public Investimento(Cliente cliente) {
        super(cliente);
        this.valorRendimentoMensal = 0.002;
        this.imposto = 0.225;
        this.taxaFixa = 0.01;
        Random random = new Random();
        this.rendimentoMensal = random.nextDouble() * (0.021) - 0.006;   
    }

    public Investimento(Cliente cliente, int numero, double saldo,double imposto, double taxaFixa, double rendimentoMensal, double valorRendimentoMensal) {
        super(cliente,numero, saldo);        
        this.imposto = imposto;
        this.taxaFixa = taxaFixa;
        this.rendimentoMensal = rendimentoMensal;
        this.valorRendimentoMensal = valorRendimentoMensal;
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
        if(validarValor(valorRendimentoMensal) == false){
            throw new Error("Não é possivel atribuir esse valor ao rendimento mensal");
        }else{
            this.valorRendimentoMensal = valorRendimentoMensal;
        }
    }

    public boolean validarValor(double valor){
        return(valor >= -0.006 && valor <= 0.015);
    }

    public double sacar(double valorSacar){

        double saldo = super.getSaldo();

        if(saldo > (valorSacar + (this.rendimentoMensal * this.imposto))){

            System.out.println("Imposto "+ this.rendimentoMensal * this.imposto);
            saldo = saldo - (this.rendimentoMensal * this.imposto);
            System.out.println("Saldo com imposto "+ saldo);

            saldo = saldo - valorSacar;
            System.out.println("Saldo com imposto e saque "+ saldo);

            Operacao operacao = new Operacao("Saque", valorSacar, super.getNumero());
            editarContaNoArquivo(getNumero(), operacao, saldo);
                        
        }else if(saldo == 0){
            throw new Error("Conta sem saldo");
        }else{
            throw new Error("Valor a sacar maior que o saldo");
        }

        super.setSaldo(saldo);

        return saldo;
    }

    public double depositar(double valorDepositar){
        
        double saldo = super.getSaldo();

        saldo += valorDepositar;
        
        super.setSaldo(saldo);

        Operacao operacao = new Operacao("Deposito", valorDepositar, super.getNumero());
        editarContaNoArquivo(getNumero(), operacao, saldo);
              
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

            Operacao operacao = new Operacao("Rendimento", rendimento, super.getNumero());
                editarContaNoArquivo(getNumero(), operacao, saldo);


            if(rendimento > 0){
                double valorTaxa = rendimento * taxaFixa;

                saldo -= valorTaxa;
    
                super.setSaldo(saldo);

                Operacao opera = new Operacao("Taxa", valorTaxa, super.getNumero());
                editarContaNoArquivo(getNumero(), opera, saldo);
            }
            
        }

        return this.rendimentoMensal;
    }

    public void extrato(String tipo, double valor, int numConta){
        super.operacoes.add(new Operacao(tipo, valor, numConta));
    }

    public void editarContaNoArquivo(int numeroConta, Operacao operacao, double novoSaldo) {
        try {
            List<String> linhas = Files.readAllLines(Paths.get("clientes.txt"), StandardCharsets.UTF_8);

            for (int i = 0; i < linhas.size(); i++) {
                if (linhas.get(i).startsWith("Conta Investimento: Número: " + numeroConta)) {
                    String[] partes = linhas.get(i).split(", Saldo: ");
                    String[] saldoParte = partes[1].split(", Cliente: ");
                    String newSaldo = Double.toString(novoSaldo);
                    linhas.set(i, partes[0] + ", Saldo: " + newSaldo + ", Cliente: " + saldoParte[1]);

                    linhas.add(operacao.infoOperacao());

                    break;
                }
            }

            Files.write(Paths.get("clientes.txt"), linhas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String infoConta() {
        return "Conta Investimento: " + super.infoConta() + ", Imposto: " + imposto + ", Taxa Fixa: " + taxaFixa + ", Rendimento Mensal: " + rendimentoMensal + ", Valor do Rendimento Mensal: " + valorRendimentoMensal + "\n";
    }
}
