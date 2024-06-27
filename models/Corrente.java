package models;

public class Corrente extends Conta{
    private double limite;

    public Corrente(Cliente cliente, double limite) {
        super(cliente);
        this.limite = limite;
    }

    public Corrente(Cliente cliente, int numero, double saldo, double limite) {
        super(cliente,numero, saldo);
        this.limite = limite;
    }

    public double getLimite() {
        return limite;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }

    public double depositar(double valorDepositar){

        double saldo = super.getSaldo();

        if(saldo < 0){
            saldo = saldo - (saldo * 0.03) - 10;
            saldo = saldo + valorDepositar;
        }else{
            saldo = saldo + valorDepositar;
        }

        super.setSaldo(saldo);
        extrato("Deposito", valorDepositar);

        return saldo;
    }

    public double sacar(double valorSacar){

        double saldo = super.getSaldo();

        if(saldo < 0 && saldo - valorSacar < (this.limite * (-1))){
            saldo = saldo - valorSacar;
        }else if(saldo > 0){
            if(valorSacar < saldo + this.limite){
                saldo = saldo - valorSacar;
            }else{
                throw new Error("Valor a sacar excedeu o limite");
            }
        }else{
            throw new Error("Valor a sacar excedeu o limite");
        }

        super.setSaldo(saldo);
        extrato("Saque", valorSacar);
        
        return saldo;
    }

    public void extrato(String tipo, double valor){
        super.operacoes.add(new Operacao(tipo, valor));
    }

    public double rendimentoMensal() {
        return 0;
    }

    @Override
    public String infoConta() {
        return "Conta Corrente: " + super.infoConta() + ", Limite de Credito: " + limite + "\n";
    }


}
