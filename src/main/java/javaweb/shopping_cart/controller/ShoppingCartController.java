package javaweb.shopping_cart.controller;

import javaweb.shopping_cart.entity.paging.CurrentPageTransporter;
import javaweb.shopping_cart.service.ProductService;
import javaweb.shopping_cart.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ProductService productService;

    @GetMapping("/shoppingCart")
    public ModelAndView shoppingCart(){
        ModelAndView modelAndView = new ModelAndView("/shoppingCart");
        modelAndView.addObject("products", shoppingCartService.getProductsInCart());
        modelAndView.addObject("total", shoppingCartService.getTotal().toString());

        return modelAndView;
    }

    @GetMapping("/shoppingCart/addProduct/{productId}")
    public String addProductToCartStayCurrentPage(@PathVariable("productId") Long productId){
        productService.findById(productId).ifPresent(shoppingCartService::addProduct);
        int qsCurrentPage = CurrentPageTransporter.getCurrentPage();

        return "redirect:/products" + "?page=" + qsCurrentPage;
    }

}
