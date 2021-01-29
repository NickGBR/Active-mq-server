package com.ngbr.server.repository;

import com.ngbr.server.dao.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDataRepository extends CrudRepository <Account, Long>{
    Account getById(Long id);
}
