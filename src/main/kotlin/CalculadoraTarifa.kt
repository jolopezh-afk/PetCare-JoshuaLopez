object CalculadoraTarifas {

    private const val FACTOR_IVA = 1.19
    private const val FACTOR_DESCUENTO_MUNICIPAL = 0.50

    fun calcular(paciente: Paciente, minutos: Int): Double {
        if (minutos <= 0) {
            throw TarifaInvalidaException("El tiempo de atención debe ser mayor a 0 minutos")
        }

        val costoBase = paciente.calcularCostoBase(minutos)

        if (paciente is Felino && paciente.esAtencionGratuita(minutos)) {
            return 0.0
        }

        var montoFinal = costoBase * FACTOR_IVA

        if (paciente.tipoDueno == TipoDueno.MUNICIPAL) {
            montoFinal *= FACTOR_DESCUENTO_MUNICIPAL
        }

        validarMonto(montoFinal)
        return montoFinal
    }

    private fun validarMonto(monto: Double) {
        if (monto.isNaN() || monto.isInfinite() || monto <= 0.0) {
            throw TarifaInvalidaException("La tarifa calculada no es válida: $monto")
        }
    }
}