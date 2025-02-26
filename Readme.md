
# Projeto do bootcamp DecolaTech

Uma api simples para gerenciamento de contas de banco.

## Diagrama de classes
```mermaid
classDiagram
    class Usuario {
        +String name
        +Account account
        +Feature[] features
        +Card card
        +News[] news
    }
    class Account {
        +String number
        +String agency
        +Double balance
        +Double limit
    }
    class Feature {
        +String icon
        +String description
    }
    class Card {
        +String number
        +Double limit
    }
    class News {
        +String icon
        +String description
    }

    Usuario "1"-->"1" Account
    Usuario "1"-->"n" Feature
    Usuario "1"-->"1" Card
    Usuario "1"-->"n" News

