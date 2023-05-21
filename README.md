

# GraphQL
## Learn
 - [Bootiful Spring Boot 3 by Josh Long](https://youtu.be/4QtW1KVZJRI?t=2340)
 - [Baeldung: Getting Started with GraphQL and Spring Boot](https://www.baeldung.com/spring-graphql)

## Client
### GraphiQL
GraphQL schema (`schema.graphqls`):
```graphql
type Profile {
    id: ID
}

type Customer {
    id: ID
    name: String
    profile: Profile
}

type Query {
    customers : [Customer]
}
```

Life query with [Localhost: GraphiQL](http://localhost:9090/graphiql) it offers control spaceablility:
```graphql
{
  customers {
    id
    name
    profile {
      id
    }
  }
}
```
