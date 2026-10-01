enum class TipoDueno {
    PARTICULAR,
    CONVENIO,
    MUNICIPAL;

    companion object {
        fun desdeString(valor: String): TipoDueno {
            return when (valor.trim().uppercase()) {
                "PARTICULAR" -> PARTICULAR
                "CONVENIO" -> CONVENIO
                "MUNICIPAL" -> MUNICIPAL
                else -> throw TipoDuenoInvalidoException(
                    "Tipo de dueño inválido: $valor"
                )
            }
        }
    }
}