package com.example.customer.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private Address address;
}
