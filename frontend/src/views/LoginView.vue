<template>
  <div class="login-wrapper" :class="{ 'login-error': loginFailed }">
    <div class="login-box">
      <h2>Είσοδος στην Εφαρμογή</h2>

      <!-- 📧 Email Field -->
      <label for="email">Διεύθυνση Email</label>
      <input
        type="email"
        id="email"
        v-model="email"
        @blur="validateEmail"
        :class="{ 'input-error': emailError }"
        placeholder="π.χ. user@example.com"
      />
      <span v-if="emailError" class="error-text">Μη έγκυρη διεύθυνση email</span>

      <!-- 🔒 Password Field -->
      <label for="password">Κωδικός Πρόσβασης</label>
      <input
        type="password"
        id="password"
        v-model="password"
        placeholder="********"
        :class="{ 'input-error': loginFailed }"
      />

      <!-- ❌ Login Error -->
      <p v-if="loginFailed" class="error-text">
        Καθορίστηκε λανθασμένο όνομα χρήστη ή κωδικός πρόσβασης.
      </p>

      <!-- ✅ Login Button -->
      <button @click="handleLogin" :disabled="!formValid">ΣΥΝΔΕΣΗ</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import { loginUser } from "../services/auth/AuthService";
import { useRouter } from "vue-router";
import { authState } from "../services/state/authState";


const router = useRouter();
const email = ref("");
const password = ref("");
const emailError = ref(false);
const loginFailed = ref(false);

const validateEmail = () => {
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  emailError.value = !emailPattern.test(email.value);
};

const formValid = computed(() => {
  return email.value && password.value && !emailError.value;
});

const handleLogin = async () => {
  validateEmail();

  if (!formValid.value) return;

  loginFailed.value = false;

  // ✨ Tο axios call στο backend
  try{
    const user = await loginUser({
      email: email.value,
      password: password.value,
    });

    authState.user = user;
    console.log("✅ Συνδέθηκε ο χρήστης:", user)

    router.push("/");
  } catch (error) {
    console.error("❌ Αποτυχία σύνδεσης:", error)
    loginFailed.value = true;
  }
  
};
</script>

<style scoped>
.login-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  height: calc(100vh - 150px);
  background-color: #f0f2f5;
}

.login-box {
  background-color: rgb(252, 252, 252);
  padding: 2rem 2.5rem;
  border-radius: 12px;
  box-shadow: 0 0 12px rgba(0, 0, 0, 0.1);
  width: 100%;
  height: 100%;
  max-width: 800px;
  max-height: 400px;
  display: flex;
  flex-direction: column;
}

h2 {
  text-align: center;
  margin-bottom: 1rem;
  color: #207ad4;
  font-size: 1.8rem;           /* μεγαλύτερο μέγεθος γραμματοσειράς */
  font-weight: 600;            /* πιο έντονο */
}
label {
  font-weight: bold;
  font-size: 1.1rem;
  margin-top: 1rem;
  margin-bottom: 0.4rem;
  color: #333;
}

input {
  padding: 0.6rem;
  border-radius: 6px;
  border: 1px solid #ccc;
  font-size: 1rem;
}

.input-error {
  border: 1px solid red;
  background-color: #fff4f4;
}

.error-text {
  color: red;
  font-size: 0.85rem;
  margin-top: 0.25rem;
}

button {
  margin-top: 2.5rem;
  padding: 0.75rem;
  background-color: #001b3b;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 1.2rem;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.25s;
}

button:disabled {
  background-color: #bbb;
  cursor: not-allowed;
}

button:hover:enabled {
  background-color: #207ad4;
}

.login-error .login-box {
  border: 1px solid red;
}
</style>