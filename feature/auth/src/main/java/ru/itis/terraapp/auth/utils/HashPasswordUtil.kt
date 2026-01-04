package ru.itis.terraapp.auth.utils

import org.mindrot.jbcrypt.BCrypt

fun hashPassword(password: String): String {
    return BCrypt.hashpw(password, BCrypt.gensalt())
}

fun checkPassword(password: String, storedHash: String): Boolean {
    return BCrypt.checkpw(password, storedHash)
}