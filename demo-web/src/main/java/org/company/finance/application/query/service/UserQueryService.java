package org.company.finance.application.query.service;

import lombok.RequiredArgsConstructor;
import org.company.finance.application.vo.UserInfoVO;
import org.company.finance.domain.repository.UserPermissionRepository;
import org.company.finance.domain.repository.QueryUserRepository;
import org.company.finance.infrastructure.persistence.entity.SysUserEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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

//    public UserInfoVO getUserInfo(String username) {
//        SysUserEntity user = queryUserRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("用户不存在"));
//
//        UserInfoVO dto = new UserInfoVO();
//        dto.setUsername(user.getUsername());
//
//        // 模拟查询角色
//        dto.setRoles(Arrays.asList("admin"));
//
//        return dto;
//    }

//    public UserPermissionDTO getUserPermissions(Long userId) {
//        // 调用权限中心或本地查询
//        List<String> perms = permissionClient.getPermissionsByUserId(userId);
//        UserPermissionDTO dto = new UserPermissionDTO();
//        dto.setPermissions(perms);
//        return dto;
//    }
}
