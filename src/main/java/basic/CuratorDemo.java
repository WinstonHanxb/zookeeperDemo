package basic;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 韩宪斌 on 2018/8/29.
 */
public class CuratorDemo {
    public static void main(String[] args) throws Exception {
        // create client
        RetryPolicy rp = new RetryUntilElapsed(5000, 1000);
        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                .connectString(Constants.SERVER)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(rp)
                .build();
        client.start();
        System.out.println(client.getState());
        
        String path = "/test/1";
        
        //create node
        createNode(client, path, "this is a test message.".getBytes());
        Thread.sleep(5000);
        
        
        //delete node
        deleteNode(client, path);
        Thread.sleep(5000);
    
    
        //get data
        createNode(client, path, "this is a test message too.".getBytes());
        Thread.sleep(5000);
        getData(client, path);
        
    
    
        //update data
        updateData(client, path, "this is a test message three.".getBytes());
        Thread.sleep(5000);
        getData(client, path);
    
        //check exists
        checkExists(client, path);
    
    
        client.close();
        
    }
    
    public static void checkExists(CuratorFramework client, String path) throws Exception {
        ExecutorService es = Executors.newFixedThreadPool(5);
        client.checkExists().inBackground((client1, event) -> {
            Stat stat = event.getStat();
            System.out.println("stat:" + stat);
            System.out.println("context:" + event.getContext());
        }, "this is a test message three.", es).forPath(path);
        
    }
    
    public static void updateData(CuratorFramework client, String path, byte[] bytes) {
        new Thread(() -> {
            Stat stat = new Stat();
            try {
                Thread.sleep(2000);
                client.setData().withVersion(stat.getVersion()).forPath(path, bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    public static void getData(CuratorFramework client, String path) throws Exception {
        Stat stat = new Stat();
        byte[] ret = client.getData().storingStatIn(stat).forPath(path);
        System.out.println(new String(ret));
        System.out.println("stat:" + stat);
    }
    
    public static void deleteNode(CuratorFramework client, String path) {
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                client.delete()
                        /**
                         * guaranteed方法会在client保持open的过程中不断重试直到删除成功
                         * 当删除有问题时任然会接收到异常
                         */
                        .guaranteed()
                        .deletingChildrenIfNeeded()
                        .forPath(path);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    public static void createNode(CuratorFramework client, String path, byte[] data) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(path, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }).start();
        
    }
}
