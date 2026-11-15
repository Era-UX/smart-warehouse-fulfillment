package com.smartwarehouse.model;

// Yerassyl - Immutable shipping address record
public record Address(
        String street,
        String city,
        String region,
        String postalCode,
        String country
) {
    public Address {
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Street address cannot be blank.");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City cannot be blank.");
        }
        if (region == null || region.isBlank()) {
            throw new IllegalArgumentException("Region cannot be blank.");
        }
        if (postalCode == null || postalCode.isBlank()) {
            throw new IllegalArgumentException("Postal code cannot be blank.");
        }
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Country cannot be blank.");
        }
    }
}