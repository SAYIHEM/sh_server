package tools;

/**
 * Created by Manuel on 22.08.2017.
 */
public class Log {
    private static final Log log_instance = new Log();

    private Log() {
    }

    public static void print(String message) {
        synchronized (log_instance) {
            log_instance.performPrint(message);
        }
    }
    private void performPrint(String message) {
        System.out.println(message);
    }
}