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

                    Cliente cliente = Cliente.pesquisarCliente(cpf); // Usar a função modificada
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
                    Cliente cliente = Cliente.pesquisarCliente(cpf); // Usar a função modificada
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
                                        System.out.print("Digite o valor que deseja depositar: ");
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
                                        System.out.print("Digite o valor que deseja depositar: ");
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
                                    } else if (con instanceof RendaFixa) {
                                        RendaFixa cont = (RendaFixa) con;
                                        System.out.println("Saldo: " + cont.getSaldo());
                                        System.out.print("Digite o valor que deseja depositar: ");
                                        String valorDepositoStr = scanner.nextLine();

                                        if (!valorDepositoStr.isEmpty()) {
                                            try {
                                                double valorDeposito = Double.parseDouble(valorDepositoStr);
                                                //cont.depositar(valorDeposito);
                                                double sald = cont.depositar(valorDeposito);
                                                System.out.println(
                                                        "Deposito de R$" + valorDeposito + " realizado com sucesso.");
                                                System.out.println("Novo saldo: R$" + sald);
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
                                        System.out.print("Digite o valor que deseja depositar: ");
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
                            case "4":
                                System.out.print("Digite o número da conta: ");
                                int nu = Integer.parseInt(scanner.nextLine());
                                Conta cn = Conta.pesquisarConta(nu);
                                if (cn != null) {
                                    Conta.extratoConta(nu);
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
                        Conta.clienteRico();
                        

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
}
