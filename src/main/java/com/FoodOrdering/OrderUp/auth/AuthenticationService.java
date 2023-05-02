package com.FoodOrdering.OrderUp.auth;


import com.FoodOrdering.OrderUp.Email.CreateOTP;
import com.FoodOrdering.OrderUp.Email.EmailSenderService;
import com.FoodOrdering.OrderUp.Enum.Role;
import com.FoodOrdering.OrderUp.Model.Restaurant;
import com.FoodOrdering.OrderUp.Repository.RestaurantRepository;
import com.FoodOrdering.OrderUp.config.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final RestaurantRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  @Autowired
  EmailSenderService emailSenderService;

  public AuthenticationResponse register(RegisterRequest request)  {

    String otp =new String( CreateOTP.OTP(6));
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.MINUTE, 5);
    Date fiveMinutesLater = calendar.getTime();
    try{
      emailSenderService.sendSimpleEmail(request.getEmail(),otp);

    }catch (MessagingException e){
      e.printStackTrace();
    }
    var restaurant = Restaurant.builder()
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.MANAGER)
            .address_detail(request.getAddress_detail())
            .address_lat(request.getAddress_lat())
            .address_long(request.getAddress_long())
            .phone(request.getPhone())
            .name(request.getName())
            .status(false)
            .createDate(new Date())
            .expiredOTP(fiveMinutesLater)
            .otp(otp)
        .build();

    Optional<Restaurant> restaurant1 =repository.findByEmail(request.getEmail());
    if(!restaurant1.isPresent()){
      repository.save(restaurant);
//      var jwtToken = jwtService.generateToken(restaurant);
//      return AuthenticationResponse.builder()
//              .token(jwtToken)
//              .build();
    }
    return null;

  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .message(jwtToken)
        .build();
  }


  public boolean validateOTP(String email, String OTP){

    Optional<Restaurant> restaurant = repository.findByEmail(email);
    Restaurant r =  new Restaurant();
    if(restaurant.isPresent()){
      r = restaurant.get();
    }
    else return false;

    Date now = new Date();
    Date expiredOPT = r.getExpiredOTP();

    if(expiredOPT == null){
      return false;
    }
    int compareDate = now.compareTo(expiredOPT);

    if(r.getOtp().equals(OTP) && compareDate <= 0 ){
      r.setStatus(true);
      r.setOtp("");
      r.setExpiredOTP(null);
      repository.save(r);
      return true;
    }
    return false;
  }
}
