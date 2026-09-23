# Sistema de Estoque de Produtos

Exercício de implementação da disciplina **Paradigmas de Linguagens de Programação** (UNIPÊ),
sobre Classes Abstratas, Herança, Interfaces, Polimorfismo, Composição e Exceções em Java.

## O que o programa faz

Simula o controle de estoque de uma loja, com dois tipos de produto (comuns e perecíveis),
venda de itens e tratamento de situações inválidas através de uma hierarquia de exceções
própria, em vez de `if`s soltos.

## Estrutura

```
src/estoque/
├── EstoqueException.java              -> exceção base do domínio
├── QuantidadeInvalidaException.java   -> preço/quantidade negativos no cadastro
├── ProdutoIndisponivelException.java  -> venda maior que o estoque disponível
├── Vendavel.java                      -> interface implementada por Product
├── Product.java                       -> classe abstrata (nome, preço, quantidade)
├── ProdutoComum.java                  -> valor total = preço x quantidade
├── ProdutoPerecivel.java              -> valor total com 20% de desconto se faltam <= 3 dias para vencer
├── Estoque.java                       -> composição: TEM uma lista de Product
└── EstoqueApp.java                    -> classe principal (main), com os testes/demonstrações
```

## Como compilar e rodar

Pelo terminal, na raiz do projeto:

```bash
javac -d out $(find src -name "*.java")
java -cp out estoque.EstoqueApp
```

Ou, no VS Code, basta abrir `src/estoque/EstoqueApp.java` e clicar em **Run** (com a extensão
Java instalada).

## O que a saída demonstra

1. Cadastro de 2 produtos comuns e 2 perecíveis (um deles vencendo em breve).
2. Tentativa de cadastro com quantidade negativa → `QuantidadeInvalidaException` capturada.
3. Listagem do estoque cadastrado.
4. Uma venda válida, reduzindo a quantidade do produto.
5. Tentativa de vender mais do que o disponível → `ProdutoIndisponivelException` capturada.
6. Valor total do estoque, somado por polimorfismo (cada produto calcula do seu próprio jeito,
   sem o `Estoque` precisar saber se é um `ProdutoComum` ou `ProdutoPerecivel`).
