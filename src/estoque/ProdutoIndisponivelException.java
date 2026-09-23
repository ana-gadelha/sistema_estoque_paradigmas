package estoque;

/**
 * Lançada quando se tenta vender mais unidades do que existem em estoque.
 */
public class ProdutoIndisponivelException extends EstoqueException {
    public ProdutoIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
