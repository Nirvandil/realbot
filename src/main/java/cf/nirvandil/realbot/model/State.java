package cf.nirvandil.realbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class State {
    @Id
    private String id;
    @NonNull
    @Indexed(unique = true)
    private Long chatId;
    @NonNull
    private Integer userId;
    @NonNull
    private Boolean isDataForCallRequested;
    @NonNull
    private Boolean isDataForConnectRequested;
}
