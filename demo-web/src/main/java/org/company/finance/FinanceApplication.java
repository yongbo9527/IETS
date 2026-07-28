package org.company.finance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-08-30 18:14
 *
 */
@SpringBootApplication
@MapperScan({
        "org.company.finance.infrastructure.persistence.mapper",
        "org.company.finance.tally.infrastructure.persistence",
        "org.company.finance.category.infrastructure.persistence"
})
public class FinanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceApplication.class, args);
    }

}
