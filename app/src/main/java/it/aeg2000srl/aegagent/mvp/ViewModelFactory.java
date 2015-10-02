package it.aeg2000srl.aegagent.mvp;

import android.content.Context;

import java.util.ArrayList;

import it.aeg2000srl.aegagent.core.Customer;
import it.aeg2000srl.aegagent.core.Order;
import it.aeg2000srl.aegagent.core.OrderItem;
import it.aeg2000srl.aegagent.core.Product;
import it.aeg2000srl.aegagent.infrastructure.CustomerRepository;
import it.aeg2000srl.aegagent.infrastructure.ProductRepository;

/**
 * Created by tiziano.michelessi on 02/10/2015.
 */
public class ViewModelFactory {
    protected Context context;

    public ViewModelFactory(Context context){
        this.context = context;
    }

    public Customer toCustomer(CustomerViewModel customerViewModel) {
        Customer customer = new Customer();
        customer.setId(customerViewModel.Id);
        customer.setCode(customerViewModel.Code);
        customer.setName(customerViewModel.Name);
        customer.setAddress(customerViewModel.Address);
        customer.setCap(customerViewModel.Cap);
        customer.setProv(customerViewModel.Province);
        customer.setCity(customerViewModel.City);
        customer.setTelephone(customerViewModel.Telephone);
        customer.setVatNumber(customerViewModel.Iva);

        return customer;
    }

    public ProductViewModel toProductViewModel(Product p) {
        ProductViewModel pvm = new ProductViewModel();
        pvm.Id = p.getId();
        pvm.Code = p.getCode();
        pvm.Name = p.getName();
        pvm.Price = p.getPrice();

        return pvm;
    }

    public Product toProduct(ProductViewModel productViewModel) {
        Product product = new Product();
        product.setId(productViewModel.Id);
        product.setPrice(productViewModel.Price);
        product.setCode(productViewModel.Code);
        product.setName(productViewModel.Name);


        return product;
    }

    public CustomerViewModel toCustomerViewModel(Customer customer) {
        CustomerViewModel customerViewModel = new CustomerViewModel();
        customerViewModel.Id = customer.getId();
        customerViewModel.Code = customer.getCode();
        customerViewModel.Name = customer.getName();
        customerViewModel.Address = customer.getAddress();
        customerViewModel.City = customer.getCity();
        customerViewModel.Cap = customer.getCap();
        customerViewModel.Province = customer.getProv();
        customerViewModel.Telephone = customer.getTelephone();
        customerViewModel.Iva = customer.getVatNumber();

        return customerViewModel;
    }


    public OrderViewModel toOrderViewModel(Order order) {
        OrderViewModel orderViewModel = new OrderViewModel();
        orderViewModel.Id = order.getId();
        orderViewModel.Customer = toCustomerViewModel(order.getCustomer());
        orderViewModel.CreationDate = order.getCreationDate();
        orderViewModel.Notes = order.getNotes();
        orderViewModel.SentDate = order.getSentDate();
        orderViewModel.UserId = order.getUserId();
        orderViewModel.Items = new ArrayList<>();
        for (OrderItem orderItem : order.getItems()) {
            orderViewModel.Items.add(toOrderItemViewModel(orderItem));
        }
        return orderViewModel;
    }

    public OrderItemViewModel toOrderItemViewModel(OrderItem orderItem) {
        ProductRepository productRepository = new ProductRepository(context);
        OrderItemViewModel orderItemViewModel = new OrderItemViewModel();
        orderItemViewModel.Quantity = orderItem.getQty();
        orderItemViewModel.Notes = orderItem.getNotes();
        orderItemViewModel.Discount = orderItem.getDiscount();
        orderItemViewModel.Product = toProductViewModel(productRepository.getById(orderItem.getProductId()));

        return orderItemViewModel;
    }

    public Order toOrder(OrderViewModel ovm) {
        CustomerRepository customerRepository = new CustomerRepository(context);
        Customer customer = customerRepository.getById(ovm.Customer.Id);

        Order order = new Order(customer);
        order.setId(ovm.Id);
        order.setCreationDate(ovm.CreationDate);
        order.setNotes(ovm.Notes);
        order.setUserId(ovm.UserId);

        for(OrderItemViewModel it : ovm.Items) {
            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setProductId(it.Product.Id);
            item.setQty(it.Quantity);
            item.setNotes(it.Notes);
            item.setDiscount(it.Discount);
            order.add(item);
        }

        return order;
    }
}
