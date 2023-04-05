package com.FoodOrdering.OrderUp.auth;

import com.FoodOrdering.OrderUp.Email.EmailSenderService;
import com.FoodOrdering.OrderUp.Model.payload.response.CommonResponse;
import com.FoodOrdering.OrderUp.Service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")

public class AuthenticationController {

  private final AuthenticationService service;

  @Autowired
  ApplicationService applicationService;
  @Autowired
  EmailSenderService emailSenderService;
  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    try {
      AuthenticationResponse response = service.authenticate(request);
      response.setStatus(1);
      return ResponseEntity.ok(response);
    } catch (BadCredentialsException e) {
      // Return a 400 response with a custom error message
      return ResponseEntity.badRequest()
              .body(new AuthenticationResponse(0,"sai ten dang nhap hoac mat khau"));
    }catch (HttpClientErrorException.Forbidden e){
      return ResponseEntity.badRequest()
              .body(new AuthenticationResponse(0,"403"));
    }

  }

  @GetMapping("/GET_ITEMSS")
  public CommonResponse<Object> GET_ITEMSS( @RequestParam(name ="page") int page) {
    CommonResponse<Object> response = new CommonResponse<>();

    Page<Document> items =  applicationService.adminGetListItems("64055a970f600942fda69f0b",page,3);

    response.setData(items);
    return response;
  }

  @PostMapping("/validateOTP")
  public CommonResponse<Object> validateOTP(@RequestParam String email, @RequestParam String OTP){

    CommonResponse<Object> response = new CommonResponse<>();

    boolean flag= service.validateOTP(email,OTP);
    if(flag){
      response.setCode(1);
      response.setMessage("validate thanh cong");
      response.setData(true);
    }
    else {
      response.setCode(0);
      response.setMessage("validate that bai");
      response.setData(false);

    }

    return response;

  }

  @PostMapping("/refreshOTP")
  public ResponseEntity<Boolean> refreshOTP(@RequestParam String email){
    boolean flag = emailSenderService.resendEmail(email);

    return ResponseEntity.ok(flag);
  }


}
