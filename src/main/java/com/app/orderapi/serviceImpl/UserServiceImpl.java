package com.app.orderapi.serviceImpl;

import com.app.orderapi.DTO.UserDTO;
import com.app.orderapi.entities.*;
import com.app.orderapi.exceptions.DuplicateUserException;
import com.app.orderapi.exceptions.ItemNotFoundException;
import com.app.orderapi.exceptions.UserNotFoundException;
import com.app.orderapi.repository.*;
import com.app.orderapi.service.EmailService;
import com.app.orderapi.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private final ConfirmationRepository confirmationRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDTO createUser(User user) {
        if(isEmailIdUnique(user.getEmailId())){
            throw new DuplicateUserException("The emailId already Exists !!! ");
        }
        else{
            Cart temporaryCart = new Cart();
            temporaryCart.setId(user.getId());
            user.setCart(temporaryCart);

            UserDTO userDTO = convertEntityToDTO(user);
            Cart cart = user.getCart();
            cart.setUser(user);

            userRepository.save(user);

            user.setEnabled(false);
            Confirmation confirmation = new Confirmation(user);
            confirmationRepository.save(confirmation);

            // Sending the Email --------------->
            String name = user.getFirstName()+" "+user.getLastName();
            emailService.sendSimpleMailMessage(name,user.getEmailId(),confirmation.getToken());

            return userDTO;
        }
    }

    @Override
    public Boolean verifyToken(String token) {

        Confirmation confirmation = confirmationRepository.findByToken(token);
        User user = userRepository.findByEmailIdIgnoreCase(confirmation.getUser().getEmailId());
        user.setEnabled(true);
        userRepository.save(user);
        return true;

    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("No such User Exists!!!"));

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setEmailId(updatedUser.getEmailId());
        existingUser.setPassword(updatedUser.getPassword());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        Confirmation confirmation = confirmationRepository.findByUserId(id);
        if(optionalUser.isPresent()){
            confirmationRepository.deleteById(confirmation.getId());
            userRepository.deleteById(id);
        }else{
            throw new UserNotFoundException("The User you are trying to delete , does't Exist!!!");
        }
    }

    @Override
    public boolean isEmailIdUnique(String emailId) {

        List<User> users = userRepository.findAll();
        String emailWithoutSpaces = emailId.replaceAll("\\s","").toLowerCase();

        return users.stream()
                .map(User :: getEmailId)
                .map(existingEmail -> existingEmail.replaceAll("\\s","").toLowerCase())
                .anyMatch(itr -> itr.compareToIgnoreCase(emailWithoutSpaces)==0);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> optionalItem = userRepository.findById(id);
        return optionalItem.orElse(null);
    }

    @Override
    public UserDTO convertEntityToDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmailId(user.getEmailId());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        return userDTO;
    }

    @Override
    public User addItemsToCart(Long userId, Long itemId,int quantity) {

        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("No such User Exists..."));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(()->new ItemNotFoundException("No such Item Exists..."));

        Pricing pricing = item.getPricing();

        Cart cart = user.getCart();

        List<CartItem> cartItems = cart.getCartItemList();

        boolean itemExists = false;
        itemExists = itemsExistsOrNot(cartItems,itemId);

        CartItem cartItem = new CartItem();
        if(!itemExists){

            cartItem.setItem(item);
            if(pricing.getDiscount()!=0.0) cartItem.setPrice(pricing.getBasePrice()-((pricing.getBasePrice())*(pricing.getDiscount()/100)));
            else cartItem.setPrice(pricing.getBasePrice());
            cartItem.setCart(cart);
            cartItem.setQuantity(quantity);

            cart.addToCartItemList(cartItem);
        }

        double totalPrice = 0.0;
        for(CartItem itr : cart.getCartItemList()){
            totalPrice = totalPrice + itr.getPrice()*itr.getQuantity();
        }
        cart.setTotalPrice(totalPrice);

        user.setCart(cart);
        return userRepository.save(user);
    }

    @Override
    public void removeItemFromCart(Long userId, Long itemId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("No such User Exists..."));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(()->new ItemNotFoundException("No such Item Exists..."));

        Cart cart = user.getCart();
        Iterator<CartItem> itr = cart.getCartItemList().iterator();

        while(itr.hasNext()){
            CartItem cartItem = itr.next();
            if(cartItem.getItem().getId() == itemId){
                cart.removeFromCartItemList(cartItem);
                cartItemRepository.deleteById(cartItem.getId());
                break;
            }
        }
        double totalPrice = 0.0;
        for(CartItem itrx : cart.getCartItemList()){
            totalPrice = totalPrice + itrx.getPrice()*itrx.getQuantity();
        }
        cart.setTotalPrice(totalPrice);
        user.setCart(cart);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public User removeAllItemsFromCart(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("No such User Exists..."));

        Cart cart = user.getCart();

        Iterator<CartItem> iterator = cart.getCartItemList().iterator();
        while(iterator.hasNext()){
            CartItem cartItem = iterator.next();
            cartItemRepository.deleteById(cartItem.getId());
            iterator.remove();
            entityManager.flush();
        }
        cart.setCartItemList(Collections.emptyList());
        cart.setTotalPrice(0.0);
        user.setCart(cart);
        return userRepository.save(user);
    }

    @Override
    public List<User> searchUsers(String query) {
        List<User> users = userRepository.searchUsers(query);
        return users;
    }

    public boolean itemsExistsOrNot(List<CartItem> cartItems , Long itemId){

        for(CartItem item : cartItems){
            if(item.getItem().getId() == itemId){
                item.setQuantity(item.getQuantity()+1);
                return true;
            }
        }
        return false;
    }
}
