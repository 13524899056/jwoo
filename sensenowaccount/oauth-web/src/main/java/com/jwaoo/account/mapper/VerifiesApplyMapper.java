package com.jwaoo.account.mapper;

import com.jwaoo.account.dto.VerifiesApplyDto;
import com.jwaoo.account.model.VerifiesApply;
import com.jwaoo.account.model.VerifiesApplyExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface VerifiesApplyMapper
{

    int countByExample(VerifiesApplyExample example);

    int deleteByExample(VerifiesApplyExample example);

    int deleteByPrimaryKey(Long id);

    int insert(VerifiesApply record);

    int insertSelective(VerifiesApply record);

    List<VerifiesApply> selectByExampleWithRowbounds(VerifiesApplyExample example, RowBounds rowBounds);

    List<VerifiesApply> selectByExample(VerifiesApplyExample example);

    VerifiesApply selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") VerifiesApply record, @Param("example") VerifiesApplyExample example);

    int updateByExample(@Param("record") VerifiesApply record, @Param("example") VerifiesApplyExample example);

    int updateByPrimaryKeySelective(VerifiesApply record);

    int updateByPrimaryKey(VerifiesApply record);

    List<VerifiesApplyDto> findByCondition(@Param("record") VerifiesApplyDto record, @Param("limit") Integer limit, @Param("num") Integer num);

    int countByCondition(VerifiesApplyDto record);

    VerifiesApplyDto findVerifyApplyById(Long id);

}