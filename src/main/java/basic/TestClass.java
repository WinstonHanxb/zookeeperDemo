package basic;

import java.io.Serializable;

/**
 * Created by 韩宪斌 on 2018/8/29.
 * 用于测试的数据
 * 必须序列化
 */
public class TestClass implements Serializable {
    /** Serialization version. */
    private static final long serialVersionUID = -72L;
    private long id;
    private String message;
    
    @Override
    public String toString() {
        return "TestClass{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
