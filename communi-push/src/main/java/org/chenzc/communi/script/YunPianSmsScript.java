package org.chenzc.communi.script;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import org.apache.commons.lang3.StringUtils;
import org.chenzc.communi.annonation.Handler;
import org.chenzc.communi.constant.CommonConstant;
import org.chenzc.communi.constant.HandlerConstant;
import org.chenzc.communi.entity.SmsParam;
import org.chenzc.communi.entity.SmsRecord;
import org.chenzc.communi.entity.YunPianSendResult;
import org.chenzc.communi.entity.YunPianSmsAccount;
import org.chenzc.communi.enums.SmsStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static org.chenzc.communi.utils.accountUtils.getAccountById;
import static org.chenzc.communi.utils.accountUtils.getSmsAccountByScriptName;

@Handler("YunPianSmsScript")
public class YunPianSmsScript implements SmsScript {

    private static final Logger log = LoggerFactory.getLogger(YunPianSmsScript.class);

    /**
     * 发送信息
     *
     * @param smsParam
     * @return {@link List }<{@link SmsRecord }>
     */
    @Override
    public List<SmsRecord> send(SmsParam smsParam) {
        try {
//            组装好请求参数
            YunPianSmsAccount account = Objects.nonNull(smsParam.getSendAccountId()) ?
                    getAccountById(smsParam.getSendAccountId(), YunPianSmsAccount.class)
                    : getSmsAccountByScriptName(smsParam.getScriptName(), YunPianSmsAccount.class);
            Map<String, Object> requestParams = assembleSmsRecordEntity(smsParam, account);

//            发起请求
            String result = HttpRequest.post(account.getUrl())
                    .header(Header.CONTENT_TYPE.getValue(), CommonConstant.CONTENT_TYPE_FORM_URL_ENCODE)
                    .header(Header.ACCEPT.getValue(), CommonConstant.CONTENT_TYPE_JSON)
                    .form(requestParams)
                    .timeout(HandlerConstant.SMS_DEFAULT_TIME_OUT)
                    .execute().body();

//            处理响应
            YunPianSendResult yunPianSendResult = JSON.parseObject(result, YunPianSendResult.class);
            return assembleSmsResponseRecordEntity(smsParam, yunPianSendResult, account);

        } catch (Exception e) {
            log.error("YunPianSmsScript#send fail:{},params:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(smsParam));
            return new ArrayList<>();
        }
    }


    /**
     * 组装云片请求参数
     *
     * @param smsParam 请求参数
     * @param account  账号信息
     * @return {@link Map }<{@link String },{@link Object }>
     */
    private Map<String, Object> assembleSmsRecordEntity(SmsParam smsParam, YunPianSmsAccount account) {
        Map<String, Object> params = new HashMap<>(HandlerConstant.SMS_DEFAULT_CAPACITY);
        params.put(HandlerConstant.SMS_API_KEY, account);
        params.put(HandlerConstant.SMS_API_MOBILE, StringUtils.join(smsParam.getPhone(), CommonConstant.COMMA));
        params.put(HandlerConstant.SMS_TPL_ID, account.getTplId());
        if (CharSequenceUtil.isNotBlank(smsParam.getContent()) && smsParam.getContent().contains(HandlerConstant.PARAMS_KV_SPLIT_KEY)) {
            params.put(HandlerConstant.SMS_TPL_VALUE, getTplValue(smsParam));
        }
        return params;
    }


    /**
     * 组装云片响应参数
     *
     * @param smsParam 请求参数
     * @param response 请求结果
     * @param account  账号信息
     * @return {@link List }<{@link SmsRecord }>
     */
    private List<SmsRecord> assembleSmsResponseRecordEntity(SmsParam smsParam, YunPianSendResult response, YunPianSmsAccount account) {

        List<SmsRecord> smsRecordList = new ArrayList<>();
        if (Objects.isNull(response) || ArrayUtil.isEmpty(response.getData())) {
            return smsRecordList;
        }

        for (YunPianSendResult.DataDTO datum : response.getData()) {
            SmsRecord smsRecord = SmsRecord.builder()
                    .sendDate(Integer.valueOf(DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN)))
                    .messageTemplateId(smsParam.getMessageTemplateId())
                    .phone(Long.valueOf(datum.getMobile()))
                    .supplierId(account.getSupplierId())
                    .supplierName(account.getSupplierName())
                    .msgContent(smsParam.getContent())
                    .seriesId(datum.getSid())
                    .chargingNum(Math.toIntExact(datum.getCount()))
                    .status(0 == datum.getCode() ? SmsStatus.SEND_SUCCESS.getCode() : SmsStatus.SEND_FAIL.getCode())
                    .reportContent(datum.getMsg())
                    .created(Math.toIntExact(DateUtil.currentSeconds()))
                    .updated(Math.toIntExact(DateUtil.currentSeconds()))
                    .build();

            smsRecordList.add(smsRecord);
        }
        return smsRecordList;

    }


    /**
     * 获取tplvalue
     *
     * @param smsParam
     * @return {@link String }
     */
    private String getTplValue(SmsParam smsParam) {
        String tplValue = "";
        if (CharSequenceUtil.isNotBlank(smsParam.getContent())) {
            tplValue = CharSequenceUtil.split(smsParam.getContent(), HandlerConstant.PARAMS_SPLIT_KEY)
                    .stream().map(item -> {
                        List<String> kv = CharSequenceUtil.splitTrim(item, HandlerConstant.PARAMS_KV_SPLIT_KEY, HandlerConstant.YUN_PIAN_SMS_DEFAULT_LIMIT);
                        return String.join(CommonConstant.EQUAL_STRING,
                                URLEncodeUtil.encodeQuery(kv.get(0)),
                                URLEncodeUtil.encodeQuery(kv.get(1)));
                    }).collect(Collectors.joining(CommonConstant.AND_STRING));
        }
        return tplValue;
    }

}
