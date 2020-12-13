package tech.ru1t3rl.madlevel7task2.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import tech.ru1t3rl.madlevel7task2.model.Quiz
import tech.ru1t3rl.madlevel7task2.repository.QuizRepository
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "FIRESTORE"
    private val questionRepository: QuizRepository = QuizRepository()

    val questions: LiveData<ArrayList<Quiz>> = questionRepository.quizzes

    fun getQuestions() {
        viewModelScope.launch {
            try {
                questionRepository.getQuiz()
            } catch (ex: QuizRepository.QuizRetrievalError) {
                val errorMsg = "Something went wrong while retrieving the Quiz"
                Log.e(TAG, ex.message ?: errorMsg)
            }
        }
    }
}