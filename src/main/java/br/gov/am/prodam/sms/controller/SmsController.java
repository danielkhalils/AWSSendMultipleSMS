package br.gov.am.prodam.sms.controller;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class SmsController {
	public static void main(String[] args) {

		// Suas Credenciais
		String ACCESS_KEY = "INSIRA SUA CHAVE ACCESS_KEY";
		String SECRET_KEY = "INSIRA SUA CHAVE SECRET_KEY";
		String topicName = "INSIRA O NOME DO TÓPICO CRIADO NO AWS SNS";
		String message = "INSIRA SUA MENSAGEM AQUI";

		// Adicionando número à lista de números
		List<String> phoneNumbers = Arrays.asList("+5592999999999", "+5592999999999", "+5592999999999", "+5592999999999");
		AmazonSNSClient snsClient = new AmazonSNSClient(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY));

		// Criando um tópico SMS
		String topicArn = createSNSTopic(snsClient, topicName);

		// Assinando um número de telefone ao tópico
		subscribeToTopic(snsClient, topicArn, "sms", phoneNumbers);

		// Publicando a mensagem ao tópico selecionado
		sendSMSMessageToTopic(snsClient, topicArn, message);
	}
	public static String createSNSTopic(AmazonSNSClient snsClient, String topicName) {
		CreateTopicRequest createTopic = new
				CreateTopicRequest(topicName);
		CreateTopicResult result =
				snsClient.createTopic(createTopic);
		return result.getTopicArn();
	}
	public static void subscribeToTopic(AmazonSNSClient snsClient, String topicArn, String protocol, List<String> phoneNumbers) {
		for (String phoneNumber : phoneNumbers) {
			SubscribeRequest subscribe = new SubscribeRequest(topicArn, protocol, phoneNumber);
			snsClient.subscribe(subscribe);
		}
	}
	public static String sendSMSMessageToTopic(AmazonSNSClient snsClient, String topicArn, String message) {
		PublishResult result = snsClient.publish(new PublishRequest()
				.withTopicArn(topicArn)
				.withMessage(message));
		return result.getMessageId();
	}
}
