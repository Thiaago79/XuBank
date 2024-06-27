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
        List<Cliente> clientes = lerClientesDoArquivo("clientes.txt");
        for (Cliente cliente : clientes) {
            if (cliente.getCpf().equals(cpf)) {
                return true;
            }
        }
        return false;
    }

    public static Cliente pesquisarCliente(String cpf) {
        List<Cliente> clientes = lerClientesDoArquivo("clientes.txt");
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
            writer.newLine(); // Adiciona uma nova linha após cada cliente
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
        List<Cliente> clientes = lerClientesDoArquivo("clientes.txt");
        for (Cliente cliente : clientes) {
            if (cliente.getCpf().equals(cpf) && cliente.getSenha().equals(senha)) {
                return true;
            }
        }
        return false;
    }

    public static List<Cliente> lerClientesDoArquivo(String nomeArquivo) {
        List<Cliente> clientes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo))) {
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

    @Override
    public String toString() {
        return "Nome: " + nome + ", CPF: " + cpf + ", Senha: " + senha;
    }
}
