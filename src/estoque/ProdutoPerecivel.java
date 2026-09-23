package estoque;

/**
 * Produto com validade. Quando faltam 3 dias ou menos para vencer, aplica
 * 20% de desconto automático no cálculo do valor total.
 */
public class ProdutoPerecivel extends Product {
    private int diasParaVencer;

    public ProdutoPerecivel(String nome, double preco, int quantidade, int diasParaVencer)
            throws QuantidadeInvalidaException {
        super(nome, preco, quantidade);
        this.diasParaVencer = diasParaVencer;
    }

    @Override
    public double calcularValorTotal() {
        return calcularValorParaQuantidade(getQuantidade());
    }

    @Override
    public double calcularValorParaQuantidade(int quantidade) {
        double total = getPreco() * quantidade;
        if (diasParaVencer <= 3) {
            total *= 0.8; // 20% de desconto automático perto do vencimento
        }
        return total;
    }

    @Override
    public String getDescricao() {
        String descricao;
        if (diasParaVencer <= 3) {
            // Mostra pro cliente/funcionário o preço original e o preço já com
            // desconto, em vez de deixar o desconto "escondido" só no total.
            double precoComDesconto = getPreco() * 0.8;
            descricao = String.format("%s | Preço: R$ %.2f (20%% de desconto por estar próximo do vencimento: R$ %.2f) | Quantidade: %d",
                getNome(), getPreco(), precoComDesconto, getQuantidade());
        } else {
            descricao = super.getDescricao();
        }
        return descricao + " | Vence em: " + diasParaVencer + " dia(s)";
    }

    public int getDiasParaVencer() {
        return diasParaVencer;
    }
}
