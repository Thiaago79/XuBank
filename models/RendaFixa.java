package models;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RendaFixa extends Conta{
    private double imposto;
    private double taxaFixa;
    private double rendimentoMensal;
    private double valorRendimentoMensal;

    public RendaFixa(Cliente cliente) {

        super(cliente);     
        this.valorRendimentoMensal = 0.005;
        this.imposto = 0.15;
        this.taxaFixa = 20;
        this.rendimentoMensal = 0;
    }

    public RendaFixa(Cliente cliente, int numero, double saldo, double imposto, double taxaFixa, double rendimentoMensal, double valorRendimentoMensal) {
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
        if(validarValor(valorRendimentoMensal) == false){
            throw new Error("Não é possivel atribuir esse valor ao rendimento mensal");
        }else{
            this.valorRendimentoMensal = valorRendimentoMensal;
        }
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

        Operacao operacao = new Operacao("Saque", valorSacar, super.getNumero());
        editarContaNoArquivo(getNumero(), operacao);

        return saldo;
    }

    public double depositar(double valorDepositar){
        
        double saldo = super.getSaldo();

        saldo += valorDepositar;
        super.setSaldo(saldo);

        Operacao operacao = new Operacao("Deposito", valorDepositar, super.getNumero());
        editarContaNoArquivo(getNumero(), operacao);
        
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
            editarContaNoArquivo(getNumero(), operacao);

            saldo -= taxaFixa;

            super.setSaldo(saldo);

            Operacao opera = new Operacao("Taxa", rendimento, super.getNumero());
            editarContaNoArquivo(getNumero(), opera);
            
        }

        return this.rendimentoMensal;
    }

    public void extrato(String tipo, double valor, int numConta){
        super.operacoes.add(new Operacao(tipo, valor, numConta));
    }

    public void editarContaNoArquivo(int numeroConta, Operacao operacao) {
        try {
            List<String> linhas = Files.readAllLines(Paths.get("clientes.txt"), StandardCharsets.UTF_8);

            for (int i = 0; i < linhas.size(); i++) {
                if (linhas.get(i).startsWith("Conta Renda Fixa: Número: " + numeroConta)) {
                    String[] partes = linhas.get(i).split(", Saldo: ");
                    String[] saldoParte = partes[1].split(", Cliente: ");
                    String novoSaldo = Double.toString(Double.parseDouble(saldoParte[0]) - operacao.getValor());
                    linhas.set(i, partes[0] + ", Saldo: " + novoSaldo + ", Cliente: " + saldoParte[1]);

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
        return "Conta Renda Fixa: " + super.infoConta() + ", Imposto: " + imposto + ", Taxa Fixa: " + taxaFixa + ", Rendimento Mensal: " + rendimentoMensal + ", Valor do Rendimento Mensal: " + valorRendimentoMensal + "\n";
    }
}
