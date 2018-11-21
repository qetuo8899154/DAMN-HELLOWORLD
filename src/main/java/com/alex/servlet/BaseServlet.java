package com.alex.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public abstract class BaseServlet extends HttpServlet {
	public void service(HttpServletRequest request,HttpServletResponse response) 
		throws ServletException,IOException{
		/*
		 * ��ȡ����������ʶ���û�����ķ���,������Ӧ�ķ���
		 */
		String methodName = request.getParameter("method");
		if(methodName==null||methodName.trim().isEmpty()) {
			throw new RuntimeException("�봫����Ҫ���õķ������ƣ�");
		}
		/*
		 * ͨ��������÷���
		 * 1.�õ�method����
		 */
		Class c = this.getClass();	//�õ���ǰ���class����
		Method method = null;
		try {
			method = c.getMethod(methodName,HttpServletRequest.class,HttpServletResponse.class );
		} catch (Exception e) {
			throw new RuntimeException("����ķ���"+method+"�����ڣ�");
		}
		/*
		 * 2.����method��ʾ�ķ���*
		 */
		try {					//�ھ���ָ�������ķ��������ϵ��ô˷��������ʾ�ĵײ㷽��
			String result = (String) method.invoke(this, request,response);		
			// ִ�к󷵻ص��ַ���,����ʾת�����ض����·��
			 
			/*
			 * �鿴���ص��ַ����Ƿ����: ���û�У�Ĭ��ת��
			 * ����У�ʹ��:�ָ�õ�ǰ׺�ͺ�׺
			 */
			if(result==null||result.trim().isEmpty()) {
				return;
			}
			if(result.contains(":")) {	//���� f:/login.jsp
				int index = result.indexOf(":");  //index=1
				String s = result.substring(0, index);	// ǰ׺��f
				String path = result.substring(index+1);	// ��׺��/login.jsp
				if(s.equalsIgnoreCase("r")) {		//redirect �ض���
					response.sendRedirect(request.getContextPath()+path) ;
				}else if(s.equalsIgnoreCase("f")){
					request.getRequestDispatcher(path).forward(request, response);
				}else {
					throw new RuntimeException("�ݲ�֧��"+s+"������");
				}	
			}else {	//û��: Ĭ��ת��
				request.getRequestDispatcher(result).forward(request, response);;
			}
		} catch (Exception e) {
			System.out.println(methodName+"�����ڲ��쳣��");
			throw new RuntimeException(e);
		}
		
		
	}
}
