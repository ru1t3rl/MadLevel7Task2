package tech.ru1t3rl.madlevel7task2.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import tech.ru1t3rl.madlevel7task2.model.Quiz

class QuizRepository {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var quizCollection =
        firestore.collection("quizquestions")

    private val _quizzes: MutableLiveData<ArrayList<Quiz>> = MutableLiveData()

    val quizzes: LiveData<ArrayList<Quiz>>
        get() = _quizzes

    // The CreateProfileFragment can use this to see if creation succeeded
    private val _createSuccess: MutableLiveData<Boolean> = MutableLiveData()

    val createSuccess: LiveData<Boolean>
        get() = _createSuccess

    suspend fun getQuiz() {
        try {
            withTimeout(5_000) {
                quizCollection
                    .orderBy("id")
                    .get().addOnSuccessListener {
                        val quizList = arrayListOf<Quiz>()
                        for (document in it) {
                            val id = document.getLong("id")
                            val question = document.getString("question")
                            val choices = document.get("choices") as List<String>
                            val correctAnswer = document.getString("correctAnswer")

                            val quizInstance = Quiz(id, question, choices, correctAnswer)
                            quizList.add(quizInstance)
                        }
                        _quizzes.value = quizList
                    }.await()
            }
        } catch (e: Exception) {
            throw QuizRetrievalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    class QuizSaveError(message: String, cause: Throwable) : Exception(message, cause)
    class QuizRetrievalError(message: String) : Exception(message)
}
