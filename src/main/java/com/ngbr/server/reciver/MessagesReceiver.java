package com.ngbr.server.reciver;

import com.ngbr.server.consts.Commands;
import com.ngbr.server.consts.JmsMessagePropertyName;
import com.ngbr.server.consts.Queue;
import com.ngbr.server.dto.BankResponse;
import com.ngbr.server.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.Message;
import org.apache.activemq.command.ActiveMQMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

@Slf4j
@Component
public class MessagesReceiver {
    private JmsTemplate jmsTemplate;
    private AccountService accountService;

    @Autowired
    public MessagesReceiver(JmsTemplate jmsTemplate, AccountService accountService) {
        this.jmsTemplate = jmsTemplate;
        this.accountService = accountService;
    }


    @JmsListener(destination = Queue.REQUEST_QUEUE)
    public void getRequest(Message message) throws JMSException {
        final String command = message.getStringProperty(JmsMessagePropertyName.COMMAND);
        switch (command) {
            case (Commands.CREATE):
                create(message);
                break;
            case (Commands.CLOSE):
                close(message);
                break;
            case (Commands.DEPOSIT):
                deposit(message);
                break;
            case (Commands.TRANSFER):
                transfer(message);
                break;
            case (Commands.WITHDRAW):
                withdraw(message);
                break;
            default:
                break;
        }
    }

    private void withdraw(Message message) throws JMSException {
        Long id = message.getLongProperty(JmsMessagePropertyName.ACCOUNT_ID);
        Double amount = message.getDoubleProperty(JmsMessagePropertyName.AMOUNT);
        sendResponse(accountService.withdraw(id, amount), message);

    }

    private void deposit(Message message) throws JMSException {
        Long id = message.getLongProperty(JmsMessagePropertyName.ACCOUNT_ID);
        Double amount = message.getDoubleProperty(JmsMessagePropertyName.AMOUNT);
        sendResponse(accountService.deposit(id, amount), message);
    }

    private void create(Message message) throws JMSException {
        Long id = message.getLongProperty(JmsMessagePropertyName.ACCOUNT_ID);
        sendResponse(accountService.createAccount(id), message);
    }

    private void close(Message message) throws JMSException {
        Long id = message.getLongProperty(JmsMessagePropertyName.ACCOUNT_ID);
        sendResponse(accountService.closeAccount(id), message);
    }

    private void transfer(Message message) throws JMSException {
        Long idFrom = message.getLongProperty(JmsMessagePropertyName.TRANSFER_FROM);
        Long idTo = message.getLongProperty(JmsMessagePropertyName.TRANSFER_TO);
        Double amount = message.getDoubleProperty(JmsMessagePropertyName.AMOUNT);
        sendResponse(accountService.transferTo(idFrom, idTo, amount), message);
    }

    private void sendResponse(BankResponse bankResponse, Message message) throws JMSException {
        log.info(message.getJMSCorrelationID());
        final Message response = new ActiveMQMessage();
        response.setJMSCorrelationID(message.getJMSCorrelationID());
        response.setStringProperty(JmsMessagePropertyName.OPERATION_STATUS, bankResponse.getOperationStatus().toString());
        if (bankResponse.getDeposit() != null) response.setDoubleProperty(JmsMessagePropertyName
                .ACCOUNT_DEPOSIT_STATUS, bankResponse.getDeposit());
        if (bankResponse.getNotValidIds()!=null && !bankResponse.getNotValidIds().isEmpty()) response.setObjectProperty(JmsMessagePropertyName
                .NOT_VALID_USER_IDs, bankResponse.getNotValidIds());
        jmsTemplate.convertAndSend(Queue.RESPONSE_TOPIC, response);
    }
}
