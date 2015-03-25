package net.vc9ufi.ChatClient;

import net.vc9ufi.ChatClient.json_classes.BigAnswer;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class RestController {

    final RestTemplate mRestTemplate;

    public static enum RESPONSE {OK, ERROR_SHORT_LOGIN, ERROR_NOT_LOGGED, ERROR_UNKNOWN, RESPONSE_EXC}

    public static enum REQUEST {LOGIN, POST, GET_NEW, LOGOUT, CHECK_SERVER}


    public RestController() {
        mRestTemplate = new RestTemplate();

        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setConnectTimeout(10000);
        mRestTemplate.setRequestFactory(httpComponentsClientHttpRequestFactory);

        mRestTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(new MediaType("text", "plain", Charset.forName("UTF-8")));
        mediaTypes.add(new MediaType("text", "html", Charset.forName("UTF-8")));

        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
        mRestTemplate.getMessageConverters().add(stringHttpMessageConverter);
    }


    public RESPONSE login(String url, String name) {
        url = url + "/login";
        RESPONSE response;
        try {
            response = mRestTemplate.postForObject(url, name, RESPONSE.class);
        } catch (RuntimeException e) {
            response = RESPONSE.RESPONSE_EXC;
        }
        return response;
    }

    public RESPONSE post(String url, String msg) {
        url = url + "/post";
        RESPONSE response;
        try {
            response = mRestTemplate.postForObject(url, msg, RESPONSE.class);
        } catch (RuntimeException e) {
            response = RESPONSE.RESPONSE_EXC;
        }
        return response;
    }

    public RESPONSE logout(String url) {
        url = url + "/logout";
        RESPONSE response;
        try {
            mRestTemplate.delete(url);
            response = RESPONSE.OK;
        } catch (RuntimeException e) {
            response = RESPONSE.RESPONSE_EXC;
        }
        return response;
    }

    public BigAnswer getNew(String url) {
        url = url + "/new/all";
        BigAnswer answer;
        try {
            answer = mRestTemplate.getForObject(url, BigAnswer.class);
        } catch (RuntimeException e) {
            answer = new BigAnswer(RESPONSE.RESPONSE_EXC);
        }
        return answer;
    }

    public RESPONSE checkServer(String url) {
        url = url + "/check";
        String answer;

        try {
            answer = mRestTemplate.getForObject(url, String.class);
        } catch (RuntimeException e) {
            return RESPONSE.RESPONSE_EXC;
        }

        if (answer.length() > 0) {
            return RESPONSE.OK;
        } else {
            return RESPONSE.ERROR_UNKNOWN;
        }
    }
}
