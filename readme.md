## 一、构建扫描器

### 1.1 扫描器类型

| 扫描器类型                               | 解释       |
| ----------------------------------- | -------- |
| `new SubTypesScanner()`             | 子类扫描     |
| `new TypeAnnotationsScanner() `     | 注解扫描     |
| `new MethodAnnotationsScanner()`    | 方法注解扫描   |
| `new FieldAnnotationsScanner()`     | 字段注解扫描   |
| `new MethodParameterNamesScanner()` | 方法参数名称扫描 |
| `new MethodParameterScanner()`      | 参数类型扫描   |
| `new MemberUsageScanner`            | 基本信息扫描   |



###  1.2 new SubTypesScanner()子类扫描

```java
 Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("reflect.modle"))
                .setScanners(new SubTypesScanner())
                .filterInputsBy(new FilterBuilder().includePackage("reflect.modle")));
        Set<Class<? extends OtoService>> subTypesOf = reflections.getSubTypesOf(OtoService.class);
```



### 1.3 new TypeAnnotationsScanner()注解扫描

```

```

### 1.4 new MethodAnnotationsScanner()方法注解扫描

```java
 Set<Method> resources =
                reflections.getMethodsAnnotatedWith(ApiMapping.class);
```

### 1.5 new FieldAnnotationsScanner()字段注解扫描

```java
 Set<Field> fieldsAnnotatedWith = reflections.getFieldsAnnotatedWith(Ignore.class);
```



### 1.6 new MethodParameterNamesScanner()方法参数名称扫描

```java
List<String> methodParamNames = reflections.getMethodParamNames(Method method);
```



### 1.7 new MethodParameterScanner()参数类型扫描

```java
Set<Method> someMethods =
 reflections.getMethodsMatchParams(String.class, Integer.class);
```



### 1.8 new MemberUsageScanner基本信息扫描

```java
Set<Member> methodUsage = reflections.getMethodUsage(x);
            methodUsage.forEach(xx->{
                System.out.println(xx.getDeclaringClass());
            });
```



## 二、构建资源路径

### 2.1过滤器

```
**包含**
FilterBuilder filter = new FilterBuilder().includePackage("org.reflections", "org.foo");

FilterBuilder filter = new FilterBuilder().includePackage(Reflections.class);

FilterBuilder filter = new FilterBuilder().include("org\\.reflections.*");

**排除**
 FilterBuilder filter = new FilterBuilder().excludePackage("org.reflections");
 
**解释器**
+:开头包含
-:不包含
FilterBuilder filter = FilterBuilder.parsePackages("+org.reflections, -org.reflections.foo");
```

### 2.2 网络加载

```java
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
                        new MethodParameterScanner()).addClassLoader(urlClassLoader1);

```

### 2.3 ClasspathHelper 工具





## 三、精确分析

### 3.1 查询条件

| 方法                                       | 解析            |
| ---------------------------------------- | ------------- |
| withName("getOrderInfo")                 | 指定方法          |
| withModifier(Modifier.PUBLIC)            | 指定修饰符         |
| withParametersCount(1)                   | 指定参数个数        |
| withPrefix("")                           | 指定前缀          |
| withReturnType(OrderDo.class)            | 指定返回值         |
| withParameters(String.class,Integer.class) | 指定参数类型        |
| withParametersAssignableTo(Collection.class) | 指定参数类型        |
| withTypeAssignableTo(int.class)          | 限制参数类型(对字段有限) |
| withAnyParameterAnnotation(TestModel.AM1.class) | 方法参数中注解       |
|                                          |               |
| withName()                               | 指定方法名         |
|                                          |               |
|                                          |               |
|                                          |               |

```java
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
```

### 3.2获取方法注解

```java
 Set<Method> getOrderInfo = getMethods(OtoServiceImpl.class, withReturnType(OrderDo.class), withName("getOrderInfo"));
        getOrderInfo.forEach(x->{
            ApiMapping annotation = x.getAnnotation(ApiMapping.class);
            System.out.println(annotation);//@reflect.modle.ApiMapping(value=orderInfo)
        });
```

### 3.3注解定义

[^Documented 注解]: Documented 注解表明这个注解应该被 javadoc工具记录. 默认情况下,javadoc是不包括注解的. 但如果声明注解时指定了 @Documented,则它会被 javadoc 之类的工具处理, 所以注解类型信息也会被包括在生成的文档中
[^Inherited 注解]: 它指明被注解的类会自动继承. 更具体地说,如果定义注解时使用了 @Inherited 标记,然后用定义的注解来标注另一个父类, 父类又有一个子类(subclass),则父类的所有属性将被继承到它的子类中
[^Target注解]: 注解的作用目标

- @Target(ElementType.TYPE)   //接口、类、枚举、注解
- @Target(ElementType.FIELD) //字段、枚举的常量
- @Target(ElementType.METHOD) //方法
- @Target(ElementType.PARAMETER) //方法参数
- @Target(ElementType.CONSTRUCTOR)  //构造函数
- @Target(ElementType.LOCAL_VARIABLE)//局部变量
- @Target(ElementType.ANNOTATION_TYPE)//注解
- @Target(ElementType.PACKAGE) ///包   

[^Retention注解]: Retention(保留)注解说明,这种类型的注解会被保留到那个阶段

- `1.RetentionPolicy.SOURCE —— 这种类型的Annotations只在源代码级别保留,编译时就会被忽略`
- `2.RetentionPolicy.CLASS —— 这种类型的Annotations编译时被保留,在class文件中存在,但JVM将会忽略`
- `3.RetentionPolicy.RUNTIME —— 这种类型的Annotations将被JVM保留,所以他们能在运行时被JVM或其他使用反射机制的代码所读取和使用.`