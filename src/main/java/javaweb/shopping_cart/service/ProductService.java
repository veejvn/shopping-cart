package javaweb.shopping_cart.service;


import javaweb.shopping_cart.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {
    Optional<Product> findById(Long id);
    Page<Product> findAllProductsPageable(Pageable pageable);
}
