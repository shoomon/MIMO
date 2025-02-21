package com.bisang.backend.transaction.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargeRequest {
    private Long amount;
    private String impUid;
    private String merchantUid;
}
