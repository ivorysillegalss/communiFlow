package org.chenzc.communi.advice;


import org.chenzc.communi.annonation.ResultController;
import org.chenzc.communi.constant.CommonConstant;
import org.chenzc.communi.entity.BasicResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Objects;

@ControllerAdvice(basePackages = "org.chenzc.communi.controller")
public class ResponseBodyAdvice implements org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getContainingClass().isAnnotationPresent(ResultController.class) || returnType.hasMethodAnnotation(ResultController.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (Objects.nonNull(body) && Objects.nonNull(body.getClass())) {
            String name = body.getClass().getSimpleName();
            if (name.equals(CommonConstant.BUSINESS_RETURN_CLASS)) {
                return body;
            }
        }
        return BasicResult.success(body);
    }
}
