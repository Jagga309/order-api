package com.app.orderapi.utils;

public class EmailUtils {

    public static String getEmailMessage(String name, String host, String token){
        return  "Dear,"+ name +",\n\n"+
                "We're Delighted to confirm your successfull login to our Orderz's \n\n"+
                "Thank You for choosing us.\n\n"+
                "We ensure a Seamless Serving from our end...\n\n"+
                "Please Click the below link to Verify your Account \n"+
                getVerificationUrl(host,token)+"\n\n"+
                "Best Regards,\n"+
                "Orderz's Team";
    }

    private static String getVerificationUrl(String host, String token) {
        return "http://localhost:9998/users/confirm-account?token="+token;
    }
}
