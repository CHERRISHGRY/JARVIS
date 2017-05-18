package pokerface.Sad.connect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import pokerface.Sad.util.Util;


public class Server extends ServerSocket{
	
	final static String heatbeatMsg = "heartbeating";
	Socket pcClient = null;  //PC��Client
	Socket webClient = null; //web��Client
	boolean pcConnectState = false;
	
	public static void main(String[] args) throws IOException {
		Server s = new Server();
		System.out.println("wait for Web part...");
		s.acceptWeb();//�ȴ�Web����
		//������������
		System.out.println("JARVIS Service Start Up Normally......");
		Thread webMonitorThread = new Thread(new WebOrderMonitor(s));
		webMonitorThread.start();
		s.acceptPC();//�ȴ�PC����
		Thread clientMonitorThead = new Thread(new PCStateMonitor(s));
		clientMonitorThead.start();
		while(true)
		{
			//if((!webMonitorThread.isAlive())&&(!clientMonitorThead.isAlive()))
			if(!clientMonitorThead.isAlive())
			{
				s.acceptPC(); //�ȴ����߳���ֹ��PC���ѶϿ����ӣ��ȴ�PC�ٴ�����
				//webMonitorThread = new Thread(new WebOrderMonitor(s));
				clientMonitorThead = new Thread(new PCStateMonitor(s));
//				webMonitorThread.start();
				clientMonitorThead.start();
			}
		}
		
	}
	public Server() throws IOException {
		//�������ļ��ж�ȡ�˿ڣ�������Server����
		super(new Integer(Util.getProperties().getProperty("serverPort")));
	}
	//�ȴ�PC����
	public void acceptPC() throws IOException{
		System.out.println("wait for PC connect......");
		pcClient = this.accept();
		System.out.println("PC :"+pcClient.getInetAddress()+" connect");
		pcConnectState = true;
	}
	//�ȴ�WebӦ������
	public void acceptWeb() throws IOException{
		webClient = this.accept();
		System.out.println("Web :"+webClient.getInetAddress()+" connect");
	}
	//��PC�˷�������
	public void sendMsgToClient(Socket client,String msg) throws IOException{
		OutputStream os = client.getOutputStream();
		os.write(msg.getBytes());
		os.flush();
		//os.close(); socket�����ܹر�
	}
	//��PC�˽��ս����Ϣ
	public String getMsgFromClient(Socket client){
		InputStream is = null;
		byte[] buf = null;
		int Len = 0;
		try {
			is = client.getInputStream();
			buf = new byte[1024];
			Len = is.read(buf);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String result = null;
		try {
			result = new String(buf,0,Len,"GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	
		return result;
	}
	
	////��webӦ�ô���������,�����ȴ�ָ��ֱ��PC�˶Ͽ�Ϊֹ
	//��Web���ֳ�����
	public String receiveOrder() throws IOException{
//		this.webClient.setSoTimeout(5000);//���ó�ʱ���ԣ���ֹ������read������
		InputStream is = this.webClient.getInputStream();
		byte[] buf = new byte[1024];
		Integer len = null;
		String msg = null;
		
		if((len=is.read(buf))!=-1)
		{
			msg = new String(buf, 0, len);
			return msg;
		}
		/*
		//��PC�������ӣ���һֱ�ȴ�Web�˴���ָ��
		//pcConnectState��ClientMonitor�߳�ά��
		while(this.pcConnectState)
		{
			try {
				if((len=is.read(buf))!=-1)
				{
					msg = new String(buf, 0, len);
					return msg;
				}
			} catch (SocketTimeoutException e) {
				//ÿ��������һ���ж�PC���Ƿ�����
			}
		}
		*/
		return null;
	}
	public void close(){
		if(this.pcClient!=null)
		{
			try {
				this.pcClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.close();
	}
	public boolean isConnected(){
        try{
        	/*
        	 * �˷��ͽ������ݵķ�����Windows�����»�����쳣
        	 * this.pcClient.sendUrgentData(0xff);
        	 * */
        	this.sendMsgToClient(this.pcClient,Server.heatbeatMsg);
        	return true;
        }catch(Exception e){
            return false;
        }
}
}

//�ȴ�Web�����߳�
class WebOrderMonitor implements Runnable{
	Server server = null;
	public WebOrderMonitor(Server Server) {
		this.server = Server;
	}
	public void run() {
		try {
			
			String order = null;
			
			while(true)
			{
//				if(!server.isConnected())
//				{
//					//��PC���ѶϿ�����ֹ�߳�
//					return;
//				}
				
				//��������Web��ָ��ֱ��PC�˶Ͽ�������null
				order = server.receiveOrder();
//				if(order==null) 
//				{
//					System.out.println("�ȴ�Web�����߳���ֹ");
//					return; //��PC���ѶϿ�����ֹ�߳�
//				}
				//�����յ�������ת����PC��
				String result = null;
				if(server.pcConnectState == true)
				{
					server.sendMsgToClient(server.pcClient,order);
					//����PC�˷����Ľ����Ϣ
					result = server.getMsgFromClient(server.pcClient);
				}else{
					result = "PC������";
				}
				//�������Ϣת����web��
				server.sendMsgToClient(server.webClient, result);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
//�ȴ��ͻ��˹ر��߳�
class PCStateMonitor implements Runnable{

	Server Server = null;
	public PCStateMonitor(Server s) {
		this.Server = s;
	}
	public void run() {

		while(Server.isConnected()){
			//���PC���Ƿ�����
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
		System.out.println("PC��������");
		this.Server.pcConnectState = false;
		System.out.println("�ȴ��ͻ��˹ر��߳���ֹ");
	}
}
