package com.example.tracking_orderad.service.impl;

import com.example.tracking_orderad.config.basicauthconfig.AuthenticationFacade;
import com.example.tracking_orderad.configmapper.UserAddressMapper;
import com.example.tracking_orderad.dto.request.CreateUserAddressReq;
import com.example.tracking_orderad.dto.request.RegisterReq;
import com.example.tracking_orderad.dto.response.UserAddressRes;
import com.example.tracking_orderad.dto.response.UserProfileRes;
import com.example.tracking_orderad.entity.Cart;
import com.example.tracking_orderad.entity.User;
import com.example.tracking_orderad.entity.UserAddress;
import com.example.tracking_orderad.exception.BadRequestException;
import com.example.tracking_orderad.exception.NotFoundException;
import com.example.tracking_orderad.repository.CartRepo;
import com.example.tracking_orderad.repository.UserAddressRepo;
import com.example.tracking_orderad.repository.UserRepo;
import com.example.tracking_orderad.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.tracking_orderad.common.RoleEnum;
import com.example.tracking_orderad.common.UserStatusEnum;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationFacade authenticationFacade;
    private final UserAddressRepo userAddressRepo;
    private final UserAddressMapper userAddressMapper;
    private final UserRepo userRepo;
    private final CartRepo cartRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserProfileRes getCurrentUserProfile() {
        User user = authenticationFacade.getCurrentUser();
        return UserProfileRes.builder()
                .id(user.getId())
                .username(user.getUsername())
                .phone(user.getPhone())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .build();
    }

    @Override
    public List<UserAddressRes> getMyAddresses() {
        User user = authenticationFacade.getCurrentUser();
        return userAddressMapper.toUserAddressResList(userAddressRepo.findByUser(user));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserAddressRes addAddress(CreateUserAddressReq req) {
        User user = authenticationFacade.getCurrentUser();

        // Nếu là địa chỉ mặc định, bỏ default của địa chỉ cũ
        if (Boolean.TRUE.equals(req.getIsDefault())) {
            Optional<UserAddress> existingDefault = userAddressRepo.findByUserAndIsDefaultTrue(user);
            existingDefault.ifPresent(addr -> {
                addr.setIsDefault(false);
                userAddressRepo.save(addr);
            });
        }

        UserAddress address = new UserAddress();
        address.setUser(user);
        address.setName(req.getName());
        address.setPhone(req.getPhone());
        address.setProvince(req.getProvince());
        address.setCity(req.getCity());
        address.setDistrict(req.getDistrict());
        address.setDetailAddress(req.getDetailAddress());
        address.setIsDefault(Boolean.TRUE.equals(req.getIsDefault()));

        address = userAddressRepo.save(address);
        return userAddressMapper.toUserAddressRes(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAddress(String addressId) {
        User user = authenticationFacade.getCurrentUser();
        UserAddress address = userAddressRepo.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Address not found"));
        userAddressRepo.delete(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserAddressRes setDefaultAddress(String addressId) {
        User user = authenticationFacade.getCurrentUser();

        // Bỏ default của địa chỉ cũ
        userAddressRepo.findByUserAndIsDefaultTrue(user).ifPresent(addr -> {
            addr.setIsDefault(false);
            userAddressRepo.save(addr);
        });

        // Set default cho địa chỉ mới
        UserAddress address = userAddressRepo.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Address not found"));
        address.setIsDefault(true);
        address = userAddressRepo.save(address);
        return userAddressMapper.toUserAddressRes(address);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserProfileRes register(RegisterReq req) {
        if (userRepo.findByUsername(req.getUsername()).isPresent()) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setPhone(req.getPhone());
        user.setStatus(UserStatusEnum.ACTIVE);

        try {
            user.setRole(RoleEnum.valueOf(req.getRole().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Invalid role");
        }

        user = userRepo.save(user);

        if (RoleEnum.BUYER.equals(user.getRole())) {
            Cart cart = new Cart();
            cart.setUser(user);
            cartRepo.save(cart);
        }

        return UserProfileRes.builder()
                .id(user.getId())
                .username(user.getUsername())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .build();
    }
}
