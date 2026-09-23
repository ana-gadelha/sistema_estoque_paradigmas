package estoque;

import java.util.NoSuchElementException;
import java.util.Scanner;

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

        // Aqui o polimorfismo está em ação nos bastidores: calcularValorTotalEstoque()
        // chama p.calcularValorTotal() pra cada item sem saber se é ProdutoComum ou
        // ProdutoPerecivel — cada um resolve o próprio cálculo (com ou sem desconto).
        System.out.println("\n=== 6. Valor total do estoque ===");
        System.out.printf("Valor total: R$ %.2f%n", estoque.calcularValorTotalEstoque());

        System.out.println("\n=== 7. Simulação de compra ===");
        // Item extra (não pedido no enunciado): digite um índice e uma quantidade
        // pra ver o cálculo de preço aberto — subtotal, desconto e valor final —
        // exatamente como o cliente veria no caixa. Se não houver teclado disponível
        // nesta execução (ex.: o professor só rodando pra ler a saída), essa parte
        // é simplesmente pulada, sem quebrar o resto do programa.
        try (Scanner scanner = new Scanner(System.in)) {
            estoque.listarProdutos();
            System.out.print("Escolha o índice do produto: ");
            int indice = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Quantas unidades deseja simular? ");
            int quantidade = Integer.parseInt(scanner.nextLine().trim());

            Product produto = estoque.getProduto(indice);
            double precoUnitario = produto.getPreco();
            double subtotal = precoUnitario * quantidade;
            double valorFinal = produto.calcularValorParaQuantidade(quantidade);
            double desconto = subtotal - valorFinal;

            System.out.println("\nProduto: " + produto.getDescricao());
            System.out.printf("Preço unitário: R$ %.2f%n", precoUnitario);
            System.out.println("Quantidade informada: " + quantidade);
            System.out.printf("Subtotal (sem desconto): R$ %.2f%n", subtotal);
            System.out.printf("Desconto: R$ %.2f%n", desconto);
            System.out.printf("Valor final: R$ %.2f%n", valorFinal);
        } catch (NoSuchElementException | IllegalStateException e) {
            System.out.println("(Simulação pulada — sem entrada de teclado disponível nesta execução.)");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida: digite apenas números inteiros.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
