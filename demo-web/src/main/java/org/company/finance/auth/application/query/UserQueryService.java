package org.company.finance.auth.application.query;

import lombok.RequiredArgsConstructor;
import org.company.finance.auth.interfaces.rest.response.UserInfoVO;
import org.company.finance.domain.repository.UserPermissionRepository;
import org.company.finance.domain.repository.QueryUserRepository;
import org.company.finance.infrastructure.persistence.entity.SysUserEntity;
import org.company.finance.infrastructure.persistence.entity.SysUserInfoEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-17 14:25
 *  @Description:
 *
 */
@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final QueryUserRepository queryUserRepository;
    private final UserPermissionRepository userPermissionRepository;

    public UserInfoVO getUserInfo(Long userId) {
        // 1. 查询用户基本信息
        SysUserEntity user = queryUserRepository.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 2. 查询用户详细信息
        SysUserInfoEntity userInfo = queryUserRepository.findUserInfoByUserId(userId);

        // 3. 组装返回对象
        UserInfoVO dto = new UserInfoVO();
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRealName(userInfo != null ? userInfo.getRealName() : user.getUsername());
        dto.setAvatar(userInfo != null ? userInfo.getAvatar() : null);
        dto.setRoles(Collections.singletonList("USER"));
        dto.setPermissions(Collections.emptyList());
        return dto;
    }
}
