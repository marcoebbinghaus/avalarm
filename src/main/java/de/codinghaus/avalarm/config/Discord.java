package de.codinghaus.avalarm.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Discord {
    private String token;
    private String channelId;
    private String userId;
}
