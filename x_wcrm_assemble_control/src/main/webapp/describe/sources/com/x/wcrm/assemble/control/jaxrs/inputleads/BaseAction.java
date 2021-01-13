package com.x.wcrm.assemble.control.jaxrs.inputleads;

import com.x.base.core.project.cache.ApplicationCache;
import com.x.base.core.project.jaxrs.StandardJaxrsAction;
import com.x.wcrm.assemble.control.service.LeadsService;

import net.sf.ehcache.Ehcache;

public class BaseAction extends StandardJaxrsAction {

	protected Ehcache cache = ApplicationCache.instance().getCache(CacheInputResult.class);

	protected LeadsService leadsService = new LeadsService();
	
	public static class CacheInputResult {

		private String name;

		private byte[] bytes;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public byte[] getBytes() {
			return bytes;
		}

		public void setBytes(byte[] bytes) {
			this.bytes = bytes;
		}

	}
}
