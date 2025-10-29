package org.company.finance.domain.repository;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-28 15:28
 *  @Description:
 *
 */
public interface QueryCaptchaRepository {
    boolean validate(String captchaId, String captchaCode);
}
