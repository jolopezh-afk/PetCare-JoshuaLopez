import java.time.LocalDateTime

open class Paciente(
    val codigo: String,
    val nombre: String,
    val especie: String,
    val fechaIngreso: LocalDateTime,
    val tipoDueno: TipoDueno
) {

    init {
        Validador.validarCodigo(codigo)
    }

    open fun calcularCostoBase(minutos: Int): Double {
        return 0.0
    }

    open fun detalle(): String {
        return "Código: $codigo | Nombre: $nombre | Especie: $especie"
    }
}