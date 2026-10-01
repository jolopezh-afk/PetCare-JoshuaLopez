object Validador {

    private val regexCodigo = Regex("^[A-Za-z]{2}\\d{2}[A-Za-z]{2}$")

    fun validarCodigo(codigo: String) {
        if (!regexCodigo.matches(codigo)) {
            throw CodigoInvalidoException(
                "Código inválido: $codigo. Debe tener el formato AA00AA."
            )
        }
    }

    fun validarTipoDueno(tipoDueno: String): TipoDueno {
        return TipoDueno.desdeString(tipoDueno)
    }
}