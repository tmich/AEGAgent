package it.aeg2000srl.aegagent.mvp;

import java.util.Date;
import java.util.List;

import it.aeg2000srl.aegagent.core.OrderItem;
import it.aeg2000srl.aegagent.core.Product;

/**
 * Created by tiziano.michelessi on 02/10/2015.
 */
public class OrderViewModel {
    public long Id;
    public CustomerViewModel Customer;
    public Date CreationDate;
    public String Notes;
    public long UserId;
    public Date SentDate;
    public List<OrderItemViewModel> Items;
}
