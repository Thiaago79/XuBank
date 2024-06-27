package models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

abstract class Conta {
    private int numero;
    private double saldo;
    private Cliente cliente;
    protected List<Operacao> operacoes;

    public Conta(Cliente cliente) {
        this.numero = num();
        this.saldo = 0;
        this.cliente = cliente;
        this.operacoes = new ArrayList<>();
    }

    public Conta(Cliente cliente, int numero, double saldo) {
        this.numero = numero;
        this.saldo = saldo;
        this.cliente = cliente;
        this.operacoes = new ArrayList<>();
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public abstract double depositar(double valor);

    public abstract double sacar(double valor);

    public abstract double rendimentoMensal();

    public abstract void extrato(String tipo, double valor);

    public static int num() {
        List<Conta> contas = lerContasDeArquivo();
        int maiorAtual = 0;
    
        for (Conta conta : contas) {
            if (conta.getNumero() >= maiorAtual) {
                maiorAtual = conta.getNumero();
            }
        }
    
        return maiorAtual + 1;
    }

    public static List<Conta> lerContasDeArquivo() {
        List<Conta> contas = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("clientes.txt"));
            String line = reader.readLine();
            while (line != null) {
                if (line.startsWith("Conta Corrente")) {
                    contas.add(criarContaCorrente(line));
                } else if (line.startsWith("Conta Poupanca")) {
                    contas.add(criarContaPoupanca(line));
                } else if (line.startsWith("Conta Renda Fixa")) {
                    contas.add(criarContaRendaFixa(line));
                } else if (line.startsWith("Conta Investimento")) {
                    contas.add(criarContaInvestimento(line));
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contas;
    }

    private static Conta criarContaCorrente(String line) {
        Cliente cliente = extrairCliente(line);
        int numero = extrairNumeroConta(line);
        double saldo = extrairSaldo(line);
        double limite = extrairLimiteCredito(line);
        return new Corrente(cliente, numero, saldo, limite);
    }

    private static Conta criarContaPoupanca(String line) {
        Cliente cliente = extrairCliente(line);
        int numero = extrairNumeroConta(line);
        double saldo = extrairSaldo(line);
        
        return new Poupanca(cliente, numero, saldo);
    }

    private static Conta criarContaRendaFixa(String line) {
        Cliente cliente = extrairCliente(line);
        int numero = extrairNumeroConta(line);
        double saldo = extrairSaldo(line);
        double imposto = extrairImposto(line);
        double taxaFixa = extrairTaxaFixa(line);
        double rendimentoMensal = extrairRendimentoMensal(line);
        double valorRendimentoMensal = extrairValorRendimentoMensal(line);
        return new RendaFixa(cliente, numero, saldo, imposto, taxaFixa, rendimentoMensal, valorRendimentoMensal);
    }

    private static Conta criarContaInvestimento(String line) {
        Cliente cliente = extrairCliente(line);
        int numero = extrairNumeroConta(line);
        double saldo = extrairSaldo(line);
        double imposto = extrairImposto(line);
        double taxaFixa = extrairTaxaFixa(line);
        double rendimentoMensal = extrairRendimentoMensal(line);
        double valorRendimentoMensal = extrairValorRendimentoMensal(line);
        System.out.println("Num inv" +numero);
        return new Investimento(cliente, numero, saldo, imposto, taxaFixa, rendimentoMensal, valorRendimentoMensal);
    }

    public static Cliente extrairCliente(String cpf) {
        List<Cliente> clientes = lerClientesDoArquivo();
        for (Cliente cliente : clientes) {
            if (cliente.getCpf().equals(cpf)) {
                return cliente;
            }
        }
        return null; // Cliente não encontrado
    }

    private static int extrairNumeroConta(String line) {
        String inicioMarcador = "Número: ";
        String fimMarcador = ", Saldo: ";
        return Integer.parseInt(extrairValorEntreMarcadores(line, inicioMarcador, fimMarcador));
    }

    private static double extrairSaldo(String line) {
        String inicioMarcador = "Saldo: ";
        String fimMarcador = ", Cliente: ";
        return Double.parseDouble(extrairValorEntreMarcadores(line, inicioMarcador, fimMarcador));
    }

    private static double extrairLimiteCredito(String line) {
        String inicioMarcador = "Limite de Credito: ";
        return Double.parseDouble(extrairValorEntreMarcadores(line, inicioMarcador, ""));
    }

    private static double extrairImposto(String line) {
        String inicioMarcador = "Imposto: ";
        String fimMarcador = ", Taxa Fixa: ";
        return Double.parseDouble(extrairValorEntreMarcadores(line, inicioMarcador, fimMarcador));
    }

    private static double extrairTaxaFixa(String line) {
        String inicioMarcador = "Taxa Fixa: ";
        String fimMarcador = ", Rendimento Mensal: ";
        return Double.parseDouble(extrairValorEntreMarcadores(line, inicioMarcador, fimMarcador));
    }

    private static double extrairRendimentoMensal(String line) {
        String inicioMarcador = "Rendimento Mensal: ";
        String fimMarcador = ", Valor do Rendimento Mensal";
        return Double.parseDouble(extrairValorEntreMarcadores(line, inicioMarcador, fimMarcador));
    }

    private static double extrairValorRendimentoMensal(String line) {
        String[] partes = line.split(", ");
        for (String parte : partes) {
            if (parte.startsWith("Rendimento Mensal: ")) {
                String valorRendimento = parte.substring("Rendimento Mensal: ".length()).trim();
                // Remover caracteres não numéricos extras, se houver
                valorRendimento = valorRendimento.replaceAll("[^\\d.]", ""); // Remove tudo exceto dígitos e ponto
                try {
                    return Double.parseDouble(valorRendimento);
                } catch (NumberFormatException e) {
                    System.err.println("Erro ao converter rendimento mensal: " + e.getMessage());
                }
            }
        }
        return 0.0; // Valor padrão se não encontrar ou ocorrer erro na conversão
    }

    protected static String extrairValorEntreMarcadores(String linha, String marcadorInicio, String marcadorFim) {
        int inicioIndex = linha.indexOf(marcadorInicio) + marcadorInicio.length();
        int fimIndex = marcadorFim.isEmpty() ? linha.length() : linha.indexOf(marcadorFim, inicioIndex);

        if (inicioIndex < marcadorInicio.length()) {
            throw new IllegalArgumentException("Marcador inicial não encontrado na linha: " + linha);
        }
        if (fimIndex < 0) {
            return linha.substring(inicioIndex).trim(); // Retorna o resto da linha se o marcador final não for encontrado
        }

        return linha.substring(inicioIndex, fimIndex).trim();
    }


    public static List<Cliente> lerClientesDoArquivo() {
        List<Cliente> clientes = new ArrayList<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader("clientes.txt"))) {
            String linha;
    
            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Nome: ") && linha.contains("CPF: ") && linha.contains("Senha: ")) {
                    String[] partes = linha.split(", ");
                    String nome = partes[0].substring("Nome: ".length()).trim();
                    String cpf = partes[1].substring("CPF: ".length()).trim();
                    String senha = partes[2].substring("Senha: ".length()).trim();
    
                    Cliente cliente = new Cliente(nome, cpf, senha);
                    clientes.add(cliente);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return clientes;
    }

    public static Cliente pesquisarCliente(String cpf) {
        List<Cliente> clientes = lerClientesDoArquivo();
        for (Cliente cliente : clientes) {
            if (cliente.getCpf().equals(cpf)) {
                return cliente;
            }
        }
        return null;
    }

    public String infoConta() {
        return "Número: " + numero + ", Saldo: " + saldo + ", Cliente: " + cliente.getCpf();
    }
}
