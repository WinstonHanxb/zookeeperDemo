package basic;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;

import java.util.List;

/**
 * Created by 韩宪斌 on 2018/8/29.
 */
public class ZkClientDemo {
    public static void main(String[] args) {
        ZkClient zc = new ZkClient(Constants.SERVER, 10000, 10000, new SerializableSerializer());
        System.out.println("connection established!");
        zc.deleteRecursive("/test123");
        zc.delete("/test123");
       
        // create node
        TestClass tc = new TestClass();
        tc.setId(1);
        tc.setMessage("This is a test message.");
        String path = zc.create("/test123", tc, CreateMode.PERSISTENT);
        System.out.println("created path:" + path);
        
        // get data
        TestClass tc2 = zc.readData("/test123");
        System.out.println("get data:" + tc2.toString());
        
        // create child node
        TestClass tc3 = new TestClass();
        tc3.setId(2);
        tc3.setMessage("This is a test message,too.");
        String path2 = zc.create("/test123/testChild", tc3, CreateMode.PERSISTENT);
        System.out.println("created path:" + path2);
        
        // get child node
        List<String> cList = zc.getChildren("/test123");
        System.out.println(cList.toString());
        
        // write data
        TestClass tc4 = new TestClass();
        tc4.setId(1);
        tc4.setMessage("This is a test message,three.");
        zc.writeData("/test123",tc4);
        System.out.println("get new data:" + zc.readData("/test123"));
        
        
        // check node exists
        boolean e = zc.exists("/test123");
        boolean e2 = zc.exists("/random");
        System.out.println("/test123" + " exists:" + e);
        System.out.println("/random" + " exists:" + e2);
        
        // delete node
        boolean d1 = zc.deleteRecursive("/test123");
        boolean d2 = zc.delete("/test123");
        boolean d3 = zc.delete("/random");
        System.out.println("d1:" + d1);
        System.out.println("d2:" + d2);
        System.out.println("d3:" + d3);
        
        
        
    }
}
