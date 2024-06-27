import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import models.Cliente;
// import models.Conta;
import models.Corrente;
import models.Investimento;
import models.Poupanca;
import models.RendaFixa;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        

        System.out.println("Cadastro de Clientes");
        System.out.println("--------------------");

        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Cadastrar novo cliente");
            System.out.println("2 - Cadastrar nova conta");
            System.out.println("3 - Acessar conta");
            System.out.println("4 - Informações do XuBank");
            System.out.println("0 - Cancelar");

            String resposta = scanner.nextLine();

            if (resposta.equals("1")) {
                Cliente cliente = Cliente.criarNovoCliente();
                cliente.salvarCliente();                
                System.out.println("Cliente cadastrado com sucesso!");
            } else if (resposta.equals("2")) {
                System.out.print("Digite seu CPF: ");
                String cpf = scanner.nextLine();
                System.out.print("Digite sua senha: ");
                String senha = scanner.nextLine();

                if (Cliente.senhaExiste(cpf, senha)) {
                    System.out.println("Qual tipo de conta deseja cadastrar?");
                    System.out.println("1 - Conta Corrente");
                    System.out.println("2 - Poupança");
                    System.out.println("3 - Renda Fixa");
                    System.out.println("4 - Investimento");

                    String tipoConta = scanner.nextLine();

                    Cliente cliente = Cliente.pesquisarCliente(cpf);
                    if (cliente != null) {
                        switch (tipoConta) {
                            case "1":
                                System.out.print("Digite o valor do crédito especial (limite): ");
                                double limite = 0.0;
                                try {
                                    limite = Double.parseDouble(scanner.next());
                                } catch (NumberFormatException e) {
                                    System.out.println("Valor inválido para o limite. Utilizando valor padrão (0.0).");
                                }
                                scanner.nextLine(); // Limpar o buffer após ler o número
                                
                                Corrente contaCorrente = new Corrente(cliente, limite);
                                cliente.criarConta(contaCorrente);
                                System.out.println("Conta Corrente criada com sucesso!");
                                break;
                            case "2":
                                Poupanca poupanca = new Poupanca(cliente);
                                cliente.criarConta(poupanca);
                                System.out.println("Poupança criada com sucesso!");
                                break;
                            case "3":
                                RendaFixa rendaFixa = new RendaFixa(cliente);
                                cliente.criarConta(rendaFixa);
                                System.out.println("Renda Fixa criada com sucesso!");
                                break;
                            case "4":
                                Investimento investimento = new Investimento(cliente);
                                cliente.criarConta(investimento);
                                System.out.println("Investimento criado com sucesso!");
                                break;
                            default:
                                System.out.println("Opção inválida.");
                        }
                    } else {
                        System.out.println("Cliente não encontrado.");
                    }
                } else {
                    System.out.println("CPF ou senha inválidos.");
                }
            } else if (resposta.equals("3")) {
                System.out.print("Digite seu CPF: ");
                String cpf = scanner.nextLine();
                System.out.print("Digite sua senha: ");
                String senha = scanner.nextLine();

                if (Cliente.senhaExiste(cpf, senha)) {
                    Cliente cliente = Cliente.pesquisarCliente(cpf);
                    if (cliente != null) {
                        System.out.println("Bem-vindo, " + cliente.getNome() + "! Escolha uma opção:");
                        System.out.println("1 - Sacar");
                        System.out.println("2 - Depositar");
                        System.out.println("3 - Verificar saldo");
                        System.out.println("4 - Ver extrato");
                        System.out.println("0 - Cancelar");

                        String opcao = scanner.nextLine();
                        switch (opcao) {
                            case "1":
                                // Implementar lógica de saque
                                break;
                            case "2":
                                // Implementar lógica de depósito
                                break;
                            case "3":
                                // Implementar lógica de verificação de saldo
                                break;
                            case "4":
                                // Implementar lógica de ver extrato
                                break;
                            case "0":
                                System.out.println("Operação cancelada.");
                                break;
                            default:
                                System.out.println("Opção inválida.");
                        }
                    } else {
                        System.out.println("Cliente não encontrado.");
                    }
                } else {
                    System.out.println("CPF ou senha inválidos.");
                }
            } else if (resposta.equals("4")) {
                // Implementar lógica para mostrar informações do XuBank
            } else if (resposta.equals("0")) {
                System.out.println("Encerrando...");
                break;
            } else {
                System.out.println("Opção inválida.");
            }
        }

        scanner.close();
    }

    public static Cliente pesquis(String cpf) {

        List<Cliente> clientes = lerClientesDoArquivo("clientes.txt");

        for(Cliente cliente : clientes){
            if(cliente.getCpf().equals(cpf)){
                return cliente;
            }
        }
        return null;
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
    
}