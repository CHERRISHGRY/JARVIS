package pokerface.Sad.util;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.apache.commons.mail.EmailException;

import pokerface.Sad.mail.MailUtil;


public class Util {
	//��ȡ�����ļ�
	public static Properties getProperties() throws FileNotFoundException, IOException{
		Properties pro = new Properties();
		pro.load(new FileInputStream("PCClient.properties"));
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
	//��ͼ
	public static void screenShot() throws IOException, AWTException, EmailException {
			Properties pro = getProperties();
			String filePath = pro.getProperty("RobotWorkPlace")+"PrtSc/screen.jpg";
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			BufferedImage bim = new Robot().createScreenCapture(new Rectangle(
					0, 0, dim.width, dim.height));
			ImageIO.write(bim, "jpg", new File(filePath));
			//�ʼ����ͽ�ͼ
			MailUtil.sendScreenShot();
	}

	//�ػ�
	public static void shutdown() throws IOException, AWTException, EmailException{
		Runtime.getRuntime().exec("shutdown -s -t 60");
		screenShot();
	}
}
