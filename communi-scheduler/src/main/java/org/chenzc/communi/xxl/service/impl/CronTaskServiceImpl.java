package org.chenzc.communi.xxl.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.xxl.job.core.biz.model.ReturnT;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.chenzc.communi.entity.BasicResult;
import org.chenzc.communi.enums.RespEnums;
import org.chenzc.communi.xxl.constant.XxlJobConstant;
import org.chenzc.communi.xxl.entity.XxlJobGroup;
import org.chenzc.communi.xxl.entity.XxlJobInfo;
import org.chenzc.communi.xxl.service.CronTaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CronTaskServiceImpl implements CronTaskService {

    @Value("${xxl.job.admin.username}")
    private String xxlUserName;

    @Value("${xxl.job.admin.password}")
    private String xxlPassword;

    @Value("${xxl.job.admin.addresses}")
    private String xxlAddresses;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RestTemplate restTemplate;

    @Override
    public BasicResult saveCronTask(XxlJobInfo xxlJobInfo) {
        HashMap<String, Object> params = JSON.parseObject(JSON.toJSONString(xxlJobInfo), HashMap.class);
        String path = Objects.isNull(xxlJobInfo.getId()) ?
                StringUtils.join(xxlAddresses, XxlJobConstant.INSERT_URL)
                : StringUtils.join(xxlAddresses, XxlJobConstant.UPDATE_URL);
        ReturnT returnT = null;
        try {
            ResponseEntity<String> responseEntity = doXxl(params, path);

            if (Objects.isNull(responseEntity))
                throw new NullPointerException();

            returnT = JSON.parseObject(responseEntity.getBody(), ReturnT.class);
            if (Objects.nonNull(returnT) && responseEntity.getStatusCode().is2xxSuccessful() && ReturnT.SUCCESS_CODE == returnT.getCode()) {
//                判断是更新还是保存操作 如果是保存需要返回id  更新不需要

                if (path.contains(XxlJobConstant.INSERT_URL)) {
                    int taskId = Integer.parseInt(String.valueOf(returnT.getContent()));
                    return BasicResult.success(taskId);

                } else if (path.contains(XxlJobConstant.UPDATE_URL)) {
                    return BasicResult.success();
                }
            }

        } catch (Exception e) {
            log.error("TaskHandler#saveTask fail,e:{},param:{},response:{}", Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(xxlJobInfo), JSON.toJSONString(returnT));
        }
        invalidateCookie();
        return BasicResult.fail(RespEnums.CRON_TASK_SERVICE_ERROR, JSON.toJSONString(returnT));
    }

    @Override
    public BasicResult startCronTask(Integer taskId) {
        String path = StringUtils.join(xxlAddresses, XxlJobConstant.RUN_URL);

        HashMap<String, Object> params = MapUtil.newHashMap();
        params.put(XxlJobConstant.XXL_ID, taskId.toString());

        ReturnT returnT = null;
        try {
            ResponseEntity<String> responseEntity = doXxl(params, path);
            if (Objects.isNull(responseEntity)) {
                throw new NullPointerException();
            }

            returnT = JSON.parseObject(responseEntity.getBody(), ReturnT.class);
        } catch (Exception e) {
            log.error("TaskHandler#startCronTask fail,e:{},param:{},response:{}", Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(params), JSON.toJSONString(returnT));
        }
        invalidateCookie();
        return BasicResult.success();
    }

    @Override
    public BasicResult getGroupId(String appName, String title) {
        String path = StringUtils.join(xxlAddresses, XxlJobConstant.JOB_GROUP_PAGE_LIST);

        HashMap<String, Object> params = MapUtil.newHashMap();
        params.put(XxlJobConstant.XXL_APPNAME, appName);
        params.put(XxlJobConstant.XXL_TITLE, title);
        ReturnT returnT = null;
        try {
            ResponseEntity<String> responseEntity = doXxl(params, path);
            if (Objects.isNull(responseEntity)) {
                throw new NullPointerException();
            }

            returnT = JSON.parseObject(responseEntity.getBody(), ReturnT.class);
            Integer groupId = JSON.parseObject(responseEntity.getBody()).getJSONArray("data").getJSONObject(0).getInteger("id");
            if (responseEntity.getStatusCode().is2xxSuccessful() && Objects.nonNull(groupId))
                return BasicResult.success(groupId);

        } catch (Exception e) {
            log.error("TaskHandler#getGroupId fail,e:{},param:{},response:{}", Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(params), JSON.toJSONString(returnT));
        }
        invalidateCookie();
        return BasicResult.fail(RespEnums.CRON_TASK_SERVICE_ERROR, JSON.toJSONString(returnT));
    }

    @Override
    public BasicResult createGroup(XxlJobGroup xxlJobGroup) {
        HashMap<String, Object> params = JSON.parseObject(JSON.toJSONString(xxlJobGroup), HashMap.class);
        String path = StringUtils.join(xxlAddresses, XxlJobConstant.JOB_GROUP_INSERT_URL);

        ReturnT returnT = null;

        try {
            ResponseEntity<String> responseEntity = doXxl(params, path);
            if (Objects.isNull(responseEntity)) {
                throw new NullPointerException();
            }
            returnT = JSON.parseObject(responseEntity.getBody(), ReturnT.class);
            if (responseEntity.getStatusCode().is2xxSuccessful() && ReturnT.SUCCESS_CODE == returnT.getCode()) {
                return BasicResult.success();
            }

        } catch (Exception e) {
            log.error("TaskHandler#createGroup fail,e:{},param:{},response:{}", Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(params), JSON.toJSONString(returnT));
        }

        invalidateCookie();
        return BasicResult.fail(RespEnums.CRON_TASK_SERVICE_ERROR, JSON.toJSONString(returnT));
    }

    @Override
    public BasicResult stopCronTask(Integer cronTaskId) {
        String path = StringUtils.join(xxlAddresses, XxlJobConstant.STOP_URL);
        HashMap<String, Object> params = MapUtil.newHashMap();
        params.put(XxlJobConstant.XXL_ID, cronTaskId);
        ReturnT returnT = null;
        try {
            ResponseEntity<String> responseEntity = doXxl(params, path);
            if (Objects.isNull(responseEntity)) {
                throw new NullPointerException();
            }

            returnT = JSON.parseObject(responseEntity.getBody(), ReturnT.class);
            if (responseEntity.getStatusCode().is2xxSuccessful() && Objects.nonNull(returnT) && ReturnT.SUCCESS_CODE == returnT.getCode()) {
                return BasicResult.success();
            }

        } catch (Exception e) {
            log.error("TaskHandler#stopCronTask fail,e:{},param:{},response:{}", Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(params), JSON.toJSONString(returnT));
        }
        invalidateCookie();
        return BasicResult.fail(RespEnums.CRON_TASK_SERVICE_ERROR, JSON.toJSONString(returnT));
    }

    private ResponseEntity<String> doXxl(HashMap<String, Object> params, String path) throws Exception {
        ReturnT returnT = null;

//        请求头写入缓存当中
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(XxlJobConstant.XXL_COOKIES, getCookie());
        HttpEntity<HashMap<String, Object>> requestEntity = new HttpEntity<>(params, httpHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(path, HttpMethod.POST, requestEntity, String.class);

        returnT = JSON.parseObject(responseEntity.getBody(), ReturnT.class);
        if (responseEntity.getStatusCode().is2xxSuccessful() && !Objects.isNull(returnT) && ReturnT.SUCCESS_CODE == returnT.getCode()) {
            return responseEntity;
        }
        return null;
    }


    /**
     * 清除xxl cookie 缓存的方法
     */
    private void invalidateCookie() {
        stringRedisTemplate.delete(StringUtils.join(XxlJobConstant.COOKIE_PREFIX, xxlUserName));
    }


    /**
     * 获取xxl cookie
     *
     * @return {@link String }
     */
    private String getCookie() {
        String cachedCookie = stringRedisTemplate.opsForValue().get(StringUtils.join(XxlJobConstant.COOKIE_PREFIX, xxlUserName));

//        若有缓存直接返回缓存
        if (StrUtil.isNotBlank(cachedCookie)) {
            return cachedCookie;
        }

        HashMap<String, Object> params = MapUtil.newHashMap();
        params.put(XxlJobConstant.XXL_USERNAME, xxlUserName);
        params.put(XxlJobConstant.XXL_PASSWORD, xxlPassword);
        params.put(XxlJobConstant.XXL_RANDOM_CODE, IdUtil.fastSimpleUUID());

        String path = StringUtils.join(xxlAddresses, XxlJobConstant.LOGIN_URL);
        HttpEntity<HashMap<String, Object>> requestEntity = new HttpEntity<>(params, new HttpHeaders());

        ResponseEntity<String> responseEntity = restTemplate.exchange(path, HttpMethod.POST, requestEntity, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            List<String> setCookieHeaders = responseEntity.getHeaders().get(HttpHeaders.SET_COOKIE);

            if (Objects.nonNull(setCookieHeaders) && Objects.nonNull(setCookieHeaders)) {
//                使用流处理 并且在每一次分割之后 都加上一个;分割他们
                String cookiesString = setCookieHeaders.stream()
                        .flatMap(header -> HttpCookie.parse(header).stream())
                        .map(HttpCookie::toString)
                        .collect(Collectors.joining(";"));

                stringRedisTemplate.opsForValue().set(StringUtils.join(XxlJobConstant.COOKIE_PREFIX, xxlUserName), cookiesString);
                return cookiesString;
            }
        }
        return null;
    }
}
