package com.sapient.football.standings.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static com.sapient.football.standings.constants.Constants.*;

@Slf4j
@Component
public class RequestIdInterceptor implements HandlerInterceptor {

    private static final String X_REQUEST_ID = "X-REQUEST-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) {
        ContentCachingRequestWrapper requestCacheWrapperObject =
                new ContentCachingRequestWrapper(request);
        requestCacheWrapperObject.getParameterMap();

        String requestId = requestCacheWrapperObject.getHeader(X_REQUEST_ID);

        if (!StringUtils.hasLength(requestId)) {
            requestId = UUID.randomUUID().toString();
        }
        MDC.put(REQUEST_ID, requestId);
        ThreadContext.put(REQUEST_ID, requestId);
        response.addHeader(REQUEST_ID, requestId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
        MDC.remove(REQUEST_ID);
        ThreadContext.remove(REQUEST_ID);
    }
}
