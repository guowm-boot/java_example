package com.solaris.guava;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class MyHdfs {
    private static org.apache.hadoop.conf.Configuration configuration;
    private static FileSystem staticFs;
    public static void main(String[] args) throws Exception {
        configuration = new Configuration();
        staticFs = FileSystem.newInstance(configuration);
        String[] strArr=new String[2];
        strArr[0]="aaaaaaaa";
        strArr[1] = "bbbbbbbbbbb";
        appendWithCompress("org.apache.hadoop.io.compress.GzipCodec",strArr,
                "hdfs://hd-n2:8020//tmp/solaris.gz");
    }
    public static void appendWithCompress(String codecClassStr, String[] strArr, String hdfsPath) throws IOException, ClassNotFoundException {
        OutputStream out = null;
        CompressionOutputStream cout = null;
        try {
            //利用反射机制通过类名获取类对象
            Class<?> codecClass = Class.forName(codecClassStr);
            //重点来了。我们通过Hadoop提供的ReflectionUtils工具类中的newInstance来获取指定的压缩格式类。这里返回的是该类的泛型，因此需要强转一下。
            CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, configuration);
            configuration.setBoolean("dfs.support.append", true);

            //打开追加流，将我们要输出的文件路径对象放入输出流中
            staticFs = FileSystem.get(URI.create(hdfsPath), configuration);
            //out = staticFs.append(new Path(hdfsPath));

            //将输出流加工一下，转换为“压缩过后”的输出流
            cout = codec.createOutputStream(out);
            for (String str : strArr) {
                String writeStr = str + "\n";
                cout.write(writeStr.getBytes());
            }
        } finally {
            IOUtils.closeStream(cout);
            IOUtils.closeStream(out);
        }
    }

}
