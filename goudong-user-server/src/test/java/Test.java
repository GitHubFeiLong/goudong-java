import lombok.Data;

/**
 * @Author msi
 * @Date 2021-05-11 13:58
 * @Version 1.0
 */
@Data
public class Test {
    private String name;

    public Test(String name) {
        this.name = name;
    }
}
