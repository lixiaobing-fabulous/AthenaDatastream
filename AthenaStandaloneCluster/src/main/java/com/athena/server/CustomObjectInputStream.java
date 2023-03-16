package com.athena.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/15
 * @Version 1.0
 **/
public class CustomObjectInputStream extends ObjectInputStream {

    protected ClassLoader classLoader = this.getClass().getClassLoader();

    /**
     * @param in
     * @throws IOException
     */
    public CustomObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    public CustomObjectInputStream(InputStream in, ClassLoader cl)
            throws IOException {
        super(in);
        this.classLoader = cl;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.io.ObjectInputStream#resolveClass(java.io.ObjectStreamClass)
     */
    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException,
            ClassNotFoundException {
        String name = desc.getName();
        try {
            return Class.forName(name, false, this.classLoader);
        } catch (ClassNotFoundException ex) {
            return super.resolveClass(desc);
        }
    }

}

