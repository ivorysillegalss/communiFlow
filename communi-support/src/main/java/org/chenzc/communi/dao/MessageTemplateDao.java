package org.chenzc.communi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.chenzc.communi.entity.MessageTemplate;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MessageTemplateDao extends BaseMapper<MessageTemplate> {
}
