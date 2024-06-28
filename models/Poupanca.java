package models;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Poupanca extends Conta {
    private double rendimentoMensal;

    public Poupanca(Cliente cliente) {
        super(cliente);
        this.rendimentoMensal = 0;
    }

    public Poupanca(Cliente cliente, int numero, double saldo) {
        super(cliente, numero, saldo);
        this.rendimentoMensal = 0;
    }

    public double getRendimentoMensal() {
        return rendimentoMensal;
    }

    public void setRendimentoMensal(double rendimentoMensal) {
        this.rendimentoMensal = rendimentoMensal;
    }

    @Override
    public double sacar(double valorSacar) {
        double saldo = super.getSaldo();

        if (valorSacar > saldo) {
            throw new Error("Valor a sacar maior que o saldo");
        }

        saldo -= valorSacar;
        super.setSaldo(saldo);

        Operacao operacao = new Operacao("Saque", valorSacar, super.getNumero());
        editarContaNoArquivo(getNumero(), operacao, saldo);

        return saldo;
    }

    @Override
    public double depositar(double valorDepositar) {
        double saldo = super.getSaldo();

        saldo += valorDepositar;
        super.setSaldo(saldo);

        Operacao operacao = new Operacao("Deposito", valorDepositar, super.getNumero());
        editarContaNoArquivo(getNumero(), operacao, saldo);

        return saldo;
    }

    @Override
    public double rendimentoMensal() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int diaDoMes = calendar.get(Calendar.DAY_OF_MONTH);

        if (diaDoMes == 1) {
            double saldo = super.getSaldo();
            double rendimento = saldo * 0.6;
            saldo += rendimento;

            super.setSaldo(saldo);
            this.rendimentoMensal = rendimento;

            Operacao opera = new Operacao("Rendimento", rendimento, super.getNumero());
            editarContaNoArquivo(getNumero(), opera, saldo);
        }

        return this.rendimentoMensal;
    }

    public void editarContaNoArquivo(int numeroConta, Operacao operacao, double novoSaldo) {
        try {
            List<String> linhas = Files.readAllLines(Paths.get("clientes.txt"), StandardCharsets.UTF_8);

            for (int i = 0; i < linhas.size(); i++) {
                if (linhas.get(i).startsWith("Conta Poupanca: Número: " + numeroConta)) {
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
    public void extrato(String tipo, double valor, int numConta) {
        super.operacoes.add(new Operacao(tipo, valor, numConta));
    }

    @Override
    public String infoConta() {
        return "Conta Poupanca: " + super.infoConta() + ", Rendimento Mensal: " + rendimentoMensal + "\n";
    }
}