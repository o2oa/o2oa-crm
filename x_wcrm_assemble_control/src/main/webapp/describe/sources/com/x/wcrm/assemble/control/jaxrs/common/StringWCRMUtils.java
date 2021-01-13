package com.x.wcrm.assemble.control.jaxrs.common;

import org.apache.commons.lang3.StringUtils;

/**
 * 校验关键字是否为空
 * keyString : 关键字
 */
public class StringWCRMUtils {

	public static boolean isEmptyKeyString(String keyString) {
		if (null == keyString || StringUtils.isBlank(keyString) || StringUtils.isEmpty(keyString)) {
			return true;
		}

		if (StringUtils.isNotEmpty(keyString)) {
			String key = StringUtils.trim(StringUtils.replaceEach(keyString, new String[] { "\u3000", "?", "%" },
					new String[] { " ", "", "" }));
			if (StringUtils.isEmpty(key) || StringUtils.isBlank(key)) {
				return true;
			} else {
				return false;
			}

		} else {
			return false;
		}
	}

}
