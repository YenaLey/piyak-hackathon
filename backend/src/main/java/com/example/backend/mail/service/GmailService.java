package com.example.backend.mail.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GmailService {

    private final String GMAIL_API_LIST_URL = "https://gmail.googleapis.com/gmail/v1/users/me/messages?maxResults=5&format=full"; // 메일 목록 가져오는 URL

    public String getLatestEmails(String accessToken) {
        // 1. Gmail API 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 2. RestTemplate을 사용하여 메일 목록 가져오기
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // 메일 목록을 가져오는 요청
        ResponseEntity<String> response = restTemplate.exchange(GMAIL_API_LIST_URL, HttpMethod.GET, requestEntity, String.class);

        // 3. 메일 목록에서 각 메일의 ID를 가져오기
        String responseBody = response.getBody();
        JSONObject jsonResponse = new JSONObject(responseBody);
        JSONArray messages = jsonResponse.getJSONArray("messages");

        // 메일 ID만 추출하여 메일 상세 정보 가져오기
        JSONArray mailDetailsArray = new JSONArray();

        for (int i = 0; i < messages.length(); i++) {
            String messageId = messages.getJSONObject(i).getString("id");

            // 각 메일에 대한 상세 정보를 가져오는 요청 (get API)
            String mailDetailsUrl = "https://gmail.googleapis.com/gmail/v1/users/me/messages/" + messageId;
            ResponseEntity<String> mailDetailResponse = restTemplate.exchange(mailDetailsUrl, HttpMethod.GET, requestEntity, String.class);

            // 상세 정보 응답 본문
            String mailDetails = mailDetailResponse.getBody();
            JSONObject mailDetailsJson = new JSONObject(mailDetails);

            // 메일의 제목, 발신자, 본문 등 필요한 정보 추가
            JSONObject mailData = new JSONObject();
            mailData.put("snippet", mailDetailsJson.optString("snippet")); // 메일 일부 내용

            mailData.put("subject", getHeaderValue(mailDetailsJson, "Subject"));


            // 메일 세부 정보를 배열에 추가
            mailDetailsArray.put(mailData);
        }

        // 메일 목록에 대한 JSON 결과 반환
        JSONObject result = new JSONObject();
        result.put("emails", mailDetailsArray);

        return result.toString(); // 결과를 JSON 문자열로 반환
    }

    // 메일의 헤더 값을 추출하는 메서드
    private String getHeaderValue(JSONObject messageJson, String headerName) {
        JSONArray headers = messageJson.optJSONObject("payload").optJSONArray("headers");
        for (int i = 0; i < headers.length(); i++) {
            JSONObject header = headers.getJSONObject(i);
            if (header.getString("name").equals(headerName)) {
                return header.getString("value");
            }
        }
        return "";
    }
}