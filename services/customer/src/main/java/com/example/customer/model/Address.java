package com.example.customer.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private String street;
    private String houseNumber;
    private String zipCode;
}
