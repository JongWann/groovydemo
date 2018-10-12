import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.Eval;
import groovy.util.GroovyScriptEngine;
import java.util.concurrent.TimeUnit;

/**
 *  在Java程序中执行Groovy脚本
 */
public class ExcuteGroovyAtJava {

    public static void main(String[] args) throws Exception {
        /**groovy.util.Eval 类是最简单的用来在运行时动态执行
         * Groovy 代码的类，提供了几个静态工厂方法供使用，内部其实就是对GroovyShell的封装。
         */
        // 直接调用groovy脚本
        Eval.me("println 'hello world'");

        // 绑定变量
        Object result = Eval.me("age",22,"if(age > 18) '已经成年'");
        System.out.println(result.toString());

        //绑定一个名为 x 的参数的简单计算
        Eval.x(4, "println 2*x");
        //绑定两个参数进行计算
        Eval.xy(2,4,"println x*y");
        //绑定三个参数进行计算
        Eval.xyz(2,4,8,"println x*y*z");

        /**
         * groovy.lang.GroovyShell除了可以执行
         * Groovy 代码外，提供了更丰富的功能，比如可以绑定更多的变量，从文件系统、网络加载代码等。
         */
        GroovyShell shell = new GroovyShell();
        shell.setVariable("age", 16);
        //立即执行
        shell.evaluate("if ( age > 18) println '已成年' else println '未成年'");

        //解析为脚本,延后执行
        Script script = shell.parse("if ( age > 18) println '已成年' else println '未成年'");
        script.run();

        /**
         * groovy.lang.GroovyClassLoader是一个定制的类加载器，可以在运行时加载 Groovy 代码，生成 Class 对象。
         */
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        String scriptText = "class Hello { void hello() { println 'hello' } }";
        //将Groovy脚本解析为Class对象
        Class clazz = groovyClassLoader.parseClass(scriptText);
        //Class clazz = groovyClassLoader.parseClass(new File("script.groovy"));
        clazz.getMethod("hello").invoke(clazz.newInstance());

        /**
         * groovy.util.GroovyScriptEngine能够处理任何 Groovy 代码的动态编译与加载，
         * 可以从统一的位置加载脚本，并且能够监听脚本的变化，当脚本发生变化时会重新加载。
         */
        GroovyScriptEngine scriptEngine = new GroovyScriptEngine("script");
        Binding binding = new Binding();
        binding.setVariable("name", "groovy");
        while (true){
            scriptEngine.run("GroovyScript.groovy", binding);
            TimeUnit.SECONDS.sleep(1);
        }
        
        
        
        
    }
}
