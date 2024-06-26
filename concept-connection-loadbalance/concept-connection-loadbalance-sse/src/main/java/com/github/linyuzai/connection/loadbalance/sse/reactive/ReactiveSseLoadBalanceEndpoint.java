package com.github.linyuzai.connection.loadbalance.sse.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.sse.concept.DefaultSseIdGenerator;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseIdGenerator;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.Map;

@Getter
@Setter
@RestController
@RequestMapping("${concept.sse.load-balance.observable-endpoint:concept-sse-subscriber}")
@RequiredArgsConstructor
public class ReactiveSseLoadBalanceEndpoint {

    private SseIdGenerator sseIdGenerator = new DefaultSseIdGenerator();

    private SseFluxFactory sseFluxFactory = new DefaultSseFluxFactory();

    private final SseLoadBalanceConcept concept;

    private final String endpoint;

    @GetMapping
    public Flux<ServerSentEvent<Object>> loadBalanceEndpoint(@RequestParam Map<Object, Object> params) {
        Object id = sseIdGenerator.generateId(params);
        return sseFluxFactory.create(Flux.create((FluxSink<ServerSentEvent<Object>> fluxSink) -> {
                    ReactiveSseCreation request = new ReactiveSseCreation(id, endpoint, fluxSink);
                    ReactiveSseConnection connection = new ReactiveSseConnection(fluxSink);
                    connection.setCreation(request);
                    connection.setType(Connection.Type.OBSERVABLE);
                    connection.getMetadata().putAll(params);
                    concept.onEstablish(connection);
                }))
                .doOnError(it -> concept.onError(id, Connection.Type.OBSERVABLE, it))
                .doOnComplete(() -> concept.onClose(id, Connection.Type.OBSERVABLE, null));
    }
}
