package app.iesjdlc.tipslab.repository

import app.iesjdlc.tipslab.utils.FirebaseClient

class AuthRepository {
    private val auth = FirebaseClient.auth
    private val db = FirebaseClient.db
}