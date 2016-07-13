package jp.vmi.selenium.selenese.inject;

import jp.vmi.selenium.selenese.result.Result;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public abstract class AbstractExecuteTestRunInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object[] args = invocation.getArguments();
        String[] filenames = (String[])args[0];
        return invoke(invocation, filenames);
    }

    protected abstract Result invoke(MethodInvocation invocation, String... filenames) throws Throwable;
}
