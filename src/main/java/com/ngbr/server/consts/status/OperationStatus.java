package com.ngbr.server.consts.status;

public enum OperationStatus {
    SUCCESS("Operation complete successfully"),
    DEPOSIT_SUCCESS("Deposit operation complete successfully"),
    TRANSFER_SUCCESS("Transfer complete successfully"),
    CLOSE_SUCCESS("Account closed successfully"),
    CREATE_SUCCESS("Account created successfully"),
    WITHDRAW_SUCCESS("Withdraw operation complete successfully"),
    NOT_ENOUGH_MONEY("Not enough money"),
    NOT_EXITING_ID("User doesn't exist"),
    EXITING_ID("User with this id exists already"),
    CLOSE_ERROR("This account are not empty. Please transfer your deposit to another account!");
    private String value;

    OperationStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
