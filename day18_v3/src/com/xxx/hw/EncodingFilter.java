package com.xxx.hw;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Filter implementation class EncodingFilter
 */
public class EncodingFilter implements Filter {

    /**
     * Default constructor. 
     */
    public EncodingFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		// pass the request along the filter chain
//		chain.doFilter(request, response);
		final HttpServletRequest req = (HttpServletRequest) request; // �����ڲ�����õĶ�����final
		HttpServletRequest myReq = (HttpServletRequest) 
				Proxy.newProxyInstance(EncodingFilter.class.getClassLoader(), req.getClass().getInterfaces(), new InvocationHandler(){

					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						Object obj = null;
						
						// TODO Auto-generated method stub
						if (method.getName().equalsIgnoreCase("getParameter")) {
							// ��ȡ��������
							String md = req.getMethod();
							if ("get".equalsIgnoreCase(md)) {
								// get����ʽ
								// ����req�ϵ�getParameter
								String v = (String) method.invoke(req, args);
								// ת��
								String vv = new String(v.getBytes("iso-8859-1"), "utf-8");
								return vv;
							} else {
								// post����ʽ
								req.setCharacterEncoding("utf-8");
								obj = method.invoke(req, args);
							}
						} else {
							obj = method.invoke(req, args);
						}
						return obj;
					}
				});
		chain.doFilter(myReq, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
