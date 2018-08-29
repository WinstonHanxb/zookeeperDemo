package basic;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.RetryUntilElapsed;

/**
 * Created by 韩宪斌 on 2018/8/29.
 */
public class CuratorDemoNodeWatch {
    public static void main(String[] args) {
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
        
        
        CuratorDemo.createNode(client, path, "this is a test message".getBytes());
        
        final NodeCache cache = new NodeCache(client, path);
        cache.getListenable().addListener(() -> {
            byte[] ret = cache.getCurrentData().getData();
            System.out.println("data changed:" + new String(ret));
        });
        try {
            cache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        
        
        CuratorDemo.updateData(client, path, "this is a test message,too".getBytes());
    
        try {
            // 休眠是为了监控到所有变化
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
