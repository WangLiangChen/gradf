package liangchen.wang.gradf.framework.commons.exception;

import java.sql.SQLException;

/**
 * @author LiangChen.Wang
 */
public enum ExceptionUtil {
    /**
     * instance
     */
    INSTANCE;

    public String parseMessage(Throwable throwable) {
        if (throwable instanceof GradfException) {
            return throwable.getMessage();
        }
        String message;
        if (throwable instanceof ArithmeticException) {
            message = "错误01：运算条件异常";
        } else if (throwable instanceof ArrayIndexOutOfBoundsException) {
            message = "错误02：数组索引越界";
        } else if (throwable instanceof ArrayStoreException) {
            message = "错误03：试图将错误类型的对象存储到一个对象数组";
        } else if (throwable instanceof ClassCastException) {
            message = "错误04：类型强制转换异常";
        } else if (throwable instanceof ClassNotFoundException) {
            message = "错误05：引用不存在的类";
        } else if (throwable instanceof CloneNotSupportedException) {
            message = "错误06：对象复制异常，该对象的类无法实现 Cloneable 接口";
        } else if (throwable instanceof EnumConstantNotPresentException) {
            message = "错误07：枚举常量名称不存在";
        } else if (throwable instanceof IllegalAccessException) {
            message = "错误08：使用反射时，无法访问指定类、字段、方法或构造方法";
        } else if (throwable instanceof IllegalArgumentException) {
            message = "错误09：不合法或不正确的方法参数；线程没有处于请求操作所要求的适当状态；字符串转换数值异常；";
        } else if (throwable instanceof IllegalMonitorStateException) {
            message = "错误10：线程监视器异常";
        } else if (throwable instanceof IllegalStateException) {
            message = "错误11：非法或不适当的时间调用方法";
        } else if (throwable instanceof IndexOutOfBoundsException) {
            message = "错误12：索引越界";
        } else if (throwable instanceof InstantiationException) {
            message = "错误13：newInstance 方法无法实例化指定类";
        } else if (throwable instanceof InterruptedException) {
            message = "错误14：正常线程被中断";
        } else if (throwable instanceof NegativeArraySizeException) {
            message = "错误15：数组大小不能为负数";
        } else if (throwable instanceof NoSuchFieldException) {
            message = "错误16：类不包含指定名称的字段";
        } else if (throwable instanceof NoSuchMethodException) {
            message = "错误17：无法找到某方法:";
        } else if (throwable instanceof NullPointerException) {
            message = "错误18：使用值为NULL的对象";
        } else if (throwable instanceof TypeNotPresentException) {
            message = "错误19：使用字符串访问类型时产生错误";
        } else if (throwable instanceof UnsupportedOperationException) {
            message = "错误20：不支持的请求";
        } else if (throwable instanceof AbstractMethodError) {
            message = "错误21：抽象方法不能被调用";
        } else if (throwable instanceof AssertionError) {
            message = "错误22：断言失败";
        } else if (throwable instanceof ClassCircularityError) {
            message = "错误23：类初始化时发现循环调用";
        } else if (throwable instanceof ClassFormatError) {
            message = "错误24：虚拟机无法读取文件，格式错误或者不能解释为类文件";
        } else if (throwable instanceof ExceptionInInitializerError) {
            message = "错误25：静态初始化程序中发生意外异常";
        } else if (throwable instanceof IllegalAccessError) {
            message = "错误26：应用程序试图访问或修改它不能访问的字段，或调用它不能访问的方法";
        } else if (throwable instanceof IncompatibleClassChangeError) {
            message = "错误27：不兼容的类更改；应用程序试图使用 Java 的 new 结构来实例化一个抽象类或一个接口；";
        } else if (throwable instanceof InternalError) {
            message = "错误28：虚拟机内部错误";
        } else if (throwable instanceof LinkageError) {
            message = "错误29：相依赖的类发生不相容的改变；Java虚拟机或 ClassLoader无法找到该类的定义；试图访问或修改不存在的字段或方法；版本不支持；";
        } else if (throwable instanceof OutOfMemoryError) {
            message = "错误30：内存溢出或没有可用的内存";
        } else if (throwable instanceof StackOverflowError) {
            message = "错误31：递归太深，堆栈溢出";
        } else if (throwable instanceof ThreadDeath) {
            message = "错误32：带有零个参数的stop方法";
        } else if (throwable instanceof UnknownError) {
            message = "错误33：虚拟机发生未知严重错误";
        } else if (throwable instanceof VirtualMachineError) {
            message = "错误34：Java 虚拟机崩溃或用尽了它继续操作所需的资源";
        } else if (throwable instanceof SQLException) {
            message = "错误35：SQL异常";
        } else if (throwable instanceof RuntimeException) {
            message = "错误36：运行时异常";
        } else if (throwable instanceof Exception) {
            message = "错误37：程序异常Exception,";
        } else if (throwable instanceof Error) {
            message = "错误38：程序错误Error";
        } else {
            message = "错误40：其它异常";
        }
        return String.format("%s,%s", message, throwable.getMessage());
    }
}
