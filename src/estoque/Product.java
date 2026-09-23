package estoque;

/**
 * Classe abstrata que representa um produto genérico do estoque.
 * Cada subclasse decide, do seu próprio jeito, como calcular o valor total
 * (é aí que entra o polimorfismo dinâmico).
 */
public abstract class Product implements Vendavel {
    private String nome;
    private double preco;
    private int quantidade;

    public Product(String nome, double preco, int quantidade) throws QuantidadeInvalidaException {
        if (preco < 0 || quantidade < 0) {
            throw new QuantidadeInvalidaException(
                "Preço e quantidade não podem ser negativos (produto: \"" + nome + "\").");
        }
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    // Método abstrato: cada subclasse decide como calcular
    public abstract double calcularValorTotal();

    // Método concreto: descrição formatada
    public String getDescricao() {
        return String.format("%s | Preço: R$ %.2f | Quantidade: %d", nome, preco, quantidade);
    }

    // Implementação da interface Vendavel
    @Override
    public void vender(int quantidadeDesejada) throws ProdutoIndisponivelException {
        if (quantidadeDesejada > this.quantidade) {
            throw new ProdutoIndisponivelException(
                "Estoque insuficiente para \"" + nome + "\": disponível " + this.quantidade
                + ", solicitado " + quantidadeDesejada + ".");
        }
        this.quantidade -= quantidadeDesejada;
    }

    // Sobrecarga (polimorfismo estático) - versão 1: desconto simples por percentual
    public void aplicarDesconto(double percentual) {
        this.preco -= this.preco * (percentual / 100.0);
    }

    // Sobrecarga (polimorfismo estático) - versão 2: com teto de desconto em R$
    public void aplicarDesconto(double percentual, double descontoMaximo) {
        double valorDesconto = this.preco * (percentual / 100.0);
        if (valorDesconto > descontoMaximo) {
            valorDesconto = descontoMaximo;
        }
        this.preco -= valorDesconto;
    }

    // Getters protegidos: as subclasses precisam deles para calcular o valor total,
    // mas os atributos continuam encapsulados (private) para o resto do programa.
    protected String getNome() { return nome; }
    protected double getPreco() { return preco; }
    protected int getQuantidade() { return quantidade; }
}
