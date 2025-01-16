package com.example.backend.mail.service;

import com.example.backend.mail.model.repository.EmailResponseRepository;
import com.example.backend.mail.model.request.EmailRequest;
import com.example.backend.mail.model.response.EmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.JSONObject;

@Service
@Log4j2
@RequiredArgsConstructor
public class ChatGptService {

    private final EmailResponseRepository emailResponseRepository;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    @Value("${openai.api.key}")
    private String apiKey;

    public EmailResponse generateEmail(EmailRequest emailRequest) {
        String prompt = createPrompt(emailRequest);
        String content = sendChatGptRequest(prompt, "You are an email assistant.", 500);
        String title = createTitle(emailRequest);

        EmailResponse emailResponse = new EmailResponse();
        emailResponse.setGeneratedTitle(title);
        emailResponse.setGeneratedContent(content);
        emailResponse.setRecipientMail(emailRequest.getForm2().getRecipientMail());

        emailResponseRepository.save(emailResponse);
        return emailResponse;
    }

    private String createPrompt(EmailRequest emailRequest) {
        String etiquetteGuidance = getEtiquetteGuidance(emailRequest.getLanguage());
        return String.format(
                "Create a professional email in %s language following this etiquette: %s\n" +
                        "From: %s (%s, %s, %s)\n" +
                        "To: %s (%s)\n" +
                        "Situation: %s\n" +
                        "Expected Reply: %s\n" +
                        "Tone: %s" +
                        "The email should avoid including any subject line or additional metadata, or sender's phone number.",
                emailRequest.getLanguage(),
                etiquetteGuidance,
                emailRequest.getForm1().getName(),
                emailRequest.getForm1().getAffiliation(),
                emailRequest.getForm1().getNumber(),
                emailRequest.getForm1().getJob(),
                emailRequest.getForm2().getRecipientName(),
                emailRequest.getForm2().getRecipientMail(),
                emailRequest.getForm3().getSituation(),
                emailRequest.getForm3().getDesiredAnswer(),
                emailRequest.getForm3().getTone()
        );
    }

    private String createTitle(EmailRequest emailRequest) {
        String originalTitle = String.format(
                "[%s] %s",
                emailRequest.getForm1().getAffiliation(),
                emailRequest.getForm3().getSituation()
        );
        String translatedTitle = sendChatGptRequest(
                String.format("Translate the following title into %s. Only return the translated title without any additional explanation or prefix: \"%s\"",
                        emailRequest.getLanguage(), originalTitle),
                "You are a professional translator.",
                100
        );
        return translatedTitle.trim();
    }

    private String getEtiquetteGuidance(String language) {
        switch (language) {
            case "english":
                return "Please compose the email in English only.";
            case "korean":
                return "이메일을 한국어로만 작성해주세요.";
            case "japanese":
                return "メールを日本語でのみ作成してください。";
            case "chinese":
                return "请仅使用中文撰写邮件。";
            case "french":
                return "Veuillez rédiger l'email uniquement en français.";
            case "german":
                return "Bitte verfassen Sie die E-Mail ausschließlich auf Deutsch.";
            default:
                return "Please compose the email in the specified language only.";
        }
    }

    private String sendChatGptRequest(String prompt, String systemMessage, int maxTokens) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-4o-mini-2024-07-18");
        requestBody.put("messages", new JSONArray()
                .put(new JSONObject().put("role", "system").put("content", systemMessage))
                .put(new JSONObject().put("role", "user").put("content", prompt))
        );
        requestBody.put("max_tokens", maxTokens);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
            JSONObject responseBody = new JSONObject(response.getBody());
            return responseBody.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim();
        } catch (HttpClientErrorException e) {
            log.error("HTTP Status: {}", e.getStatusCode());
            log.error("Response Body: {}", e.getResponseBodyAsString());
            throw new RuntimeException("Failed to process request. Please try again later.");
        }
    }
}
