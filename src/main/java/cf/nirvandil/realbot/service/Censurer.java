package cf.nirvandil.realbot.service;

import org.telegram.telegrambots.api.objects.Message;

public interface Censurer {
    boolean hasBadWords(Message message);
    String clearMessage(Message message);
}
