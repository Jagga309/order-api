package com.app.orderapi.serviceImpl;

import com.app.orderapi.DTO.OrderDTO;
import com.app.orderapi.DTO.OrderDetailsDTO;
import com.app.orderapi.DTO.UserOrderDTO;
import com.app.orderapi.entities.*;
import com.app.orderapi.enums.OrderStatus;
import com.app.orderapi.enums.PaymentType;
import com.app.orderapi.exceptions.AddressNotFoundException;
import com.app.orderapi.exceptions.ItemNotFoundException;
import com.app.orderapi.exceptions.OrderNotFoundException;
import com.app.orderapi.exceptions.UserNotFoundException;
import com.app.orderapi.repository.AddressRepository;
import com.app.orderapi.repository.ItemRepository;
import com.app.orderapi.repository.OrderRepository;
import com.app.orderapi.repository.UserRepository;
import com.app.orderapi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public OrderDTO createOrder(Long userID) {

        User user = userRepository.findById(userID)
                .orElseThrow(()->new UserNotFoundException("User Not Found with that ID!!!"));
        Cart cart = user.getCart();
        List<CartItem> cartItemList = cart.getCartItemList();
        List<Address> addressList = user.getAddressList();

        if(!cartItemList.isEmpty() && !addressList.isEmpty()){

           for(CartItem cartItem : cartItemList){
               Order order = new Order();
               order.setOrderStatus(OrderStatus.ORDER_PLACED);
               order.setLocalDateTime(LocalDateTime.now());
               order.setUser(user);
               order.setAddress(addressList.get(0));
               order.setItem(cartItem.getItem());

               orderRepository.save(order);
           }
        }
        return null;
    }

    @Override
    public OrderDTO createOrder(Long userId, Long addressId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User Not Found with that ID!!!"));
        Address address = addressRepository.findById(addressId)
                .orElseThrow(()->new AddressNotFoundException("Address Does't Exists!!!"));

        Cart cart = user.getCart();
        List<CartItem> cartItemList = cart.getCartItemList();
        if(!cartItemList.isEmpty()){

            for(CartItem cartItem : cartItemList){
                Order order = new Order();
                order.setOrderStatus(OrderStatus.ORDER_PLACED);
                order.setLocalDateTime(LocalDateTime.now());
                order.setUser(user);
                order.setAddress(address);
                order.setItem(cartItem.getItem());

                orderRepository.save(order);
            }
        }
        return null;
    }

    @Override
    public OrderDTO createOrder(Long userId, Long addressId, Long itemID) {

        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User with that ID does't exists"));
        Address address = addressRepository.findById(addressId)
                .orElseThrow(()->new AddressNotFoundException("Address Does't Exists!!!"));
        Item item = itemRepository.findById(itemID)
                .orElseThrow(()->new ItemNotFoundException("Item Does't Exist!!!"));

        Order order = new Order();
        order.setOrderStatus(OrderStatus.ORDER_PLACED);
        order.setLocalDateTime(LocalDateTime.now());
        order.setUser(user);
        order.setAddress(address);
        order.setItem(item);

        orderRepository.save(order);
        return convertEntityToDTO(order);
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderID) {

        Order order = orderRepository.findById(orderID)
                .orElseThrow(()->new OrderNotFoundException("order does't Exist..."));
        order.setOrderStatus(order.getOrderStatus().next());
        orderRepository.save(order);
        return convertEntityToDTO(order);
    }

    @Override
    public List<Order> findOrderByUserEmail(String emailID) {
        return null;
    }

    @Override
    public String cancelOrder(Long orderId) {
        orderRepository.deleteById(orderId);
        return "Order Cancelled!!!";
    }

    @Override
    public OrderDTO getOrderDetailsById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new OrderNotFoundException("Order Does't Exists..."));
        return convertEntityToDTO(order);
    }

    @Override
    public List<OrderDTO> searchOrders(String query) {

        List<User> users = userRepository.searchUsers(query);
        List<OrderDTO> orderDTOList = new ArrayList<>();
        for(User user : users){
            orderDTOList.addAll(orderRepository.searchOrders(user.getId())
                    .stream().map(this::convertEntityToDTO).collect(Collectors.toList()));
        }
        return orderDTOList;
    }

    @Override
    public List<OrderDTO> searchOrdersByOrderStatus(String orderStatus) {

        return  orderRepository.searchOrderByStatus(orderStatus)
                .stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserOrderDTO getPreviousOrders(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User with that ID does't exists"));

        UserOrderDTO userOrderDTO = new UserOrderDTO();
        userOrderDTO.setUserName(user.getFirstName()+" "+user.getLastName());
        userOrderDTO.setEmailId(user.getEmailId());
        userOrderDTO.setPhoneNumber(user.getPhoneNumber());

        List<Order> previousOrders = orderRepository.getPreviousOrders(userId);
        List<OrderDetailsDTO> orderDetailsDTOList = new ArrayList<>();

        orderDetailsDTOList.addAll(previousOrders.stream()
                .map(this::convertion).collect(Collectors.toList()));

        userOrderDTO.setOrderDetailsDTOList(orderDetailsDTOList);

        return userOrderDTO;
    }


    public OrderDetailsDTO convertion(Order order){

        OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO();
        double discount = order.getItem().getPricing().getDiscount();
        double basePrice = order.getItem().getPricing().getBasePrice();
        double totalPrice = basePrice - (basePrice * discount /100);
        orderDetailsDTO.setQuantity(1);
        orderDetailsDTO.setItemName(order.getItem().getName());
        orderDetailsDTO.setTotalPrice(totalPrice);
        orderDetailsDTO.setAddress(order.getAddress().getStreetNumber()+","+order.getAddress().getCity());
        return orderDetailsDTO;
    }
























    public OrderDTO convertEntityToDTO(Order order){

        OrderDTO orderDTO = new OrderDTO();
        User user = order.getUser();
        Address address = order.getAddress();

        orderDTO.setOrderId(order.getId());
        orderDTO.setUserName(user.getFirstName()+" "+user.getLastName());
        orderDTO.setPhoneNumber(user.getPhoneNumber());
        orderDTO.setEmailId(user.getEmailId());
        orderDTO.setItem(order.getItem().getName());
        orderDTO.setPrice(order.getItem().getPricing().getBasePrice());
        orderDTO.setAddress(address.getStreetNumber()+","+address.getCity()+","+address.getState());
        orderDTO.setOrderStatus(order.getOrderStatus());
        orderDTO.setPaymentType(PaymentType.COD);

        return orderDTO;
    }



}
