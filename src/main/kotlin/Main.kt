import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

fun main() {
    runBlocking {
        val petCare = PetCare()

        println("========================================")
        println("   Sistema ${petCare.nombre} - Inicio de turno")
        println("========================================")

        println("\n>>> REGISTRO DE ENTRADAS")
        registrarCanino(petCare, "CA12CD", "Max", "Golden Retriever", "convenio")
        registrarCanino(petCare, "CA99ZA", "Luna", "Labrador", "particular")
        registrarFelino(petCare, "FE22TO", "Misi", "Siamés", "particular")
        registrarExotico(petCare, "EX44RG", "Loro", "Amazónico", "municipal", true)
        registrarExotico(petCare, "EX77RG", "Iguana", "Verde", "particular", false)

        println("\n>>> ERRORES EN ENTRADAS")

        registrarCanino(petCare, "123ABC", "Rocky", "Bulldog", "particular")

        registrarCanino(petCare, "CA55ZZ", "Toby", "Beagle", "vip")

        Reporte.mostrarEstadoBoxes(petCare)

        println("\n>>> PRUEBA DE SISTEMA SIN CAPACIDAD")

        for (numero in 6..10) {
            petCare.inhabilitarBox(numero, "Mantención programada")
        }
        registrarFelino(petCare, "FE55TO", "Tom", "Angora", "particular")

        println("\n>>> ERRORES EN SALIDAS")

        registrarSalida(petCare, "ZZ99ZZ", 30)

        registrarSalida(petCare, "CA12CD", 0)

        println("\n>>> SALIDAS")
        registrarSalida(petCare, "CA12CD", 75)
        registrarSalida(petCare, "CA99ZA", 180)
        registrarSalida(petCare, "FE22TO", 18)
        registrarSalida(petCare, "EX44RG", 120)
        registrarSalida(petCare, "EX77RG", 45)

        println("\n>>> CONSULTAS DE NEGOCIO")
        Reporte.mostrarBoxesDisponibles(petCare)
        Reporte.mostrarPacientesConvenio(petCare)
        Reporte.mostrarPromedioIngresos(petCare)
        Reporte.mostrarCodigosFinalizados(petCare)
        Reporte.mostrarPacienteMayorUso(petCare)
        Reporte.mostrarRecaudacionPorTipo(petCare)

        Reporte.mostrarResumenCierre(petCare)
    }
}


suspend fun registrarCanino(
    petCare: PetCare,
    codigo: String,
    nombre: String,
    especie: String,
    tipoDueno: String
) {
    try {
        val paciente = Canino(
            codigo, nombre, especie, LocalDateTime.now(),
            Validador.validarTipoDueno(tipoDueno)
        )
        petCare.registrarEntrada(paciente)
    } catch (e: Exception) {
        mostrarError(e)
    }
}

suspend fun registrarFelino(
    petCare: PetCare,
    codigo: String,
    nombre: String,
    especie: String,
    tipoDueno: String
) {
    try {
        val paciente = Felino(
            codigo, nombre, especie, LocalDateTime.now(),
            Validador.validarTipoDueno(tipoDueno)
        )
        petCare.registrarEntrada(paciente)
    } catch (e: Exception) {
        mostrarError(e)
    }
}

suspend fun registrarExotico(
    petCare: PetCare,
    codigo: String,
    nombre: String,
    especie: String,
    tipoDueno: String,
    esSilvestre: Boolean
) {
    try {
        val paciente = Exotico(
            codigo, nombre, especie, LocalDateTime.now(),
            Validador.validarTipoDueno(tipoDueno), esSilvestre
        )
        petCare.registrarEntrada(paciente)
    } catch (e: Exception) {
        mostrarError(e)
    }
}

suspend fun registrarSalida(petCare: PetCare, codigo: String, minutos: Int) {
    try {
        petCare.registrarSalida(codigo, minutos)
    } catch (e: Exception) {
        mostrarError(e)
    }
}


fun mostrarError(e: Exception) {
    when (e) {
        is CodigoInvalidoException -> println("[ERROR] Código de atención inválido. ${e.message}")
        is TipoDuenoInvalidoException -> println("[ERROR] Tipo de dueño inválido. ${e.message}")
        is TarifaInvalidaException -> println("[ERROR] Tarifa inválida. ${e.message}")
        is PacienteNoEncontradoException -> println("[ERROR] Paciente no encontrado. ${e.message}")
        is SistemaLlenoException -> println("[ERROR] Sistema sin capacidad. ${e.message}")
        is PacienteDuplicadoException -> println("[ERROR] Paciente duplicado. ${e.message}")
        else -> println("[ERROR] Ocurrió un problema inesperado: ${e.message}")
    }
}