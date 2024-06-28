import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import models.Conta;
import models.Cliente;
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

                    Cliente cliente = pesquisarCliente(cpf); // Usar a função modificada
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
                    Cliente cliente = pesquisarCliente(cpf); // Usar a função modificada
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
                                System.out.print("Digite o número da conta: ");
                                int numeroConta = Integer.parseInt(scanner.nextLine());
                                Conta conta = Conta.pesquisarConta(numeroConta);

                                if (conta != null) {
                                    if (conta instanceof Corrente) {
                                        Corrente cont = (Corrente) conta;
                                        System.out.println("Saldo: " + cont.getSaldo());
                                        System.out.print("Digite o valor que deseja sacar: ");
                                        String valorSaqueStr = scanner.nextLine();

                                        if (!valorSaqueStr.isEmpty()) {
                                            try {
                                                double valorSaque = Double.parseDouble(valorSaqueStr);
                                                cont.sacar(valorSaque);
                                                System.out.println(
                                                        "Saque de R$" + valorSaque + " realizado com sucesso.");
                                                System.out.println("Novo saldo: R$" + cont.getSaldo());
                                            } catch (NumberFormatException e) {
                                                System.out.println(
                                                        "Valor de saque inválido. Certifique-se de digitar um valor numérico válido.");
                                            } catch (Error e) {
                                                System.out.println(e.getMessage());
                                            }
                                        } else {
                                            System.out.println("Valor de saque não pode estar vazio.");
                                        }
                                    } else if (conta instanceof Poupanca) {
                                        Poupanca cont = (Poupanca) conta;
                                        System.out.println("Saldo: " + cont.getSaldo());
                                        System.out.print("Digite o valor que deseja sacar: ");
                                        String valorSaqueStr = scanner.nextLine();

                                        if (!valorSaqueStr.isEmpty()) {
                                            try {
                                                double valorSaque = Double.parseDouble(valorSaqueStr);
                                                cont.sacar(valorSaque);
                                                System.out.println(
                                                        "Saque de R$" + valorSaque + " realizado com sucesso.");
                                                System.out.println("Novo saldo: R$" + cont.getSaldo());
                                            } catch (NumberFormatException e) {
                                                System.out.println(
                                                        "Valor de saque inválido. Certifique-se de digitar um valor numérico válido.");
                                            } catch (Error e) {
                                                System.out.println(e.getMessage());
                                            }
                                        } else {
                                            System.out.println("Valor de saque não pode estar vazio.");
                                        }
                                    } else if (conta instanceof RendaFixa) {
                                        RendaFixa cont = (RendaFixa) conta;
                                        System.out.println("Saldo: " + cont.getSaldo());
                                        System.out.print("Digite o valor que deseja sacar: ");
                                        String valorSaqueStr = scanner.nextLine();

                                        if (!valorSaqueStr.isEmpty()) {
                                            try {
                                                double valorSaque = Double.parseDouble(valorSaqueStr);
                                                cont.sacar(valorSaque);
                                                System.out.println(
                                                        "Saque de R$" + valorSaque + " realizado com sucesso.");
                                                System.out.println("Novo saldo: R$" + cont.getSaldo());
                                            } catch (NumberFormatException e) {
                                                System.out.println(
                                                        "Valor de saque inválido. Certifique-se de digitar um valor numérico válido.");
                                            } catch (Error e) {
                                                System.out.println(e.getMessage());
                                            }
                                        } else {
                                            System.out.println("Valor de saque não pode estar vazio.");
                                        }
                                    } else if (conta instanceof Investimento) {
                                        Investimento cont = (Investimento) conta;
                                        System.out.println("Saldo: " + cont.getSaldo());
                                        System.out.print("Digite o valor que deseja sacar: ");
                                        String valorSaqueStr = scanner.nextLine();

                                        if (!valorSaqueStr.isEmpty()) {
                                            try {
                                                double valorSaque = Double.parseDouble(valorSaqueStr);
                                                cont.sacar(valorSaque);
                                                System.out.println(
                                                        "Saque de R$" + valorSaque + " realizado com sucesso.");
                                                System.out.println("Novo saldo: R$" + cont.getSaldo());
                                            } catch (NumberFormatException e) {
                                                System.out.println(
                                                        "Valor de saque inválido. Certifique-se de digitar um valor numérico válido.");
                                            } catch (Error e) {
                                                System.out.println(e.getMessage());
                                            }
                                        } else {
                                            System.out.println("Valor de saque não pode estar vazio.");
                                        }
                                    } else {
                                        System.out.println("Tipo de conta não reconhecido.");
                                    }
                                } else {
                                    System.out.println("Conta não encontrada.");
                                }
                                break;
                            case "2":
                                System.out.print("Digite o número da conta: ");
                                int num = Integer.parseInt(scanner.nextLine());
                                Conta con = Conta.pesquisarConta(num);

                                if (con != null) {
                                    if (con instanceof Corrente) {
                                        Corrente cont = (Corrente) con;
                                        System.out.println("Saldo: " + cont.getSaldo());
                                        System.out.print("Digite o valor que deseja sacar: ");
                                        String valorDepositoStr = scanner.nextLine();

                                        if (!valorDepositoStr.isEmpty()) {
                                            try {
                                                double valorDeposito = Double.parseDouble(valorDepositoStr);
                                                cont.depositar(valorDeposito);
                                                System.out.println(
                                                        "Deposito de R$" + valorDeposito + " realizado com sucesso.");
                                                System.out.println("Novo saldo: R$" + cont.getSaldo());
                                            } catch (NumberFormatException e) {
                                                System.out.println(
                                                        "Valor de Deposito inválido. Certifique-se de digitar um valor numérico válido.");
                                            } catch (Error e) {
                                                System.out.println(e.getMessage());
                                            }
                                        } else {
                                            System.out.println("Valor de deposito não pode estar vazio.");
                                        }
                                    } else if (con instanceof Poupanca) {
                                        Poupanca cont = (Poupanca) con;
                                        System.out.println("Saldo: " + cont.getSaldo());
                                        System.out.print("Digite o valor que deseja sacar: ");
                                        String valorDepositoStr = scanner.nextLine();

                                        if (!valorDepositoStr.isEmpty()) {
                                            try {
                                                double valorDeposito = Double.parseDouble(valorDepositoStr);
                                                cont.depositar(valorDeposito);
                                                System.out.println(
                                                        "Deposito de R$" + valorDeposito + " realizado com sucesso.");
                                                System.out.println("Novo saldo: R$" + cont.getSaldo());
                                            } catch (NumberFormatException e) {
                                                System.out.println(
                                                        "Valor de deposito inválido. Certifique-se de digitar um valor numérico válido.");
                                            } catch (Error e) {
                                                System.out.println(e.getMessage());
                                            }
                                        } else {
                                            System.out.println("Valor de saque não pode estar vazio.");
                                        }
                                    } else if (con instanceof RendaFixa) {
                                        RendaFixa cont = (RendaFixa) con;
                                        System.out.println("Saldo: " + cont.getSaldo());
                                        System.out.print("Digite o valor que deseja sacar: ");
                                        String valorDepositoStr = scanner.nextLine();

                                        if (!valorDepositoStr.isEmpty()) {
                                            try {
                                                double valorDeposito = Double.parseDouble(valorDepositoStr);
                                                cont.depositar(valorDeposito);
                                                System.out.println(
                                                        "Deposito de R$" + valorDeposito + " realizado com sucesso.");
                                                System.out.println("Novo saldo: R$" + cont.getSaldo());
                                            } catch (NumberFormatException e) {
                                                System.out.println(
                                                        "Valor de deposito inválido. Certifique-se de digitar um valor numérico válido.");
                                            } catch (Error e) {
                                                System.out.println(e.getMessage());
                                            }
                                        } else {
                                            System.out.println("Valor de deposito não pode estar vazio.");
                                        }
                                    } else if (con instanceof Investimento) {
                                        Investimento cont = (Investimento) con;
                                        System.out.println("Saldo: " + cont.getSaldo());
                                        System.out.print("Digite o valor que deseja sacar: ");
                                        String valorDepositoStr = scanner.nextLine();

                                        if (!valorDepositoStr.isEmpty()) {
                                            try {
                                                double valorDeposito = Double.parseDouble(valorDepositoStr);
                                                cont.depositar(valorDeposito);
                                                System.out.println(
                                                        "Deposito de R$" + valorDeposito + " realizado com sucesso.");
                                                System.out.println("Novo saldo: R$" + cont.getSaldo());
                                            } catch (NumberFormatException e) {
                                                System.out.println(
                                                        "Valor de deposito inválido. Certifique-se de digitar um valor numérico válido.");
                                            } catch (Error e) {
                                                System.out.println(e.getMessage());
                                            }
                                        } else {
                                            System.out.println("Valor de deposito não pode estar vazio.");
                                        }
                                    } else {
                                        System.out.println("Tipo de conta não reconhecido.");
                                    }
                                } else {
                                    System.out.println("Conta não encontrada.");
                                }
                                break;
                            case "3":
                                System.out.print("Digite o número da conta: ");
                                int n = Integer.parseInt(scanner.nextLine());
                                Conta c = Conta.pesquisarConta(n);

                                if (c != null) {
                                    System.out.println("O saldo da conta " + c.getNumero() + ": " + c.getSaldo());
                                } else {
                                    System.out.println("Conta não existe");
                                }
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
                System.out.println("Bem-vindo, XuBank escolha uma opção:");
                System.out.println("1 - Saldo Médio de Cada Tipo de Conta");
                System.out.println("2 - Cliente com maior saldo total");
                System.out.println("3 - Cliente com menor saldo total");
                String op = scanner.nextLine();
                //imprimirConta(lerContasDeArquivo());
                switch (op) {
                    case "1":

                        break;
                    case "2":
                        Cliente cliente = Conta.clienteRico();
                        System.out.println("Cliente rico "+cliente);
                        break;
                    case "3":
                        // //Cliente clientePobre = clientePobre();

                        // if (clientePobre != null) {
                        //     System.out.println("Cliente com menor saldo: " + clientePobre.getNome());
                        // } else {
                        //     System.out.println("Nenhum cliente encontrado.");
                        // }
                        break;                   
                }
            } else if (resposta.equals("0")) {
                System.out.println("Encerrando...");
                break;
            } else {
                System.out.println("Opção inválida.");
            }
        }

        scanner.close();
    }

    // public static Cliente clienteRico(List<Cliente> clientes, List<Conta> contas) {
    //     double maiorSaldo = 0;
    //     Cliente clienteRico = null;
    
    //     // Itera sobre todos os clientes
    //     for (Cliente cliente : clientes) {
    //         double saldoTotalCliente = 0;
    //         System.out.println("Nome Rico: " + cliente.getNome());
    
    //         // Itera sobre todas as contas
    //         for (Conta conta : contas) {
    //             // Verifica se a conta tem um cliente associado e se o CPF corresponde ao cliente atual do loop externo
    //             if (conta.getCliente() != null && conta.getCliente().getCpf().equals(cliente.getCpf())) {
    //                 saldoTotalCliente += conta.getSaldo();
    //                 System.out.println("Conta Saldo: " + conta.getSaldo());
    //                 System.out.println("Cliente: " + conta.getCliente().getCpf());
    //                 System.out.println("Saldo Rico: " + saldoTotalCliente);
    //             }
    //         }
    
    //         // Verifica se o saldo total do cliente atual é maior que o maior saldo registrado até agora
    //         if (saldoTotalCliente > maiorSaldo) {
    //             maiorSaldo = saldoTotalCliente;
    //             clienteRico = cliente;
    //         }
    //     }
    
    //     // Após percorrer todos os clientes e contas, imprime o cliente com o maior saldo ou avisa se nenhum cliente foi encontrado
    //     if (clienteRico != null) {
    //         System.out.println("Cliente com maior saldo: " + clienteRico.getNome());
    //     } else {
    //         System.out.println("Nenhum cliente encontrado.");
    //     }
    
    //     // Retorna o cliente com o maior saldo encontrado (ou null se nenhum cliente foi encontrado)
    //     return clienteRico;
    // }

    // public static Cliente clientePobre() {

    //     List<Cliente> clientes = lerClientesDoArquivo();
    //     //List<Conta> contas = lerContasDeArquivo();

    //     double menorSaldo = 0;
    //     Cliente clientePobre = null;

    //     for (Cliente cliente : clientes) {
    //         double saldoTotalCliente = 0;

    //         for (Conta conta : contas) {
    //             if (conta.getCliente() != null && conta.getCliente().getCpf().equals(cliente.getCpf())) {
    //                 saldoTotalCliente += conta.getSaldo();
    //             }
    //         }

    //         if (saldoTotalCliente < menorSaldo) {
    //             menorSaldo = saldoTotalCliente;
    //             clientePobre = cliente;
    //         }
    //     }

    //     return clientePobre;
    // }

    public static void imprimirConta(List<Conta> contas) {
        for(Conta conta : contas){
            System.out.println("Numero "+conta.getNumero());
            System.out.println("Cliente "+conta.getCliente());
            System.out.println("Saldo "+conta.getSaldo());

        }
    }


    // public static List<Conta> lerContasDeArquivo() {
    //     List<Conta> contas = new ArrayList<>();
    //     List<Cliente> clientes = lerClientesDoArquivo(); // Ler os clientes do arquivo antes de ler as contas
    
    //     try (BufferedReader reader = new BufferedReader(new FileReader("clientes.txt"))) {
    //         String line;
    //         while ((line = reader.readLine()) != null) {
    //             if (line.startsWith("Conta Corrente")) {
    //                 contas.add(criarContaCorrente(line, clientes));
    //             } else if (line.startsWith("Conta Poupanca")) {
    //                 contas.add(criarContaPoupanca(line, clientes));
    //             } else if (line.startsWith("Conta Renda Fixa")) {
    //                 contas.add(criarContaRendaFixa(line, clientes));
    //             } else if (line.startsWith("Conta Investimento")) {
    //                 contas.add(criarContaInvestimento(line, clientes));
    //             }
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    
    //     return contas;
    // }

    private static Conta criarContaCorrente(String line) {
        Cliente cliente = extrairCliente(line); // Verifique se extrairCliente está retornando o cliente correto
        int numero = extrairNumeroConta(line);
        double saldo = extrairSaldo(line);
        double limite = extrairLimiteCredito(line);
        return new Corrente(cliente, numero, saldo, limite);
    }
    
    private static Conta criarContaPoupanca(String line) {
        Cliente cliente = extrairCliente(line); // Verifique se extrairCliente está retornando o cliente correto
        int numero = extrairNumeroConta(line);
        double saldo = extrairSaldo(line);
        return new Poupanca(cliente, numero, saldo);
    }
    
    private static Conta criarContaRendaFixa(String line) {
        Cliente cliente = extrairCliente(line); // Verifique se extrairCliente está retornando o cliente correto
        int numero = extrairNumeroConta(line);
        double saldo = extrairSaldo(line);
        double imposto = extrairImposto(line);
        double taxaFixa = extrairTaxaFixa(line);
        double rendimentoMensal = extrairRendimentoMensal(line);
        double valorRendimentoMensal = extrairValorRendimentoMensal(line);
        return new RendaFixa(cliente, numero, saldo, imposto, taxaFixa, rendimentoMensal, valorRendimentoMensal);
    }
    
    private static Conta criarContaInvestimento(String line) {
        Cliente cliente = extrairCliente(line); // Verifique se extrairCliente está retornando o cliente correto
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
            return linha.substring(inicioIndex).trim(); // Retorna o resto da linha se o marcador final não for
                                                        // encontrado
        }

        return linha.substring(inicioIndex, fimIndex).trim();
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
}
