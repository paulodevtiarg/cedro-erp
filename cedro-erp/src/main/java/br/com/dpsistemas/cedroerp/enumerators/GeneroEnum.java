package br.com.dpsistemas.cedroerp.enumerators;

public enum GeneroEnum {
	MASCULINO("M", "Masculino"),
	FEMININO("F", "Feminino"),
	TRANS_MASCULINO("TM","Masculino Trans"),
	TRANS_FEMININO("TF", "Feminino Trans"),
	NAO_INFORMADO("NI","Não Informado"),
	OUTRO("O", "Outro");

	private final String codigo;
	private final String descricao;
	
	GeneroEnum(String codigo, String descricao){
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public String getCodigo(){
		return codigo;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public static GeneroEnum fromCodigo(String codigo) {
		for(GeneroEnum genero : GeneroEnum.values()) {
			if(genero.getCodigo().equals(codigo)) {
				return genero;
			}
		}
		throw new IllegalArgumentException("Código Inválido"+codigo);
	}

}
