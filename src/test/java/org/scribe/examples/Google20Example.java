package org.scribe.examples;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

public class Google20Example
{
  private static final String NETWORK_NAME = "Google";
  private static final String PROTECTED_RESOURCE_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
  private static final Token EMPTY_TOKEN = null;

  public static void main(String[] args)
  {
    // Replace these with your own api key and secret
    String apiKey = "809136187279.apps.googleusercontent.com";
    String apiSecret = "kFxdEsBZvXC0nHyw38tZqaSH";
    
    OAuthService service = new ServiceBuilder()
                                  .provider(GoogleApi20.class)
                                  .apiKey(apiKey)
                                  .apiSecret(apiSecret)
                                  .scope("https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile")
                                  .callback("http://openam.test.aomai.jp/oauth2callback")
                                  .build();
    /* Set HTTP proxy.*/
    Proxy proxy = null; //new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8081));
    service.getConfig().setProxy(proxy);
    
    Scanner in = new Scanner(System.in);

    System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
    System.out.println();

    // Obtain the Authorization URL
    System.out.println("Fetching the Authorization URL...");
    String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
    System.out.println("Got the Authorization URL!");
    System.out.println("Now go and authorize Scribe here:");
    System.out.println(authorizationUrl);
    System.out.println("And paste the authorization code here");
    System.out.print(">>");
    Verifier verifier = new Verifier(in.nextLine());
    System.out.println();
    
    // Trade the Request Token and Verfier for the Access Token
    System.out.println("Trading the Request Token for an Access Token...");
    service = new ServiceBuilder()
    .provider(GoogleApi20.class)
    .apiKey(apiKey)
    .apiSecret(apiSecret)
    .callback("http://openam.test.aomai.jp/oauth2callback")
    .build();
    service.getConfig().setProxy(proxy);
    Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
    System.out.println("Got the Access Token!");
    System.out.println("(if your curious it looks like this: " + accessToken + " )");
    System.out.println();

    // Now let's go and ask for a protected resource!
    System.out.println("Now we're going to access a protected resource...");
    OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
    request.setProxy(proxy);
    
    service.signRequest(accessToken, request);
    Response response = request.send();
    System.out.println("Got it! Lets see what we found...");
    System.out.println();
    System.out.println(response.getCode());
    System.out.println(response.getBody());

    System.out.println();
    System.out.println("Thats it man! Go and build something awesome with Scribe! :)");

  }
}