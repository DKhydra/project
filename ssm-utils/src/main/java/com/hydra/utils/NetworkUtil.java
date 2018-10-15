package com.hydra.utils;

import java.net.InetAddress;

public class NetworkUtil {

	private static String hostname;

	private static String ip;

	static {
		try {
			InetAddress address = InetAddress.getLocalHost();
			hostname = address.getHostName();
			ip = address.getHostAddress();
		} catch (Exception e) {
			throw new RuntimeException("hostname/ip not found", e);
		}
	}

	public static String getHostname() {
		return hostname;
	}

	public static String getHostIp() {
		return ip;
	}

}
