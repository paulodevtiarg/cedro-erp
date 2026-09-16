package br.com.dpsistemas.cedroerp.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCodigo(String email, String codigo, Boolean primeiroAcesso) {


        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);

            // 🔥 Título do email (assunto)
            String assunto = (primeiroAcesso != null && primeiroAcesso)
                    ? "Primeiro Acesso - Cedro ERP"
                    : "Recuperação de Senha - Cedro ERP";

            helper.setSubject(assunto);

            // 🔥 Conteúdo dinâmico
            String titulo = (primeiroAcesso != null && primeiroAcesso)
                    ? "Bem-vindo ao Cedro ERP"
                    : "Código de Recuperação de Senha";

            String mensagem = (primeiroAcesso != null && primeiroAcesso)
                    ? "Utilize o código abaixo para criar sua senha de acesso:"
                    : "Utilize o código abaixo para redefinir sua senha:";
            String html = """
    	        		<div style="font-family: Arial, sans-serif; background-color:#F2F4F1; padding:20px;">
    	        		    <div style="max-width:600px; margin:0 auto; background:white; border-radius:12px; overflow:hidden; box-shadow:0 4px 20px rgba(0,0,0,0.1);">
    	        		    <!-- HEADER -->
    	        		        <div style="background: linear-gradient(135deg, #1F4D3A, #8B5E3C); padding:20px; text-align:center;">

    	        		            <!-- 🔥 OVERLAY ESCURO (SEGREDO AQUI) -->
    	        		            <div style="
    	        		                position:absolute;
    	        		                top:0;
    	        		                left:0;
    	        		                width:100%%;
    	        		                height:100%%;
    	        		                background: rgba(0,0,0,0.45);
    	        		            "></div>

    	        		            <!-- LOGO -->
    	        		            <img src="https://res.cloudinary.com/dyl8tkovm/image/upload/v1789495910/logos/logo-white_iw3qiu.png" 
    	        		                 alt="Cedro ERP Logo" 
    	        		                 style="
    	        		                    position:relative;
    	        		                    z-index:1;
    	        		                    max-width:160px;
    	        		                    width:100%%;
    	        		                     
    	        		                 ">

    	        		            <p style="
    	        		                position:relative;
    	        		                z-index:1;
    	        		                color:#ffffff;
    	        		                margin-top:10px;
    	        		                font-size:13px;
    	        		                font-weight:500;
    	        		            ">
    	        		                Sistema Cedro 2026
    	        		            </p>
    	        		        </div>

    	        		        <!-- BODY -->
    	        		        <div style="padding:30px; text-align:center;">
    	        		        
    	        		            <h3 style="color:#1F4D3A;">%s</h3>
    	        		            
    	        		            <p style="font-size:15px; color:#343A36;">
    	        		                %s
    	        		            </p>

    	        		            <!-- CODE BOX -->
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

    	        		            <p style="font-size:13px; color:#6B706D; margin-top:20px;">
    	        		                Este código é válido por <b>15 minutos</b>.<br>
    	        		                Se você não solicitou isso, ignore este e-mail.
    	        		            </p>

    	        		        </div>

    	        		        <!-- FOOTER -->
    	        		        <div style="background:#1F4D3A; color:white; text-align:center; padding:12px; font-size:12px;">
    	        		            © CEDRO ERP - Sistema Gestão
    	        		        </div>

    	        		    </div>
    	        		</div>
    	        		""".formatted(titulo, mensagem, codigo);

            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
             System.err.println("ERRO REAL NO ENVIO DO EMAIL:");
    e.printStackTrace();

    throw new RuntimeException(
            "Erro ao enviar e-mail",
            e
    );
        }
    }
}
