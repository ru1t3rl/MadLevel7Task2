package tech.ru1t3rl.madlevel7task2.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import tech.ru1t3rl.madlevel7task2.viewModel.QuizViewModel
import tech.ru1t3rl.madlevel7task2.R
import tech.ru1t3rl.madlevel7task2.databinding.FragmentQuestionsBinding
import tech.ru1t3rl.madlevel7task2.model.Quiz


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class QuestionsFragment : Fragment() {
    private lateinit var  binding: FragmentQuestionsBinding

    private val viewModel: QuizViewModel by activityViewModels()
    private var currentQuestion: Int = 0
    private var correctAnswer: String? = ""
    private var questions: ArrayList<Quiz> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionsBinding.inflate(layoutInflater)
        return inflater.inflate(R.layout.fragment_questions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeQuestions()

        binding.btnConfirm.setOnClickListener {
            when (binding.rgChoices.checkedRadioButtonId) {
                R.id.rbFirstChoice -> validate(binding.rbFirstChoice)
                R.id.rbSecondChoice -> validate(binding.rbSecondChoice)
                R.id.rbThirdChoice -> validate(binding.rbThirdChoice)
            }
        }
    }

    private fun validate(choice: RadioButton) {
        if (currentQuestion <= questions.size - 1) {
            if (choice.text == correctAnswer) {
                setQuestions()
                Toast.makeText(requireContext(), getString(R.string.correct_answer), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), getString(R.string.wrong_answer), Toast.LENGTH_SHORT).show()
            }
        } else {

            Toast.makeText(requireContext(), getString(R.string.quiz_completed), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.homeFragment)
        }
    }

    private fun observeQuestions() {
        viewModel.getQuestions()

        viewModel.questions.observe(viewLifecycleOwner, {
            questions.addAll(it)
            setQuestions()
        })
    }

    private fun setQuestions() {
        binding.tvCurrentQuestion.text =
            getString(R.string.tv_current_question, currentQuestion + 1, questions.size)

        binding.tvQuestion.text = questions[currentQuestion].question
        binding.rbFirstChoice.text = questions[currentQuestion].choices[0]
        binding.rbSecondChoice.text = questions[currentQuestion].choices[1]
        binding.rbThirdChoice.text = questions[currentQuestion].choices[2]
        correctAnswer = questions[currentQuestion].correctAnswer
        currentQuestion++

        Log.i("question", binding.tvQuestion.text.toString())
    }
}