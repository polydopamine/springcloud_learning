package com.springcloud.github.demo.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class AccessFilter extends ZuulFilter {

    private static Logger log = Logger.getLogger(AccessFilter.class);

    @Override
    // 四种不同的生命周期过滤器类型
    public String filterType() {
        // 在请求路由之前被调用
        return "pre";
    }

    @Override
    // 过滤器执行顺序
    public int filterOrder() {
        return 0;
    }

    @Override
    // 判断该多虑期是否要执行
    public boolean shouldFilter() {
        return true;
    }

    @Override
    // 过滤器的具体逻辑
    // 只有请求带"&accessToken=token"时才有效
    // 否则返回401错误
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

        Object accessToken = request.getParameter("accessToken");
        if (accessToken == null) {
            log.warn("access token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            return null;
        }
        log.info("access token ok");
        return null;
    }
}
