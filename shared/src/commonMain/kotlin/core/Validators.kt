package core

private object Patterns {
    val EMAIL    = Regex("""^[^@\s]+@[^@\s]+\.[^@\s]+$""")
    val USERNAME = Regex("""^[a-zA-Z0-9_][a-zA-Z0-9_ ]{4,18}[a-zA-Z0-9_]$""")
    val CODE     = Regex("""^\d{6}$""")
    val PASSWORD = Regex("""^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&*]).{8,}$""")
}
object Validators {
    fun isEmail(s: String) : Boolean = Patterns.EMAIL.matches(s.trim())
    fun isUsername(s: String) : Boolean = Patterns.USERNAME.matches(s)
    fun isCode(s: String) : Boolean = Patterns.CODE.matches(s)
    fun isPassword(s: String): Boolean = Patterns.PASSWORD.matches(s)
}
