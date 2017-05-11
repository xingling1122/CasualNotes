package com.xl.projectno.base;

import java.util.concurrent.ThreadFactory;

/**
 * 
 * <br>类描述:
 * <br>功能详细描述:
 * 
 * @author  linhang
 * @date  [2013-12-17]
 */
public class CustomThreadFactory implements ThreadFactory {

	@Override
	public Thread newThread(Runnable r) {
		return new CustomThread(r);
	}

}
