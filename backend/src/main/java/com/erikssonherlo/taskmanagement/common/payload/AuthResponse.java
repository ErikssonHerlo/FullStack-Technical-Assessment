package com.erikssonherlo.taskmanagement.common.payload;

import lombok.*;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    String token;
}