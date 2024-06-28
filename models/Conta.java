package models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public abstract class Conta {
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

    public abstract void extrato(String tipo, double valor, int numConta);

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

    public static void extratoConta(int numConta) {
        List<Operacao> operacoes = lerOperacoesDeArquivo();
        System.out.println("Extrato do Último Mês");
        List<Operacao> opera = new ArrayList<>();
    
        Date dataAtual = new Date();
    
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(dataAtual);
        calendario.add(Calendar.MONTH, -1);
    
        Date dataLimite = calendario.getTime();
    
        for (Operacao operacao : operacoes) {
            if (operacao.getConta() == numConta && operacao.getData().after(dataLimite)) {
                opera.add(operacao);
            }
        }
    
        if (opera.isEmpty()) {
            System.out.println("Nenhuma operação encontrada no último mês.");
        } else {
            for (Operacao operacao : opera) {
                System.out.println("---------------------");
                System.out.println("Tipo: " + operacao.getTipo() + " Valor: " + operacao.getValor() + " Data: " + operacao.getData());
            }
        }
    }

    public static void impriConta(){
        List<Conta> contas = lerContasDeArquivo();

        for(Conta conta : contas){
            System.out.println("Cliente Conta: "+conta.getCliente());
        }
    }

    public static Cliente clienteRico() {
        List<Cliente> clientes = Cliente.lerClientesDoArquivo();
        List<Conta> contas = lerContasDeArquivo();

        double maiorSaldo = 0;
        Cliente clienteRico = null;
    
        // Itera sobre todos os clientes
        for (Cliente cliente : clientes) {
            double saldoTotalCliente = 0;
            System.out.println("Nome Rico: " + cliente.getNome());
    
            // Itera sobre todas as contas
            for (Conta conta : contas) {
                if (conta.getCliente() != null && conta.getCliente().getCpf().equals(cliente.getCpf())) {
                    saldoTotalCliente += conta.getSaldo();
                    System.out.println("Conta Saldo: " + conta.getSaldo());
                    System.out.println("Cliente: " + conta.getCliente().getCpf());
                    System.out.println("Saldo Rico: " + saldoTotalCliente);
                }
            }
    
            // Verifica se o saldo total do cliente atual é maior que o maior saldo registrado até agora
            if (saldoTotalCliente > maiorSaldo) {
                maiorSaldo = saldoTotalCliente;
                clienteRico = cliente;
            }
        }
    
        // Após percorrer todos os clientes e contas, imprime o cliente com o maior saldo ou avisa se nenhum cliente foi encontrado
        if (clienteRico != null) {
            System.out.println("Cliente com maior saldo: " + clienteRico.getNome());
        } else {
            System.out.println("Nenhum cliente encontrado.");
        }
    
        // Retorna o cliente com o maior saldo encontrado (ou null se nenhum cliente foi encontrado)
        return clienteRico;
    }


    public static String validarTipoConta(int numeroConta) {
        List<Conta> contas = lerContasDeArquivo();

        for (Conta conta : contas) {
            if (conta.getNumero() == numeroConta) {
                if (conta instanceof Corrente) {
                    Corrente contaCor = new Corrente(null, numeroConta);
                    return "Corrente";
                } else if (conta instanceof Poupanca) {
                    return "Poupanca";
                } else if (conta instanceof RendaFixa) {
                    return "Renda Fixa";
                } else if (conta instanceof Investimento) {
                    return "Investimento";
                }
            }
        }

        return "Conta não encontrada";
    }

    public void editarContaNoArquivo(int numeroConta, double novoSaldo) {
        File arquivo = new File("clientes.txt");
        List<String> linhas = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Conta Corrente: Número: " + numeroConta)) {
                    linha = linha.replaceFirst("Saldo: [0-9]+\\.[0-9]+", "Saldo: " + novoSaldo);
                }
                linhas.add(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Escreve todas as linhas de volta no arquivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            for (String linha : linhas) {
                writer.write(linha + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Conta pesquisarConta(int numConta){
        List<Conta> contas = lerContasDeArquivo();

        for(Conta conta : contas){
            if(conta.getNumero() == numConta){
                return conta;
            }
        }

        throw new Error("Conta não existe");
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

    public static Cliente extrairCliente(String line) {
        List<Cliente> clientes = Cliente.lerClientesDoArquivo();

        String inicioMarcador = "Cliente: ";
        String fimMarcador = ", ";
        String cpf = extrairValorEntreMarcadores(line, inicioMarcador, fimMarcador);
        System.out.println(cpf);


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

    public static List<Operacao> lerOperacoesDeArquivo() {
        List<Operacao> operacoes = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("clientes.txt"));
            String line = reader.readLine();
            while (line != null) {
                if (line.startsWith("Operacao")) {
                    operacoes.add(criarOperacao(line));
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return operacoes;
    }
    
    private static Operacao criarOperacao(String line){
        String tipo = extrairTipoExtrato(line);
        double valor = extrairValorExtrato(line);
        Date data = extrairDataExtrato(line);
        int conta = extrairContaExtrato(line);
        return new Operacao(tipo, valor, data, conta);
    }

    private static Date extrairDataExtrato(String line) {
        String inicioMarcador = "Data: ";
        String fimMarcador = ",";
        String dataStr = extrairValorEntreMarcadores(line, inicioMarcador, fimMarcador);
        
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        try {
            return format.parse(dataStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static String extrairTipoExtrato(String line) {
        String inicioMarcador = "Operacao ";
        String fimMarcador = ",";
        return extrairValorEntreMarcadores(line, inicioMarcador, fimMarcador);
    }
    
    private static double extrairValorExtrato(String line) {
        String inicioMarcador = "Valor: ";
        String fimMarcador = ",";
        return Double.parseDouble(extrairValorEntreMarcadores(line, inicioMarcador, fimMarcador));
    }
    
    private static int extrairContaExtrato(String line) {
        String inicioMarcador = "Conta: ";
        String fimMarcador = ".";
        return Integer.parseInt(extrairValorEntreMarcadores(line, inicioMarcador, fimMarcador));
    }
    

    public String infoConta() {
        return "Número: " + numero + ", Saldo: " + saldo + ", Cliente: " + cliente.getCpf();
    }
}