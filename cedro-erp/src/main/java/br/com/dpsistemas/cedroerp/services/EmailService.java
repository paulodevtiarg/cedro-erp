package br.com.dpsistemas.cedroerp.services;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class EmailService {

	private final RestClient restClient;

	private final String apiKey;
	private final String remetente;

	public EmailService(
			@Value("${resend.api.key}") String apiKey,
			@Value("${resend.from}") String remetente) {

		this.apiKey = apiKey;
		this.remetente = remetente;

		this.restClient = RestClient.builder()
				.baseUrl("https://api.resend.com")
				.build();
	}

	public void enviarCodigo(
			String email,
			String codigo,
			Boolean primeiroAcesso) {

		try {

			String assunto =
					Boolean.TRUE.equals(primeiroAcesso)
							? "Primeiro Acesso - Cedro ERP"
							: "Recuperação de Senha - Cedro ERP";

			String titulo =
					Boolean.TRUE.equals(primeiroAcesso)
							? "Bem-vindo ao Cedro ERP"
							: "Código de Recuperação de Senha";

			String mensagem =
					Boolean.TRUE.equals(primeiroAcesso)
							? "Utilize o código abaixo para criar sua senha de acesso:"
							: "Utilize o código abaixo para redefinir sua senha:";

			String html = """
                    <div style="font-family: Arial, sans-serif; background-color:#F2F4F1; padding:20px;">

                        <div style="
                            max-width:600px;
                            margin:0 auto;
                            background:white;
                            border-radius:12px;
                            overflow:hidden;
                            box-shadow:0 4px 20px rgba(0,0,0,0.1);">

                            <div style="
                                background:linear-gradient(135deg, #1F4D3A, #8B5E3C);
                                padding:20px;
                                text-align:center;">

                                <img
                                    src="https://res.cloudinary.com/dyl8tkovm/image/upload/v1789495910/logos/logo-white_iw3qiu.png"
                                    alt="Cedro ERP"
                                    style="
                                        max-width:160px;
                                        width:100%%;
                                    ">

                                <p style="
                                    color:#ffffff;
                                    margin-top:10px;
                                    font-size:13px;
                                    font-weight:500;">

                                    Sistema Cedro 2026

                                </p>

                            </div>

                            <div style="padding:30px; text-align:center;">

                                <h3 style="color:#1F4D3A;">
                                    %s
                                </h3>

                                <p style="font-size:15px; color:#343A36;">
                                    %s
                                </p>

                                <div style="
                                    margin:20px auto;
                                    font-size:28px;
                                    font-weight:bold;
                                    letter-spacing:6px;
                                    color:#1F4D3A;
                                    background:#F5F2EA;
                                    padding:15px 25px;
                                    border-radius:10px;
                                    display:inline-block;
                                    border:2px dashed #C79A3B;">

                                    %s

                                </div>

                                <p style="
                                    font-size:13px;
                                    color:#6B706D;
                                    margin-top:20px;">

                                    Este código é válido por
                                    <b>15 minutos</b>.

                                    <br>

                                    Se você não solicitou isso,
                                    ignore este e-mail.

                                </p>

                            </div>

                            <div style="
                                background:#1F4D3A;
                                color:white;
                                text-align:center;
                                padding:12px;
                                font-size:12px;">

                                © CEDRO ERP - Sistema Gestão

                            </div>

                        </div>

                    </div>
                    """.formatted(
					titulo,
					mensagem,
					codigo
			);

			Map<String, Object> body = Map.of(
					"from", remetente,
					"to", List.of(email),
					"subject", assunto,
					"html", html
			);

			restClient
					.post()
					.uri("/emails")
					.header(
							"Authorization",
							"Bearer " + apiKey
					)
					.body(body)
					.retrieve()
					.toBodilessEntity();

		} catch (Exception e) {

			throw new RuntimeException(
					"Erro ao enviar e-mail",
					e
			);
		}
	}
}