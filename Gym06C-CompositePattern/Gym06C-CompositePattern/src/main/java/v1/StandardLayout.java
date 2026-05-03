package v1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StandardLayout extends Layout{
    private final DateTimeFormatter _formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public String format(Logger logger ,String message) {
        /*
        時間 |-訊息分級 日誌器名稱 - 訊息內容
        時間格式為：yyyy-MM-dd HH:mm:ss.SSS
        */

        return String.format("%s |-%s - %s - %s",
                LocalDateTime.now().format(_formatter),
                logger.getThreshold(),
                logger.getName(),
                message);
    }
}
