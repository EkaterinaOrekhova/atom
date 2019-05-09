package ru.atom.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import ru.atom.chat.model.User;
import ru.atom.chat.service.ChatService;

@Controller
@RequestMapping("chat")
public class ChatController {
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    File file = new File("chathistory.txt");

    @Autowired
    private ChatService chatService;

    /**
     * curl -X POST -i localhost:8080/chat/login -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> login(@RequestParam("name") String name) {
        if (name.length() < 1) {
            return ResponseEntity.badRequest().body("Too short name, sorry");
        }
        if (name.length() > 20) {
            return ResponseEntity.badRequest().body("Too long name, sorry");
        }

        User alreadyLoggedIn = chatService.getLoggedIn(name);

        if (alreadyLoggedIn != null) {
            return ResponseEntity.badRequest()
                .body("Already logged in");
        }

        chatService.login(name);
        return ResponseEntity.ok().build();

    }

    /**
     * curl -i localhost:8080/chat/chat
     */
    @RequestMapping(
            path = "chat",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> chat() {
        String messages = chatService.getChat();
        return ResponseEntity.ok(messages);
    }

    /**
     * curl -i localhost:8080/chat/online
     */
    @RequestMapping(
            path = "online",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity online() {
        List<User> online = chatService.getOnlineUsers();

        String responseBody = online.stream()
            .map(User::getLogin)
            .collect(Collectors.joining("\n"));

        return ResponseEntity.ok().body(responseBody);
    }

    /**
     * curl -X POST -i localhost:8080/chat/logout -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "logout",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity logout(@RequestParam("name") String name) {

        User user = chatService.getLoggedIn(name);
        if (user == null)
            return ResponseEntity.badRequest().body("User already logged out");
        chatService.logout(user);
        return ResponseEntity.ok().build();
    }

    /**
     * curl -X POST -i localhost:8080/chat/say -d "name=I_AM_STUPID&msg=Hello everyone in this chat"
     */
    @RequestMapping(
            path = "say",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity say(@RequestParam("name") String name, @RequestParam("msg") String msg) {

        User user = chatService.getLoggedIn(name);

        if (user == null)
            return ResponseEntity.badRequest().body("You must write under logined username");

        chatService.say(name, msg);
        return ResponseEntity.ok().build();
    }

    /**
     * curl -X DELETE localhost:8080/chat/deleteMsg
     * Очистить все сообщения в чате
     */
    @RequestMapping(
        path = "deleteMsg",
        method = RequestMethod.DELETE,
        produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity deleteMsg() {
        return ResponseEntity.ok().build();
    }

    /**
     * curl -i localhost:8080/chat/saveMsg
     */
    @RequestMapping(
        path = "saveMsg",
        method = RequestMethod.GET,
        produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity saveMsg() {
        return ResponseEntity.ok().build();
    }

    /**
     * curl -i localhost:8080/chat/loadMsg
     */
    @RequestMapping(
        path = "loadMsg",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity loadMsg() {
        return ResponseEntity.ok().build();
    }
}
