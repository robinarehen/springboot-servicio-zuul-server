package com.rahdevelopers.api.zuulserver.filters;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PostFilter extends ZuulFilter {

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

		Long tiempoInicial = (Long) request.getAttribute("tiempoInicial");
		Long tiempoFinal = System.currentTimeMillis();
		Long tiempoTotal = tiempoFinal - tiempoInicial;
		
		log.info(String.format("tiempoTotal: %s ms", tiempoTotal ));
		log.info(String.format("tiempoTotal: %s seg", (tiempoTotal / 1000) ));

		return null;
	}

	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public int filterOrder() {
		return 2;
	}

}
