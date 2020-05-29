/**
 *
 * Copyright (c) 北京畅易财税科技有限公司 2019
 * 本著作物的著作权归北京畅易财税有限公司所有
 */
package com.cycs.poskp.dao;

import com.cycs.poskp.entity.EnterpriseExtend;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EnterpriseExtendMapper {
    int deleteByPrimaryKey(String id);

    int insert(EnterpriseExtend record);

    int insertSelective(EnterpriseExtend record);

    EnterpriseExtend selectByPrimaryKey(EnterpriseExtend record);

    int updateByPrimaryKeySelective(EnterpriseExtend record);

    int updateByPrimaryKey(EnterpriseExtend record);
}