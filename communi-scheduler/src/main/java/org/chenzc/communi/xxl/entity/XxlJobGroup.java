package org.chenzc.communi.xxl.entity;

import cn.hutool.core.text.StrPool;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.*;

/**
 *
 * 记录了执行器组的信息
 * @author chenz
 * @date 2024/06/04
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class XxlJobGroup {
    private int id;
    private String appname;

    private String title;
    /**
     * 执行器的地址类型 0=自动注册 1=手动录入
     */
    private int addressType;

    /**
     * 执行器地址列表 多地址逗号分割 （手动录入才有的）
     */
    private String addressList;
    private Date updateTime;

    /**
     * registry 的执行器地址列表 （系统注册）
     */
    private List<String> registryList;

    /**
     * 获得执行器的列表
     * @return {@link List }<{@link String }>
     */
    public List<String> getRegistryList(){
        if (Objects.nonNull(addressList) && addressList.trim().length() > 0){
            registryList = new ArrayList<>(Arrays.asList(addressList.split(StrPool.COMMA)));
        }
        return registryList;
    }


}
