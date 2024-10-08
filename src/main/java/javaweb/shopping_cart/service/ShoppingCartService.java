package javaweb.shopping_cart.service;

import javaweb.shopping_cart.entity.Product;
import javaweb.shopping_cart.exception.NotEnoughProductsInStockException;

import java.math.BigDecimal;
import java.util.Map;

public interface ShoppingCartService {
    void addProduct(Product product);
    void removeProduct(Product product);
    Map<Product, Integer> getProductsInCart();
    void checkout() throws NotEnoughProductsInStockException;
    BigDecimal getTotal();
}
