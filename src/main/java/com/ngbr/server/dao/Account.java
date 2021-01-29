package com.ngbr.server.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "account_data")
public class Account {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    Long id;
    @Column(name = "amount")
    Double amount;
}
