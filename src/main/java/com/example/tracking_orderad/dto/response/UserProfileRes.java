package com.example.tracking_orderad.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileRes {
    private String id;
    private String username;
    private String phone;
    private String role;
}
