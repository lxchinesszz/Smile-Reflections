package reflectest;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.reflections.Reflections;
import org.reflections.scanners.*;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import reflect.modle.*;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import static org.reflections.ReflectionUtils.*;

/**
 * @Package: reflectest
 * @Description: 反射工具
 * @author: liuxin
 * @date: 2017/9/18 上午11:16
 */
public class Test {
    @org.junit.Test
    public void test0()throws Exception{
        Set<Method> getOrderInfo = getMethods(OtoServiceImpl.class, withReturnType(OrderDo.class), withName("getOrderInfo"));
        getOrderInfo.forEach(x->{
            ApiMapping annotation = x.getAnnotation(ApiMapping.class);
            System.out.println(annotation);
        });
    }


    /**
     * TODO 精确查找
     * withName("getOrderInfo") 指定方法
     * withModifier(Modifier.PUBLIC),//修饰符
     * withParametersCount(1),//参数
     * withPrefix(""));//前缀
     * withReturnType(OrderDo.class) 返回值类型
     * withParameters(String.class,Integer.class) 区分先后顺序
     * withParametersAssignableTo(Collection.class) 限制参数类型
     * withTypeAssignableTo(int.class) 对字段有效
     */
    @org.junit.Test
    public void test() {
        System.out.println("----------------");
        //TODO 查询该类中所有的修饰符为public的方法
        Set<Method> allMethods = getAllMethods(OtoServiceImpl.class, withModifier(Modifier.PUBLIC), withParameters(String.class, Integer.class));
        allMethods.forEach(x -> System.out.println(x));

        //TODO 根据注解和类型查询
        Set<Field> fields = getAllFields(OrderDo.class, withAnnotation(Ignore.class), withTypeAssignableTo(int.class));
        fields.forEach(x->{
            System.out.println(x.getType()+"/"+x.getName());
        });

        //TODO 参数类型是集合，返回值是布尔类型
        Set<Method> listMethodsFromCollectionToBoolean =
                getAllMethods(List.class,
                        withParametersAssignableTo(Collection.class), withReturnType(boolean.class));
        listMethodsFromCollectionToBoolean.forEach(x->{
            System.out.println(x.getName());
        });
    }

    /**
     * 根据包扫描
     */
    @org.junit.Test
    public void test1() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("reflect.modle"))
                .setScanners(new SubTypesScanner())
                .filterInputsBy(new FilterBuilder().includePackage("reflect.modle")));
        Set<Class<? extends OtoService>> subTypesOf = reflections.getSubTypesOf(OtoService.class);

        subTypesOf.forEach(x -> {
            System.out.println(x.getName());
            try {
                Method payment = x.getMethod("payment", String.class, String.class);
                ;
                System.out.println(payment.getGenericReturnType());
            } catch (NoSuchMethodException e) {

            } catch (SecurityException se) {

            }
        });


    }

    /**
     * 根据注解扫描
     */
    @org.junit.Test
    public void test2() {
        Reflections reflections = new Reflections("reflect.modle");
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(GateWayMapping.class);
        typesAnnotatedWith.forEach(x -> {
            if (x.isInterface()) {
                System.out.println("接口:" + x.getName());
            } else {
                System.out.println("类" + x.getName());
            }
        });
    }

    /**
     * 根据方法上的注解扫描方法
     * 要配置扫描器
     * new SubTypesScanner() 子类扫描
     * new TypeAnnotationsScanner() 注解扫描
     * new MethodAnnotationsScanner() 方法扫描
     * new FieldAnnotationsScanner() 字段扫描
     * new MethodParameterNamesScanner() 方法参数查找
     */
    @org.junit.Test
    public void test3() throws Exception{
        final URL[] urls1 = {new URL("file", "foo", 1111, "foo"), new URL("file", "bar", 1111, "bar"),new URL("file", "baz", 1111, "baz")};
        final List<URL> urlsList2 = Arrays.asList(urls1);
        Collections.reverse(urlsList2);
        final URL[] urls2 = urlsList2.toArray(new URL[urlsList2.size()]);

        final URLClassLoader urlClassLoader1 = new URLClassLoader(urls1, null);

        //TODO 1.构建扫描器
        ConfigurationBuilder reflectConfig = new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("reflect"))
                .setScanners(new SubTypesScanner(),
                        new TypeAnnotationsScanner(),
                        new MethodAnnotationsScanner(),
                        new MemberUsageScanner(),
                        new MethodParameterNamesScanner(),
                        new FieldAnnotationsScanner(),
                        new MethodParameterScanner());//.addClassLoader(urlClassLoader1);从网络加载

        //TODO 2.配置过滤器
        ConfigurationBuilder configurationBuilder = reflectConfig.filterInputsBy(new FilterBuilder().excludePackage("reflect.modle").excludePackage(OtoServiceImpl.class).excludePackage(OtoService.class));

        Reflections reflections = new Reflections(configurationBuilder);

        //TODO 方法扫描 new MethodAnnotationsScanner()
        Set<Method> resources =
                reflections.getMethodsAnnotatedWith(ApiMapping.class);
        resources.forEach(x -> {
            //TODO 查找方法参数名 new MethodParameterNamesScanner()
            List<String> methodParamNames = reflections.getMethodParamNames(x);
            methodParamNames.forEach(xx -> {
                System.out.println("方法:" + x.getName() + "参数名:" + xx.intern());
            });
        });

        //TODO 字段扫描 - FieldAnnotationsScanner()
        Set<Field> fieldsAnnotatedWith = reflections.getFieldsAnnotatedWith(Ignore.class);
        fieldsAnnotatedWith.forEach(x -> {
            System.out.println(x.getType()+":"+x.getName()
            );
        });

        //TODO 参数类型查找 MethodParameterScanner
        Set<Method> someMethods =
                reflections.getMethodsMatchParams(String.class, Integer.class);
        someMethods.forEach(x->{
            //TODO new MemberUsageScanner
            Set<Member> methodUsage = reflections.getMethodUsage(x);
            methodUsage.forEach(xx->{
                System.out.println(xx.getDeclaringClass());
            });
            System.out.println("参数类型查询"+x.getName());
        });

        //TODO 按照返回值查询
        Set<Method> voidMethods =
                reflections.getMethodsReturn(void.class);
        voidMethods.forEach(x->{
            System.out.println("返回值查询"+x.getName());
        });

        //TODO 按照参数查询
        Set<Method> pathParamMethods =
                reflections.getMethodsWithAnyParamAnnotated(ParamKey.class);
        pathParamMethods.forEach(x->{
            System.out.println("方法中注解:"+x.getName());
        });



    }


}
