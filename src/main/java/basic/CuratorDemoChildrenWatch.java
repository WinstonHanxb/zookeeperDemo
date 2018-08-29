package basic;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.RetryUntilElapsed;

/**
 * Created by 韩宪斌 on 2018/8/29.
 */
public class CuratorDemoChildrenWatch {
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
        
        
        final PathChildrenCache cache = new PathChildrenCache(client, "/test", true);
        try {
            cache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        CuratorDemo.createNode(client, path, "this is a test message".getBytes());
        
        cache.getListenable().addListener((client1, event) -> {
            switch (event.getType()) {
                case CHILD_ADDED:
                    System.out.println("CHILD_ADDED:" + new String(event.getData().getData()));
                    break;
                case CHILD_UPDATED:
                    System.out.println("CHILD_UPDATED:" + new String(event.getData().getData()));
                    break;
                case CHILD_REMOVED:
                    System.out.println("CHILD_REMOVED:" + new String(event.getData().getData()));
                    break;
                default:
                    break;
            }
        });
        
        
        CuratorDemo.updateData(client, path, "this is a test message,too".getBytes());
        
        
        CuratorDemo.deleteNode(client, path);
    
    
        try {
            // 休眠是为了监控到所有变化
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }
}
