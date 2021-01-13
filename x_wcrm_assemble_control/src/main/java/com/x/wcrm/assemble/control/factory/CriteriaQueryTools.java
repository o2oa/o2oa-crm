package com.x.wcrm.assemble.control.factory;

import java.lang.reflect.Field;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

public class CriteriaQueryTools {
	public static Order setOrder(CriteriaBuilder cb, Root<?> root, Class<?> clazz_, String fieldName, String orderType) {

		Boolean fieldExists = false;
		Field[] fields = clazz_.getFields();
		for (Field field : fields) {
			if (StringUtils.equalsIgnoreCase(field.getName(), fieldName)) {
				fieldName = field.getName(); // 忽略大小写之后，重置查询字段的名称
				fieldExists = true;
			}
		}

		if (!fieldExists) {
			return null; // 如果查询字段根本和object 对不上，那么就返回null
		}

		if (StringUtils.equalsIgnoreCase(orderType, "asc")) {
			return cb.asc(root.get(fieldName));
		} else {
			return cb.desc(root.get(fieldName));
		}
	}

//	public static <T extends JpaObject, T_ extends SliceJpaObject_> Order getOrder(CriteriaBuilder cb, Root<T> root, Class<T_> cls_, String fieldName,
//			String orderType) {
//		Boolean fieldExists = false;
//		Field[] fields = cls_.getDeclaredFields();
//		for (Field field : fields) {
//			if (field.getName().equalsIgnoreCase(fieldName)) {
//				fieldName = field.getName(); // 校正排序列的名称
//				fieldExists = true;
//			}
//		}
//		if (!fieldExists) { // 如果排序列不存在，就直接返回空，不排序，让SQL可以正常执行
//			return null;
//		}
//
//		if (StringUtils.equalsIgnoreCase(orderType, "desc")) {
//			return cb.desc(root.get(fieldName).as(String.class));
//		} else {
//			return cb.asc(root.get(fieldName).as(String.class));
//		}
//	}

	public static Predicate predicate_or(CriteriaBuilder criteriaBuilder, Predicate predicate, Predicate predicate_target) {
		if (predicate == null) {
			return predicate_target;
		} else {
			if (predicate_target != null) {
				return criteriaBuilder.or(predicate, predicate_target);
			} else {
				return predicate;
			}
		}
	}

	public static Predicate predicate_and(CriteriaBuilder criteriaBuilder, Predicate predicate, Predicate predicate_target) {
		if (predicate == null) {
			return predicate_target;
		} else {
			if (predicate_target != null) {
				return criteriaBuilder.and(predicate, predicate_target);
			} else {
				return predicate;
			}
		}
	}
}
