package pokerface.Sad.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.commons.mail.EmailException;

import pokerface.Sad.connect.Client;
import pokerface.Sad.util.Util;

public class ServiceImpl1 implements Service{
	@Override
	public String execOrder(String order) {
		String result = "ִ��ʧ��";
		Client c1 = null;
		String methodName = null;
		Properties pro = null;
		Class cls = null;
		Method method = null;
		//������Ƽ��ع�����
		try {
			cls = Class.forName("pokerface.Sad.util.Util");
			pro = Util.getProperties();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		methodName = pro.getProperty(order); //�������ļ��л�ȡ�����Ӧ�ķ�����
		
		if(methodName!=null)
		{
			System.out.println("���յ�"+order+"ָ��");
			System.out.println("ִ��"+methodName+"����");
			//������Ƶ��÷���
			try {
				cls.getDeclaredMethod(methodName).invoke(cls);
				result = order+"ִ�гɹ�";
			} catch (IllegalAccessException | IllegalArgumentException  | InvocationTargetException e) {
				e.printStackTrace();
				Throwable cause = e.getCause();
				if(cause instanceof EmailException){
					result = "�ʼ�����ʧ��";
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			
			order=null;
			methodName=null;
		}else{
			//û�ж�Ӧ����
			System.out.println("���յ�"+order+"ָ��");
			System.out.println("û�ж�Ӧ����");
			result = order+"ָ�����û�ж�Ӧ����";
		}
		return result;
	}

}
