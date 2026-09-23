package estoque;

import java.util.ArrayList;
import java.util.List;

/**
 * Estoque TEM UMA lista de produtos (composição) — não herda de Product.
 */
public class Estoque {
    private List<Product> produtos;

    public Estoque() {
        this.produtos = new ArrayList<>();
    }

    public void adicionarProduto(Product p) {
        produtos.add(p);
    }

    public void venderProduto(int indice, int quantidade) throws ProdutoIndisponivelException {
        if (indice < 0 || indice >= produtos.size()) {
            throw new IllegalArgumentException("Índice de produto inválido: " + indice);
        }
        produtos.get(indice).vender(quantidade);
    }

    public double calcularValorTotalEstoque() {
        double total = 0.0;
        for (Product p : produtos) {
            // Polimorfismo em ação: cada produto sabe calcular o seu próprio valor,
            // o Estoque não precisa saber se é comum ou perecível.
            total += p.calcularValorTotal();
        }
        return total;
    }

    public void listarProdutos() {
        for (int i = 0; i < produtos.size(); i++) {
            System.out.println("[" + i + "] " + produtos.get(i).getDescricao());
        }
    }
}
