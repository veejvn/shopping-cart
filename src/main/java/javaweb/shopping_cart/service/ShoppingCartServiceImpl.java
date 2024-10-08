package javaweb.shopping_cart.service;

import javaweb.shopping_cart.entity.Product;
import javaweb.shopping_cart.exception.NotEnoughProductsInStockException;
import javaweb.shopping_cart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Transactional
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService{
    @Autowired
    private ProductRepository productRepository;
    private final Map<Product, Integer> products = new HashMap<>();

    @Override
    public void addProduct(Product product) {
        if (products.containsKey(product)) {
            products.replace(product, products.get(product) + 1);
        } else {
            products.put(product, 1);
        }
    }

    @Override
    public void removeProduct(Product product) {
        if(products.containsKey(product)) {
            if(products.get(product) > 1) {
                products.replace(product, products.get(product) - 1);
            }
            else if(products.get(product) == 1){
                products.remove(product);
            }
        }
    }

    @Override
    public Map<Product, Integer> getProductsInCart(){
        return Collections.unmodifiableMap(products);
    }

    @Override
    public void checkout() throws NotEnoughProductsInStockException{
        Product product;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            product = productRepository.getReferenceById(entry.getKey().getId());
            if(product.getQuantity() < entry.getValue()) throw new NotEnoughProductsInStockException(product);
            entry.getKey().setQuantity(product.getQuantity() - entry.getValue());
        }
        productRepository.saveAll(products.keySet());
        productRepository.flush();
    }

    @Override
    public BigDecimal getTotal() {
        return products.entrySet().stream().map(entry -> entry.getKey().getPrice()
                .multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }
}
