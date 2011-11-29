package com.isu.internals.service;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Lists;

public final class ServiceManager 
{	
	private static List<Service> services = new LinkedList<Service>();
	
	/**
	 * 
	 * @param service
	 */
	public static void addShutdownHook(Service service) {
		services.add(service);
	}
	
	/**
	 * Shuts down all services
	 */
	public static void shutdownAllServices() {
		// We have to reverse them so that shutdown order is the opposite of
		// start order.
		List<Service> reversedServices = Lists.reverse(services);
		for (Service service : reversedServices) {
			service.shutdown();
		}
	}
}
