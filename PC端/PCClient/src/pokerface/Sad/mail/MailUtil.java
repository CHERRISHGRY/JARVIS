package pokerface.Sad.mail;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.commons.mail.EmailException;  
import org.apache.commons.mail.HtmlEmail;  

import pokerface.Sad.util.Util;

  
  
/**  
 * �ʼ����͹���ʵ����  
 */  
public class MailUtil { 
	public static void sendPic(String subject,String picPath) throws FileNotFoundException, IOException, EmailException{
		Properties pro = Util.getProperties();
		Mail mail = new Mail();  
	    mail.setHost(pro.getProperty("mailHost")); // �����ʼ�������  
	    mail.setSender(pro.getProperty("mailSender"));  
	    mail.setReceiver(pro.getProperty("mailReceiver")); // ������  
	    mail.setUsername(pro.getProperty("mailUsername")); // ��¼�˺�  
	    mail.setPassword(pro.getProperty("mailPassword")); // ����������ĵ�¼����  
	    mail.setSubject(subject);  
	    mail.setMessage(Util.getDate());
	    mail.setAttachment(picPath, "", "");
	    new MailUtil().send(mail);
	}
	
    public void send(Mail mail) throws EmailException {  
        // ����email  
        HtmlEmail email = new HtmlEmail();  
        // ������SMTP���ͷ����������֣�163�����£�"smtp.163.com"  
        email.setHostName(mail.getHost());  
        // �ַ����뼯������  
        email.setCharset(Mail.ENCODEING);  
        // �ռ��˵�����  
        email.addTo(mail.getReceiver());  
        // �����˵�����  
        email.setFrom(mail.getSender(), mail.getName());  
        // �����Ҫ��֤��Ϣ�Ļ���������֤���û���-���롣�ֱ�Ϊ���������ʼ��������ϵ�ע�����ƺ�����  
        email.setAuthentication(mail.getUsername(), mail.getPassword());  
        // Ҫ���͵��ʼ�����  
        email.setSubject(mail.getSubject());  
        // Ҫ���͵���Ϣ������ʹ����HtmlEmail���������ʼ�������ʹ��HTML��ǩ  
        email.setMsg(mail.getMessage());  
        //��Ӹ���
        if(mail.getAttachment()!=null)
        	email.attach(mail.getAttachment());
        // ����  
        email.send();  
        System.out.println(mail.getSender() + " �����ʼ��� " + mail.getReceiver());  
    }  
  
}  