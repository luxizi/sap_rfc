package com.luxizi.rfc.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分包工具类
 */
public class PackageUtil {

    /**
     * @Author luxizi
     * @Description TODO 将大包拆成若干个小包
     * @Date 18:03 2019/8/13
     * @Param [size, list] size拆分的小包元素个数，list传入要拆分的包
     * @return java.util.List<java.lang.Object>
     **/
    public static <T> List<List<T>> packing(Integer size, List<T> list) {
        List<List<T>> bigPack = new ArrayList<>();
        int groupNum = list.size() % size == 0 ? list.size() / size : list.size() / size + 1;
        for (int i = 0; i < groupNum; i++) {
            List<T> littlePack = new ArrayList<>();
            for (int j = i * size; j < size * (i + 1); j++) {
                if (i == groupNum - 1) {
                    for (int f = j; f < list.size(); f++) {
                        littlePack.add(list.get(f));
                    }
                    break;
                }
                littlePack.add(list.get(j));
            }
            bigPack.add(littlePack);
        }
        return bigPack;
    }

    /**
     * 将集合按照某字段分成多个小集合
     * @param attributeName 要进行区分的属性名字母要大写
     * @param list 要进行分组的的集合
     */
    public static <T> Map<String,List<T>> groupByAttribute(String attributeName, List<T> list){
        Map<String,List<T>> map = new HashMap<>();
        if (list.size()==0){
            return map;
        }
        try {
            T t = list.get(0);
            Class<?> aClass = t.getClass();
            Method method = aClass.getMethod("get" + attributeName);
            for (T object : list) {
                String attribute =(String) method.invoke(object);
                //如果分类的属性为空那么跳过
                if (attribute == null || "".equals(attribute)){
                    continue;
                }
                if (map.containsKey(attribute)){
                    map.get(attribute).add(object);
                }else{
                    List<T> objects = new ArrayList<>();
                    objects.add(object);
                    map.put(attribute,objects);
                }
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }
}
