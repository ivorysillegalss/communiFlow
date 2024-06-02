package org.chenzc.communi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.chenzc.communi.entity.SmsRecord;

@Mapper
public interface SmsRecordDao extends BaseMapper<SmsRecord> {
}
