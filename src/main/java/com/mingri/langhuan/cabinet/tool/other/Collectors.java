package com.mingri.langhuan.cabinet.tool.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Collectors<K, V> {

	private Map<K, V> map;

	private List<V> list;

	private Set<V> set;

	private boolean isDft;

	public Collectors(Map<K, V> map, List<V> list, Set<V> set) {
		this.map = map;
		this.list = list;
		this.set = set;
	}

	/**
	 * 创建一个默认的实例,默认会创建map、lis、set对象，默认map：HashMap，list:ArrayList，set:HashSet</br>
	 * @author ljl 
	 * @date 2020-11-5
	 * @param <K>  map的键类型
	 * @param <V>  map、list、set的值类型
	 * @return
	 */
	public static <K, V> Collectors<K, V> createDft() {
		Collectors<K, V> c = new Collectors<>();
		c.isDft = true;
		return c;
	}

	public Collectors() {
	}

	public Collectors(Map<K, V> map) {
		this.map = map;
	}

	public Collectors(Set<V> set) {
		this.set = set;
	}

	public Collectors(List<V> list) {
		this.list = list;
	}

	public void setMap(Map<K, V> map) {
		this.map = map;
	}

	public void setList(List<V> list) {
		this.list = list;
	}

	public void setSet(Set<V> set) {
		this.set = set;
	}

	public Map<K, V> map() {
		return map;
	}

	public List<V> list() {
		return list;
	}

	public Set<V> set() {
		return set;
	}

	public boolean listAdd(V v) {

		if (isDft && list == null) {
			list = new ArrayList<V>();
		}
		return list.add(v);
	}

	public boolean setAdd(V v) {
		if (isDft && set == null) {
			set = new HashSet<V>();
		}
		return set.add(v);
	}

	public V mapPut(K k, V v) {
		if (isDft && map == null) {
			map = new HashMap<K, V>();
		}
		return map.put(k, v);
	}

}
