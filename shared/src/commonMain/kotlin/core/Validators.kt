package core

object Patterns {
    val EMAIL    = Regex("""^[^@\s]+@[^@\s]+\.[^@\s]+$""")
    val USERNAME = Regex("""^[a-zA-Z0-9_]{3,20}$""")
    val CODE     = Regex("""^\d{6}$""")
}
object Validators {
    fun isEmail(s: String) : Boolean = Patterns.EMAIL.matches(s.trim())
    fun isUsername(s: String) : Boolean = Patterns.USERNAME.matches(s)
    fun isCode(s: String) : Boolean = Patterns.CODE.matches(s)
    fun isPassword(s: String): Boolean = s.length >= 8
}
