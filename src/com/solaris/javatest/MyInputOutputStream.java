package com.solaris.javatest;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

//input/output iostream
/*
    file stream /fileStream
    IO流，文件流
 */
public class MyInputOutputStream {
    static String path = "D:\\tmp\\excel_test\\employee.txt";
    static String seriPath = "D:\\tmp\\excel_test\\seri.txt";

    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("user.dir"));//工作目录
        String xx = System.getProperty("line.separator");//行分隔符
        //testFileStream();
        testGzip();//gzip
        toBinFile();//二进制方式

        simpleReadFile();//简单的读文件,用于小的text文件处理
        simpleWriteFile();//简单的写文件,用于小的text文件处理
        bigOrBinFile();//大文件 or bin文件
        accessObject();//读写对象
        testPath();//Path类 相对路径/绝对路径

        //createFile();//创建文件

    }

    private static void bigOrBinFile() {
        try {
            Charset cs = StandardCharsets.UTF_8;
            InputStream in = Files.newInputStream(Paths.get(path));
            OutputStream out = Files.newOutputStream(Paths.get(path));
            BufferedReader inReader = Files.newBufferedReader(Paths.get(path), cs);
            BufferedWriter outWriter = Files.newBufferedWriter(Paths.get(path), cs);
        } catch (Exception e) {
        }
    }

    public static void testPath() {
        // \home\aaa\harry
        Path p = Paths.get("/home/aaa", "\\harry");
        //resolve-一般用于输入不知道是否是相对或绝对的路径，返回绝对路径
        Path absolute = p.resolve("d:/tmp");// d:\tmp
        Path absolute1 = p.resolve("a/b/c/d");// \home\aaa\harry\a\b\c\d

        //如果输入的是相对路径，则join到left的父目录上
        Path xx = p.resolveSibling("a/b/c");//\home\aaa\a\b\c

        //解析成为相对目录
        Path z = p.relativize(Paths.get("/home/b/c"));//-> ../../b/c

        //去掉路径中冗余的值，如..和.  -> /home/user
        Path normalPath = Paths.get("/home/user/.././user").normalize();

        //将相对路径变成绝对路径，依据是当前工作目录
        Path absolutePath = z.toAbsolutePath();//D:\文档\开发\test\git\java-test\..\..\b\c

        //变成文件对象,但是文件并没有真正打开
        File f = p.toFile();
        System.out.println("is file exist:" + f.exists());
    }

    private static void accessObject() throws Exception {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(seriPath));
        LocalDateTime dt = LocalDateTime.now();
        LocalTime t = LocalTime.now();
        out.writeObject(String.valueOf("harry"));
        out.writeObject(dt);
        out.writeObject(t);
        out.close();
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(seriPath));
        String e0 = (String) in.readObject();
        LocalDateTime e1 = (LocalDateTime) in.readObject();
        LocalTime e2 = (LocalTime) in.readObject();
        System.out.println(e0 + "&&" + e1 + "&&" + e2);
        in.close();
    }

    private static void toBinFile() {
        DataOutputStream x;
    }

    private static void simpleReadFile() {
        Path tmpPath = Paths.get(path);
        Charset charset = StandardCharsets.UTF_8;
        try {
            // 1 读取所有到字符串
            String content = new String(Files.readAllBytes(tmpPath), charset);

            //2 读取所有行到字符串list
            List<String> lines = Files.readAllLines(tmpPath, charset);

            //3 适合读大文件，延迟加载到内存
            try (Stream<String> lineStream = Files.lines(tmpPath, charset)) {
            }

            //4 bufferedReader
            try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
                //or 方法①
                Stream<String> tmpStream = in.lines();
                long size0 = tmpStream.mapToInt(s -> s.length()).sum();
                //or 方法②
                String line;
                int fileCharNum = 0;
                while ((line = in.readLine()) != null) {
                    fileCharNum += line.length();
                }
                System.out.println("file size:" + size0 + " size1:" + fileCharNum);
            }

        } catch (Exception e) {
            System.out.println("ex:" + e);
        }
    }

    private static void simpleWriteFile() {
        String content = "simpleWriteFile test";
        Charset cs = StandardCharsets.UTF_8;
        try {
            //1 源是String
            Files.write(Paths.get(path), content.getBytes(cs), StandardOpenOption.APPEND, StandardOpenOption.CREATE);

            //2 源是容器
            List<String> contentArray = IntStream.range(1, 10).mapToObj(String::valueOf).collect(Collectors.toList());
            Files.write(Paths.get(path), contentArray, cs, StandardOpenOption.APPEND, StandardOpenOption.CREATE);

        } catch (Exception e) {
            System.out.println("simpleWriteFile err:" + e);
        }
    }

    private static void testGzip() throws Exception {
        GZIPInputStream zin = new GZIPInputStream(new FileInputStream("D:\\360极速浏览器下载\\20190801.gz"));
        DataInputStream din = new DataInputStream(zin);
        System.out.println(din.readUTF());
    }

    @Test
    public void randomAcessFile() throws Exception {
        RandomAccessFile inOut = new RandomAccessFile(path, "rw");
        System.out.println(inOut.getFilePointer());
        int line = 0;
        for (; line < 20; line++) {
            inOut.writeChars("line:" + line + "\n");
        }
        inOut.seek(0);
        for (; line < 25; line++) {
            inOut.writeChars("line:" + line + "\n");
        }
    }

    @Test
    public void zip() throws Exception {
        FileOutputStream fout = new FileOutputStream("out/test.zip");
        ZipOutputStream zout = new ZipOutputStream(fout);
        List<String> fileNames = Arrays.asList("1","2");
        String content="this is content";
        for (String filename :fileNames ){
            ZipEntry ze = new ZipEntry(filename);
            zout.putNextEntry(ze);
            zout.write(content.getBytes());
            //send data to zout ;
            zout.closeEntry();
        } zout.close();
    }

    @Test//文本方式（字符串，非二进制）
    public void toTextFile() throws Exception {
        // autoflush
        PrintWriter out = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream("D:\\tmp\\excel_test\\employee.txt"
                                , true)
                        , "UTF-8"
                ), true
        );
        out = new PrintWriter("D:\\tmp\\excel_test\\employee.txt", "UTF-8");
        out.println(1);
        out.print(false);
        out.print(2.0);
        out.printf(" this is %s%n", "solaris");
        out.close();
    }

    @Test
    public void testByteBuffer() {
        ByteBuffer buff ;
        try (FileChannel channel = FileChannel.open(Paths.get(seriPath))) {
            buff=channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            System.out.println("position:" + buff.position() + " limit:" + buff.limit());
            buff.flip();

            byte[] byteArray = new byte[100];
            buff.get(byteArray, 0, Math.min(byteArray.length, buff.remaining()));
            System.out.println("testByteBuffer1:" + byteArray);
            buff.rewind();
            byte[] byteArray1 = new byte[100];
            buff.get(byteArray1, 0, Math.min(byteArray1.length, buff.remaining()));
            System.out.println("testByteBuffer2:" + byteArray1 + " equals:" + Arrays.equals(byteArray, byteArray1));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createFile() {
        try {
            Path createDirectory = Paths.get("D:\\tmp\\test_directory");

            //中间目录必须存在，如不存在或最终目录已存在会抛出异常
            if (!Files.isDirectory(createDirectory))
                Files.createDirectory(createDirectory);

            //中间目录没有则创建,如果已存在也不抛出异常。★常用
            Files.createDirectories(createDirectory.resolve("1/2/3"));

            //创建一个文件，如果文件已存在或中间目录不存在，则抛出异常
            if (!Files.exists(createDirectory.resolve("1.txt"))) {
                Files.createFile(createDirectory.resolve("1.txt"));
            }

            //在系统的临时文件夹里创建文件
            Path tmpPath = Files.createTempFile(null, ".txt");
            Files.delete(tmpPath);
            System.out.println("tmpFile:" + tmpPath);
            Path dic = Files.createTempDirectory("solaris");
            Path tmpPath1 = Files.createTempFile(dic, "tt", ".txt");
            Files.delete(tmpPath1);
            System.out.println("tmpFile1:" + tmpPath1);

            //获取文件属性
            if (Files.exists(createDirectory.resolve("1.txt"))) {
                BasicFileAttributes attributes = Files.readAttributes(createDirectory.resolve("1.txt"), BasicFileAttributes.class);
                System.out.println(attributes.creationTime() + "|" + attributes.fileKey());
            }

            //stream
            try (Stream<Path> pathStream = Files.walk(Paths.get("C:\\Users\\guowm\\AppData\\Local\\Temp"))) {
                System.out.println(pathStream.limit(10).collect(Collectors.toList()));
            }

            //文件夹的stream
            try (DirectoryStream<Path> fStream = Files.newDirectoryStream(createDirectory, "**.{java,txt}")) {
                //获取到的其实是一个集合，并且可以使用通配符
                fStream.forEach(p -> System.out.println("newDirectoryStream:" + p));
            }

            //zip文件系统
            /*
            FileSystem fs = FileSystems.newFileSystem(Paths.get(zipname), null);
            Files.walkFileTree(fs.getPath("/"), new SimpleFileVisitor<Path>() public FileVisitResult visitFile (Path
            file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file);
                return FileVisitResult.CONTINUE;
            }*/
        } catch (Exception e) {
            System.out.println("createFile err:" + e);
        }
        ;
    }

    private void testFileStream() throws Exception {
        DataInputStream din = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream("employee.dat"))
        );
        din.readChar();
    }

    @Test
    public void testVmsData() throws Exception {
        byte[] oldFile = Files.readAllBytes(Paths.get("D:\\360极速浏览器下载\\gzcompara\\20200116.new.gz"));
        byte[] newFile = Files.readAllBytes(Paths.get("D:\\360极速浏览器下载\\gzcompara\\20200116.new2.gz"));
        int length = Math.min(oldFile.length, newFile.length);
        for (int i = 0; i < length; i++) {
            if (oldFile[i] != newFile[i])
                System.out.println("error");
        }

        System.out.println("end,size:" + length);
        byte[] decompressBuff = new byte[1024 * 1024 * 10];
        GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(oldFile));
        in.read(decompressBuff, 0, decompressBuff.length);

    }
}
