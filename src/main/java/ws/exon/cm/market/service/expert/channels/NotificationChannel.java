package ws.exon.cm.market.service.expert.channels;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.Channel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationChannel implements Channel {
    private final Gson gson;

    @Override
    public void send(final Object object) {
        final Map<String, Object> obj = new HashMap<>();
        obj.put("name", object);
    }
}
