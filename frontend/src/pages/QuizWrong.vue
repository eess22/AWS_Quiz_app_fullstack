<template>
  <div class="centered-container">
    <RouterLink to="/dashboard" class="btn btn-login">홈</RouterLink>
  </div>
  <div class="quiz-main">
    <div class="progress-section">
      <progress :value="current + 1" :max="questions.length"></progress>
      <p>문제번호: {{ current + 1 }} / {{ questions.length }}</p>
    </div>

    <section v-if="question" class="question-block">
      <p class="question-text">{{ question.question }}</p>

      <div class="choices">
        <button
          v-for="(text, key) in question.choices"
          :key="key"
          :class="buttonClass(key)"
          @click="toggleChoice(key)"
        >
          {{ key }}. {{ text }}
        </button>
      </div>

      <div v-if="showAnswer" class="answer-block">
        <p><strong>정답:</strong> {{ question.answers.join(', ') }}</p>
        <button @click="showExplanation = true">해설 보기</button>
      </div>

      <div v-if="showExplanation" class="explanation-block">
        <p v-for="(exp, i) in question.explanations" :key="i">• {{ exp }}</p>
      </div>

      <div class="nav-buttons">
        <button @click="prevQuestion" :disabled="current === 0">← 이전</button>
        <button @click="nextQuestion" :disabled="current + 1 === questions.length">다음 →</button>
      </div>
    </section>
  </div>
</template>

<script>
import axios from 'axios'
import { useRouter } from 'vue-router'
import allQuestions from '@/assets/quiz.JSON'

export default {
  data() {
    return {
      fullQuestions: allQuestions,
      questions: [],
      current: 0,
      selectedChoices: [],
      showAnswer: false,
      showExplanation: false,
    }
  },
  computed: {
    question() {
      return this.questions[this.current]
    }
  },
  async created() {
    this.router = useRouter()
    await this.fetchWrongQuestions()
  },
  methods: {
    async fetchWrongQuestions() {
      try {
        const res = await axios.get('http://localhost:8080/api/wrong', {
          withCredentials: true
        })

        const wrongIds = res.data
        this.questions = this.fullQuestions.filter(q => wrongIds.includes(q.id))

        if (this.questions.length === 0) {
          alert('오답 문제가 없습니다.')
          this.router.push('/dashboard')
        }
      } catch (err) {
        console.error('오답 데이터 불러오기 실패:', err)
      }
    },
    toggleChoice(choice) {
      if (this.showAnswer) return

      const correctCount = this.question.answers.length

      if (this.selectedChoices.includes(choice)) {
        this.selectedChoices = this.selectedChoices.filter(c => c !== choice)
      } else {
        if (this.selectedChoices.length >= correctCount) return
        this.selectedChoices.push(choice)
      }

      if (this.selectedChoices.length === correctCount) {
        this.submitAnswer()
      }
    },
    async submitAnswer() {
      this.showAnswer = true

      const selectedSet = new Set(this.selectedChoices.sort())
      const answerSet = new Set(this.question.answers.sort())
      const isCorrect =
        selectedSet.size === answerSet.size &&
        [...selectedSet].every(choice => answerSet.has(choice))

      if (isCorrect) {
        const token = localStorage.getItem('token')
        try {
          await axios.delete(`http://localhost:8080/api/wrong/${this.question.id}`, {
            headers: {
              Authorization: `Bearer ${token}`
            }
          })
          console.log("오답 DB에서 삭제 완료")
        } catch (err) {
          console.error("오답 삭제 실패:", err)
        }
      }
    },
    buttonClass(choice) {
      if (!this.showAnswer) {
        return this.selectedChoices.includes(choice) ? 'selected' : ''
      }
      const isCorrect = this.question.answers.includes(choice)
      const isSelected = this.selectedChoices.includes(choice)

      if (isCorrect) return 'correct'
      if (isSelected && !isCorrect) return 'incorrect'
      return ''
    },
    nextQuestion() {
      if (this.current + 1 < this.questions.length) {
        this.current++
        this.resetState()
      }
    },
    prevQuestion() {
      if (this.current > 0) {
        this.current--
        this.resetState()
      }
    },
    resetState() {
      this.selectedChoices = []
      this.showAnswer = false
      this.showExplanation = false
    }
  }
}
</script>

<style scoped>
.quiz-main {
  max-width: 700px;
  margin: auto;
  padding: 2rem;
  font-family: 'Segoe UI', sans-serif;
}

.progress-section {
  text-align: center;
  margin-bottom: 2rem;
}

.question-block {
  margin-top: 1rem;
}

.question-text {
  font-weight: bold;
  margin-bottom: 1rem;
}

.choices button {
  display: block;
  margin: 0.5rem 0;
  width: 100%;
  padding: 0.8rem;
  border-radius: 10px;
  border: 1px solid #ccc;
  background: #f9f9f9;
  text-align: left;
  transition: 0.2s ease-in-out;
  cursor: pointer;
}

button.selected {
  background-color: #e0f2fe;
  border: 2px solid #3b82f6;
}

button.correct {
  background-color: #d1fae5;
  border: 2px solid #10b981;
}

button.incorrect {
  background-color: #fee2e2;
  border: 2px solid #ef4444;
}

.nav-buttons {
  margin-top: 2rem;
  display: flex;
  justify-content: space-between;
}
</style>