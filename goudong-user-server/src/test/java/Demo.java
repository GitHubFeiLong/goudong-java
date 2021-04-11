import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;

public class Demo {
    public static void main(String[] args) {
//        String hashpw = BCrypt.hashpw("123456", BCrypt.gensalt());
//        System.out.println("hashpw = " + hashpw);
//        LocalDateTime localDateTime = LocalDateTime.now();
//        localDateTime.plusHours(2);
//
//        System.out.println(" = " + localDateTime);
        
        String method = "get";
        System.out.println("HttpMethod.resolve(method) = " + HttpMethod.resolve(method));
    }
}
