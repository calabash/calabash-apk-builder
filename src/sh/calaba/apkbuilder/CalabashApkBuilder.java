package sh.calaba.apkbuilder;

import java.io.*;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CalabashApkBuilder {

    public static void main(String[] args) throws Exception
    {
        File out = new File(args[0]);
        File zipFile = new File(args[1]);
        File dexFile = new File(args[2]);
        if (!zipFile.exists())
        {
            System.err.println("No such file: " + zipFile);
            System.exit(1);
        }
        if (!dexFile.exists())
        {
            System.err.println("No such file: " + dexFile);
            System.exit(1);
        }

        CalabashApkBuilder builder = new CalabashApkBuilder(out);
        builder.appZipFile(zipFile);
        builder.addDexFile(dexFile);
        builder.close();
    }

    private JarOutputStream output;

    public CalabashApkBuilder(File out) throws Exception
    {
        output = new JarOutputStream(new FileOutputStream(out));
    }

    void addDexFile(File file) throws Exception
    {
        addEntry(new FileInputStream(file), "classes.dex");
    }

    void appZipFile(File zipFile) throws Exception
    {
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
        try {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null)
            {
                addEntry(zis, entry.getName());
            }
        } finally {
            zis.close();
        }
    }

    void close() throws Exception
    {
        output.close();
    }

    void addEntry(InputStream i, String name) throws Exception
    {
        System.out.println("Adding '" + name + " to zip");
        output.putNextEntry(new JarEntry(name));
        int count;
        byte[] buffer = new byte[4096];
        while ((count = i.read(buffer)) != -1)
        {
            output.write(buffer, 0, count);
        }
        output.closeEntry();
    }
}
