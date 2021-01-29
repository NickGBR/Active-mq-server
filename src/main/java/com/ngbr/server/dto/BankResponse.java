package com.ngbr.server.dto;

import com.ngbr.server.consts.status.OperationStatus;
import java.util.*;
public interface BankResponse {
    void setNotValidIds(List<Long> id);
    List<Long> getNotValidIds();
    Double getDeposit();
    OperationStatus getOperationStatus();
    void setOperationStatus(OperationStatus operationStatus);
    void setDeposit(Double deposit);

}
