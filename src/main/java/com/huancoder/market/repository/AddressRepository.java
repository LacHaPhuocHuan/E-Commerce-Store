package com.huancoder.market.repository;

import com.huancoder.market.model.Address;
import com.huancoder.market.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserId(@Param("userId") Long id);
}
