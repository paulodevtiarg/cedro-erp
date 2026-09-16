package br.com.dpsistemas.cedroerp.services;

import br.com.dpsistemas.cedroerp.repositorys.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class CodigoSegurancaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    public String gerarCodigo() {
        SecureRandom random = new SecureRandom();
        int codigo = 100000 + random.nextInt(900000);
        return String.valueOf(codigo);
    }




}