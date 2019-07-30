package netty.chatting.status;

import lombok.Data;

@Data
public class Session {

    private String userId;

    private String name;

    public Session(){}

    public Session(String userId, String name){
        this.userId = userId;
        this.name = name;
    }

}
