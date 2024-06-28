package models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cliente {
    private String nome;
    private String cpf;
    private String senha;
    private List<Conta> contas;

    public Cliente(String nome, String cpf, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.senha = senha;
        this.contas = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public String getCpf() {
        return cpf;
    }

    public List<Conta> getContas() {
        return contas;
    }

    public static boolean clienteExiste(String cpf) {
        List<Cliente> clientes = lerClientesDoArquivo();
        for (Cliente cliente : clientes) {
            if (cliente.getCpf().equals(cpf)) {
                return true;
            }
        }
        return false;
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

    public static Cliente criarNovoCliente() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Criando um novo cliente...");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        String cpf;
        while (true) {
            System.out.print("CPF: ");
            cpf = scanner.nextLine();

            if (!clienteExiste(cpf)) {
                break;
            } else {
                System.out.println("CPF já existente. Por favor, tente novamente.");
            }
        }

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        return new Cliente(nome, cpf, senha);
    }

    public void criarConta(Conta conta) {
        contas.add(conta);
        salvarConta(conta);
        System.out.println("Conta criada com sucesso para o cliente " + nome + ".");
        System.out.println("Número da conta: " + conta.getNumero());
    }

    public void salvarCliente() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("clientes.txt", true))) {
            writer.write(this.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void salvarConta(Conta conta) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("clientes.txt", true))) {
            writer.write(conta.infoConta());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean senhaExiste(String cpf, String senha) {
        List<Cliente> clientes = lerClientesDoArquivo();
        for (Cliente cliente : clientes) {
            if (cliente.getCpf().equals(cpf) && cliente.getSenha().equals(senha)) {
                return true;
            }
        }
        return false;
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
        return new Investimento(cliente, numero, saldo, imposto, taxaFixa, rendimentoMensal, valorRendimentoMensal);
    }

    public static Cliente extrairCliente(String cpf) {
        List<Cliente> clientes = lerClientesDoArquivo();
        for (Cliente cliente : clientes) {
            if (cliente.getCpf().equals(cpf)) {
                return cliente;
            }
        }
        return null;
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
                valorRendimento = valorRendimento.replaceAll("[^\\d.]", ""); 
                try {
                    return Double.parseDouble(valorRendimento);
                } catch (NumberFormatException e) {
                    System.err.println("Erro ao converter rendimento mensal: " + e.getMessage());
                }
            }
        }
        return 0.0;
    }

    protected static String extrairValorEntreMarcadores(String linha, String marcadorInicio, String marcadorFim) {
        int inicioIndex = linha.indexOf(marcadorInicio) + marcadorInicio.length();
        int fimIndex = marcadorFim.isEmpty() ? linha.length() : linha.indexOf(marcadorFim, inicioIndex);

        if (inicioIndex < marcadorInicio.length()) {
            throw new IllegalArgumentException("Marcador inicial não encontrado na linha: " + linha);
        }
        if (fimIndex < 0) {
            return linha.substring(inicioIndex).trim();
        }

        return linha.substring(inicioIndex, fimIndex).trim();
    }

    @Override
    public String toString() {
        return "Nome: " + nome + ", CPF: " + cpf + ", Senha: " + senha;
    }
}
