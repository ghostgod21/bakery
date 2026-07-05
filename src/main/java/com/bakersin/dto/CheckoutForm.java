package com.bakersin.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CheckoutForm {

    @NotBlank(message = "Please enter your full name")
    private String name;

    @NotBlank(message = "Please enter your email")
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotBlank(message = "Please enter your phone number")
    @Pattern(regexp = "^[0-9+\\-\\s]{7,15}$", message = "Please enter a valid phone number")
    private String phone;

    @NotBlank(message = "Please enter your delivery address")
    private String address;

    @NotBlank(message = "Please enter your city")
    private String city;

    @NotBlank(message = "Please enter your PIN code")
    @Pattern(regexp = "^[0-9]{6}$", message = "PIN code must be 6 digits")
    private String pin;

    @NotBlank(message = "Please choose a payment method")
    private String paymentMethod;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
