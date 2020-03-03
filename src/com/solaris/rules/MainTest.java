package com.solaris.rules;

import com.alibaba.excel.util.NumberUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.Options;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang.math.NumberUtils.*;

//规则引擎 表达式求值引擎 Aviator
public class MainTest {
    @Test
    public void test1() {
        int[] a = {1, 2, 3, 4, 5};
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("a", a);

        System.out.println(AviatorEvaluator.execute("1 + 2 + 3"));
        System.out.println(AviatorEvaluator.execute("2==2"));
        System.out.println(AviatorEvaluator.execute("a[1] + 100", env));
        System.out.println(AviatorEvaluator.execute("'a[1]=' + a[1]", env));
        System.out.println(AviatorEvaluator.execute("string.contains(\"test\", string.substring('hello', 1, 2))"));

        Map<String, Object> paramMap = new HashMap();
        paramMap.put("str", "test");
        paramMap.put("src", "hello");
        paramMap.put("startIndex", 1);
        paramMap.put("endIndex", 2);
        System.out.println(AviatorEvaluator.execute("string.contains(str, string.substring(src, startIndex, endIndex))", paramMap));


    }

    @Test
    public void testPress() {
        //Expression exp= AviatorEvaluator.compile("long(a)-long(b)>20 ");
        AviatorEvaluator.setOption(Options.OPTIMIZE_LEVEL, AviatorEvaluator.EVAL);
        //AviatorEvaluator.setOption(Options.TRACE_EVAL, true);
        Expression exp = AviatorEvaluator.compile("a ==0 && a+b < 100*10000", true);
        System.out.println(exp.getVariableNames());
        System.out.println(exp.getVariableFullNames());
        List<Integer> list = IntStream.range(1, 10000 * 100).boxed().collect(Collectors.toList());
        Collections.shuffle(list);
        Integer pre = null;
        HashMap<String, Object> aMap = new HashMap<>();
        int sucNum = 0;
        long beginTime = System.currentTimeMillis();
        for (Integer now : list) {
            if (pre == null) {
                pre = now;
                continue;
            }
            aMap.put("a", pre);
            aMap.put("b", now);
            boolean bSucc = (Boolean) exp.execute(aMap);
            if (bSucc)
                sucNum++;
            pre = now;
        }
        long endTime = System.currentTimeMillis();
        System.out.println("cost mills:" + (endTime - beginTime) + " predicate num:" + sucNum);
    }

    public Set<String> readSignal() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("D:\\文档\\开发\\test\\data\\x9e\\bds.txt"), UTF_8);
        return lines.stream().flatMap(s -> AviatorEvaluator.compile(s).getVariableNames().stream())
                .distinct()
                .filter(s -> s.contains("_"))
                .sorted().collect(Collectors.toSet());
        //System.out.println("qb size:" + xx.size());
        //System.out.println(xx);
    }

    @Test
    public void readX9eFiles() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("D:\\文档\\开发\\test\\data\\x9e\\0.txt"), UTF_8);
        lines.addAll(Files.readAllLines(Paths.get("D:\\文档\\开发\\test\\data\\x9e\\1.txt"), UTF_8));
        lines.addAll(Files.readAllLines(Paths.get("D:\\文档\\开发\\test\\data\\x9e\\5.txt"), UTF_8));
        lines.addAll(Files.readAllLines(Paths.get("D:\\文档\\开发\\test\\data\\x9e\\30.txt"), UTF_8));
        lines.addAll(Files.readAllLines(Paths.get("D:\\文档\\开发\\test\\data\\x9e\\60.txt"), UTF_8));

        Map<String, Item> items = new HashMap();
        Set<String> predicateSet= readSignal();
        try {
            Map<String, Item> resultMap = lines.stream().reduce(items, (tmpMap, line) -> {
                JSONObject jsonObj = JSON.parseObject(line);
                String period = jsonObj.get("msgType").toString();
                JSONObject dataList = (JSONObject) jsonObj.get("dataList");
                for (Map.Entry<String, Object> entry : dataList.entrySet()) {
                    String k = entry.getKey();
                    if (k.contains("_") && predicateSet.contains(k)) {
                        String v = entry.getValue().toString();
                        Item item = new Item(k, v, period);
                        tmpMap.merge(k, item, Item::merge);
                    }
                }
                return tmpMap;
            }, (s1, s2) -> {
                s1.putAll(s2);
                return s1;
            });
            System.out.println("signal num:" + resultMap.size() + " " + resultMap);
            //resultMap.entrySet().stream().collect(Collectors.groupingBy());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testisDigits(){
        System.out.println(isDigits("2.1"));//false
        System.out.println(isDigits("2"));//true
        System.out.println(isDigits("-2"));//false
        System.out.println(isNumber("2"));//true
        System.out.println(isNumber("2.1"));//true
    }
}


class Item {
    String name;
    Set<String> valueSet = new HashSet();
    Set<String> period = new HashSet();

    public Item(String _name, String _value, String _period) {
        name = _name;
        valueSet.add(_value);
        period.add(_period);
    }

    public String getValueType(){
        String rtn="null";
        boolean isInterger=false;
        boolean isNum=false;
        for (String s:valueSet){
            if (isNumber(s))
                isNum=true;
            if (isInteger(s))
                isInterger=true;
        }
        if (isInterger)
            return "int";
        if (isNum)
            return "decimal";
        return rtn;
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    Item merge(Item other) {
        valueSet.addAll(other.valueSet);
        period.addAll(other.period);
        return this;
    }

    @Override
    public String toString() {
        return " period:" + period +" type:"+getValueType()+" example:"+valueSet.stream().limit(10)
                .collect(Collectors.joining(","))+ "\n";
    }
}
