package com.ngbr.server.dto.impl;

import com.ngbr.server.consts.status.OperationStatus;
import com.ngbr.server.dto.BankResponse;
import lombok.Builder;
import lombok.Setter;
import java.util.*;

@Setter
@Builder
public class BankResponseImpl implements BankResponse {
    private Double deposit;
    private OperationStatus operationStatus;
    private List <Long> notValidIds;

    @Override
    public Double getDeposit() {
        return deposit;
    }

    @Override
    public OperationStatus getOperationStatus() {
        return operationStatus;
    }

    @Override
    public List<Long> getNotValidIds() {
        return notValidIds;
    }
}
