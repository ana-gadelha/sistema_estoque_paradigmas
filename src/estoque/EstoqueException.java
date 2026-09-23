package estoque;

/**
 * Exceção base para qualquer problema relacionado ao estoque.
 * As exceções mais específicas (QuantidadeInvalidaException,
 * ProdutoIndisponivelException) herdam dela.
 */
public class EstoqueException extends Exception {
    public EstoqueException(String mensagem) {
        super(mensagem);
    }
}
