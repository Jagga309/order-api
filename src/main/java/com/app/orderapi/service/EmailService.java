package com.app.orderapi.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {

    void sendSimpleMailMessage(String name, String to , String token);

}
