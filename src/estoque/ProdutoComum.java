package estoque;

/**
 * Produto sem nenhuma regra especial: valor total = preço x quantidade.
 */
public class ProdutoComum extends Product {
    public ProdutoComum(String nome, double preco, int quantidade) throws QuantidadeInvalidaException {
        super(nome, preco, quantidade);
    }

    @Override
    public double calcularValorTotal() {
        return calcularValorParaQuantidade(getQuantidade());
    }

    @Override
    public double calcularValorParaQuantidade(int quantidade) {
        return getPreco() * quantidade;
    }
}
