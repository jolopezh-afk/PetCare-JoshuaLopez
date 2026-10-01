class CodigoInvalidoException(
    mensaje: String
) : Exception(mensaje)

class TarifaInvalidaException(
    mensaje: String
) : Exception(mensaje)

class PacienteNoEncontradoException(
    mensaje: String
) : Exception(mensaje)

class SistemaLlenoException(
    mensaje: String
) : Exception(mensaje)

class TipoDuenoInvalidoException(
    mensaje: String
) : Exception(mensaje)

class PacienteDuplicadoException(
    mensaje: String
) : Exception(mensaje)