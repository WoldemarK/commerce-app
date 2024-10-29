package com.example.product.service;

import com.example.product.exception.ProductPurchaseException;
import com.example.product.mapper.ProductMapper;
import com.example.product.model.product.*;
import com.example.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper mapper;
    private final ProductRepository repository;

    public Integer createProduct(final ProductRequest request) {
        final Product product = this.repository.save(this.mapper.toProduct(request));
        return product.getId();
    }

    public ProductResponse findById(Integer id) {
        return this.repository.findById(id)
                .map(this.mapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID:: " + id));
    }

    public List<ProductResponse> findAll() {
        return this.repository.findAll()
                .stream()
                .map(this.mapper::toProductResponse)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = ProductPurchaseException.class)
    public List<ProductPurchaseResponse> purchaseProducts(final List<ProductPurchaseRequest> request) throws ProductPurchaseException {

        List<Product> storedProducts = this.repository.findAllByIdInOrderById(request.stream()
                .map(ProductPurchaseRequest::productId)
                .toList());

        List<ProductPurchaseRequest> sortedRequest = request.stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        List<ProductPurchaseResponse> purchasedProducts = new ArrayList<>();

        for (int i = 0; i < storedProducts.size(); i++) {
            Product product = storedProducts.get(i);
            ProductPurchaseRequest productRequest = sortedRequest.get(i);

            if (product.getAvailableQuantity() < productRequest.quantity()) {
                throw new ProductPurchaseException(MessageFormat.format(
                        "Insufficient stock quantity for product with ID:: {0}", productRequest.productId()));
            }
            double newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);
            this.repository.save(product);
            purchasedProducts.add(this.mapper.toproductPurchaseResponse(product, productRequest.quantity()));
        }
        return purchasedProducts;
    }
}
