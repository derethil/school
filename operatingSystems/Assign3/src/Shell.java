// https://stackoverflow.com/questions/4480334/how-to-call-a-method-stored-in-a-hashmap-java
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Commands {
    public void ptime() {
        System.out.println("ptime");
    }

    public void history() {
        System.out.println("history");
    }

    public void execHistory(int commandIndex) {
        System.out.println("another history");
    }

    public void list() {
        System.out.println("list");
    }

    public void cd() {
        System.out.println("cd");
    }

    public void mdir() {
        System.out.println("mdir");
    }

    public void rdir() {
        System.out.println("rdir");
    }

    public void external(String command) {

    }
}
