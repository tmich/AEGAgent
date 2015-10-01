package it.aeg2000srl.aegagent.services;

import android.content.Context;

import java.util.List;

import it.aeg2000srl.aegagent.core.IOrderItemRepository;
import it.aeg2000srl.aegagent.core.OrderItem;
import it.aeg2000srl.aegagent.core.OrderItemRepository;

/**
 * Created by tiziano.michelessi on 01/10/2015.
 */
public class OrderItemService {
    IOrderItemRepository repo;
    Context context;

    public OrderItemService(Context context, IOrderItemRepository repository) {
        this.context = context;
        this.repo = repository;
    }

    public OrderItemService(Context context) {
        this.context = context;
        this.repo = new OrderItemRepository(context);
    }

    public Iterable<OrderItem> getItemsByOrderId(long orderId) {
        return repo.getByOrderId(orderId);
    }
}
