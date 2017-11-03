package gui.demo.com.demoapplication.dto;

import java.io.Serializable;

/**
 * Created by 006283 on 2/11/2560.
 */

public class Greeting implements Serializable {

    private String id;
    private String content;

    public String getId() {
        return this.id;
    }

    public String getContent() {
        return this.content;
    }
}
