package com.bisang.backend.videocall.controller;
import io.openvidu.java.client.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/openvidu")
public class OpenViduController {

    private OpenVidu openvidu;
//    private Map<String, Session> activeSessions = new ConcurrentHashMap<>();

    @Value("${openvidu.url}")
    private String openviduUrl;

    @Value("${openvidu.secret}")
    private String openviduSecret;

    // Spring이 필드 값을 모두 주입한 후 실행됨
    @PostConstruct
    public void init() {
        if (openviduUrl == null || openviduUrl.isEmpty()) {
            throw new RuntimeException("OpenVidu URL이 설정되지 않았습니다.");
        }
        if (openviduSecret == null || openviduSecret.isEmpty()) {
            throw new RuntimeException("OpenVidu Secret이 설정되지 않았습니다.");
        }
        this.openvidu = new OpenVidu(openviduUrl, openviduSecret);
    }

    @GetMapping("/sessions")
    public List<Session> getActiiveSessions(){
        return openvidu.getActiveSessions();
    }

    @PostMapping("/sessions")
    public ResponseEntity<String> initializeSession(@RequestBody(required = false) Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {
        SessionProperties properties = SessionProperties.fromJson(params).build();
        Session session = openvidu.createSession(properties);
        return new ResponseEntity<>(session.getSessionId(), HttpStatus.OK);
    }

    @PostMapping("/sessions/{sessionId}/connections")
    public ResponseEntity<String> createConnection(@PathVariable("sessionId") String sessionId,
                                                   @RequestBody(required = false) Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {
        Session session = openvidu.getActiveSession(sessionId);
        if (session == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ConnectionProperties properties = ConnectionProperties.fromJson(params).build();
        Connection connection = session.createConnection(properties);
//        System.out.println("sessionId: " + sessionId);
//        System.out.println("token: " + connection.getToken().split("token=")[1]);
        System.out.println("wss url: " + connection.getToken());
//        System.out.println(openvidu.getActiveSession(sessionId));
        return new ResponseEntity<>(connection.getToken(), HttpStatus.OK);
    }
}
