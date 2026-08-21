package com.example.tracking_orderad.configmapper;

import com.example.tracking_orderad.dto.response.UserAddressRes;
import com.example.tracking_orderad.entity.UserAddress;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserAddressMapper {
    UserAddressRes toUserAddressRes(UserAddress address);
    List<UserAddressRes> toUserAddressResList(List<UserAddress> addresses);
}
