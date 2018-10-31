package org.voltdb;

import java.util.function.Function;
import java.util.function.Supplier;

public class JavaProtectedTry<Exn extends Exception> {
    public interface ThrowableRunnable<E extends Exception> {
        void run() throws E;
    }

    public interface ThrowableSupplier<E extends Exception, R> {
        R run() throws E;
    }

    public interface Catcher<E extends Exception> {
        void run(Exception arg) throws E;
    }
    public interface SafeCatcher {
        void run(Exception e);
    }
    public interface ReturnedCatcher<E extends Exception, R> {
        R run(Exception arg) throws E;
    }
    public interface SafeReturnedCatcher<R> {
        R run(Exception arg);
    }
    public interface ExceptionMapper<S extends Exception, D extends Exception> {
        D convert(S src);
    }

    public JavaProtectedTry() {}

    public void tryWith(ThrowableRunnable<Exn> func, Runnable finalizer) throws Exn {
        try {
            func.run();
        } finally {
            finalizer.run();
        }
    }
    public void tryWith(ThrowableRunnable<Exn> func, Catcher<Exn> catcher, Runnable finalizer) throws Exn {
        try {
            func.run();
        } catch (Exception e) {
            catcher.run(e);
        } finally {
            finalizer.run();
        }
    }
    public void tryWith(ThrowableRunnable<Exn> func, Catcher<Exn> catcher) throws Exn {
        try {
            func.run();
        } catch (Exception e) {
            catcher.run(e);
        }
    }

    public void tryWith(ThrowableRunnable<Exception> func, ExceptionMapper<Exception, Exn> mapper) throws Exn {
        try {
            func.run();
        } catch (Exception e) {
            throw mapper.convert(e);
        }
    }

    public <R> R tryWith(ThrowableSupplier<Exn, R> func, Runnable finalizer) throws Exn {
        try {
            return func.run();
        } finally {
            finalizer.run();
        }
    }
    public <R> R tryWith(ThrowableSupplier<Exn, R> func, SafeReturnedCatcher<R> catcher, Runnable finalizer) {
        try {
            return func.run();
        } catch (Exception e) {
            return catcher.run(e);
        } finally {
            finalizer.run();
        }
    }
    public <R> R safeTryWith(ThrowableSupplier<Exn, R> func, ReturnedCatcher<Exn, R> catcher, Runnable finalizer) throws Exn {
        try {
            return func.run();
        } catch (Exception e) {
            return catcher.run(e);
        } finally {
            finalizer.run();
        }
    }
    public <R> R tryWith(ThrowableSupplier<Exn, R> func, ReturnedCatcher<Exn, R> catcher) throws Exn {
        try {
            return func.run();
        } catch (Exception e) {
            return catcher.run(e);
        }
    }

    static public void safeTryWith(Runnable func, Runnable finalizer) {
        try {
            func.run();
        } finally {
            finalizer.run();
        }
    }
    public void safeTryWith(Runnable func, SafeCatcher catcher, Runnable finalizer) {
        try {
            func.run();
        } catch (Exception e) {
            catcher.run(e);
        } finally {
            finalizer.run();
        }
    }

    static public<R> R safeTryWith(Supplier<R> func, Runnable finalizer) {
        try {
            return func.get();
        } finally {
            finalizer.run();
        }
    }
    public <R> R safeTryWith(ThrowableSupplier<Exn, R> func, Function<Exception, R> catcher) {
        try {
            return func.run();
        } catch (Exception e) {
            return catcher.apply(e);
        }
    }
    static public <R> R genericSafeTryWith(ThrowableSupplier<Exception, R> func, Function<Exception, R> catcher) {
        try {
            return func.run();
        } catch (Exception e) {
            return catcher.apply(e);
        }
    }
    public <R> R failableGet(ThrowableSupplier<Exception, R> fun, ExceptionMapper<Exception, Exn> mapper) throws Exn {
        try {
            return fun.run();
        } catch (Exception e) {
            throw mapper.convert(e);
        }
    }
}
