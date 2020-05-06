package com.chenx.proxy;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyExample {

    public static void main(String[] args) throws Exception {

        //jdk
        BookApi delegate = new BookApiImpl();
        BookApi jdkProxy = createJdkDynamicProxy(delegate);
        jdkProxy.sell();//warm
        //cglib
        BookApi cglibProxy = createCglibDynamicProxy(delegate);
        cglibProxy.sell();//warm
        //javassist
        BookApi javassistBytecodeProxy = createJavassistBytecodeDynamicProxy();
        javassistBytecodeProxy.sell();//warm


        Class<?> serviceClass = delegate.getClass();
        String methodName = "sell";

        // 执行反射调用
        Method method = serviceClass.getMethod(methodName, new Class[]{});
        method.invoke(delegate, new Object[]{});
        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, new Class[]{});
        serviceFastMethod.invoke(delegate, new Object[]{});



    }

    private static BookApi createJdkDynamicProxy(final BookApi delegate) {
        BookApi jdkProxy = (BookApi) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                new Class[]{BookApi.class}, new JdkHandler(delegate));
        return jdkProxy;
    }

    private static class JdkHandler implements InvocationHandler {

        final Object delegate;

        JdkHandler(Object delegate) {
            this.delegate = delegate;
        }

        @Override
        public Object invoke(Object object, Method method, Object[] objects)
                throws Throwable {
            // 添加代理逻辑
            if (method.getName().equals("sell")) {
                System.out.print("");
            }
            return null;
//            return method.invoke(delegate, objects);
        }
    }

    private static BookApi createCglibDynamicProxy(final BookApi delegate) throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new CglibInterceptor(delegate));
        enhancer.setInterfaces(new Class[]{BookApi.class});
        BookApi cglibProxy = (BookApi) enhancer.create();
        return cglibProxy;
    }

    private static class CglibInterceptor implements MethodInterceptor {

        final Object delegate;

        CglibInterceptor(Object delegate) {
            this.delegate = delegate;
        }

        @Override
        public Object intercept(Object object, Method method, Object[] objects,
                                MethodProxy methodProxy) throws Throwable {
            // 添加代理逻辑
            if (method.getName().equals("sell")) {
                System.out.print("");
            }
            return null;
//            return methodProxy.invoke(delegate, objects);
        }
    }

    private static BookApi createJavassistBytecodeDynamicProxy() throws Exception {
        ClassPool mPool = new ClassPool(true);
        CtClass mCtc = mPool.makeClass(BookApi.class.getName() + "JavaassistProxy");
        mCtc.addInterface(mPool.get(BookApi.class.getName()));
        mCtc.addConstructor(CtNewConstructor.defaultConstructor(mCtc));
        mCtc.addMethod(CtNewMethod.make(
                "public void sell(){ System.out.print(\"\") ; }", mCtc));
        Class<?> pc = mCtc.toClass();
        BookApi bytecodeProxy = (BookApi) pc.newInstance();
        return bytecodeProxy;
    }

    private interface BookApi {
        void sell();
    }

    private static class BookApiImpl implements BookApi {
        @Override
        public void sell() {
            System.out.println("sell book...");
        }
    }
}