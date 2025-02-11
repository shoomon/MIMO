package com.bisang.backend.transaction.controller.request;

import com.bisang.backend.user.domain.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QrCodeRequest {

    private User user;

    private String accountNumber;

    private Long teamId;
}
