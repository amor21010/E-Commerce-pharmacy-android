package com.aboesmail.omar.pharma.Repository.order;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.aboesmail.omar.pharma.Api.order.Order;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {
    LiveData<List<Order>> userOrders;
    OrderRepository repository;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        repository = new OrderRepository();

    }

    public void updateOrderQuantities(String orderID, String productID, int newQ) {
        repository.updateOrderQuantities(orderID, productID, newQ);
    }

    public void updateTotal(String orderID, double total) {
        repository.updateTotalPrice(orderID, total);
    }

    public Order getOrderDetails(String id) {
        return repository.getOrderDetails(id);
    }

    public List<Order> getUserOrders(String useName) {
        return repository.getUserOrder(useName);
    }
}
