package com.mingri.langhuan.cabinet.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mingri.langhuan.cabinet.constant.FileSufx;

public class FileTool {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileTool.class);

	public static void copy(InputStream sourceStream, File targetFile) throws FileNotFoundException, IOException {
		try (OutputStream outStream = new FileOutputStream(targetFile);) {
			byte[] buffer = new byte[8 * 1024];
			int bytesRead;
			while ((bytesRead = sourceStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
		} finally {
			try {
				if (sourceStream != null) {
					sourceStream.close();
				}
			} catch (IOException e) {
				LOGGER.error("捕获到异常,打印日志：{}", e);
			}
		}
	}

	public static void copy(File source, File target) throws FileNotFoundException, IOException {
		try (FileOutputStream output = new FileOutputStream(target);
				FileInputStream input = new FileInputStream(source);) {
			byte[] bt = new byte[1024];
			int realbyte = 0;
			int count = 0;
			while ((realbyte = input.read(bt)) > 0) {
				count++;
				output.write(bt, 0, realbyte);
				if (count > 100) {
					output.flush();
					count = 0;
				}
			}

		}
	}


	public static boolean isClassFile(String fileName) {
		return fileName.endsWith(FileSufx.CLAZZ);
	}

	/**
	 * 获取指定路径里面的指定包的class文件名称
	 * 
	 * @param packageName 包名称
	 * @param packagePath 物理路径
	 * @param recursive   是否递归获取子包里面的class
	 * @param classes     class集合
	 */
	public static void findClassName(String packageName, String packagePath, final boolean recursive,
			List<String> classes) {
		 findFileName(packageName, packagePath, recursive, classes, FileSufx.CLAZZ);
	}
	/**
	 * 获取指定路径里面的指定包的class文件名称
	 * 
	 * @param packageName 包名称
	 * @param packagePath 物理路径
	 * @param recursive   是否递归获取子包里面的class
	 * @param classes     class集合
	 * @param fileSufx    文件后缀名称
	 */
	public static void findFileName(String packageName, String packagePath, final boolean recursive,
			List<String> classes,String fileSufx) {
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		// 。如果存在 就获取包下的所有文件 包括目录
		// 。自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
		File[] dirfiles = dir
				.listFiles(file -> (recursive && file.isDirectory()) || (file.getName().endsWith(fileSufx)));
		// 。循环所有文件
		for (File file : dirfiles) {
			// 。如果是目录 则继续扫描
			if (file.isDirectory()) {
				findClassName(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
			} else {
				classes.add(packageName + '.' + file.getName());
			}
		}
	}
	/**
	 * 从包package中获取所有的Class
	 * 
	 * @param packageName 包名称 a.b.c
	 * @param recursive   是否循环迭代
	 * @return 返回类名集合
	 */
	public static List<String> scanClass(String packageName, boolean recursive) {

		// 。 class类的集合
		List<String> classes = new ArrayList<>();
		String packageDirName = FileTool.pkgToDir(packageName);

		// 。定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				// 。得到协议的名称
				String protocol = url.getProtocol();
				if ("file".equals(protocol)) {
					// 。获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					// 。以文件的方式扫描整个包下的文件 并添加到集合中
					FileTool.findClassName(packageName, filePath, recursive, classes);
					continue;
				}

				if ("jar".equals(protocol)) {

					try (JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();) {
						// 。从此jar包 得到一个枚举类
						Enumeration<JarEntry> entries = jar.entries();
						while (entries.hasMoreElements()) {
							// 。获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
							JarEntry entry = entries.nextElement();
							String qualifiedName = entry.getName();
							qualifiedName = qualifiedName.charAt(0) == FileTool.DIR_SEPARATOR ? qualifiedName.substring(1)
									: qualifiedName;
							// 。如果前半部分和定义的包名相同
							if (!qualifiedName.startsWith(packageDirName)) {
								continue;
							}
							int idx = qualifiedName.lastIndexOf(FileTool.DIR_SEPARATOR);
							boolean isPkg = idx != -1;
							if (isPkg) {
								packageName = FileTool.dirToPkg(qualifiedName.substring(0, idx));
							}
							// 。如果可以迭代下去 并且是一个包 如果是一个.class文件 而且不是目录
							if ((!isPkg || recursive) && FileTool.isClassFile(qualifiedName)) {
								classes.add(FileTool.dirToPkg(qualifiedName));
							}
						}
					}
				}
			}
		} catch (IOException e) {
			LOGGER.error("扫描类异常：", e);
		}

		return classes;
	}
	
	/**
	 * 目录分隔符
	 */
	public static final char DIR_SEPARATOR = '/';
	
	/**
	 * 包格式转目录格式：a.b.c 转 a/b/c
	 * @param pkgPath 包名称
	 * @return 目录格式字符串
	 */
	public static String pkgToDir(String pkgPath) {
		return pkgPath.replace('.', DIR_SEPARATOR);
	}
	
	/**
	 * 目录格式转包格式：a/b/c 转 a.b.c
	 * @param dirPath 目录名称
	 * @return java包格式字符串
	 */
	public static String dirToPkg(String dirPath) {
		return dirPath.replace(DIR_SEPARATOR, '.');
	}
}
