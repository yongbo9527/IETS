package org.company.finance.test;

import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-13 10:31
 *  @Description: 排除 MyBatis Mapper 接口，防止在 @WebMvcTest 中因缺少 sqlSessionFactory 报错
 *
 */
public class MapperExclusionFilter extends TypeExcludeFilter {

    private static final String BASE_MAPPER_NAME = "com/baomidou/mybatisplus/core/mapper/BaseMapper";

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        // 排除被 @Mapper 注解的接口
        if (metadataReader.getAnnotationMetadata().hasAnnotation("org.apache.ibatis.annotations.Mapper")) {
            return true;
        }

        // 排除继承 BaseMapper 的类（MyBatis-Plus 场景）
        String superclass = metadataReader.getClassMetadata().getSuperClassName();
        return superclass != null && superclass.equals(BASE_MAPPER_NAME);
    }

    // ✅ 必须重写 hashCode()
    @Override
    public int hashCode() {
        return MapperExclusionFilter.class.hashCode();
    }

    // ✅ 必须重写 equals()
    @Override
    public boolean equals(Object obj) {
        return obj instanceof MapperExclusionFilter;
    }
}
