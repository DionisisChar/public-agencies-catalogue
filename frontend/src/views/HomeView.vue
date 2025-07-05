<template>
  <div class="home-container">
    <h1>Καλώς ήρθατε στην Εφαρμογή Καταλόγου Οργανισμών και Δημοσίων Υπηρεσιών</h1>
    <h3 id="typing-text"></h3>
  </div>
</template>

<script setup>
// Δεν χρειαζόμαστε ακόμα JS
import { onMounted, ref } from 'vue';
onMounted(() => {
  const text = 'Η εφαρμογή βρίσκεται σε στάδιο ανάπτυξης'
  const element = document.getElementById('typing-text')
  let index = 0

  const typeNext = () => {
    if (index < text.length) {
      element.textContent += text.charAt(index)
      index++
      setTimeout(typeNext, 80) // ↩️ ταχύτητα ανά γράμμα
    }
  }

  typeNext()
})

</script>

<style scoped>
.home-container {
  padding: 40px;
  text-align: center;
}

h1 {
  color: #002855;
}

h3 {
  color: #333;
}

#typing-text {
  color: #333;
  margin-top: 24px; /* απόσταση από το h1 */
  font-size: 1.2em;
  letter-spacing: 0.5px;
  min-height: 1.5em; /* για να μη "πηδάει" το layout όταν ξεκινάει άδειο */
}

</style>