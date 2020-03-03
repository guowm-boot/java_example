package com.solaris.javatest;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import org.junit.Test;
import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.zip.*;

public class ZipTest {
    @Test
    public void testDeflater() {
        // Encode a String into bytes
        String inputString = "blahblahblahblahblah" +
                "blahblahblahblahblahblahblahblahblahblahblahblah" +
                "blahblahblafhblahblahblahblxahblahbladhblahblahblah" +
                "blahblahblahblahblahblafhblahblahblahblahblahblah" +
                "blahblahblahdblahbxxxblahblahblahblahblah" +
                "blahblahblahblahblahblxxxhblahblahblahblahblah" +
                "blahblahblahblahblahblahblahblahblahdblahblahblah" +
                "blahblahblahblahblahbldfblahblahblahblahblsahblah" +
                "blahblahblah";
        byte[] input = inputString.getBytes(StandardCharsets.UTF_8);
        byte[] dic = "blah".getBytes(StandardCharsets.UTF_8);

        // Compress the bytes
        byte[] output = new byte[100];
        Deflater compresser = new Deflater(Deflater.BEST_COMPRESSION, true);//不含zlib的header和checksum
        compresser.setDictionary(dic);
        compresser.setStrategy(Deflater.HUFFMAN_ONLY);//设置deflate的压缩算法
        compresser.setLevel(Deflater.DEFAULT_COMPRESSION);

        ByteBuffer.allocateDirect(5).put(input,0,input.length);


//            ByteArrayOutputStream
//
//            // Decompress the bytes
//            Inflater decompresser = new Inflater(true);
//            decompresser.setDictionary(dic);
//            decompresser.setInput(output, 0, compressedDataLength);
//            byte[] result = new byte[100];
//            int resultLength = decompresser.inflate(result);
//            decompresser.end();
//
//            // Decode the bytes into a String
//            String outputString = new String(result, 0, resultLength, "UTF-8");
//            System.out.println("compress size:" + compressedDataLength + " decompress size:" + inputString.length());
//            System.out.println("decompress content:" + outputString);
    }
}
