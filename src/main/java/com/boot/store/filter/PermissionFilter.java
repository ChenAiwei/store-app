package com.boot.store.filter;

import com.boot.store.context.UserContextHolder;
import com.boot.store.entity.TUser;
import com.boot.store.exception.PermissionException;
import com.boot.store.exception.TokenException;
import com.boot.store.service.system.ITUserService;
import com.boot.store.utils.EhcacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author：chenaiwei
 * @Description：PermissionFilter
 * @CreateDate：2020/7/12 20:29
 */
@WebFilter("/*")
@Slf4j
@Order(2)
@Component
public class PermissionFilter implements Filter {

	@Autowired
	@Qualifier("handlerExceptionResolver")
	private HandlerExceptionResolver resolver;

	@Autowired
	EhcacheUtil cacheUtil;

	@Autowired
	private ITUserService userService;

	private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
			Arrays.asList("/verify/getCode/blend", "/verify/getCode/number","/login", "/logout", "/register")));
    private static final String WEBSOCKET_TAG ="/websocket";
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, PermissionException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String path = request.getRequestURI();
		log.info("Request URI:{}", path);
		String userId = request.getHeader("userId");
		String token = request.getHeader("token");
		if (!ALLOWED_PATHS.contains(path) && !path.startsWith(WEBSOCKET_TAG)) {
			if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(token)) {
				resolver.resolveException(request, response, null, new TokenException("token用户信息验证失败，请重新登录！"));
				return;
			}
			if (StringUtils.isEmpty(cacheUtil.get(userId))) {
				resolver.resolveException(request, response, null, new TokenException("token会话过期，请重新登录！"));
				return;
			}
			if (!cacheUtil.get(userId).equals(token)) {
				resolver.resolveException(request, response, null, new TokenException("token用户信息验证失败，请重新登录！"));
				return;
			}
			TUser user = userService.getById(userId);
			if (null == user || !user.getStatus()){
				resolver.resolveException(request, response, null, new TokenException("非法用户！用户不存在或者已禁用！"));
				return;
			}
			UserContextHolder.set(user);
			/*if (!ALLOWED_PATHS.contains(path)) {
				resolver.resolveException(request, response, null, new PermissionException("请求的URl没有权限！"));
				return;
			}*/
			cacheUtil.set(userId, token, 60 * 60*12);
		}
		filterChain.doFilter(request, response);
	}


	@Override
	public void destroy() {
		UserContextHolder.shutdown();
	}
}
