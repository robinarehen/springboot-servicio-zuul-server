package com.rahdevelopers.api.zuulserver.filters;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PreFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		// TODO Auto-generated method stub
		RequestContext requestContext = RequestContext.getCurrentContext();

		HttpServletRequest request = requestContext.getRequest();

		Long tiempoInicial = System.currentTimeMillis();

		request.setAttribute("tiempoInicial", tiempoInicial);
		
		log.info(String.format("Url: %s - Metodo: %s", request.getMethod(), request.getRequestURI()));

		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

}
