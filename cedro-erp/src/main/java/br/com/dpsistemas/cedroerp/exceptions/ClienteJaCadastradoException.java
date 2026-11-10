package br.com.dpsistemas.cedroerp.exceptions;

public class ClienteJaCadastradoException extends RuntimeException {

    private final String campo;
    private final Long idClienteExistente;

    public ClienteJaCadastradoException(String campo, String mensagem, Long idClienteExistente) {
        super(mensagem);
        this.campo = campo;
        this.idClienteExistente = idClienteExistente;
    }

    public String getCampo() { return campo; }
    public Long getIdClienteExistente() { return idClienteExistente; }
}
