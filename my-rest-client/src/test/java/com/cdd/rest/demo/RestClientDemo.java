package com.cdd.rest.demo;


import com.cdd.rest.util.Maps;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class RestClientDemo {

    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();
        Entity.json("{\n" +
                "\t\"id\": 91001,\n" +
                "\t\"name\": \"错对对\",\n" +
                "\t\"email\": \"780106788@qq.com\"\n" +
                "}");
        Response response = client
                .target("http://127.0.0.1:80/user-web/login/testPost")      // WebTarget
                .request() // Invocation.Builder
                .post(Entity.json(Maps.of("id",91001,"name","错对对","email","780106788@qq.com")));                                    //  Response

        String content = response.readEntity(String.class);

        System.out.println(content);

    }
}
