package com.hz.show.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hz.show.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liyj
 * @Description
 * @createTime 2021/1/6 14:53
 **/
@Component
public class TokenFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String msg = StringUtils.format("请求访问：{}，权限不足，无法访问系统资源", request.getRequestURI());
        String uri = request.getRequestURI();
        if (uri.contains("validateUser")) {
            filterChain.doFilter(request, response);
        } else {
            String userName = request.getHeader("userName");
            String token = request.getHeader("token");
            if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(token)) {
                User user = (User) request.getSession().getAttribute(userName);
                if (user != null) {
                    long expiredTime = user.getExpiredTime();
                    if (System.currentTimeMillis() - expiredTime < 30 * 60 * 1000) {
                        if (token.equals(user.getToken())) {
                            user.setExpiredTime(System.currentTimeMillis());
                            filterChain.doFilter(request, response);
                        } else {
                            ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error("401", msg)));
                        }
                    }
                } else {
                    ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error("401", msg)));
                }
            } else {
                ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error("401", msg)));
            }
        }
    }
}
