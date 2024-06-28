package models;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Corrente extends Conta {
    private double limite;

    public Corrente(Cliente cliente, double limite) {
        super(cliente);
        this.limite = limite;
    }

    public Corrente(Cliente cliente, int numero, double saldo, double limite) {
        super(cliente, numero, saldo);
        this.limite = limite;
    }

    public double getLimite() {
        return limite;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }

    public double depositar(double valorDepositar) {

        double saldo = super.getSaldo();

        if (saldo < 0) {
            double tarifa = saldo * 0.03;
            saldo = (saldo - tarifa) - 10;
            saldo = saldo + valorDepositar;
            System.out.println("Slaso atual "+saldo);
            Operacao operacao = new Operacao("Deposito", valorDepositar, super.getNumero());
            editarContaNoArquivo(getNumero(), operacao, saldo);
        } else {
            saldo = saldo + valorDepositar;
            System.out.println("Slaso atual "+saldo);
            Operacao operacao = new Operacao("Deposito", saldo, super.getNumero());
            editarContaNoArquivo(getNumero(), operacao, saldo);
        }

        super.setSaldo(saldo);

        return saldo;
    }

    public double sacar(double valorSacar) {

        // -12 > -10-20 = -30
        //-12 > -30

        double saldo = super.getSaldo();

        if (saldo <= 0 &&  (this.limite * (-1)) <= (saldo - valorSacar) ) {
            System.out.println("Saldo < 0 : " + this.limite * (-1));
            System.out.println("Saldo < 0");
            saldo = saldo - valorSacar;
            Operacao operacao = new Operacao("Saque", valorSacar, super.getNumero());
            editarContaNoArquivo(getNumero(), operacao, saldo);
        } else if (saldo > 0) {
            if (valorSacar <= (saldo + this.limite)) {

                System.out.println("Saldo > 0");

                saldo = saldo - valorSacar;
                Operacao operacao = new Operacao("Saque", valorSacar, super.getNumero());
                editarContaNoArquivo(getNumero(), operacao, saldo);
            } else {
                throw new Error("Valor a sacar excedeu o limite");
            }
        } else {
            throw new Error("Valor a sacar excedeu o limite");
        }

        super.setSaldo(saldo);
        

        return saldo;
    }

    public void extrato(String tipo, double valor, int numConta) {
        super.operacoes.add(new Operacao(tipo, valor, numConta));
    }

    public double rendimentoMensal() {
        return 0;
    }

    public void editarContaNoArquivo(int numeroConta, Operacao operacao, double novoSaldo) {
        try {
            List<String> linhas = Files.readAllLines(Paths.get("clientes.txt"), StandardCharsets.UTF_8);

            for (int i = 0; i < linhas.size(); i++) {
                if (linhas.get(i).startsWith("Conta Corrente: Número: " + numeroConta)) {
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
        return "Conta Corrente: " + super.infoConta() + ", Limite de Credito: " + limite + "\n";
    }

}
