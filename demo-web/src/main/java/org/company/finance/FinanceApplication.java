package org.company.finance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.company.finance.infrastructure.persistence.mapper")
public class FinanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceApplication.class, args);
    }

}
