package org.company.finance.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.company.finance.domain.repository.QueryCaptchaRepository;
import org.springframework.stereotype.Repository;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-28 15:29
 *  @Description:
 *
 */
@Repository
@RequiredArgsConstructor
public class QueryCaptchaRepositoryImpl implements QueryCaptchaRepository {
    @Override
    public boolean validate(String captchaId, String captchaCode) {
        return false;
    }
}
