package com.fubao.dearbao.domain.enquiry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {

}
