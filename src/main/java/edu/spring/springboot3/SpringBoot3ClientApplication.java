package edu.spring.springboot3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@SpringBootApplication
public class SpringBoot3ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot3ClientApplication.class, args);
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> ready(CustomerClient customerClient) {
        return event -> customerClient.all().subscribe(System.out::println);
    }

    @Bean
    CustomerClient customerClient(WebClient.Builder httpBuilder) {
        var webClient =  httpBuilder
                .baseUrl("http://localhost:8080/")
                .build();
        return HttpServiceProxyFactory
                .builder()
                .clientAdapter(WebClientAdapter.forClient(webClient))
                .build()
                .createClient(CustomerClient.class);
    }
}

@Controller
class GraphqlController {
    private final CustomerClient customerClient;

    GraphqlController(CustomerClient customerClient) {
        this.customerClient = customerClient;
    }

    @QueryMapping
    Flux<Customer> customers() {
        return this.customerClient.all();
    }

    @BatchMapping
    Map<Customer, Profile> profile(List<Customer> customerList) {
/*
        var map = new HashMap<Customer, Profile>();
        for (var customer : customerList) {
            map.put(customer, new Profile(customer.id()));
        }
        return map;
*/
/*
        return customerList.stream().collect(Collectors.toMap(
           customer -> customer, // Function.identity(),
           customer -> new Profile(customer.id())
        ));
*/

        return customerList.stream().collect(Collectors.toMap(identity(), customer -> new Profile(customer.id())));
    }

/*
        @SchemaMapping(typeName = "Customer")
        Profile profile(Customer customer) {
            return new Profile(customer.id());
        }
*/
}

record Profile(Integer id) {
}

record Customer(Integer id, String name) {
}

interface CustomerClient {
    @GetExchange("/customer")
    Flux<Customer> all();
}


