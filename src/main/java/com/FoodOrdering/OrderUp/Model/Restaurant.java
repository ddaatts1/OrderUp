package com.FoodOrdering.OrderUp.Model;

//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.bson.types.ObjectId;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//import javax.persistence.Id;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Document(collection = "restaurants")
//public class Restaurant {
//
//    @Id
//    private ObjectId _id;
//    private String name;
//    private String email;
//    private String password;
//    private String status;
//    private String address_detail;
//    private Double address_long;
//    private Double address_lat;
//    private String phone;
//
//}

import com.FoodOrdering.OrderUp.Enum.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "restaurants")
public class Restaurant implements UserDetails {

    @Id
    private ObjectId _id;
    private String name;
    private String email;
    private String password;
    private boolean status;
    private String address_detail;
    private Double address_long;
    private Double address_lat;
    private Date createDate;
    private String phone;
    private String uid;
    private Date expiredOTP;
    private String otp;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));

    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status;
    }
}

