package cf.nirvandil.realbot.service.impl;

import cf.nirvandil.realbot.service.Censurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Message;

@Service
public class RegexCensurer implements Censurer {
    private final MatMatcher matMatcher;
    @Value("${bot.bip}")
    private String bip;

    @Autowired
    public RegexCensurer(MatMatcher matMatcher) {
        this.matMatcher = matMatcher;
    }

    @Override
    public boolean hasBadWords(Message message) {
        return matMatcher.detect(message.getText());
    }

    @Override
    public String clearMessage(Message message) {
        return matMatcher.replace(message.getText(), bip);
    }
}
