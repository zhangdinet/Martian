package com.mars.tomcat.server;

import com.mars.common.base.config.MarsConfig;
import com.mars.common.constant.MarsConstant;
import com.mars.common.constant.MarsSpace;
import com.mars.common.util.MarsConfiguration;
import com.mars.tomcat.server.threadpool.ThreadPool;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * tomcat服务
 * @author yuye
 *
 */
public class MarsServer {

	private static Logger log = LoggerFactory.getLogger(MarsServer.class);
	
	/**
	 * 启动tomcat服务
	 * @param portNumber
	 */
	public static void start(int portNumber) {
		try {

			// 获取最大并发数
			MarsConfig marsConfig = MarsConfiguration.getConfig();
			int backLog = marsConfig.getThreadPoolConfig().getBackLog();

			// 创建服务
			HttpServer httpServer = HttpServer.create(new InetSocketAddress(portNumber),backLog);
			httpServer.createContext("/", new MarsServerHandler());

			//设置服务器的线程池对象
			httpServer.setExecutor(ThreadPool.getThreadPoolExecutor());

			/* 标识tomcat是否已经启动 */
			MarsSpace.getEasySpace().setAttr(MarsConstant.HAS_NETTY_START,"yes");
			log.info("启动成功");

			//启动服务器
			httpServer.start();
		} catch (Exception e) {
			log.error("启动tomcat报错",e);
		}
	}
}
