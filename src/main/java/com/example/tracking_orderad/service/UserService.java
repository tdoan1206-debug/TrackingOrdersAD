package com.example.tracking_orderad.service;

import com.example.tracking_orderad.dto.request.CreateUserAddressReq;
import com.example.tracking_orderad.dto.response.UserAddressRes;
import com.example.tracking_orderad.dto.request.RegisterReq;
import com.example.tracking_orderad.dto.response.UserProfileRes;

import java.util.List;

public interface UserService {
    UserProfileRes getCurrentUserProfile();
    List<UserAddressRes> getMyAddresses();
    UserAddressRes addAddress(CreateUserAddressReq req);
    void deleteAddress(String addressId);
    UserAddressRes setDefaultAddress(String addressId);
    UserProfileRes register(RegisterReq req);
}
