package com.mingri.langhuan.cabinet.tool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CollectionTool {

	private CollectionTool() {
	}

	public static boolean notEmpty(Collection<?> collection) {
		return collection != null && !collection.isEmpty();
	}
	public static boolean isEmpty(Collection<?> collection) {
		return !notEmpty(collection);
	}

	/**
	 * 
	 * @param mainList     要切的集合
	 * @param subedList    切出来的集合
	 * @param startStepNum 第几步开始，一般都是第一步
	 * @param stepLen      一步多长
	 */
	public static <T> void subList(List<T> mainList, List<List<T>> subedList, int startStepNum, int stepLen) {

		int fromIndex = startStepNum * stepLen - stepLen;
		int toIndex = stepLen * startStepNum;
		int maxToIndex = mainList.size();
		toIndex = maxToIndex > toIndex ? toIndex : maxToIndex;

		List<T> subList = mainList.subList(fromIndex, toIndex);
		if (!subList.isEmpty()) {
			subedList.add(subList);
		}
		if (toIndex == maxToIndex) {
			return;
		}
		startStepNum++;
		CollectionTool.subList(mainList, subedList, startStepNum, stepLen);
	}

	/**
	 * 
	 * @param mainList     要切的集合
	 * @param subedList    切出来的集合
	 * @param startStepNum 第几步开始，一般都是第一步
	 * @param stepLen      一步多长
	 */
	public static <T> List<List<T>> subList(List<T> list, int groupCount) {

		List<List<T>> subedList = new ArrayList<>();
		int size = list.size();
		if (groupCount >= size) {
			list.forEach(e -> subedList.add(Collections.singletonList(e)));
		} else {
			// 残余
			int residue = size % groupCount;
			// 平均
			int average = size / groupCount;
			// 残余分配
			int spoilsCount = residue;
			for (int i = 0; i < groupCount; i++) {
				int fromIndex = i * average + (residue - spoilsCount);
				int toIndex = (i + 1) * average + (residue - spoilsCount);
				if (spoilsCount > 0) {
					subedList.add(list.subList(fromIndex, toIndex + 1));
					spoilsCount--;
				} else {
					subedList.add(list.subList(fromIndex, toIndex));
				}
			}
		}
		return subedList;
	}

	public static String aryToString(Collection<?> set, boolean itemToStr) {
		String str = "";
		if (set == null || set.isEmpty()) {
			return str;
		}
		str = set.toString().trim();
		str = str.substring(0, str.length() - 1).substring(1).replace(", ", ",");
		if (itemToStr) {
			str = "'" + str.replace(" ", "").replace(",", "','") + "'";
		}
		return str;
	}

	public static String aryToString(Collection<?> set, String splitStr) {
		String str = "";
		if (set == null || set.isEmpty()) {
			return str;
		}
		str = set.toString().trim();
		str = str.substring(0, str.length() - 1).substring(1).replace(", ", ",");
		if (splitStr != null) {
			str = str.toString().replace(",", splitStr);
		}
		return str;
	}

}
