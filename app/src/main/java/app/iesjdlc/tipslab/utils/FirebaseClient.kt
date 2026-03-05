package app.iesjdlc.tipslab.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Se usa lazy para inicializar las instancias de FirebaseAuth y FirebaseFirestore solo cuando se necesiten por primera vez
object FirebaseClient {
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
}