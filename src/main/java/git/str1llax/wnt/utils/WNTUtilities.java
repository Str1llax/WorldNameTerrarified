package git.str1llax.wnt.utils;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class WNTUtilities {
    public static Field findField(@Nonnull Class<?> clazz, @Nonnull String fieldName, @Nonnull String obfFieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            try {
                Field field = clazz.getDeclaredField(obfFieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException ex) {
                throw new RuntimeException("Could not find field " + fieldName + " / " + obfFieldName + " in class " + clazz.getName(), ex);
            }
        }
    }

    public static Method findMethod(@Nonnull Class<?> clazz, @Nonnull String methodName, @Nonnull String obfMethodName, Class<?>... parameterTypes)
    {
        try {
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            try {
                Method method = clazz.getDeclaredMethod(obfMethodName, parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException("Could not find field " + methodName + " / " + obfMethodName + " in class " + clazz.getName(), ex);
            }
        }
    }
}
