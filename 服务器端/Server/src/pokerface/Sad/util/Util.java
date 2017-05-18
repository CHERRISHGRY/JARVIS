package pokerface.Sad.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;




public class Util {
	//��ȡ�����ļ�
	public static Properties getProperties() throws FileNotFoundException, IOException{
		Properties pro = new Properties();
		pro.load(new FileInputStream("Server.properties"));
		return pro;
	}
	//��ȡ��ǰʱ��
	public static String getDate() throws FileNotFoundException, IOException {
		Properties pro = null;
		String date = null;
		pro = getProperties();
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(pro.getProperty("dateFormat"));
		date = sdf.format(d);
		return date;
	}
}
