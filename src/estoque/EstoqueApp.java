package estoque;

public class EstoqueApp {
    public static void main(String[] args) {
        Estoque estoque = new Estoque();

        System.out.println("=== 1. Cadastrando produtos válidos ===");
        try {
            estoque.adicionarProduto(new ProdutoComum("Caderno", 20.0, 10));
            estoque.adicionarProduto(new ProdutoComum("Caneta", 3.0, 50));
            estoque.adicionarProduto(new ProdutoPerecivel("Iogurte", 4.0, 15, 5)); // > 3 dias: sem desconto
            estoque.adicionarProduto(new ProdutoPerecivel("Carne", 40.0, 10, 2));  // <= 3 dias: com desconto
            System.out.println("4 produtos cadastrados com sucesso.");
        } catch (QuantidadeInvalidaException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        } catch (EstoqueException e) {
            System.out.println("Erro genérico de estoque: " + e.getMessage());
        }

        System.out.println("\n=== 2. Tentando cadastrar produto com quantidade negativa ===");
        try {
            Product produtoInvalido = new ProdutoComum("Produto Errado", 10.0, -5);
            estoque.adicionarProduto(produtoInvalido);
        } catch (QuantidadeInvalidaException e) {
            // Único checked exception que este bloco pode lançar, então basta este catch.
            System.out.println("[Capturado com sucesso] " + e.getMessage());
        }

        System.out.println("\n=== 3. Estoque cadastrado ===");
        estoque.listarProdutos();

        System.out.println("\n=== 4. Venda bem-sucedida (2 unidades do produto [0]) ===");
        try {
            estoque.venderProduto(0, 2);
            System.out.println("Venda realizada com sucesso!");
            estoque.listarProdutos();
        } catch (ProdutoIndisponivelException e) {
            System.out.println("Erro na venda: " + e.getMessage());
        }

        System.out.println("\n=== 5. Tentando vender mais do que o disponível (produto [1]) ===");
        // venderProduto() só declara "throws ProdutoIndisponivelException", então só
        // é permitido capturar aqui ProdutoIndisponivelException e seus supertipos
        // (EstoqueException, Exception...) — não QuantidadeInvalidaException, que é
        // uma exceção "irmã" e nunca poderia ser lançada por este bloco.
        // Ainda assim, o catch mais específico (ProdutoIndisponivelException) vem
        // antes do mais genérico (EstoqueException), como pede o enunciado: colocar
        // o genérico primeiro tornaria o catch seguinte inalcançável.
        try {
            estoque.venderProduto(1, 1000);
        } catch (ProdutoIndisponivelException e) {
            System.out.println("[Capturado com sucesso] " + e.getMessage());
        } catch (EstoqueException e) {
            // Rede de segurança para qualquer outra EstoqueException futura.
            System.out.println("Erro genérico de estoque: " + e.getMessage());
        }

        System.out.println("\n=== 6. Valor total do estoque (polimorfismo em ação) ===");
        System.out.printf("Valor total: R$ %.2f%n", estoque.calcularValorTotalEstoque());
    }
}
