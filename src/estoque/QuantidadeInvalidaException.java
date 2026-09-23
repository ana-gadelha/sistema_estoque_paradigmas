package estoque;

/**
 * Lançada quando um produto é criado com preço ou quantidade negativos.
 */
public class QuantidadeInvalidaException extends EstoqueException {
    public QuantidadeInvalidaException(String mensagem) {
        super(mensagem);
    }
}
