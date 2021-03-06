package mappingdocumentation;

import java.io.IOException;

/**
 *
 * @author dmitryrassakhatsky
 */
public class MessageNum {

    public String getMessageNumber(String source) throws IOException {
        String messageNum;
        int messages_end, message_start, end_of_number;

        messages_end = source.indexOf("Messages/");
        message_start = source.indexOf("Message", messages_end + 1);
        end_of_number = source.indexOf("/", message_start + 1);
        
        if (message_start != -1) {
            messageNum = source.substring(message_start + "Message".length(), end_of_number);
        } else {
            messageNum = "0";
        }
        return messageNum;
    }
}
