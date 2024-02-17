package de.codinghaus.avalarm.service;

import de.codinghaus.avalarm.config.ConfigProperties;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.MessageChannel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscordClient {

    private final ConfigProperties configProperties;
    private discord4j.core.DiscordClient client;

    @PostConstruct
    public void setup() {
        client = discord4j.core.DiscordClient.create(configProperties.getDiscord().getToken());
    }

    public void informAboutAlarm(final String message) {
        Snowflake channelId = Snowflake.of(configProperties.getDiscord().getChannelId());
        Snowflake userId = Snowflake.of(configProperties.getDiscord().getUserId());
        GatewayDiscordClient gateway = client.login().block();

        if (gateway != null) {
            gateway.getChannelById(channelId)
                    .ofType(MessageChannel.class)
                    .flatMap(channel -> channel.createMessage("<@!" + userId.asString() + "> Alarm! " + "(" + message +")"))
                    .subscribe();
        }
    }
}
