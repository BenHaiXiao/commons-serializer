package com.github.benhaixiao.commons.serializer.performance;

import com.github.benhaixiao.commons.serializer.fst.FstUtils;
import com.github.benhaixiao.commons.serializer.kryo.KryoPoolFactoryImpl;
import com.github.benhaixiao.commons.serializer.kryo.KryoUtils;
import com.github.benhaixiao.commons.serializer.test.EntLiveInfo;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaobenhai
 */
public class PerformanceTest {
	private static KryoUtils kryoUtils = new KryoUtils();

	@Before
    public void before(){
        kryoUtils.setKryoPool(new KryoPoolFactoryImpl());
    }

    @Test
    public  void testMapDeserialize() {
        //body
        Map map = new HashMap();
        map.put("threadname", Thread.currentThread().getName());
        map.put("threadname2", new EntLiveInfo());
        map.put("i", 1);

        //Kryo
        byte[] bsKryo = kryoUtils.kryoSerialize(map);
        System.out.println(new String(bsKryo));
        Long tKryo = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            Object oKryo = kryoUtils.kryoDeserialize(bsKryo);
        }
        System.out.println("Kryo,反序列化Map对象，100万次，总耗时：" + (System.currentTimeMillis() - tKryo) + "ms");
        System.out.println("Kryo,pool create size : " + kryoUtils.getCreateCount());

        //Fst
        byte[] bsFst = FstUtils.fastSerialize(map);
        Long tFst = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            Object oFst = FstUtils.fastDeserialize(bsFst);
        }
        System.out.println("Fst ,反序列化Map对象，100万次，总耗时：" + (System.currentTimeMillis() - tFst) + "ms");

        //Jvm
        byte[] bsJvm = SerializationUtils.serialize((Serializable) map);
        Long tJvm = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            Object oJvm = SerializationUtils.deserialize(bsJvm);
        }
        System.out.println("Jvm ,反序列化Map对象，100万次，总耗时：" + (System.currentTimeMillis() - tJvm) + "ms");


    }

    @Test
    public  void testMapSerialize() {
        Map map = new HashMap();
        map.put("threadname", Thread.currentThread().getName());
        map.put("threadname2", new EntLiveInfo());
        //Kryo
        Long tKryo = System.currentTimeMillis();
        byte[] bsKryo = null;
        for (int i = 0; i < 1000000; i++) {
            map.put("b", i);
            bsKryo = kryoUtils.kryoSerialize(map);
        }
        System.out.println("Kryo,序列化Map对象，100万次，总耗时：" + (System.currentTimeMillis() - tKryo) + "ms  序列化对象后大小：" + bytes2kb(bsKryo.length * 1000000));
        System.out.println("Kryo,pool create size : " + kryoUtils.getCreateCount());

        Long tFst = System.currentTimeMillis();
        byte[] bsFst = null;
        for (int i = 0; i < 1000000; i++) {
            map.put("b", i);
            bsFst = FstUtils.fastSerialize(map);
        }
        System.out.println("Fst ,序列化Map对象，100万次，总耗时：" + (System.currentTimeMillis() - tFst) + "ms  序列化对象后大小：" + bytes2kb(bsFst.length * 1000000));

        //Jvm
        Long tJvm = System.currentTimeMillis();
        byte[] bsJvm = null;
        for (int i = 0; i < 1000000; i++) {
            map.put("b", i);
            bsJvm = SerializationUtils.serialize((Serializable) map);
        }
        System.out.println("Jvm ,序列化Map对象，100万次，总耗时：" + (System.currentTimeMillis() - tJvm) + "ms  序列化对象后大小：" + bytes2kb(bsJvm.length * 1000000));

    }


    private static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
        if (returnValue > 1)
            return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();
        return (returnValue + "KB");
    }
}
