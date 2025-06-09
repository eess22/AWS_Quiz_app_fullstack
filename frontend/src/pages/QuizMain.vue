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

    <section class="jump-section">
      <h3>문제 번호 이동</h3>
      <input
        type="number"
        v-model.number="jumpNumber"
        placeholder="번호 입력"
        min="1"
        :max="questions.length"
      />
      <button @click="jumpTo">이동</button>
    </section>
  </div>
</template>

<script>
import questions from '@/assets/quiz.JSON'

export default {
  data() {
    return {
      questions: questions,
      current: 0,
      selectedChoices: [],
      showAnswer: false,
      showExplanation: false,
      jumpNumber: null,
    }
  },
  computed: {
    question() {
      return this.questions[this.current]
    }
  },
  mounted() {
    // 로컬스토리지에서 저장된 마지막 문제 번호 불러오기
    const savedCurrent = localStorage.getItem('current')
    if (savedCurrent !== null && !isNaN(savedCurrent)) {
      const idx = parseInt(savedCurrent)
      if (idx >= 0 && idx < this.questions.length) {
        this.current = idx
      }
    }
  },
  methods: {
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
      const selectedSorted = [...this.selectedChoices].sort().join(',')
      const correctSorted = [...this.question.answers].sort().join(',')

      if (selectedSorted !== correctSorted) {
        try {
          await this.$axios.post(
            'http://localhost:8080/api/wrong',
            { questionId: this.question.id },
            { withCredentials: true }
          )
        } catch (error) {
          console.error('오답 저장 실패:', error)
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
        localStorage.setItem('current', this.current)
      }
    },
    prevQuestion() {
      if (this.current > 0) {
        this.current--
        this.resetState()
        localStorage.setItem('current', this.current)
      }
    },
    goTo(index) {
      this.current = index
      this.resetState()
      localStorage.setItem('current', this.current)
    },
    jumpTo() {
      if (this.jumpNumber >= 1 && this.jumpNumber <= this.questions.length) {
        this.goTo(this.jumpNumber - 1)
      } else {
        alert('올바른 문제 번호를 입력하세요.')
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

.jump-section {
  margin-top: 2rem;
  text-align: center;
}

.jump-section input {
  width: 100px;
  padding: 0.5rem;
  margin-right: 0.5rem;
}
</style>