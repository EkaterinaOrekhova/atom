package ru.atom.chat;

import okhttp3.Response;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.atom.chat.ChatClient;
import ru.atom.chat.ChatApplication;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ChatApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ChatClientTest {
    private static final Logger log = LoggerFactory.getLogger(ChatClientTest.class);

    private static String MY_NAME_IN_CHAT = "I_AM_STUPID";
    private static String MY_MESSAGE_TO_CHAT = "KILL_ME_SOMEONE";

    @Test
    public void viewChat() throws IOException {
        ChatClient.login(MY_NAME_IN_CHAT);
        ChatClient.say(MY_NAME_IN_CHAT, MY_MESSAGE_TO_CHAT);
        Response response = ChatClient.viewChat();
        System.out.println("[" + response + "]");
        String body = response.body().string();
        Assert.assertNotEquals(0, body.length());

        ChatClient.logout(MY_NAME_IN_CHAT);
    }

    @Test
    public void login() throws IOException {
        Response response = ChatClient.login(MY_NAME_IN_CHAT);
        System.out.println("[" + response + "]");
        String body = response.body().string();
        Assert.assertTrue(response.code() == 200 || body.equals("User already logged in"));

        ChatClient.logout(MY_NAME_IN_CHAT);
    }

    @Test
    public void viewOnline() throws IOException {
        Response response = ChatClient.viewOnline();
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void say() throws IOException {
        Response response =  ChatClient.say(MY_NAME_IN_CHAT, MY_MESSAGE_TO_CHAT);
        System.out.println("[" + response + "]");
        String body = response.body().string();
        Assert.assertTrue(body.contains("You must write under logined username"));
    }

    @Test
    public void logout() throws IOException {
        Response response = ChatClient.logout(MY_NAME_IN_CHAT);
        System.out.println("[" + response + "]");
        String body = response.body().string();
        Assert.assertTrue(body.contains("User already logged out"));
    }


}
