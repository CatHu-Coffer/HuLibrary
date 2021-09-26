package com.example.catl.hulibarry.sqlite.util.reflect;

import com.example.catl.hulibarry.sqlite.util.StringUtil;
import com.example.catl.hulibarry.sqlite.util.ValidateUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by tanqimin on 2015/11/4.
 */
public class ReflectUtil {
    private final static Map<String, Method> METHOD_CACHE = new HashMap<>();

    public static <TModel> Object getFieldValue(
            TModel model,
            Field field) {
        try {
            field.setAccessible(true);
            return field.get(model);
        } catch (IllegalAccessException e) {
//            Logger.e(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static <TModel> Object getFieldValue(
            TModel model,
            String fieldName) {
        return getFieldValue(model, getField(model.getClass(), fieldName));
    }

    /**
     * 获取类的所有字段（包括父类的私有字段）
     *
     * @param clazz
     * @return
     */
    public static List<Field> getFields(Class<?> clazz) {
        List<Field> result = new ArrayList<>();
        if (ValidateUtil.equalsIgnoreCase(clazz.getName(), "java.lang.Object")) return result;
        for (Field field : clazz.getDeclaredFields()) {
            result.add(field);
        }
        result.addAll(getFields(clazz.getSuperclass()));
        return result;
    }

    public static Field getField(
            Class<?> clazz,
            String fieldName) {
        List<Field> fields = getFields(clazz);
        for (Field field : fields) {
            if (ValidateUtil.equalsIgnoreCase(field.getName(), fieldName)) {
                return field;
            }
        }
        return null;
    }

    public static Method getSetter(
            Class<?> clazz,
            String fieldName) {
        String methodName = "set" + StringUtil.capitalize(fieldName);
        String cacheKey = clazz.getName().concat(".").concat(methodName);
        if (METHOD_CACHE.containsKey(cacheKey)) {
            return METHOD_CACHE.get(cacheKey);
        }

        Method method;
        try {
            method = clazz.getMethod(methodName, getField(clazz, fieldName).getType());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("Class : %s 字段 %s 不存在Setter方法", clazz.getName(), fieldName));
        }

        METHOD_CACHE.put(cacheKey, method);
        return method;
    }

    public static Method getGetter(
            Class<?> clazz,
            String fieldName) {
        String methodName = "get" + StringUtil.capitalize(fieldName);
        String cacheKey = clazz.getName().concat(".").concat(methodName);
        if (METHOD_CACHE.containsKey(cacheKey)) {
            return METHOD_CACHE.get(cacheKey);
        }

        Method method;
        try {
            method = clazz.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("Class : %s 字段 %s 不存在Getter方法", clazz.getName(), fieldName));
        }

        METHOD_CACHE.put(cacheKey, method);
        return method;
    }

    /**
     * 获取类加载器
     *
     * @return
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类
     *
     * @param className     类名
     * @param isInitialized
     * @return
     */
    public static Class<?> loadClass(
            String className,
            boolean isInitialized) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
//            Logger.e("Load class failure!", e);
            throw new RuntimeException(e);
        }

        return clazz;
    }

    /**
     * 加载指定包下的所有类
     *
     * @param packageName 包名
     * @return
     */
    public static Set<Class<?>> loadPackage(String packageName) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        String packagePath = url.getPath().replaceAll("%20", " ");
                        addClass(classSet, packagePath, packageName);
                    } else if (protocol.equals("jar")) {
                        JarURLConnection jarUrlConnection = (JarURLConnection) url.openConnection();
                        if (jarUrlConnection != null) {
                            JarFile jarFile = jarUrlConnection.getJarFile();
                            if (jarFile != null) {
                                Enumeration<JarEntry> jarEntries = jarFile.entries();
                                while (jarEntries.hasMoreElements()) {
                                    JarEntry jarEntry = jarEntries.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    if (jarEntryName.endsWith(".class")) {
                                        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                        doAddClass(classSet, className);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
//            Logger.e("Get class failure", e);
            throw new RuntimeException(e);
        }
        return classSet;
    }

    private static void addClass(
            Set<Class<?>> classSet,
            String packagePath,
            String packageName) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            public boolean accept(File filter) {
                return (filter.isFile() && filter.getName().endsWith(".class")) || filter.isDirectory();
            }
        });

        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (ValidateUtil.isNotBlank(packageName)) {
                    className = packageName.concat(".").concat(className);
                }
                doAddClass(classSet, className);
            } else {
                String subPackagePath = fileName;
                if (ValidateUtil.isNotBlank(packageName)) {
                    subPackagePath = packagePath.concat("/").concat(subPackagePath);
                }
                String subPackageName = fileName;
                if (ValidateUtil.isNotBlank(packageName)) {
                    subPackageName = packageName.concat(".").concat(subPackageName);
                }
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

    private static void doAddClass(
            Set<Class<?>> classSet,
            String className) {
        Class<?> clazz = loadClass(className, false);
        classSet.add(clazz);
    }
}
