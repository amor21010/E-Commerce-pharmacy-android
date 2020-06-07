package com.aboesmail.omar.pharma.Repository.product;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.aboesmail.omar.pharma.Api.Category.Category;
import com.aboesmail.omar.pharma.Api.Product.Product;
import com.aboesmail.omar.pharma.Database.product.CartProduct;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {


    private LiveData<List<Product>> getAllProduct;
    private LiveData<List<Category>> getCategory;


    private LiveData<List<CartProduct>> getAllCartProducts;

    private ProductRepository repository;

    public ProductViewModel(Application application) {
        super(application);
        repository = new ProductRepository(application);
        getAllProduct = repository.getAllProduct();
        getCategory = repository.getCategory();
        getAllCartProducts = repository.getAllCartProducts();
    }


    public void patchQuantity(String id, int orderedQuantity, int oldQ) {
        repository.patchQuantity(id, orderedQuantity, oldQ);
    }

    public void insert(CartProduct cartProduct) {
        repository.insertProduct(cartProduct);
    }

    void update(CartProduct cartProduct) {
        repository.updateProduct(cartProduct);
    }

    public void delete(CartProduct cartProduct) {
        repository.deleteProduct(cartProduct);
    }

    public void deleteAll() {
        repository.deleteAllProduct();
    }

    public void addToCart(String id, int count, LifecycleOwner activity) {
        repository.addToCart(id, count, activity);
    }

    public LiveData<Product> getProductInfo(String id) {
        return repository.getProductInfo(id);
    }

    public LiveData<List<Product>> getAllProduct() {
        return getAllProduct;
    }

    public LiveData<List<Category>> getCategory() {
        return repository.getCategory();
    }

    public LiveData<List<CartProduct>> getAllCartProducts() {
        return getAllCartProducts;
    }


}
