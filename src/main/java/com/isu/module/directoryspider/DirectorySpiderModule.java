package com.isu.module.directoryspider;

import com.google.inject.AbstractModule;

public class DirectorySpiderModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DirectorySpider.class);
	}
}
