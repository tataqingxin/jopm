package com.zjy.jopm.base.common;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.mail.HtmlEmail;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.unistc.utils.PropertiesUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class SendMailUtil {
	// private static final String smtphost = "192.168.1.70";
	private static  String fromEmail = "";
	private static  String fromName = "";
	private static  String charSet = "utf-8";
	public static  String username = "";
	private static  String password = "";
	private static Map<String, String> hostMap = new HashMap<String, String>();
	static {
		 PropertiesUtil p = new PropertiesUtil("emailconfig.properties");
		 fromName = p.readProperty("fromname");
		 fromEmail = p.readProperty("fromemail");
		 username = p.readProperty("emailusername");
		 password = p.readProperty("emailuserpwd");
		// 126
		hostMap.put("smtp.126", "smtp.126.com");
		// qq
		hostMap.put("smtp.qq", "smtp.qq.com");

		// 163
		hostMap.put("smtp.163", "smtp.163.com");

		// sina
		hostMap.put("smtp.sina", "smtp.sina.com.cn");

		// tom
		hostMap.put("smtp.tom", "smtp.tom.com");

		// 263
		hostMap.put("smtp.263", "smtp.263.net");

		// yahoo
		hostMap.put("smtp.yahoo", "smtp.mail.yahoo.com");

		// hotmail
		hostMap.put("smtp.hotmail", "smtp.live.com");

		// gmail
		hostMap.put("smtp.gmail", "smtp.gmail.com");
		hostMap.put("smtp.port.gmail", "465");
		
		hostMap.put("smtp.moe", "202.205.184.197");
//		hostMap.put("smtp.moe", "smtp.moe.edu.cn");
		// zijiny
		hostMap.put("smtp.zijinyun", "smtp.mxhichina.com");
	}

	public static String getHost(String email) throws Exception {
		Pattern pattern = Pattern.compile("\\w+@(\\w+)(\\.\\w+){1,2}");
		Matcher matcher = pattern.matcher(email);
		String key = "unSupportEmail";
		if (matcher.find()) {
			key = "smtp." + matcher.group(1);
		}
		if (hostMap.containsKey(key)) {
			return hostMap.get(key);
		} else {
			throw new Exception("unSupportEmail");
		}
	}

	public static int getSmtpPort(String email) throws Exception {
		Pattern pattern = Pattern.compile("\\w+@(\\w+)(\\.\\w+){1,2}");
		Matcher matcher = pattern.matcher(email);
		String key = "unSupportEmail";
		if (matcher.find()) {
			key = "smtp.port." + matcher.group(1);
		}
		if (hostMap.containsKey(key)) {
			return Integer.parseInt(hostMap.get(key));
		} else {
			return 25;
		}
	}

	/**
	 * 发送模板邮件
	 * 
	 * @param toMailAddr
	 *            收信人地址
	 * @param subject
	 *            email主题
	 * @param templatePath
	 *            模板地址
	 * @param map
	 *            模板map
	 */
	@SuppressWarnings("deprecation")
	public static boolean sendFtlMail(String toMailAddr, String subject, String templatePath,
			Map<String, Object> map) {
		Template template = null;
		Configuration freeMarkerConfig = null;
		HtmlEmail hemail = new HtmlEmail();
		try {
			hemail.setHostName(getHost(fromEmail));
			hemail.setSmtpPort(getSmtpPort(fromEmail));
			hemail.setCharset(charSet);
			hemail.addTo(toMailAddr);
			hemail.setFrom(fromEmail, fromName);
			hemail.setAuthentication(username, password);
			hemail.setSubject(subject);
			freeMarkerConfig = new Configuration();
			freeMarkerConfig.setDirectoryForTemplateLoading(new File(getFilePath()));
			// 获取模板
			template = freeMarkerConfig.getTemplate(getFileName(templatePath), new Locale("Zh_cn"),
					"UTF-8");
			// 模板内容转换为string
			String htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
//			System.out.println(htmlText);
			hemail.setMsg(htmlText);
			hemail.send();
//			System.out.println("email send true!");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
//			System.out.println("email send error!");
			return false;
		}
	}

	/**
	 * 发送系统生成账户信息模板邮件
	 * @param toMailAddr 收件人邮箱
	 * @param subject 内容
	 * @param map
	 */
	public static boolean sendSystemUserInfoMail(String toMailAddr,String subject, Map<String, Object> map) {
		String templatePath = "sendusernamepwd.ftl";
		boolean result = sendFtlMail(toMailAddr, subject, templatePath, map);
		return result;
	}
	
	/**
	 * 发送修改密码地址模板邮件
	 * @param toMailAddr 收件人邮箱
	 * @param subject 内容
	 * @param map
	 */
	public static boolean sendUpdPwdUrlMail(String toMailAddr,String subject, Map<String, Object> map) {
		String templatePath = "sendUpdatePwdUrl.ftl";
		boolean result = sendFtlMail(toMailAddr, subject, templatePath, map);
		return result;
	}
	
	/**
	 * 发送服务平台监控信息
	 * @param toMailAddr 收件人邮箱
	 * @param subject 内容
	 * @param map
	 */
	public static boolean sendSystemExceptionMail(String toMailAddr,String subject, Map<String, Object> map) {
		String templatePath = "systemexceptionemail.ftl";
		boolean result = sendFtlMail(toMailAddr, subject, templatePath, map);
		return result;
	}


	/**
	 * 发送普通邮件
	 * 
	 * @param toMailAddr
	 *            收信人地址
	 * @param subject
	 *            email主题
	 * @param message
	 *            发送email信息
	 */
	public static void sendCommonMail(String toMailAddr, String subject, String message) {
		HtmlEmail hemail = new HtmlEmail();
		try {
			hemail.setHostName(getHost(fromEmail));
			hemail.setSmtpPort(getSmtpPort(fromEmail));
			hemail.setCharset(charSet);
			hemail.addTo(toMailAddr);
			hemail.setFrom(fromEmail, fromName);
			hemail.setAuthentication(username, password);
			hemail.setSubject(subject);
			hemail.setMsg(message);
			hemail.send();
			System.out.println("email send true!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("email send error!");
		}

	}

	@SuppressWarnings("deprecation")
	public static String getHtmlText(String templatePath, Map<String, Object> map) {
		Template template = null;
		String htmlText = "";
		try {
			Configuration freeMarkerConfig = null;
			freeMarkerConfig = new Configuration();
			freeMarkerConfig.setDirectoryForTemplateLoading(new File(getFilePath()));
			// 获取模板
			template = freeMarkerConfig.getTemplate(getFileName(templatePath), new Locale("Zh_cn"),
					"UTF-8");
			// 模板内容转换为string
			htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
//			System.out.println(htmlText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return htmlText;
	}

	private static String getFilePath() {
		String path = getAppPath(SendMailUtil.class);
		path = path + File.separator + "mailtemplate" + File.separator;
		path = path.replace("\\", "/");
//		System.out.println(path);
		return path;
	}

	private static String getFileName(String path) {
		path = path.replace("\\", "/");
//		System.out.println(path);
		return path.substring(path.lastIndexOf("/") + 1);
	}

	public static String getAppPath(Class<?> cls) {
		// 检查用户传入的参数是否为空
		if (cls == null)
			throw new java.lang.IllegalArgumentException("参数不能为空！");
		ClassLoader loader = cls.getClassLoader();
		// 获得类的全名，包括包名
		String clsName = cls.getName() + ".class";
		// 获得传入参数所在的包
		Package pack = cls.getPackage();
		String path = "";
		// 如果不是匿名包，将包名转化为路径
		if (pack != null) {
			String packName = pack.getName();
			// 此处简单判定是否是Java基础类库，防止用户传入JDK内置的类库
			if (packName.startsWith("java.") || packName.startsWith("javax."))
				throw new java.lang.IllegalArgumentException("不要传送系统类！");
			// 在类的名称中，去掉包名的部分，获得类的文件名
			clsName = clsName.substring(packName.length() + 1);
			// 判定包名是否是简单包名，如果是，则直接将包名转换为路径，
			if (packName.indexOf(".") < 0)
				path = packName + "/";
			else {// 否则按照包名的组成部分，将包名转换为路径
				int start = 0, end = 0;
				end = packName.indexOf(".");
				while (end != -1) {
					path = path + packName.substring(start, end) + "/";
					start = end + 1;
					end = packName.indexOf(".", start);
				}
				path = path + packName.substring(start) + "/";
			}
		}
		// 调用ClassLoader的getResource方法，传入包含路径信息的类文件名
		java.net.URL url = loader.getResource(path + clsName);
		// 从URL对象中获取路径信息
		String realPath = url.getPath();
		// 去掉路径信息中的协议名"file:"
		int pos = realPath.indexOf("file:");
		if (pos > -1)
			realPath = realPath.substring(pos + 5);
		// 去掉路径信息最后包含类文件信息的部分，得到类所在的路径
		pos = realPath.indexOf(path + clsName);
		realPath = realPath.substring(0, pos - 1);
		// 如果类文件被打包到JAR等文件中时，去掉对应的JAR等打包文件名
		if (realPath.endsWith("!"))
			realPath = realPath.substring(0, realPath.lastIndexOf("/"));
		/*------------------------------------------------------------ 
		 ClassLoader的getResource方法使用了utf-8对路径信息进行了编码，当路径 
		  中存在中文和空格时，他会对这些字符进行转换，这样，得到的往往不是我们想要 
		  的真实路径，在此，调用了URLDecoder的decode方法进行解码，以便得到原始的 
		  中文及空格路径 
		-------------------------------------------------------------*/
		try {
			realPath = java.net.URLDecoder.decode(realPath, "utf-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
//		System.out.println("realPath----->" + realPath);
		return realPath;
	}

	// private static File getFile(String path){
	// File file =
	// SendMail.class.getClassLoader().getResource("mailtemplate/test.ftl").getFile();
	// return file;
	// }
	//

	public static void main(String[] args) throws Exception {
		// HtmlEmail hemail = new HtmlEmail();
		// try {
		// hemail.setHostName("smtp.exmail.qq.com");
		// hemail.setCharset("utf-8");
		// hemail.addTo("fly.1206@qq.com");
		// hemail.setFrom("zhoujunfeng@et-bank.com", "周俊峰");
		// hemail.setAuthentication("zhoujunfeng@et-bank.com", "31415926@aa");
		// hemail.setSubject("sendemail test!");
		// hemail.setMsg("<a href=\"http://www.google.cn\">谷歌</a><br/>");
		// hemail.send();
		// System.out.println("email send true!");
		// } catch (Exception e) {
		// e.printStackTrace();
		// System.out.println("email send error!");
		// }
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("receiveorgname", "测试标题");
		map.put("username", "测试 内容");
		map.put("pwd", "测试 内容");
		map.put("sendorgname", "测试 内容");
		String templatePath = "sendusernamepwd.ftl";
		sendFtlMail("lideming00@163.com", "sendemail test!", templatePath, map);
		// System.out.println(getFileName("mailtemplate/test.ftl"));
	}

}