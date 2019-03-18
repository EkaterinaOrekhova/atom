package ru.atom.chat.client;

import okhttp3.Response;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.atom.chat.client.ChatClient;
import ru.atom.chat.server.ChatApplication;

import java.io.IOException;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ChatApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ChatClientTest {
    private static String MY_NAME_IN_CHAT = "I_AM_STUPID";
    private static String MY_MESSAGE_TO_CHAT = "SOMEONE_KILL_ME";

    @Test
    public void viewChat() throws IOException {
        ChatClient.login(MY_NAME_IN_CHAT);
        ChatClient.say(MY_NAME_IN_CHAT, MY_MESSAGE_TO_CHAT);
        Response response = ChatClient.viewChat();
        System.out.println("[" + response + "]");
        String body = response.body().string();
        Assert.assertNotEquals(0, body.length());
    }

    @Test
    public void login() throws IOException {
        Response response = ChatClient.login(MY_NAME_IN_CHAT);
        System.out.println("[" + response + "]");
        String body = response.body().string();
        Assert.assertTrue(response.code() == 200 || body.equals("Already logged in :("));
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
        Response response = ChatClient.say(MY_NAME_IN_CHAT, MY_MESSAGE_TO_CHAT);
        System.out.println("[" + response + "]");
        String body = response.body().string();
        Assert.assertTrue(response.code() == 200 || body.equals("User doesn't log in"));
    }

    @Test
    public void logout() throws IOException {
        ChatClient.login(MY_NAME_IN_CHAT);
        Response response = ChatClient.logout(MY_NAME_IN_CHAT);
        System.out.println("[" + response + "]");
        System.out.println(response.body().string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void deleteMsg() throws IOException {
        ChatClient.login(MY_NAME_IN_CHAT);
        ChatClient.say(MY_NAME_IN_CHAT, MY_MESSAGE_TO_CHAT);
        Response response = ChatClient.deleteMsg();
        System.out.println("[" + response + "]");
        String body = response.body().string();
        Assert.assertEquals("Chat is clear", body);
    }

    @Test
    public void quote() throws IOException {
        ChatClient.login(MY_NAME_IN_CHAT);
        ChatClient.deleteMsg();
        Response response = ChatClient.quote(MY_NAME_IN_CHAT, MY_MESSAGE_TO_CHAT);
        System.out.println("[" + response + "]");
        String test = response.body().string();
        Assert.assertEquals("No messages to quote", test);
    }
}
