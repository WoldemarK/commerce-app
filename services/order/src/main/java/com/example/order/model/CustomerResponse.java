package com.example.order.model;

public record CustomerResponse
        (
                String id,
                String firstname,
                String lastname,
                String email
        ) {
}
